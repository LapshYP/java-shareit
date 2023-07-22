package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.user.dto.UserDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class RequestDtoTest {

    private RequestDto requestDto;

    @BeforeEach
    public void setUp() {
        requestDto = new RequestDto();
    }

    @Test
    public void testSetAndGetId() {
        int id = 1;
        requestDto.setId(id);
        assertEquals(id, requestDto.getId());
    }

    @Test
    public void testSetAndGetDescription() {
        String description = "This is a test description";
        requestDto.setDescription(description);
        assertEquals(description, requestDto.getDescription());
    }

    @Test
    public void testSetAndGetRequestor() {
        UserDTO requestor = new UserDTO();
        requestDto.setRequestor(requestor);
        assertNotNull(requestDto.getRequestor());
    }

    @Test
    public void testSetAndGetCreatedTime() {
        LocalDateTime createdTime = LocalDateTime.now();
        requestDto.setCreatedtime(createdTime);
        assertEquals(createdTime, requestDto.getCreatedtime());
    }

    @Test
    public void testSetAndGetItems() {
        List<ItemDTO> items = new ArrayList<>();
        items.add(new ItemDTO());
        requestDto.setItems(items);
        assertEquals(items, requestDto.getItems());
    }
}