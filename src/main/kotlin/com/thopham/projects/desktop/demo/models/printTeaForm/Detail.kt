package com.thopham.projects.desktop.demo.models.printTeaForm

data class Detail(
        val restaurant: String,
        val logo: String,
        val phone: String,
        val table: String,
        val count: String,
        val id: Int,
        val size: String,
        val name: String,
        val price: Int,
        val quantity: Int,
        val toping: List<Toping>,
        val special_note: List<String>
)