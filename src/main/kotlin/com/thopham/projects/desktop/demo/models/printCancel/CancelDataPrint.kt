package com.thopham.projects.desktop.demo.models.printCancel


data class CancelDataPrint (
        val account : Account,
        val quantity : Int,
        val post : Post,
        val full : Int
){
    constructor(): this(Account(), 0, Post(), 0)
}