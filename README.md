This is a simple spring boot rabbitmq publisher for local testing/debugging purposes.

Usage:
1. paste json payload body into `src/main/resources/payload.json`
2. In `application.properties` set rabbitmq exchange and routing key
3. In `application.properties` set total number of messages to be published and the rate (messages/sec)
4. Run

App will publish the same message N times and exit. If rate is left as `0` it will publish at max speed. 

Naturally, all the above requires running rabbitmq instance. Try
`docker run -p 5672:5672 -p 15672:15672 -d rabbitmq:3-management-alpine`
