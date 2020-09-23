package art.manguste.android.gamesearch.db;

import android.content.Context;

import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {FavoriteGame.class}, version = 1)
public abstract class GameDatabase extends RoomDatabase{

    private static volatile GameDatabase sInstance;
    public abstract FavoriteGameDao favoriteGameDao();

    public static synchronized GameDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room
                    .databaseBuilder(context.getApplicationContext(), GameDatabase.class, "SavedGames.db")
                    .build();
            //sInstance.populateInitialData();
        }
        return sInstance;
    }



}
