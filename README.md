## BackendDemo is a small application for discovering popular repositories on GitHub. It allows user:<br>
  - to list most popular repositories created from given date onwards<br>
  - to add given repository to their favourites
  - to list their favourite repositories

## Prerequisites
  - JDK 17
  - Git
    
## Running locally
- clone the project
```markdown
 git clone https://github.com/aga-malaga/backendDemo.git
```
- there are several ways to run SpringBoot application on your machine:

 start the application with Gradle Wrapper:
```markdown
  cd RepoViewer
 ./gradlew bootRun
```
 build the application with Gradle Wrapper and then start the JAR file:
```markdown
  cd backendDemo
 ./gradlew build
  cd build/libs
  java -jar backendDemo.jar
```
Swagger documentation available:
http://localhost:8080/swagger-ui/index.html
