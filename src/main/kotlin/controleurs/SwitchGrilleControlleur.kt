package controleurs

import Modele
import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import javafx.stage.Stage
import vues.GrilleJeu
import vues.GrilleJeuReponse

class SwitchGrilleControlleur(private val stage: Stage, private var vue: Any, private val modele: Modele) :
    EventHandler<MouseEvent> {

    override fun handle(event: MouseEvent?) {
        if (vue is GrilleJeu) {
            val vueActuelle = vue as GrilleJeu

            val nouvelleVue = if (vueActuelle.buttonChangeGrille.text == "ALLER VERS MA GRILLE") {
                val g = GrilleJeu(modele.obtenirJoueur()?.obtenirGrille()!!, vueActuelle.couleur)
                g.buttonChangeGrille.text = "ALLER VERS LA GRILLE DE ${modele.obtenirAdversaire()?.obtenirPrenom()}"
                g
            } else {
                val g = GrilleJeu(modele.obtenirAdversaire()?.obtenirGrille()!!, vueActuelle.couleur)
                g.buttonChangeGrille.text = "ALLER VERS MA GRILLE"
                g
            }

            nouvelleVue.buttonChangeGrille.onMouseClicked = SwitchGrilleControlleur(stage, nouvelleVue, modele)
            nouvelleVue.buttonGuessPersonnage.onMouseClicked = GuessControleur(stage, nouvelleVue, modele)
            nouvelleVue.buttonEnvoiQuestion.onMouseClicked = PoserQuestionControleur(stage, nouvelleVue, modele)

            for (btn in nouvelleVue.list_btn) {
                btn.addEventHandler(MouseEvent.MOUSE_CLICKED, CarteGriseeControleur())
            }

            stage.scene.root = nouvelleVue
        }

        if (vue is GrilleJeuReponse) {
            val vueActuelle = vue as GrilleJeuReponse

            val nouvelleVue = if (vueActuelle.buttonChangeGrille.text == "ALLER VERS MA GRILLE") {
                val g = GrilleJeuReponse(modele.obtenirJoueur()?.obtenirGrille()!!, vueActuelle.couleur)
                g.buttonChangeGrille.text = "ALLER VERS LA GRILLE DE ${modele.obtenirAdversaire()?.obtenirPrenom()}"
                g
            } else {
                val g = GrilleJeuReponse(modele.obtenirAdversaire()?.obtenirGrille()!!, vueActuelle.couleur)
                g.buttonChangeGrille.text = "ALLER VERS MA GRILLE"
                g
            }

            nouvelleVue.buttonChangeGrille.onMouseClicked = SwitchGrilleControlleur(stage, nouvelleVue, modele)
            nouvelleVue.buttonOui.onMouseClicked = DonnerReponseControleur(stage, nouvelleVue, modele, "oui")
            nouvelleVue.buttonNon.onMouseClicked = DonnerReponseControleur(stage, nouvelleVue, modele, "non")

            for (btn in nouvelleVue.list_btn) {
                btn.addEventHandler(MouseEvent.MOUSE_CLICKED, CarteGriseeControleur())
            }

            val attendreQuestionControleur = AttendreQuestionControleur(stage, nouvelleVue, modele)
            attendreQuestionControleur.handle(null)

            val attenteDefaiteControleur = AttenteDefaiteControleur(stage, nouvelleVue, modele)
            attenteDefaiteControleur.handle(null)

            stage.scene.root = nouvelleVue
        }
    }
}
