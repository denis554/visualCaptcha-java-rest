package com.kuhniverse.web;

import com.kuhniverse.business.CaptchaSession;
import com.kuhniverse.domain.CaptchaData;
import com.kuhniverse.domain.CaptchaFrontEndData;
import com.kuhniverse.domain.CaptchaValidationResult;
import com.kuhniverse.integration.CaptchaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

/**
 * controller methods for captcha API
 *
 * @author by timafe on 22.05.2015.
 */

@RestController
public class CaptchaController {

    private Logger log = LoggerFactory.getLogger(CaptchaController.class);
    private static final int DEFAULT_NUM_OPTIONS = 5;

    @Inject
    private CaptchaSession captchaSession;

    /**
     * Get init json
     */
    @RequestMapping(value = "/start/{howMany}", method = RequestMethod.GET)
    @ResponseBody
    public CaptchaFrontEndData start(@PathVariable int howMany) {
        return captchaSession.start(howMany);
    }


    /**
     * Get Image response
     */
    @RequestMapping(value = "/image/{index}", method = RequestMethod.GET)
    // RequestParam boolean retina
    public void image(@PathVariable int index, HttpServletResponse response) {
        boolean retina = false; // TODO Implement
        InputStream input = captchaSession.getImage(index, retina);
        MediaType contentType = MediaType.IMAGE_PNG;
        writeResponse(contentType, input, response);
    }

    /**
     * Get Audio response
     */
    @RequestMapping(value = "/audio/{audioType}", method = RequestMethod.GET)
    public void audio(@PathVariable String audioType, HttpServletResponse response) {
        CaptchaRepository.AudioType audioTypEnum = CaptchaRepository.AudioType.valueOf(audioType.toUpperCase());
        // TODO exeption handling if type not found
        InputStream input = captchaSession.getAudio(audioTypEnum);
        MediaType contentType = MediaType.APPLICATION_OCTET_STREAM;
        writeResponse(contentType, input, response);
    }

    /**
     * Get Audio response in default mp3 format
     */
    @RequestMapping(value = "/audio", method = RequestMethod.GET)
    public void audio(HttpServletResponse response) {
        audio(CaptchaRepository.AudioType.MP3.name().toLowerCase(), response);
    }

    @RequestMapping(value = "/try", method = RequestMethod.POST)
    public void validate(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Validating captcha response");
        Enumeration<String> keys = request.getParameterNames();
        CaptchaData captchaData = new CaptchaData();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            if (key.startsWith("submit")) {
                continue;
            }
            captchaData.setName(key);
            captchaData.setValue(request.getParameter(key));
            // captchaData.put(key, request.getParameter(key));
        }
        CaptchaValidationResult result = captchaSession.validate(captchaData);
        String status = null;
        switch (result) {
            case FAILED_IMAGE: status= "failedImage";break;
            case VALID_IMAGE: status= "validImage";break;
            case FAILED_AUDIO: status= "failedAudio";break;
            case VALID_AUDIO: status= "validAudio";break;
            default: status="otherError";
        }
        response.setStatus(HttpStatus.FOUND.value());
        response.setHeader("Location", "/?status=" + status);
        captchaSession.invalidate();
    }

    private void writeResponse(MediaType contentType, InputStream input, HttpServletResponse response) {
        OutputStream output = null;
        byte[] buffer = new byte[10240];
        try {
            response.setContentType(contentType.toString());
            output = response.getOutputStream();
            for (int length = 0; (length = input.read(buffer)) > 0; ) {
                output.write(buffer, 0, length);
            }
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException("Cannot load resource", e);
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (IOException ignore) {
            }
            try {
                input.close();
            } catch (IOException ignore) {
            }
        }

    }

}
