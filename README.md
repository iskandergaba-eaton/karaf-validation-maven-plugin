# maven-plugin-dependency

Build and run the plugin on karaf-dependency-check-plugin:
```bash
mvn clean install && mvn com.eaton.maven.plugin:karaf-helper-plugin:1:analyze
```

Build and run the plugin on a complex project:
```bash
mvn clean install && mvn -f src/test/resources/multiproject-test/aggregator-pom.xml com.eaton.maven.plugin:karaf-helper-plugin:1:analyze
```
