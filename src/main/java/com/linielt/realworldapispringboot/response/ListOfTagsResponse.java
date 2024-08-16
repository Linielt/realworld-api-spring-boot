package com.linielt.realworldapispringboot.response;

import com.linielt.realworldapispringboot.model.Tag;

import java.util.List;

public class ListOfTagsResponse {

    private final List<String> tags;

    public ListOfTagsResponse(List<Tag> tags) {
        this.tags = tags.stream().map(Tag::getValue).toList();
    }

    public List<String> getTags() {
        return tags;
    }
}
