package main.wheelmaster.member.entity;

import javax.persistence.PrePersist;

public class MemberListner {
    @PrePersist
    public void persist(Member member){
        member.setRoles(Roles.ROLE_USER.toString());
    }
}
