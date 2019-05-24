package com.thopham.projects.desktop.demo.controller.rx

import com.thopham.projects.desktop.demo.App
import com.thopham.projects.desktop.demo.common.FxmlPaths
import com.thopham.projects.desktop.demo.common.UI
import com.thopham.projects.desktop.demo.customViews.MaterialButton
import com.thopham.projects.desktop.demo.domain.service.rx.RxCPTService
import com.thopham.projects.desktop.demo.domain.service.rx.RxPrintTeaService
import com.thopham.projects.desktop.demo.domain.service.rx.RxPrinterService
import com.thopham.projects.desktop.demo.domain.service.rx.RxUserService
import com.thopham.projects.desktop.demo.models.*
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import org.springframework.stereotype.Component

@Component
class RxSetupController(val cptService: RxCPTService, val userService: RxUserService, val printerService: RxPrinterService, val printTeaService: RxPrintTeaService){
    lateinit var restaurantName: Label
    lateinit var userName: Label
    lateinit var temPrinterComboBox: ComboBox<Printer>
    lateinit var table: TableView<CPT>
    lateinit var categoryColumn: TableColumn<CPT, Label>
    lateinit var printerColumn: TableColumn<CPT, HBox>
    lateinit var testPrinterColumn: TableColumn<CPT, HBox>
    lateinit var logoutButton: Button
    lateinit var syncButton: Button
    lateinit var testPrintTeaButton: Button
    @FXML
    fun initialize(){
        initUserInformation()
        initTemPrinterComboBox()
        initCategoriesTableView()
        setupButtons()
    }

    private fun initTemPrinterComboBox() {
        Single.zip(
                printerService.fetchPrinters(),
                printTeaService.fetchTeaPrinter(),
                BiFunction<List<Printer>, PrintTea, Pair<List<Printer>, PrintTea>>{printers, printTea ->
                    Pair(printers, printTea)
                }
        )
                .observeOn(UI)
                .subscribe({(printers, printTea) ->
                    temPrinterComboBox.items = FXCollections.observableArrayList(printers)
                    temPrinterComboBox.selectionModel.select(printTea.printer)
                    temPrinterComboBox.setOnAction {
                        val selectedPrinter = temPrinterComboBox.selectionModel.selectedItem
                        printTeaService.requestChangePrinter(selectedPrinter)
                                .observeOn(UI)
                                .subscribe({
                                    println("saved")
                                }, {err ->
                                    App.showAlert("Lỗi", err.message ?: "Lỗi cập nhật máy in. Vui lòng thử lại")
                                    err.printStackTrace()
                                })
                    }
                }, { err ->
                    App.showAlert("Lỗi", err.message ?: "Lỗi lấy thông tin các máy in. Vui lòng thử lại")
                    err.printStackTrace()
                })
    }

    private fun setupButtons(){
        logoutButton.setOnAction {
            logoutButton.isDisable = true
            userService.logout()
                    .observeOn(UI)
                    .subscribe({
                        App.showConfirm("Xác nhận", "Bạn có muốn đăng xuất khỏi hệ thống", {
                            App.navigateTo(FxmlPaths.LOGIN)
                        }){
                            logoutButton.isDisable = false
                        }
                    }, {err ->
                        App.showAlert("Lỗi", err.message ?: "Đăng xuất thất bại. Vui lòng thử lại")
                        err.printStackTrace()
                        logoutButton.isDisable = false
                    })
        }
        syncButton.setOnAction {
            syncButton.isDisable = true
            cptService.refetchCpts()
                    .observeOn(UI)
                    .subscribe({cpts ->
                        table.items = FXCollections.observableArrayList(cpts)
                        table.refresh()
                        syncButton.isDisable = false
                    }, {err ->
                        App.showAlert("Lỗi", err.message ?: "Đồng bộ thất bại. Vui lòng thử lại. Cảm ơn.")
                        err.printStackTrace()
                        syncButton.isDisable = false
                    })
        }
        testPrintTeaButton.setOnAction {
            testPrintTeaButton.isDisable = true
            printerService.testPrintTea(temPrinterComboBox.selectionModel.selectedItem)
                    .observeOn(UI)
                    .subscribe({
                        println("Done.")
                        testPrintTeaButton.isDisable = false
                    }, {err ->
                        App.showAlert("Lỗi", err.message ?: "In thử thất bại. Vui lòng thử lại. Cảm ơn.")
                        testPrintTeaButton.isDisable = false
                    })
        }
    }

    private fun initCategoriesTableView() {
        printerService.fetchPrinters()
                .flatMapCompletable {printers ->
                    Completable.fromAction {
                        //Build category column
                        categoryColumn.setCellValueFactory { features ->
                            val category = features.value.category.toString()
                            ReadOnlyObjectWrapper(
                                    Label(category)
                            )
                        }
                        //Build printer column
                        printerColumn.setCellValueFactory { features ->
                            val cpt = features.value
                            ReadOnlyObjectWrapper(
                                    buildPrinterColumnCell(cpt, printers)
                            )
                        }
                        testPrinterColumn.setCellValueFactory { features ->
                            val cpt = features.value
                            ReadOnlyObjectWrapper(
                                    buildTestPrinterColumnCell(cpt)
                            )
                        }
                    }
                }
                .andThen(
                        cptService.fetchCPTs()
                )
                .observeOn(UI)
                .subscribe({cpts ->
                    table.items = FXCollections.observableArrayList(cpts)
                    table.refresh()
                }, {err ->
                    App.showAlert("Lỗi", err.message ?: "...")
                    err.printStackTrace()
                })
    }

    private fun initUserInformation() {
        userService.fetchForceCurrentUser()
                .observeOn(UI)
                .subscribe({ user ->
                    restaurantName.text = user.restaurantName
                    userName.text = user.username
                }, { err ->
                    App.showAlert("Lỗi", err.message ?: "...")
                    err.printStackTrace()
                })
    }

    private fun buildTestPrinterColumnCell(cpt: CPT): HBox{
        return HBox(
                MaterialButton(
                        "In thử"
                ).apply{
                    setOnMouseClicked {
                        isDisable = true
                        printerService.requestTestPrinter(cpt)
                                .observeOn(UI)
                                .subscribe({
                                    isDisable = false
                                }, {err ->
                                    App.showAlert(" Lỗi", err.message ?: "...")
                                    isDisable = false
                                })
                    }
                    maxWidth = Double.MAX_VALUE
                    HBox.setHgrow(this, Priority.ALWAYS)
                }
        )
    }

    private fun buildPrinterColumnCell(cpt: CPT, printers: List<Printer>): HBox {
        val selectedPrinter = cpt.printer
        return HBox(
                ComboBox<Printer>(
                        FXCollections.observableArrayList(
                                printers
                        )
                ).apply {
                    selectionModel.select(selectedPrinter)
                    maxWidth = Double.MAX_VALUE
                    HBox.setHgrow(this, Priority.ALWAYS)
                    setOnAction {
                        val newPrinter = selectionModel.selectedItem
                        cpt.printer = newPrinter
                        cptService.saveCPT(cpt)
                                .subscribe({
                                    println("Saved")
                                }, { err ->
                                    App.showAlert("Lỗi", err.message ?: "...")
                                    err.printStackTrace()
                                })
                    }
                }
        )
    }
}
