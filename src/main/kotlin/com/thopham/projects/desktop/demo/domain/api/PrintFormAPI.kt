package com.thopham.projects.desktop.demo.domain.api

import com.google.gson.Gson
import com.thopham.projects.desktop.demo.models.printForm.DataPrint
import com.thopham.projects.desktop.demo.models.printTeaForm.Detail
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.*

@Component
class PrintFormAPI {
    @Value("\${debug.enabled}")
    var isDebug = false
    fun fetchPrintFormUrl(dataPrint: DataPrint): String {
        val dataPrintJson = Gson().toJson(dataPrint)
        val dataBase64 = Base64.getEncoder().encodeToString(dataPrintJson.toByteArray())
        return ("https://api.wesave.vn/printform?print=1&data=$dataBase64").apply{
            if(isDebug)
                println("Print Form Url: $this")
        }
    }

    fun fetchPrintTeaFormUrl(details: List<Detail>): String {
        val detailsInJson = Gson().toJson(details)
        val dataInBase64 = Base64.getEncoder().encodeToString(detailsInJson.toByteArray())
        return "https://api.wesave.vn/printform?print_tea=1&data=$dataInBase64".apply{
            if(isDebug)
                println("Print Tea Form Url: $this")
        }
    }
}