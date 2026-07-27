package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public Message createMessage(Message newMessage) {
        String messageText = newMessage.getMessageText();
        if (messageText == null || messageText.length() == 0 || messageText.length() > 255)
            throw new IllegalArgumentException("The message text must be greater than 0 and less than or equal to 255 characters.");
        if (accountRepository.findById(newMessage.getPostedBy()).isEmpty())
            throw new IllegalArgumentException("User does not exist.");
        return messageRepository.save(newMessage);
    }

    public List<Message> getAllMessages() {
        return (List<Message>) messageRepository.findAll();
    }

    public Message getMessageById(Integer messageId) {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if (optionalMessage.isPresent()) 
            return optionalMessage.get();
        else
            return null;
    }

    public int deleteMessage(Integer messageId) {
        if (!messageRepository.existsById(messageId)) 
            return 0;
        messageRepository.deleteById(messageId);
        return 1;
    }

    public int updateMessage(Integer messageId, String newMessageText) {
        if (newMessageText == null || newMessageText.length() == 0 || newMessageText.length() > 255)
            return 0;
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if (optionalMessage.isEmpty())
            return 0;
        Message message = optionalMessage.get();
        message.setMessageText(newMessageText);
        messageRepository.save(message);
        return 1;
    }

    public List<Message> getMessagesByPostedBy(Integer postedBy) {
        List<Message> messages = messageRepository.findByPostedBy(postedBy);
        return messages;
    }
}