package com.example.trackerbackend.DTO.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Integer id;
    private String username;
    @JsonProperty("full_name")
    private String fullName;

}