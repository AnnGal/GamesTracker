package art.manguste.android.gamesearch.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

@Database(entities = {FavoriteGame.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class GameDatabase extends RoomDatabase{

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "favoritegames";
    private static volatile GameDatabase sInstance;
    public abstract FavoriteGameDao favoriteGameDao();

    public static synchronized GameDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room
                        .databaseBuilder(context.getApplicationContext(),
                                GameDatabase.class,
                                GameDatabase.DATABASE_NAME)
                        .build();
            }
        }
        return sInstance;
    }
}
