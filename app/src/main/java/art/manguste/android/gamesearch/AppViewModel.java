package art.manguste.android.gamesearch;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

import art.manguste.android.gamesearch.core.Game;
import art.manguste.android.gamesearch.db.FavoriteGame;
import art.manguste.android.gamesearch.db.GameDatabase;

public class AppViewModel  extends AndroidViewModel {

    private LiveData<List<FavoriteGame>> gamesFavorite;
    // private LiveData<ArrayList<Game>> gamesHot;
    // private LiveData<ArrayList<Game>> gamesSearch;

    public AppViewModel(@NonNull Application application) {
        super(application);
        GameDatabase gameDatabase = GameDatabase.getInstance(this.getApplication());
        gamesFavorite = gameDatabase.favoriteGameDao().selectAll();
    }

    public LiveData<List<FavoriteGame>> getGamesFavorite() {
        return gamesFavorite;
    }

    /*public LiveData<ArrayList<Game>> getGamesHot() {
        return gamesHot;
    }

    public LiveData<ArrayList<Game>> getGamesSearch() {
        return gamesSearch;
    }*/
}
