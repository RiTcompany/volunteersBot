package org.example.services;

import org.example.entities.BotMessageButton;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BotMessageButtonService {
    BotMessageButton create(long botMessageId, String buttonName, String buttonLink);

    List<BotMessageButton> getListByMessageId(long botMessageId);

    void deleteButtons(long botMessageId);
}
