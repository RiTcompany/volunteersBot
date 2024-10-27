package org.example.commands.register;

import org.example.entities.Event;
import org.example.entities.Volunteer;
import org.example.enums.EColor;
import org.example.exceptions.EntityNotFoundException;
import org.example.services.EventService;
import org.example.services.QrCodeService;
import org.example.services.VolunteerService;
import org.example.utils.MessageUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.File;
import java.io.IOException;

@Component
public class RegisterToEventCommand extends BotCommand {
    private final EventService eventService;
    private final VolunteerService volunteerService;
    private final QrCodeService qrCodeService;
    private static final String PHOTO_EXCEPTION_MESSAGE_TEXT = "Возникли проблемы с формированием вашего QR кода. Пожалуйста обратитесь в поддержку";
    private static final String NOT_REGISTERED_MESSAGE_TEXT = "Сначала вам необходимо стать волонтёром. Используйте команду /volunteer_register";
    private static final String INCORRECT_INPUT_MESSAGE_TEXT = """
            Неверный ввод. Пожалуйста введите команду, а затем номер мероприятия через пробел.
            Список мероприятий и их номеров вы можете получить по команде /event_list.
            Пример: /register_event 2""";
    private static final String ANSWER = "Спасибо за регистрацию на мероприятие";

    public RegisterToEventCommand(
            EventService eventService, VolunteerService volunteerService, QrCodeService qrCodeService
    ) {
        super("register_event", "Register to event");
        this.eventService = eventService;
        this.volunteerService = volunteerService;
        this.qrCodeService = qrCodeService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        if (strings.length != 1) {
            MessageUtil.sendMessageText(chat.getId(), INCORRECT_INPUT_MESSAGE_TEXT, absSender);
            return;
        }

        try {
            Volunteer volunteer = volunteerService.getByChatId(chat.getId());
            if (EColor.RED.equals(volunteer.getColor())) {
                MessageUtil.sendMessageText(chat.getId(), ANSWER, absSender);
            } else {
                registerToEvent(volunteer, Long.parseLong(strings[0]), absSender);
            }
        } catch (EntityNotFoundException e) {
            MessageUtil.sendMessageText(chat.getId(), NOT_REGISTERED_MESSAGE_TEXT, absSender);
        } catch (NumberFormatException e) {
            MessageUtil.sendMessageText(chat.getId(), INCORRECT_INPUT_MESSAGE_TEXT, absSender);
        }
    }

    private void registerToEvent(Volunteer volunteer, long eventId, AbsSender absSender) {
        Event event = eventService.getById(eventId);
        if (event == null) {
            MessageUtil.sendMessageText(volunteer.getChatId(), INCORRECT_INPUT_MESSAGE_TEXT, absSender);
        } else {
            if (!volunteer.getEventList().contains(event)) {
                volunteerService.saveEvent(volunteer, event);
            }

            try {
                sendQrCode(volunteer, eventId, absSender);
            } catch (IOException e) {
                MessageUtil.sendMessageText(volunteer.getChatId(), PHOTO_EXCEPTION_MESSAGE_TEXT, absSender);
            }

            MessageUtil.sendMessageText(volunteer.getChatId(), ANSWER, absSender);
        }
    }

    private void sendQrCode(Volunteer volunteer, Long eventId, AbsSender absSender) throws IOException {
        File qrCodeImageFile = qrCodeService.generateFile(volunteer.getId(), eventId);
        SendPhoto sendPhoto = new SendPhoto(volunteer.getChatId().toString(), new InputFile(qrCodeImageFile));
        MessageUtil.sendPhoto(sendPhoto, absSender);
        qrCodeImageFile.delete();
    }

}