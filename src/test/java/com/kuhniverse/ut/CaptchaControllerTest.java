package com.kuhniverse.ut;

import com.kuhniverse.domain.CaptchaFrontEndData;
import com.kuhniverse.business.CaptchaSession;
import com.kuhniverse.integration.CaptchaRepository;
import com.kuhniverse.web.CaptchaController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by tillkuhn on 21.11.2014.
 */
public class CaptchaControllerTest {

    private CaptchaController controller;

    @Before
    public void before() {

        controller = new CaptchaController();
        CaptchaSession cs = new CaptchaSession();
        CaptchaRepository cr = new CaptchaRepository();
        cr.init();
        ReflectionTestUtils.setField(cs, "captchaRepository", cr);
        ReflectionTestUtils.setField(controller,"captchaSession",cs);
    }

    @Test
    public void testStart() {
        int size = 5;
        CaptchaFrontEndData cfed = controller.start(size);
        Assert.assertEquals(size,cfed.getValues().size());
    }


    @Test
    public void testValidate() {
        controller.start(2);
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse  response = new MockHttpServletResponse();
        controller.validate(request, response);
        Assert.assertTrue(response.getStatus() == HttpStatus.FOUND.value());
    }

    @Test
    public void testGetImageIndex() {
        int size = 2;
        CaptchaFrontEndData cfed = controller.start(size);
        Assert.assertEquals(size,cfed.getValues().size());
        for (int i = 0; i < size; i++) {
            HttpServletResponse  response = new MockHttpServletResponse();
            controller.image(i, response);
            Assert.assertEquals(MediaType.IMAGE_PNG_VALUE,response.getContentType());
        }
    }

    // @Test
    public void testGetAudio() {
        int size = 2;
        CaptchaFrontEndData cfed = controller.start(size);
        Assert.assertEquals(size,cfed.getValues().size());
        for (int i = 0; i < size; i++) {
            HttpServletResponse  response = new MockHttpServletResponse();
            controller.audio("mp3", response);
            // TODO AUDIO MEDIA TYPE
            Assert.assertNotEquals(MediaType.IMAGE_PNG_VALUE, response.getContentType());
        }
    }

}
