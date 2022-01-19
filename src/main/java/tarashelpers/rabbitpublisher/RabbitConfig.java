package tarashelpers.rabbitpublisher;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.*;

@Configuration
@ConditionalOnProperty("tap.output")
public class RabbitConfig {

    @Bean
    protected Queue tapQueue(@Value("${tap.queue}") String queue) {
        return QueueBuilder.durable(queue).build();
    }

    @Bean
    protected TopicExchange outboundExchange(@Value("${tap.exchange}") String exchange) {
        return new TopicExchange(exchange);
    }

    @Bean
    protected Binding bindTapToExchange(Queue tapQueue, TopicExchange outboundExchange) {
        return BindingBuilder.bind(tapQueue).to(outboundExchange).with("#");
    }

    @Bean
    protected RabbitAdmin rabbitAdmin(final ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

}
