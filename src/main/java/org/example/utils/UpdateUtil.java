package org.example.utils;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class UpdateUtil {
    public static String getUserName(Update update) {
        return getChat(update).getUserName();
    }

    public static String getUserInputText(Update update) {
        if (isCallback(update)) {
            return update.getCallbackQuery().getData();
        } else {
            return update.getMessage().getText();
        }
    }

    public static boolean isCallback(Update update) {
        return update.hasCallbackQuery();
    }

    public static long getChatId(Update update) {
        return getChat(update).getId();
    }

    public static boolean isPrivateChat(Update update) {
        return getChat(update).isUserChat();
    }

    public static Chat getChat(Update update) {
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getMessage().getChat();
        } else if (update.hasMessage()) {
            return update.getMessage().getChat();
        } else {
            return update.getMyChatMember().getChat();
        }
    }

    public static Message getMessage(Update update) {
        System.out.println(update);
        return (update.hasCallbackQuery())
                ? update.getCallbackQuery().getMessage()
                : update.getMessage();
    }
}
