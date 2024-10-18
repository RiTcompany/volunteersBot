package org.example.bots;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.AbstractException;
import org.example.services.UpdateHandleService;
import org.example.utils.LogUtil;
import org.example.utils.MessageUtil;
import org.example.utils.UpdateUtil;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;

@Slf4j
@Getter
@Setter
public class TGWebHookBot extends SpringWebhookBot {
    private String botUsername;
    private String botPath;
    private UpdateHandleService updateHandleService;

    public TGWebHookBot(String token, SetWebhook setWebhook) {
        super(setWebhook, token);
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        try {
            if (update.hasCallbackQuery() && UpdateUtil.isPrivateChat(update)) {
                updateHandleService.handleCallbackRequest(update, this);
            } else if (update.hasMessage() && UpdateUtil.isPrivateChat(update)) {
                updateHandleService.handleMessageRequest(update, this);
            } else if (update.hasMyChatMember()) {
                updateHandleService.handleMyChatMember(update);
            }
        } catch (AbstractException e) {
            log.error(LogUtil.getExceptionLog(update, e.getMessage()), e);
            MessageUtil.sendMessageText(UpdateUtil.getChatId(update), e.getUserMessage(), this);
        } catch (Exception e) {
            log.error(LogUtil.getExceptionLog(update, e.getMessage()), e);
            MessageUtil.sendMessageText(UpdateUtil.getChatId(update), "Что-то пошло не так. Нужно обратиться в поддержку", this);
        }

        return null;
    }
}
