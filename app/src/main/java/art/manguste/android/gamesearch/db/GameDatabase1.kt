package art.manguste.android.gamesearch.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [FavoriteGame::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class GameDatabase : RoomDatabase() {
    abstract fun favoriteGameDao(): FavoriteGameDao?

    companion object {
        private val LOCK = Any()
        private const val DATABASE_NAME = "favoritegames"

        @Volatile
        private var sInstance: GameDatabase? = null
        @Synchronized
        fun getInstance(context: Context): GameDatabase? {
            if (sInstance == null) {
                synchronized(LOCK) {
                    sInstance = Room
                            .databaseBuilder(context.applicationContext,
                                    GameDatabase::class.java,
                                    DATABASE_NAME)
                            .build()
                }
            }
            return sInstance
        }
    }
}