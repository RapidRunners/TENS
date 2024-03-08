package az.code.tensapi.service;

import az.code.tensapi.dto.AIRequestDto;
import az.code.tensapi.dto.AIResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AiService {

    private final RestTemplate template;

    @Value("${openai.model}")
    private String model;

    @Value(("${openai.api.url}"))
    private String apiURL;

    public String chat(String prompt) {
        AIRequestDto request = new AIRequestDto(model, prompt);
        AIResponseDto chatGptResponse = template.postForObject(apiURL, request, AIResponseDto.class);
        return chatGptResponse.getChoices().get(0).getMessage().getContent();
    }
}
