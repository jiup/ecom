/*
 * Copyright 2017 Jiupeng Zhang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.codeager.ecom.util;

import com.captcha.botdetect.*;
import com.captcha.botdetect.web.servlet.Captcha;
import com.captcha.botdetect.web.servlet.CaptchaServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRequest;
import java.util.ArrayList;

import static io.codeager.ecom.util.Routing.API_BASE;

/**
 * @author Jiupeng Zhang
 * @since 12/21/2017
 */
public class BotDetectUtils {
    public static final String SERVLET_PATH = API_BASE + "/captcha";
    public static final int CAPTCHA_VALID_IN_SECONDS = 300; // 5 minutes
    public static final int REPEATED_REQUESTS_ALLOWED_COUNT = 10;
    public static final int DEFAULT_IMAGE_WIDTH = 100;
    public static final int DEFAULT_IMAGE_HEIGHT = 50;
    public static final int DEFAULT_CODE_LENGTH_MIN = 3;
    public static final int DEFAULT_CODE_LENGTH_MAX = 5;

    public static void join(ServletContext servletContext) {
        ServletRegistration.Dynamic captchaServlet = servletContext.addServlet("BotDetectCaptcha", CaptchaServlet.class);
        captchaServlet.setLoadOnStartup(1);
        captchaServlet.addMapping(SERVLET_PATH);
        servletContext.setInitParameter("BDC_servletRequestPath", SERVLET_PATH);
        servletContext.setInitParameter("BDC_requestFilterRepeatedRequestsAllowed", String.valueOf(REPEATED_REQUESTS_ALLOWED_COUNT));
    }

    public static boolean isValid(ServletRequest request, String captchaId, String userInput) {
        Captcha captcha = Captcha.load(request, captchaId);
        return captcha != null && captcha.validate(userInput);
    }

    public static Captcha load(ServletRequest request, String captchaId) {
        return load(request, captchaId, DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);
    }

    public static Captcha load(ServletRequest request, String captchaId, int imageWidth, int imageHeight) {
        Captcha captcha = Captcha.load(request, captchaId);
        fillPreferSettings(captcha, imageWidth, imageHeight);
        return captcha;
    }

    public static String getCustomizedHtml(ServletRequest request, String captchaId) {
        return getCustomizedHtml(load(request, captchaId));
    }

    public static String getCustomizedHtml(ServletRequest request, String captchaId, int imageWidth, int imageHeight) {
        if (imageWidth < 20 || imageWidth > 500)
            imageWidth = BotDetectUtils.DEFAULT_IMAGE_WIDTH;

        if (imageHeight < 10 || imageHeight > 190)
            imageHeight = BotDetectUtils.DEFAULT_IMAGE_HEIGHT;

        return getCustomizedHtml(load(request, captchaId, imageWidth, imageHeight));
    }

    public static String getCustomizedHtml(Captcha captcha) {
        return new StringBuilder().append("<div id='").append(captcha.getCaptchaId())
                .append("_CaptchaField' imageWidth='").append(captcha.getImageSize().getWidth())
                .append("' imageHeight='").append(captcha.getImageSize().getHeight() - 10)
                .append("'>").append(captcha.getHtml()).append("</div>").toString()
                .replaceAll("Retype the CAPTCHA code from the image", "Please refresh this page")
                .replaceAll("Change the CAPTCHA code", "");
    }

    @Deprecated
    public static String getCustomizedScript(Captcha captcha) {
        String captchaId = captcha.getCaptchaId();
        int imageWidth = captcha.getImageSize().getWidth();
        int imageHeight = captcha.getImageSize().getHeight() - 10;

        StringBuilder builder = new StringBuilder();
        builder.append("var captchaDiv=document.getElementById('").append(captchaId).append("_CaptchaDiv');");
        builder.append("var captchaImageDiv=document.getElementById('").append(captchaId).append("_CaptchaImageDiv');");
        builder.append("var captchaIconsDiv=document.getElementById('").append(captchaId).append("_CaptchaIconsDiv');");
        builder.append("var captchaReloadLink=document.getElementById('").append(captchaId).append("_ReloadLink');");
        builder.append("captchaDiv.style.width='").append(imageWidth).append("px';");
        builder.append("captchaDiv.style.height='").append(imageHeight).append("px';");
        builder.append("captchaImageDiv.style.cursor='pointer';");
        builder.append("captchaImageDiv.style.height='").append(imageHeight).append("px';");
        builder.append("captchaImageDiv.removeChild(captchaImageDiv.children[1]);");
        builder.append("captchaIconsDiv.style.display='none';");
        builder.append("captchaImageDiv.onclick=function(){captchaReloadLink.click();}");
        return builder.toString();
    }

    private static void fillPreferSettings(Captcha captcha, int imageWidth, int imageHeight) {
        captcha.setCodeLength(CaptchaRandomization.getRandomCodeLength(DEFAULT_CODE_LENGTH_MIN, DEFAULT_CODE_LENGTH_MAX));
        captcha.setCodeStyle(CodeStyle.ALPHANUMERIC);
        captcha.setCodeTimeout(CAPTCHA_VALID_IN_SECONDS);
        captcha.setDisallowedCodeSubstringsCsv("MM,NN,OO,OC,00,0C,C0,CO,b6,6b,B8,8B,UJ,UW,WU,UV,VU,VW,WV,NV,VN");
        captcha.setImageStyle(new ArrayList<ImageStyle>() {{
            this.add(ImageStyle.SPIDER_WEB2);
            this.add(ImageStyle.NEGATIVE);
            this.add(ImageStyle.OVERLAP);
        }});
        captcha.setImageSize(new ImageSize(imageWidth, imageHeight + 10));
        captcha.setImageFormat(ImageFormat.PNG);
        captcha.setReloadEnabled(true);
        captcha.setSoundEnabled(false);
        captcha.setUseHorizontalIcons(false);
        captcha.setSoundIconUrl("");
        captcha.setReloadIconUrl("");
        captcha.setUseSmallIcons(true);
        captcha.setHelpLinkText("Powered by BotDetect&#8482;");
        captcha.setHelpLinkMode(HelpLinkMode.TEXT);
        captcha.setTabIndex(-1);
        captcha.setAddCssInclude(false);
        captcha.setAddScriptInclude(false);
        captcha.setAutoUppercaseInput(false);
        captcha.setAutoFocusInput(false);
        captcha.setAutoClearInput(false);
        captcha.setAutoReloadExpiredCaptchas(true);
    }
}
