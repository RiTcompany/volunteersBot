package org.example.commands.register;

import lombok.extern.slf4j.Slf4j;
import org.example.entities.BotUser;
import org.example.entities.Role;
import org.example.entities.Volunteer;
import org.example.enums.ERole;
import org.example.exceptions.EntityNotFoundException;
import org.example.services.BotUserService;
import org.example.services.RoleService;
import org.example.services.VolunteerService;
import org.example.utils.MessageUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
@Component
public class RegisterRoleCommand extends BotCommand {
    private final BotUserService botUserService;
    private final VolunteerService volunteerService;
    private final RoleService roleService;
    private static final String INCORRECT_INPUT_MESSAGE_TEXT = """
            Неверный ввод. Пожалуйста введите команду, а затем ID волонтера через пробел, а также желаемую роль.
            ID волонтера можно найти на платформе.
            Пример: /register_admin 123 роль""";

    public RegisterRoleCommand(
            BotUserService botUserService,
            VolunteerService volunteerService,
            RoleService roleService
    ) {
        super("register_admin", "Register admin");
        this.botUserService = botUserService;
        this.volunteerService = volunteerService;
        this.roleService = roleService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        if (arguments.length != 2) {
            MessageUtil.sendMessageText(chat.getId(), INCORRECT_INPUT_MESSAGE_TEXT, absSender);
            return;
        }

        try {
            BotUser botUserAdmin = botUserService.getByChatId(chat.getId());
            ERole newRole = ERole.valueOf(arguments[1]);

            if (hasAdminAccess(botUserAdmin, newRole)) {
                Volunteer volunteer = volunteerService.getByVolunteerId(Long.valueOf(arguments[0]));
                BotUser newBotUserAdmin = getBotUser(volunteer.getChatId());
                getRoleToUser(newBotUserAdmin, newRole);
                sendAcceptMessage(chat.getId(), volunteer.getVolunteerId(), newRole, absSender);

                return;
            }

            MessageUtil.sendMessageText(chat.getId(), "Недостаточно прав", absSender);
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            MessageUtil.sendMessageText(chat.getId(), "Недостаточно прав", absSender);
        } catch (NumberFormatException e) {
            MessageUtil.sendMessageText(chat.getId(), INCORRECT_INPUT_MESSAGE_TEXT, absSender);
        } catch (IllegalArgumentException e) {
            MessageUtil.sendMessageText(chat.getId(), "Такой роли не существует", absSender);
        }
    }

    private boolean hasAdminAccess(BotUser botUserAdmin, ERole newRole) {
        return botUserAdmin.getRoleList().stream().anyMatch(role -> ERole.compare(role.getRoleName(), newRole) > 0);
    }

    private BotUser getBotUser(Long chatId) {
        return !botUserService.existsByTgId(chatId)
                ? botUserService.create(chatId)
                : botUserService.getByChatId(chatId);
    }

    private void getRoleToUser(BotUser newBotUserAdmin, ERole newRole) {
        if (!botUserService.hasRole(newBotUserAdmin, newRole)) {
            newBotUserAdmin.getRoleList().add(roleService.getByName(newRole));
            botUserService.save(newBotUserAdmin);
        }
    }

    private void sendAcceptMessage(long chatId, long volunteerId, ERole newRole, AbsSender absSender) {
        MessageUtil.sendMessageText(
                chatId,
                "Роль %s присвоена волонтёру ID=%d".formatted(newRole, volunteerId),
                absSender
        );
    }
}