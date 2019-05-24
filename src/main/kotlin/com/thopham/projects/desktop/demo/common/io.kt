package com.thopham.projects.desktop.demo.common


data class LoginInput(
        val username: String,
        val password: String,
        val device_id: String
)
data class LoginOutput(
        val userId: Int,
        val username: String,
        val token: String,
        val restaurantId: Int,
        val restaurantName: String
)