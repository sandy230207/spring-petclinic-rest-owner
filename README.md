# spring-petclinic-rest-owner
## Start API server
Make sure you are in the root directory of the project ( the directory would be like `*/spring-petclinic-rest-owner` )
1. Build the docker image for MySQL database if you **never build** it.
    ```sh
    make db-build
    ```
2. Run MySQL container
    ```sh
    make db-run
    ```
3. Start API server
    ```sh
    make run
    ```