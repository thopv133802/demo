package com.thopham.projects.desktop.demo.domain.service.rx

import com.thopham.projects.desktop.demo.common.COMPUTATION
import com.thopham.projects.desktop.demo.common.IO
import com.thopham.projects.desktop.demo.domain.api.CategoryAPI
import com.thopham.projects.desktop.demo.domain.api.DeviceAPI
import com.thopham.projects.desktop.demo.domain.repository.CPTRepository
import com.thopham.projects.desktop.demo.domain.api.PrinterAPI
import com.thopham.projects.desktop.demo.models.CPT
import com.thopham.projects.desktop.demo.models.IDs
import com.thopham.projects.desktop.demo.models.Printer.Companion.NON_PRINTER
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import org.springframework.stereotype.Component

@Component
class RxCPTService(val userService: RxUserService, val deviceAPI: DeviceAPI, val cpRepository: CPTRepository, val categoryAPI: CategoryAPI, val printerAPI: PrinterAPI) {
    fun fetchCPTs(): Single<List<CPT>> {
        return userService.fetchCurrentRestaurantId()
                .map { restaurantID ->
                    cpRepository.findAllByIdsRestaurantID(restaurantID)
                }
                .map { entities ->
                    entities.map { entity -> entity.toModel() }
                }
                .flatMap { cpts ->
                    if (cpts.isNullOrEmpty()) {
                        initCPTs()
                    } else {
                        updateCPTs(cpts)
                    }
                }
                .subscribeOn(
                        IO
                )
    }

    private fun updateCPTs(cpts: List<CPT>): Single<List<CPT>> {
        return Single.zip(userService.fetchCurrentUserToken(), userService.fetchCurrentRestaurantId(), BiFunction<String, Int, Pair<String, Int>> { token, restaurantId ->
            Pair(token, restaurantId)
        })
                .flatMap {(token, restaurantId) ->
                    println("Token: $token")
                    println("Restaurant ID:  $restaurantId")
                    val deviceId = deviceAPI.fetchMacAddress()
                    val categories = categoryAPI.fetchCategories(token, deviceId, restaurantId)
                    val printers = printerAPI.fetchPrinters()
                    val nonePrinter = NON_PRINTER

                    val newCPTs = mutableListOf<CPT>()
                    for (cpt in cpts) {
                        if (!categories.contains(cpt.category))
                            continue
                        if (!printers.contains(cpt.printer))
                            cpt.printer = nonePrinter
                        newCPTs.add(cpt)
                    }
                    Single.just(newCPTs.toList())
                }
                .subscribeOn(IO)
    }

    fun saveCPT(cpt: CPT): Completable{
        return Completable.fromAction{
            cpRepository.save(cpt.toEntity())
        }
                .subscribeOn(IO)
    }

    private fun initCPTs(): Single<List<CPT>> {
        return Single.zip(userService.fetchCurrentUserToken(), userService.fetchCurrentRestaurantId(), BiFunction<String, Int, Pair<String, Int>>{token, restaurant ->
            Pair(token, restaurant)
        })
                .flatMap {(token, restaurantId) ->
                    val deviceId = deviceAPI.fetchMacAddress()
                    val categories = categoryAPI.fetchCategories(token, deviceId, restaurantId)
                    val cpts = mutableListOf<CPT>()
                    for (category in categories) {
                        val categoryId = category.id
                        val cp = CPT(
                                ids = IDs(
                                        menuID = categoryId,
                                        restaurantID = restaurantId
                                ),
                                category = category,
                                printer = NON_PRINTER
                        )
                        cpts.add(cp)
                    }
                    Single.just(cpts)
                }
                .subscribeOn(COMPUTATION)
                .flatMap {cpts ->
                    Completable.fromAction {
                        cpRepository.saveAll(cpts.map { model ->
                            model.toEntity()
                        })
                    }.andThen(
                            Single.just(cpts.toList())
                    )
                }
                .subscribeOn(IO)
    }

    fun refetchCpts(): Single<List<CPT>> {
        return Single.zip(userService.fetchCurrentUserToken(), userService.fetchCurrentRestaurantId(), BiFunction<String, Int, Pair<String, Int>>{token, restaurant ->
            Pair(token, restaurant)
        })
                .flatMap {(token, restaurantId) ->
                    val deviceId = deviceAPI.fetchMacAddress()
                    val categories = categoryAPI.fetchCategories(token, deviceId, restaurantId)
                    val cpts = mutableListOf<CPT>()
                    for (category in categories) {
                        val categoryId = category.id

                        val ids = IDs(
                                menuID = categoryId,
                                restaurantID = restaurantId
                        )
                        if(cpRepository.existsById(ids)){
                            cpts.add(cpRepository.getOne(ids).toModel())
                        }
                        else{
                            val cp = CPT(
                                    ids = ids,
                                    category = category,
                                    printer = NON_PRINTER
                            )
                            cpts.add(cp)
                        }
                    }
                    Single.just(cpts)
                }
                .subscribeOn(COMPUTATION)
                .flatMap {cpts ->
                    Completable.fromAction {
                        cpRepository.saveAll(cpts.map { model ->
                            model.toEntity()
                        })
                    }.andThen(
                            Single.just(cpts.toList())
                    )
                }
                .subscribeOn(IO)
    }
}