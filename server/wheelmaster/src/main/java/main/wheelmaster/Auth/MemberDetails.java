package main.wheelmaster.Auth;

import lombok.Data;
import main.wheelmaster.member.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
public class MemberDetails implements UserDetails {
    private long memberId;
    private String nickName;
    private String email;
    private Collection<? extends GrantedAuthority> roles;
    private Map<String, Object> attributes;

    public MemberDetails(long memberId, String nickName, String email, Collection<? extends GrantedAuthority> roles) {
        this.memberId = memberId;
        this.nickName = nickName;
        this.email = email;
        this.roles = roles;
    }

    public static MemberDetails create(Member member){
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        member.getRoleList().forEach(n -> {
            authorities.add(() -> n);
        });
        return new MemberDetails(member.getMemberId(), member.getNickName(), member.getEmail(), authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
