package com.linielt.realworldapispringboot.rest.controller;

import com.linielt.realworldapispringboot.model.Tag;
import com.linielt.realworldapispringboot.response.ListOfTagsResponse;
import com.linielt.realworldapispringboot.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/tags")
    public ResponseEntity<ListOfTagsResponse> getTags() {
        return new ResponseEntity<>(new ListOfTagsResponse(tagService.getListOfTags()), HttpStatus.OK);
    }
}
