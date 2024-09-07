package org.example.model

import java.time.LocalDateTime

class StartActionEvent implements Event {
    LocalDateTime timestamp
    String message
    String type = "Action started"

    StartActionEvent(LocalDateTime timestamp, Action action) {
        this.timestamp = timestamp
    }
}
