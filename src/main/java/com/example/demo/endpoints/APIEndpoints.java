package com.example.demo.endpoints; // match your actual package name

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class APIEndpoints {

  @GetMapping("/hello")
  public String hello() {
    return "Hello, world!";
  }
}