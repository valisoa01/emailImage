package com.example.demo.endpoint.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.service.HelloWorldService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HelloWorldControllerTest {

  @Mock private HelloWorldService helloWorldService;

  private HelloWorldController helloWorldController;

  @BeforeEach
  void setUp() {
    helloWorldController = new HelloWorldController(helloWorldService);
  }

  @Test
  void delegates_to_the_service_and_returns_its_result() {
    when(helloWorldService.uploadHelloWorldMessage("John")).thenReturn("https://presigned-url");

    String result = helloWorldController.helloWorld("John");

    assertThat(result).isEqualTo("https://presigned-url");
    verify(helloWorldService).uploadHelloWorldMessage("John");
  }
}
