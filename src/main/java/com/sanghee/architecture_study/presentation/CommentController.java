package com.sanghee.architecture_study.presentation;

import com.sanghee.architecture_study.application.CommentService;
import com.sanghee.architecture_study.application.command.CommentCreateCommand;
import com.sanghee.architecture_study.application.command.CommentDeleteCommand;
import com.sanghee.architecture_study.application.command.CommentUpdateCommand;
import com.sanghee.architecture_study.application.dto.CommentDto;
import com.sanghee.architecture_study.application.query.CommentListQuery;
import com.sanghee.architecture_study.presentation.request.CommentCreateRequest;
import com.sanghee.architecture_study.presentation.request.CommentUpdateRequest;
import com.sanghee.architecture_study.presentation.response.CommentCreateResponse;
import com.sanghee.architecture_study.presentation.response.CommentGetResponse;
import com.sanghee.architecture_study.presentation.response.CommentUpdateResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentCreateResponse> createComment(
            @PathVariable int postId,
            @RequestBody CommentCreateRequest commentCreateRequest
    ) {
        CommentDto commentDto = commentService.createComment(
                new CommentCreateCommand(postId, commentCreateRequest.content())
        );

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(commentDto.id())
                .toUri();

        return ResponseEntity.created(location).body(
                new CommentCreateResponse(commentDto.id(), commentDto.postId(), commentDto.content())
        );
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentGetResponse>> getComments(@PathVariable int postId) {
        List<CommentDto> comments = commentService.getComments(new CommentListQuery(postId));
        List<CommentGetResponse> response = comments.stream()
                .map(dto -> new CommentGetResponse(dto.id(), dto.postId(), dto.content()))
                .toList();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentUpdateResponse> updateComment(
            @PathVariable int postId,
            @PathVariable int commentId,
            @RequestBody CommentUpdateRequest commentUpdateRequest
    ) {
        CommentDto commentDto = commentService.updateComment(
                new CommentUpdateCommand(
                        postId,
                        commentId,
                        commentUpdateRequest.content())
        );
        return ResponseEntity.ok(
                new CommentUpdateResponse(
                        commentDto.id(),
                        commentDto.postId(),
                        commentDto.content()
                )
        );
    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable int postId,
            @PathVariable int commentId
    ) {
        commentService.deleteComment(new CommentDeleteCommand(postId, commentId));
        return ResponseEntity.noContent().build();
    }
}
