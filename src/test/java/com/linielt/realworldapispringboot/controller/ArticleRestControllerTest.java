package com.linielt.realworldapispringboot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linielt.realworldapispringboot.dtos.ArticleDto;
import com.linielt.realworldapispringboot.model.User;
import com.linielt.realworldapispringboot.request.ArticleCreationRequest;
import com.linielt.realworldapispringboot.rest.controller.ArticleController;
import com.linielt.realworldapispringboot.security.SecurityConfig;
import com.linielt.realworldapispringboot.service.ArticleService;
import com.linielt.realworldapispringboot.service.JwtTokenProviderService;
import com.linielt.realworldapispringboot.service.UserService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArticleController.class)
@Import(SecurityConfig.class)
public class ArticleRestControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private ArticleService articleService;
    @MockBean
    private UserService userService;
    @MockBean
    JwtTokenProviderService jwtTokenProviderService;

    @MethodSource("createInvalidArticleCreationRequests")
    @ParameterizedTest
    void creatingArticlesWithInvalidRequestBodyTest(ArticleCreationRequest invalidRequest) throws Exception {
        when(userService.getUserByToken(any(JwtAuthenticationToken.class))).thenReturn(User.createUser("test", "test", "test"));
        mockMvc.perform(post("/articles")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> createInvalidArticleCreationRequests() {
        return Stream.of(
                Arguments.of(new ArticleCreationRequest(null, "test", "test", Collections.emptyList())),
                Arguments.of(new ArticleCreationRequest("test", null, "test", Collections.emptyList())),
                Arguments.of(new ArticleCreationRequest("test", "test", null, Collections.emptyList())),
                Arguments.of(new ArticleCreationRequest("test", "test", "test", null)),
                Arguments.of(new ArticleCreationRequest(" ", "test", "test", Collections.emptyList())),
                Arguments.of(new ArticleCreationRequest("test", " ", "test", Collections.emptyList())),
                Arguments.of(new ArticleCreationRequest("test", "test", " ", Collections.emptyList()))
        );
    }
}
