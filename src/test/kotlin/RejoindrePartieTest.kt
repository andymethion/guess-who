import info.but1.sae2025.QuiEstCeClient
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

@TestInstance(PER_CLASS)
class RejoindrePartieTest {
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
    //Test RejoindrePartie


    @Test
    fun testRejoindrePartie1() {
        // Test que le second joueur peut rejoindre une partie créée par le premier joueur,
        // et que les deux joueurs sont bien enregistrés dans l'état de la partie.
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
        assertEquals(etatPartie.idJoueur1, idJoueur1)
    }

    @Test
    fun testRejoindrePartie2() {
        // Test que rejoindre une partie inexistante (ID invalide) déclenche une exception.
        val (nom1, prenom1) = obtenirNomEtPrenom()
        val joueur1 = client.requeteCreationJoueur(nom1, prenom1)

        val (nom2, prenom2) = obtenirNomEtPrenom()
        val joueur2 = client.requeteCreationJoueur(nom2, prenom2)
        val idJoueur1 = joueur1.id
        val cleJoueur1 = joueur1.cle

        val idJoueur2 = joueur2.id
        val cleJoueur2 = joueur2.cle


        var random = kotlin.random.Random.nextInt(10_000, 100_000)

        if (random in client.requeteListeParties()) {
            random = kotlin.random.Random.nextInt(10_000, 100_000)
        }

        assertThrows<QuiEstCeException> {
            client.requeteRejoindrePartie(random, idJoueur2, cleJoueur2)
        }
    }

    @Test
    fun testRejoindrePartie3() {
        // Test que le joueur créateur de la partie ne peut pas la rejoindre une seconde fois (évite doublons).
        val (nom1, prenom1) = obtenirNomEtPrenom()
        val joueur1 = client.requeteCreationJoueur(nom1, prenom1)

        val idJoueur1 = joueur1.id
        val cleJoueur1 = joueur1.cle

        val idPartie = client.requeteCreationPartie(idJoueur1, cleJoueur1)

        assertThrows<QuiEstCeException> {
            client.requeteRejoindrePartie(idPartie, idJoueur1, cleJoueur1)
        }
    }

    @Test
    fun testRejoindrePartie4() {
        // Test que la clé fournie pour rejoindre la partie doit être celle du joueur rejoignant,
        // sinon une exception est levée (sécurité/authentification).
        val (nom1, prenom1) = obtenirNomEtPrenom()
        val joueur1 = client.requeteCreationJoueur(nom1, prenom1)

        val (nom2, prenom2) = obtenirNomEtPrenom()
        val joueur2 = client.requeteCreationJoueur(nom2, prenom2)
        val idJoueur1 = joueur1.id
        val cleJoueur1 = joueur1.cle

        val idJoueur2 = joueur2.id


        val idPartie = client.requeteCreationPartie(idJoueur1, cleJoueur1)

        assertThrows<QuiEstCeException> {

            client.requeteRejoindrePartie(idPartie, idJoueur2, cleJoueur1)
        }
    }

    @Test
    fun testRejoindrePartie5() {
        // Test que fournir une clé totalement invalide lors de la tentative de rejoindre une partie
        // provoque une exception.
        val (nom1, prenom1) = obtenirNomEtPrenom()
        val joueur1 = client.requeteCreationJoueur(nom1, prenom1)

        val (nom2, prenom2) = obtenirNomEtPrenom()
        val joueur2 = client.requeteCreationJoueur(nom2, prenom2)
        val idJoueur1 = joueur1.id
        val cleJoueur1 = joueur1.cle

        val idJoueur2 = joueur2.id


        val idPartie = client.requeteCreationPartie(idJoueur1, cleJoueur1)

        assertThrows<QuiEstCeException> {
            client.requeteRejoindrePartie(idPartie, idJoueur2, "tttttttttttttttttttttttttttttttt")
        }
    }
}