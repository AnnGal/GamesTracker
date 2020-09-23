package art.manguste.android.gamesearch.db;

import android.content.ContentValues;
import android.provider.BaseColumns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.util.Date;

import art.manguste.android.gamesearch.core.Game;

@Entity(tableName = FavoriteGame.TABLE_NAME)
public class FavoriteGame {
    @Ignore
    public static final String TABLE_NAME = "games";
    @Ignore
    public static final String COLUMN_API_ID = "api_id";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = BaseColumns._ID)
    public long id;

    @ColumnInfo(index = true, name = "api_id")
    public long apiId;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "api_alias")
    public String apiAlias;

    @ColumnInfo(name = "last_update")
    public long lastUpdate;

    @ColumnInfo(name = "release")
    public long release;

    @ColumnInfo(name = "rating")
    public double rating;

    @ColumnInfo(name = "json")
    public String json;

    public FavoriteGame(long apiId, String name, String apiAlias, long lastUpdate, long release, Double rating, String json) {
        this.apiId = apiId;
        this.name = name;
        this.apiAlias = apiAlias;
        this.lastUpdate = lastUpdate;
        this.release = release;
        this.rating = rating;
        this.json = json;
    }

    public static FavoriteGame formFavoriteGame(@Nullable ContentValues value) {
        //ask https
        final FavoriteGame favoriteGame = new FavoriteGame(
                /*game.getApiId()*/
                0,
                "test",
                "test",
                1l,
                2l,
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

    public long getLastUpdate() {
        return lastUpdate;
    }

    public long getRelease() {
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
