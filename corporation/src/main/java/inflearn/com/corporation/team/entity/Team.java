package inflearn.com.corporation.team.entity;

import inflearn.com.corporation.member.entity.Member;
import inflearn.com.corporation.member.entity.type.Role;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;
    
    @Column(nullable = false)
    private String name;

    private String manager;

    @OneToMany(mappedBy = "team")
    List<Member> members = new ArrayList<>();

    private Long memberCount;

    protected Team() {}

    public Team(String name) {
        this.name = name;
        this.memberCount = 0L;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getManager() {
        return manager;
    }

    public List<Member> getMembers() {
        return members;
    }

    public Long getMemberCount() {
        return memberCount;
    }

    public boolean hasManager() {
        return members.stream().anyMatch(member -> member.getRole() == Role.MANAGER);
    }

    public void setManagerName(String manager) {
        this.manager = manager;
    }

    public void addMember(Member member) {
        members.add(member);
        memberCount++;
    }

}
