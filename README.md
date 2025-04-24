# Weather App
This repository contains code for an application connected to OpenWeather.

## Config
This is the example app config in `application.properties`:
```
spring.datasource.url=jdbc:mysql://localhost:3306/PPJweatherDB
spring.datasource.username=<username>
spring.datasource.password=<password>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

openweather.api.key=<api-key>
openweather.baseurl=https://history.openweathermap.org/data/2.5

#spring.profiles.active=devel

#Hibernate
spring.jpa.open-in-view=false
spring.jpa.properties.hibernate.transaction.jta.platform=org.hibernate.engine.transaction.jta.platform.internal.NoJtaPlatform
spring.jpa.show-sql=false
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQLDialect
```

Available is also config for dev/test profile `application-devel.properties`:
```
spring.datasource.url=jdbc:hsqldb:mem:inmemorydb;DB_CLOSE_DELAY=-1
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.driver-class-name=org.hsqldb.jdbc.JDBCDriver

# Hibernate
spring.jpa.defer-datasource-initialization=true
spring.jpa.hibernate.ddl-auto=create
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.HSQLDialect

```
> **Warning**: The dev profile uses an in-memory database, while production uses a MySQL database.