package com.conduit.mapper;

import com.conduit.entity.User;
import com.conduit.openapi.model.NewUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openapitools.jackson.nullable.JsonNullable;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "hashPassword", ignore = true)
    @Mapping(target = "bio", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "id", ignore = true)
    User newUserToUser(NewUser newUser);

    @Mapping(target = "image", source = "imageUrl")
    @Mapping(target = "token", ignore = true)
    com.conduit.openapi.model.User toUserDTO(User user);

    default <T> JsonNullable<T> toJsonNullable(T value) {
        return value == null ? null : JsonNullable.of(value);
    }


}
