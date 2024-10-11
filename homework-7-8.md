- любую библиотеку кодогенерации для java
- самостоятельно
```groovy
def template = """
package ${packageName}

class ${className} {

}
"""
new File(dir, "${fileName}.groovy"
```
- регистрация перед компиляцией
```groovy
project.tasks.register('generate')
//GrrovyCompile, JavaCompile
project.tasks.withType(JavaCompile).configure {
  dependsOn generate
}
```

```
class OurPlugin implements Plugin {

def apply(Project project) {
// register extension
}
}

class OurTask implements Task {
@Input
abstract Property<String> fileName()

  def apply() {
  }
}


```groovy
def PrintSysTime() {
 def scriptBody = """
  """
  sh(script: "echo "
}

def filesCOunt() {
  sh()
}

node {
// /var/lib/docker
  state('parallel stage') {
    def modulesStages = [:]
    // todo: наполнить
    // gradlw biuld --parallel -p 
    // cd to module directory из этой директории запускаем gradle build
    // используем разные врапперы (не один и тот же файл/gпроцесс)
    // собираем именно build.gradle именно конкретного модуля (родительский build,gradle не используем)
    // можно попробовать опцию parallel
    "stageName" : {
      printTime()
      build()
      dockerBuild()
      runTest()
    }
    parallel modulesStages
  }
}
```
