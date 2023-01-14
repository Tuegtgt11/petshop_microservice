package com.tass.shoppingcartservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class ResdisPusherMessageService implements MessageListener {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String msg = message.toString();

    }

    public void sendMessage(String message , ChannelTopic channelTopic){
        redisTemplate.convertAndSend(channelTopic.getTopic() , message);
    }
}
