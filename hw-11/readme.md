
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

```groovy
```

## Реализация с Spring

```groovy
```
