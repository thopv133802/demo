package com.thopham.projects.desktop.demo.models

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Printer(
        @Id
        var id: Int,
        var name: String
){
    constructor(): this(0, "")
    companion object{
        val NON_PRINTER = Printer(-1, "Chưa thiết lập máy in")
    }
    override fun toString(): String {
        return name
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Printer

        if (id != other.id) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        return result
    }

    fun isNotPrinter(): Boolean{
        return equals(NON_PRINTER)
    }
}