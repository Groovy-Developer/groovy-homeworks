package org.example.service

import org.example.model.Event
import org.example.model.StartActionEvent
import org.example.store.ActionStore

import java.time.LocalDateTime

class TrackActionsService {
    EventBus eventBus = new EventBus()
    ActionStore actionStore = new ActionStore()

    def onSchedule() {
        def currentTime = LocalDateTime.now()
        // search actions that start or end at current time
        def startedActions = []
        def endedActions = []

        startedActions.forEach {
            StartActionEvent event = new StartActionEvent(currentTime, it)
            produce(event)
        }
    }

    def produce(Event event) {
        eventBus.send(event)
    }
}
