package ru.nsu.prokofiev.pharmacy;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FrontendController {

    @RequestMapping(value = { "/" })
    public String forward() {
        return "forward:/index.html";
    }
}
