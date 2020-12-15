package art.manguste.android.gamesearch.db

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface GameDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(game: Game)

    @Update
    suspend fun update(game: Game)

    @Delete
    suspend fun delete(game: Game)

    @Query("SELECT * FROM games ") //ORDER BY gameId DESC
    suspend fun getAll(): List<Game>

    @Query("SELECT * FROM games ") //ORDER BY gameId DESC
    fun getAllGamesLiveData(): LiveData<List<Game>>

    @Query("SELECT COUNT(*) FROM games ")
    fun count(): Int


/*    @Query("DELETE FROM " + FavoriteGame.TABLE_NAME + " WHERE " + FavoriteGame.Companion.COLUMN_API_ALIAS + " = :alias")
    fun deleteByAlias(alias: String?): Int

    @Query("SELECT COUNT(*) FROM " + FavoriteGame.Companion.TABLE_NAME + " WHERE " + FavoriteGame.Companion.COLUMN_API_ALIAS + " = :alias")
    fun IsGameInFavorite(alias: String?): Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(favoriteGame: FavoriteGame?): Int

    @Query("SELECT * FROM " + FavoriteGame.Companion.TABLE_NAME)
    fun selectAll(): LiveData<List<FavoriteGame?>?>?

    @Query("SELECT * FROM " + FavoriteGame.Companion.TABLE_NAME)
    fun selectAllNoLiveData(): List<FavoriteGame?>?   */

}





