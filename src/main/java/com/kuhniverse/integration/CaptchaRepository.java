package com.kuhniverse.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.kuhniverse.domain.CaptchaAnswer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Provides access to captcha resources
 *
 * @author by tillkuhn on 26.05.2015.
 */
@Repository
public class CaptchaRepository {

    public enum AudioType {
        MP3,OGG;
    }

    private static final String RESOURCE_ROOT = "captcha";
    private Logger log = LoggerFactory.getLogger(CaptchaRepository.class);

    private List<CaptchaAnswer> images;
    private List<CaptchaAnswer> audios;
    private ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, CaptchaAnswer.class);
        try {
            this.images = objectMapper.readValue(getResourceAsStream("images.json"), collectionType );
            this.audios= objectMapper.readValue(getResourceAsStream("audios.json"), collectionType );
        } catch(IOException e){
            throw new RuntimeException("Unable to initialize CaptchaServlet. Failed to load resources.", e);
        }
        log.debug("Init CaptchaRepository with {} images and {} audios",images.size(),audios.size());
    }


    public List<CaptchaAnswer> getImages() {
        return images;
    }

    public List<CaptchaAnswer> getAudios() {
        return audios;
    }

    public InputStream getImageStream(String path) {
        return getResourceAsStream("images/" + path);
    }

    public InputStream getAudioStream(String path,AudioType audioType) {
        if ( AudioType.OGG == audioType) {
            path = path.replace(".mp3", ".ogg");
        }
        return getResourceAsStream("audios/" + path);
    }


    InputStream getResourceAsStream(String resource) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(RESOURCE_ROOT + "/" + resource);
    }


}
