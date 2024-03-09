package az.code.tensapi.dto.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AIRequestDto {

    private String model;
    private List<Message> messages;

    public AIRequestDto(String model) {
        this.model = model;
        this.messages = new ArrayList<>();
    }

    public void setPrompt(String prompt, String role){
        messages.add(new Message(role,prompt));
    }
}
