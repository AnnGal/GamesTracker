package art.manguste.android.gamesearch.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import art.manguste.android.gamesearch.core.Genre
import com.squareup.moshi.Json

@Entity(tableName = "games")
data class Game (
    //@PrimaryKey(autoGenerate = true)
    //var gameId: Long = 0L,  // this is database Id, not Id from API
    var name: String,
    //@field:ColumnInfo(index = true)
    @PrimaryKey(autoGenerate = false)
    var alias: String,
    var json: String
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Game

        if (alias != other.alias) return false

        return true
    }

    override fun hashCode(): Int {
        return alias.hashCode()
    }
}

