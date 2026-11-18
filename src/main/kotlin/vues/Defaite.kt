package vues

import Couleur
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import java.io.FileInputStream

class Defaite(val couleur: Couleur) : BorderPane() {

    val labelTitre = Label("OH NON !! TU AS PERDU !")
    val labelRejouer = Label("Tu veux rejouer ?")

    val btnNouvellePartie = Button("Nouvelle Partie")
    val btnQuitter = Button("Quitter")

    val vbox = VBox(20.0)
    val hbox = HBox(40.0)

    init {
        val boutonStyle: String

        if (couleur == Couleur.Rouge) {
            style = """
            -fx-background-image: url("file:src/main/kotlin/assets/def_vic1.png");
            -fx-background-size: cover;
            -fx-background-repeat: no-repeat;
            -fx-background-position: center;
            """.trimIndent()

            boutonStyle = """
            -fx-background-color: #d78e88;
            -fx-text-fill: white;
            -fx-font-size: 16px;
            -fx-padding: 10px 20px;
            """.trimIndent()
        } else {
            style = """
            -fx-background-image: url("file:src/main/kotlin/assets/def_vic2.png");
            -fx-background-size: cover;
            -fx-background-repeat: no-repeat;
            -fx-background-position: center;
            """.trimIndent()

            boutonStyle = """
            -fx-background-color: #7887bc;
            -fx-text-fill: white;
            -fx-font-size: 16px;
            -fx-padding: 10px 20px;
            """.trimIndent()
        }

        // Logo (en haut à gauche)
        val imageLogo = ImageView(Image(FileInputStream("src/main/kotlin/assets/logo2.png")))
        imageLogo.fitWidth = 100.0
        imageLogo.fitHeight = 100.0

        val topBox = HBox(imageLogo)
        topBox.alignment = Pos.TOP_LEFT
        topBox.padding = Insets(10.0)
        top = topBox


        val imageTriste = ImageView(Image(FileInputStream("src/main/kotlin/assets/img_1.png")))
        imageTriste.fitHeight = 230.0
        imageTriste.isPreserveRatio = true


        labelTitre.style = """
            -fx-font-size: 32px;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-font-family: 'Arial Black';
        """.trimIndent()

        labelRejouer.style = "-fx-font-size: 18px; -fx-text-fill: white;"

        btnNouvellePartie.style = boutonStyle
        btnQuitter.style = boutonStyle

        // Boutons
        hbox.children.addAll(btnNouvellePartie, btnQuitter)
        hbox.alignment = Pos.CENTER
        hbox.padding = Insets(10.0, 0.0, 0.0, 0.0)

        // Contenu vertical central
        vbox.children.addAll(imageTriste, labelTitre, labelRejouer, hbox)
        vbox.alignment = Pos.CENTER
        vbox.padding = Insets(20.0)

        // BorderPane
        center = vbox
        padding = Insets(15.0, 20.0, 10.0, 20.0)
    }
}