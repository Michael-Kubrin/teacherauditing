# Teacher Auditing

## Application Docs

Swagger UI: `GET /docs`

Download as YAML: `GET /api/docs.yaml`

## Env variables

`SERVER_HOST` - application web-server host. Example: `127.0.0.1`.

`SERVER_PORT` - application web-server port. Example: `9999`.

`SERVER_ADMIN_BEARER` - bearer token for executing requests as admin. 
Actually this could be any string. Example: `abobus`.

`POSTGRES_HOST` - postgresql database host. Example: `127.0.0.1`.

`POSTGRES_PORT` - postgresql database port. Example: `5678`.

`POSTGRES_DB_NAME` - postgresql database name. Example: `teacher_auditing`.

`POSTGRES_USER` - postgresql database user. Example: `teacher_auditing`.

`POSTGRES_PASSWORD` - postgresql database password. Example: `teacher_auditing`.

## Postgres usage

Application will create `public` schema in `POSTGRES_DB_NAME` database. 
When successfully connected, application will migrate schema with scripts defined in 
project resources.

## Running app

### Build via SBT

Before build cleanup assembly files. Just delete `target` folder 
or delete file `target/scala-2.13/teacherauditing.jar`.

After cleanup run `sbt`. This action assembles fat-jar in `target/scala-2.13` directory.

```shell
sbt "clean; assembly"
```

### Docker

Build image from Dockerfile using this command:

```shell
docker build -t teacherauditing:0.0.1 .
```

### Run example via docker-compose

```yaml
version: '3'

services:
  app:
    image: 'teacherauditing:0.0.1'
    build:
      context: .
    container_name: app
    depends_on:
      - postgres
    restart: on-failure
    ports:
      - "9999:9999"
    environment:
      - SERVER_HOST=0.0.0.0
      - SERVER_PORT=9999
      - SERVER_ADMIN_BEARERS=exodus
      - POSTGRES_HOST=postgres
      - POSTGRES_PORT=5432
      - POSTGRES_DB_NAME=teacherauditing
      - POSTGRES_USER=teacherauditing
      - POSTGRES_PASSWORD=teacherauditing

  postgres:
    image: 'postgres:latest'
    container_name: postgres
    environment:
      - POSTGRES_PORT=5432
      - POSTGRES_PASSWORD=teacherauditing
      - POSTGRES_USER=teacherauditing
    ports:
      - "5432:5432"
```

In the project directory (near the `docker-compose.yaml`):

```shell
docker-compose up
```


