# Getting Started

This is the JAVA based Blood Bank server code which helps to handling the business logic. 
These instructions will get you a copy of the project up and running on local machine.

## Installing

1. Software Installation
  
 ``` 
$ Download and Install latest version of development IDE for Java (Eclipse or IntelliJ)
$ Java installed and set up in environment variable of the operating system.
  *Note: project has been configured to compile using java 1.8 in pom.xml file. If required change java version.
$ Install Git version 2.18 or above installed as a code editor.	
```

2. Git Repository Clone

``` bash
# clone the repo
$ git clone https://github.com/PiyumeeJ/bloodBankServer.git

# go into app's directory
$ cd my-project

# checkout stable version
$ git checkout master

# install app's dependencies
$ mvn clean install
```
	
3. Running the project in IDE 

  ```
	1.	Right Click the pom.xml --> Run As --> Maven Build.
	2.	Enter the below maven goal in goals field and Run.
  ```
  ```
	mvn clean install 
  ```
  ```
  Run the Main method to start 
  ```     	
	
## Author 
* **PiyumeeJ** - [bloodBankServer](https://github.com/PiyumeeJ/bloodBankServer.git) 
