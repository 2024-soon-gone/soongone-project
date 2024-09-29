package org.example.springbootserver.testRoute;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class testController {
    @GetMapping("/")
    @ResponseBody
    public String test() {
        return "test route home";
    }

    @GetMapping("/private")
    @ResponseBody
    public String test2() {
        return "test route PRIVATE";
    }
}
