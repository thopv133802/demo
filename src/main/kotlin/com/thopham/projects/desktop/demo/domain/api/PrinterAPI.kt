package com.thopham.projects.desktop.demo.domain.api

import com.sun.javafx.print.PrintHelper
import com.sun.javafx.print.Units
import com.thopham.projects.desktop.demo.models.Printer
import javafx.application.Platform
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.concurrent.Worker
import javafx.print.Collation
import javafx.print.PageOrientation
import javafx.print.Paper
import javafx.print.PrinterJob
import javafx.scene.Scene
import javafx.scene.transform.Scale
import javafx.scene.web.WebView
import javafx.stage.Stage
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class PrinterAPI {

    private var printers = mutableListOf<Printer>()
    private lateinit var printServices: Array<javafx.print.Printer>

    @Value("\${debug.enabled}")
    var isDebug = false
    init {
        if (printers.isNullOrEmpty()){
            printServices = javafx.print.Printer.getAllPrinters().toTypedArray()
            printers = mutableListOf<Printer>().apply{
                add(Printer.NON_PRINTER)
            }
            if(printServices.isNotEmpty()) {
                for ((index, printService) in printServices.withIndex()) {
                    //println("Index: $index, Service name: ${printService.name}")
                    printers.add(
                            Printer(index, printService.name)
                    )
                }
            }
        }
    }

    fun fetchPrinters(): List<Printer>{
        return printers
    }
    private fun fetchPrinterService(printer: Printer): javafx.print.Printer{
        if(printer == Printer.NON_PRINTER)
            throw Exception("Máy in không khả dụng hoặc chưa được thiết lập. Vui lòng kiểm tra lại")
        return printServices[printer.id]
    }
    fun printByUrl(printer: Printer, url: String, copies: Int = 1){
        Platform.runLater {
            val printerService = fetchPrinterService(printer)
            val printerJob = PrinterJob.createPrinterJob(printerService)
            val jobSettings = printerJob.jobSettings.apply{
                this.copies = copies
            }
            val pageLayout = printerJob.printer.createPageLayout(
                    Paper.NA_LETTER,
                    PageOrientation.PORTRAIT,
                    javafx.print.Printer.MarginType.HARDWARE_MINIMUM
            )
            jobSettings.pageLayout = pageLayout

            val webView = WebView().apply{
                setPrefSize(pageLayout.printableWidth, pageLayout.printableHeight)
            }
            loadUrlThenPrint(webView, printerJob, url)
        }
    }
    fun printByContent(printer: Printer, content: String, copies: Int = 1){
        Platform.runLater {
            val printerService = fetchPrinterService(printer)
            val printerJob = PrinterJob.createPrinterJob(printerService)
            val jobSettings = printerJob.jobSettings.apply{
                this.copies = copies
            }
            val pageLayout = printerJob.printer.createPageLayout(
                    Paper.NA_LETTER,
                    PageOrientation.PORTRAIT,
                    javafx.print.Printer.MarginType.HARDWARE_MINIMUM
            )
            jobSettings.pageLayout = pageLayout

            val webView = WebView().apply{
                setPrefSize(pageLayout.printableWidth, pageLayout.printableHeight)
            }
            loadContentThenPrint(webView, printerJob, content)
        }
    }
    fun printTeaByContent(printer: Printer, content: String, copies: Int = 1, width: Double = 40.0, height: Double = 30.0/*, units: Units = Units.MM*/){
        Platform.runLater {
            val printerService = fetchPrinterService(printer)
            val printerJob = PrinterJob.createPrinterJob(printerService)
            val jobSettings = printerJob.jobSettings

//            val paper = PrintHelper.createPaper(
//                    "Tea Paper",
//                    width * 4 / 3,
//                    height * 4 / 3,
//                    units
//            )
            val paper = printerJob.printer.defaultPageLayout.paper


            val teaPageLayout = printerJob.printer.createPageLayout(
                    paper,
                    PageOrientation.PORTRAIT,
                    javafx.print.Printer.MarginType.HARDWARE_MINIMUM
            )

            jobSettings.pageLayout = teaPageLayout
            jobSettings.copies = copies

            val webView = WebView().apply{
                setPrefSize(
                        paper.width,
                        paper.height
                )
            }
            webView.transforms.add(Scale(3.0 / 4, 3.0 / 4))

            loadContentThenPrint(webView, printerJob, content)
        }
    }
    fun printTeaByUrl(printer: Printer, url: String, copies: Int = 1, width: Double = 40.0, height: Double = 30.0, units: Units = Units.MM){
        Platform.runLater {
            val printerService = fetchPrinterService(printer)
            val printerJob = PrinterJob.createPrinterJob(printerService)
            val jobSettings = printerJob.jobSettings

            val paper = PrintHelper.createPaper(
                    "Tea Paper",
                    width * 4 / 3,
                    height * 4 / 3,
                    units
            )

            val teaPageLayout = printerJob.printer.createPageLayout(
                    paper,
                    PageOrientation.PORTRAIT,
                    javafx.print.Printer.MarginType.HARDWARE_MINIMUM
            )

            jobSettings.pageLayout = teaPageLayout
            jobSettings.copies = copies

            val webView = WebView().apply{
                setPrefSize(
                        paper.width,
                        paper.height
                )
            }
            webView.transforms.add(Scale(3.0 / 4, 3.0 / 4))

            loadUrlThenPrint(webView, printerJob, url)
        }
    }

    private fun loadUrlThenPrint(webView: WebView, printerJob: PrinterJob, url: String) {
        println("PrintForm Url: $url")
        val engine = webView.engine

        engine.loadWorker.stateProperty().addListener(object : ChangeListener<Worker.State> {
            override fun changed(observable: ObservableValue<out Worker.State>, oldValue: Worker.State, newValue: Worker.State) {
                val isEnd = newValue == Worker.State.SUCCEEDED || newValue == Worker.State.CANCELLED || newValue == Worker.State.FAILED
                if (isEnd) engine.loadWorker.stateProperty().removeListener(this)
                if (newValue == Worker.State.SUCCEEDED) {
                    engine.loadWorker.stateProperty().removeListener(this)
                    if (isDebug) {
                        println("Tải nội dung thành công.")
                    }
                    val closeWebView = showWebView(webView)
                    val success = printerJob.printPage(webView)
                    if (success) {
                        printerJob.endJob()
                        if (isDebug)
                            println("In thành công.")
                    } else {
                        println("Lỗi: In không thành công hoặc bạn đã hủy in. Xin vui lòng thử lại.")
                    }
                    closeWebView()
                } else if (newValue == Worker.State.FAILED) {
                    throw Exception("Lỗi: không thể load url: $url")
                } else if (newValue == Worker.State.CANCELLED) {
                    throw Exception("Cảnh bảo: Bạn vừa dừng quy trình lại.")
                }
            }
        })
        engine.load(url)
    }
    private fun loadContentThenPrint(webView: WebView, printerJob: PrinterJob, content: String) {
        println("PrintForm Content: $content")
        val engine = webView.engine

        engine.loadWorker.stateProperty().addListener(object : ChangeListener<Worker.State> {
            override fun changed(observable: ObservableValue<out Worker.State>, oldValue: Worker.State, newValue: Worker.State) {
                val isEnd = newValue == Worker.State.SUCCEEDED || newValue == Worker.State.CANCELLED || newValue == Worker.State.FAILED
                if (isEnd) engine.loadWorker.stateProperty().removeListener(this)
                if (newValue == Worker.State.SUCCEEDED) {
                    engine.loadWorker.stateProperty().removeListener(this)
                    if (isDebug) {
                        println("Tải nội dung thành công.")
                    }
                    val closeWebView = showWebView(webView)
                    val success = printerJob.printPage(webView)
                    if (success) {
                        printerJob.endJob()
                        if (isDebug)
                            println("In thành công.")
                    } else {
                        println("Lỗi: In không thành công hoặc bạn đã hủy in. Xin vui lòng thử lại.")
                    }
                    closeWebView()
                } else if (newValue == Worker.State.FAILED) {
                    throw Exception("Lỗi: không thể load content: $content")
                } else if (newValue == Worker.State.CANCELLED) {
                    throw Exception("Cảnh bảo: Bạn vừa dừng quy trình lại.")
                }
            }
        })
        engine.loadContent(content)
    }
    private fun showWebView(webView: WebView, onClose: (() -> Unit) = {}): () -> Unit{
        val stage = Stage()
        stage.scene = Scene(webView)
        stage.title = "Đang tiến hành in..."
        stage.setOnHidden{
            onClose()
        }
        stage.show()
        return {
            stage.close()
        }
    }

    fun printTeaByContents(printer: Printer, contents: List<String>, copies: Int = 1) {
        for(content in contents)
            printTeaByContent(printer, content, copies)
    }
}
