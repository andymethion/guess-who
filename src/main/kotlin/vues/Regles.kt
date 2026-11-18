package vues

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.Font
import java.io.FileInputStream

class Regles() : VBox(20.0) {
    val boutonValider = Button("Valider")

    init {
        alignment = Pos.TOP_CENTER
        padding = Insets(20.0)
        style = "-fx-background-color: white;"

        // Image en haut
        val imageView = ImageView(Image(FileInputStream("src/main/kotlin/assets/logo.png")))
        imageView.fitWidth = 170.0
        imageView.fitHeight = 170.0
        imageView.isPreserveRatio = true

        // "Bouton" style bouton Règles mais inactif
        val fauxBouton = Label("Règles")
        fauxBouton.prefWidth = 100.0
        fauxBouton.prefHeight = 40.0
        fauxBouton.alignment = Pos.CENTER
        fauxBouton.font = Font.font(16.0)
        fauxBouton.textFill = Color.WHITE
        fauxBouton.style = "-fx-background-color: #3730A3; -fx-background-radius: 8px;"

        // Zone de texte avec ScrollPane
        val texte = Label(
            """Règles du jeu Qui est-ce ?

        1. Préparation
        
        - Chaque joueur prend un plateau avec 24 personnages.
        - Chacun pioche une carte jaune (personnage mystère) et la place devant 
            soi sans la montrer à l’adversaire.
        - Les autres cartes sont posées à côté du plateau.


        2. Déroulement de la partie
        
        - Le joueur le plus jeune commence.
        - À tour de rôle, chaque joueur pose une question à laquelle l’autre répond 
            par "Oui" ou "Non".
        - Exemples de questions :
        - "Est-ce que ton personnage porte des lunettes ?"
        - "Est-ce que ton personnage a les cheveux blonds ?"
        - "Est-ce que ton personnage est un homme ?"
        - Selon la réponse, abaissez les cartes qui ne correspondent pas à la caractéristique 
            demandée :
        - Réponse OUI : abaissez les personnages qui n’ont pas la caractéristique.
        - Réponse NON : abaissez les personnages qui ont la caractéristique.


        3. Deviner le personnage mystère
        
        - Quand vous pensez avoir trouvé, annoncez le nom du personnage à votre tour.
        - Si c’est le bon, vous gagnez la partie.
        - Si vous vous trompez, vous perdez la partie (ou l’adversaire continue 
            selon les variantes).


        4. Fin de la partie
        
        - La partie s’achève dès qu’un joueur devine le personnage mystère de l’adversaire.
        - Pour rejouer, mélangez les cartes jaunes et piochez-en une nouvelle 
            pour chaque joueur.

        Conseils
        - Posez des questions qui divisent le nombre de personnages restants.
        - Vous pouvez jouer à plusieurs ou ajouter des variantes."""
        )
        texte.isWrapText = true
        texte.style = "-fx-text-fill: black;"
        texte.alignment = Pos.TOP_CENTER
//        texte.padding =

        val scrollPane = ScrollPane(texte)
        scrollPane.prefWidth = 270.0
        scrollPane.prefHeight = 355.0
        scrollPane.vbarPolicy = ScrollPane.ScrollBarPolicy.AS_NEEDED
        scrollPane.hbarPolicy = ScrollPane.ScrollBarPolicy.NEVER
        scrollPane.style = "-fx-background: white; -fx-border-color: lightgray;"
        scrollPane.style = "-fx-background-radius: 8px;"
        scrollPane.isFitToWidth = true

        // Bouton Valider en bas à droite de la scrollbox

        boutonValider.prefWidth = 90.0
        boutonValider.minHeight = 50.0
        boutonValider.textFill = Color.WHITE
        boutonValider.style = "-fx-background-color: #3730A3; -fx-font-size: 14px;"
        boutonValider.style += "-fx-background-radius: 8px;"

        // Conteneur pour scroll + bouton valider en bas à droite
        val boxAvecValider = StackPane()
        val innerVBox = VBox()
        innerVBox.children.addAll(scrollPane)

        val boutonContainer = HBox()
        boutonContainer.alignment = Pos.BOTTOM_RIGHT
        boutonContainer.children.add(boutonValider)
        boutonContainer.padding = Insets(10.0)

        val scrollBoxContainer = VBox(0.0, innerVBox, boutonContainer)
        scrollBoxContainer.style = "-fx-background-color: white;"
        scrollBoxContainer.prefWidth = 350.0
        scrollBoxContainer.prefHeight = 270.0

        listOf(fauxBouton, boxAvecValider, scrollBoxContainer, scrollBoxContainer).forEach {
            it.style += "-fx-background-radius: 8px;"
        }

        children.addAll(imageView, fauxBouton, scrollBoxContainer)
    }
}
