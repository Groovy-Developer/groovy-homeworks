## Задание 9: API и web UI для todo-list

## Инструкция
Создать веб-интерфейс для todo-list:
1. Создать ветку homework-9, переключиться на нее
2. Добавить интеграцию с фреймворком Grails или Micronaut
3. Добавить rest-api для работы с todo-list
4.* Добавить web ui для приложения todo-list

Формат сдачи: 
- ссылка на PR в github
- postman-коллекция с запросами
- docker-compose файл для запуска приложения

## Примеры реализации:

### Grails:

```groovy
import grails.rest.RestfulController

class TaskController extends RestfulController<Task> {
    static responseFormats = ['json']
    def todoListService

    TaskController() {
        super(Task)
    }

    @Override
    def save() {
        def task = new Task(request.JSON)
        if (todoListService.addTask(task)) {
            respond task, [status: 201]
        } else {
            respond([error: 'Overlap detected or invalid data'], status: 400)
        }
    }

    @Override
    def delete() {
    }

    def editActions() {
    }

    def getTasksByDate(String dateStr) {
    }

    def getCountByDate(String dateStr) {
        Date date = Date.parse('yyyy-MM-dd', dateStr)
        respond([count: todoListService.getTaskCountForDate(date)])
    }

    def getBusyPeriods(String dateStr) {
    }
}
```

### Micronaut

```groovy
import io.micronaut.http.HttpStatus
import jakarta.annotation.Nullable
import jakarta.inject.Inject
import jakarta.validation.Valid
import groovy.util.logging.Slf4j
import io.micronaut.validation.Validated
import io.micronaut.http.annotation.*
import io.micronaut.data.model.Pageable
import java.time.LocalDateTime

@Slf4j
@Validated
@Controller("/tasks")
class TaskController {
    @Inject
    TaskServiceImpl taskService

    @Status(HttpStatus.CREATED)
    @Post(produces="application/json")
    TaskInfo save(@Body @Valid Task task) {
    }

    @Put(uri="/{id}", produces="application/json")
    TaskInfo updateById(@Body @Valid Task task, @PathVariable UUID id) {
    }

    @Get(uri="/{id}", produces="application/json")
    TaskInfo findById(@PathVariable UUID id) {
    }

    @Get(produces="application/json")
    List<TaskInfo> findAll(Pageable pageable) {
    }

    @Status(HttpStatus.NO_CONTENT)
    @Delete(uri="/{id}", produces="application/json")
    void deleteById(@PathVariable UUID id) {
    }

    @Get(uri="/list{?date}", produces="application/json")
    List<TaskInfo> findAllByDate(@QueryValue @Nullable LocalDateTime date) {
        log.info("Send get request /tasks/list?date={}", date)
        return taskService.findAllByDate(date)
    }

    @Get(uri="/range{?startDate,endDate}", produces="application/json")
    List<TaskInfo> findAllByStartDateAndEndDate(@QueryValue @Nullable LocalDateTime startDate, @QueryValue @Nullable LocalDateTime endDate) {
    }

    @Get(uri="/count{?date}", produces="application/json")
    long countAllByDate(@QueryValue @Nullable LocalDateTime date) {
    }
}
```
