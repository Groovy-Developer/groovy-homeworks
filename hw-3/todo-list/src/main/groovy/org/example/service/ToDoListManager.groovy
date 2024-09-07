package org.example.service

import org.example.model.Action
import org.example.model.Task
import org.example.store.TaskStore

class ToDoListManager {
    TaskStore taskStore

    ToDoListManager(TaskStore taskStore) {
        this.taskStore = taskStore
    }

    def addTask(Task task) {
        checkIntersections(task)
        taskStore.add()
    }

    def showAllTasks() {
        taskStore.findAll()
    }

    def addAction(taskId, Action action) {
        // todo
    }

    def showBusyTime(start, end) {
        return []
    }

    private checkIntersections(Task task) {
        // todo
    }
    // todo
}
