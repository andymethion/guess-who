package controleurs

import Modele
import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import javafx.stage.Stage
import vues.Accueil

class AllerAccueilControleur(private val stage: Stage, private val modele: Modele) : EventHandler<MouseEvent> {
    override fun handle(event: MouseEvent) {
        val accueil = Accueil()

        accueil.btnRegles.onMouseClicked = AllerReglesControleur(stage, modele)
        accueil.btnValider.onMouseClicked = CreationJoueurControleur(stage, accueil, modele)

        stage.scene.root = accueil
    }
}