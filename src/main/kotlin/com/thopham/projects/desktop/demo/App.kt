package com.thopham.projects.desktop.demo

import com.google.gson.Gson
import com.thopham.projects.desktop.demo.common.FxmlPaths
import javafx.application.Application
import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.web.WebView
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.util.Callback
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.ConfigurableApplicationContext
import java.awt.*
import java.io.IOException
import java.net.URL
import javax.swing.SwingUtilities

val GSON = Gson()

@SpringBootApplication
@EnableFeignClients
class App : Application() {

    @Throws(Exception::class)
    override fun init() {
        super.init()
        mAppContext = SpringApplication.run(App::class.java)
    }

    @Throws(Exception::class)
    override fun start(primaryStage: Stage) {
        Platform.setImplicitExit(false)
        mPrimaryStage = primaryStage
        getAppIcon()?.let{icon ->
            mPrimaryStage.icons.add(icon)
        }
        val loginView: Parent? = loadView(FxmlPaths.LOGIN)
        val scene = Scene(loginView)
        mPrimaryStage.scene = scene
        mPrimaryStage.title = "In tự động"
        mPrimaryStage.setOnCloseRequest {
            if(SystemTray.isSupported())
                showAlert("Thông báo", "Ứng dụng sẽ được chuyển vào khay hệ thống"){
                    mPrimaryStage.hide()
                    pushToSystemTray()
                }
            else
                showAlert("Thông báo", "Vì Khay hệ thống không được hỗ trợ. Ứng dụng sẽ bị đóng. Xin lỗi vì sự bất tiện này."){
                    mPrimaryStage.close()
                }
        }
        mPrimaryStage.show()
    }
    private fun pushToSystemTray(){
        SwingUtilities.invokeLater {
            val iconUrl = App::class.java.getResource("/images/icon.png")
            val appIconImage = Toolkit.getDefaultToolkit().getImage(iconUrl)

            val appTrayIcon = TrayIcon(appIconImage, "weSave In tự động").apply{
                isImageAutoSize = true
            }
            val systemTray = SystemTray.getSystemTray()
            val popup = PopupMenu()

            // Create a pop-up menu components
            val openApp = MenuItem("Open app").apply{
                addActionListener {
                    Platform.runLater {
                        mPrimaryStage.show()
                        systemTray.remove(appTrayIcon)
                    }
                }
            }
            val closeApp = MenuItem("Exit app").apply{
                addActionListener {
                    Platform.runLater {
                        mPrimaryStage.close()
                        systemTray.remove(appTrayIcon)
                    }
                }
            }
            //Add components to pop-up menu
            popup.add(openApp)
            popup.add(closeApp)

            appTrayIcon.popupMenu = popup
            appTrayIcon.addActionListener {
                Platform.runLater {
                    mPrimaryStage.show()
                    systemTray.remove(appTrayIcon)
                }
            }

            systemTray.add(appTrayIcon)
        }
    }
    private fun getAppIcon(): Image?{
        return try{
            Image(App::class.java.getResource("/images/icon.png").openStream())
        }
        catch (ignored: Exception){
            ignored.printStackTrace()
            null
        }
    }

    @Throws(Exception::class)
    override fun stop() {
        super.stop()
        mAppContext.close()
    }

    companion object {
        @JvmStatic
        private lateinit var mAppContext: ConfigurableApplicationContext
        @JvmStatic
        private lateinit var mPrimaryStage: Stage
        @JvmStatic
        fun main(args: Array<String>) {
            System.setProperty("java.awt.headless", "false")
            Toolkit.getDefaultToolkit()
            launch(App::class.java, *args)
        }

        @Throws(IOException::class)
        fun loadView(url: URL): Parent {
            try {
                val loader = FXMLLoader(url)
                loader.controllerFactory = Callback<Class<*>, Any> { mAppContext.getBean(it) }
                return loader.load()
            } catch (exception: Exception) {
                println("Đã xảy ra lỗi: ${exception.message}}")
            }
            return Label("http://xnxx.com")
        }

        @Throws(IOException::class)
        fun loadView(relativePath: String): Parent {
            return loadView(getUrl(relativePath))
        }

        fun getUrl(relativePath: String): URL {
            return App::class.java.getResource(relativePath)
        }

        fun navigateTo(view: Parent) {
            mPrimaryStage.scene = Scene(view)
        }

        @Throws(IOException::class)
        fun navigateTo(url: URL) {
            val view = loadView(url)
            navigateTo(view)
        }

        @Throws(IOException::class)
        fun navigateTo(relativePath: String) {
            navigateTo(getUrl(relativePath))
        }

        @Throws(IOException::class)
        fun showPopup(title: String, relativePath: String) {
            val view = loadView(relativePath)
            val stage = Stage()
            stage.title = title
            stage.initModality(Modality.APPLICATION_MODAL)
            stage.scene = Scene(view)
            stage.showAndWait()
        }

        fun showAlert(title: String, message: String, onClose: (() -> Unit) = {}) {
            val stage = Stage()
            stage.title = title
            stage.initModality(Modality.APPLICATION_MODAL)

            //Message
            val content = Label(message)
            //Confirm button
            val button = Button("Xác nhận")
            button.setOnMouseClicked { _ -> stage.hide() }
            //Container
            val box = VBox(18.0, content, button)
            box.padding = Insets(36.0, 36.0, 36.0, 36.0)
            box.alignment = Pos.CENTER

            //setup stage
            stage.scene = Scene(
                    box
            )
            stage.setOnHidden { onClose() }
            stage.showAndWait()
        }

        fun showDialog(webView: WebView) {
            val stage = Stage()
            stage.title = "DIALOG"
            stage.initModality(Modality.APPLICATION_MODAL)
            stage.scene = Scene(
                    webView
            )
            stage.showAndWait()
        }

        fun showConfirm(title: String, content: String, onConfirm: () -> Unit, onCancel: () -> Unit = {}) {
            val stage = Stage()
            stage.title = title
            stage.initModality(Modality.APPLICATION_MODAL)

            //Message
            val contentLabel = Label(content)
            //Confirm button
            val confirm = Button("Xác nhận")
            confirm.setOnMouseClicked {
                onConfirm()
                stage.close()
            }
            val cancel = Button("Hủy bỏ")
            cancel.setOnMouseClicked {
                onCancel()
                stage.close()
            }
            //Container
            val box = VBox(
                    36.0,
                contentLabel,
                HBox(
                        8.0,
                    confirm,
                    cancel
                ).apply{
                    alignment = Pos.CENTER
                }
            )
            box.padding = Insets(36.0, 36.0, 36.0, 36.0)
            box.alignment = Pos.CENTER

            //setup stage
            stage.scene = Scene(
                    box
            )
            stage.showAndWait()
        }
    }
}

