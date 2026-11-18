package controleurs

import Modele
import javafx.event.EventHandler
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import javafx.stage.Stage
import vues.GrilleJeu
import vues.GrilleJeuReponse
import vues.Victoire
import java.io.FileInputStream

class GuessControleur(private val stage: Stage, private val vue: GrilleJeu, private val modele: Modele) :
    EventHandler<MouseEvent> {

    fun allerGrilleJeuReponse() {
        val grille = modele.obtenirAdversaire()!!.obtenirGrille()!!
        val grilleJeuReponse = GrilleJeuReponse(grille, vue.couleur)

        grilleJeuReponse.buttonChangeGrille.onMouseClicked = SwitchGrilleControlleur(stage, grilleJeuReponse, modele)
        grilleJeuReponse.buttonOui.onMouseClicked = DonnerReponseControleur(stage, grilleJeuReponse, modele, "oui")
        grilleJeuReponse.buttonOui.onMouseClicked = DonnerReponseControleur(stage, grilleJeuReponse, modele, "non")

        for (btn in grilleJeuReponse.list_btn) {
            btn.addEventHandler(MouseEvent.MOUSE_CLICKED, CarteGriseeControleur())
        }

        val attendreQuestionControleur = AttendreQuestionControleur(stage, grilleJeuReponse, modele)
        attendreQuestionControleur.handle(null)

        stage.scene.root = grilleJeuReponse
    }

    override fun handle(event: MouseEvent) {
        val boutonClique = vue.btnselect ?: return
        val position = vue.btnPos[boutonClique] ?: return
        val (ligne, colonne) = position

        if (!confirmerGuess()) return

        modele.obtenirJoueur()?.let { joueur ->
            val idPartie = modele.obtenirIdPartie() ?: return
            val guessOk = joueur.trouver(idPartie, ligne, colonne)

            if (guessOk) {
                afficherInformation("BRAVO!!", "Tu as Guess le bon personnage !!")
                val victoire = Victoire(vue.couleur)
                victoire.btnNouvellePartie.onMouseClicked = NouvellePartieControleur(stage, modele)
                victoire.btnQuitter.onMouseClicked = QuitterApplicationControleur()
                stage.scene.root = victoire
            } else {
                afficherInformation("OH NON !!", "Tu as Guess le mauvais personnage.\nRéessaie plus tard.")
                allerGrilleJeuReponse()
            }
        }

        reinitialiserBoutonGuess()
    }

    private fun confirmerGuess(): Boolean {
        val dialog = Alert(Alert.AlertType.CONFIRMATION).apply {
            title = "CONFIRMATION"
            headerText = "Es-tu sur de vouloir Guess ce personnage ?"
        }
        val result = dialog.showAndWait()
        return result.isPresent && result.get() == ButtonType.OK
    }

    private fun afficherInformation(titre: String, message: String) {
        Alert(Alert.AlertType.INFORMATION).apply {
            title = titre
            headerText = message
        }.showAndWait()
    }

    private fun reinitialiserBoutonGuess() {
        vue.btnselect = null
        vue.buttonGuessPersonnage.graphic =
            ImageView(Image(FileInputStream("src/main/kotlin/assets/guess.png"))).apply {
                fitHeight = 24.0
                fitWidth = 24.0
            }
        vue.buttonGuessPersonnage.style = "-fx-background-color : #4B4B4B;"
    }
}
