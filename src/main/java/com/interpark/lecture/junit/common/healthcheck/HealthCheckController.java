package com.interpark.lecture.junit.common.healthcheck;

import com.interpark.ncl.std.common.controller.CommonController;
import com.interpark.ncl.std.common.response.ResponseVO;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * HealthCheckController.java
 * 
 * @author Ma Joonchae
 */
@Api(value = "Health Check", tags = {"HealthCheck"})
@RestController
@RequestMapping(value = "/lecture-junit/v1")
public class HealthCheckController extends CommonController {

    @RequestMapping(value = "/health/check", method = RequestMethod.GET)
    public ResponseVO<String> doHealthCheck() {
        return super.makeResponseData(HttpStatus.OK, "API Server Alive!!");
    }
}