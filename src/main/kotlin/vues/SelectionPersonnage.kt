package vues

import Couleur
import info.but1.sae2025.data.Personnage
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.Tooltip
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.GridPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import java.io.FileInputStream

class SelectionPersonnage(listePersonnage: List<List<Personnage>>, private val couleur: Couleur) : VBox() {
    val list_btn = mutableListOf<Button>()
    val btnPos = mutableMapOf<Button, Pair<Int, Int>>()
    val listPerso = listePersonnage

    init {
        if (couleur == Couleur.Rouge) {
            this.style = "-fx-background-color: #AE1E12;"
        } else {
            this.style = "-fx-background-color: #3730A3;"
        }
        alignment = Pos.TOP_CENTER
        val textChoixPerso = Label("Veuillez choisir un personnage")
        textChoixPerso.textFill = Color.WHITE
        textChoixPerso.padding = Insets(20.0)

        val grid = GridPane(6.0, 4.0)
        for (i in 0 until listPerso.size) {
            for (j in 0 until listPerso[i].size) {
                var btnPerso = Button()
                val path = "resources/but1/${listPerso[i][j].url}"
                val ele = ImageView(Image(FileInputStream(path)))

                ele.fitWidth = 80.0
                ele.fitHeight = 120.0
                btnPerso.graphic = ele
                val prenom = listPerso[i][j].prenom
                val nom = listPerso[i][j].nom
                btnPerso.tooltip = Tooltip("$prenom $nom")
                list_btn.add(btnPerso)
                btnPos[btnPerso] = Pair(i, j)
                grid.add(btnPerso, j, i)
            }
        }
        grid.alignment = Pos.CENTER
        this.children.addAll(textChoixPerso, grid)

//        list_btn.forEach { btn ->
//            btn.onMouseClicked = ChoisirPersonnageControleur(stage, vue, modele)
//        }
    }
}
