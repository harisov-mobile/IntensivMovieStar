package ru.androidschool.intensiv.data.mappers

import ru.androidschool.intensiv.data.dbo.ProductionCompanyDBO
import ru.androidschool.intensiv.data.dto.ProductionCompany

object ProductionCompanyMapper {
    fun toProductionCompanyDBOList(productionCompanies: List<ProductionCompany>): List<ProductionCompanyDBO> {
        return productionCompanies.map { toProductionCompanyDBO(it) }
    }

    fun toProductionCompanyDBO(productionCompany: ProductionCompany): ProductionCompanyDBO {
        return ProductionCompanyDBO(
            productionCompanyId = productionCompany.id,
            name = productionCompany.name
        )
    }
}