package me.fwfurtado.chatstomp.controller;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class ChatController {

    @GetMapping("sse")
    SseEmitter sse() {
        SseEmitter emitter = new SseEmitter();

        ExecutorService executorService = Executors.newFixedThreadPool(1);

        executorService.execute( () -> createStream(emitter));

        return emitter;
    }

    private void createStream(SseEmitter emitter) {
        AtomicInteger counter = new AtomicInteger();

        while (true) {
            int value = counter.getAndIncrement();
            if (!(value <= 10))
                break;
            try {
                emitter.send(SseEmitter.event().id(String.valueOf(value)).name("sse").data("{\"counter\": \"" + value + "\"}"));
                TimeUnit.SECONDS.sleep(2);
            } catch (IOException | InterruptedException e) {
                emitter.completeWithError(e);
            }
        }

        emitter.complete();
    }
}
