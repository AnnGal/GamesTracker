package art.manguste.android.gamesearch.db;

import android.content.ContentValues;
import android.provider.BaseColumns;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

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
    
}
