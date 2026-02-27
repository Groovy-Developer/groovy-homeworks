## Занятие 13: Создание плана нагрузочного тестирования с использованием Groovy-скриптов

На практике настроить план нагрузочного тестирования;

Расширить стандартную функциональность инструментов JMeter с помощью Groovy-скриптов.

## Инструкция
Разработать план нагрузочного тестирования для приложения todo-list.
1. Определить, какие метрики необходимо учитывать
2. Определить список наиболее нагруженных операций
3. Реализовать план нагрузочного тестирования в JMeter. План должен содержать Sampler-ы и PostProcessor-ы, написанные на groovy
4. Добавить графики и отчеты для отображения результатов нагрузочного тестирования

Формат сдачи: 
- ссылка на PR в github
- отчет о проведении нагрузочного тестирования

Критерии оценивания:
1. Задание сдано на проверку - 1 балл
2. Создан PR - 1 балл
3. Определен набор метрик для нагрузки -1 балл
4. Определен набор наиболее нагруженных операций - 1 балл
5. Реализован план нагрузочного тестирования в JMeter с использованием jsr223 - 4 балла. 
6. Отображены графики и отчеты по результатам тестирования - 2 балла

## Примеры с JMeter DSL

build.gradle:
```groovy
testImplementation 'us.abstracta.jmeter:jmeter-java-dsl'
```

```groovy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.micronaut.context.annotation.Property
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import java.time.Duration
import java.time.LocalDateTime
import spock.lang.Specification
import org.apache.http.entity.ContentType\
import us.abstracta.jmeter.javadsl.core.TestPlanStats
import static us.abstracta.jmeter.javadsl.JmeterDsl.*;

@MicronautTest
@Property(name = "datasources.default.driver-class-name", value = "org.testcontainers.jdbc.ContainerDatabaseDriver")
@Property(name = "datasources.default.url", value = "jdbc:tc:postgresql:///db")
class PerfomansTest extends Specification {
    Task task
    Action action
    Gson gson

    String taskId
    String actionId

    static final String BASE_URL = "http://localhost:9090"

    def setup() {
        task = new Task(
                name: "learning",
                description: "read book",
                startDate: LocalDateTime.of(2024, 11, 1, 0, 0),
                endDate: LocalDateTime.of(2024, 11, 10, 0, 0),
        )
        action = new Action(
                name: "read",
                description: "read math",
                startDate: LocalDateTime.of(2024, 11, 2, 0, 0),
                endDate: LocalDateTime.of(2024, 11, 3, 0, 0),
                task: task
        )
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .serializeNulls()
                .create()
    }

    void cleanup() {
        task = null
        action = null
        gson = null
    }

    def "saveTask"() {
        when:
        TestPlanStats stats = testPlan(
                threadGroup(10, 20,
                        httpSampler(BASE_URL + "/tasks")
                                .post(gson.toJson(task), ContentType.APPLICATION_JSON)
                                .children(
                                        jsr223PostProcessor(
                                                "if (prev.responseCode == '400') { prev.successful = true }")
                                )
                ),
                jtlWriter("build/jtls"),
                htmlReporter("build/reports/jmeter")
        ).run()

        then:
        stats.overall().sampleTimePercentile99() <= Duration.ofSeconds(1)
    }

    def "saveAction"() {
        when:
        TestPlanStats stats = testPlan(
                threadGroup(10, 20,
                        httpSampler(BASE_URL + "/actions")
                                .post(gson.toJson(action), ContentType.APPLICATION_JSON)
                                .children(
                                        jsr223PostProcessor(
                                                "if (prev.responseCode == '400') { prev.successful = true }")
                                )
                ),
                jtlWriter("build/jtls"),
                htmlReporter("build/reports/jmeter")
        ).run()

        then:
        stats.overall().sampleTimePercentile99() <= Duration.ofSeconds(1)
    }

    def "findByIdTask"() {
        when:
        TestPlanStats stats = testPlan(
                threadGroup(10, 20,
                        httpSampler(BASE_URL + "/tasks")
                                .post(gson.toJson(task), ContentType.APPLICATION_JSON)
                                .children(
                                        jsonExtractor(taskId, "[].id"),
                                        debugPostProcessor()
                                ),
                        httpSampler(BASE_URL + "/tasks/${taskId}")
                ),
                jtlWriter("build/jtls"),
                htmlReporter("build/reports/jmeter")
        ).run()

        then:
        stats.overall().sampleTimePercentile99() <= Duration.ofSeconds(1)
    }

    def "findByIdAction"() {
        when:
        TestPlanStats stats = testPlan(
                threadGroup(10, 20,
                        httpSampler(BASE_URL + "/actions")
                                .post(gson.toJson(action), ContentType.APPLICATION_JSON)
                                .children(
                                        jsonExtractor(actionId, "[].id"),
                                        debugPostProcessor()
                                ),
                        httpSampler(BASE_URL + "/actions/${actionId}")
                ),
                jtlWriter("build/jtls"),
                htmlReporter("build/reports/jmeter")
        ).run()

        then:
        stats.overall().sampleTimePercentile99() <= Duration.ofSeconds(1)
    }
}
```

