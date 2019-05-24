package com.thopham.projects.desktop.demo.domain.repository

import com.thopham.projects.desktop.demo.models.CPTEntity
import com.thopham.projects.desktop.demo.models.IDs
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CPTRepository: JpaRepository<CPTEntity, IDs> {
    fun findAllByIdsRestaurantID(restaurantID: Int): List<CPTEntity>
    fun findOneByIds(iDs: IDs): Optional<CPTEntity>
}