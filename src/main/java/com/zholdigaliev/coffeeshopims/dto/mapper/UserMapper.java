package com.zholdigaliev.coffeeshopims.dto.mapper;

import com.zholdigaliev.coffeeshopims.dto.UserDto.UserRequest;
import com.zholdigaliev.coffeeshopims.dto.UserDto.UserResponse;
import com.zholdigaliev.coffeeshopims.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "branch.id", target = "branchId")
    @Mapping(source = "branch.name", target = "branchName")
    UserResponse toResponse(User user);

    User toEntity(UserRequest request);
}
