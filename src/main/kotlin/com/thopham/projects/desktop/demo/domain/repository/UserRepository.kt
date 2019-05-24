package com.thopham.projects.desktop.demo.domain.repository

import com.thopham.projects.desktop.demo.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Int>