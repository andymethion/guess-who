package controleurs

import javafx.event.EventHandler
import javafx.scene.input.MouseEvent

class QuitterApplicationControleur : EventHandler<MouseEvent> {
    override fun handle(event: MouseEvent) {
        javafx.application.Platform.exit()
    }
}