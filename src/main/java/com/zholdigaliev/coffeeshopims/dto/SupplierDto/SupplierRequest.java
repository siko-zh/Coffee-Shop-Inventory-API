package com.zholdigaliev.coffeeshopims.dto.SupplierDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SupplierRequest {

    @NotBlank(message = "name is required")
    private String name;

    private String contactName;

    @NotBlank(message = "phone is required")
    private String phone;

    @Email(message = "invalid email format")
    private String email;
}
