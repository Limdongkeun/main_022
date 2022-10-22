package main.wheelmaster.vote.controller;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.wheelmaster.Auth.MemberDetails;
import main.wheelmaster.vote.dto.VoteGetDto;
import main.wheelmaster.vote.dto.VotePostDto;
import main.wheelmaster.vote.dto.VoteResponseDto;
import main.wheelmaster.vote.entity.Vote;
import main.wheelmaster.vote.mapper.VoteMapper;
import main.wheelmaster.vote.service.VoteService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/wheel-center/{wheelCenterId}/vote")
public class VoteController {

    private final VoteService voteService;
    private final VoteMapper mapper;

    @ApiOperation(value = "추천&비추천")
    @PostMapping
    public VoteResponseDto vote(@AuthenticationPrincipal MemberDetails memberDetails,
                                @Positive @PathVariable("wheelCenterId") Long wheelCenterId,
                                @RequestBody VotePostDto votePostDto) {
        votePostDto.setWheelCenterId(wheelCenterId);
        votePostDto.setMemberId(memberDetails.getMemberId());

        Vote savedVote = voteService.create(mapper.VotePostDtoToVote(votePostDto));

        return mapper.VoteToVoteResponseDto(savedVote);
    }

    @ApiOperation(value = "추천&비추천 삭제")
    @DeleteMapping("/{voteId}")
    public void cancelVote(@AuthenticationPrincipal MemberDetails memberDetails,
                           @Positive @PathVariable("wheelCenterId")Long wheelCenterId,
                           @Positive @PathVariable("voteId") Long voteId) {

        Vote vote = new Vote();
        vote.setVoteId(voteId);

        voteService.deleteVote(vote);
    }

    @ApiOperation(value = "추천확인")
    @GetMapping
    public VoteResponseDto getVote(@AuthenticationPrincipal MemberDetails memberDetails,
                                   @Positive @PathVariable("wheelCenterId") Long wheelCenterId){

        VoteGetDto voteGetDto = new VoteGetDto(memberDetails.getMemberId(), wheelCenterId);
        Vote vote = voteService.readVote(mapper.VoteGetResponseDtoToVote(voteGetDto));
        return (vote == null) ? null : mapper.VoteToVoteResponseDto(vote);
    }

}