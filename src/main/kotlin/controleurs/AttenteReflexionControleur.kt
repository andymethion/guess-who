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
import vues.GrilleJeu
import vues.GrilleJeuReponse

class AttenteReflexionControleur(
    private val stage: Stage,
    private val vue: GrilleJeuReponse,
    private val modele: Modele
) : EventHandler<ActionEvent> {
    fun allerGrilleJeu() {
        val grille = modele.obtenirAdversaire()!!.obtenirGrille()!!
        val grilleJeu = GrilleJeu(grille, vue.couleur)

        grilleJeu.buttonChangeGrille.onMouseClicked = SwitchGrilleControlleur(stage, grilleJeu, modele)
        grilleJeu.buttonGuessPersonnage.onMouseClicked = GuessControleur(stage, grilleJeu, modele)
        grilleJeu.buttonEnvoiQuestion.onMouseClicked = PoserQuestionControleur(stage, grilleJeu, modele)

        for (btn in grilleJeu.list_btn) {
            btn.addEventHandler(MouseEvent.MOUSE_CLICKED, CarteGriseeControleur())
        }

        stage.scene.root = grilleJeu
    }

    fun obtenirKeyFrame(timeline: Timeline): KeyFrame {
        return KeyFrame(Duration.seconds(1.0), EventHandler {
            val idPartie = modele.obtenirIdPartie()!!
            val etatPartie = modele.obtenirClient().requeteEtatPartie(idPartie)

            if (etatPartie.etape == ETAPE.TERMINEE) {
                timeline.stop()
            }

            if (etatPartie.etape != ETAPE.TERMINEE && etatPartie.etape != ETAPE.ATTENTE_REFLEXION) {
                allerGrilleJeu()
                timeline.stop()
            }
        })
    }

    fun demarrerTimeline() {
        val timeline = Timeline()
        val keyFrame = obtenirKeyFrame(timeline)

        timeline.cycleCount = Timeline.INDEFINITE
        timeline.keyFrames.add(keyFrame)

        timeline.play()
    }

    override fun handle(event: ActionEvent?) {
        demarrerTimeline()
    }
}