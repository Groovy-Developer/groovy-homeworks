## ДЗ 8: настройка пайплайна для сборки проекта

**Описание:**
Создать пайплайн сборки проекта, создаваемого в предыдущих заданиях.



- Создать ветку homework-7, переключиться на нее
- Запустить Jenkins локально из docker-образа, настроить Jenkins для работы с java/groovy проектами
- Создать скрипт Jenkins.groovy (scripted pipeline), который будет содержать следующую функциональность:
  - переключаться на выбранную ветку
  - в параллельном режиме выполнять сборку и тестирование каждого из модулей 1-5
  - перед сборкой каждого модуля должна выполняться кастомная функция PrintSysTime, которая выводит системное время и название ветки, на которой выполняется сборка
  - после сборки jar файла должно происходить создание докер-образа для данного модуля
  - после создания докер-образа, задеплоить его в docker hub
  - вызвать кастомную функцию, которая определяет кол-во файлов в папке модуля на ветке сборки
- Загрузить созданный скрипт в Jenkins, проверить, что сборка выполняется успешно


**Формат сдачи:**
- ссылка на PR в github
- скриншот прошедшей сборки в jenkins


**Критерии оценивания:**
- Задание сдано на проверку - 1 балл
- Создан PR - 1 балл
- Jenkins настроен на выполнение сборок на ветках репозитория -1 балл
- Написаны кастомные функции PrintSysTime и CalculateFilesCount - 2 балл
- Пайплайн содержит все указанные в задании шаги - 3 балл
- Пайплайн успешно выполняется для репозитория с проектом ДЗ - 2 балла

Задание принимается, если набрано минимум 7 баллов

### Вспомогательные материалы:

Пример кастомных функций, которые можно использовать при выполнении задания:

```groovy
// Кастомная функция: Вывод времени и ветки
def PrintSysTime(contextName) {
    def now = new Date().format("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone('Europe/Moscow'))
    echo """
    ****************************************************
    TIME: ${now}
    BRANCH: ${env.BRANCH_NAME}
    MODULE/CONTEXT: ${contextName}
    ****************************************************
    """
}

// Кастомная функция: Подсчет файлов в папке модуля
def countFiles(modulePath) {
    // Находимся внутри dir(moduleName), поэтому считаем файлы в текущей папке
    def fileCount = sh(script: "find . -maxdepth 1 -not -path '*/.*' | wc -l", returnStdout: true).trim()
    echo "RESULT: Module '${modulePath}' has ${fileCount} visible files/folders in the root."
}
```

Пример с parallel секцией:
```groovy
 stage('Parallel Build & Deploy Modules') {
            parallel {
                stage('HW02') {
                    steps {  }
                }
                stage('HW03') {
                    steps {  }
                }
                stage('HW07') {
                    steps {  }
                }
                stage('HW08') {
                    steps {  }
                }
            }
        }
    }
```

Для публикации образа на dockerhub понадобятся credentials (так как это чувствительные данные - не хардкодим их, а используем переменные окружения)

```groovy
environment {
        // Укажите ваш логин на Docker Hub
        DOCKER_HUB_USER = ''
        DOCKER_CREDS_ID = ''
}
```

Сама сборка и публикация docker-образа:

```groovy
// Предполагается, что Dockerfile лежит в корне папки модуля
def imageName = "${DOCKER_HUB_USER}/${moduleName}:${env.BRANCH_NAME.replace('/', '-')}-${env.BUILD_ID}"

dir(moduleName) {
        def customImage = docker.build(imageName, "-f Dockerfile .")

        // Деплой в Docker Hub
        docker.withRegistry('index.docker.io', DOCKER_CREDS_ID) {
            customImage.push()
            customImage.push('latest')
        }
}
```
