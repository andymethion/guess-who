import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.ETAPE
import info.but1.sae2025.exceptions.QuiEstCeException
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.api.assertThrows
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFalse


@TestInstance(PER_CLASS)
class EtatPartieTest {
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


    //Test requeteEtatPartie

    @Test
    fun testEtatPartie1() {
        // Crée un joueur et une partie. Vérifie que l'ID du joueur 2 n'est pas encore défini.
        val (nom1, prenom1) = obtenirNomEtPrenom()
        val joueur1 = client.requeteCreationJoueur(nom1, prenom1)

        val (nom2, prenom2) = obtenirNomEtPrenom()
        val joueur2 = client.requeteCreationJoueur(nom2, prenom2)
        val idJoueur = joueur1.id
        val cleJoueur = joueur1.cle

        val idPartie = client.requeteCreationPartie(idJoueur, cleJoueur)

        val etatPartie = client.requeteEtatPartie(idPartie)

        println("${etatPartie}")

        assertFalse(etatPartie.idJoueur2 == idJoueur)
    }


    @Test
    fun testEtatPartie2() {
        // Crée deux joueurs, une partie, puis fait rejoindre le joueur 2. Vérifie que le joueur 2 est bien enregistré.
        val (nom1, prenom1) = obtenirNomEtPrenom()
        val joueur1 = client.requeteCreationJoueur(nom1, prenom1)

        val (nom2, prenom2) = obtenirNomEtPrenom()
        val joueur2 = client.requeteCreationJoueur(nom2, prenom2)
        val idJoueur1 = joueur1.id
        val cleJoueur1 = joueur1.cle


        val idJoueur2 = joueur2.id
        val cleJoueur2 = joueur2.cle


        val idPartie = client.requeteCreationPartie(idJoueur1, cleJoueur1)

        client.requeteRejoindrePartie(idPartie, idJoueur2, cleJoueur2)

        val etatPartie = client.requeteEtatPartie(idPartie)

        assertEquals(etatPartie.idJoueur2, idJoueur2)
    }


    @Test
    fun testEtatPartie3() {
        // Tente d'obtenir l'état d'une partie inexistante. Vérifie qu'une exception est levée.

        var random = kotlin.random.Random.nextInt(10_000, 100_000)

        if (client.requeteListeParties().contains(random)) {
            random = kotlin.random.Random.nextInt(10_000, 100_000)
        }
        assertThrows<QuiEstCeException> {
            client.requeteEtatPartie(99999)
        }
    }

    @Test
    fun testEtatPartie4() {
        // Crée deux joueurs, une partie, puis fait rejoindre le joueur 2. Vérifie que l'étape est toujours INITIALISATION.
        val (nom1, prenom1) = obtenirNomEtPrenom()
        val joueur1 = client.requeteCreationJoueur(nom1, prenom1)

        val (nom2, prenom2) = obtenirNomEtPrenom()
        val joueur2 = client.requeteCreationJoueur(nom2, prenom2)

        val idJoueur1 = joueur1.id
        val cleJoueur1 = joueur1.cle


        val idJoueur2 = joueur2.id
        val cleJoueur2 = joueur2.cle


        val idPartie = client.requeteCreationPartie(idJoueur1, cleJoueur1)

        client.requeteRejoindrePartie(idPartie, idJoueur2, cleJoueur2)

        val etatPartie = client.requeteEtatPartie(idPartie)

        assertEquals(etatPartie.etape, ETAPE.INITIALISATION)
    }

    @Test
    fun testEtatPartie5() {
        // Crée un joueur et une partie. Vérifie que l'étape de la partie n'est pas déjà TERMINÉE.
        val (nom1, prenom1) = obtenirNomEtPrenom()
        val joueur1 = client.requeteCreationJoueur(nom1, prenom1)
        val idJoueur = joueur1.id
        val cleJoueur = joueur1.cle

        val idPartie = client.requeteCreationPartie(idJoueur, cleJoueur)

        val etatPartie = client.requeteEtatPartie(idPartie)

        assertFalse(etatPartie.etape == ETAPE.TERMINEE)
    }
}
