package ru.androidschool.intensiv.data.dbo

import androidx.room.Entity

@Entity(primaryKeys = ["movieId", "productionCompanyId"])
data class MovieAndProductionCompanyCrossRef(
    val movieId: Int,
    val productionCompanyId: Int
)
