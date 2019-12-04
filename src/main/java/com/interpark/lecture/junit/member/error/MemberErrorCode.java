package com.interpark.lecture.junit.member.error;

import com.interpark.ncl.std.common.error.ErrorCode;

public enum MemberErrorCode implements ErrorCode {

    INVALID_MEMBER_ID("MEM0N01-400")
    ;

    private String errorCode;

    MemberErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }
}
