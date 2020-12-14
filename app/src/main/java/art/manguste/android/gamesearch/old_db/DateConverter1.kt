package art.manguste.android.gamesearch.old_db

import androidx.room.TypeConverter
import java.util.*

object DateConverter {

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return if (timestamp == null) null else Date(timestamp)
    }
}