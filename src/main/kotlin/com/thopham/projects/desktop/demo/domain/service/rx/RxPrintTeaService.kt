package com.thopham.projects.desktop.demo.domain.service.rx

import com.thopham.projects.desktop.demo.common.IO
import com.thopham.projects.desktop.demo.domain.api.PrinterAPI
import com.thopham.projects.desktop.demo.domain.repository.PrintTeaRepository
import com.thopham.projects.desktop.demo.models.PrintTea
import com.thopham.projects.desktop.demo.models.Printer
import com.thopham.projects.desktop.demo.models.Printer.Companion.NON_PRINTER
import io.reactivex.Completable
import io.reactivex.CompletableSource
import io.reactivex.Single
import io.reactivex.SingleSource
import org.springframework.stereotype.Component

@Component
class RxPrintTeaService(val printTeaRepository: PrintTeaRepository, val userService: RxUserService, val printerAPI: PrinterAPI){
    companion object {

    }
    fun fetchTeaPrinter(): Single<PrintTea> {
        return userService.fetchCurrentRestaurantId()
                .map { restId ->
                    val printTeaOptional = printTeaRepository.findById(restId)
                    val isExists = printTeaOptional.isPresent
                    if(isExists)
                        printTeaOptional.get().toModel()
                    else
                        PrintTea(
                                restaurantId = restId,
                                printer = NON_PRINTER
                        )
                }
                .subscribeOn(IO)
    }

    fun requestChangePrinter(newPrinter: Printer): Completable{
        return userService.fetchCurrentRestaurantId()
                .flatMapCompletable{restId ->
                    Completable.fromAction {
                        printTeaRepository.save(
                                PrintTea(
                                        restaurantId = restId,
                                        printer = newPrinter
                                ).toEntity()
                        )
                    }
                }
                .subscribeOn(IO)
    }
}