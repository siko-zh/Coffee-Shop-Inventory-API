package com.zholdigaliev.coffeeshopims.dto.UserDto;

import com.zholdigaliev.coffeeshopims.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequest {

    @NotBlank(message = "username is required")
    private String username;

    @NotBlank(message = "email is required")
    @Email(message = "invalid email format")
    private String email;

    @NotBlank(message = "password is required")
    @Size(min = 4, message = "password must be at least 4 characters")
    private String password;

    @NotNull(message = "role is required")
    private Role role;

    private Long branchId;
}
