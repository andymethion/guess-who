package controleurs

import Modele
import info.but1.sae2025.data.ETAPE
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.event.EventHandler
import javafx.scene.control.Alert
import javafx.scene.input.MouseEvent
import javafx.stage.Stage
import javafx.util.Duration
import vues.GrilleJeu

class PoserQuestionControleur(private val stage: Stage, private val vue: GrilleJeu, private val modele: Modele) :
    EventHandler<MouseEvent> {
    fun afficherDialogConfirmationQuestion(question: String) {
        val dialog = Alert(Alert.AlertType.INFORMATION)
        dialog.title = "Question envoyée !"
        dialog.headerText =
            "La question \"$question\" a bien été envoyée.\nEn attente de la réponse de l'adversaire..."
        dialog.show()
    }

    fun afficherDialogConfirmationReponse(question: String, reponse: String) {
        val dialog = Alert(Alert.AlertType.CONFIRMATION)
        dialog.title = "Réponse reçue !"
        dialog.headerText =
            "La réponse à la question \"$question\" est \"$reponse\".\nTu peux essayer de trouver son personnage !"
        dialog.show()
    }

    fun obtenirKeyFrame(timeline: Timeline, question: String): KeyFrame {
        return KeyFrame(Duration.seconds(1.0), EventHandler {
            val idPartie = modele.obtenirIdPartie()!!
            val etatPartie = modele.obtenirClient().requeteEtatPartie(idPartie)

            if (etatPartie.etape == ETAPE.TERMINEE) {
                timeline.stop()
            }

            if (etatPartie.etape != ETAPE.TERMINEE && etatPartie.etape != ETAPE.ATTENTE_REPONSE) {
                val reponse = etatPartie.reponseCourante
                afficherDialogConfirmationReponse(question, reponse)
                timeline.stop()
            }
        })
    }

    fun lancerTimelineAttenteResponse(question: String) {
        val timeline = Timeline()
        val keyFrame = obtenirKeyFrame(timeline, question)
        timeline.keyFrames.add(keyFrame)
        timeline.cycleCount = Timeline.INDEFINITE
        timeline.play()
    }

    override fun handle(event: MouseEvent) {
        val idPartie = modele.obtenirIdPartie()!!
        val question = vue.questionBar.text
        modele.obtenirJoueur()!!.poserQuestion(idPartie, question)

        afficherDialogConfirmationQuestion(question)
        lancerTimelineAttenteResponse(question)
    }
}