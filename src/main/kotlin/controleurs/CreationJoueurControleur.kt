package controleurs

import Modele
import classes.Joueur
import info.but1.sae2025.data.IdentificationJoueur
import info.but1.sae2025.exceptions.QuiEstCeException
import javafx.event.EventHandler
import javafx.scene.control.Alert
import javafx.scene.input.MouseEvent
import javafx.stage.Stage
import vues.Accueil
import vues.SelectionServeur

class CreationJoueurControleur(private val stage: Stage, private val vue: Accueil, private val modele: Modele) :
    EventHandler<MouseEvent> {
    fun obtenirNomEtPrenom(): Pair<String, String> {
        val nom = vue.nomField.text!!
        val prenom = vue.prenomField.text!!
        return Pair(nom, prenom)
    }

    fun estStringValide(string: String): Boolean {
        if (string.isBlank()) return false
        if (string.startsWith(" ") || string.endsWith(" ")) return false
        if (string.contains("  ")) return false
        return true
    }

    fun estNomEtPrenomValides(nom: String, prenom: String): Boolean {
        val nomValide = estStringValide(nom)
        val prenomValide = estStringValide(prenom)

        if (!nomValide && !prenomValide) {
            val alert = Alert(Alert.AlertType.ERROR, "Votre nom et votre prénom ne sont pas valides !")
            alert.show()
            return false
        }

        if (!nomValide) {
            val alert = Alert(Alert.AlertType.ERROR, "Votre nom n'est pas valide !")
            alert.show()
            return false
        }

        if (!prenomValide) {
            val alert = Alert(Alert.AlertType.ERROR, "Votre prénom n'est pas valide !")
            alert.show()
            return false
        }

        return true
    }

    fun obtenirIdentificationJoueur(nom: String, prenom: String): IdentificationJoueur? {
        val identificationJoueur: IdentificationJoueur

        try {
            identificationJoueur = modele.obtenirClient().requeteCreationJoueur(nom, prenom)
        } catch (exception: QuiEstCeException) {
            val alert = Alert(Alert.AlertType.ERROR, "Cette combinaison nom / prénom existe déjà !\n$exception")
            alert.show()
            return null
        }

        return identificationJoueur
    }

    fun associerRegarderPartieControleur(selectionServeur: SelectionServeur) {
        for (spectate in selectionServeur.listeSpectate) {
            spectate.second.onMouseClicked = RegarderPartieControleur(stage, modele, spectate.first)
        }
    }

    fun associerRejoindrePartieControleur(selectionServeur: SelectionServeur) {
        for (join in selectionServeur.listeJoin) {
            join.second.onMouseClicked = RejoindrePartieControleur(stage, modele, join.first)
        }
    }

    override fun handle(event: MouseEvent) {
        val (nom, prenom) = obtenirNomEtPrenom()
        val nomEtPrenomValides = estNomEtPrenomValides(nom, prenom)
        if (!nomEtPrenomValides) return

        val identificationJoueur = obtenirIdentificationJoueur(nom, prenom)
        if (identificationJoueur == null) return

        val joueur = Joueur(modele.obtenirClient(), nom, prenom, identificationJoueur.id, identificationJoueur.cle)
        modele.definirJoueur(joueur)

        val serveurs = modele.obtenirServeurs()
        val selectionServeur = SelectionServeur(serveurs)

        selectionServeur.buttonCreateParty.onMouseClicked = CreerPartieControleur(stage, modele)
        associerRegarderPartieControleur(selectionServeur)
        associerRejoindrePartieControleur(selectionServeur)

        stage.scene.root = selectionServeur
    }
}