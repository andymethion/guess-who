package controleurs

import Modele
import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import javafx.stage.Stage
import vues.GrilleJeuReponse

class DonnerReponseControleur(
    private val stage: Stage,
    private val vue: GrilleJeuReponse,
    private val modele: Modele,
    private val reponse: String
) : EventHandler<MouseEvent> {
    override fun handle(event: MouseEvent) {
        val idPartie = modele.obtenirIdPartie()!!
        modele.obtenirJoueur()!!.donnerReponse(idPartie, reponse)

        val attendreReflexionControleur = AttenteReflexionControleur(stage, vue, modele)
        attendreReflexionControleur.handle(null)
    }
}