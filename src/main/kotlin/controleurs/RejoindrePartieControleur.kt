package controleurs

import Couleur
import Modele
import classes.Adversaire
import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import javafx.stage.Stage
import vues.SelectionPersonnage

class RejoindrePartieControleur(private val stage: Stage, private val modele: Modele, private val idPartie: Int) :
    EventHandler<MouseEvent> {
    fun creerAdversaire() {
        val client = modele.obtenirClient()
        val etatPartie = client.requeteEtatPartie(idPartie)

        val idAdversaire = etatPartie.idJoueur1
        val infoAdversaire = client.requeteJoueur(idAdversaire)

        val adversaire = Adversaire(client, infoAdversaire.nom, infoAdversaire.prenom, idAdversaire)
        modele.definirAdversaire(adversaire)
    }

    fun definirGrilles() {
        val idPartie = modele.obtenirIdPartie()!!
        modele.obtenirAdversaire()!!.definirGrille(idPartie)
        modele.obtenirJoueur()!!.definirGrille(idPartie)
    }

    fun associerChoisirPersonnageControleur(selectionPersonnage: SelectionPersonnage) {
        for (btn in selectionPersonnage.list_btn) {
            btn.onMouseClicked = ChoisirPersonnageControleur(stage, selectionPersonnage, modele)
        }
    }

    override fun handle(event: MouseEvent) {
        modele.definirIdPartie(idPartie)
        modele.obtenirJoueur()!!.rejoindrePartie(idPartie)

        creerAdversaire()
        definirGrilles()

        val selectionPersonnage = SelectionPersonnage(modele.obtenirJoueur()!!.obtenirGrille()!!, Couleur.Bleu)
        associerChoisirPersonnageControleur(selectionPersonnage)

        stage.scene.root = selectionPersonnage
    }
}