package com.mycompany.app.dao;

import java.util.List;
import com.mycompany.app.domain.Member;


public interface MemberDAO {
    void addMember(Member member);
    void updateMember(Member member);
    void deleteMember(int memberId);
    List<Member> getAllMembers();
    Member getMemberById(int memberId);
}
