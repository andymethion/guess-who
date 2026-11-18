import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.ETAPE
import info.but1.sae2025.exceptions.QuiEstCeException
import org.junit.jupiter.api.*
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@TestInstance(PER_CLASS)
class ChercherEncoreTest {
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

        client.requeteChoixPersonnage(idPartie, joueur1.id, joueur1.cle, 0, 0)
        client.requeteChoixPersonnage(idPartie, joueur2.id, joueur2.cle, 0, 0)

        val question = "question valide test ?"
        val reponse = "oui"
        client.requetePoserQuestion(idPartie, joueur1.id, joueur1.cle, question)
        client.requeteDonnerReponse(idPartie, joueur2.id, joueur2.cle, reponse)

        return Pair(
            Triple(idPartie, joueur1.id, joueur1.cle),
            Triple(idPartie, joueur2.id, joueur2.cle)
        )
    }

    @Test//
    fun testEtatAvantAppelChercherEncore() {
        val (joueur1, _) = creerPartieAvecDeuxJoueurs()
        val (idPartie, _, _) = joueur1

        val etatAvant = client.requeteEtatPartie(idPartie)

        assertEquals(
            ETAPE.ATTENTE_REFLEXION,
            etatAvant.etape,
            "L'état doit être ATTENTE_REFLEXION avant chercher encore"
        )
    }

    @Test
    fun requeteChercherEncore_test() {
        val (joueur1, _) = creerPartieAvecDeuxJoueurs()
        val (idPartie, idJoueur, cleJoueur) = joueur1

        val etatPartie = client.requeteChercherEncore(idPartie, idJoueur, cleJoueur)

        assertTrue(
            etatPartie.etape != ETAPE.INITIALISATION,
            "L'étape ne doit pas être ,Initialisation, après chercher encore"
        )

    }

    @Test
    fun requeteChercherEncore_etatCorrect() {
        val (joueur1, _) = creerPartieAvecDeuxJoueurs()
        val (idPartie, idJoueur, cleJoueur) = joueur1

        val etatPartie = client.requeteChercherEncore(idPartie, idJoueur, cleJoueur)
        assertEquals(ETAPE.ATTENTE_QUESTION, etatPartie.etape)

    }

    @Test
    fun testChercherEncore_QuiEstCeException() {
        val (joueur1, _) = creerPartieAvecDeuxJoueurs()
        val (idPartie, idJoueur, _) = joueur1

        val mauvaiseCle = "12345678901234567890123456789012" // clé correcte mais fausse

        assertThrows<QuiEstCeException> {
            client.requeteChercherEncore(idPartie, idJoueur, mauvaiseCle)
        }
    }

    @Test
    fun testChercherEncore_idPartieNegatif() {
        val (_, joueur2) = creerPartieAvecDeuxJoueurs()
        val (_, idJoueur, cleJoueur) = joueur2

        // id négatif
        assertThrows<IllegalArgumentException> {
            client.requeteChercherEncore(-1, idJoueur, cleJoueur)
        }
    }

    @Test
    fun testChercherEncore_clefTropCourte() {
        val (_, joueur2) = creerPartieAvecDeuxJoueurs()
        val (idPartie, idJoueur, _) = joueur2

        // clé trop courte
        assertThrows<IllegalArgumentException> {
            client.requeteChercherEncore(idPartie, idJoueur, "cle_trop_courte")
        }
    }

    @Test
    fun testChercherEncore_idJoueurNegatif() {
        val (_, joueur2) = creerPartieAvecDeuxJoueurs()
        val (idPartie, _, cleJoueur) = joueur2

        // id joueur négatif
        assertThrows<IllegalArgumentException> {
            client.requeteChercherEncore(idPartie, -5, cleJoueur)
        }
    }
}






