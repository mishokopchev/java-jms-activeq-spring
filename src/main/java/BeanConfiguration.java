/**
 * Created by mihailkopchev on 4/5/18.
 */

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

import javax.jms.ConnectionFactory;

@Configuration
public class BeanConfiguration {

    @Bean
    @Primary
    public ConnectionFactory activeMqConnectionFactory(
            @Qualifier("activeMQProperties") ActiveMQProperties properties) {
        ActiveMQConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory(properties.getBrokerUrl());
        connectionFactory.getPrefetchPolicy().setQueuePrefetch(1);
        RedeliveryPolicy redeliveryPolicy
                = connectionFactory.getRedeliveryPolicy();
        redeliveryPolicy.setUseExponentialBackOff(true);
        redeliveryPolicy.setMaximumRedeliveries(4);
        return connectionFactory;
    }

    @Bean(name = "activeMQProperties")
    @ConfigurationProperties(prefix = "spring.activemq")
    public ActiveMQProperties activeMQProperties() {
        return new ActiveMQProperties();
    }

    @Bean
    public ConnectionFactory cachingConnectionFactory(
            @Qualifier("activeMqConnectionFactory")
                    ConnectionFactory activeMqConnectionFactory) {
        CachingConnectionFactory cachingConnectionFactory
                = new CachingConnectionFactory(activeMqConnectionFactory);
        cachingConnectionFactory.setCacheConsumers(true);
        cachingConnectionFactory.setCacheProducers(true);
        cachingConnectionFactory.setReconnectOnException(true);
        return cachingConnectionFactory;
    }

    @Bean
    public MessageConverter jacksonMessageConverter() {
        MappingJackson2MessageConverter converter
                = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_messageType");
        return converter;
    }


    @Bean
    @Primary
    public JmsTemplate queueJmsTemplate(
            @Qualifier("cachingConnectionFactory")
                    ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setDestinationResolver(new DynamicDestinationResolver());
        jmsTemplate.setMessageConverter(jacksonMessageConverter());
        jmsTemplate.setPriority(4);
        jmsTemplate.setTimeToLive(30000L);
        jmsTemplate.setExplicitQosEnabled(true);
        return jmsTemplate;
    }


    @Bean
    public JmsTemplate topicJmsTemplate(
            @Qualifier("cachingConnectionFactory")
                    ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setPubSubDomain(true);
        jmsTemplate.setDestinationResolver(new DynamicDestinationResolver());
        jmsTemplate.setMessageConverter(jacksonMessageConverter());
        return jmsTemplate;
    }

    @Value("${clientId:default_clientId}")
    private String clientId;

    @Bean(name = "TopicListenerContainerFactory")
    public DefaultJmsListenerContainerFactory exampleListenerContainerFactory(
            @Qualifier("activeMqConnectionFactory")
                    ConnectionFactory connectionFactory) throws Exception {
        DefaultJmsListenerContainerFactory factory =
                new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setDestinationResolver(new DynamicDestinationResolver());
        factory.setConcurrency("1-1");
        factory.setClientId(clientId);
        factory.setSubscriptionDurable(true);
        factory.setCacheLevel(DefaultMessageListenerContainer.CACHE_AUTO);
        factory.setMessageConverter(jacksonMessageConverter());
        factory.setPubSubDomain(true);
        return factory;
    }


}
