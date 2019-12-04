package com.interpark.lecture.junit.member;

import com.interpark.lecture.junit.member.vo.MemberVO;
import com.interpark.ncl.std.common.dao.CommonDAO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class MemberDAO extends CommonDAO {

    public List<MemberVO> selectMembers() {

        return super.getSqlSession().selectList("member.member.selectMember");
    }

    public MemberVO selectMember(String memberId) {

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("memberId", memberId);

        return super.getSqlSession().selectOne("member.member.selectMember", params);
    }

    public boolean insertMember(MemberVO member) {

        // String memberId = super.selectSequence(SequenceTable.IMAGE);
        String memberId = "madosa";
        member.setMemberId(memberId);
        member.setCreateDate(super.getNowDate());
        member.setModifyDate(super.getNowDate());

        return getSqlSession().insert("member.member.insertMember", member) > 0 ? true : false;
    }

    public boolean updateMember(MemberVO member) {

        member.setModifyDate(super.getNowDate());
        return getSqlSession().update("member.member.updateMember", member) > 0 ? true : false;
    }

    public boolean deleteMember(String memberId) {

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("memberId", memberId);

        return getSqlSession().delete("member.member.deleteMember", params) > 0 ? true : false ;
    }
}
