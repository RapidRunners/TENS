package az.code.tensapi.controller;

import az.code.tensapi.dto.AIRequestDto;
import az.code.tensapi.dto.AIResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/bot")
@RequiredArgsConstructor
public class AiController {

    private final RestTemplate template;

    @Value("${openai.model}")
    private String model;

    @Value(("${openai.api.url}"))
    private String apiURL;


    @GetMapping
    public String chat(@RequestParam("prompt") String prompt) {
        AIRequestDto request = new AIRequestDto(model, prompt);
        AIResponseDto chatGptResponse = template.postForObject(apiURL, request, AIResponseDto.class);
        return chatGptResponse.getChoices().get(0).getMessage().getContent();
    }
}

