package main.wheelmaster.JWT;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.Base64UrlCodec;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import main.wheelmaster.exception.BusinessLogicException;
import main.wheelmaster.exception.ExceptionCode;
import main.wheelmaster.member.entity.Member;
import main.wheelmaster.response.token.TokenResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("spring.jwt.secret")
    private String secretKey;
    private static final String grantType = "Bearer";
    private static final Long ACCESS_TOKEN_EXPIRED_IN = 30 * 60 * 1000L;         // 5 min
    private static final Long REFRESH_TOKEN_EXPIRED_IN = 24 * 60 * 60 * 1000L;  // 1 day

    private static final String ROLES = "roles";
    private final UserDetailsService userDetailsService;

    @PostConstruct
    protected void init(){
        secretKey = Base64UrlCodec.BASE64URL.encode(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public TokenResponseDto.Token createTokenDto(Member member){
        Claims claims = Jwts.claims().setSubject(member.getEmail());
        claims.put(ROLES, member.getRoleList());

        Date now = new Date();

        String accessToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRED_IN))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .setHeaderParam(Header.TYPE,Header.JWT_TYPE)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRED_IN))
                .signWith(SignatureAlgorithm.ES256, secretKey)
                .compact();

        return TokenResponseDto.Token.builder()
                .grantType(grantType)
                .accessToken(accessToken)
                .accessTokenExpiredTime(ACCESS_TOKEN_EXPIRED_IN)
                .refreshToken(refreshToken)
                .refreshTokenExpiredTime(REFRESH_TOKEN_EXPIRED_IN)
                .build();
    }

    public TokenResponseDto.ReIssueToken createReIssueTokenDto(Member member) {
        Claims claims = Jwts.claims().setSubject(member.getEmail());
        claims.put(ROLES, member.getRoleList());

        Date now = new Date();

        String accessToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRED_IN))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return TokenResponseDto.ReIssueToken.builder()
                .grantType(grantType)
                .accessToken(accessToken)
                .accessTokenExpiredTime(ACCESS_TOKEN_EXPIRED_IN)
                .build();
    }

    // jwt로 인증정보를 조회
    public Authentication getAuthentication(String token) {
        // claims 추출
        Claims claims = parseClaims(token);
        // 권한 정보 없으면
        if(claims.get(ROLES) == null)
            throw new BusinessLogicException(ExceptionCode.ROLE_IS_NOT_EXISTS);
        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // Jwt 토큰 복호화해서 가져오기
    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // HTTP Request 의 Header 에서 Token Parsing -> "X-AUTH-TOKEN: jwt"
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    // jwt 의 유효성 및 만료일자 확인
    public boolean validationToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("잘못된 Jwt 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원하지 않는 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("잘못된 토큰입니다.");
        }
        return false;
    }

}
