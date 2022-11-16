# maven-plugin-dependency

Build and run the plugin on karaf-dependency-check-plugin:
```bash
mvn clean install && mvn com.eaton.maven.plugin:karaf-validation-maven-plugin:1:check-dependencies
```

Build and run the plugin on a complex project:
```bash
mvn clean install && mvn -f src/test/resources/multiproject-test verify
```

TODO list:
* Add feature.xml management, to validate it you can run the following command should print a message if the feature works:
```bash
mvn clean install -s $M2_SETTINGS && mvn -f src/test/resources/dependency-check-multi-project-test verify -s $M2_SETTINGS | grep spoon-core -A 20
```
* Check range inside of transitive feature, you can validate it by using the following command:
```bash
mvn clean install -s $M2_SETTINGS && mvn -f src/test/resources/dependency-check-multi-project-test verify -s $M2_SETTINGS | grep osgi.core -A 20
```
* List high level module only instead of all modules having the dependency
* Manage a whitelist file
