package com.bso.sleuthservice2.infra.aws.message.sender;

public interface MessageSender {
    <T> void send(T message, String queue);
}
