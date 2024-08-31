package com.tofu.practice.controllers;

import com.tofu.practice.models.GreetModel;
import com.tofu.practice.services.GreetService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/greetings")
public class GreetController {

    @Autowired
    private GreetService greetService;

    @GetMapping
    public ResponseEntity<List<GreetModel>> getGreetings (
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<GreetModel> greetings = greetService.getAllGreetings();
        return ResponseEntity.ok(greetings);
    }

    // creates a personalized greeting
    @PostMapping
    public ResponseEntity<String> createPersonalizedGreeting (@RequestParam String name) {
        try {
            GreetModel greeting = greetService.personalGreeting(name);
            return ResponseEntity.ok(greeting.getMessage());
        } catch (Exception err) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occured: " + err.getMessage());
        }
    }

    // adds/removes a message as a favorite
    @PatchMapping("/{id}/favorite")
    public ResponseEntity<GreetModel> markAsFave (
            @PathVariable Long id,
            @RequestParam boolean favorite) {
        GreetModel updatedGreet = greetService.markAsFave(id, favorite);
        return ResponseEntity.ok(updatedGreet);
    }

    // get all favorite greetings
    @GetMapping("/favorites")
    public ResponseEntity<Page<GreetModel>> getFaveGreetings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<GreetModel> faveGreetings = greetService.getFaveGreetings(page, size);
        return ResponseEntity.ok(faveGreetings);
    }

    // search a greeting by keyword
    @GetMapping("/search")
    public ResponseEntity<Page<GreetModel>> searchGreetings(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<GreetModel> foundGreetings = greetService.searchGreetings(keyword, page, size);
        return ResponseEntity.ok(foundGreetings);
    }
}
