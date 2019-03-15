package netflix.simulation.entities;

import lombok.Data;

@Data
public class Movie extends Video {
    private String[] cast;
    private String trailerLink;
    private String timeAvailable;

    public Movie(String ID, String imagePath, String title, String dateOfProduction, String genre, String description
            , int duration, String countryOfOrigin, String rating, String[] cast, int price, String trailerLink,
            String timeAvailable, String distributor) {
        super(ID, imagePath, title, dateOfProduction, genre, description, duration, countryOfOrigin, rating,
                distributor, price);
        this.cast = cast;
        this.trailerLink = trailerLink;
        this.timeAvailable = timeAvailable;
    }

    public String getTrailerLink() {
        return trailerLink;
    }

    @Override
    public String toString() {
        /*return "Movie{" +
                "title=" + this.getTitle() +
                ", rating=" + this.getRating() +
                ", genre=" + this.getGenre() +
                ", timeAvailable=" + this.getTimeAvailable() + " days" +
                ", cast=" + Arrays.toString(cast) +
                ", description=" + this.getDescription() +
                ", countryOfOrigin=" + this.getCountryOfOrigin() +
                ", trailerLink=" + this.trailerLink +
                ", distributor=" + this.getDistributor() +
                ", price=" + this.getPrice() + "$" +
                '}';*/
        return this.getID();
    }

    public String getTimeAvailable() {
        return timeAvailable;
    }

}
