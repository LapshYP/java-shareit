package ru.practicum.shareit.item.comment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "author.name", source = "authorName")
    Comment commentDtoToComment(CommentDto commentDto);

    @Mapping(target = "authorName", source = "author.name")
    CommentDto commentToCommentDto(Comment comment);
}