package classes

import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.Personnage

abstract class Identite(
    private val client: QuiEstCeClient,
    private val nom: String,
    private val prenom: String,
    private val id: Int,
) {
    private var grille: List<List<Personnage>>? = null

    fun obtenirClient(): QuiEstCeClient {
        return client
    }

    fun obtenirNom(): String {
        return nom
    }

    fun obtenirPrenom(): String {
        return prenom
    }

    fun obtenirId(): Int {
        return id
    }

    fun obtenirGrille(): List<List<Personnage>>? {
        return grille
    }

    fun definirGrille(idPartie: Int) {
        grille = client.requeteGrilleJoueur(idPartie, id)
    }
}