package netflix.simulation.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

public class Season implements Serializable {
    private int numberOfSeason;
    private ArrayList<Episode> season = new ArrayList<>();

    public Season() {
        this.numberOfSeason = -1;
    }

    public Season(int numberOfSeason, int price, int duration) {
        this.numberOfSeason = numberOfSeason;
        this.fillWithBlankEpisodes(new Random().nextInt(12) + 8, price, duration);
    }

    private void fillWithBlankEpisodes(int numberOfEpisodes, int price, int duration) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(("dd.mm.yyyy"));
        for (int i = 0; i < numberOfEpisodes; i++) {
            LocalDateTime now = LocalDateTime.now();
            season.add(new Episode(duration, "Episode" + (i + 1), price, i + 1, dtf.format(now)));
        }
    }

    @Override
    public String toString() {
        return "Season{" + "numberOfSeason=" + numberOfSeason + +'}';
    }
    //Might be useful
    /*
        public void showSeasonContent() throws NullPointerException {
        if (numberOfSeason == -1)
            System.out.println("Trying to print empty season with index of " + numberOfSeason + ", nothing to show...");
        else {
            System.out.println("\nPrinting season " + numberOfSeason + " contents...");
            try {
                if (season.isEmpty())
                    throw new NullPointerException("NullPointerException occurred, season " + numberOfSeason + " is
                    empty...");
                for (Episode episode : season) {
                    System.out.println(episode);
                }
            } catch (NullPointerException e) {
                System.out.println("Exception: " + e.getMessage());
            }
        }
    }
        public void setNumberOfSeason(int numberOfSeason) {
        this.numberOfSeason = numberOfSeason;
    }

    public void setEpisodesPrice(int price) {
        for (Episode episode : season) {
            episode.setPrice(price);
        }
    }

    public void addEpisode(int duration, String title, int price, int episodeNumber, String premiereDate) {
        season.add(new Episode(duration, title, price, episodeNumber, premiereDate));
    }
        public Episode getEpisode(int numberOfEpisode) {
        return season.get(numberOfEpisode - 1);
    }

     */
}
