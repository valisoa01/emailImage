package com.example.demo.endpoint.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.emailimage.EmailImage;
import com.example.demo.emailimage.EmailImageService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class EmailImageControllerTest {

  @Mock private EmailImageService emailImageService;

  private EmailImageController emailImageController;

  @BeforeEach
  void setUp() {
    emailImageController = new EmailImageController(emailImageService);
  }

  @Test
  void post_sends_the_image_and_returns_201_with_the_created_record() throws Exception {
    var image = new MockMultipartFile("image", "photo.png", "image/png", "content".getBytes());
    var email = "recipient@example.com";
    var expected = new EmailImage(UUID.randomUUID(), "photo.png", email, Instant.now());

    when(emailImageService.sendImageByEmailAndStore(image, email)).thenReturn(expected);

    var response = emailImageController.sendImageByEmail(image, email);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody()).isEqualTo(expected);
    verify(emailImageService).sendImageByEmailAndStore(image, email);
  }

  @Test
  void get_returns_200_with_all_the_records() {
    var records =
        List.of(
            new EmailImage(UUID.randomUUID(), "a.png", "a@example.com", Instant.now()),
            new EmailImage(UUID.randomUUID(), "b.png", "b@example.com", Instant.now()));
    when(emailImageService.findAll()).thenReturn(records);

    var response = emailImageController.getAll();

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo(records);
  }
}
