package vues

import IdPartie
import Nom
import Prenom
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.*
import javafx.scene.paint.Color
import java.io.FileInputStream

class SelectionServeur(private val serveurs: MutableList<Triple<IdPartie, Nom, Prenom>>) : VBox() {
    val buttonCreateParty = Button("Créer une partie")
    var listeSpectate=listOf<Pair<IdPartie, Button>>()
    var listeJoin=listOf<Pair<IdPartie, Button>>()

    init {
        alignment = Pos.CENTER

        val image = ImageView(Image(FileInputStream("src/main/kotlin/assets/logo.png")))
        image.fitWidth = 250.0
        image.fitHeight = 250.0
        image.isPreserveRatio = true

        val contenuVBox = VBox(10.0)
        contenuVBox.padding = Insets(10.0)
        contenuVBox.style = "-fx-background-color: #FFFFFF;"
        contenuVBox.prefWidth = 450.0
        contenuVBox.maxWidth = Double.MAX_VALUE

        // Version avec boucle sur liste
        if(serveurs.isEmpty()){
            val msgNoServer = Label("Aucune partie créée")
            contenuVBox.alignment = Pos.CENTER
            contenuVBox.children.add(msgNoServer)
        } else {
            for (i in 0 until serveurs.size){
                val hbox = HBox(10.0)
                hbox.prefHeight = 50.0
                hbox.maxWidth = Double.MAX_VALUE
                hbox.alignment = Pos.CENTER_LEFT
                hbox.style = "-fx-background-color: #3730A3; -fx-background-radius: 8px;"
                hbox.padding = Insets(5.0, 10.0, 5.0, 10.0)

                val label = Label("Serveur de ${serveurs[i].second} ${serveurs[i].third}")
                label.style = "-fx-text-fill: white;"  // Texte blanc

                val spacer = Region()
                HBox.setHgrow(spacer, Priority.ALWAYS)  // Pousse les boutons à droite

                val btnMilieu = Button("Spec")
                btnMilieu.prefHeight = 50.0
                btnMilieu.prefWidth = 50.0
                btnMilieu.style = "-fx-background-color: #767676"
                btnMilieu.textFill = Color.WHITE

                val btnDroite = Button("Play")
                btnDroite.prefHeight = 50.0
                btnDroite.prefWidth = 50.0
                btnDroite.style = "-fx-background-color: #60C467"
                btnDroite.textFill = Color.WHITE

                listeSpectate = listeSpectate.plus(Pair(serveurs[i].first, btnMilieu))
                listeJoin = listeJoin.plus(Pair(serveurs[i].first, btnDroite))
                hbox.children.addAll(label, spacer, btnMilieu, btnDroite)
                contenuVBox.children.add(hbox)
            }
        }

        val scrollPane = ScrollPane(contenuVBox)
        scrollPane.prefWidth = 450.0
        scrollPane.prefHeight = 200.0
        scrollPane.isFitToWidth = true  // ✅ indispensable pour que les HBox s'étendent
        scrollPane.style = "-fx-background: white;"



        children.addAll(image, scrollPane, buttonCreateParty)
    }
}
