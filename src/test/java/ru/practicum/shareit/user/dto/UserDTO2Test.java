package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
@SpringBootTest
class UserDTO2Test {

        @Test
        public void testEquals() {
            UserDTO user1 = UserDTO.builder()
                    .id(1)
                    .name("John")
                    .email("john@example.com")
                    .build();

            UserDTO user2 = UserDTO.builder()
                    .id(2)
                    .name("John")
                    .email("john@example.com")
                    .build();

            UserDTO user3 = UserDTO.builder()
                    .id(3)
                    .name("Jane")
                    .email("jane@example.com")
                    .build();

            assertEquals(user1, user2);
            assertNotEquals(user1, user3);
        }

        @Test
        public void testHashCode() {
            UserDTO user1 = UserDTO.builder()
                    .id(1)
                    .name("John")
                    .email("john@example.com")
                    .build();

            UserDTO user2 = UserDTO.builder()
                    .id(2)
                    .name("John")
                    .email("john@example.com")
                    .build();

            UserDTO user3 = UserDTO.builder()
                    .id(3)
                    .name("Jane")
                    .email("jane@example.com")
                    .build();

            assertEquals(user1.hashCode(), user2.hashCode());
            assertNotEquals(user1.hashCode(), user3.hashCode());
        }
}