
package com.example.trackerbackend.DTO.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreationRequestDTO {

    @NotBlank(message = "Username is required")
    @Size(max = 50, message = "Username must not exceed 50 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&^#()_\\-+=])[A-Za-z\\d@$!%*?&^#()_\\-+=]{8,}$",
            message = "Password must contain 8+ chars, upper, lower, digit and special character"
    )
    private String password;

    @NotBlank(message = "Full name is required")
    @Size(max = 50, message = "Full name must not exceed 50 characters")
    @JsonProperty("full_name")
    private String fullName;
}