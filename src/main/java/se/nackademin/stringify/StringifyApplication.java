package se.nackademin.stringify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StringifyApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(StringifyApplication.class, args);
/*        String uri = "amqps://pzbbhejr:Ntqpxp8wrq4dsn_pgZweAc8yX5KdtdBo@bonobo.rmq.cloudamqp.com/pzbbhejr";

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(uri);
        factory.setRequestedHeartbeat(30);
        factory.setConnectionTimeout(10_000);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare("hello", false, false, false, null);
        String message = "Hello CloudAMQP!";
        channel.basicPublish("", "hello", null, message.getBytes());*/
    }

}
