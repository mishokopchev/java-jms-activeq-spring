package com.misho.repo;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by mihailkopchev on 4/5/18.
 */
public class Application {

    public ConnectionFactory createConnectionFactory() {
        return new ActiveMQConnectionFactory("tcp://localhost:61616");
    }

    public Connection createConnection(ConnectionFactory connectionFactory) throws JMSException {
        return connectionFactory.createConnection();
    }

    public Session createSession(Connection connection) throws JMSException {
        return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    public void sendTextMessageToQueue(String message, Session session) throws JMSException {
        Queue queue = session.createQueue("TEST");
        TextMessage textMessage = session.createTextMessage(message);
        MessageProducer messageProducer = session.createProducer(queue);
        messageProducer.send(textMessage);

    }

    public MessageConsumer consumeMessageFromQueue(String destination, Session session, MessageListener messageListener) {
        MessageConsumer messageConsumer = null;
        try {
            Queue queue = session.createQueue(destination);
            messageConsumer = session.createConsumer(queue);
            messageConsumer.setMessageListener(messageListener);

        } catch (JMSException e) {
            e.printStackTrace();
        }

        return messageConsumer;
    }

    public static void main(String[] args) {

        Application application = new Application();
        final Session session;
        final Connection connection;

        try {
            ConnectionFactory connectionFactory = application.createConnectionFactory();
            connection = application.createConnection(connectionFactory);
            session = application.createSession(connection);

            final MessageConsumer messageConsumer = application.consumeMessageFromQueue("TEST", session, new MessageListener() {
                public void onMessage(Message message) {
                    System.out.println(message.toString());
                }
            });

            connection.start();

            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        connection.stop();
                        messageConsumer.close();
                        session.close();
                        connection.close();
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }


                }
            });

        } catch (JMSException e) {
            e.printStackTrace();
        }


    }
}
