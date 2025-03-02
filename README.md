# MSA Project

## Topic:

Online movie ticket booking system

## Members: 

B115 Tanay Shah
B116 Adit Khandelwal
B119 Suhani Pugalia
B124 Athang Yawalkar

## Microservices:

### 1.User Microservices

Users have the following attributes:

1. Userid
2. Username
3. Email
4. Password
5. Role (user,admin,etc)
6. A timestamp showing time of creation of user

Crud operations implemented:

1. Creating new Users
2. Getting all Users
3. Getting user by ID 
4. Updating user details by ID
5. Deletion of user by ID

#### How to run locally?

1. Clone the entire repository and open in intellij ide
2. Make sure that Maven script has been detected or else add manually
3. Make a users_db in mysqlworkbench connection on port 3306
   (you run the code: `CREATE DATABASE users_db;`)