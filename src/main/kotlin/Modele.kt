import classes.Adversaire
import classes.Joueur
import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.EtatPartie

class Modele {
    private val client = QuiEstCeClient("172.26.69.145", 8080)
    private var joueur: Joueur? = null
    private var adversaire: Adversaire? = null
    private var idPartie: Int? = null

    // GETTERS

    fun obtenirClient(): QuiEstCeClient {
        return client
    }

    fun obtenirJoueur(): Joueur? {
        return joueur
    }

    fun obtenirAdversaire(): Adversaire? {
        return adversaire
    }

    fun obtenirIdPartie(): Int? {
        return idPartie
    }

    // SETTERS

    fun definirJoueur(joueur: Joueur) {
        this.joueur = joueur
    }

    fun definirAdversaire(adversaire: Adversaire?) {
        this.adversaire = adversaire
    }

    fun definirIdPartie(idPartie: Int?) {
        this.idPartie = idPartie
    }

    // METHODS

    fun obtenirServeurs(): MutableList<Triple<IdPartie, Nom, Prenom>> {
        val partiesCreees = client.requeteListePartiesCreees()

        val mutableList = mutableListOf<Triple<IdPartie, Nom, Prenom>>()

        val threads = partiesCreees.map { it ->
            Thread {
                val etatPartie = client.requeteEtatPartie(it)
                val joueur = client.requeteJoueur(etatPartie.idJoueur1)
                mutableList.add(Triple(it, joueur.nom, joueur.prenom))
            }
        }

        threads.forEach {
            it.start()
        }

        threads.forEach {
            it.join()
        }

        return mutableList
    }
}