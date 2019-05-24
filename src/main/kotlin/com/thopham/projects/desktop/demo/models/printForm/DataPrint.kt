package com.thopham.projects.desktop.demo.models.printForm
data class DataPrint (
		val account : Account,
		val restaurant_info : RestaurantInfo,
		val print_tem : Boolean,
		val post : Post,
		val full : Int
){
	constructor(): this(Account(), RestaurantInfo(), false, Post(), 0)
}