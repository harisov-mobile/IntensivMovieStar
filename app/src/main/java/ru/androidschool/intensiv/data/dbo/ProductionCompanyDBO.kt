package ru.androidschool.intensiv.data.dbo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productionCompanies")
data class ProductionCompanyDBO(
    @PrimaryKey
    val productionCompanyId: Int,
    val name: String
)
