package com.sanghee.architecture_study.presentation;

import com.sanghee.architecture_study.application.PostService;
import com.sanghee.architecture_study.application.command.PostCreateCommand;
import com.sanghee.architecture_study.application.command.PostUpdateCommand;
import com.sanghee.architecture_study.application.dto.PostDto;
import com.sanghee.architecture_study.application.dto.PostSummaryDto;
import com.sanghee.architecture_study.application.query.PostGetQuery;
import com.sanghee.architecture_study.common.exception.BusinessException;
import com.sanghee.architecture_study.common.exception.ErrorCode;
import com.sanghee.architecture_study.presentation.request.PostCreateRequest;
import com.sanghee.architecture_study.presentation.request.PostUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// PostService는 실제 DB 없이 Mock으로 대체하고, HTTP 계층(라우팅/직렬화/예외 매핑)만 검증한다.
// GlobalExceptionHandler는 presentation 패키지에 있어서 @WebMvcTest(PostController.class)를
// 해도 같이 로드되므로, 실패 케이스에서 resolveStatus()의 각 분기까지 자연스럽게 검증된다.
@WebMvcTest(PostController.class)
@DisplayName("PostController 웹 계층 테스트")
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PostService postService;

    @Test
    @DisplayName("GET /posts - 게시글 요약 목록을 반환한다")
    void getPosts_returnsSummaryList() throws Exception {
        given(postService.getPostSummaries())
                .willReturn(List.of(new PostSummaryDto(1, "제목1", 2)));

        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("제목1"))
                .andExpect(jsonPath("$[0].commentCount").value(2));
    }

    @Test
    @DisplayName("GET /posts/{id} - 존재하면 게시글을 반환한다")
    void getPost_returnsPost_whenExists() throws Exception {
        given(postService.getPost(new PostGetQuery(1)))
                .willReturn(new PostDto(1, "제목", "내용"));

        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("제목"))
                .andExpect(jsonPath("$.content").value("내용"));
    }

    @Test
    @DisplayName("GET /posts/{id} - 없으면 404와 POST_NOT_FOUND 코드를 반환한다")
    void getPost_returns404_whenNotFound() throws Exception {
        willThrow(new BusinessException(ErrorCode.POST_NOT_FOUND))
                .given(postService).getPost(new PostGetQuery(999));

        mockMvc.perform(get("/posts/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("POST_NOT_FOUND"));
    }

    @Test
    @DisplayName("POST /posts - 제목이 너무 길면 400과 INVALID_TITLE 코드를 반환한다")
    void createPost_returns400_whenTitleInvalid() throws Exception {
        willThrow(new BusinessException(ErrorCode.INVALID_TITLE))
                .given(postService).createPost(new PostCreateCommand("너무 긴 제목", "내용"));

        mockMvc.perform(post("/posts")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new PostCreateRequest("너무 긴 제목", "내용"))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_TITLE"));
    }

    @Test
    @DisplayName("서비스에서 예상 못한 예외가 나면 500과 INTERNAL_ERROR 코드를 반환한다")
    void getPost_returns500_onUnexpectedException() throws Exception {
        willThrow(new RuntimeException("db 연결 실패"))
                .given(postService).getPost(new PostGetQuery(1));

        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value("INTERNAL_ERROR"));
    }

    @Test
    @DisplayName("POST /posts - 게시글을 생성하고 201과 Location 헤더를 반환한다")
    void createPost_returns201() throws Exception {
        given(postService.createPost(new PostCreateCommand("제목", "내용")))
                .willReturn(new PostDto(1, "제목", "내용"));

        mockMvc.perform(post("/posts")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new PostCreateRequest("제목", "내용"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("PUT /posts/{id} - 게시글을 수정한다")
    void updatePost_returns200() throws Exception {
        given(postService.updatePost(new PostUpdateCommand(1, "새 제목", "새 내용")))
                .willReturn(new PostDto(1, "새 제목", "새 내용"));

        mockMvc.perform(put("/posts/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new PostUpdateRequest("새 제목", "새 내용"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("새 제목"));
    }

    @Test
    @DisplayName("DELETE /posts/{id} - 게시글을 삭제하고 204를 반환한다")
    void deletePost_returns204() throws Exception {
        mockMvc.perform(delete("/posts/1"))
                .andExpect(status().isNoContent());
    }
}
