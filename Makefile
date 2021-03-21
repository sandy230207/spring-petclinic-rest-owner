DB_NAME = "mysql-petclinic-owner"
OWNER_SERVER_NAME = "spring-petclinic-rest-owner"


db-build:
	docker build \
		-f docker/db.Dockerfile \
		-t $(DB_NAME) .

db-run:
	docker run -d --rm \
		--name=mysql-petclinic-owner \
		-h localhost \
		-p 3306:3306 \
		-e MYSQL_ROOT_PASSWORD=petclinic \
		-e MYSQL_DATABASE=petclinic \
		$(DB_NAME)

app-build:
	docker build \
		-f docker/app.Dockerfile \
		-t $(OWNER_SERVER_NAME) .

app-run:
	docker run -d --rm \
		--name=spring-petclinic-owner \
		-h localhost \
		--link=mysql-petclinic-owner \
		-p 9966:9966 \
		$(OWNER_SERVER_NAME)

run:
	./mvnw spring-boot:run

test:
	./mvnw test

build:
	./mvnw clean install

clean:
	./mvnw clean