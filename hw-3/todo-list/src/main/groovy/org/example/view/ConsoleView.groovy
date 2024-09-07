package org.example.view

import org.example.model.Event
import org.example.service.EventBus
import org.example.service.ToDoListManager

class ConsoleView {
    ToDoListManager manager

    ConsoleView(ToDoListManager manager, EventBus eventBus) {
        this.manager = manager
        eventBus.registerConsumer(this::consumeEvent)
    }

    def consumeEvent(Event event) {
        println event
    }
}
