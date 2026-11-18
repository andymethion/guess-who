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
import javafx.scene.layout.*
import javafx.scene.paint.Color
import java.io.FileInputStream

class GrilleJeuReponse(
    listePersonnage: List<List<Personnage>>,
    val couleur: Couleur,
    private val question: String = "Aucune question pour le moment..."
) : VBox() {
    val list_btn = mutableListOf<Button>()
    val btnPos = mutableMapOf<Button, Pair<Int, Int>>()
    val listPerso = listePersonnage
    val buttonOui = Button("OUI")
    val buttonNon = Button("NON")
    val buttonChangeGrille =
        Button("ALLER VERS MA GRILLE") // si grille actuelle est grille de joueur1 alors afficher "Grille de Joueur2", sinon afficher "MA GRILLE"

    init {
        if (couleur == Couleur.Rouge) {
            this.style = "-fx-background-color: #AE1E12;"
        } else {
            this.style = "-fx-background-color: #3730A3;"
        }
        alignment = Pos.TOP_CENTER


        val topPart = HBox(0.0)
        topPart.alignment = Pos.CENTER_LEFT
        topPart.padding = Insets(0.0, 20.0, 0.0, 20.0)

        val logo = ImageView(Image(FileInputStream("src/main/kotlin/assets/logo2.png")))
        logo.fitHeight = 80.0
        logo.isPreserveRatio = true

        val spacer = Region()
        HBox.setHgrow(spacer, Priority.ALWAYS)  // Pousse les boutons à droite

        buttonChangeGrille.style = "-fx-background-color: #DC2626; -fx-text-fill: white;"

        topPart.children.addAll(logo, spacer, buttonChangeGrille)
        this.children.add(topPart)

        //val textChoixPerso = Label("")
        //textChoixPerso.textFill = Color.WHITE
        //textChoixPerso.padding = Insets(15.0, 0.0, 15.0, 0.0)

        val grid = GridPane(6.0, 4.0)
//         Version avec grille en INPUT/paramètre
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

        val bottomPart = HBox(10.0)
        bottomPart.alignment = Pos.CENTER
        bottomPart.padding = Insets(10.0)

        // Si la question est posé au joueur
        val questionText = "Question : " + question
        val questionLabel = Label(questionText)
        questionLabel.textFill = Color.WHITE

        bottomPart.children.addAll(questionLabel, buttonOui, buttonNon)

        grid.alignment = Pos.CENTER
        this.children.addAll(grid)
        this.children.add(bottomPart)
    }
}