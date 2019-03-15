package netflix.simulation.entities;

import lombok.Getter;
import netflix.simulation.Simulation;
import netflix.simulation.entities.managers.ProductManager;
import netflix.simulation.entities.parser.ReadJSONFile;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

@Getter
public class Distributor extends Person implements Runnable {
    private String name;
    private int watchCount = 0;
    private BigDecimal bankAccount = BigDecimal.ZERO;
    private int watchPrice;

    public Distributor(int count) {
        this.name = ReadJSONFile.parseFile("distributor.json").get("distributor").toString();
        for (int i = 0; i < count; i++) {
            this.addRandomProduct();
        }
        // Crucial condition, it must not start unless the simulation is running
        if (Simulation.getInstance().isRunning()) startThread();
    }

    private void promotion() {
        double promotion = 0.05 + Math.random() * (0.5 - 0.1);
        System.out.println("New promotion! " + ((Double) (promotion * 100)).intValue() + "% off!");
        ProductManager.getInstance().getProductList().
                stream().
                filter(product -> product.getDistributor().equals(this.name)).
                forEach(video -> video.calculatePromotion(promotion));
    }

    /**
     * Contains couple of empty loops, catching all scenarios
     */
    @Override
    public void run() {
        System.out.println("Starting distributor thread: " + this.name + "...");
        //Falls here if simulation is not running, but executor is viable and is ready to accept tasks
        while (!Simulation.getInstance().isRunning() && !Simulation.getInstance()
                .getDistributorExecutorService()
                .isShutdown()) {
        }
        while (Simulation.getInstance().isRunning() && this.isAllowed()) {
            try {
                if (new Random().nextInt(10) < 4) {
                    this.addRandomProduct();
                    if (new Random().nextInt(100) < 30) promotion();
                    watchPrice += 1;
                    System.out.println("New product added: " + ProductManager.getInstance().
                            getProductByIndex(ProductManager.getInstance().getProductList().size() - 1));
                    sleep(4000 / Simulation.getInstance().getSpeed().longValue());
                } else {
                    if (watchPrice > 1) watchPrice -= 1;
                    sleep(4000 / Simulation.getInstance().getSpeed().longValue());
                }
            } catch (InterruptedException e) {
                System.out.println("Sleep interrupted");
            }
        }
        while (!Simulation.getInstance().isRunning() && !Simulation.getInstance()
                .getDistributorExecutorService()
                .isShutdown() || !this.isAllowed()) {
        }
        System.out.println("Ending distributor thread: " + this.name + "...");
    }

    /**
     * Adds a random product depending on random number, 50% chance
     */
    private void addRandomProduct() {
        if (new Random().nextInt(2) < 1) {
            ProductManager.getInstance().addProduct(this.newSeries(this.name));
        } else {
            ProductManager.getInstance().addProduct(this.newMovie(this.name));
        }
    }

    public void incrementWatchCount() {
        watchCount += 1;
    }

    public void addMoney(double money) {
        bankAccount = bankAccount.add(BigDecimal.valueOf(money));
    }

    public int getWatchCount() {
        return watchCount;
    }

    public void setWatchCount(int val) {
        watchCount = val;
    }

    public void startThread() {
        Simulation.getInstance().getDistributorExecutorService().submit(this);
    }


    /**
     * Needs to save current list state to local variable to prevent concurrency errors, multiple threads accessing
     * same data set
     *
     * @return random product, may not be always up to date
     */
    public Video getRandomProduct() {
        List<Video> list = new ArrayList<>(ProductManager.getInstance().getProductList());
        List<Video> collect = list.stream().
                filter(video -> video.getDistributor().equals(this.name)).
                collect(Collectors.toList());

        return collect.get(new Random().nextInt(collect.size()));
    }

    public String getName() {
        return name;
    }


    private String[] toStringArray(JSONArray array) {
        if (array == null) return null;

        String[] arr = new String[array.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (String) array.get(i);
        }
        return arr;
    }

    private Video newSeries(String distributor) {
        while (true) {
            JSONObject newSeries = ReadJSONFile.parseFile("movies.json");
            if (newSeries != null) {
                int length;
                String[] countries = toStringArray((JSONArray) newSeries.get("countries"));
                if (countries == null) {
                    countries = new String[]{"Poland"};
                }
                String[] genres = toStringArray((JSONArray) newSeries.get("genres"));
                if (genres == null) {
                    genres = new String[]{"Comedy"};
                }
                try {
                    length = Integer.parseInt(newSeries.get("length").toString());
                } catch (NullPointerException e) {
                    length = 100;
                }
                try {
                    return new Series(IDGenerator.nextProduct(), //Product ID
                            newSeries.get("image").toString(), //ImagePath
                            newSeries.get("title").toString(), //Product Title
                            newSeries.get("date").toString(), //Date of Production
                            genres[0],//Genre
                            newSeries.get("description").toString(), length, countries[0], newSeries.get("rating")
                            .toString(), toStringArray((JSONArray) newSeries.get("actors")),
                            new Random().nextInt(10) + 5, distributor);
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
            }
        }
    }

    private Video newMovie(String distributor) {
        while (true) {
            JSONObject newMovie = ReadJSONFile.parseFile("movies.json");
            if (newMovie != null) {
                int length;
                String[] countries = toStringArray((JSONArray) newMovie.get("countries"));
                if (countries == null) {
                    countries = new String[]{"Poland"};
                }
                String[] genres = toStringArray((JSONArray) newMovie.get("genres"));
                if (genres == null) {
                    genres = new String[]{"Comedy"};
                }
                try {
                    length = Integer.parseInt(newMovie.get("length").toString());
                } catch (NullPointerException e) {
                    length = 100;
                }
                try {
                    return new Movie(IDGenerator.nextProduct(), //Product ID
                            newMovie.get("image").toString(), //ImagePath
                            newMovie.get("title").toString(), //Product Title
                            newMovie.get("date").toString(), //Date of Production
                            genres[0],//Genre
                            newMovie.get("description").toString(), length, countries[0], newMovie.get("rating")
                            .toString(), toStringArray((JSONArray) newMovie.get("actors")),
                            (new Random().nextInt(10) + 5), "www.youtube.com/results?search_query=" + newMovie
                            .get("title")
                            .toString(), String.valueOf(new Random().nextInt(10)), distributor);
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
                ;
            }
        }
    }

}
