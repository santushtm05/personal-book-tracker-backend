package com.example.trackerbackend.DTO.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreationRequestDTO {

    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    @Size(min = 6, max = 65)
    private String password;

    @NotBlank
    @Size(max = 50)
    @JsonProperty("full_name")
    private String fullName;
}