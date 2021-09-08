package tarashelpers.rabbitpublisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.*;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;

import static java.util.concurrent.TimeUnit.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class PublisherRunner implements ApplicationRunner {

    private final RabbitTemplate rabbitTemplate;
    private final ConfigurableApplicationContext context;

    @Value("classpath:payload.json")
    private Resource resource;

    @Value("${rabbitmq.total.messages}")
    private int totalMessages;

    @Value("${rabbitmq.publish.rate}")
    private int publishRate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Message message = fileToMessage();
        if (publishRate > 0) {
            publishAtRate(message);
        } else {
            publishUnrestricted(message);
        }
        context.close();
    }

    private void publishAtRate(Message... messages) throws InterruptedException {
        long secondInNanos = SECONDS.toNanos(1);
        long from = System.nanoTime();
        for (int i = 0, counter = 1; i < totalMessages; i++, counter++) {

            rabbitTemplate.send(messages[0]);

            if (counter == publishRate) {
                long now = System.nanoTime();
                if (now - from < secondInNanos) {
                    long restOfTheSecond = NANOSECONDS.toMillis(from + secondInNanos - now) - 1;
                    Thread.sleep(restOfTheSecond);
                    from = System.nanoTime();
                }
                counter = 0;
            }
        }
    }

    private void publishUnrestricted(Message... messages) {
        for (int i = 0; i < totalMessages; i++) {
            rabbitTemplate.send(messages[0]);
        }
    }

    private Message fileToMessage() throws IOException {
        String file = Files.readString(resource.getFile().toPath());
        MessageProperties properties = new MessageProperties();
        properties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        return new Message(file.getBytes(), properties);
    }
}
