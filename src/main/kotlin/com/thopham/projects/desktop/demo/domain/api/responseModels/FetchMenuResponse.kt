package com.thopham.projects.desktop.demo.domain.api.responseModels

data class FetchMenuResponse(
        val status: Boolean,
        val mes: String?,
        val response: Body?
){
    constructor(): this(false, "", Body())
    data class Body(
            val menu: List<Menu>
    ){
        constructor(): this(listOf())
    }
    data class Menu(
            val id: Int,
            val name: String
    ){
        constructor(): this(0, "")
    }
}