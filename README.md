## Команды
### Переустановка сборки:
.\mvnw install
### REST (без RabbitMQ):
.\mvnw spring-boot:run -pl demo-rest -am
### REST (с RabbitMQ): 
.\mvnw -pl demo-rest spring-boot:run -DskipTests
### Аудит (c RabbitMQ):
.\mvnw -pl audit-service spring-boot:run -DskipTests
### gRPC-сервер аналитики:
.\mvnw spring-boot:run -pl grpc-analytics-server -DskipTests
### gRPC-клиент диагностирования:
.\mvnw spring-boot:run -pl grpc-diagnosis-client -DskipTests
### notification-service (WebSocket push):
.\mvnw spring-boot:run -pl notification-service -DskipTests
## URL
### SwaggerUI:
http://localhost:8080/swagger-ui.html
### GraphiQL:
http://localhost:8080/graphiql
### RabbitMQ:
http://localhost:15672
### Аудит:
http://localhost:8081/api/audit
### Центр уведомлений:
http://localhost:8084
