package com.kuhniverse.it;

import com.kuhniverse.domain.CaptchaAnswer;
import com.kuhniverse.integration.CaptchaRepository;
import org.junit.Assert;
import org.junit.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tillkuhn on 15.06.2015.
 */
public class I18NResourceBuilder extends BaseIntegrationTest {


    @Inject
    private CaptchaRepository captchaRepository;


    // Only activate to create resource bundle
    @Test
    public void createResourceBundle() {
        List<CaptchaAnswer> options = new ArrayList<CaptchaAnswer>(captchaRepository.getImages());
        StringBuilder resource = new StringBuilder();
        for (CaptchaAnswer option : options) {
            String normalizedName = option.getPath().substring(0, option.getPath().indexOf("."));
            Assert.assertNotNull(normalizedName);
            resource.append( "\""+ normalizedName + "\": \"" + option.getName() + "\",\n");
        }
        // System.out.println(resource);

    }
}
