OWNER_SERVER_NAME = "sandy230207/spring-petclinic-rest-owner:v1"
# OWNER_SERVER_NAME = "a123453906/spring-petclinic-rest-owner:latest"

app-build:
	docker build \
		-f Dockerfile \
		-t $(OWNER_SERVER_NAME) .

app-push:
	docker push $(OWNER_SERVER_NAME)

app-run:
	docker run -d --rm \
		--name=spring-petclinic-owner \
		-h localhost \
		--link=mysql-petclinic-owner \
		-e MYSQL_HOST="chart-example.local" \
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