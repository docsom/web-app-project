# Univice: Electonic device rental service for UNIST dormitory resident

## Introduction

Welcome to the user documentation for our web application: **Univice**.
Univice provides a service which allows users to rent and return electronic devices such as monitor and iPad.
Users are specified as UNIST students who live in the dormitory.
This guide will provide you with all necessary information to use our application conveniently.

## 1. Getting Started

### Running the Application on the host machine

You first download the Dockerfile, your-service.war and run.sh in the environment folder.  
Run the following commands:  
**$ docker build -t image_name /path/to/Dockerfile**  
**$ docker run -p 8080:8080 -it image_name**

Inside the docker container, you can run:  
**root@containerID$ sh run.sh**

In run.sh, MongoDB wants to know our space.  
**You should enter "6" and "69".**

The container will execute a bash shell by default when the built image is launched.  
It takes **10 minutes** in our local machine. Please wait for building completely.

### Accessing the Application

To access our application, follow these steps:

1. Open your web browser.
2. Enter the URL http://localhost:8080/your-service.
3. You will be directed to the application's login page.
4. Log in to your account or click the "sign up" button to register your account.
5. If you log in successfully, you will be directed to the application's homepage.

### Registering an Account

Our application is only for UNIST residents. Therefore, you need to authenticate yourself by student number and identification number when you register your account. 
For authentication, the application checks if student number is in dormResident table and if it matches to its identification number.   
Here are the examples of the student number with identification number in the database.  

**student number, identification number**  
20181111,1111111111111  
20192222,2222222222222  
20203333,3333333333333  

Please use one of them to register your account for checking our features.  
Follow these steps to register an account:

1. On the application's login page, you can find the "sign up" button.
2. Click the button to proceed to the registration page.
3. Fill in the required information including id, password, email address, student number and identification number.
4. Click the "Register" button to register your account.
5. You will be directed to the application's login page if there is no error in your information.

## 2. Homepage

If you log in successfully, you can access the homepage.
Also, you will be directed to this page by clicking "Current" button in the sidebar.

### Current Using Device

The application's homepage shows your current using device.
If you have a rented device, you can see the device name, rental start date, end date, and "return" button.
If not, you will see a blank page.

### Returning your rented device

If the end date has passed today, your rented device will be returned automatically.
To return your rented device before the end date, click the "return" button on the application's homepage.

## 2. Device Rental Status Board

You will be directed to this page by clicking "Devices" button in the sidebar. 


### Device Categories

Our service offers two categories of devices, tablet and monitor. To browse devices by category, follow these steps:

1. Select the desired category from the top-right corner of this page.
2. A list of devices in the chosen category will be displayed.

### Device Availability

Devices have different availability status such as "Available" or "Occupied". Here's what each status means:

- **Available**: The device is currently available for rent. "Rent" button will appear.
- **Occupied**: The device is currently rented by another user. "Rent" button will disappear.

### Renting a Device

The rental period starts on the day of renting a device and ends on 3 months later.

## 3. Inquiry Board for using the service better

You will be directed to this page by clicking "Board" button in the sidebar.
If you encounter any issues or have questions while using our service, you can make a post on this page.
The admin will check the post and assist you.

### Browsing the inquiries

## 4. My page

You will be directed to this page by clicking "My page" button in the sidebar.

### User Profile

This page shows your user profile.

- ID
- Student ID
- Current Using Device

### Logout

To log out your account, click the "Logout" button.