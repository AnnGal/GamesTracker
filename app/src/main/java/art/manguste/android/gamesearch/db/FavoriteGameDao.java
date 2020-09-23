package art.manguste.android.gamesearch.db;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface FavoriteGameDao {

    @Query("SELECT COUNT(*) FROM " + FavoriteGame.TABLE_NAME)
    int count();

    @Insert
    long insert(FavoriteGame favoriteGame);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(FavoriteGame[] favoriteGames);

    @Query("SELECT * FROM " + FavoriteGame.TABLE_NAME)
    Cursor selectAll();

    @Query("SELECT * FROM " + FavoriteGame.TABLE_NAME + " WHERE " + FavoriteGame.COLUMN_ID + " = :id")
    Cursor selectById(long id);

    @Query("DELETE FROM " + FavoriteGame.TABLE_NAME + " WHERE " + FavoriteGame.COLUMN_ID + " = :id")
    int deleteById(long id);

    @Query("DELETE FROM " + FavoriteGame.TABLE_NAME )
    void deleteAll();

    @Update
    int update(FavoriteGame cheese);
}
