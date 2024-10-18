package com.linielt.realworldapispringboot.request;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@JsonTypeName("article")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
public record ArticleCreationRequest(@NotBlank String title, @NotBlank String description, @NotBlank String body, @NotNull List<String> tagList) {
}
