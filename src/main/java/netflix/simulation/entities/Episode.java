package netflix.simulation.entities;

import lombok.Data;

import java.io.Serializable;

@Data
public class Episode implements Serializable {

    private int duration;
    private String title;
    private int price;
    private int episodeNumber;
    private String premiereDate;

    public Episode() {
        this.duration = -1;
        this.title = "undefined";
        this.price = -1;
        this.episodeNumber = -1;
        this.premiereDate = "dd.mm.yyyy";
    }

    public Episode(int duration, String title, int price, int episodeNumber, String premiereDate) {
        this.duration = duration;
        this.title = title;
        this.price = price;
        this.episodeNumber = episodeNumber;
        this.premiereDate = premiereDate;
    }

    @Override
    public String toString() {
        return "Episode{" + "duration=" + duration + ", title='" + title + '\'' + ", price=" + price + ", " +
                "episodeNumber=" + episodeNumber + ", premiereDate=" + premiereDate + '}';
    }

    public int getDuration() {
        return duration;
    }

}
