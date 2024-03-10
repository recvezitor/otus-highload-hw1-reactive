# hw1

монолит, без ОРМ, без оптимизации БД, ручная накатка схемы
+ реактивщина
- генерация рест апи через openapi для реактивности не нашел, поэтому забрал только модели, чтобы было одинаково с нереактивным стеком
- 
### install

- mvn clean install
- docker compose up
- run sql scripts:
    - src/main/resources/db/01_schema-init.sql
    - src/main/resources/db/02_data-init.sql

docker build -f Dockerfile.jvm -t otus-highload-hw1-reactive:latest .
docker images
     
### launch

via IDEA:
DB_USERNAME=postgres;DB_URL=postgresql://localhost:5432/postgres;DB_PASSWORD=postgres;
-Dquarkus.profile=prod

via docker:
docker run -i --rm -p 8080:8080 -e DB_URL=postgresql://host.docker.internal:5432/postgres -e DB_USERNAME=postgres -e DB_PASSWORD=postgres otus-highload-hw1-reactive:latest

### publish

docker tag otus-highload-hw1-reactive:latest recvezitor/otus-highload-hw1-reactive:latest
docker login -> recvezitor/password
docker push recvezitor/otus-highload-hw1-reactive:latest


### TODO

TODO

- научиться форматировать лог чтобы он был фиксированной ширины
  2024-03-09 14:55:41 INFO  [com.dim.lon.pck.bla] - messsage
  2024-03-09 14:55:41 INFO  [com.dim.StartUp    ] - messsage
- Понять нафиг нужен JBossLogManager
- как уюрать кракозябры из лога jdbc
  Caused by: org.postgresql.util.PSQLException: ������� firstName �� ������� � ���� ResultSet���.
- По нормальному обрабатывать ошибки
- Засунуть инициализацию БД внутрь образа или отдельным образом