package com.example.trackerbackend.DTO.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdationRequestDTO {
    @Size(max = 50)
    @JsonProperty("full_name")
    private String fullName;
}