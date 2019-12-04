package com.interpark.lecture.junit.member.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class MemberVO implements Serializable {

    private static final long serialVersionUID = 7239237902991273723L;

    private String memberId;
    private String memberName;
    private String gender;
    private int dateOfBirth;
    private String email;
    private String mobile;
    private Date createDate;
    private Date modifyDate;
}
