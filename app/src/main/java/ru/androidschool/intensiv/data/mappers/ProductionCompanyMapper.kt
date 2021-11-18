package ru.androidschool.intensiv.data.mappers

import ru.androidschool.intensiv.data.dbo.ProductionCompanyDBO
import ru.androidschool.intensiv.data.dto.ProductionCompany

object ProductionCompanyMapper : ViewObjectMapper<ProductionCompanyDBO, ProductionCompany> {
    override fun toViewObject(productionCompany: ProductionCompany): ProductionCompanyDBO {
        return ProductionCompanyDBO(
            productionCompanyId = productionCompany.id,
            name = productionCompany.name
        )
    }
}
