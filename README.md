# spring-petclinic-rest-owner

## Prerequisite
Build and run the [docker image/container for MySQL database](https://github.com/sandy230207/mysql-petclinic)

## Start API server
Make sure you are in the root directory of the project ( the directory would be like `*/spring-petclinic-rest-owner` )

Export environment variable
```sh
export MYSQL_PASSWORD=petclinic
export export MYSQL_HOST=127.0.0.1
```

Run server
```sh
make run
```

## Kubernetes
[Document](https://hackmd.io/@NLsj-cc1SqyixEbz3Sh6wA/SJjI98iE_)