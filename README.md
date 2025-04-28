# Weather App
This repository contains code for an application connected to OpenWeather.

## Config
This is the example .env file:
```
DB_USER=<username>
DB_PSWD=<passwd>
OWM_KEY=<api-key>
```

## Profiles
* `prod` - profile for production that uses a mysql database
* `devel` - profile for development that uses an in-memory database
* `test` - profile for testing that uses an in-memory database