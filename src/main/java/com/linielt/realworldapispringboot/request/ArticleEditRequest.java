package com.linielt.realworldapispringboot.request;

import jakarta.validation.constraints.NotBlank;

public record ArticleEditRequest(@NotBlank String title, @NotBlank String description, @NotBlank String body) {
}
