package com.thopham.projects.desktop.demo.domain.api

import com.google.gson.Gson
import com.thopham.projects.desktop.demo.models.printCancel.CancelDataPrint
import com.thopham.projects.desktop.demo.models.printForm.DataPrint
import com.thopham.projects.desktop.demo.models.printTeaForm.PrintTeaDetail
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
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

    fun fetchPrintFormUrl(dataPrint: CancelDataPrint): String{
        val dataPrintJson = Gson().toJson(dataPrint)
        val dataBase64 = Base64.getEncoder().encodeToString(dataPrintJson.toByteArray())
        return ("https://api.wesave.vn/printform?print=1&data=$dataBase64").apply{
            if(isDebug)
                println("Print Form Url: $this")
        }
    }

    fun fetchPrintTeaFormUrl(details: List<PrintTeaDetail>): String {
        val detailsInJson = Gson().toJson(details)
        val dataInBase64 = Base64.getEncoder().encodeToString(detailsInJson.toByteArray())
        return "https://api.wesave.vn/printform?print_tea=1&data=$dataInBase64".apply{
            if(isDebug)
                println("Print Tea Form Url: $this")
        }
    }

    private fun getCurrentDateTimeInString(): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        //TODO: warning --- this current time in millis la cua Client
        return "" + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + " " + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR)
    }

    fun fetchPrintTeaFormContents(details: List<PrintTeaDetail>): List<String>{
        return details.map { detail ->
            fetchPrintTeaFormContent(detail)
        }
    }
    fun fetchPrintTeaFormContent(detail: PrintTeaDetail): String{
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Print Size Toping</title>
                <style type="text/css">
                    body {
                        margin-top: 0px;
                    }

                    #content_print {
                        font-family: 'tahoma';
                    }
                    #content_print {
                        margin: auto;
                        font-size: 11px;
                    }

                    #content_print table {
                        width: 100%;
                        font-size: 10px;
                    }

                    #content_print table tr th,
                    #content_print table tr td {
                        text-align: left;
                    }

                    #content_print th {
                        font-weight: bold;
                        font-size: 9px;
                    }

                    #content_print .list-products td {
                        border: none;
                    }

                    #content_print .list-toping td {
                        border: none;
                    }


                    #content_print p {
                        margin: 5px;
                        padding: 0px;
                    }
                    #content_print .title {
                        font-weight: 700;
                        text-align: center;
                    }

                    #content_print .info {
                        margin: 15px 0px;
                    }
                    #content_print .thanks {
                        padding: 15px 0px;
                        text-align: center;
                    }
                    tbody tr td {
                        padding: 0;
                        margin: 0;
                    }
                </style>
            </head>
            <body>
            <div id="content_print">
                <table border="0" style="break-after: page;">
                    <tr class="">
                        <td>${detail.table} | (${detail.count})</td>
                    </tr>
                    <tr class="">
                        <td>
                            <b>${detail.name}  - (${detail.size})</b>
                        </td>
                    </tr>
                    """+ StringBuilder().apply{
                        detail.toping.forEach{topping ->
                        this.append(
                            """
                                <tr class="">
                                    <td>+ ${topping.name} x ${topping.quantity}</td>
                                </tr>
                            """)
                        }
                    }.toString() +
                    """
                </table>
            </div>
            </body>
            </html>
        """.trimIndent().apply{
            println(this)
        }
    }
}