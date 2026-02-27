## Задание 11: Backend с БД для todo-list
Научиться создавать CRUD-приложения на Grails, Micronaut, Spring Boot.


## Инструкция
Перенести логику todo-list на GORM:
1. Создать ветку homework-10, переключиться на нее
2. Поменять классы модели в модуле hw03-gradle, таким образом, чтобы они соответствовали модели хранения данных в GORM
3. Добавить нужные классы сервисов, чтобы контроллеры могли взаимодействовать с уронем хранения данных

Формат сдачи: 
- ссылка на PR в github
- docker-compose файл для запуска приложения

## Критерии оценивания

1. Задание сдано на проверку - 1 балл
2. Создан PR - 1 балл
3. Есть CI/CD пайплайн в github actions, по которому можно определить успешность сборки - 1 балла
4. Классы модели соответствуют модели хранения данных в GORM -2 балл
5. Реализован слой хранения данных - 2 балл
6. Реализованы классы сервисов, для взаимодействия контрллеров с уровнем хранения данных - 2 балла
7. Корректно настроена конфигурация приложения - 1 балл

Задание принимается, если набрано минимум 7 баллов


## Реализация с Grails

Преобразуйте класс Entity (не забудьте добавить необходимые ограничения):
```groovy
import grails.gorm.annotation.Entity
import org.grails.datastore.gorm.GormEntity

@Entity
class Task implements GormEntity {
    String name
    LocalDateTime startTime
    LocalDateTime endTime
    static hasMany = [actions: Action]

    static constraints = {
        name blank: false
        startTime nullable: false
        endTime nullable: false
        actions nullable: true
    }

    static mapping = {
        actions cascade: 'all-delete-orphan'
    }
}
```

Преобразуйте ваш класс-сервис (из предыдущего ДЗ)
```groovy
import grails.gorm.transactions.Transactional
import java.time.LocalDateTime

@Transactional
class TaskServiceImpl implements TaskService {

    @Override
    Task save(String name, LocalDateTime startTime, LocalDateTime endTime) {
        Task task = new Task(
                name: name,
                startTime: startTime,
                endTime: endTime
        )
        task.save(flush: true)
        return task
    }

```

крайне желательно, чтобы был интерфейс, у которого может быть несколько реализаций (в зависимости от способа хранения данных):

```groovy
import grails.gorm.services.Service
import java.time.LocalDateTime

@Service(Task)
interface TaskService {
    Task save(String name, LocalDateTime startTime, LocalDateTime endTime)
    void delete(Serializable id)
    Task findById(Serializable id)
    List findAll()
    Number count()
}
```

## Реализация с Micronaut

Создайте репозиторий и используйте в сервисе:
```groovy

import io.micronaut.data.annotation.*
import io.micronaut.data.repository.PageableRepository
import java.time.LocalDateTime

@Repository
interface TaskRepository extends PageableRepository<Task, UUID> {

    @Query(
            nativeQuery = true,
            value = "SELECT t.* FROM tasks t WHERE :date BETWEEN t.start_date AND t.end_date"
    )
    List<Task> findAllByDate(LocalDateTime date)

    @Query(
            nativeQuery = true,
            value = "SELECT t.* FROM tasks t WHERE :startDate BETWEEN t.start_date AND t.end_date OR :endDate BETWEEN t.start_date AND t.end_date"
    )
    List<Task> findAllByStartDateAndEndDate(LocalDateTime startDate, LocalDateTime endDate)

    @Query(
            nativeQuery = true,
            value = "SELECT COUNT(t.id) FROM tasks t WHERE :date BETWEEN t.start_date AND t.end_date"
    )
    long countAllByDate(LocalDateTime date)
}
```

Преобразуйте класс Entity (не забудьте добавить необходимые ограничения):
```groovy

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import groovy.transform.EqualsAndHashCode
import jakarta.persistence.*
import java.time.LocalDateTime
import groovy.transform.ToString
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.NotBlank
import io.micronaut.serde.annotation.Serdeable
import io.micronaut.core.annotation.Introspected

@Entity
@ToString
@Serdeable
@Introspected
@EqualsAndHashCode
@Table(name = "tasks")
class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id

    @NotNull
    @NotBlank
    String name

    @NotNull
    @NotBlank
    String description

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "start_date")
    LocalDateTime startDate

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "end_date")
    LocalDateTime endDate

    @JsonIgnore
    @OneToMany(mappedBy = "task", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    Set<Action> actions = new HashSet<>()

    Task(String name, String description, LocalDateTime startDate, LocalDateTime endDate, Set<Action> actions) {
        this.name = name
        this.description = description
        this.startDate = startDate
        this.endDate = endDate
        this.actions = actions
    }

    Task() {

    }
}
```

## Реализация с Spring

```groovy
```
