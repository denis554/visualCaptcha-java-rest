package com.kuhniverse.it;

import com.kuhniverse.Application;
import com.kuhniverse.domain.CaptchaData;
import com.kuhniverse.domain.CaptchaFrontEndData;
import com.kuhniverse.domain.CaptchaValidationException;
import com.kuhniverse.domain.SampleRequest;
import com.kuhniverse.domain.SampleResponse;
import com.kuhniverse.web.CaptchaController;
import com.kuhniverse.web.SampleController;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;

/**
 * Created by tillkuhn on 27.05.2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class SampleIntegrationTest {

    @Inject
    private SampleController controller;

    @Inject
    private CaptchaController captchaController;

    @Test
    public void testSampleRequest() {
        CaptchaFrontEndData cfed = captchaController.start(3);
        SampleRequest req = new SampleRequest();
        CaptchaData cd = new CaptchaData();
        cd.setName("invalid");
        cd.setValue("invalid");
        req.setCaptchaData(cd);
        try {
            SampleResponse sr = controller.processResponse(req);
            Assert.fail("There should be an exception");
        } catch (CaptchaValidationException expected) {

        }
    }
}
