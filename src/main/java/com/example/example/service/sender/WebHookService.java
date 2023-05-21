package com.example.example.service.sender;

import com.example.example.configuration.AMQPConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

/**
 * Service for send payload to webhook.
 */
@Service
@RequiredArgsConstructor
public class WebHookService {


    private final AmqpTemplate amqpTemplate;


    public void sendPayload(Object payload) {
        this.amqpTemplate.convertAndSend(
                AMQPConfiguration.WEB_HOOK_KEY,
                payload
        );
    }
}
