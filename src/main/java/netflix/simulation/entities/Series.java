package netflix.simulation.entities;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
public class Series extends Video {
    private String[] cast;
    private List<Season> listOfSeasons = new ArrayList<>();


    public Series(String ID, String imagePath, String title, String dateOfProduction, String genre,
            String description, int duration, String countryOfOrigin, String rating, String[] cast, int price,
            String distributor) {
        super(ID, imagePath, title, dateOfProduction, genre, description, duration, countryOfOrigin, rating,
                distributor, price);
        this.cast = cast;
        //On creation fills with random seasons
        this.fillWithBlankSeasons(new Random().nextInt(6) + 1, price, duration);
    }

    /**
     * Fills a series with a random seasons
     *
     * @param numberOfSeaons the number of seasons to fill
     * @param price          the price of each episode
     * @param duration       the duration of episodes
     */
    public void fillWithBlankSeasons(int numberOfSeaons, int price, int duration) {
        for (int i = 0; i < numberOfSeaons; i++) {
            listOfSeasons.add(new Season(i + 1, price, duration));
        }
    }

    @Override
    public String toString() {
        /*return "Series{" +
                "title=" + this.getTitle() +
                ", rating=" + this.getRating() +
                ", genre=" + this.getGenre() +
                ", numberOfSeasons=" + this.getNumberOfSeasons() +
                ", cast=" + Arrays.toString(cast) +
                ", description=" + this.getDescription() +
                ", countryOfOrigin=" + this.getCountryOfOrigin() +
                ", distributor=" + this.getDistributor() +
                ", price=" + getPrice() + "$" +
                '}';*/
        return this.getID();
    }

}
