
package com.example.service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService
{
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository;

    public Optional<Message> createMessage(Message message)
    {
        if (message.getMessageText() == null || message.getMessageText().isBlank() ||
            message.getMessageText().length() > 255 ||
            !accountRepository.existsById(message.getPostedBy()))
        {
            return Optional.empty();
        }

        return Optional.of(messageRepository.save(message));
    }

    public List<Message> getAllMessages()
    {
        return messageRepository.findAll();
    }

    public Optional<Message> getMessageById(int id)
    {
        return messageRepository.findById(id);
    }

    public int deleteMessage(int id)
    {
        if (messageRepository.existsById(id))
        {
            messageRepository.deleteById(id);
            return 1;
        }

        return 0;
    }

    public int updateMessage(int id, String newText)
    {
        Optional<Message> optional = messageRepository.findById(id);

        if (optional.isPresent() && newText != null && !newText.isBlank() && newText.length() <= 255)
        {
            Message msg = optional.get();
            msg.setMessageText(newText);
            messageRepository.save(msg);
            return 1;
        }

        return 0;
    }

    public List<Message> getMessagesByUser(int accountId)
    {
        return messageRepository.findByPostedBy(accountId);
    }
}
