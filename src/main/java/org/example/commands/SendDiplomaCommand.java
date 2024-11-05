package org.example.commands;

import lombok.extern.slf4j.Slf4j;
import org.example.builders.MessageBuilder;
import org.example.entities.BotUser;
import org.example.entities.Event;
import org.example.entities.Volunteer;
import org.example.enums.ERole;
import org.example.exceptions.EntityNotFoundException;
import org.example.services.BotUserService;
import org.example.services.DiplomaService;
import org.example.services.EventService;
import org.example.services.VolunteerEventService;
import org.example.services.VolunteerService;
import org.example.utils.DateUtil;
import org.example.utils.MessageUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.File;
import java.util.Date;

@Slf4j
@Component
public class SendDiplomaCommand extends BotCommand {
    private final BotUserService botUserService;
    private final VolunteerEventService volunteerEventService;
    private final VolunteerService volunteerService;
    private final EventService eventService;
    private final DiplomaService diplomaService;
    private static final String INCORRECT_INPUT_MESSAGE_TEXT = """
            Неверный ввод. Пожалуйста введите команду, а затем номер мероприятия через пробел.
            Список мероприятий и их номеров вы можете получить на платформе.
            Пример: /send_diploma 2""";

    public SendDiplomaCommand(
            BotUserService botUserService,
            VolunteerEventService volunteerEventService,
            VolunteerService volunteerService,
            EventService eventService,
            DiplomaService diplomaService
    ) {
        super("send_diploma", "Send diploma to users");
        this.botUserService = botUserService;
        this.volunteerEventService = volunteerEventService;
        this.volunteerService = volunteerService;
        this.eventService = eventService;
        this.diplomaService = diplomaService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        if (arguments.length != 1) {
            MessageUtil.sendMessageText(chat.getId(), INCORRECT_INPUT_MESSAGE_TEXT, absSender);
            return;
        }

        try {
            BotUser botUser = botUserService.getByChatIdAndRole(chat.getId(), ERole.ROLE_WRITER);
            sendMessageToEventVolunteers(Long.valueOf(arguments[0]), absSender);
            MessageUtil.sendMessageText(chat.getId(), "Все дипломы отправлены", absSender);
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            MessageUtil.sendMessageText(chat.getId(), "Недостаточно прав", absSender);
        } catch (NumberFormatException e) {
            MessageUtil.sendMessageText(chat.getId(), INCORRECT_INPUT_MESSAGE_TEXT, absSender);
        }
    }

    private void sendMessageToEventVolunteers(Long eventId, AbsSender sender) {
        Event event = eventService.getById(eventId);
        if (event != null) {
            volunteerEventService.getEventVolunteerIdList(eventId).forEach(volunteerId -> {
                Volunteer volunteer = volunteerService.getById(volunteerId);
                String diplomaName = diplomaService.formDiploma(
                        volunteer.getFullName(),
                        event.getName(),
                        getDateDateIntervalStr(event.getStartTime(), event.getEndTime())
                );
                MessageUtil.sendDocument(
                        MessageBuilder.create()
                                .setText("Спасибо за участие в мероприятии! Ваше благодарственное письмо")
                                .setFile(new File(diplomaName))
                                .sendDocument(volunteer.getChatId()),
                        sender
                );
                diplomaService.deleteDiplomaFile(diplomaName);
            });
            volunteerService.flush();
        }
    }

    private String getDateDateIntervalStr(Date startDate, Date endDate) {
        return DateUtil.convertDate(startDate).concat(" - ").concat(DateUtil.convertDate(endDate));
    }
}