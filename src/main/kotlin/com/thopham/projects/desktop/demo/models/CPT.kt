package com.thopham.projects.desktop.demo.models

import com.google.gson.Gson
import com.thopham.projects.desktop.demo.GSON
import java.io.Serializable
import javax.persistence.Embeddable
import javax.persistence.EmbeddedId
import javax.persistence.Entity

data class CPT(
        var ids: IDs,
        var category: Category,
        var printer: Printer
){
        fun toEntity(): CPTEntity{
                return CPTEntity(
                        ids = ids,
                        category = GSON.toJson(category),
                        printer = GSON.toJson(printer)
                )
        }
}

@Entity
data class CPTEntity(
        @EmbeddedId
        var ids: IDs,
        var category: String,
        var printer: String
){
        constructor(): this(IDs(0, 0), "", "")
        fun toModel(): CPT{
                return CPT(
                        ids = ids,
                        category = GSON.fromJson(category, Category::class.java),
                        printer = GSON.fromJson(printer, Printer::class.java)
                )
        }
}

@Embeddable
data class IDs(
        var menuID: Int,
        var restaurantID: Int
): Serializable{
        constructor(): this(0, 0)
        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as IDs

                if (menuID != other.menuID) return false
                if (restaurantID != other.restaurantID) return false

                return true
        }
        override fun hashCode(): Int {
                var result = menuID
                result = 31 * result + restaurantID
                return result
        }
}