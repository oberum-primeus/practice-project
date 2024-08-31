package com.tofu.practice.services;

import com.tofu.practice.models.GreetModel;
import com.tofu.practice.repositories.GreetRepo;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GreetService {

    @Autowired
    private GreetRepo greetRepo;

//    @PostConstruct
//    public void init() {
//        GreetModel greeting = new GreetModel("Hello, World.");
//        greetRepo.save(greeting);
//    }

    public GreetModel personalGreeting(String name) {
        // check if the name is only whitespaces
        if (name.replaceAll("\\s", "").isEmpty()) {
            throw new IllegalArgumentException("No name was provided!");
        }

        // check if the name is already existing
        if (greetRepo.findByName(name) != null) {
            String nameCheck = greetRepo.findByName(name).getName();
            if (name.replaceAll("\\s", "").equalsIgnoreCase(nameCheck)) {
                throw new IllegalArgumentException("Name already exists!");
            }
        }

        // create the greeting
        String message = "Hello, " + name;
        GreetModel greeting = new GreetModel(message, name);
        return greetRepo.save(greeting);
    }

    public List<GreetModel> getAllGreetings() {
        return greetRepo.findAll();
    }

    // method for marking the message as favorite
    public GreetModel markAsFave (Long id, boolean isFave) {
        Optional<GreetModel> greetOptional = greetRepo.findById(id);
        if (greetOptional.isPresent()) {
            GreetModel greet = greetOptional.get();
            greet.setFavorite(isFave);
            return greetRepo.save(greet);
        }
        throw new IllegalArgumentException("Greeting not found with ID: " + id);
    }

    // method for getting only the "favorite"-d greetings
    public Page<GreetModel> getFaveGreetings (int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return greetRepo.findByFavoriteTrue(pageable);
    }

    // method for searching greetings via keyword
    public Page<GreetModel> searchGreetings (String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        try {
            return greetRepo.findByMessageContaining(keyword, pageable);
        } catch (Exception err) {
            throw new IllegalArgumentException("No messages found with keyword: " + keyword);
        }
    }
}
