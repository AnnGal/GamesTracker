package art.manguste.android.gamesearch;

import android.util.Log;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Game {

    private static final String DELIMITER = ", ";
    
    private String siteName;
    private String name;
    private String description;
    private Date releaseDate;
    private String imgHttp;
    private String rating;
    private String metacritic;
    private String[] genresList;
    private String[] platformsList;
    private String[] developersList;
    private String[] publishersList;
    

    public Game(String siteName, String name, String description, Date releaseDate, String imgHttp, String rating, String metacritic,
                String[] genresList, String[] platformsList, String[] developersList, String[] publishersList) {
        this.siteName = siteName;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.imgHttp = imgHttp;
        this.rating = rating;
        this.metacritic = metacritic;
        this.genresList = genresList;
        this.platformsList = platformsList;
        this.developersList = developersList;
        this.publishersList = publishersList;
    }

    // the shot version for tests only
    public Game(String name, String description) {
        this.description = description;
        this.name = name;
    }

    public String getSiteName() {
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
        return (new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())).format(releaseDate);
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
                "released="+getReleaseStr()+"\n"+
                "imgHttp="+imgHttp+"\n"+
                "rating="+rating+"\n"+
                "metacritic="+metacritic+"\n"+
                "genres="+getGenresList()+"\n"+
                "platforms="+getPlatformsList();
        return info;
    }
}
