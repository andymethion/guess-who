package vues

import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.Font
import java.io.FileInputStream

class AttenteChoixPerso : VBox(15.0) {
    val label = Label("En attente du choix de l'adversaire ...")

    init {
        alignment = Pos.TOP_CENTER
        style = "-fx-background-color: white;"

        val image = ImageView(Image(FileInputStream("src/main/kotlin/assets/logo2.png")))
        image.fitWidth = 340.0
        image.fitHeight = 340.0
        image.isPreserveRatio = true


        label.font = Font.font("Itim",20.0)
        label.textFill = Color.WHITE
        label.style = "-fx-background-color: #3730A3; -fx-background-radius : 8px;-fx-padding: 20px;"
        label.prefHeight = 50.0
        label.prefWidth = 420.0

        children.addAll(image, label)
    }
}