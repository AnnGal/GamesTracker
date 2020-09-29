package art.manguste.android.gamesearch.core;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.VisibleForTesting;
import androidx.room.Ignore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Locale;

public class Game {

    private static final String DELIMITER = ", ";
    private static final String DATE_PATTERN = "dd.MM.yyyy";
    private static final String API_SITE = "https://rawg.io/games/";
    public static Date NULL_DATE = null; // date for replace null value

    static {
        String dateStr = "01-01-1900";
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        try {
            NULL_DATE = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private Integer id;
    private String alias;
    private String name;
    private String description;
    private Date releaseDate;
    private String imgHttp;
    private String rating;
    private String metacritic;
    private String website;
    private String[] genresList;
    private String[] platformsList;
    private String[] developersList;
    private String[] publishersList;
    private String jsonString;
    private Boolean isFavorite = false;

    /**
     * Constructor for short game info
     * @param alias - site code name - used to find game via https request
     * @param name - name of the game
     * @param releaseDate - when game will or was released
     * @param imgHttp - games cover image
     * @param rating - site rating
     * @param metacritic - metacritic rating
     * @param genresList - list of genres
     * @param platformsList  - what platforms can run
     * @param isFavorite  - is this game noted as a favorite by user
     */
    public Game(String alias, String name, Date releaseDate, String imgHttp, String rating, String metacritic,
                String[] genresList, String[] platformsList, Boolean isFavorite) {
        this.alias = alias;
        this.name = name;
        this.releaseDate = releaseDate;
        this.imgHttp = imgHttp;
        this.rating = rating;
        this.metacritic = metacritic;
        this.genresList = genresList;
        this.platformsList = platformsList;
        this.isFavorite = isFavorite;
    }

    /**
     * Constructor for full game info (+ id, website)
     * @param id - game id
     * @param alias - site code name - used to find game via https request
     * @param name - name of the game
     * @param description - description with html tags
     * @param releaseDate - when game will or was released
     * @param imgHttp - games cover image
     * @param rating - site rating
     * @param metacritic - metacritic rating
     * @param website - game official website
     * @param genresList - list of genres
     * @param platformsList  - what platforms can run
     * @param developersList - who developed
     * @param publishersList - who published
     * @param isFavorite  - is this game noted as a favorite by user
     */
    public Game(Integer id, String alias, String name, String description, Date releaseDate,
                String imgHttp, String rating, String metacritic, String website,
                String[] genresList, String[] platformsList, String[] developersList, String[] publishersList,
                String jsonString, Boolean isFavorite) {
        this.id = id;
        this.alias = alias;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.imgHttp = imgHttp;
        this.rating = rating;
        this.metacritic = metacritic;
        this.website = website;
        this.genresList = genresList;
        this.platformsList = platformsList;
        this.developersList = developersList;
        this.publishersList = publishersList;
        this.jsonString = jsonString;
        this.isFavorite = isFavorite;
    }

    @VisibleForTesting
    public Game(String name, String description) {
        this.description = description;
        this.name = name;
    }

    public String getGameAlias() {
        return alias;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    /**
     * get date in right format
     */
    public String getReleaseStr() {
        String dateStr = "";
        if (releaseDate != null){
            dateStr = (new SimpleDateFormat(DATE_PATTERN, Locale.getDefault())).format(releaseDate);
        }
        return dateStr;
    }

    /**
     * get date or message, if null
     */
    public String getNotNullReleaseStr() {
        String release = getReleaseStr();
        if (release == null || release.length() <= 0){
            release = "Unknown";
        }
        return release;
    }

    /**
     * get date in any case, null is not an option
     */
    public Date getReleaseDateDef(Date defDate) {
        if (releaseDate == null){
            return defDate;
        }
        else return getReleaseDate();
    }

    public String getImgHttp() {
        return imgHttp;
    }

    public String getRating() {
        return rating;
    }

    public String getMetacritic() {
        return metacritic;
    }

    public String getGenresList() {
        return makeString(genresList);
    }

    public String getPlatformsList() {
        return makeString(platformsList);
    }

    public String getDevelopersList() {
        return makeString(developersList);
    }

    public String getPublishersList() {
        return makeString(publishersList);
    }


    /**
     * Make string from list of string values.
     */
    private String makeString(String[] list){
        if (list == null || list.length == 0){
            return null;
        }

        StringBuilder result = new StringBuilder();
        for (String s : list) {
            if (result.length() == 0) {
                result.append(s);
            } else {
                result.append(DELIMITER).append(s);
            }
        }

        return result.toString();
    }

    @NonNull
    @Override
    public String toString() {
        return "siteName="+ alias +"\n"+
        "name="+name+"\n"+
        "description"+description+"\n"+
        "released="+getReleaseStr()+"\n"+
        "imgHttp="+imgHttp+"\n"+
        "rating="+rating+"\n"+
        "metacritic="+metacritic+"\n"+
        "genres="+getGenresList()+"\n"+
        "platforms="+getPlatformsList()+"\n"+
        "developers="+getDevelopersList()+"\n"+
        "publisher="+getPublishersList();
    }

    public String getWebsite() {
        return website;
    }

    public String getJsonString() {
        return jsonString;
    }

    public Integer getId() {
        return id;
    }

    public Boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public String getApiLink() {
        return API_SITE + alias;
    }


}
