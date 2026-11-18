package controleurs

import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.image.ImageView
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent

class CarteGriseeControleur() : EventHandler<MouseEvent> {
    override fun handle(event: MouseEvent) {
        if (event.button == MouseButton.SECONDARY) {
            val boutonClique = event.source as Button
            val imageView = boutonClique.graphic as ImageView

            if (imageView.opacity == 1.0) {
                imageView.opacity = 0.05
            } else {
                imageView.opacity = 1.0
            }
        }
    }
}