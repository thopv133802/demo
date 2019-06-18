package com.thopham.projects.desktop.demo.controller.rx

import com.thopham.projects.desktop.demo.App
import com.thopham.projects.desktop.demo.common.FxmlPaths
import com.thopham.projects.desktop.demo.common.UI
import com.thopham.projects.desktop.demo.domain.service.rx.RxUserService
import io.reactivex.disposables.Disposable
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent
import org.springframework.stereotype.Component

@Component
class RxLoginController(val userService: RxUserService){
    lateinit var account: TextField
    lateinit var password: PasswordField
    lateinit var login: Button

    @FXML
    fun initialize(){
        userService.userLogged()
                .observeOn(UI)
                .subscribe({userLogged ->
                    if(userLogged)
                        navigateToSetupView()
                }, { err->
                    err.printStackTrace()
                })
    }

    fun requestLogin(){
        login.isDisable = true
        userService.login(account.text, password.text)
                .observeOn(UI)
                .subscribe({
                    App.showAlert("Thông báo", "Đăng nhập thành công"){
                        navigateToSetupView()
                    }
                }, {
                    showError(it.message)
                    it.printStackTrace()
                    login.isDisable = false
                })
    }

    private fun navigateToSetupView() {
        App.navigateTo(FxmlPaths.SETUP)
    }

    private fun showError(message: String?, onClose: () -> Unit = {}) {
        val errorContent = message ?: "Có lỗi xảy ra trong quá trình đăng nhập."
        App.showAlert("Lỗi", errorContent, onClose)
    }

    fun onKeyPressed(keyEvent: KeyEvent) {
//        if (keyEvent.code == KeyCode.ENTER){
//            requestLogin()
//        }
    }

    fun onMouseClicked(mouseEvent: MouseEvent) {
        requestLogin()
    }

}