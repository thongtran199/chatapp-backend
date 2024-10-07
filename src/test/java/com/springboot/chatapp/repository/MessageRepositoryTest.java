package com.springboot.chatapp.repository;

import com.springboot.chatapp.TestDataUtils;
import com.springboot.chatapp.model.entity.Message;
import com.springboot.chatapp.model.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MessageRepositoryTest {

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;


    private User sender;
    private User receiver;
    private Message message;

    @BeforeEach
    void setUp() {
        sender = TestDataUtils.createUser("thongtran", "thongtran@gmail.com", "Tran Van Thong");
        receiver = TestDataUtils.createUser("khanhvan", "khanhvan@gmail.com", "Tran Nguyen Khanh Van");
        userRepository.save(sender);
        userRepository.save(receiver);

        message = TestDataUtils.createMessage(sender, receiver, "Hello!");
        message = messageRepository.save(message);
    }

    @AfterEach
    void tearDown() {
        messageRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findByMessageSenderAndReceiver() {
        List<Message> messages = messageRepository.findByMessageSenderAndReceiver(sender.getUserId(), receiver.getUserId());
        assertThat(messages).hasSize(1);
        assertThat(messages.get(0).getContent()).isEqualTo("Hello!");
    }

    @Test
    @Transactional
    void markMessageAsRead() {
        messageRepository.markMessageAsRead(message.getMessageId());

        entityManager.flush();
        entityManager.clear();

        Message updatedMessage = messageRepository.findById(message.getMessageId()).orElseThrow();

        assertThat(updatedMessage.isRead()).isTrue();
    }

    @Test
    void findLatestMessagesByUserId() {
        List<Message> latestMessages = messageRepository.findLatestMessagesByUserId(sender.getUserId());
        assertThat(latestMessages).hasSize(1);
    }
}