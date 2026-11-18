import controleurs.AllerReglesControleur
import controleurs.CreationJoueurControleur
import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import vues.Accueil

class App() : Application() {
    override fun start(stage: Stage) {
        val vue = Accueil()
        val modele = Modele()

        vue.btnRegles.onMouseClicked = AllerReglesControleur(stage, modele)
        vue.btnValider.onMouseClicked = CreationJoueurControleur(stage, vue, modele)

        val scene = Scene(vue, 650.0, 650.0)
        stage.title = "Qui Est Ce ?"
        stage.scene = scene
        stage.show()
    }
}

fun main() {
    Application.launch(App::class.java)
}