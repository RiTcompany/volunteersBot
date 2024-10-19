package org.example.configs;

import lombok.extern.slf4j.Slf4j;
import org.example.bots.TGLongPoolingBot;
import org.example.bots.TGWebHookBot;
import org.example.services.CommandService;
import org.example.services.UpdateHandleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.CommandRegistry;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Configuration
public class BotConfig {
    @Value("${telegram.bot.enabled}")
    private boolean isBotEnabled;
    @Value("${telegram.bot.name}")
    private String name;
    @Value("${telegram.bot.token}")
    private String token;
    @Value("${telegram.webhook-path}")
    String webhookPath;

    @Bean
    public TGLongPoolingBot tgLongPoolingBot(@Autowired UpdateHandleService updateHandleService) {
        if (isBotEnabled) {
            TGLongPoolingBot TGLongPoolingBot = new TGLongPoolingBot(
                    name, token, updateHandleService
            );
            log.info("Bot initialized");
            return TGLongPoolingBot;
        }

        log.error("Bot was not initialized");
        return null;
    }

//    @Bean
//    public TGWebHookBot bot(
//            @Autowired UpdateHandleService updateHandleService,
//            SetWebhook setWebhook
//    ) {
//        if (isBotEnabled) {
//            TGWebHookBot bot = new TGWebHookBot(token, setWebhook);
//            bot.setBotPath(webhookPath);
//            bot.setBotUsername(name);
//            bot.setUpdateHandleService(updateHandleService);
//
//            log.info("Bot initialized");
//            return bot;
//        }
//
//        log.error("Bot was not initialized");
//        return null;
//    }

//    @Bean
//    public SetWebhook setWebhookInstance() {
//        return SetWebhook.builder().url(webhookPath).build();
//    }

    @Bean
    public TelegramBotsApi telegramBotsApi(TGLongPoolingBot myTelegramTGLongPoolingBot) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(myTelegramTGLongPoolingBot);
        return botsApi;
    }

    @Bean
    public CommandRegistry commandRegistry(@Autowired CommandService commandService) {
        return commandService.registerCommands(name);
    }
}
