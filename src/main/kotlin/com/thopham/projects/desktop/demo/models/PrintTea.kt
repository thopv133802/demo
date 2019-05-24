package com.thopham.projects.desktop.demo.models

import com.google.gson.Gson
import com.thopham.projects.desktop.demo.GSON
import javax.persistence.Entity
import javax.persistence.Id


data class PrintTea(
        val restaurantId: Int,
        val printer: Printer
) {
    fun toEntity(): PrintTeaEntity{
        return PrintTeaEntity(
                restaurantId = restaurantId,
                printer = GSON.toJson(printer)
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PrintTea

        if (restaurantId != other.restaurantId) return false
        if (printer != other.printer) return false

        return true
    }

    override fun hashCode(): Int {
        var result = restaurantId
        result = 31 * result + printer.hashCode()
        return result
    }

    override fun toString(): String {
        return printer.name
    }


}

@Entity
data class PrintTeaEntity(
    @Id
    val restaurantId: Int,
    val printer: String
){
    fun toModel(): PrintTea{
        return PrintTea(
                restaurantId = restaurantId,
                printer = GSON.fromJson(printer, Printer::class.java)
        )
    }
    constructor(): this(0, "")
}