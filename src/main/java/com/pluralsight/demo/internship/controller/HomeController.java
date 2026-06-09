package com.pluralsight.demo.internship.controller;

import com.pluralsight.demo.internship.model.Internship;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("home/")
@CrossOrigin(origins = "*") // Allow frontend to connect

public class HomeController {

        @GetMapping
        public ResponseEntity<String> getHome() {
            return ResponseEntity.ok("Hello World");
        }


    @GetMapping("/{name}")
    public ResponseEntity<String> getHomeUnique(@PathVariable String name) {

        return ResponseEntity.ok(name);
    }
}
