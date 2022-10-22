package main.wheelmaster.member.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import main.wheelmaster.comment.entity.Comment;
import main.wheelmaster.favoritePlace.entity.FavoritePlace;
import main.wheelmaster.vote.entity.Vote;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Getter
@Setter
@Entity
@NoArgsConstructor
@EntityListeners(value = MemberListner.class)
public class Member{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false, updatable = false, length = 200, unique = true)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;
    @Column(nullable = false, unique = true)
    private String nickName;
    @Column(length = 50, unique = true)
    private String phoneNumber;

    private String provider;
    private String roles;

    public List<String> getRoleList(){
        if (this.roles.length() > 0)
            return Arrays.asList(this.roles.split(","));
        return new ArrayList<>();
    }

    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY)
    private List<Comment> comments;

    public void addComment(Comment comment){
        if(!this.comments.contains(comment) && comment != null){
            comments.add(comment);

            if(comment.getMember() == null)
                comment.setMember(this);
        }
    }

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Vote> votes;

    public void addVote(Vote vote) {
        if(!this.votes.contains(vote) && vote != null){
            votes.add(vote);

            if(vote.getMember() == null)
                vote.setMember(this);
        }
    }

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<FavoritePlace> favoritePlaces;

    public void addFavoritePlace(FavoritePlace favoritePlace){
        if(!this.favoritePlaces.contains(favoritePlace) && favoritePlace != null) {
            favoritePlaces.add(favoritePlace);

            if(favoritePlace.getMember() == null)
                favoritePlace.setMember(this);
        }
    }

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime modifiedAt = LocalDateTime.now();

}