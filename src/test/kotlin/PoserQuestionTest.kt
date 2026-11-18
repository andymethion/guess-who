import classes.Joueur
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
import kotlin.test.assertTrue

@TestInstance(PER_CLASS)
class PoserQuestionTest {
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
    fun testPoserQuestionValide() {
        val (nom1, prenom1) = obtenirNomEtPrenom()
        val joueur1 = client.requeteCreationJoueur(nom1, prenom1)

        val (nom2, prenom2) = obtenirNomEtPrenom()
        val joueur2 = client.requeteCreationJoueur(nom2, prenom2)

        val idPartie = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        client.requeteRejoindrePartie(idPartie, joueur2.id, joueur2.cle)


        client.requeteChoixPersonnage(idPartie,joueur1.id,joueur1.cle,0,0)
        client.requeteChoixPersonnage(idPartie,joueur2.id,joueur2.cle,0,0)

        val question = "Est-ce que ton personnage porte des lunettes ?"
        val etatPartie = client.requetePoserQuestion(idPartie, joueur1.id, joueur1.cle, question)

        assertTrue(etatPartie.etape != ETAPE.INITIALISATION)

        assertTrue(etatPartie.idJoueur1 > 0)
        assertTrue(etatPartie.idJoueur2 > 0)
    }

    @Test
    fun testPoserQuestionPartieInexistante() {
        val (joueur1, joueur2) = creerPartieAvecDeuxJoueurs()
        val idPartieInexistant = 999999
        val question = "Est-ce que ton personnage est blond ?"

        assertThrows<QuiEstCeException> {
            client.requetePoserQuestion(idPartieInexistant, joueur1.second, joueur1.third, question)
        }
    }

    @Test
    fun testPoserQuestionMauvaiseCle() {
        val (joueur1, joueur2) = creerPartieAvecDeuxJoueurs()
        val idPartie = joueur1.first
        val question = "Est-ce que ton personnage est grand ?"
        val mauvaiseCle = "cle_invalide"

        assertThrows<IllegalArgumentException> {
            client.requetePoserQuestion(idPartie, joueur1.second, mauvaiseCle, question)
        }
    }

    @Test
    fun testPoserQuestionQuestionVide() {
        val (joueur1, joueur2) = creerPartieAvecDeuxJoueurs()
        val (idPartie, idJoueur, cleJoueur) = joueur1
        val question = ""

        assertThrows<IllegalArgumentException> {
            client.requetePoserQuestion(idPartie, idJoueur, cleJoueur, question)
        }
    }

    @Test
    fun testPoserQuestionTourNonAutorisé() {
        val (joueur1, joueur2) = creerPartieAvecDeuxJoueurs()
        val (idPartie, idJoueur, cleJoueur) = joueur1
        val question = "Est-ce que ton personnage a une barbe ?"

        // Simule que ce n'est pas le tour du joueur (par exemple en posant deux questions de suite)
        client.requetePoserQuestion(idPartie, idJoueur, cleJoueur, question)

        assertThrows<QuiEstCeException> {
            client.requetePoserQuestion(idPartie, idJoueur, cleJoueur, question)
        }
    }
}
