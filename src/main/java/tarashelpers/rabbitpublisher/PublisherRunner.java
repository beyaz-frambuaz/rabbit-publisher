package tarashelpers.rabbitpublisher;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.*;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;

@Component
@RequiredArgsConstructor
public class PublisherRunner implements ApplicationRunner {

    private final RabbitTemplate rabbitTemplate;
    private final ConfigurableApplicationContext context;

    @Value("classpath:payload.json")
    private Resource resource;

    @Value("${rabbitmq.total.messages}")
    private int totalMessages;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (int i = 0; i < totalMessages; i++) {
            rabbitTemplate.send(fileToMessage());
        }
        context.close();
    }

    private Message fileToMessage() throws IOException {
        String file = Files.readString(resource.getFile().toPath());
        MessageProperties properties = new MessageProperties();
        properties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        return new Message(file.getBytes(), properties);
    }
}
