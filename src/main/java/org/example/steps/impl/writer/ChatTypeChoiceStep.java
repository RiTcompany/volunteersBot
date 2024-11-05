package org.example.steps.impl.writer;

import lombok.RequiredArgsConstructor;
import org.example.dto.MessageDto;
import org.example.dto.ResultDto;
import org.example.entities.ChatHash;
import org.example.enums.EChat;
import org.example.enums.ERole;
import org.example.exceptions.EntityNotFoundException;
import org.example.mappers.KeyboardMapper;
import org.example.services.BotMessageService;
import org.example.services.BotUserService;
import org.example.steps.ChoiceStep;
import org.example.utils.ButtonUtil;
import org.example.utils.StepUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@RequiredArgsConstructor
public class ChatTypeChoiceStep extends ChoiceStep {
    private final KeyboardMapper keyboardMapper;
    private final BotMessageService botMessageService;
    private final BotUserService botUserService;
    private static final String PREPARE_MESSAGE_TEXT = "Вы хотите разослать это ссобщение только участникамм конкретного мероприятия?";

    @Override
    protected ResultDto isValidData(MessageDto messageDto) throws EntityNotFoundException {
        if (!isCallback(messageDto.getEMessage())) {
            return new ResultDto(false, EXCEPTION_MESSAGE_TEXT);
        }

        try {
            EChat.valueOf(messageDto.getData());
            return new ResultDto(true);
        } catch (IllegalArgumentException ignored) {
        }

        return new ResultDto(false, EXCEPTION_MESSAGE_TEXT);
    }

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) throws EntityNotFoundException {
        StepUtil.sendPrepareMessageWithInlineKeyBoard(
                chatHash,
                PREPARE_MESSAGE_TEXT,
                keyboardMapper.keyboardDto(chatHash, ButtonUtil.chatTypeButtonList()),
                sender
        );
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) throws EntityNotFoundException {
        EChat eChat = EChat.valueOf(data);

        long userId = botUserService.getByChatIdAndRole(chatHash.getChatId(), ERole.ROLE_WRITER).getId();
        botMessageService.saveChatType(userId, eChat);

        if (EChat.EVENT.equals(eChat)) {
            return 0;
        }

        return 1;
    }
}
