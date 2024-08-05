package com.linielt.realworldapispringboot.mapper;

import com.linielt.realworldapispringboot.dtos.UserDto;
import com.linielt.realworldapispringboot.model.User;
import org.mapstruct.Mapper;

/**
 * Maps Users to UserDTO
 */
@Mapper(
        componentModel = "spring"
)
public interface UserMapper {
    UserDto toUserDto(User user);
}
