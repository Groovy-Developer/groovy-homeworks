# ДЗ 4: Обработчик JSON-ов

## Инструкция:
Реализовать программу для скачивания, парсинга и преобразования json-файлов.

- Создать ветку homework-4, переключиться на нее
- Создайте модуль hw04-json-gradle
- Реализовать в модуле hw04-json-gradle логику работы программы.
- Программа скачивает json файл по заданному url (в качестве примера можно взять json из репозитория или по ссылке https://github.com/CycloneDX/bom-examples/blob/master/SBOM/cern-lhc-vdm-editor-e564943/bom.json/
- Программа парсит скачанный json файл и генерирует html страницу по шаблону
```html
<div>
  <div id="employee">
    <p>name</p><br/>
    <p>age</p><br/>
    <p>secretIdentity</p><br/>
    <ul id="powers">
      <li>power</li>
    </ul>
  </div>
</div>
```
- Программа преобразует скачанный json файл в xml файл
```xml
<employee>
  <name>name</p><br/>
  <age>age</p><br/>
  <secretIdentity>secretIdentity</secretIdentity><br/>
  <powers>
    <power>powerName</power>
  </powers>
</employee>
```

### Формат сдачи:

- ссылка на PR в github
