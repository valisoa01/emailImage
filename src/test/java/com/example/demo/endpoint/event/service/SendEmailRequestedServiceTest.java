package com.example.demo.endpoint.event.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import com.example.demo.endpoint.event.model.SendEmailRequested;
import com.example.demo.mail.Email;
import com.example.demo.mail.Mailer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SendEmailRequestedServiceTest {

  @Mock private Mailer mailer;

  private SendEmailRequestedService service;

  @BeforeEach
  void setUp() {
    service = new SendEmailRequestedService(mailer);
  }

  @Test
  void sends_an_email_to_the_requested_recipient() {
    var event = new SendEmailRequested();
    event.setTo("recipient@example.com");

    service.accept(event);

    ArgumentCaptor<Email> captor = ArgumentCaptor.forClass(Email.class);
    verify(mailer).accept(captor.capture());
    assertThat(captor.getValue().to().getAddress()).isEqualTo("recipient@example.com");
  }
}
