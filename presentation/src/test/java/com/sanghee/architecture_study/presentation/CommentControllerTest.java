package com.sanghee.architecture_study.presentation;

import com.sanghee.architecture_study.application.CommentService;
import com.sanghee.architecture_study.application.command.CommentCreateCommand;
import com.sanghee.architecture_study.application.command.CommentDeleteCommand;
import com.sanghee.architecture_study.application.command.CommentUpdateCommand;
import com.sanghee.architecture_study.application.dto.CommentDto;
import com.sanghee.architecture_study.application.query.CommentListQuery;
import com.sanghee.architecture_study.common.exception.BusinessException;
import com.sanghee.architecture_study.common.exception.ErrorCode;
import com.sanghee.architecture_study.presentation.request.CommentCreateRequest;
import com.sanghee.architecture_study.presentation.request.CommentUpdateRequest;
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

@WebMvcTest(CommentController.class)
@DisplayName("CommentController 웹 계층 테스트")
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CommentService commentService;

    @Test
    @DisplayName("POST /posts/{postId}/comments - 댓글을 생성하고 201을 반환한다")
    void createComment_returns201() throws Exception {
        given(commentService.createComment(new CommentCreateCommand(1, "댓글 내용")))
                .willReturn(new CommentDto(10, 1, "댓글 내용"));

        mockMvc.perform(post("/posts/1/comments")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new CommentCreateRequest("댓글 내용"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    @DisplayName("POST /posts/{postId}/comments - 게시글이 없으면 404와 POST_NOT_FOUND를 반환한다")
    void createComment_returns404_whenPostNotFound() throws Exception {
        willThrow(new BusinessException(ErrorCode.POST_NOT_FOUND))
                .given(commentService).createComment(new CommentCreateCommand(999, "댓글"));

        mockMvc.perform(post("/posts/999/comments")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new CommentCreateRequest("댓글"))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("POST_NOT_FOUND"));
    }

    @Test
    @DisplayName("GET /posts/{postId}/comments - 댓글 목록을 반환한다")
    void getComments_returnsList() throws Exception {
        given(commentService.getComments(new CommentListQuery(1)))
                .willReturn(List.of(new CommentDto(10, 1, "댓글1"), new CommentDto(11, 1, "댓글2")));

        mockMvc.perform(get("/posts/1/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("댓글1"))
                .andExpect(jsonPath("$[1].content").value("댓글2"));
    }

    @Test
    @DisplayName("PUT /posts/{postId}/comments/{commentId} - 댓글을 수정한다")
    void updateComment_returns200() throws Exception {
        given(commentService.updateComment(new CommentUpdateCommand(1, 10, "새 내용")))
                .willReturn(new CommentDto(10, 1, "새 내용"));

        mockMvc.perform(put("/posts/1/comments/10")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new CommentUpdateRequest("새 내용"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("새 내용"));
    }

    @Test
    @DisplayName("PUT /posts/{postId}/comments/{commentId} - 다른 게시글 소속이면 404와 COMMENT_POST_MISMATCH를 반환한다")
    void updateComment_returns404_whenMismatch() throws Exception {
        willThrow(new BusinessException(ErrorCode.COMMENT_POST_MISMATCH))
                .given(commentService).updateComment(new CommentUpdateCommand(1, 10, "새 내용"));

        mockMvc.perform(put("/posts/1/comments/10")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new CommentUpdateRequest("새 내용"))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("COMMENT_POST_MISMATCH"));
    }

    @Test
    @DisplayName("DELETE /posts/{postId}/comments/{commentId} - 댓글이 없으면 404와 COMMENT_NOT_FOUND를 반환한다")
    void deleteComment_returns404_whenNotFound() throws Exception {
        willThrow(new BusinessException(ErrorCode.COMMENT_NOT_FOUND))
                .given(commentService).deleteComment(new CommentDeleteCommand(1, 999));

        mockMvc.perform(delete("/posts/1/comments/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("COMMENT_NOT_FOUND"));
    }

    @Test
    @DisplayName("DELETE /posts/{postId}/comments/{commentId} - 정상 삭제되면 204를 반환한다")
    void deleteComment_returns204() throws Exception {
        mockMvc.perform(delete("/posts/1/comments/10"))
                .andExpect(status().isNoContent());
    }
}
