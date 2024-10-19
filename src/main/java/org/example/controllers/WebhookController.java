package org.example.controllers;

import lombok.AllArgsConstructor;
import org.example.bots.TGWebHookBot;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@AllArgsConstructor
public class WebhookController {
//    private final TGWebHookBot bot;
//
//    @PostMapping("/")
//    public void onUpdateReceived(@RequestBody Update update) {
//        bot.onWebhookUpdateReceived(update);
//    }
}