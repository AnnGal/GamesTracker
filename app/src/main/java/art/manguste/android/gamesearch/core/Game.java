package art.manguste.android.gamesearch.core;

import android.util.Log;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Game {

    private static final String DELIMITER = ", ";

    private Integer id;
    private String siteName;
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
     * @param siteName - site code name - used to find game via https request
     * @param name - name of the game
     * @param releaseDate - when game will or was released
     * @param imgHttp - games cover image
     * @param rating - site rating
     * @param metacritic - metacritic rating
     * @param genresList - list of genres
     * @param platformsList  - which platforms can launch
     */
    public Game(String siteName, String name, Date releaseDate, String imgHttp, String rating, String metacritic,
                String[] genresList, String[] platformsList, Boolean isFavorite) {
        this.siteName = siteName;
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
     * @param siteName - site code name - used to find game via https request
     * @param name - name of the game
     * @param description - description with html tags
     * @param releaseDate - when game will or was released
     * @param imgHttp - games cover image
     * @param rating - site rating
     * @param metacritic - metacritic rating
     * @param website - game official website
     * @param genresList - list of genres
     * @param platformsList  - which platforms can launch
     * @param developersList - who developed
     * @param publishersList - who published
     */
    public Game(Integer id, String siteName, String name, String description, Date releaseDate,
                String imgHttp, String rating, String metacritic, String website,
                String[] genresList, String[] platformsList, String[] developersList, String[] publishersList,
                String jsonString, Boolean isFavorite) {
        this.id = id;
        this.siteName = siteName;
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

    // the shot version for tests only
    public Game(String name, String description) {
        this.description = description;
        this.name = name;
    }

    public String getGameAlias() {
        return siteName;
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

    public String getReleaseStr() {
        String dateStr = "";
        if (releaseDate != null){
            dateStr = (new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())).format(releaseDate);
        }
        return dateStr;
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
        String info =
                "siteName="+siteName+"\n"+
                "name="+name+"\n"+
                "description"+description+"\n"+
                "released="+getReleaseStr()+"\n"+
                "imgHttp="+imgHttp+"\n"+
                "rating="+rating+"\n"+
                "metacritic="+metacritic+"\n"+
                "genres="+getGenresList()+"\n"+
                "platforms="+getPlatformsList()+"\n"+
                "developers="+getDevelopersList()+"\n"+
                "publisher="+getPublishersList()
                ;
        return info;
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
}
