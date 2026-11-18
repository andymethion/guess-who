package controleurs

import Modele
import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import javafx.stage.Stage
import vues.Regles

class AllerReglesControleur(private val stage: Stage, private val modele: Modele) : EventHandler<MouseEvent> {
    override fun handle(event: MouseEvent) {
        val regles = Regles()

        regles.boutonValider.onMouseClicked = AllerAccueilControleur(stage, modele)

        stage.scene.root = regles
    }
}