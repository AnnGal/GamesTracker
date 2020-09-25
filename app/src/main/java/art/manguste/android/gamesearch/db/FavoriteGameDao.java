package art.manguste.android.gamesearch.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface FavoriteGameDao {

    @Query("SELECT COUNT(*) FROM " + FavoriteGame.TABLE_NAME)
    int count();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(FavoriteGame favoriteGame);

    @Query("SELECT * FROM " + FavoriteGame.TABLE_NAME)
    List<FavoriteGame> selectAll();

    @Query("SELECT * FROM " + FavoriteGame.TABLE_NAME + " WHERE " + FavoriteGame.COLUMN_API_ID + " = :id")
    FavoriteGame selectById(long id);

    @Query("SELECT COUNT(*) FROM " + FavoriteGame.TABLE_NAME + " WHERE " + FavoriteGame.COLUMN_API_ALIAS + " = :alias")
    int IsGameInFavorite(String alias);

    @Query("DELETE FROM " + FavoriteGame.TABLE_NAME + " WHERE " + FavoriteGame.COLUMN_API_ID + " = :id")
    int deleteById(long id);

    @Query("DELETE FROM " + FavoriteGame.TABLE_NAME + " WHERE " + FavoriteGame.COLUMN_API_ALIAS + " = :alias")
    int deleteByAlias(String alias);

    @Query("DELETE FROM " + FavoriteGame.TABLE_NAME )
    void deleteAll();

/*    @Update
    int update(FavoriteGame cheese);*/
}
