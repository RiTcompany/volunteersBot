package org.example.commands.education;

import lombok.extern.slf4j.Slf4j;
import org.example.entities.BotUser;
import org.example.enums.ERole;
import org.example.exceptions.EntityNotFoundException;
import org.example.services.BotUserService;
import org.example.services.TrainingService;
import org.example.utils.MessageUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
@Component
public class AddResultLinkCommand extends BotCommand {
    private final BotUserService botUserService;
    private final TrainingService trainingService;
    private static final String INCORRECT_INPUT_MESSAGE_TEXT = """
            Неверный ввод. Пожалуйста введите команду, а затем номер мероприятия через пробел и ссылку на таблицу результатов обучения по данному мероприятию.
            Список мероприятий и их номеров вы можете получить на платформе.
            Пример: /add_result_link 2 ссылка""";

    public AddResultLinkCommand(BotUserService botUserService, TrainingService trainingService) {
        super("add_result_link", "Add Result Link");
        this.botUserService = botUserService;
        this.trainingService = trainingService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        try {
            BotUser botUser = botUserService.getByChatIdAndRole(chat.getId(), ERole.ROLE_WRITER);

            if (arguments.length != 2) {
                MessageUtil.sendMessageText(chat.getId(), INCORRECT_INPUT_MESSAGE_TEXT, absSender);
                return;
            }

            long eventId = Long.parseLong(arguments[0]);
            String link = arguments[1];
            trainingService.addResultsLink(eventId, link);
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            MessageUtil.sendMessageText(chat.getId(), "Недостаточно прав", absSender);
        } catch (NumberFormatException e) {
            MessageUtil.sendMessageText(chat.getId(), INCORRECT_INPUT_MESSAGE_TEXT, absSender);
        }
    }
}