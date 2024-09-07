package org.example.store

class TaskStore {
    def tasks = []

    def add(task) {
        tasks << task
    }

    def delete(task) {
        tasks - task
    }

    def findAll() {
        tasks
    }
}
