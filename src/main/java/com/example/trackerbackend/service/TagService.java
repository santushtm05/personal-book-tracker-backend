package com.example.trackerbackend.service;

import com.example.trackerbackend.DTO.request.tag.TagCreationRequestDTO;
import com.example.trackerbackend.DTO.response.TagDTO;
import com.example.trackerbackend.entity.Tag;

import java.util.List;

public interface TagService {
    TagDTO createTag(TagCreationRequestDTO tagCreationRequestDTO);
    List<TagDTO> getTags();
}