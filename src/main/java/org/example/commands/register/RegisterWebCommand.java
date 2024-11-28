package org.example.commands.register;

import lombok.extern.slf4j.Slf4j;
import org.example.entities.BotUser;
import org.example.entities.Volunteer;
import org.example.enums.ERole;
import org.example.exceptions.EntityNotFoundException;
import org.example.services.BotUserService;
import org.example.services.VolunteerService;
import org.example.utils.MessageUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
@Component
public class RegisterWebCommand extends BotCommand {
    private final BotUserService botUserService;
    private final VolunteerService volunteerService;
    private final PasswordEncoder passwordEncoder;
    private static final String INCORRECT_INPUT_MESSAGE_TEXT = """
            Неверный ввод. Пожалуйста введите команду, а затем ID волонтера через пробел, а также пароль (от 8 до 255 символов).
            ID волонтера можно найти на платформе.
            Пример: /register_web_permission 123 пароль123""";

    public RegisterWebCommand(
            BotUserService botUserService,
            VolunteerService volunteerService,
            PasswordEncoder passwordEncoder
    ) {
        super("register_web_permission", "Register user to web platform");
        this.botUserService = botUserService;
        this.volunteerService = volunteerService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        try {
            BotUser botUserAdmin = botUserService.getByChatIdAndRole(chat.getId(), ERole.ROLE_BOSS);

            if (arguments.length != 2 || 8 > arguments[1].length() || arguments[1].length() > 255) {
                MessageUtil.sendMessageText(chat.getId(), INCORRECT_INPUT_MESSAGE_TEXT, absSender);
                return;
            }

            long volunteerId = Long.parseLong(arguments[0]);
            String password = arguments[1];

            Volunteer volunteer = volunteerService.getByVolunteerId(volunteerId);
            BotUser botUser = getBotUser(volunteer.getChatId());
            botUser.setEmail(volunteer.getEmail());
            botUser.setPassword(passwordEncoder.encode(password));
            botUserService.save(botUser);

            sendAcceptMessage(chat.getId(), botUser.getEmail(), password, absSender);
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            MessageUtil.sendMessageText(chat.getId(), "Недостаточно прав", absSender);
        } catch (NumberFormatException e) {
            MessageUtil.sendMessageText(chat.getId(), INCORRECT_INPUT_MESSAGE_TEXT, absSender);
        } catch (IllegalArgumentException e) {
            MessageUtil.sendMessageText(chat.getId(), "Такой роли не существует", absSender);
        }
    }

    private BotUser getBotUser(Long chatId) {
        return !botUserService.existsByTgId(chatId)
                ? botUserService.create(chatId)
                : botUserService.getByChatId(chatId);
    }

    private void sendAcceptMessage(long chatId, String email, String password, AbsSender absSender) {
        MessageUtil.sendMessageText(
                chatId,
                "Доступ к платформе теперь доступен.\nLogin: %s\nPassword: %s".formatted(email, password),
                absSender
        );
    }
}