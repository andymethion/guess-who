import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.exceptions.QuiEstCeException
import org.junit.jupiter.api.*
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.io.File
import kotlin.test.assertEquals

@TestInstance(PER_CLASS)
class CreationJoueurTest {
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

    fun obtenirCharacters(character: String): String {
        val repeatCount = (0..100).random()
        return character.repeat(repeatCount)
    }

    fun obtenirLongueChaineCharacters(longueur: Int): String {
        val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..longueur).map { chars.random() }.joinToString("")
    }

    @Test
    fun `création d'un joueur sans nom`() {
        assertThrows<IllegalArgumentException> {
            client.requeteCreationJoueur("", "John")
        }
    }

    @Test
    fun `création d'un joueur sans prénom`() {
        assertThrows<IllegalArgumentException> {
            client.requeteCreationJoueur("Doe", "")
        }
    }

    @Test
    fun `création d'un joueur dont le nom et le prénom sont des espaces`() {
        val nom = obtenirCharacters(" ")
        val prenom = obtenirCharacters(" ")

        val identificationJoueur = client.requeteCreationJoueur(nom, prenom)
        val joueur = client.requeteJoueur(identificationJoueur.id)

        assertEquals(nom, joueur.nom)
        assertEquals(prenom, joueur.prenom)
    }

    @Test
    fun `création d'un joueur dont le nom et le prénom sont des chiffres`() {
        val nom = obtenirCharacters("0")
        val prenom = obtenirCharacters("0")

        val identificationJoueur = client.requeteCreationJoueur(nom, prenom)
        val joueur = client.requeteJoueur(identificationJoueur.id)

        assertEquals(nom, joueur.nom)
        assertEquals(prenom, joueur.prenom)
    }

    @Test
    fun `création d'un joueur dont le nom et le prénom sont des caractères spéciaux`() {
        val nom = obtenirCharacters("!")
        val prenom = obtenirCharacters("!")

        val identificationJoueur = client.requeteCreationJoueur(nom, prenom)
        val joueur = client.requeteJoueur(identificationJoueur.id)

        assertEquals(nom, joueur.nom)
        assertEquals(prenom, joueur.prenom)
    }

    @Test
    fun `création d'un joueur dont le nom et le prénom font 2033 charactères`() {
        val nom = obtenirLongueChaineCharacters(2033)
        val prenom = obtenirLongueChaineCharacters(2033)

        val identificationJoueur = client.requeteCreationJoueur(nom, prenom)
        val joueur = client.requeteJoueur(identificationJoueur.id)

        assertEquals(nom, joueur.nom)
        assertEquals(prenom, joueur.prenom)
    }

    @Test
    fun `création d'un joueur dont le nom et le prénom font 2034 charactères`() {
        val nom = obtenirLongueChaineCharacters(2034)
        val prenom = obtenirLongueChaineCharacters(2034)

        assertThrows<QuiEstCeException> {
            client.requeteCreationJoueur(nom, prenom)
        }
    }

    @Test
    fun `création d'un joueur`() {
        val (nom, prenom) = obtenirNomEtPrenom()

        val identificationJoueur = client.requeteCreationJoueur(nom, prenom)
        val joueur = client.requeteJoueur(identificationJoueur.id)

        assertEquals(nom, joueur.nom)
        assertEquals(prenom, joueur.prenom)
    }

    @Test
    fun `création d'un joueur en double`() {
        val (nom, prenom) = obtenirNomEtPrenom()
        client.requeteCreationJoueur(nom, prenom)

        assertThrows<QuiEstCeException> {
            client.requeteCreationJoueur(nom, prenom)
        }
    }
}