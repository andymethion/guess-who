import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.exceptions.QuiEstCeException
import org.junit.jupiter.api.*
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.io.File
import kotlin.test.assertEquals

@TestInstance(PER_CLASS)
class RequeteJoueurTest {
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
    fun `Vérifie que l'exception QuiEstCeException est levée lorsqu'on tente d'obtenir un joueur dont l'id n'existe pas`() {
        var joueur = (0..Int.MAX_VALUE).random()
        var joueurs = client.requeteJoueurs()

        while (joueurs.contains(joueur)) {
            joueur = (0..Int.MAX_VALUE).random()
            joueurs = client.requeteJoueurs()
        }

        assertThrows<QuiEstCeException> {
            client.requeteJoueur(joueur)
        }
    }

    @Test
    fun `Vérifie que l'exception QuiEstCeException est levée lorsqu'on tente d'obtenir un joueur dont l'id est négatif`() {
        assertThrows<IllegalArgumentException> {
            client.requeteJoueur(-1)
        }
    }

    @Test
    fun `Vérifie les informations d'un joueur qu'on vient de créer`() {
        val (nom, prenom) = obtenirNomEtPrenom()

        val identificationJoueur = client.requeteCreationJoueur(nom, prenom)
        val joueur = client.requeteJoueur(identificationJoueur.id)

        assertEquals(nom, joueur.nom)
        assertEquals(prenom, joueur.prenom)
    }

    @Test
    fun `Vérifie que les informations d'un joueur sont les mêmes entre deux appels de la méthode`() {
        val (nom, prenom) = obtenirNomEtPrenom()

        val identificationJoueur = client.requeteCreationJoueur(nom, prenom)
        val joueur0 = client.requeteJoueur(identificationJoueur.id)
        val joueur1 = client.requeteJoueur(identificationJoueur.id)

        assertEquals(joueur0.nom, joueur1.nom)
        assertEquals(joueur0.prenom, joueur1.prenom)
    }
}