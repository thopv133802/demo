package com.thopham.projects.desktop.demo.customViews

import javafx.scene.control.Button


fun MaterialButton(content: String, textColor:String = Colors.BUTTON_TEXT_FILL, backgroundColor:String = Colors.BUTTON_BACKGROUND_COLOR): Button{
    val button = Button(content)
    //button.style += "-fx-background-color: $backgroundColor; -fx-text-fill: $textColor"
    return button
}