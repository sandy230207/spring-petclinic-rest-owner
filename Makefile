db_name = "sandy230207/mysql-petclinic-owner:v1"

db-build:
	docker build \
		-f ./src/main/resources/db/Dockerfile \
		-t $(db_name) .

db-run:
	docker run --rm \
		--name=mysql-petclinic-owner \
		-h localhost \
		-p 3306:3306 \
		-e MYSQL_ROOT_PASSWORD=petclinic \
		-e MYSQL_DATABASE=petclinic \
		$(db_name)

# db-run:
# 	docker run --name mysql-petclinic -e MYSQL_ROOT_PASSWORD=petclinic -e MYSQL_DATABASE=petclinic -p 3307:3306 mysql:5.7.8

run:
	./mvnw spring-boot:run

test:
	./mvnw test

build:
	./mvnw clean install

clean:
	./mvnw clean