package com.interpark.lecture.junit.member;

import com.interpark.lecture.junit.member.vo.MemberVO;
import com.interpark.ncl.std.common.controller.CommonController;
import com.interpark.ncl.std.common.error.CommonException;
import com.interpark.ncl.std.common.response.CommonResultDataVO;
import com.interpark.ncl.std.common.response.ResponseVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(value = "Member", tags = {"Member"})
@RestController
@RequestMapping(value = "/lecture-junit")
public class MemberController extends CommonController {

    @Autowired
    private MemberService memberService;

    @RequestMapping(value = "/v1/members", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseVO<MemberVO> getMembers() {

        List<MemberVO> result = null;
        try {
            result = memberService.getMember();
        } catch(Exception e) {
            throw new CommonException(e);
        }

        return super.makeResponseData(HttpStatus.OK, result);
    }

    @RequestMapping(value = "/v1/members/{memberId}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseVO<MemberVO> getMember(@PathVariable String memberId) {

        MemberVO result = null;
        try {
            result = memberService.getMember(memberId);
        } catch(Exception e) {
            throw new CommonException(e);
        }

        return super.makeResponseData(HttpStatus.OK, result);
    }

    @RequestMapping(value = "/v1/members", method = RequestMethod.POST, consumes = "application/json", produces = "application/json;charset=UTF-8")
    public ResponseVO<CommonResultDataVO> createMember(@RequestBody @Valid MemberVO member, BindingResult bindingResult) {

        boolean result = false;
        try {
            result = memberService.createMember(member);
        } catch(Exception e) {
            throw new CommonException(e);
        }

        return super.makeResponseData(HttpStatus.OK, result ? super.getProcessSuccessCode() : super.getProcessFailCode());
    }
    @RequestMapping(value = "/v1/members/{memberId}", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
    public ResponseVO<CommonResultDataVO> modifyMember(@PathVariable String memberId, @RequestBody @Valid MemberVO member, BindingResult bindingResult) {

        boolean result = false;
        try {
            result = memberService.modifyMember(member);
        } catch(Exception e) {
            throw new CommonException(e);
        }

        return super.makeResponseData(HttpStatus.OK, result ? super.getProcessSuccessCode() : super.getProcessFailCode());
    }
    @RequestMapping(value = "/v1/members/{memberId}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
    public ResponseVO<CommonResultDataVO> removeExhibition(@PathVariable String memberId) {

        boolean result = false;
        try {
            result = memberService.removeMember(memberId);
        } catch(Exception e) {
            throw new CommonException(e);
        }

        return super.makeResponseData(HttpStatus.OK, result ? super.getProcessSuccessCode() : super.getProcessFailCode());
    }
}
