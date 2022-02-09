package app.com.feedcat.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_result")
data class GameResult(
    @ColumnInfo(name = "satiety") val satiety: Int,
    @ColumnInfo(name = "user_id") val userId: Long,
    @ColumnInfo(name = "datetime") val datetime: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0
}
