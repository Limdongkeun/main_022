package main.wheelmaster.member.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.wheelmaster.Auth.MemberDetails;
import main.wheelmaster.exception.BusinessLogicException;
import main.wheelmaster.exception.ExceptionCode;
import main.wheelmaster.member.dto.MemberRequestDto;
import main.wheelmaster.member.entity.Member;
import main.wheelmaster.member.mapper.MemberMapper;
import main.wheelmaster.member.service.MemberService;
import main.wheelmaster.response.SingleResponseWithMessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

import java.util.HashMap;

import static main.wheelmaster.member.SessionConst.LOGIN_MEMBER;


@Validated
@Slf4j
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberMapper mapper;
    private final MemberService memberService;

    //회원정보 수정
    @ApiOperation(value = "회원정보 수정")
    @PatchMapping
    public SingleResponseWithMessageDto updateMember(@RequestBody MemberRequestDto.updateDto updateDto,
                                                     @AuthenticationPrincipal MemberDetails memberDetails) {
        return new SingleResponseWithMessageDto(memberService.updateMember(updateDto
                .setMemberId(memberDetails.getMemberId())), "SUCCESS");
    }

    //회원정보 삭제
    @ApiOperation(value = "회원정보 삭제")
    @DeleteMapping("/{memberId}")
    public ResponseEntity deleteMember(@AuthenticationPrincipal MemberDetails memberDetails) {
        memberService.deleteMember(memberDetails.getMemberId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //로그아웃
    @ApiOperation(value = "로그아웃")
    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session == null) {
            throw new BusinessLogicException(ExceptionCode.CONSTRAINT_VIOLATION_ERROR);
        }

        session.invalidate();
        return "redirect:/";
    }
}