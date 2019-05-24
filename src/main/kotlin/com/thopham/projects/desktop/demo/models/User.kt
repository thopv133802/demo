package com.thopham.projects.desktop.demo.models

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class User(
        @Id
        var id: Int,
        var userId: Int,
        var restaurantId: Int,
        var restaurantName: String,
        var username: String,
        var token: String
){
        constructor(): this(0, 0, 0, "", "", "")
}