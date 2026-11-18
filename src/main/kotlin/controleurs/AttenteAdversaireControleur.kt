package controleurs

import Couleur
import Modele
import classes.Adversaire
import info.but1.sae2025.data.ETAPE
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.stage.Stage
import javafx.util.Duration
import vues.SelectionPersonnage

class AttenteAdversaireControleur(private val stage: Stage, private val modele: Modele) : EventHandler<ActionEvent> {
    fun creerAdversaire(idJoueur: Int) {
        val client = modele.obtenirClient()
        val joueur = client.requeteJoueur(idJoueur)
        val adversaire = Adversaire(client, joueur.nom, joueur.prenom, idJoueur)
        modele.definirAdversaire(adversaire)
    }

    fun definirGrilles() {
        val idPartie = modele.obtenirIdPartie()!!
        modele.obtenirJoueur()!!.definirGrille(idPartie)
        modele.obtenirAdversaire()!!.definirGrille(idPartie)
    }

    fun allerSelectionPersonnage() {
        val selectionPersonnage = SelectionPersonnage(modele.obtenirJoueur()!!.obtenirGrille()!!, Couleur.Rouge)

        for (btn in selectionPersonnage.list_btn) {
            btn.onMouseClicked = ChoisirPersonnageControleur(stage, selectionPersonnage, modele)
        }

        stage.scene.root = selectionPersonnage
    }

    fun obtenirKeyFrame(timeline: Timeline): KeyFrame {
        return KeyFrame(Duration.seconds(1.0), EventHandler {
            val idPartie = modele.obtenirIdPartie()!!
            val etatPartie = modele.obtenirClient().requeteEtatPartie(idPartie)

            if (etatPartie.etape == ETAPE.INITIALISATION) {
                creerAdversaire(etatPartie.idJoueur2)
                definirGrilles()

                allerSelectionPersonnage()
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