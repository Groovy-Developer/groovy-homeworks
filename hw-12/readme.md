## Задание 12: Создание автотестов с использование Geb Spock
Научиться писать тест-кейсы для интеграционного тестирования Groovy-приложений;

Научиться создавать автотесты для пользовательского интерфейса с использованием фреймворка Geb.

## Инструкция
Покрыть логику работы todo-list из предыдущих заданий интеграционными тестами со Spock и протестировать работу приложения, написав автотесты с использованием Geb.

Формат сдачи: 
- ссылка на PR в github
- скриншот/отчет о % покрытии кода тестами

Критерии оценки:
1. Задание сдано на проверку - 1 балл
2. Создан PR - 1 балл
3. Есть CI/CD пайплайн в github actions, по которому можно определить успешность сборки - 1 балла
4. Бизнес логика покрыта модульными и интеграционными тестами -2 балл
5. Реализованы автотесты для основных сценариев использования приложения - 2 балл
6. Процент покрытия кода тестами не ниже 60% - 3 балла

Задание принимается, если набрано минимум 7 баллов

## Примеры

Тестирование API:
```groovy

import jakarta.inject.Inject
import io.micronaut.test.annotation.MockBean
import io.micronaut.context.annotation.Property
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import java.time.LocalDateTime
import spock.lang.Specification

@MicronautTest
@Property(name = "datasources.default.driver-class-name", value = "org.testcontainers.jdbc.ContainerDatabaseDriver")
@Property(name = "datasources.default.url", value = "jdbc:tc:postgresql:///db")
class ActionControllerTest extends Specification {
    @Inject
    TaskServiceImpl taskService
    @Inject
    ActionServiceImpl actionService
    Task task
    Action action

    def setup() {
        task = new Task(
                id: UUID.randomUUID(),
                name: "learning",
                description: "read book",
                startDate: LocalDateTime.of(2024, 11, 1, 0, 0),
                endDate: LocalDateTime.of(2024, 11, 10, 0, 0),
                actions: null
        )
        action = new Action(
                id: UUID.randomUUID(),
                name: "read",
                description: "read math",
                startDate: LocalDateTime.of(2024, 11, 2, 0, 0),
                endDate: LocalDateTime.of(2024, 11, 3, 0, 0),
                task: task
        )
    }

    void cleanup() {
        task = null
        action = null
    }

    def "deleteById"() {
        when:
        taskClient.save(task)
        actionClient.save(action)
        actionClient.deleteById(action.id)
        def actionDb = actionClient.findById(action.id)

        then:
        1 * taskService.save(task) >> taskMapper.toTaskInfo(task)
        1 * actionService.save(action) >> actionMapper.toActionInfo(action)
        1 * actionService.deleteById(action.id)
        1 * actionService.findById(action.id) >> null

        then:
        actionDb == null
    }

    def "findAllByStartDateAndEndDate"() {
        when:
        taskClient.save(task)
        actionClient.save(action)
        def actions = actionClient.findAllByStartDateAndEndDate(action.startDate, action.endDate)

        then:
        1 * taskService.save(task) >> taskMapper.toTaskInfo(task)
        1 * actionService.save(action) >> actionMapper.toActionInfo(action)
        1 * actionService.findAllByStartDateAndEndDate(action.startDate, action.endDate) >> List.of(actionMapper.toActionInfo(action))

        expect:
        actions.size() == 1
    }


    @MockBean(TaskServiceImpl)
    TaskServiceImpl taskService() {
        Mock(TaskServiceImpl)
    }

    @MockBean(ActionServiceImpl)
    ActionServiceImpl actionService() {
        Mock(ActionServiceImpl)
    }

    static Action updateAction(Action action, String name) {
        action.name = name
        return action
    }
}
```

Тесты для сервисов и репозиториев:

```groovy

import jakarta.inject.Inject
import io.micronaut.context.annotation.Property
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import java.time.LocalDateTime
import spock.lang.Specification
import io.micronaut.data.model.Sort
import io.micronaut.data.model.Pageable

@MicronautTest(startApplication = false)
@Property(name = "datasources.default.driver-class-name", value = "org.testcontainers.jdbc.ContainerDatabaseDriver")
@Property(name = "datasources.default.url", value = "jdbc:tc:postgresql:///db")
class TaskRepositoryTest extends Specification {
    @Inject
    TaskRepository taskRepository
    @Inject
    TaskMapper taskMapper
    Task task

    def setup() {
        task = new Task(
                name: "learning",
                description: "read book",
                startDate: LocalDateTime.of(2024, 11, 1, 0, 0),
                endDate: LocalDateTime.of(2024, 11, 10, 0, 0)
        )
    }

    void cleanup() {
        task = null
    }

    def "save"() {
        when:
        def taskDb = taskRepository.save(task)

        then:
        taskDb.id != null
        taskDb.name == task.name
        taskDb.description == task.description
        taskDb.startDate == task.startDate
        taskDb.endDate == task.endDate
    }

    def "update"() {
        when:
        def taskDb = taskRepository.save(task)

        then:
        taskDb.id != null

        when:
        task.name = "work"
        taskDb = taskRepository.update(task)

        then:
        taskDb.id != null
        taskDb.name == task.name
        taskDb.description == task.description
        taskDb.startDate == task.startDate
        taskDb.endDate == task.endDate
    }

    def "findById"() {
        when:
        def taskDb = taskRepository.save(task)

        then:
        taskDb.id != null

        when:
        taskDb = taskRepository.findById(taskDb.id).orElseThrow()

        then:
        taskDb.id != null
        taskDb.name == task.name
        taskDb.description == task.description
        taskDb.startDate == task.startDate
        taskDb.endDate == task.endDate
    }
}
```
