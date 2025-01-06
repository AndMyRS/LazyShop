package com.andrusevich.catalogservice.controller.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewProductPayload(
        @NotNull
        @Size(min = 5, max = 65, message = "Name of product should be between {min} and {max} characters")
        String name,
        @Size(max = 550)
        String description
) {
}
