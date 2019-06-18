package com.thopham.projects.desktop.demo.controller.rx

import com.thopham.projects.desktop.demo.domain.service.rx.RxPrinterService
import com.thopham.projects.desktop.demo.models.printCancel.CancelDataPrint
import com.thopham.projects.desktop.demo.models.printForm.DataPrint
import com.thopham.projects.desktop.demo.models.printTeaForm.PrintTeaDetail
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/printer")
class RxPrinterController(val printerService: RxPrinterService){
    @PostMapping("/print")
    fun requestPrint(@RequestBody input: PrintInput): PrintOutput {
        return try {
            printerService.print(input.dataPrint).blockingAwait()
            PrintOutput(
                    true,
                    "Đang tiến hành in..."
            )
        }
        catch (exception: Exception){
            PrintOutput(
                    false,
                    "Lỗi: ${exception.message}"
            )
        }
    }
    @PostMapping("/printTea")
    fun requestPrintTea(@RequestBody input: PrintTeaInput): PrintTeaOutput{
        return try{
            printerService.printTea(
                    input.res_id,
                    input.data_tem
            ).blockingAwait()
            PrintTeaOutput(
                    true,
                    "Đang tiến hành in tem..."
            )
        }
        catch (exception: Exception){
            PrintTeaOutput(
                    false,
                    "Lỗi: ${exception.message}"
            )
        }
    }
    @PostMapping("/printCancel")
    fun cancel(@RequestBody input: CancelInput): CancelOutput{
        return try {
            printerService.printCancel(input.res_id, input.dataPrint).blockingAwait()
            CancelOutput(
                    true,
                    "Đang tiến hành in..."
            )
        }
        catch (exception: Exception){
            CancelOutput(
                    false,
                    "Lỗi: ${exception.message}"
            )
        }
    }
    data class PrintInput(
            val dataPrint: DataPrint
    ){
        constructor(): this(DataPrint())
    }
    data class PrintOutput(
            val status: Boolean,
            val message: String
    )
    data class PrintTeaInput(
            val res_id: Int,
            val data_tem: List<PrintTeaDetail>
    )
    data class PrintTeaOutput(
            val status: Boolean,
            val message: String
    )
    data class CancelInput(
            val res_id: Int,
            val dataPrint: CancelDataPrint
    )
    data class CancelOutput(
            val status: Boolean,
            val message: String
    )
}