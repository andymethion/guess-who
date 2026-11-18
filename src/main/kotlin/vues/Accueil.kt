package vues

import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.Font
import java.io.FileInputStream

class Accueil : VBox(15.0) {
    val btnRegles = Button("Règles")
    val btnValider = Button("Valider")
    val prenomField = TextField()
    val nomField = TextField()

    init {
        alignment = Pos.TOP_CENTER
        style = "-fx-background-color: white;"

        val image = ImageView(Image(FileInputStream("src/main/kotlin/assets/logo2.png")))
        image.fitWidth = 340.0
        image.fitHeight = 340.0
        image.isPreserveRatio = true

        btnRegles.font = Font.font(16.0)
        btnRegles.textFill = Color.WHITE
        btnRegles.style = "-fx-background-color: #3730A3;"
        btnRegles.prefHeight = 50.0
        btnRegles.prefWidth = 90.0


        nomField.promptText = "Nom"
        nomField.maxWidth = 300.0
        nomField.prefHeight = 50.0
        nomField.style = """
            -fx-background-color: #3730A3;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-border-radius: 5px;
            -fx-background-radius: 5px;
        """.trimIndent()


        prenomField.promptText = "Prénom"
        prenomField.maxWidth = 300.0
        prenomField.prefHeight = 50.0
        prenomField.style = """
            -fx-background-color: #3730A3;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-border-radius: 5px;
            -fx-background-radius: 5px;
        """.trimIndent()

        btnValider.font = Font.font(16.0)
        btnValider.textFill = Color.WHITE
        btnValider.style = "-fx-background-color: #3730A3;"
        btnValider.prefHeight = 50.0
        btnValider.prefWidth = 90.0

        listOf(btnRegles, btnValider, prenomField, nomField).forEach {
            it.style += "-fx-background-radius: 8px;"
        }

        children.addAll(image, btnRegles, nomField, prenomField, btnValider)
    }
}