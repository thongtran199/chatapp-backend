## Installation Guide
1. Install Docker and Docker Compose.

2. Run the command:

    ``` bash
    ./mvnw clean package
    ```
    
3. Run the command:

    ``` bash
    docker-compose up --build -d
    ```
4. Access http://localhost:8080 to use the API.

5. MySQL runs with username "root" and password "root" on port 3307 on your machine.

6. Register an account and start using it.

## WebSocket Connection Addresses
To connect to the WebSocket, use the following address:

    ``` bash
   ws://localhost:8080/ws-endpoint
    ```
You can receive messages from the WebSocket through the following topics:

- /topic/{your-topic}: Receive messages from specific topics.
- /queue/{your-queue}: Receive messages from queues.

Make sure you have correctly configured allowed-localhost-domain in your application's configuration file to allow connections from valid sources.

Have a great day!
