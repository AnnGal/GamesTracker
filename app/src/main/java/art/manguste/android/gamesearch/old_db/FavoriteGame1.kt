package art.manguste.android.gamesearch.old_db

import android.content.ContentValues
import android.provider.BaseColumns
import androidx.annotation.VisibleForTesting
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = FavoriteGame.TABLE_NAME)
class FavoriteGame(
        @field:ColumnInfo(index = true, name = "api_id")
        var apiId: Long, @field:ColumnInfo(name = "name")
        var name: String, @field:ColumnInfo(index = true, name = COLUMN_API_ALIAS)
        var apiAlias: String, @field:ColumnInfo(name = "last_update")
        var lastUpdate: Date, @field:ColumnInfo(name = "release")
        var release: Date, @field:ColumnInfo(name= "rating")
        var rating: Double, @field:ColumnInfo(name = "json") var json: String) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = BaseColumns._ID)
    var id: Long = 0

    companion object {
        @Ignore
        const val TABLE_NAME = "games"

        @Ignore
        const val COLUMN_API_ID = "api_id"

        @Ignore
        const val COLUMN_API_ALIAS = "api_alias"

        @VisibleForTesting
        @Ignore
        fun formFavoriteGame(value: ContentValues?): FavoriteGame {
            //ask https
            return FavoriteGame( /*game.getApiId()*/
                    0,
                    "test",
                    "test",
                    Date(),
                    Date(),
                    1.0,
                    "testJson")
        }
    }
}