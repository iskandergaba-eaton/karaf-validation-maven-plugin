# maven-plugin-dependency

Build and run the plugin on karaf-dependency-check-plugin:
```bash
mvn clean install && mvn com.eaton.maven.plugin:karaf-validation-maven-plugin:1:check-dependencies
```

Build and run the plugin on a complex project:
```bash
mvn clean install && mvn -f src/test/resources/multiproject-test verify
```
