package controleurs

import Modele
import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import javafx.stage.Stage
import vues.AttenteAdversaire

class CreerPartieControleur(private val stage: Stage, private val modele: Modele) : EventHandler<MouseEvent> {
    fun creerPartieEtDefinirIdPartie() {
        val idPartie = modele.obtenirJoueur()!!.creerPartie()
        modele.definirIdPartie(idPartie)
    }

    fun allerAttenteAdversaire() {
        stage.scene.root = AttenteAdversaire()
        val attenteAdversaireControleur = AttenteAdversaireControleur(stage, modele)
        attenteAdversaireControleur.handle(null)
    }

    override fun handle(event: MouseEvent) {
        creerPartieEtDefinirIdPartie()
        allerAttenteAdversaire()
    }
}