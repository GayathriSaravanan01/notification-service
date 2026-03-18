package com.notification.service.controller;

import com.notification.service.dto.ChatRequest;
import com.notification.service.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping("/")
    public Map<String, String> home() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Groq Chatbot Running Successfully 🚀");
        return response;
    }

    @PostMapping("/chat")
    public Map<String, String> chat(@RequestBody ChatRequest request) {

        String answer = chatService.getChatResponse(request.getQuestion());

        Map<String, String> response = new HashMap<>();
        response.put("question", request.getQuestion());
        response.put("answer", answer);

        return response;
    }
}