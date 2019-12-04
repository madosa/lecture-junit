package com.interpark.lecture.junit.member;

import com.interpark.lecture.junit.member.vo.MemberVO;
import com.interpark.ncl.std.common.error.CommonErrorCode;
import com.interpark.ncl.std.common.error.CommonException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    private MemberDAO memberDAO;

    @Autowired
    MemberService(MemberDAO memberDAO) {
        this.memberDAO = memberDAO;
    }

    public List<MemberVO> getMember() throws Exception {

        List<MemberVO> members = memberDAO.selectMembers();
        if(members == null || members.size() == 0) {
            throw new CommonException(CommonErrorCode.NOTFOUND);
        }

        return members;
    }

    public MemberVO getMember(String memberId) throws Exception {

        MemberVO member = memberDAO.selectMember(memberId);
        if(member == null) {
            throw new CommonException(CommonErrorCode.NOTFOUND);
        }

        return member;
    }

    public boolean createMember(MemberVO member) throws Exception {

        boolean result = memberDAO.insertMember(member);

        return result;
    }

    public boolean modifyMember(MemberVO member) throws Exception {

        boolean result = memberDAO.updateMember(member);

        return result;
    }

    public boolean removeMember(String memberId) throws Exception {

        MemberVO member = memberDAO.selectMember(memberId);
        if(member == null) {
            throw new CommonException(CommonErrorCode.NOTFOUND);
        }

        boolean result = memberDAO.deleteMember(memberId);

        return true;
    }
}
