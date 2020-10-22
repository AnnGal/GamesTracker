package art.manguste.android.gamesearch.db;

import android.content.ContentValues;
import android.provider.BaseColumns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;


@Entity(tableName = FavoriteGame.TABLE_NAME)
public class FavoriteGame {
    @Ignore
    public static final String TABLE_NAME = "games";
    @Ignore
    public static final String COLUMN_API_ID = "api_id";
    @Ignore
    public static final String COLUMN_API_ALIAS = "api_alias";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = BaseColumns._ID)
    public long id;

    @ColumnInfo(index = true, name = "api_id")
    public long apiId;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(index = true, name = COLUMN_API_ALIAS)
    public String apiAlias;

    @ColumnInfo(name = "last_update")
    public Date lastUpdate;

    @ColumnInfo(name = "release")
    public Date release;

    @ColumnInfo(name = "rating")
    public double rating;

    @ColumnInfo(name = "json")
    public String json;

    public FavoriteGame(long apiId, String name, String apiAlias, Date lastUpdate, Date release, Double rating, String json) {
        this.apiId = apiId;
        this.name = name;
        this.apiAlias = apiAlias;
        this.lastUpdate = lastUpdate;
        this.release = release;
        this.rating = rating;
        this.json = json;
    }

    @VisibleForTesting
    @Ignore
    public static FavoriteGame formFavoriteGame(@Nullable ContentValues value) {
        //ask https
        final FavoriteGame favoriteGame = new FavoriteGame(
                /*game.getApiId()*/
                0,
                "test",
                "test",
                new Date(),
                new Date(),
                1.0d,
                "testJson");
        return  favoriteGame;
    }

    public long getId() {
        return id;
    }

    public long getApiId() {
        return apiId;
    }

    public String getName() {
        return name;
    }

    public String getApiAlias() {
        return apiAlias;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public Date getRelease() {
        return release;
    }

    public Double getRating() {
        return rating;
    }

    public String getJson() {
        return json;
    }

/*
    @TypeConverter
    public Long dateToTimestamp(Date date) {
        if (date == null) {
            return null;
        } else {
            return date.getTime();
        }
    }
*/


}
