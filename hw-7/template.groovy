static String generateFromTemplate(String className, Map<String, String> fields) {
        def template = """
package {{package}}

import groovy.transform.*

@Canonical
@ToString(includeNames = true)
@Builder
class {{className}} {
    {{#fields}}
    {{type}} {{name}}
    {{/fields}}
    
    static {{className}} create(Map properties = [:]) {
        return new {{className}}(properties)
    }
    
    void update(Map updates) {
        updates.each { key, value ->
            if (this.hasProperty(key)) {
                this[key] = value
            }
        }
    }
}
"""
        
        def binding = [
            package: "com.example.model",
            className: className,
            fields: fields.collect { name, type -> [name: name, type: type] }
        ]
        
        return template
            .replace("{{package}}", binding.package)
            .replace("{{className}}", binding.className)
            .replace("{{#fields}}", binding.fields.collect { "    ${it.type} ${it.name}" }.join("\n"))
            .replace("{{/fields}}", "")
    }
}
