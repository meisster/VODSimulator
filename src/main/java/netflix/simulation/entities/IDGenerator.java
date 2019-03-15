package netflix.simulation.entities;

public class IDGenerator {
    private static volatile int userID = 1000000;
    private static volatile int distributorID = 1000;
    private static volatile int productID = 10000;

    public static synchronized String nextUser() {
        return "U" + (userID++);
    }

    public static synchronized String nextDistributor() {
        return "D" + (distributorID++);
    }

    public static synchronized String nextProduct() {
        return "P" + (productID++);
    }
}
