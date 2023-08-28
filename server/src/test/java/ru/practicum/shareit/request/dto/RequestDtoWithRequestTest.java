package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.user.dto.UserDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
class RequestDtoWithRequestTest {
    @Test
    public void testEqualsAndHashCode() {
        LocalDateTime currentTime = LocalDateTime.now();

        UserDTO user1 = new UserDTO(1, "John", "Doe");
        UserDTO user2 = new UserDTO(2, "Jane", "Smith");

        List<ItemDTO> items1 = new ArrayList<>();
        items1.add(new ItemDTO().builder()
                .id(1)
                .name("Щётка для обуви")
                .description("Стандартная щётка для обуви")
                .available(true)
                .requestId(1)
                .ownerId(1)
                .build());


        List<ItemDTO> items2 = new ArrayList<>();
        items2.add(new ItemDTO().builder()
                .id(2)
                .name("Щётка для обуви2")
                .description("Стандартная щётка для обуви2")
                .available(true)
                .requestId(2)
                .ownerId(2).build());

        RequestDtoWithRequest request1 = new RequestDtoWithRequest(1, "Request 1", user1, currentTime, items1);
        RequestDtoWithRequest request2 = new RequestDtoWithRequest(2, "Request 2", user2, currentTime, items2);
        RequestDtoWithRequest request3 = new RequestDtoWithRequest(1, "Request 1", user1, currentTime, items1);

        // Verify equals() method
        assertNotEquals(request1, request2);
        assertEquals(request1, request3);

        // Verify hashCode() method
        int hashCode1 = request1.hashCode();
        int hashCode2 = request2.hashCode();
        int hashCode3 = request3.hashCode();

        assertNotEquals(hashCode1, hashCode2);
        assertEquals(hashCode1, hashCode3);
    }
}