package main.wheelmaster.comment.controller;


import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import main.wheelmaster.Auth.MemberDetails;
import main.wheelmaster.comment.dto.CommentRequestDto;
import main.wheelmaster.comment.entity.Comment;
import main.wheelmaster.comment.mapper.CommentMapper;
import main.wheelmaster.comment.service.CommentService;
import main.wheelmaster.response.MessageResponseDto;
import main.wheelmaster.response.SingleResponseWithMessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import static main.wheelmaster.comment.dto.CommentResponseDto.*;

@Validated
@RestController
@RequestMapping("wheel-center/{wheelCenterId}/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper mapper;

    @ApiOperation(value = "후기 작성")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SingleResponseWithMessageDto<CommentInfo> createComment(@AuthenticationPrincipal MemberDetails memberDetails,
                                                                   @Positive@PathVariable("wheelCenterId") long wheelCenterId,
                                                                   @RequestBody @Valid CommentRequestDto.createCommentDto createCommentDto){

        createCommentDto.setWheelCenterId(wheelCenterId);
        createCommentDto.setMemberId(memberDetails.getMemberId());
        Comment comment = commentService.createComment(createCommentDto);
        return new SingleResponseWithMessageDto<>(mapper.commentToCommentInfo(comment),"SUCCESS");
    }

    @ApiOperation(value = "후기 수정")
    @PatchMapping("/{commentId}")
    public SingleResponseWithMessageDto<CommentInfo> updateComment(@AuthenticationPrincipal MemberDetails memberDetails,
                                        @Positive @PathVariable("wheelCenterId") long wheelCenterId,
                                        @Positive @PathVariable("commentId") long commentId,
                                        @RequestBody @Valid CommentRequestDto.updateCommentDto updateCommentDto){
        updateCommentDto.setMemberId(memberDetails.getMemberId());
        updateCommentDto.setCommentId(commentId);
        updateCommentDto.setWheelCenterId(wheelCenterId);
        Comment comment = commentService.updateComment(updateCommentDto);
        return new SingleResponseWithMessageDto<>(mapper.commentToCommentInfo(comment),"SUCCESS");
    }

    @ApiOperation(value = "후기 삭제")
    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public MessageResponseDto deleteComment(@AuthenticationPrincipal MemberDetails memberDetails,
                                         @Positive @PathVariable("wheelCenterId") long wheelCenterId,
                                         @Positive @PathVariable("commentId") long commentId){
        commentService.deleteComment(commentId,memberDetails.getMemberId());
        return new MessageResponseDto("NO_CONTENT");
    }
}