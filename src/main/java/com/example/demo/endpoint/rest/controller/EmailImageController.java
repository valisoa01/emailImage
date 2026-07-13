package com.example.demo.endpoint.rest.controller;

import com.example.demo.emailimage.EmailImage;
import com.example.demo.emailimage.EmailImageService;
import java.io.IOException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
public class EmailImageController {

  private final EmailImageService emailImageService;

  @PostMapping(value = "/email-images", consumes = "multipart/form-data")
  public ResponseEntity<EmailImage> sendImageByEmail(
      @RequestPart("image") MultipartFile image, @RequestPart("email") String email)
      throws IOException {
    EmailImage saved = emailImageService.sendImageByEmailAndStore(image, email);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
  }

  @GetMapping("/email-images")
  public ResponseEntity<List<EmailImage>> getAll() {
    return ResponseEntity.ok(emailImageService.findAll());
  }
}
