package controleurs

import Modele
import info.but1.sae2025.data.ETAPE
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import javafx.stage.Stage
import javafx.util.Duration
import vues.GrilleJeuReponse

class AttendreQuestionControleur(
    private val stage: Stage,
    private val vue: GrilleJeuReponse,
    private val modele: Modele,
) : EventHandler<ActionEvent> {
    fun actualiserGrilleJeuReponse(question: String) {
        val grille = modele.obtenirAdversaire()!!.obtenirGrille()!!
        val grilleJeuReponse = GrilleJeuReponse(grille, vue.couleur, question)

        grilleJeuReponse.buttonChangeGrille.onMouseClicked = SwitchGrilleControlleur(stage, grilleJeuReponse, modele)
        grilleJeuReponse.buttonOui.onMouseClicked = DonnerReponseControleur(stage, grilleJeuReponse, modele, "oui")
        grilleJeuReponse.buttonNon.onMouseClicked = DonnerReponseControleur(stage, grilleJeuReponse, modele, "non")

        for (btn in grilleJeuReponse.list_btn) {
            btn.addEventHandler(MouseEvent.MOUSE_CLICKED, CarteGriseeControleur())
        }

        val attenteDefaiteControleur = AttenteDefaiteControleur(stage, grilleJeuReponse, modele)
        attenteDefaiteControleur.handle(null)

        stage.scene.root = grilleJeuReponse
    }

    fun obtenirKeyFrame(timeline: Timeline): KeyFrame {
        return KeyFrame(Duration.seconds(1.0), EventHandler {
            val idPartie = modele.obtenirIdPartie()!!
            val etatPartie = modele.obtenirClient().requeteEtatPartie(idPartie)

            if (etatPartie.etape == ETAPE.TERMINEE) {
                timeline.stop()
            }

            if (etatPartie.etape != ETAPE.TERMINEE && etatPartie.etape == ETAPE.ATTENTE_REPONSE) {
                actualiserGrilleJeuReponse(etatPartie.questionCourante)
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