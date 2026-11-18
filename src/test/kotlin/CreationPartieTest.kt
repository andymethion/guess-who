import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.exceptions.QuiEstCeException
import org.junit.jupiter.api.*
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.io.File
import kotlin.test.assertTrue

@TestInstance(PER_CLASS)
class CreationPartieTest {
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
    fun `Vérifie qu'on ne peut pas créer une partie avec des paramètres vides`() {
        assertThrows<IllegalArgumentException> {
            client.requeteCreationPartie(0, "")
        }
    }

    @Test
    fun `Vérifie qu'on ne peut pas créer une partie avec un idJoueur négatif`() {
        val (nom, prenom) = obtenirNomEtPrenom()
        val identificationJoueur = client.requeteCreationJoueur(nom, prenom)

        assertThrows<IllegalArgumentException> {
            client.requeteCreationPartie(-1, identificationJoueur.cle)
        }
    }

    @Test
    fun `Vérifie qu'on ne peut pas créer une partie si la clé n'est pas de longueur 32`() {
        val (nom, prenom) = obtenirNomEtPrenom()
        val identificationJoueur = client.requeteCreationJoueur(nom, prenom)

        assertThrows<IllegalArgumentException> {
            client.requeteCreationPartie(identificationJoueur.id, identificationJoueur.cle.take(31))
        }
    }

    @Test
    fun `Vérifie qu'on ne peut pas créer une partie si la clé est composée d'un caractère spécial`() {
        val (nom, prenom) = obtenirNomEtPrenom()
        val identificationJoueur = client.requeteCreationJoueur(nom, prenom)

        assertThrows<QuiEstCeException> {
            client.requeteCreationPartie(identificationJoueur.id, identificationJoueur.cle.replaceFirstChar{ "!" })
        }
    }

    @Test
    fun `Vérifie qu'on ne peut pas créer une partie si l'id du joueur n'est pas valide`() {
        val (nom, prenom) = obtenirNomEtPrenom()
        val identificationJoueur = client.requeteCreationJoueur(nom, prenom)

        assertThrows<QuiEstCeException> {
            client.requeteCreationPartie(0, identificationJoueur.cle)
        }
    }

    @Test
    fun `Vérifie qu'on ne peut pas créer une partie si la clé du joueur n'est pas valide`() {
        val (nom, prenom) = obtenirNomEtPrenom()
        val identificationJoueur = client.requeteCreationJoueur(nom, prenom)

        assertThrows<QuiEstCeException> {
            client.requeteCreationPartie(identificationJoueur.id, "00000000000000000000000000000000")
        }
    }

    @Test
    fun `Vérifie qu'une partie créée est bien enregistrée`() {
        val (nom, prenom) = obtenirNomEtPrenom()

        val joueur = client.requeteCreationJoueur(nom, prenom)
        val idPartie = client.requeteCreationPartie(joueur.id, joueur.cle)
        val partiesCreees = client.requeteListePartiesCreees()

        assertTrue(idPartie in partiesCreees)
    }

    @Test
    fun `Vérifie si un joueur peut créer plusieurs parties`() {
        val (nom, prenom) = obtenirNomEtPrenom()

        val joueur = client.requeteCreationJoueur(nom, prenom)

        val idPartie0 = client.requeteCreationPartie(joueur.id, joueur.cle)
        val idPartie1 = client.requeteCreationPartie(joueur.id, joueur.cle)

        val partiesCreees = client.requeteListePartiesCreees()

        assertTrue(idPartie0 in partiesCreees)
        assertTrue(idPartie1 in partiesCreees)
    }
}