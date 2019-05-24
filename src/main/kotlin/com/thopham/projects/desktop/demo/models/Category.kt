package com.thopham.projects.desktop.demo.models

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Category(
        @Id
        var id: Int,
        var name: String
){

    constructor(): this(0, "")
    override fun toString(): String {
        return "($id) $name"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Category

        if (id != other.id) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        return result
    }
}