package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
class ItemDTOTest {

    @Test
    public void testEquals() {
        ItemDTO item1 = ItemDTO.builder()
                .id(1)
                .name("Item")
                .description("Item description")
                .build();

        ItemDTO item2 = ItemDTO.builder()
                .id(1)
                .name("Item")
                .description("Item description")
                .build();

        ItemDTO item3 = ItemDTO.builder()
                .id(2)
                .name("Another item")
                .description("Another description")
                .build();

        assertEquals(item1, item2);
        assertNotEquals(item1, item3);
    }

    @Test
    public void testHashCode() {
        ItemDTO item1 = ItemDTO.builder()
                .id(1)
                .name("Item")
                .description("Item description")
                .build();

        ItemDTO item2 = ItemDTO.builder()
                .id(1)
                .name("Item")
                .description("Item description")
                .build();

        assertEquals(item1.hashCode(), item2.hashCode());
    }

}