package za.co.jse.services;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.co.jse.entities.dtos.MessageDto;
import za.co.jse.entities.dtos.MessageRespDto;
import za.co.jse.exceptions.InvalidMessageException;
import za.co.jse.exceptions.UserNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MessageServiceImplTest {

    private static final String TEST_USERNAME = "TestUser";
    private static final String TEST_TEXT = "Hi all";

    private final IMessageService messageService;
    private final MessageDto testMessage = MessageDto.builder()
            .username(TEST_USERNAME)
            .text(TEST_TEXT)
            .build();

    @BeforeEach
    public void prepareDatastore() {
        messageService.getDefaultChatRoom().getChat().clear();
    }

    @Test
    void should_create_user_with_empty_messages_when_user_does_not_exist() {
        final MessageRespDto testUser = messageService.findByUsername(TEST_USERNAME);

        assertNotNull(testUser);
        assertTrue(testUser.getMessages().isEmpty());
    }

    @Test
    void should_throw_UserNotFoundException_if_user_does_not_exist() {
        assertThrows(UserNotFoundException.class, () -> messageService.send(testMessage));
    }

    @Test
    void should_send_message_if_user_already_exists() {
//        find user by username or create one if user does not exist
        messageService.findByUsername(TEST_USERNAME);

        assertDoesNotThrow(() -> messageService.send(testMessage));
    }

    @Test
    void should_throw_InvalidMessageException_if_username_is_null() {
        final InvalidMessageException invalidMessageException = assertThrows(InvalidMessageException.class, () -> messageService.send(MessageDto.builder().build()));

        assertEquals("Username and message cannot be null", invalidMessageException.getMessage());
    }

    @Test
    void should_throw_InvalidMessageException_if_message_text_is_null() {
        final MessageDto messageDto = MessageDto.builder().username(TEST_USERNAME).build();
        final InvalidMessageException invalidMessageException = assertThrows(InvalidMessageException.class, () -> messageService.send(messageDto));

        assertEquals("Username and message cannot be null", invalidMessageException.getMessage());
    }
}