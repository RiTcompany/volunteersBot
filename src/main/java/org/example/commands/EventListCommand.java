package org.example.commands;

import org.example.services.EventService;
import org.example.utils.MessageUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class EventListCommand extends BotCommand {
    private final EventService eventService;

    public EventListCommand(EventService eventService) {
        super("event_list", "Get event list command");
        this.eventService = eventService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        StringBuilder builder = new StringBuilder();
        eventService.getFutureAll().forEach(event -> builder.append("%d   -   %s\n".formatted(event.getId(), event.getName())));
        MessageUtil.sendMessageText(chat.getId(), builder.toString(), absSender);
    }
}