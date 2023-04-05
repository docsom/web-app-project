# 23Spring_SoftwareEngineering_Group5

You first download the Dockerfile and run.sh in the environment folder and  
run the following commands:  
**$ docker build -t image_name /path/to/Dockerfile**  
**$ docker run -it image_name**

Inside the docker container, you can run:  
**root@containerID$ sh run.sh**

The container will execute a bash shell by default when the built image is launched.  
It takes **10 minutes** in our local machine. Please wait for building completely.

## REST APIs
In another terminal of the docker container, you can run curl commands that request GET, PUT, and POST methods.  
In the database, there are records about employees and movies.  
### 1. Employees
1. GET  
A GET request is supposed to get the list of employees stored in the database.  
Examples of curl command for GET requests are as follows:  
- curl -X GET http://localhost:8080/employees  
You can get a list of all employees in the database. The expected output of it will be  
**[{"id":1,"name":"Bilbo Baggins","role":"burglar"},{"id":2,"name":"Frodo Baggins","role":"thief"}]**

- curl -X GET http://localhost:8080/employees/1  
You can get a record of employee whose id is 1 in JSON format. The expected output of it will be  
**{"id":1,"name":"Bilbo Baggins","role":"burglar"}**

- curl -X GET http://localhost:8080/employees/99  
You can get an error message if you request a record of the employee not in the database.  
The expected output of it will be  
**Could not find employee 99**

2. POST  
A POST request is suppose to create a new record in the database.  
An example of curl command for POST requests is as follows:  
- curl -X POST http://localhost:8080/employees -H "Content-type:application/json" -d '{"name": "Samwise Gamgee", "role": "gardener"}'  
You can create a new record in JSON format. The id of the employee will be assigned automatically.  
The expected output of it will be  
**{"id":3,"name":"Samwise Gamgee","role":"gardener"}**
3. PUT  
A PUT request is suppose to update an existing record in the database.  
An example of curl command for PUT requests is as follows:  
- curl -X PUT http://localhost:8080/employees/3 -H "Content-type:application/json" -d '{"name": "Samwise Gamgee", "role": "ring bearer"}'  
You can update an existing record of the employee whose id is 3 in JSON format.  
The expected output of it will be  
**{"id":3,"name":"Samwise Gamgee","role":"ring bearer"}**

### 2. Movies
1. GET  
A GET request is supposed to get the list of movies rated whose average ratings are greater than or equal to the given rating in JSON format.  
Examples of curl command for GET requests are as follows:  
- curl -X GET http://localhost:8080/ratings/4  
The expected output of it will be  
**{  
  “title” : “Toy Story (1995)”,  
  “genre”: “Animation|Children's|Comedy”  
},  
{  
  “title” : “xxx”,  
  “genres”: “xxx”  
},  
{ ...**

- curl -X GET http://localhost:8080/ratings/0  
You can get an error message if you request with the given rating which is not 1, 2, 3, 4, or 5.  
The expected output of it will be  
**{"message":"Value of the rating is invalid."}**

2. POST  
A POST request is suppose to create a new record in the database.  
An example of curl command for POST requests is as follows:  
- curl -X POST http://localhost:8080/movie/create -H "Content-type:application/json" -d '{"movieId": "3953", "title": "Avatar 2 (2022)", "genre": "Sci-Fi"}'  
You can create a new record in JSON format. 
The expected output of it will be  
**{"movieId":3953,"title":"Avatar 2 (2022)","genre":"Sci-Fi"}**
3. PUT  
A PUT request is suppose to update an existing record in the database.  
An example of curl command for PUT requests is as follows:  
- curl -X PUT http://localhost:8080/movie/3953 -H "Content-type:application/json" -d '{"movieId": "3953", "titles": "Avatar 2 (2022)", "genre": "Sci-Fi|Action"}'  
You can update an existing record of the movie whose id is 3953 in JSON format.  
The expected output of it will be  
**{"movieId":3953,"title":"Avatar 2 (2022)","genre":"Sci-Fi|Action"}**
