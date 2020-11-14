package art.manguste.android.gamesearch.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteGameDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favoriteGame: FavoriteGame?): Long

    @Query("DELETE FROM " + FavoriteGame.TABLE_NAME + " WHERE " + FavoriteGame.Companion.COLUMN_API_ALIAS + " = :alias")
    fun deleteByAlias(alias: String?): Int

    @Query("SELECT COUNT(*) FROM " + FavoriteGame.Companion.TABLE_NAME + " WHERE " + FavoriteGame.Companion.COLUMN_API_ALIAS + " = :alias")
    fun IsGameInFavorite(alias: String?): Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(favoriteGame: FavoriteGame?): Int

    @Query("SELECT * FROM " + FavoriteGame.Companion.TABLE_NAME)
    fun selectAll(): LiveData<List<FavoriteGame?>?>?

    @Query("SELECT * FROM " + FavoriteGame.Companion.TABLE_NAME)
    fun selectAllNoLiveData(): List<FavoriteGame?>?

    @Query("SELECT * FROM " + FavoriteGame.Companion.TABLE_NAME + " WHERE " + FavoriteGame.Companion.COLUMN_API_ID + " = :id")
    fun selectById(id: Long): FavoriteGame?

    @Query("SELECT COUNT(*) FROM " + FavoriteGame.Companion.TABLE_NAME)
    fun count(): Int

    @Query("DELETE FROM " + FavoriteGame.Companion.TABLE_NAME + " WHERE " + FavoriteGame.Companion.COLUMN_API_ID + " = :id")
    fun deleteById(id: Long): Int

    @Delete
    fun delete(favoriteGame: FavoriteGame?)

    @Query("DELETE FROM " + FavoriteGame.Companion.TABLE_NAME)
    fun deleteAll()
}