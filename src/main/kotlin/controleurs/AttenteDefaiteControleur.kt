package controleurs

import Modele
import info.but1.sae2025.data.ETAPE
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.stage.Stage
import javafx.util.Duration
import vues.Defaite
import vues.GrilleJeuReponse

class AttenteDefaiteControleur(
    private val stage: Stage,
    private val vue: GrilleJeuReponse,
    private val modele: Modele
) : EventHandler<ActionEvent> {
    fun allerDefaite() {
        val defaite = Defaite(vue.couleur)

        defaite.btnNouvellePartie.onMouseClicked = NouvellePartieControleur(stage, modele)
        defaite.btnQuitter.onMouseClicked = QuitterApplicationControleur()

        stage.scene.root = defaite
    }

    fun obtenirKeyFrame(timeline: Timeline): KeyFrame {
        return KeyFrame(Duration.seconds(1.0), EventHandler {
            val idPartie = modele.obtenirIdPartie()!!
            val etatPartie = modele.obtenirClient().requeteEtatPartie(idPartie)

            if (etatPartie.etape == ETAPE.TERMINEE) {
                allerDefaite()
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