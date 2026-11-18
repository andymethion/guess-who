package controleurs

import Modele
import javafx.event.EventHandler
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ButtonType
import javafx.scene.input.MouseEvent
import javafx.stage.Stage
import vues.AttenteChoixPerso
import vues.SelectionPersonnage
import java.util.*

class ChoisirPersonnageControleur(
    private val stage: Stage,
    private val vue: SelectionPersonnage,
    private val modele: Modele
) : EventHandler<MouseEvent> {
    fun demanderConfirmation(): Optional<ButtonType?>? {
        val dialog = Alert(Alert.AlertType.CONFIRMATION)
        dialog.title = "CONFIRMATION"
        dialog.headerText = "Es-tu sur de vouloir choisir ce personnage ?"
        return dialog.showAndWait()
    }

    fun allerAttenteChoixPersonnage() {
        val attenteChoixPerso = AttenteChoixPerso()

        val attenteChoixPersonnageControleur = AttenteChoixPersonnageControleur(stage, modele)
        attenteChoixPersonnageControleur.handle(null)

        stage.scene.root = attenteChoixPerso
    }

    override fun handle(event: MouseEvent) {
        val boutonClique = event.source as Button
        val position = vue.btnPos[boutonClique] ?: return
        val (ligne, colonne) = position

        val confirmation = demanderConfirmation()
        if (confirmation == null) return
        if (confirmation.get() != ButtonType.OK) return

        modele.obtenirJoueur()!!.choisirPersonnage(modele.obtenirIdPartie()!!, ligne, colonne)
        allerAttenteChoixPersonnage()
    }
}