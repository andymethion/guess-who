import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.ETAPE
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.api.assertThrows
import java.io.File
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@TestInstance(PER_CLASS)
class TrouveTest {
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

    fun creerPartieAvecDeuxJoueurs(): Pair<Triple<Int, Int, String>, Triple<Int, Int, String>> {
        val (nom1, prenom1) = obtenirNomEtPrenom()
        val joueur1 = client.requeteCreationJoueur(nom1, prenom1)

        val (nom2, prenom2) = obtenirNomEtPrenom()
        val joueur2 = client.requeteCreationJoueur(nom2, prenom2)

        val idPartie = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        client.requeteRejoindrePartie(idPartie, joueur2.id, joueur2.cle)

        client.requeteChoixPersonnage(idPartie,joueur1.id,joueur1.cle,0,0)
        client.requeteChoixPersonnage(idPartie,joueur2.id,joueur2.cle,0,0)

        return Pair(
            Triple(idPartie, joueur1.id, joueur1.cle),
            Triple(idPartie, joueur2.id, joueur2.cle)
        )
    }

    @Test
    fun requeteTrouve1() {
        val (joueur1, joueur2) = creerPartieAvecDeuxJoueurs()
        val idPartie = joueur1.first
        client.requetePoserQuestion(idPartie,joueur1.second, joueur1.third, "Est-ce un homme ?")
        client.requeteDonnerReponse(idPartie,joueur2.second, joueur2.third, "oui")

        assertTrue(client.requeteTrouve(idPartie,joueur1.second, joueur1.third,0,0))
    }

    @Test
    fun requeteTrouve2() {
        val (joueur1, joueur2) = creerPartieAvecDeuxJoueurs()
        val idPartie = joueur1.first
        client.requetePoserQuestion(idPartie,joueur1.second, joueur1.third, "Est-ce un homme ?")
        client.requeteDonnerReponse(idPartie,joueur2.second, joueur2.third, "oui")

        assertFalse(client.requeteTrouve(idPartie,joueur1.second, joueur1.third,3,1))
    }

    @Test
    fun requeteTrouve3() {
        val (joueur1, joueur2) = creerPartieAvecDeuxJoueurs()
        val idPartie = joueur1.first
        client.requetePoserQuestion(idPartie,joueur1.second, joueur1.third, "Est-ce un homme ?")
        client.requeteDonnerReponse(idPartie,joueur2.second, joueur2.third, "oui")

        assertThrows<IllegalArgumentException> {
            client.requeteTrouve(-1, joueur1.second, joueur1.third, 0,0)
        }
    }

    @Test
    fun requeteTrouve4() {
        val (joueur1, joueur2) = creerPartieAvecDeuxJoueurs()
        val idPartie = joueur1.first
        client.requetePoserQuestion(idPartie,joueur1.second, joueur1.third, "Est-ce un homme ?")
        client.requeteDonnerReponse(idPartie,joueur2.second, joueur2.third, "oui")

        assertThrows<IllegalArgumentException> {
            client.requeteTrouve(idPartie, -1, joueur1.third, 0,0)
        }
    }

    @Test
    fun requeteTrouve5() {
        val (joueur1, joueur2) = creerPartieAvecDeuxJoueurs()
        val idPartie = joueur1.first
        client.requetePoserQuestion(idPartie,joueur1.second, joueur1.third, "Est-ce un homme ?")
        client.requeteDonnerReponse(idPartie,joueur2.second, joueur2.third, "oui")

        assertThrows<IllegalArgumentException> {
            client.requeteTrouve(idPartie, joueur1.second, "la clé n'est pas bien", 0,0)
        }
    }
}