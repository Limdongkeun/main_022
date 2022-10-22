package main.wheelmaster.vote.dto;

import lombok.*;
import main.wheelmaster.member.entity.Member;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VotePostDto {
    private Long wheelCenterId;
    private long memberId;
    @NonNull
    private Boolean upDown;
}
