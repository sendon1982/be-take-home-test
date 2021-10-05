# be-take-home-test

# DB connect config (since it is just a test project, so it is fine to put secret information)

Username: faBRers6dU

Database name: faBRers6dU

Password: VJcPvVgU8b

Server: remotemysql.com

Port: 3306

# How to run (it is a bit slow to start as DB service from remotemysql.com is slow)

1. `git clone https://github.com/sendon1982/be-take-home-test`

   
2. `cd be-take-home-test`


3. `mvn clean package`


4. `java -jar target/user-service-0.0.1-SNAPSHOT.jar`


5. Run curl command to update password below:

`curl -i -X POST -H 'Content-Type: application/json' -d '{"email":"sendon1982@gmail.com", "newPassword": "Abcd@1234", "confirmNewPassword": "Abcd@1234"}' 'http://localhost:8080/users/changePassword'`

6. Added a login endpoint to verify password udpated correctly

`curl -i -X POST -H 'Content-Type: application/json' -d '{"email":"sendon1982@gmail.com", "newPassword": "Abcd@1234"}' 'http://localhost:8080/users/login'`

# Some concerns and improvement

1. User should login first before user can change password


2. Update `SecurityConfig` to only allow request with login token. Currently, it is allowed for all the request


3. Maybe add unit test for dao layer


4. Error message for validation can be more elegant