package com.tofu.practice.repositories;

import com.tofu.practice.models.GreetModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GreetRepo extends JpaRepository<GreetModel, Long> {

    // method to find all favorite greetings with pagination
    Page<GreetModel> findByFavoriteTrue (Pageable pageable);

    // method to search greetings by message containing a keyword
    Page<GreetModel> findByMessageContaining (String keyword, Pageable pageable);

    // method to find by name
    GreetModel findByName (String name);
}
