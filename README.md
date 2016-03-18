# Rotation

Simple Spring Data, Web, Angular project for managing rotating schedules. It also includes a notification system that alerts the next person in the rotation when the schedule rotates.

This application can be used to manage "on-call" rotations, temporary team assignments, designated driver duty or even snack-mom schedules.

By default, its configured to use an in-memory H2 database, but it can be configured to work with PostgreSql.

Application properties can be overriden in the usual Spring way.  The relevant properties are listed below:

spring.datasource.driverClassName=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://localhost/rotation
spring.datasource.username=rotation
spring.datasource.password=rotation
emailHost=<smtp host name>
emailAuthUser=<from email address>
emailAuthPass=<from email passworld>
emailSmtpPort=25
emailTransportProtocol=smtp
