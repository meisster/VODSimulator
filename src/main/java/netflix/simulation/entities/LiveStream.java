package netflix.simulation.entities;

import lombok.Data;

@Data
public class LiveStream extends Video {
    private String releaseDate;
    private String title;
    private String genre;
    private String rating;
    private String country;

    public LiveStream(int price, String releaseDate, String title, String genre, String rating, String country) {
        this.releaseDate = releaseDate;
        this.title = title;
        this.genre = genre;
        this.rating = rating;
        this.country = country;
    }
}
