package vues

import Couleur
import info.but1.sae2025.data.Personnage
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.control.Tooltip
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.*
import java.io.FileInputStream

class GrilleJeu(listePersonnage: List<List<Personnage>>, val couleur: Couleur) : VBox() {
    val list_btn = mutableListOf<Button>()
    val btnPos = mutableMapOf<Button, Pair<Int, Int>>()
    val listPerso = listePersonnage // PLACEHOLDER
    val buttonGuessPersonnage = Button()
    val buttonEnvoiQuestion = Button()
    var btnselect: Button? = null
    val questionBar = TextField()
    val buttonChangeGrille =
        Button("ALLER VERS MA GRILLE") // si grille actuelle est grille du joueur adversaire sinon afficher "Grille de Prénom de l'adversaire"

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
//        val question = Label("Question: Exemple de question à but PLACEHOLDER ?")
//        question.textFill = Color.WHITE
//        val buttonOui = Button("OUI")
//        val buttonNon = Button("NON")
//        bottomPart.children.addAll(question, buttonOui, buttonNon)


//         si le joueur pose la question
        questionBar.promptText = "Veuillez poser une question"
        questionBar.maxWidth = 400.0
        questionBar.prefHeight = 50.0
        HBox.setHgrow(questionBar, Priority.ALWAYS)
        questionBar.style = """
        -fx-font-size: 14px;
        -fx-border-radius: 5px;
        -fx-background-radius: 5px;
        """.trimIndent()

//        val buttonGuessPersonnage = Button()
        val iconGuess = ImageView(Image(FileInputStream("src/main/kotlin/assets/guess.png")))
        val iconGuess2 = ImageView(Image(FileInputStream("src/main/kotlin/assets/guess2.png")))
        iconGuess.fitHeight = 24.0
        iconGuess.fitWidth = 24.0
        iconGuess2.fitHeight = 24.0
        iconGuess2.fitWidth = 24.0
        buttonGuessPersonnage.graphic = iconGuess
        buttonGuessPersonnage.style = "-fx-background-color : #4B4B4B;"
        buttonGuessPersonnage.prefHeight = 50.0
        list_btn.forEach { btn ->
            btn.setOnMouseClicked {
                btnselect = btn
                buttonGuessPersonnage.graphic = iconGuess2
                buttonGuessPersonnage.style = "-fx-background-color : #60C467;"
                buttonGuessPersonnage.prefHeight = 50.0
            }
        }

//        val buttonEnvoiQuestion = Button()
        val iconCheck = ImageView(Image(FileInputStream("src/main/kotlin/assets/check.png")))
        iconCheck.fitHeight = 24.0
        iconCheck.fitWidth = 24.0
        buttonEnvoiQuestion.graphic = iconCheck
        buttonEnvoiQuestion.style = "-fx-background-color: #16A34A;"
        buttonEnvoiQuestion.prefHeight = 50.0

        bottomPart.children.addAll(questionBar, buttonGuessPersonnage, buttonEnvoiQuestion)

        grid.alignment = Pos.CENTER
        this.children.addAll(grid)
        this.children.add(bottomPart)
    }
}