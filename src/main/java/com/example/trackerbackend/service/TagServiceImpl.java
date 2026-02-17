package com.example.trackerbackend.service;

import com.example.trackerbackend.DAO.TagDAO;
import com.example.trackerbackend.DTO.request.tag.TagCreationRequestDTO;
import com.example.trackerbackend.DTO.response.TagDTO;
import com.example.trackerbackend.entity.Tag;
import com.example.trackerbackend.utils.conversion.EntityConversionUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagDAO tagDAO;

    @Override
    @Transactional
    public TagDTO createTag(TagCreationRequestDTO tagCreationRequestDTO) {
        // check for blank tag name and throw exception
        Tag tagDB = tagDAO.findByName(
                tagCreationRequestDTO.getName()).orElse(
                        tagDAO.save(
                                Tag.builder()
                                        .id(null)
                                        .name(tagCreationRequestDTO.getName())
                                        .build()));
        return EntityConversionUtils.toTagDTO(tagDB);
    }

    @Override
    public List<TagDTO> getTags() {
        List<Tag> tagsDB = tagDAO.findAll();
        return EntityConversionUtils.toTagDTOs(tagsDB);
    }
}