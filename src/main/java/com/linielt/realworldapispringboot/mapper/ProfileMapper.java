package com.linielt.realworldapispringboot.mapper;

import com.linielt.realworldapispringboot.dtos.ProfileDto;
import com.linielt.realworldapispringboot.model.Profile;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring"
)
public interface ProfileMapper {
    ProfileDto toProfileDto(Profile profile);
}
