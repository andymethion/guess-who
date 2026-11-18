package controleurs

import Modele
import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import javafx.stage.Stage
import vues.SelectionServeur

class NouvellePartieControleur(private val stage: Stage, private val modele: Modele) :
    EventHandler<MouseEvent> {
    override fun handle(event: MouseEvent) {
        modele.definirAdversaire(null)
        modele.definirIdPartie(null)

        val serveurs = modele.obtenirServeurs()
        val selectionServeur = SelectionServeur(serveurs)

        selectionServeur.buttonCreateParty.onMouseClicked = CreerPartieControleur(stage, modele)

        for (spectate in selectionServeur.listeSpectate) {
            spectate.second.onMouseClicked = RegarderPartieControleur(stage, modele, spectate.first)
        }

        for (join in selectionServeur.listeJoin) {
            join.second.onMouseClicked = RejoindrePartieControleur(stage, modele, join.first)
        }

        stage.scene.root = selectionServeur
    }
}