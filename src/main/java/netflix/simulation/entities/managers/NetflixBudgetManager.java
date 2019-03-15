package netflix.simulation.entities.managers;

import netflix.simulation.entities.Video;

import java.io.Serializable;
import java.math.BigDecimal;

public class NetflixBudgetManager implements Serializable {
    private static BigDecimal budget = BigDecimal.ZERO;

    public NetflixBudgetManager(double budget) {
        NetflixBudgetManager.budget = NetflixBudgetManager.budget.add(BigDecimal.valueOf(budget));
    }

    public static synchronized void addToBudget(double userMoney) {
        NetflixBudgetManager.budget = NetflixBudgetManager.budget.add(BigDecimal.valueOf(userMoney));
    }

    private static void takeFromBudget(int money) {
        NetflixBudgetManager.budget = NetflixBudgetManager.budget.subtract(BigDecimal.valueOf(money));
    }

    public synchronized static BigDecimal getBudget() {
        return budget;
    }

    public static void setBudget(int value) {
        budget = BigDecimal.ZERO;
    }

    public static void payFeeToDistributors() {
        DistributorManager.getInstance().getDistributors().parallelStream().
                forEach(distributor -> {
                    int money = distributor.getWatchCount() * distributor.getWatchPrice();
                    NetflixBudgetManager.takeFromBudget(money);
                    distributor.addMoney(money);
                    distributor.setWatchCount(0);
                });
    }

    public static void setWatchData() {
        ProductManager.getInstance().getProductList().parallelStream().forEach(Video::writeData);
    }
}
