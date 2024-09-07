package org.example.service

import org.example.model.Event

class EventBus {
    def consumers = []

    //todo: bus to send event to consumers
    def send(Event event) {
        consumers.forEach {
            it(event)
        }
    }

    registerConsumer(Closure closure) {
        consumers << closure
    }
}
