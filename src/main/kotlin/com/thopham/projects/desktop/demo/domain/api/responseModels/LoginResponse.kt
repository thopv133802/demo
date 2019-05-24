package com.thopham.projects.desktop.demo.domain.api.responseModels


data class LoginResponse
constructor(
        val status : Boolean,
        val response : Response?,
        val mes: String?
){
	constructor(): this(false, null, null)

	data class Info  constructor(
			val id : Int,
			val username : String,
			val name : String,
			val restaurant : Restaurant
	){
		constructor(): this(0, "", "", Restaurant())
	}

	data class Response constructor(
            val token : String,
            val info : Info,
            val date_now : Int,
            val date_end : Int,
            val token_card : String
	){
		constructor(): this("", Info(), 0, 0, "")
	}

	data class Restaurant  constructor(
			val id : Int,
			val name : String
	){
		constructor(): this(0, "")
	}
}