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
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@TestInstance(PER_CLASS)
class RequeteJoueursTest {
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
    fun testRequeteJoueurs1() {

        //verifie que lorsqu'un joueur est créer, celui-ci est bien présent dans requeteJoueurs()

        val (nom, prenom) = obtenirNomEtPrenom()

        val joueur = client.requeteCreationJoueur(nom, prenom)



        assert(joueur.id in client.requeteJoueurs())
    }

    @Test
    fun testRequeteJoueurs2() {

        // Vérifie qu'un ID aléatoire non attribué n'est pas présent dans la liste des joueurs.

        var joueur = (0..Int.MAX_VALUE).random()
        var joueurs = client.requeteJoueurs()

        while (joueurs.contains(joueur)) {
            joueur = (0..Int.MAX_VALUE).random()
            joueurs = client.requeteJoueurs()
        }


        assertFalse(joueur in client.requeteJoueurs())
    }

    @Test
    fun testRequeteJoueurs3() {

        //Vérifie que la liste requeteJoueurs() lance une exception lorsqu’on utilise un ID négatif.

        assertFalse(-1 in client.requeteJoueurs())
    }

    @Test
    fun testRequeteJoueurs4() {

        //Vérifie que la liste requeteJoueurs() ne possèdent pas de doublons.

        assertTrue(client.requeteJoueurs().distinct().size == client.requeteJoueurs().size)

    }

    @Test
    fun testRequeteJoueurs5() {

        //verifie que tous les IDs sont positifs

        val joueurs = client.requeteJoueurs()
        for (id in joueurs) {
            assertTrue(id >= 0)

        }
    }


}