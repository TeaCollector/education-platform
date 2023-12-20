package ru.coffee.proselytestudywebfluxjwt.mapper;

import org.mapstruct.Mapper;
import ru.coffee.proselytestudywebfluxjwt.dto.UserDto;
import ru.coffee.proselytestudywebfluxjwt.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(UserEntity userEntity);

    UserEntity toEntity(UserDto dto);
}
