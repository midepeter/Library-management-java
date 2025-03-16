package com.mycompany.app.dao;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

import com.mycompany.app.domain.Member;

public class MemberDAOImplTest {

    @Test
    public void testAddMemberSuccessful() {
        MemberDAOImpl memberDAO = new MemberDAOImpl(null);
        Member member = new Member();
        member.setMemberId(1);
        member.setName("Test Name");
        member.setEmail("test@email.com"); 
        member.setPhone("1234567890");
        
        memberDAO.addMember(member);
        
        List<Member> members = memberDAO.getAllMembers();
        assertEquals(1, members.size());
        assertEquals("Test Name", members.get(0).getName());
        assertEquals("test@email.com", members.get(0).getEmail());
        assertEquals("1234567890", members.get(0).getPhone());
    }
    
    @Test
    public void testUpdateMemberSuccessful() {
        MemberDAOImpl memberDAO = new MemberDAOImpl(null);
        Member member = new Member();
        member.setMemberId(1);
        member.setName("Updated Name");
        member.setEmail("updated@email.com");
        member.setPhone("9876543210");
        
        memberDAO.updateMember(member);
        
        List<Member> members = memberDAO.getAllMembers();
        assertEquals(1, members.size());
        assertEquals("Updated Name", members.get(0).getName());
        assertEquals("updated@email.com", members.get(0).getEmail());
        assertEquals("9876543210", members.get(0).getPhone());
    }
    
    @Test
    public void testDeleteMemberSuccessful() {
        MemberDAOImpl memberDAO = new MemberDAOImpl(null);
        
        memberDAO.deleteMember(1);
        
        List<Member> members = memberDAO.getAllMembers();
        assertEquals(0, members.size());
    }
    
    @Test
    public void testGetAllMembersNoMemberFound() {
        MemberDAOImpl memberDAO = new MemberDAOImpl(null);
        List<Member> members = memberDAO.getAllMembers();
        
        assertEquals(0, members.size());
    }
    
    @Test
    public void testGetMemberById() {
        MemberDAOImpl memberDAO = new MemberDAOImpl(null);
        Member member = new Member();
        member.setMemberId(1);
        member.setName("Test Name");
        member.setEmail("test@email.com");
        member.setPhone("1234567890");
        memberDAO.addMember(member);
        
        Member foundMember = memberDAO.getMemberById(1);
        
        assertNotNull(foundMember);
        assertEquals("Test Name", foundMember.getName());
        assertEquals("test@email.com", foundMember.getEmail());
        assertEquals("1234567890", foundMember.getPhone());
    }

    @Test
    public void testGetMemberByIdNotFound() {
        MemberDAOImpl memberDAO = new MemberDAOImpl(null);
        
        Member foundMember = memberDAO.getMemberById(999);
        
        assertNull(foundMember);
    }
}