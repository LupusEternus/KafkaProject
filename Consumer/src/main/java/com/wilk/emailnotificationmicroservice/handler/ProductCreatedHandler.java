package com.wilk.emailnotificationmicroservice.handler;


import com.wilk.core.ProductCreatedEvent;
import com.wilk.emailnotificationmicroservice.entity.ProcessedEventEntity;
import com.wilk.emailnotificationmicroservice.repository.ProcessedEventRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Transactional
@AllArgsConstructor
@NoArgsConstructor
@Component
@KafkaListener(topics = "product-created-events-topic")
public class ProductCreatedHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private ProcessedEventRepository processedEventRepository;

    @KafkaHandler
    public void handler(@Payload ProductCreatedEvent productCreateEvent, @Header("msgId") String msgId, @Header(KafkaHeaders.RECEIVED_KEY) String messageKey) {

        if(processedEventRepository.findByMsgId(msgId) != null){
            LOGGER.info("Found duplicate message id: {}",msgId);
            return;
        }
        LOGGER.info("Received a new event: {}", productCreateEvent.getTitle());
        ProcessedEventEntity processedEventEntity = new ProcessedEventEntity();
        processedEventEntity.setMsgId(msgId);
        processedEventEntity.setProductId(processedEventEntity.getProductId());
        try {
            processedEventRepository.save(processedEventEntity);
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("{}", e.getMessage());
        }
    }

}
