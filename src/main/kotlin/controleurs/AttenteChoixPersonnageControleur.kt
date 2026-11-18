package controleurs

import Couleur
import Modele
import info.but1.sae2025.data.ETAPE
import info.but1.sae2025.data.Personnage
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import javafx.stage.Stage
import javafx.util.Duration
import vues.GrilleJeu
import vues.GrilleJeuReponse

class AttenteChoixPersonnageControleur(private val stage: Stage, private val modele: Modele) :
    EventHandler<ActionEvent> {
    fun allerGrilleJeu(grille: List<List<Personnage>>) {
        val grilleJeu = GrilleJeu(grille, Couleur.Rouge)

        grilleJeu.buttonChangeGrille.onMouseClicked = SwitchGrilleControlleur(stage, grilleJeu, modele)
        grilleJeu.buttonGuessPersonnage.onMouseClicked = GuessControleur(stage, grilleJeu, modele)
        grilleJeu.buttonEnvoiQuestion.onMouseClicked = PoserQuestionControleur(stage, grilleJeu, modele)

        for (btn in grilleJeu.list_btn) {
            btn.addEventHandler(MouseEvent.MOUSE_CLICKED, CarteGriseeControleur())
        }

        stage.scene.root = grilleJeu
    }

    fun allerGrilleJeuReponse(grille: List<List<Personnage>>) {
        val grilleJeuReponse = GrilleJeuReponse(grille, Couleur.Bleu)

        grilleJeuReponse.buttonChangeGrille.onMouseClicked = SwitchGrilleControlleur(stage, grilleJeuReponse, modele)
        grilleJeuReponse.buttonOui.onMouseClicked = DonnerReponseControleur(stage, grilleJeuReponse, modele, "oui")
        grilleJeuReponse.buttonNon.onMouseClicked = DonnerReponseControleur(stage, grilleJeuReponse, modele, "non")

        for (btn in grilleJeuReponse.list_btn) {
            btn.addEventHandler(MouseEvent.MOUSE_CLICKED, CarteGriseeControleur())
        }

        val attendreQuestionControleur = AttendreQuestionControleur(stage, grilleJeuReponse, modele)
        attendreQuestionControleur.handle(null)

        val attenteDefaiteControleur = AttenteDefaiteControleur(stage, grilleJeuReponse, modele)
        attenteDefaiteControleur.handle(null)

        stage.scene.root = grilleJeuReponse
    }

    fun obtenirKeyFrame(timeline: Timeline): KeyFrame {
        return KeyFrame(Duration.seconds(1.0), EventHandler {
            val idPartie = modele.obtenirIdPartie()!!
            val etatPartie = modele.obtenirClient().requeteEtatPartie(idPartie)

            if (etatPartie.etape == ETAPE.ATTENTE_QUESTION) {
                val adversaire = modele.obtenirAdversaire()!!
                val grilleAdversaire = adversaire.obtenirGrille()!!

                if (modele.obtenirJoueur()!!.obtenirId() == etatPartie.idJoueur1) {
                    allerGrilleJeu(grilleAdversaire)
                } else {
                    allerGrilleJeuReponse(grilleAdversaire)
                }

                timeline.stop()
            }
        })
    }

    override fun handle(event: ActionEvent?) {
        val timeline = Timeline()
        val keyFrame = obtenirKeyFrame(timeline)

        timeline.keyFrames.add(keyFrame)
        timeline.cycleCount = Timeline.INDEFINITE

        timeline.play()
    }
}