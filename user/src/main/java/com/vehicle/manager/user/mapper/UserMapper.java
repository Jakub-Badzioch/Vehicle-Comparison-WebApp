package com.vehicle.manager.user.mapper;


import com.vehicle.manager.commons.dto.model.UserDTO;
import com.vehicle.manager.user.dao.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toEntity(UserDTO userDTO);

    @Mapping(target = "password", ignore = true)
    UserDTO toDto(User user);

}
