package com.kuhniverse.it;

import com.kuhniverse.business.CaptchaSession;
import com.kuhniverse.domain.CaptchaFrontEndData;
import com.kuhniverse.domain.CaptchaValidationException;
import com.kuhniverse.domain.CaptchaValidationResult;
import com.kuhniverse.web.CaptchaController;
import org.junit.Assert;
import org.junit.Test;

import javax.inject.Inject;

/**
 * Created by tillkuhn on 27.05.2015.
 */
public class CaptchaIntegrationTest extends BaseIntegrationTest {

    @Inject
    private CaptchaController controller;

    @Test
    public void testCpatchas() {
        int size =  CaptchaSession.MIN_OPTION_COUNT;
        CaptchaFrontEndData cfe = controller.start(size);
        Assert.assertEquals(size, cfe.getValues().size());
    }

    @Test
    public void testInvalidOptionCount() {
        int size = CaptchaSession.MIN_OPTION_COUNT -1;
        try {
            CaptchaFrontEndData cfe = controller.start(size);
            Assert.fail("calling init with insufficient options must fail");
        } catch (CaptchaValidationException e) {
            Assert.assertTrue("Expected " + CaptchaValidationResult.INSUFFICIENT_OPTION_COUNT, e.getValidationResult() == CaptchaValidationResult.INSUFFICIENT_OPTION_COUNT);
        }
    }


}
