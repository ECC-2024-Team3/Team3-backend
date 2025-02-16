package io.github.ecc2024team3.oimarket.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public Map<String, String> home() {
        return Map.of(
            "message", "Oimarket API is running.",
            "postsList", "/api/posts",
            "createPost", "POST /api/posts (body: {title, ...})",
            "apiList", "https://www.notion.so/8802952071a8459b97368a527f4a6623?v=8f1e362488894d3187a4b561868d3d7e"
        );
    }
}
