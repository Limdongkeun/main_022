package main.wheelmaster.vote.dto;

import lombok.*;
import main.wheelmaster.member.entity.Member;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoteGetDto {
    private long memberId;
    private Long wheelCenterId;
}
