package netflix.simulation.entities;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
public abstract class Video extends RecursiveTreeObject<Video> implements Serializable {

    private String imagePath;
    private String title;
    private String genre;
    private String description;
    private int duration;
    private String distributor;
    private String countryOfOrigin;
    private String dateOfProduction;
    private String rating;
    private String ID;
    private BigDecimal originalPrice;
    private BigDecimal price;
    private int watchCount = 0;
    private List<Integer> watchData = new ArrayList<>();

    public Video() { }

    public Video(String ID, String imagePath, String title, String dateOfProduction, String genre, String description
            , int duration, String countryOfOrigin, String rating, String distributor, int price) {
        this.imagePath = imagePath;
        this.title = title;
        this.genre = genre;
        this.dateOfProduction = dateOfProduction;
        this.description = description;
        this.duration = duration;
        this.distributor = distributor;
        this.countryOfOrigin = countryOfOrigin;
        this.rating = rating;
        this.ID = ID;
        this.price = new BigDecimal(price);
        this.originalPrice = new BigDecimal(price);
    }

    public void calculatePromotion(double promotionValue) {
        price = originalPrice;
        if (new Random().nextInt(10) < 5) {
            price = BigDecimal.valueOf(price.intValue() * promotionValue);
        }
    }

    public final synchronized void addWatch() {
        this.watchCount += 1;
    }

    public final void subWatch() {
        this.watchCount -= 1;
    }

    public boolean contains(String value) {
        return title.contains(value) || genre.contains(value);
    }

    public void writeData() {
        watchData.add(watchCount);
    }

}