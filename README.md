#Pre-Requisites

Java 1.8
Maven 3.0 or above
Postman for API testing
#Follow below mentioned steps to clone from Repository

Fork this repository and clone the code in your local machine

Clone using git clone - https://github.com/Suraj-Git-Cloud/Project_POC.git

Navigate to Project_POC folder cd Project_POC

run maven command 'mvn clean spring-boot : run' or run mvn clean install and copy the jar name from target folder and run command 'java -jar {jarname} 'folder in comand prompt.

open any Api testing tool (Postman) hit the url 'http://localhost:8096/api/v1/robobank/inputfile/{fileName}' Note- The code is designed as per requirement to read only csv or xml file.

Verify the responses { "records": [ "112806 Clothes from Peter de Vries", "112806 Tickets for Erik Dekker" ] }

You will get the report of records (reference that are not unique and the end balance is incorrect in the file)

Further Instructions on Release
Right click on the Assignment, select Run As -> JUnit Test to run your Assignment.
