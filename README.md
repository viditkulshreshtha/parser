# Log Parser
#### A Springboot application to parse the logs from a given file and store it in a database

### Prerequisites:
* JDK 8 or above
* Maven 3.5 or above
* Springboot 2.2.0 or above
* Postgres 9.5 or above
* Intellij IDEA or Eclipse

### Starting the application:
1. Unzip the project and open it in Intellij or Eclipse
2. In the ```application.properties``` file:
   1. Change the ```spring.datasource.url``` to your database url.
   2. Change the ```spring.datasource.username``` to your database username.
   3. Change the ```spring.datasource.password``` to your database password.
3. For improving the performance, you can increase the memory heap size in intellij by:
   1. From the main menu, select Help | Change Memory Settings. 
   2. Set the necessary amount of memory that you want to allocate and click Save and Restart.
4. Run the application
4. Visit ```localhost:8080/swagger-ui.html``` for the API documentation.
5. To upload a file to the application using swagger, visit ```POST /api/v1/logs``` and upload the file.
6. A sample log file ```log-small.txt``` present in root folder is used as a reference for logs.

