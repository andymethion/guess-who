import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.exceptions.QuiEstCeException
import org.junit.jupiter.api.*
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@TestInstance(PER_CLASS)
class ObtentionJoueurTest {
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
    fun `obtention d'un joueur dont l'id existe`() {
        val (nom, prenom) = obtenirNomEtPrenom()

        val identificationJoueur = client.requeteCreationJoueur(nom, prenom)
        val joueur = client.requeteJoueur(identificationJoueur.id)

        assertEquals(nom, joueur.nom)
        assertEquals(prenom, joueur.prenom)
    }

    @Test
    fun `obtention d'un joueur dont l'id n'existe pas`() {
        var joueur = kotlin.random.Random.nextInt(0, Int.MAX_VALUE)
        var joueurs = client.requeteJoueurs()

        while (joueurs.contains(joueur)) {
            joueur = kotlin.random.Random.nextInt(0, Int.MAX_VALUE)
            joueurs = client.requeteJoueurs()
        }

        assertThrows<QuiEstCeException> {
            client.requeteJoueur(joueur)
        }
    }

    @Test
    fun `obtention d'un joueur dont l'id est négatif`() {
        assertThrows<IllegalArgumentException> {
            client.requeteJoueur(-1)
        }
    }

    @Test
    fun `obtention des joueurs sans l'ajout de nouveau joueur au préalable`() {
        val joueurs = client.requeteJoueurs()
        assertEquals(joueurs, client.requeteJoueurs())
    }

    @Test
    fun `obtention des joueurs avec l'ajout d'un nouveau joueur au préalable`() {
        val (nom, prenom) = obtenirNomEtPrenom()

        val identificationJoueur = client.requeteCreationJoueur(nom, prenom)
        var joueurs = client.requeteJoueurs()

        assertTrue(joueurs.contains(identificationJoueur.id))
    }
}