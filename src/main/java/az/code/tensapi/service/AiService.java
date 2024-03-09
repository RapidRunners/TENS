package az.code.tensapi.service;

import az.code.tensapi.dto.AIRequestDto;
import az.code.tensapi.dto.AIResponseDto;
import az.code.tensapi.dto.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiService {

    private final RestTemplate template;

    @Value("${openai.model}")
    private String model;

    @Value(("${openai.api.url}"))
    private String apiURL;

    public String chat(String role, String prompt) {
        AIRequestDto request = new AIRequestDto(model);
        request.setPrompt(prompt,role);
        AIResponseDto aiResponse = template.postForObject(apiURL, request, AIResponseDto.class);
        return aiResponse.getChoices().get(0).getMessage().getContent();
    }

    public String summarize(Map<String, String> message) {
        AIRequestDto request = new AIRequestDto(model);
        message.forEach((k, v) -> request.setPrompt(v, k));
        AIResponseDto aiResponse = template.postForObject(apiURL, request, AIResponseDto.class);
        return aiResponse.getChoices().get(0).getMessage().getContent();
    }


}


