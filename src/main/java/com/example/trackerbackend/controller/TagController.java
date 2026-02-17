package com.example.trackerbackend.controller;

import com.example.trackerbackend.DTO.request.tag.TagCreationRequestDTO;
import com.example.trackerbackend.DTO.response.APIResponse;
import com.example.trackerbackend.DTO.response.TagDTO;
import com.example.trackerbackend.service.TagService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping("/")
    public ResponseEntity<APIResponse<List<TagDTO>>> getAllTags() {
        List<TagDTO> tags = this.tagService.getTags();
        APIResponse<List<TagDTO>> response = APIResponse.<List<TagDTO>>builder()
                .success(true)
                .error(false)
                .message("Fetched Tags")
                .data(tags)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/create")
    public ResponseEntity<APIResponse<TagDTO>> createTag(@Valid @RequestBody TagCreationRequestDTO tagCreationRequestDTO) {
        TagDTO createdTag = this.tagService.createTag(tagCreationRequestDTO);
        APIResponse<TagDTO> response = APIResponse.<TagDTO>builder()
                .success(true)
                .error(false)
                .message("Created Tag!")
                .data(createdTag)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}