package com.thopham.projects.desktop.demo.domain.repository

import com.thopham.projects.desktop.demo.models.PrintTea
import com.thopham.projects.desktop.demo.models.PrintTeaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PrintTeaRepository: JpaRepository<PrintTeaEntity, Int>{

}