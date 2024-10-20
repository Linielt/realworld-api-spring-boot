package com.linielt.realworldapispringboot.service;

import com.linielt.realworldapispringboot.model.Tag;
import com.linielt.realworldapispringboot.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Transactional
    public Set<Tag> loadTagsIfPresent(List<String> tagValues) {
        Set<Tag> tags = new HashSet<>();
        for (String tagValue : tagValues) {
            if (tagRepository.existsTagByValue(tagValue)) {
                tags.add(tagRepository.getTagByValue(tagValue));
            }
            else {
                Tag createdTag = new Tag(tagValue);
                tags.add(createdTag);
                tagRepository.save(createdTag);
            }
        }

        return tags;
    }

    @Transactional(readOnly = true)
    public List<Tag> getListOfTags() {
        return tagRepository.findAll();
    }
}
