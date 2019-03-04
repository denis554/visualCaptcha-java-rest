package com.kuhniverse.business;

import com.kuhniverse.domain.*;
import com.kuhniverse.integration.CaptchaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.inject.Inject;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static java.util.Collections.shuffle;

/**
 * Main business logic for captcha interaction + holder for session information
 *
 * Based on the work of https://github.com/bdotzour/visualCaptcha-java
 */
@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
public class CaptchaSession implements Serializable {

    private Logger log = LoggerFactory.getLogger(CaptchaSession.class);

    public static int MIN_OPTION_COUNT = 2;

    private Random rand = new Random();
    private CaptchaSessionInfo captchaSessionInfo;

    @Inject
    private CaptchaRepository captchaRepository;

    /**
     * Init captchas with a size of optionCount
     *
     */
    public CaptchaFrontEndData start(int optionCount) {
        validateOptionCount(optionCount);
        String salt = UUID.randomUUID().toString();
        List<CaptchaAnswer> choices = getRandomCaptchaOptions(optionCount, salt);
        CaptchaAnswer validChoice = choices.get(rand.nextInt(optionCount));
        CaptchaAnswer audioOption = getRandomCaptchaAudio(salt);
        String fieldName = hash(UUID.randomUUID().toString(), salt);
        String audioFieldName = hash(UUID.randomUUID().toString(), salt);
        this.captchaSessionInfo = new CaptchaSessionInfo(fieldName, validChoice.getObfuscatedName(), audioFieldName, audioOption, choices);

        List<String> frontEndOptions = new ArrayList<>(choices.size());
        for (CaptchaAnswer choice : choices) {
            frontEndOptions.add(choice.getObfuscatedName());
        }
        CaptchaFrontEndData frontendData = new CaptchaFrontEndData(
                validChoice.getName(), fieldName, frontEndOptions, audioFieldName);

        //resp.setContentType("application/json");
        //resp.getWriter().write(new GsonBuilder().create().toJson(frontendData));
        log.debug("Capture session data field {} initialized with {} values", fieldName, optionCount);
        return frontendData;
    }

    /**
     * see https://github.com/tillkuhn/visualCaptcha-java-rest/issues/3
     * if only 1 or a small amount of options are requested chances are that captcha
     * can be resolved automatically
     */
    private void validateOptionCount(int size) {
        if (size < MIN_OPTION_COUNT) {
            throw new CaptchaValidationException(CaptchaValidationResult.INSUFFICIENT_OPTION_COUNT,"Must request at least " + MIN_OPTION_COUNT + " options");
        }
    }
    /**
     * Get data for a particular image
     */
    public InputStream getImage(int index, boolean retina) {
        if (this.captchaSessionInfo == null) {
            throw new RuntimeException("Captcha not initialized, cannot return image");
        }
        List<CaptchaAnswer> answers = captchaSessionInfo.getChoices();
        if (answers != null && answers.size() > index) {
            CaptchaAnswer ca = answers.get(index);
            log.debug("Fetch Image {}", ca);
            return captchaRepository.getImageStream(ca.getPath());
        } else {
            throw new RuntimeException("Requested image for invalid index: " + index);
        }
    }

    /**
     * Get data for a particular audio file
     */
    public InputStream getAudio(CaptchaRepository.AudioType audioType) {
        if (this.captchaSessionInfo == null) {
            throw new RuntimeException("Captcha not initialized, cannot return audio");
        }
        CaptchaAnswer captchaAnswer = this.captchaSessionInfo.getAudioAnswer();
        if (captchaAnswer == null) {
            throw new RuntimeException("Requested audio answer not found");
        }
        log.debug("Serving audio {}", captchaAnswer);
        return captchaRepository.getAudioStream(captchaAnswer.getPath(), audioType);
    }

    /**
     * Validate Solution
     */
    public CaptchaValidationResult validate(CaptchaData captchaData) {
        // String fieldName, String obfuscatedChoiceName
        if (this.captchaSessionInfo == null) {
            throw new RuntimeException("Captcha not initialized, cannot validate");
        }
        String audioFieldName = captchaSessionInfo.getAudioFieldName();
        String imageFieldName = captchaSessionInfo.getFieldName();
        String receivedName = captchaData.getName();
        if (receivedName == null) {
            log.warn("Invalid response, no name received");
            return CaptchaValidationResult.MISSING_FIELD_NAME;
        } else if (captchaData.getValue() == null) {
                log.warn("Invalid response, no value received");
                return CaptchaValidationResult.MISSING_VALUE;
        } else if (receivedName .equals(audioFieldName)) {
            return validateAudio(captchaData.getValue()) ? CaptchaValidationResult.VALID_AUDIO : CaptchaValidationResult.FAILED_AUDIO;
        } else if (receivedName .equals(imageFieldName)) {
            return validateImage(captchaData.getValue()) ? CaptchaValidationResult.VALID_IMAGE : CaptchaValidationResult.FAILED_IMAGE;
        } else {
            log.warn("Invalid response, neither audio fieldname {} nor image fieldname {} found in params {}", audioFieldName, imageFieldName, captchaData);
            return CaptchaValidationResult.OTHER_ERROR;
        }
    }

    private boolean validateImage(String answer) {
        String expectedAnswer = captchaSessionInfo.getValidChoice();
        if (!captchaSessionInfo.getValidChoice().equals(answer)) {
            log.warn("Invalid response, image choice {} does not match expected answer {}", answer, expectedAnswer);
            return false;
        } else {
            log.debug("Image Captcha successfully verified ({}={})", answer, expectedAnswer);
            return true;
        }
    }

    private boolean validateAudio(String answer) {
        String expectedAnswer = captchaSessionInfo.getAudioAnswer().getName().toLowerCase();
        if (!expectedAnswer.equals(answer.toLowerCase())) {
            log.warn("Invalid response, audio answer {} does not match expected anwer {}", answer, expectedAnswer);
            return false;
        } else {
            log.debug("Audio Captcha successfully verified ({}={})", answer, expectedAnswer);
            return true;
        }
    }


    /**
     * Invalidates current Session Info
     */

    public void invalidate() {
        this.captchaSessionInfo = null;
        log.debug("captchaSessionInfo invalidated");
    }


    private String hash(String somethingToHash, String salt) {
        // return Hashing.md5().hashString((somethingToHash + salt), Charsets.UTF_8).toString();
        String toHash = somethingToHash + salt;
        return DigestUtils.md5DigestAsHex(toHash.getBytes());
    }


    private List<CaptchaAnswer> getRandomCaptchaOptions(int numberOfChoices, String salt) {
        List<CaptchaAnswer> options = new ArrayList<CaptchaAnswer>(captchaRepository.getImages());
        shuffle(options);
        List<CaptchaAnswer> choices = new ArrayList<>(numberOfChoices);
        for (CaptchaAnswer answer : options.subList(0, numberOfChoices)) {
            choices.add(new CaptchaAnswer(answer.getName(), hash(answer.getName(), salt), answer.getPath()));
        }
        shuffle(choices);
        return choices;
    }

    private CaptchaAnswer getRandomCaptchaAudio(String salt) {
        //List<CaptchaAnswer> options = new ArrayList<CaptchaAnswer>(captchaRepository.getAudios());
        shuffle(captchaRepository.getAudios());
        return captchaRepository.getAudios().get(rand.nextInt(captchaRepository.getAudios().size()));
    }

}