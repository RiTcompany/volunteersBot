package org.example.commands.register;

import org.example.entities.Event;
import org.example.entities.Volunteer;
import org.example.enums.EColor;
import org.example.enums.EDocument;
import org.example.exceptions.EntityNotFoundException;
import org.example.services.DocumentService;
import org.example.services.EventEducationMessageService;
import org.example.services.EventService;
import org.example.services.QrCodeService;
import org.example.services.TrainingService;
import org.example.services.VolunteerService;
import org.example.utils.DateUtil;
import org.example.utils.MessageUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

@Component
public class RegisterToEventCommand extends BotCommand {
    private final EventService eventService;
    private final VolunteerService volunteerService;
    private final QrCodeService qrCodeService;
    private final EventEducationMessageService eventEducationMessageService;
    private final TrainingService trainingService;
    private final DocumentService documentService;
    private static final String PHOTO_EXCEPTION_MESSAGE_TEXT = "Возникли проблемы с формированием вашего QR кода. Пожалуйста обратитесь в поддержку";
    private static final String NOT_REGISTERED_MESSAGE_TEXT = "Сначала вам необходимо стать волонтёром. Используйте команду /volunteer_register";
    private static final String PHOTO_NOT_CHECKED_ANSWER = "Ваша фотография ещё не проверена. Пока что вы не можете принять участие в мероприятии";
    private static final String CHILD_DOC_NOT_CHECKED_ANSWER = "Ваш документ от родителей ещё не проверен. Пока что вы не можете принять участие в мероприятии";
    private static final String INCORRECT_INPUT_MESSAGE_TEXT = """
            Неверный ввод. Пожалуйста введите команду, а затем номер мероприятия через пробел.
            Список мероприятий и их номеров вы можете получить по команде /event_list.
            Пример: /register_event 2""";
    private static final String ANSWER = "Спасибо за регистрацию на мероприятие";

    public RegisterToEventCommand(
            EventService eventService,
            VolunteerService volunteerService,
            QrCodeService qrCodeService,
            EventEducationMessageService eventEducationMessageService,
            TrainingService trainingService,
            DocumentService documentService
    ) {
        super("register_event", "Register to event");
        this.eventService = eventService;
        this.volunteerService = volunteerService;
        this.qrCodeService = qrCodeService;
        this.eventEducationMessageService = eventEducationMessageService;
        this.trainingService = trainingService;
        this.documentService = documentService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        try {
            Volunteer volunteer = volunteerService.getByChatId(chat.getId());

            if (strings.length != 1) {
                MessageUtil.sendMessageText(chat.getId(), INCORRECT_INPUT_MESSAGE_TEXT, absSender);
                return;
            }

            if (EColor.RED.equals(volunteer.getColor())) {
                MessageUtil.sendMessageText(chat.getId(), ANSWER, absSender);
            } else {
                registerToEvent(volunteer, Long.parseLong(strings[0]), absSender);
            }
        } catch (EntityNotFoundException e) {
            MessageUtil.sendMessageText(chat.getId(), NOT_REGISTERED_MESSAGE_TEXT, absSender);
        } catch (NumberFormatException e) {
            MessageUtil.sendMessageText(chat.getId(), INCORRECT_INPUT_MESSAGE_TEXT, absSender);
        } catch (GeneralSecurityException | IOException e) {
            MessageUtil.sendMessageText(chat.getId(), "Что-то пошло не так", absSender);
        }
    }

    private void registerToEvent(Volunteer volunteer, long eventId, AbsSender absSender) throws GeneralSecurityException, IOException {
        Event event = eventService.getById(eventId);
        if (event == null || !event.getIsAvailable()) {
            MessageUtil.sendMessageText(volunteer.getChatId(), INCORRECT_INPUT_MESSAGE_TEXT, absSender);
        } else {
            if (!hasCheckedPhoto(volunteer)) {
                MessageUtil.sendMessageText(volunteer.getChatId(), PHOTO_NOT_CHECKED_ANSWER, absSender);
            } else if (!hasAgeApprove(volunteer)) {
                MessageUtil.sendMessageText(volunteer.getChatId(), CHILD_DOC_NOT_CHECKED_ANSWER, absSender);
            } else if (!trainingService.getResultByEmail(eventId, volunteer.getEmail())) {
                sendEducation(eventId, volunteer.getChatId(), absSender);
            } else {
                registerUserToEvent(volunteer, event);
                sendQrCode(volunteer, eventId, absSender);
                MessageUtil.sendMessageText(volunteer.getChatId(), ANSWER, absSender);
            }
        }
    }

    private boolean hasCheckedPhoto(Volunteer volunteer) {
        return documentService.hasCheckedDocument(volunteer.getChatId(), EDocument.VOLUNTEER_PHOTO);
    }

    private boolean hasAgeApprove(Volunteer volunteer) {
        return DateUtil.getYearCountByDate(volunteer.getBirthday()) >= 18 ||
                documentService.hasCheckedDocument(volunteer.getChatId(), EDocument.CHILD_DOCUMENT);
    }

    private void registerUserToEvent(Volunteer volunteer, Event event) {
        if (!volunteer.getEventList().contains(event)) {
            volunteerService.saveEvent(volunteer, event);
        }
    }

    private void sendQrCode(Volunteer volunteer, Long eventId, AbsSender absSender) {
        try {
            File qrCodeImageFile = qrCodeService.generateFile(volunteer.getId(), eventId);
            MessageUtil.sendPhoto(volunteer.getChatId(), qrCodeImageFile, absSender);
            qrCodeImageFile.delete();
        } catch (IOException e) {
            MessageUtil.sendMessageText(volunteer.getChatId(), PHOTO_EXCEPTION_MESSAGE_TEXT, absSender);
        }
    }

    private void sendEducation(Long eventId, Long chatId, AbsSender sender) {
        eventEducationMessageService.getEventEducationMessageList(eventId)
                .forEach(message -> {
                    switch (message.getEMessage()) {
                        case TEXT -> MessageUtil.sendMessageText(chatId, message.getData(), sender);
                        case PHOTO -> MessageUtil.sendPhoto(chatId, message.getData(), sender);
                        case VIDEO -> MessageUtil.sendVideo(chatId, message.getData(), sender);
                    }
                });
    }
}