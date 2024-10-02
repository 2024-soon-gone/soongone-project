package org.example.springbootserver.customOAuth2.controller;
import jakarta.servlet.http.HttpServletResponse;
import org.example.springbootserver.customOAuth2.dto.CustomOAuth2User;
import org.example.springbootserver.customOAuth2.service.oauth2TokenService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class oauth2TokenController {

    private final org.example.springbootserver.customOAuth2.service.oauth2TokenService oauth2TokenService;

    public oauth2TokenController(oauth2TokenService oauth2TokenService) {
        this.oauth2TokenService = oauth2TokenService;
    }

    @GetMapping("/oauth2Verify")
    @ResponseBody
    public String verifyUser(@RequestParam("provider") String provider, @RequestParam("accessToken") String accessToken) {
        HttpServletResponse response = null;
        CustomOAuth2User customOAuth2User = oauth2TokenService.verifyAccessToken(provider, accessToken);
        if (customOAuth2User == null) {
            return "oauth2 verify failed : oAuth2User is null";
        }
        // Return the response body (user info) or handle error
        // Return the response body (user info) or handle error
        try {
            // Call the method to process the successful verification
            oauth2TokenService.onTokenVerificationSuccess(response, customOAuth2User);
            return "OAuth2 verification process completed successfully.";
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("onTokenVerificationSuccess exception: " + e);
            return "OAuth2 verification process failed due to an internal error.";
        }
    }
}
