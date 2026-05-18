package com.zholdigaliev.coffeeshopims.dto.UserDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordUpdateRequest {

    @NotBlank(message = "oldPassword is required")
    private String oldPassword;

    @NotBlank(message = "newPassword is required")
    @Size(min = 4, message = "password must be at least 4 characters")
    private String newPassword;
}
