# MILESTONE2

You first download the Dockerfile and run.sh in the environment folder and  
run the following commands:  
**$ docker build -t image_name /path/to/Dockerfile**  
**$ docker run -it image_name**

Inside the docker container, you can run:  
**root@containerID$ sh run.sh**

In run.sh, MongoDB wants to know our space.  
**You should enter "6" and "69".**

The container will execute a bash shell by default when the built image is launched.  
It takes **10 minutes** in our local machine. Please wait for building completely.

## Features
Our web application manages electronic device rental service for UNIST dormitory residents. In below, it explains about 
features required to achieve the goal of our application, and APIs to implement each feature.

### Register & Login
Our application is only for UNIST residents. Therefore, if user cannot authenticate itself by student ID and identification
number, the user cannot register to our app.
1. registerNewUser 
- curl -X POST http://localhost:8080/register -H "Content-type:application/json" -d '{"movieId": 3953, "title": "Avatar 
2 (2022)", "genre": "Sci-Fi"}'
You can get a list of all employees in the database. The expected output of it will be  
**[{"id":1,"name":"Bilbo Baggins","role":"burglar"},{"id":2,"name":"Frodo Baggins","role":"thief"}]**

### Device Rental Status Board

### Rent & Return Device

### Inquiry Board for using the service better

