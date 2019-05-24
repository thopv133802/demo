package com.thopham.projects.desktop.demo.domain.service.rx

import com.thopham.projects.desktop.demo.common.IO
import com.thopham.projects.desktop.demo.domain.api.PrintFormAPI
import com.thopham.projects.desktop.demo.domain.api.PrinterAPI
import com.thopham.projects.desktop.demo.domain.repository.CPTRepository
import com.thopham.projects.desktop.demo.domain.repository.PrintTeaRepository
import com.thopham.projects.desktop.demo.models.CPT
import com.thopham.projects.desktop.demo.models.IDs
import com.thopham.projects.desktop.demo.models.Printer
import com.thopham.projects.desktop.demo.models.printForm.DataPrint
import com.thopham.projects.desktop.demo.models.printTeaForm.Detail
import io.reactivex.Completable
import io.reactivex.Single
import org.springframework.stereotype.Component

@Component
class RxPrinterService(val printerAPI: PrinterAPI, val cptRepository: CPTRepository, val userService: RxUserService, val printFormAPI: PrintFormAPI, val printTeaRepository: PrintTeaRepository) {
    companion object{
        private const val REGULAR_PRINT_FORM_URL = "https://api.wesave.vn/printform?print=1&data=eyJhY2NvdW50Ijp7Im5hbWUiOiJNaW5oIMSQ4bupYyJ9LCJyZXN0YXVyYW50X2luZm8iOnsibmFtZSI6Ik5ow6AgaMOgbmcgTWluaCDEkOG7qWMiLCJwaG9uZSI6OTA0ODM3MDk1LCJhZGRyZXNzIjoiMzQyIEtoxrDGoW5nIMSQw6xuaCwgVGhhbmggWHXDom4sIEjDoCBO4buZaSIsInR5cGVfcHJpbnQiOlsxLDIsMyw0LDUsNiw3LDgsOSwxMCwxMSwxMl0sImxvZ28iOiJodHRwczovL3RtLndlc2F2ZS52bi9jZG4vcmVzdGF1cmFudC8yNjQ3L3dlYi8yMDE4LzEwLzA0LzE1Mzg2MjA3MTdfNjAzNy5wbmciLCJ0YWxrIjoiWGluIGPhuqNtIMahbiBxdcO9IGtow6FjaCEgXHUwMDNjL2JyXHUwMDNlXHJcbkhlbGxvIEhpIn0sInByaW50X3RlbSI6ZmFsc2UsInBvc3QiOnsicmVzX2lkIjoyNjQ3LCJjdXNfbmFtZSI6IiIsInRlbGVwaG9uZSI6IiIsIm1ldGhvZCI6IiIsInR5cGVvcmRlciI6Miwib3JkZXJfdHlwZSI6InBvcyIsImRlbGl2ZXJ5X3R5cGUiOiJhcnJpdmUiLCJkZXRhaWwiOlt7ImlkIjoxNDg5NCwicXVhbnRpdHkiOjIsInByaWNlIjowLCJ0eXBlIjoxLCJwcmluY2lwbGUiOjAsImlkX29wdCI6MTQ4OTQsInRpbWUiOjE1NTgxNTAwNzIsImNhbF9ieV90aW1lIjowLCJuYW1lIjoiQsOhbmggYsO0bmcgbGFuIHRy4bupbmcgbXXhu5FpIiwidG90YWwiOiIwIiwibWVudV9pZCI6MzYxNH0seyJpZCI6MTQ4OTIsInF1YW50aXR5IjoxLCJwcmljZSI6MCwidHlwZSI6MSwicHJpbmNpcGxlIjowLCJpZF9vcHQiOjE0ODkyLCJ0aW1lIjoxNTU4MTUwMDcyLCJjYWxfYnlfdGltZSI6MCwibmFtZSI6IkLDoW5oIGdpw7IiLCJ0b3RhbCI6IjAiLCJtZW51X2lkIjozNjE0fSx7ImlkIjoxNDg5MSwicXVhbnRpdHkiOjEsInByaWNlIjowLCJ0eXBlIjoxLCJwcmluY2lwbGUiOjAsImlkX29wdCI6MTQ4OTEsInRpbWUiOjE1NTgxNTAwNzIsImNhbF9ieV90aW1lIjowLCJuYW1lIjoiQsOhbmggdOG6uyIsInRvdGFsIjoiMCIsIm1lbnVfaWQiOjM2MTR9XSwiY3VzX2lkIjotMSwicGFnZV9pZCI6MCwiY3VzX21haWwiOiIiLCJ0b3RhbF9wcmljZV9wYXkiOjAsImRpc2NvdW50IjoiIiwic3VwcGxlbWVudGFyeSI6IiIsInZhdCI6IiIsInRvdGFsX3ByaWNlX2VuZCI6MCwiaW5wdXRfbW9uZXkiOjAsIm1vbmV5X2NoYW5nZSI6IiIsIm5vdGUiOiIiLCJkZWxpdmVyIjowLCJwb3NpdGlvbiI6Ik1hbmcgduG7gSIsImlkX2JpbGwiOjg2MTg3LCJ0YWJsZV9mbG9vciI6Ik1hbmcgduG7gSJ9LCJmdWxsIjoxfQ=="
        private const val TEA_PRINT_FORM_URL = "https://api.wesave.vn/printform?print_tea=1&data=W3sicmVzdGF1cmFudCI6IlBo4bufIEfDoCBUYSBQaOG7kSBD4buVIiwibG9nbyI6Imh0dHBzOi8vdG0ud2VzYXZlLnZuL2Nkbi9yZXN0YXVyYW50LzE2MjUvd2ViLzIwMTgvMDEvMTgvMTUxNjI2ODI5OV8yLnBob2dhM18ucG5nIiwicGhvbmUiOiIwOTg2MTQ3OTQ5IiwidGFibGUiOiJU4bqnbmcgMSAtIELDoG4gVmlwMyIsImNvdW50IjoiMS8xIiwiaWQiOjEyNjAxLCJzaXplIjoicyIsIm5hbWUiOiJHw6AgaOG6pXAgcsaw4bujdSIsInByaWNlIjo0NTUwMDAsInF1YW50aXR5IjoxLCJ0b3BpbmciOlt7ImlkIjo1MTIyLCJuYW1lIjoiS2hvYWkgdMOieTEyMyIsInByaWNlIjoiNTAwMCIsInF1YW50aXR5IjoxfV0sInNwZWNpYWxfbm90ZSI6W119XQ=="
    }
    fun fetchPrinters(): Single<List<Printer>> {
        return Single.fromCallable {
            printerAPI.fetchPrinters()
        }
                .subscribeOn(IO)
    }
    fun requestTestPrinter(cpt: CPT): Completable{
        if(cpt.printer.isNotPrinter()) return Completable.error(Exception("Bạn chưa thiết lập máy in, vui lòng thiết lập máy in và thử lại. Cảm ơn."))
        return Completable.fromAction {
            val url = REGULAR_PRINT_FORM_URL
            printerAPI.print(cpt.printer, url)
        }
                .subscribeOn(IO)
    }
    fun print(totalDataPrint: DataPrint): Completable{
        return Completable.fromAction{
            val restaurantId = totalDataPrint.post.res_id
            val details = totalDataPrint.post.detail
            val detailsGrouped = details.groupBy {detail ->
                detail.menu_id
            }
            val menuIds = detailsGrouped.keys
            val dataPrints = detailsGrouped.mapValues {(_, details) ->
                totalDataPrint.copy(
                        post = totalDataPrint.post.copy(
                                detail = details
                        )
                )
            }
            val cpts = dataPrints.mapValues { (menuId, _) ->
                val cptOptional = cptRepository.findOneByIds(
                        IDs(
                                restaurantID = restaurantId,
                                menuID = menuId
                        )
                )
                val cptIsNotPresent = !cptOptional.isPresent
                if(cptIsNotPresent) throw Exception("Menu ID = $menuId không tồn tại.")
                val cpt = cptOptional.get().toModel()
                if(cpt.printer.isNotPrinter()) throw Exception("Bạn chưa thiết lập máy in cho menu ID = $menuId")
                cpt
            }
            for(menuId in menuIds){
                val cpt = cpts[menuId] ?: throw Exception("???: No way")
                val dataPrint = dataPrints[menuId] ?: throw Exception("???: No way")
                val printFormUrl = printFormAPI.fetchPrintFormUrl(dataPrint)
                printerAPI.print(cpt.printer, printFormUrl)
            }
        }
                .subscribeOn(IO)
    }

    fun printTea(restId: Int, details: List<Detail>): Completable {
        return Completable.fromAction {
            val url = printFormAPI.fetchPrintTeaFormUrl(details)
            if(printTeaRepository.existsById(restId)){
                val printTea = printTeaRepository.getOne(restId)
                val printer = printTea.toModel().printer
                if(printer.isNotPrinter())
                    throw Exception("Bạn chưa thiết lập máy in tem. Vui lòng thiết lập máy in tem và thử lại. Cám ơn.")
                printerAPI.printTea(printer, url)
            }
            else
                throw Exception("Bạn chưa thiết lập máy in tem. Vui lòng thiết lập máy in tem và thử lại. Cám ơn.")
        }
                .subscribeOn(IO)
    }

    fun testPrintTea(printer: Printer): Completable {
        return Completable.fromAction {
            if(printer.isNotPrinter())
                throw Exception("Bạn chưa thiết lập máy in tem. Vui lòng thiết lập máy in tem và thử lại. Cảm ơn.")
            printerAPI.printTea(printer, TEA_PRINT_FORM_URL)
        }
                .subscribeOn(IO)
    }
}