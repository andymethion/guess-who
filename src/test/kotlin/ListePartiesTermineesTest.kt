import info.but1.sae2025.QuiEstCeClient
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@TestInstance(PER_CLASS)
class ListePartiesTermineesTest {
    private lateinit var client: QuiEstCeClient
    private lateinit var process: Process

    @BeforeAll
    fun startServer() {
        val java = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java"
        val command = listOf(java, "-jar", "libs/server-all.jar", "localhost", "8080", "resources/")

        process = ProcessBuilder(command).inheritIO().start()
        process.waitFor()

        client = QuiEstCeClient("localhost", 8080)
    }

    @AfterAll
    fun destroyProcess() {
        process.destroy()
    }

    fun obtenirNomEtPrenom(): Pair<String, String> {
        val noms = File("src/main/kotlin/assets/names.txt").readLines()
        val prenoms = File("src/main/kotlin/assets/surnames.txt").readLines()

        val nom = noms.random().lowercase().replaceFirstChar { it.uppercase() }
        val prenom = prenoms.random().lowercase().replaceFirstChar { it.uppercase() }

        return Pair(nom, prenom)
    }

    @Test
    fun `Vérifie qu'il n'y a pas de doublon dans la liste des parties`() {
        val parties = client.requeteListePartiesTerminees()
        assertEquals(parties.distinct().size, parties.size)
    }

    @Test
    fun `Vérifie qu'une partie terminée est bien dans la liste des parties terminées`() {
        val (nom0, prenom0) = obtenirNomEtPrenom()
        val joueur0 = client.requeteCreationJoueur(nom0, prenom0)

        val (nom1, prenom1) = obtenirNomEtPrenom()
        val joueur1 = client.requeteCreationJoueur(nom1, prenom1)

        val idPartie = client.requeteCreationPartie(joueur0.id, joueur0.cle)

        client.requeteRejoindrePartie(idPartie, joueur1.id, joueur1.cle)

        client.requeteChoixPersonnage(idPartie, joueur0.id, joueur0.cle, 0, 0)
        client.requeteChoixPersonnage(idPartie, joueur1.id, joueur1.cle, 0, 0)

        client.requetePoserQuestion(idPartie, joueur0.id, joueur0.cle, "Est-ce que ton personnage a des lunettes ?")
        client.requeteDonnerReponse(idPartie, joueur1.id, joueur1.cle, "oui")
        client.requeteTrouve(idPartie, joueur0.id, joueur0.cle, 0, 0)

        val partiesTerminees = client.requeteListePartiesTerminees()

        assertTrue(partiesTerminees.contains(idPartie))
    }
}