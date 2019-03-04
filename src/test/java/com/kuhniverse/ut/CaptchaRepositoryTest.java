package com.kuhniverse.ut;

import com.kuhniverse.domain.CaptchaAnswer;
import com.kuhniverse.integration.CaptchaRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

/**
 * Created by tillkuhn on 26.05.2015.
 */
public class CaptchaRepositoryTest {

    private CaptchaRepository captchaRepository;

    @Before
    public void init() {
        this.captchaRepository = new CaptchaRepository();
    }

    @Test
    public void testInit() {
        captchaRepository.init();
        Assert.assertTrue(captchaRepository.getImages().size() > 0);
        Assert.assertTrue(captchaRepository.getAudios().size() > 0);
        for (CaptchaAnswer ca : captchaRepository.getImages()) {
            Assert.assertNotNull(ca.getName());
            Assert.assertNotNull(ca.getPath());
        }
        for (CaptchaAnswer ca : captchaRepository.getAudios()) {
            Assert.assertNotNull(ca.getName());
            Assert.assertNotNull(ca.getPath());
        }
    }

    @Test
    public void testImageResource() {
        captchaRepository.init();
        String path1 = captchaRepository.getImages().get(0).getPath();
        Assert.assertNotNull(path1);
        InputStream is = captchaRepository.getImageStream(path1);
        Assert.assertNotNull("Image Path " + path1 + " must exist", is);
    }

    @Test
    public void testAudioResource() {
        captchaRepository.init();
        String path1 = captchaRepository.getAudios().get(0).getPath();
        Assert.assertNotNull(path1);
        InputStream is = captchaRepository.getAudioStream(path1,CaptchaRepository.AudioType.MP3);
        Assert.assertNotNull("Audio Path " + path1 + " must exist", is);
    }

}
