package com.example.demo.emailimage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.mail.Email;
import com.example.demo.mail.Mailer;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class EmailImageServiceTest {

  @Mock private Mailer mailer;
  @Mock private EmailImageRepository emailImageRepository;

  private EmailImageService emailImageService;

  @BeforeEach
  void setUp() {
    emailImageService = new EmailImageService(mailer, emailImageRepository);
  }

  @Test
  void sends_the_image_as_email_attachment_and_stores_it_in_database() throws IOException {
    var image =
        new MockMultipartFile("image", "photo.png", "image/png", "fake-image-content".getBytes());
    var to = "recipient@example.com";

    when(emailImageRepository.save(any(EmailImage.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    EmailImage result = emailImageService.sendImageByEmailAndStore(image, to);

    assertThat(result.id()).isNotNull();
    assertThat(result.nomFichier()).isEqualTo("photo.png");
    assertThat(result.email()).isEqualTo(to);
    assertThat(result.createdAt()).isNotNull();
    assertThat(result.createdAt()).isBeforeOrEqualTo(Instant.now());

    ArgumentCaptor<Email> emailCaptor = ArgumentCaptor.forClass(Email.class);
    verify(mailer).accept(emailCaptor.capture());
    Email sentEmail = emailCaptor.getValue();
    assertThat(sentEmail.to().getAddress()).isEqualTo(to);
    assertThat(sentEmail.attachments()).hasSize(1);

    ArgumentCaptor<EmailImage> savedCaptor = ArgumentCaptor.forClass(EmailImage.class);
    verify(emailImageRepository).save(savedCaptor.capture());
    assertThat(savedCaptor.getValue().nomFichier()).isEqualTo("photo.png");
    assertThat(savedCaptor.getValue().email()).isEqualTo(to);
  }

  @Test
  void rejects_a_syntactically_invalid_email_address() {
    var image = new MockMultipartFile("image", "photo.png", "image/png", "content".getBytes());
    // Un guillemet non refermé est syntaxiquement invalide selon RFC 822,
    // ce qui garantit une AddressException levée par jakarta.mail.
    var malformedAddress = "\"unterminated-quote";

    org.junit.jupiter.api.Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> emailImageService.sendImageByEmailAndStore(image, malformedAddress));
  }

  @Test
  void findAll_returns_records_from_repository() {
    var expected =
        List.of(
            new EmailImage(UUID.randomUUID(), "a.png", "a@example.com", Instant.now()),
            new EmailImage(UUID.randomUUID(), "b.png", "b@example.com", Instant.now()));
    when(emailImageRepository.findAll()).thenReturn(expected);

    List<EmailImage> result = emailImageService.findAll();

    assertThat(result).isEqualTo(expected);
  }
}
