package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.commands.EventListCommand;
import org.example.commands.InfoCommand;
import org.example.commands.SendBotMessageCommand;
import org.example.commands.SendDiplomaCommand;
import org.example.commands.StartCommand;
import org.example.commands.document.change.impl.ChangeChildDocumentCommand;
import org.example.commands.document.change.impl.ChangeVolunteerPhotoCommand;
import org.example.commands.document.check.impl.CheckChildDocumentCommand;
import org.example.commands.document.check.impl.CheckVolunteerPhotoCommand;
import org.example.commands.education.AddResultLinkCommand;
import org.example.commands.education.AddTrainingLinkCommand;
import org.example.commands.register.ParentRegisterCommand;
import org.example.commands.register.RegisterToEventCommand;
import org.example.commands.register.VolunteerRegisterCommand;
import org.example.services.CommandService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.CommandRegistry;

@Service
@RequiredArgsConstructor
public class CommandServiceImpl implements CommandService {
    private final StartCommand startCommand;
    private final InfoCommand infoCommand;
    private final VolunteerRegisterCommand volunteerRegisterCommand;
    private final ParentRegisterCommand parentRegisterCommand;
    private final CheckChildDocumentCommand checkChildDocumentCommand;
    private final CheckVolunteerPhotoCommand checkVolunteerPhotoCommand;
    private final ChangeChildDocumentCommand changeChildDocumentCommand;
    private final ChangeVolunteerPhotoCommand changeVolunteerPhotoCommand;
    private final SendBotMessageCommand sendBotMessageCommand;
    private final EventListCommand eventListCommand;
    private final RegisterToEventCommand registerToEventCommand;
    private final SendDiplomaCommand sendDiplomaCommand;
    private final AddTrainingLinkCommand addTrainingLinkCommand;
    private final AddResultLinkCommand addResultLinkCommand;

    public CommandRegistry registerCommands(String botName) {
        CommandRegistry commandRegistry = new CommandRegistry(true, () -> botName);
        commandRegistry.register(startCommand);
        commandRegistry.register(infoCommand);
        commandRegistry.register(volunteerRegisterCommand);
        commandRegistry.register(parentRegisterCommand);
        commandRegistry.register(checkChildDocumentCommand);
        commandRegistry.register(checkVolunteerPhotoCommand);
        commandRegistry.register(changeChildDocumentCommand);
        commandRegistry.register(changeVolunteerPhotoCommand);
        commandRegistry.register(sendBotMessageCommand);
        commandRegistry.register(eventListCommand);
        commandRegistry.register(registerToEventCommand);
        commandRegistry.register(sendDiplomaCommand);
        commandRegistry.register(addResultLinkCommand);
        commandRegistry.register(addTrainingLinkCommand);
        return commandRegistry;
    }
}
