This is a simple spring boot rabbitmq publisher for local testing/debugging purposes.

Usage:
1. Paste json payload body into `src/main/resources/payload.json`
2. In `application.properties` set exchange `spring.rabbitmq.template.exchange` and routing key `spring.rabbitmq.template.routing-key` to publish message(s) to
3. In `application.properties` set total number of messages to be published and the rate (messages/sec). App will publish the same message N times and exit. If rate is left as `0` it will publish at max speed
4. Optionally: set up output tap. Since this is mostly used to manually test a service during development we would want to capture that service's output. To automatically set up an output tap queue set `tap.output=true`, `tap.queue` to whatever you want the queue to be called and `tap.exchange` to the value of service's outbound exchange to bind tap queue to 
5. Run

Naturally, all the above requires running rabbitmq instance. Try
`docker run -p 5672:5672 -p 15672:15672 -d rabbitmq:3-management-alpine`
