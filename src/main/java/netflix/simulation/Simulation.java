package netflix.simulation;

import lombok.Data;
import netflix.simulation.entities.User;
import netflix.simulation.entities.managers.DistributorManager;
import netflix.simulation.entities.managers.NetflixBudgetManager;
import netflix.simulation.entities.managers.ProductManager;
import netflix.simulation.entities.managers.UserManager;

import java.io.*;
import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Data
public class Simulation implements Serializable {
    private transient static Simulation INSTANCE;
    private BigDecimal speed = BigDecimal.ONE;
    private double currentMonth = 0;
    private volatile double currentTime;
    //public double progress;
    private transient ExecutorService userExecutorService = Executors.newCachedThreadPool();
    private transient ExecutorService distributorExecutorService = Executors.newCachedThreadPool();
    private transient ScheduledExecutorService timeTrackerExecutorService = Executors.newScheduledThreadPool(1);
    private BigDecimal tempBudget = BigDecimal.ZERO;
    private volatile boolean isRunning;
    private boolean isInitialized = false;
    private int usersCount;
    private boolean paused = false;
    private int lossCount;


    public static synchronized Simulation getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Simulation();
        }
        return INSTANCE;
    }


    public boolean isRunning() {
        return isRunning;
    }

    public synchronized double getTime() {
        return currentTime;
    }

    private void startCounting() {
        Runnable countTime = () -> setCurrentTime(getCurrentTime() + 1);
        timeTrackerExecutorService.scheduleAtFixedRate(countTime, 0, 1000, TimeUnit.MILLISECONDS);
    }

    public void initializeSimulation(BigDecimal speed) {
        System.out.println("Initializing simulation...");
        Simulation.getInstance().setSpeed(speed);
        userExecutorService = Executors.newCachedThreadPool();
        distributorExecutorService = Executors.newCachedThreadPool();
        timeTrackerExecutorService = Executors.newScheduledThreadPool(1);
        UserManager.getInstance().killAll();
        DistributorManager.getInstance().killAll();
        ProductManager.getInstance().killAll();
        Simulation.getInstance().setCurrentTime(0);
        Simulation.getInstance().setCurrentMonth(0);
        //initializing base user list if empty, e.g. 10 users
        System.out.println("Creating user base...");
        generateUsers(usersCount);
        System.out.println("User base generated");
        if (!isInitialized) {
            Runnable task = () -> {
                while (!Simulation.getInstance()
                        .isRunning() && !Simulation.getInstance().userExecutorService.isShutdown()) {
                    try {
                        System.out.println("Simulation ready, waiting for signal...");
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            System.out.println("Simulation ready for take-off, proceed anytime...");
            userExecutorService.submit(task);
        }
        isInitialized = true;
        paused = false;
    }

    /**
     * @param usersCount how many users to generate
     * @apiNote for each int nbr it adds a new User to userList
     */
    private void generateUsers(int usersCount) {
        IntStream.range(0, usersCount).forEach(nbr -> UserManager.getInstance().addToList(new User()));
    }

    public void save() {
        System.out.println("Saving simulation state to file...");
        serialize("simulation.bin");
        UserManager.getInstance().serialize("users.bin");
        ProductManager.getInstance().serialize("products.bin");
        DistributorManager.getInstance().serialize("distributors.bin");
        System.out.println("Simulation state saved");
    }

    public void load() {

        System.out.println("Loading simulation state from files...");
        deserialize("simulation.bin");
        UserManager.getInstance().deserialize("users.bin");
        DistributorManager.getInstance().deserialize("distributors.bin");
        ProductManager.getInstance().deserialize("products.bin");
        userExecutorService = Executors.newCachedThreadPool();
        distributorExecutorService = Executors.newCachedThreadPool();
        timeTrackerExecutorService = Executors.newScheduledThreadPool(1);
        System.out.println("Simulation state loaded");
        UserManager.getInstance().startAll();
        DistributorManager.getInstance().startAll();
    }

    public void startSimulation() {
        //If paused, resume local instances
        if (paused) {
            UserManager.getInstance().deserialize("runUsers.bin");
            ProductManager.getInstance().deserialize("runProducts.bin");
            DistributorManager.getInstance().deserialize("runDistributors.bin");
            UserManager.getInstance().startAll();
            //deserialize("runSimulation.bin");
        }
        DistributorManager.getInstance().startAll();
        System.out.println("Enabling simulation...");
        Simulation.getInstance().isRunning = true;
        System.out.println("Starting threads...");
        System.out.println("Threads started...");
        startCounting();
        Runnable simulationTask = () -> {
            while (Simulation.getInstance().isRunning()) {
                if ((int) currentMonth < (int) (currentTime / 30)) {
                    currentMonth = currentTime / 30;
                    UserManager.getInstance().collectMoneyFromSubscription();
                    NetflixBudgetManager.payFeeToDistributors();
                    NetflixBudgetManager.setWatchData();

                    if (NetflixBudgetManager.getBudget().intValue() < 0) {
                        System.out.println("===========================================================");
                        System.out.println("                         WARNING                           ");
                        System.out.println("===========================================================");
                        System.out.println("              SIMULATION IS GENERATING LOSSES!             ");
                        lossCount += 1;
                    } else lossCount = 0;
                    System.out.println("NEW MONTH - " + ((int) currentMonth));
                    if (lossCount >= 3) {
                        System.out.println("Three months elapsed with no earn, simulation ending...");
                        System.out.println("Money lost: " + NetflixBudgetManager.getBudget().intValue());
                        Simulation.getInstance().endSimulation();
                    }
                    if ((int) currentMonth > 12) {
                        System.out.println("One year elapsed, ending simulation automatically...");
                        Simulation.getInstance().endSimulation();
                    }
                }
                if (new Random().nextInt(2) == 0) {
                    UserManager.getInstance().addToList(new User());
                    System.out.println("New user!");
                    try {
                        Thread.sleep(4000 / Simulation.getInstance().getSpeed().longValue());
                    } catch (InterruptedException e) {
                        System.out.println("Thread interrupted");
                    }
                }
            }
        };
        userExecutorService.submit(simulationTask);
    }

    public void endSimulation() {
        paused = true;
        System.out.println("Stopping simulation...");
        Simulation.getInstance().isRunning = false;
        userExecutorService.shutdown();
        distributorExecutorService.shutdown();
        timeTrackerExecutorService.shutdown();
        try {
            System.out.println("Shutdown in progress...");
            userExecutorService.awaitTermination(3, TimeUnit.SECONDS);
            distributorExecutorService.awaitTermination(3, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (!userExecutorService.isTerminated()) {
                System.out.println("Shutdown timeout, shutting down immediately...");
                userExecutorService.shutdownNow();
                distributorExecutorService.shutdownNow();
                System.out.println("Shutdown complete");
            } else {
                System.out.println("Shutdown complete");
            }
            UserManager.getInstance().serialize("runUsers.bin");
            ProductManager.getInstance().serialize("runProducts.bin");
            DistributorManager.getInstance().serialize("runDistributors.bin");
            //serialize("runSimulation.bin");
            userExecutorService = Executors.newCachedThreadPool();
            distributorExecutorService = Executors.newCachedThreadPool();
            timeTrackerExecutorService = Executors.newScheduledThreadPool(1);
        }
        //AFTER LASTING 3 MONTHS
        if (lossCount >= 3) {
            //Kills all threads and nullifies collections
            UserManager.getInstance().killAll();
            DistributorManager.getInstance().killAll();
            ProductManager.getInstance().killAll();
            Simulation.getInstance().currentMonth = 0;
            userExecutorService = Executors.newCachedThreadPool();
            distributorExecutorService = Executors.newCachedThreadPool();
            timeTrackerExecutorService = Executors.newScheduledThreadPool(1);
            currentTime = 0;
        } else if (currentMonth > 12) {
            System.out.println("Money made after one year: " + NetflixBudgetManager.getBudget().intValue());
        }

        isInitialized = false;
    }

    private void serialize(String path) {
        try {
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(path));
            Simulation.getInstance().setTempBudget(NetflixBudgetManager.getBudget());
            output.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deserialize(String path) {
        ObjectInputStream input = null;
        try {
            input = new ObjectInputStream(new FileInputStream(path));
            Simulation simulation = (Simulation) input.readObject();
            Simulation.getInstance().setCurrentMonth(simulation.getCurrentMonth());
            Simulation.getInstance().setCurrentTime(simulation.getCurrentTime());
            Simulation.getInstance().setInitialized(simulation.isInitialized());
            Simulation.getInstance().setSpeed(simulation.getSpeed());
            NetflixBudgetManager.setBudget(simulation.getTempBudget().intValue());
            Simulation.getInstance().setTempBudget(BigDecimal.ZERO);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        paused = false;
    }
}
