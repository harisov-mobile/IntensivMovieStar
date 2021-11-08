package ru.androidschool.intensiv.data.dbo

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.androidschool.intensiv.BuildConfig

@Entity(tableName = "actors")
data class ActorDBO(
    @PrimaryKey
    val actorId: Int,
    val name: String,
    val profilePath: String
) {
    fun getProfile(): String = "${BuildConfig.IMAGE_URL}$profilePath"
}
