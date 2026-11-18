package classes

import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.EtatPartie

class Joueur(
    client: QuiEstCeClient,
    nom: String,
    prenom: String,
    id: Int,
    private val cle: String,
) : Identite(client, nom, prenom, id) {
    fun creerPartie(): Int {
        return obtenirClient().requeteCreationPartie(obtenirId(), cle)
    }

    fun rejoindrePartie(idPartie: Int): EtatPartie {
        return obtenirClient().requeteRejoindrePartie(idPartie, obtenirId(), cle)
    }

    fun choisirPersonnage(idPartie: Int, ligne: Int, colonne: Int): EtatPartie {
        return obtenirClient().requeteChoixPersonnage(idPartie, obtenirId(), cle, ligne, colonne)
    }

    fun poserQuestion(idPartie: Int, question: String): EtatPartie {
        return obtenirClient().requetePoserQuestion(idPartie, obtenirId(), cle, question)
    }

    fun donnerReponse(idPartie: Int, reponse: String): EtatPartie {
        return obtenirClient().requeteDonnerReponse(idPartie, obtenirId(), cle, reponse)
    }

    fun trouver(idPartie: Int, ligne: Int, colonne: Int): Boolean {
        return obtenirClient().requeteTrouve(idPartie, obtenirId(), cle, ligne, colonne)
    }
}