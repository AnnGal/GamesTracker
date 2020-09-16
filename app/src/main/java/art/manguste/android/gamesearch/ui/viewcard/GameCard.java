package art.manguste.android.gamesearch.ui.viewcard;

import java.util.ArrayList;
import java.util.Date;

public class GameCard {
    private int cover;  // img id
    private String description;
    private String title;
    private String developer; //enum?
    private String publisher; // month YYYY
    private Date release;
    private ArrayList<String> platforms = new ArrayList<>();


    public GameCard(int cover, String title, String description, String developer, String publisher, Date release/*, ArrayList<String> platforms*/) {
        //TODO: if not specified, then set 'Unknown'
        this.cover = cover;
        this.title = title;
        this.description = description;
        this.developer = developer;
        this.publisher = publisher;
        //TODO: set date format "month YYYY"
        this.release = release;
        this.platforms = platforms;
    }

    public GameCard(String title, String description) {
        this.description = description;
        this.title = title;
    }


    public int getCover() {
        return cover;
    }

    public String getTitle() {
        return title;
    }

    public String getDeveloper() {
        return developer;
    }

    public String getPublisher() {
        return publisher;
    }

    public Date getRelease() {
        return release;
    }

    public ArrayList<String> getPlatforms() {
        return platforms;
    }

    public String getDescription() {
        return description;
    }
}
