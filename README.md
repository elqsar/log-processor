### Log analyzer

Simple akka stream example:

* Upload logback logs
* Parse logs
* Persist to mongo db
* Create 2 endpoints - get by page and search by message

How to run:  

* sbt run

How to test:

* sbt test

### Set up environment

* Local environment: ```mongod --dbpath data/db``` - to create local Mongo database and use ```dev``` in configuration file
* If using mongo provider like MongoLab etc. export environment variables ```DBUSER, DBPASSWORD and PORT```
* Add database url to ```application.conf``` 