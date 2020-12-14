package art.manguste.android.gamesearch.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import art.manguste.android.gamesearch.core.Genre
import com.squareup.moshi.Json

@Entity(tableName = "games")
data class Game (
    @PrimaryKey(autoGenerate = true)
    var gameId: Long = 0L,  // this is database Id, not Id from API
    var name: String,
    @field:ColumnInfo(index = true)
    var alias: String,
    var json: String
    // todo
    //@field:ColumnInfo(name = "last_update") var lastUpdate: Date,
    //@field:ColumnInfo(name = "release") var release: Date,
    //@field:ColumnInfo(name = "json") var json: String
)

