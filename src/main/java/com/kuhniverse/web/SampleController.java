package com.kuhniverse.web;

import com.kuhniverse.business.CaptchaSession;
import com.kuhniverse.domain.CaptchaValidationException;
import com.kuhniverse.domain.CaptchaValidationResult;
import com.kuhniverse.domain.SampleRequest;
import com.kuhniverse.domain.SampleResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.UUID;

/**
 * Sample controller to simulate a "real" rest post request that contains captchadata
 * for validation purposes
 *
 * Created by tillkuhn on 27.05.2015.
 */
@RestController
public class SampleController {

    private Logger log = LoggerFactory.getLogger(SampleController.class);

    @Inject
    private CaptchaSession captchaSession;

    @RequestMapping(value = "/api/sample", method = RequestMethod.POST)
    @ResponseBody
    public SampleResponse processResponse( @RequestBody SampleRequest request) {
        CaptchaValidationResult result = captchaSession.validate(request.getCaptchaData());
        if (result.isValid()) {
            SampleResponse resp = new SampleResponse();
            resp.setRequestId(UUID.randomUUID().toString());
            log.debug("Received captcha request {} returning {} valid {}", request, resp, result);
            return resp;
        } else {
            throw new CaptchaValidationException(result, "Verification failed: " + result);
        }
    }

}
