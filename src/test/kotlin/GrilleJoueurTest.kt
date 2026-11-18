import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.Personnage
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@TestInstance(PER_CLASS)
class GrilleJoueurTest {
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

    // test GrilleJoueur

    @Test
    fun testGrilleJoueur1() {


        //verifie les bonnes dimension de la grille joueur
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


        client.requeteChoixPersonnage(idPartie, idJoueur1, cleJoueur1, 1, 1)

        client.requeteChoixPersonnage(idPartie, idJoueur2, cleJoueur2, 0, 1)


        assertEquals(4, client.requeteGrilleJoueur(idPartie, idJoueur1).size)
        assertEquals(6, client.requeteGrilleJoueur(idPartie, idJoueur1)[0].size)
    }

    @Test
    fun testGrilleJoueur2() {


        //verifie que la grille est bien composé de personnage
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


        client.requeteChoixPersonnage(idPartie, idJoueur1, cleJoueur1, 1, 1)

        client.requeteChoixPersonnage(idPartie, idJoueur2, cleJoueur2, 0, 1)


        var caseNullTrouvee = false

        for (i in client.requeteGrilleJoueur(idPartie, idJoueur1).indices) {
            for (j in client.requeteGrilleJoueur(idPartie, idJoueur1)[i].indices) {
                if (client.requeteGrilleJoueur(idPartie, idJoueur1)[i][j] == null) {
                    caseNullTrouvee = true
                    break  // Sort de la boucle intérieure
                }
            }
            if (caseNullTrouvee) break
        }

        assertFalse(caseNullTrouvee)
    }


    @Test
    fun testGrilleJoueur3() {


        //verifie si il y à une case null
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


        client.requeteChoixPersonnage(idPartie, idJoueur1, cleJoueur1, 1, 1)

        client.requeteChoixPersonnage(idPartie, idJoueur2, cleJoueur2, 0, 1)






        for (i in client.requeteGrilleJoueur(idPartie, idJoueur1).indices) {
            for (j in client.requeteGrilleJoueur(idPartie, idJoueur1)[i].indices) {
                assertTrue(client.requeteGrilleJoueur(idPartie, idJoueur1)[i][j] is Personnage)
            }

        }


    }
}