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
import kotlin.test.assertTrue

@TestInstance(PER_CLASS)
class DonnerReponseTest {
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
    fun requeteDonnerReponse1(){
        // Test si l'état de la partie est bien changé en ATTENTE_REPONSE puis en ATTENTE_REFLEXION, validant ainsi l'execution de la requête
        val (joueur1, joueur2) = creerPartieAvecDeuxJoueurs()
        val idPartie = joueur1.first

        val etatPartieQuestion = client.requetePoserQuestion(idPartie,joueur1.second, joueur1.third, "Est-ce un homme ?")
        assertTrue(etatPartieQuestion.etape == ETAPE.ATTENTE_REPONSE)

        val etatPartieReponse = client.requeteDonnerReponse(idPartie,joueur2.second, joueur2.third, "oui")
        assertTrue(etatPartieReponse.etape == ETAPE.ATTENTE_REFLEXION)
    }

    @Test
    fun requeteDonnerReponse2(){
        // Test avec une réponse autre que 'oui' ou 'non'
        val (joueur1, joueur2) = creerPartieAvecDeuxJoueurs()
        val idPartie = joueur1.first

        assertThrows<IllegalArgumentException> {
            client.requeteDonnerReponse(idPartie, joueur2.second, joueur2.third, "blabla")
        }
    }

    @Test
    fun requeteDonnerReponse3(){
        // Test avec un idPartie invalide
        val (_, joueur2) = creerPartieAvecDeuxJoueurs()

        assertThrows<IllegalArgumentException> {
            client.requeteDonnerReponse(-1, joueur2.second, joueur2.third, "oui")
        }
    }

    @Test
    fun requeteDonnerReponse4(){
        // Test avec un idJoueur invalide mais une cleJoueur valide
        val (joueur1, joueur2) = creerPartieAvecDeuxJoueurs()
        val idPartie = joueur1.first

        assertThrows<IllegalArgumentException> {
            client.requeteDonnerReponse(idPartie, -5, joueur2.third, "non")
        }
    }

    @Test
    fun requeteDonnerReponse5(){
        // Test avec un idJoueur valide mais une cleJoueur invalide
        val (joueur1, joueur2) = creerPartieAvecDeuxJoueurs()
        val idPartie = joueur1.first

        assertThrows<IllegalArgumentException> {
            client.requeteDonnerReponse(idPartie, joueur2.second, "123 c'est la clé", "non")
        }
    }

}