package art.manguste.android.gamesearch.db

import androidx.room.TypeConverter
import java.util.*

object DateConverter {
    @JvmStatic
    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }

    @JvmStatic
    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return if (timestamp == null) null else Date(timestamp)
    }
}