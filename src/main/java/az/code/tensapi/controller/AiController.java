package az.code.tensapi.controller;

import az.code.tensapi.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bot")
@RequiredArgsConstructor
public class AiController {

    private final AiService service;

    @GetMapping
    public String chat(@RequestParam("prompt") String prompt) {
        return service.chat(prompt);
    }
}

