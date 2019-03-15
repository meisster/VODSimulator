package netflix.simulation.entities;

import lombok.Data;
import netflix.simulation.Simulation;
import netflix.simulation.entities.managers.DistributorManager;
import netflix.simulation.entities.managers.NetflixBudgetManager;
import netflix.simulation.entities.parser.ReadJSONFile;
import netflix.simulation.subscription.Subscription;
import netflix.simulation.subscription.SubscriptionHolder;
import netflix.simulation.subscription.SubscriptionType;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Thread.sleep;

@Data
public class User extends Person implements Runnable {

    private String birthDay;
    private String email;
    private String creditCardNumber;
    private Subscription subscription;
    private List<WatchedProduct> watchedProducts = new ArrayList<>();

    public User() {
        JSONObject jsonObject = ReadJSONFile.parseFile("user.json");
        this.birthDay = jsonObject.get("birthDay").toString();
        this.email = jsonObject.get("email").toString();
        this.creditCardNumber = jsonObject.get("creditCard").toString();
        this.subscription = SubscriptionHolder.getInstance().getRandomSubscription();
        this.setID(IDGenerator.nextUser());
        startThread();
    }

    /**
     * Simple filter to check for given conditions, used for TableView search
     *
     * @param value value to check for presence in object
     * @return true if found anywhere
     */
    public boolean contains(String value) {
        return email.contains(value) || subscription.getType().name().contains(value) || watchedProducts.stream()
                .anyMatch(watchedProduct -> watchedProduct.getProduct().getID().contains(value));
    }

    /**
     * Submits runnable task to an executor
     */
    public void startThread() {
        Simulation.getInstance().getUserExecutorService().submit(this);
    }

    /**
     * Gets randomDistributor from singleton DistributorManager,
     * then gets randomProduct,
     * then increments randomDistributor watchCount, which is used to compute earns,
     * then gives him money,
     * then adds money to NetflixBudget
     */

    /**
     * Pays subscription fee
     */
    public void payToll() {
        NetflixBudgetManager.addToBudget(this.subscription.getPrice().intValue());
    }

    /**
     * Gets random distributor,
     * then gets a random product from him,
     * adds a watch to distributor, adds a watch to the product, adds prodcut to local "watchList",
     * pays the price to Netflix, enjoys
     */
    private void makeNewTransaction() {
        Distributor randomDistributor = DistributorManager.getInstance().getRandomDistributor();
        Video randomProduct = randomDistributor.getRandomProduct();
        randomDistributor.incrementWatchCount();
        watchedProducts.add(new WatchedProduct(randomProduct, new Random().nextInt(5) + 5, Simulation.getInstance()
                .getCurrentTime()));
        NetflixBudgetManager.addToBudget(randomProduct.getPrice().doubleValue());
        System.out.println("User " + this.getID() + " successfully bought " + randomProduct.getTitle() + " from " + randomDistributor
                .getName() + " for " + randomProduct.getPrice().intValue() + "$");

    }

    /**
     * Gets a random product from a random distributor, watches product and increments watches
     */
    private void watchSomething() {
        Distributor randomDistributor = DistributorManager.getInstance().getRandomDistributor();
        Video randomProduct = randomDistributor.getRandomProduct();
        randomDistributor.incrementWatchCount();
        watchedProducts.add(new WatchedProduct(randomProduct, new Random().nextInt(5) + 5, Simulation.getInstance()
                .getCurrentTime()));
    }

    @Override
    public String toString() {
        return this.getID();
    }

    @Override
    public void run() {
        System.out.println("Starting user thread: " + this.getID() + "...");
        while (!Simulation.getInstance().isRunning() && !Simulation.getInstance()
                .getUserExecutorService()
                .isShutdown()) {
        }
        while (Simulation.getInstance().isRunning() && this.isAllowed()) {
            watchedProducts.stream()
                    .filter(WatchedProduct::isTimeOut)
                    .forEach(watchedProduct -> watchedProducts.remove(watchedProduct));
            if (new Random().nextInt(10) < 5) {
                if (this.subscription.getType() == SubscriptionType.NONE) this.makeNewTransaction();
                else this.watchSomething();
                try {
                    sleep(4000 / Simulation.getInstance().getSpeed().longValue());
                } catch (InterruptedException e) {
                    System.out.println("Sleep interrupted");
                }
                //System.out.println("Transaction successful, added 25$.");
            } else //System.out.println("Transaction failed...");
                try {
                    sleep(4000 / Simulation.getInstance().getSpeed().longValue());
                } catch (InterruptedException e) {
                    System.out.println("Sleep interrupted");
                }
        }
        while (!Simulation.getInstance().isRunning() && !Simulation.getInstance()
                .getUserExecutorService()
                .isShutdown() || !this.isAllowed()) {
        }
        System.out.println("Ending user thread: " + this.getID() + "...");
    }
}
