package main.wheelmaster.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.wheelmaster.Auth.MemberDetails;
import main.wheelmaster.member.entity.Member;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CommentRequestDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class createCommentDto{
        @NotNull
        private long wheelCenterId;

        @NotEmpty
        private String comment;

        private long memberId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class updateCommentDto{
        @NotNull
        private long wheelCenterId;

        private long commentId;

        @NotEmpty
        private String comment;

        private long memberId;
    }
}
