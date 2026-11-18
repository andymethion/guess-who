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
import kotlin.test.assertEquals

@TestInstance(PER_CLASS)
class RequeteChoixPersonnageTest {
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

    //Test requeteChoixPersonnage


    @Test
    fun testRequeteChoixPersonnage1() {
        // Test dans lequelle les 2 joueurs choisis 1 personnage

        val (nom1, prenom1) = obtenirNomEtPrenom()
        val joueur1 = client.requeteCreationJoueur(nom1, prenom1)

        val (nom2, prenom2) = obtenirNomEtPrenom()
        val joueur2 = client.requeteCreationJoueur(nom2, prenom2)
        val idJoueur1 = joueur1.id
        val cleJoueur1 = joueur1.cle


        val idJoueur2 = joueur2.id
        val cleJoueur2 = joueur2.cle

        val idPartie1 = client.requeteCreationPartie(idJoueur1, cleJoueur1)



        client.requeteRejoindrePartie(idPartie1, idJoueur2, cleJoueur2)

        client.requeteChoixPersonnage(idPartie1, idJoueur1, cleJoueur1, 1, 1)


        var etatPartie = client.requeteEtatPartie(idPartie1)


        assertEquals(ETAPE.INITIALISATION, etatPartie.etape)

        client.requeteChoixPersonnage(idPartie1, idJoueur2, cleJoueur2, 1, 1)




        etatPartie = client.requeteEtatPartie(idPartie1)
        assertEquals(ETAPE.ATTENTE_QUESTION, etatPartie.etape)
    }

    @Test
    fun testRequeteChoixPersonnage2() {
        // Test dans lequelle un joueur choisie un personnage impossible

        val (nom1, prenom1) = obtenirNomEtPrenom()
        val joueur1 = client.requeteCreationJoueur(nom1, prenom1)

        val (nom2, prenom2) = obtenirNomEtPrenom()
        val joueur2 = client.requeteCreationJoueur(nom2, prenom2)
        val idJoueur1 = joueur1.id
        val cleJoueur1 = joueur1.cle


        val idJoueur2 = joueur2.id
        val cleJoueur2 = joueur2.cle

        val idPartie1 = client.requeteCreationPartie(idJoueur1, cleJoueur1)



        client.requeteRejoindrePartie(idPartie1, idJoueur2, cleJoueur2)

        assertThrows<IllegalArgumentException> {
            client.requeteChoixPersonnage(idPartie1, idJoueur1, cleJoueur1, 10, 10)

        }


    }

    @Test
    fun testRequeteChoixPersonnage3() {
        // Test dans lequelle un joueur choisie 2 personnage

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
        assertThrows<QuiEstCeException> {
            client.requeteChoixPersonnage(idPartie, idJoueur1, cleJoueur1, 0, 0)
        }


    }

    @Test
    fun testRequeteChoixPersonnage4() {
        // Test dans lequelle un joueur non inscrit dans une partie choisie 1 personnage

        val (nom1, prenom1) = obtenirNomEtPrenom()
        val joueur1 = client.requeteCreationJoueur(nom1, prenom1)

        val (nom2, prenom2) = obtenirNomEtPrenom()
        val joueur2 = client.requeteCreationJoueur(nom2, prenom2)

        val idJoueur1 = joueur1.id
        val cleJoueur1 = joueur1.cle
        val idJoueur2 = joueur2.id
        val cleJoueur2 = joueur2.cle

        val idPartie = client.requeteCreationPartie(idJoueur1, cleJoueur1)



        assertThrows<QuiEstCeException> {
            client.requeteChoixPersonnage(idPartie, 2, cleJoueur2, 0, 0)
        }


    }

    @Test
    fun testRequeteChoixPersonnage5() {
        // Test dans lequelle on essaye d'attribué au joueur 1 avec la clé du joueur 2 un personnage

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



        assertThrows<QuiEstCeException> {
            client.requeteChoixPersonnage(idPartie, idJoueur1, cleJoueur2, 0, 0)
        }


    }
}