# Teacher Auditing

## Docs

Swagger UI: `GET /docs`

Download as YAML: `GET /api/docs.yaml`

## Docker

You can build image from Dockerfile using this command: 

```shell
docker build -t teacherauditing:0.0.1 .
```

## Run example

Create network

```shell
docker network create \ 
  --driver=bridge
  --subnet=172.28.0.0/16 \
  --gateway=172.28.1.1 \
  sibadi-teacher-auditing-network
```

Run postgres container

```shell
docker run \
  --name sibadi-postgres \
  -e POSTGRES_PASSWORD=teacherauditing \
  -e POSTGRES_USER=teacherauditing \
  -p 9998:5432 \
  -d postgres
```

Run application container

```shell
docker run \
  --name sibadi-teacher-auditing \
  -e JAVA_OPTS \
  -p 9999:9999 \
  -d teacherauditing:0.0.1
```

Connect both containers to created network

```shell
docker network connect \
  --ip=172.28.0.1 \
  sibadi-teacher-auditing-network \
  sibadi-teacher-auditing
```

```shell
docker network connect \
  --ip=172.28.0.2 \
  sibadi-teacher-auditing-network \
  sibadi-postgres
```
