package netflix.simulation.entities;

import lombok.Data;
import netflix.simulation.Simulation;
import netflix.simulation.entities.managers.ProductManager;

import java.io.Serializable;

@Data
public class WatchedProduct implements Serializable {
    private Video product;
    private double startTime;
    private int watchedTime;

    public WatchedProduct(Video product, int watchedTime, double startTime) {
        this.startTime = startTime;
        this.product = product;
        this.watchedTime = watchedTime;
        ProductManager.getInstance().getProductByTitle(product.getTitle()).addWatch();
    }

    public boolean isTimeOut() {
        if (Simulation.getInstance().getCurrentTime() - this.startTime > watchedTime) {
            ProductManager.getInstance().getProductByTitle(product.getTitle()).subWatch();
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return product.getTitle();
    }
}
