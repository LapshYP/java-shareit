package ru.practicum.shareit.item.comment;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentDtoTest {
    @Test
    public void testAllArgsConstructor() {
        Long id = 1L;
        String content = "Test content";
        String authorName = "Test author";
        LocalDateTime created = LocalDateTime.now();

        CommentDto commentDto = new CommentDto(id, content, authorName, created);

        assertEquals(id, commentDto.getId());
        assertEquals(content, commentDto.getContent());
        assertEquals(authorName, commentDto.getAuthorName());
        assertEquals(created, commentDto.getCreated());
    }

    @Test
    public void testEquals() {
        Long id1 = 1L;
        String content1 = "Test content";
        String authorName1 = "Test author";
        LocalDateTime created1 = LocalDateTime.now();

        CommentDto commentDto1 = new CommentDto(id1, content1, authorName1, created1);
        CommentDto commentDto2 = new CommentDto(id1, content1, authorName1, created1);
        assertEquals(commentDto1, commentDto2);

        Long id2 = 2L;
        String content2 = "Test content 2";
        String authorName2 = "Test author 2";
        LocalDateTime created2 = LocalDateTime.now().minusHours(1);

        CommentDto commentDto3 = new CommentDto(id2, content2, authorName2, created2);
        assertNotEquals(commentDto1, commentDto3);
    }


    @Test
    public void testHashCode() {
        Long id1 = 1L;
        String content1 = "Test content";
        String authorName1 = "Test author";
        LocalDateTime created1 = LocalDateTime.now();

        CommentDto commentDto1 = new CommentDto(id1, content1, authorName1, created1);
        CommentDto commentDto2 = new CommentDto(id1, content1, authorName1, created1);
        assertEquals(commentDto1.hashCode(), commentDto2.hashCode());

        Long id2 = 2L;
        String content2 = "Test content 2";
        String authorName2 = "Test author 2";
        LocalDateTime created2 = LocalDateTime.now().minusHours(1);

        CommentDto commentDto3 = new CommentDto(id2, content2, authorName2, created2);
        assertNotEquals(commentDto1.hashCode(), commentDto3.hashCode());
    }
}