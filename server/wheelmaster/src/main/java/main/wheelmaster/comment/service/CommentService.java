package main.wheelmaster.comment.service;

import lombok.RequiredArgsConstructor;
import main.wheelmaster.comment.dto.CommentRequestDto.createCommentDto;
import main.wheelmaster.comment.entity.Comment;
import main.wheelmaster.comment.mapper.CommentMapper;
import main.wheelmaster.comment.repository.CommentRepository;
import main.wheelmaster.exception.BusinessLogicException;
import main.wheelmaster.exception.ExceptionCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static main.wheelmaster.comment.dto.CommentRequestDto.*;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper mapper;

    public Comment createComment(createCommentDto createCommentDto) {
        return commentRepository.save(mapper.createCommentDtoToComment(createCommentDto));
    }

    public Comment updateComment(updateCommentDto updateCommentDto) {
        Comment findComment = findVerifiedComment(updateCommentDto.getCommentId(), updateCommentDto.getMemberId());
        Optional.ofNullable(updateCommentDto.getComment()).ifPresent(findComment::setComment);
        return commentRepository.save(mapper.updateCommentDtoToComment(updateCommentDto));
    }

    public void deleteComment(long commentId, long memberId) {
        Comment findComment = findVerifiedComment(commentId, memberId);
        commentRepository.delete(findComment);
    }

    @Transactional(readOnly = true)
    public Comment findVerifiedComment(long commentId, long memberId){
        Optional<Comment> optionalComment = commentRepository.findByIdAndCenterIdAndMemberId(commentId,memberId);
        return optionalComment.orElseThrow(() -> new BusinessLogicException(ExceptionCode.COMMENT_NOT_FOUND));
    }

}
