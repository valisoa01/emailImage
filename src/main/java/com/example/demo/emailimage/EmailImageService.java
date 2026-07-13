package com.example.demo.emailimage;

import com.example.demo.mail.Email;
import com.example.demo.mail.Mailer;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class EmailImageService {

  private final Mailer mailer;
  private final EmailImageRepository emailImageRepository;

  public EmailImage sendImageByEmailAndStore(MultipartFile image, String toEmail)
      throws IOException {
    InternetAddress recipient = toInternetAddress(toEmail);
    File attachment = toTempFile(image);

    try {
      mailer.accept(
          new Email(
              recipient,
              List.of(),
              List.of(),
              ">Your image",
              "<p>Please see the attached image.</p>",
              List.of(attachment)));
    } finally {
      attachment.delete();
    }

    EmailImage emailImage =
        new EmailImage(UUID.randomUUID(), image.getOriginalFilename(), toEmail, Instant.now());
    return emailImageRepository.save(emailImage);
  }

  public List<EmailImage> findAll() {
    return emailImageRepository.findAll();
  }

  private InternetAddress toInternetAddress(String email) {
    try {
      return new InternetAddress(email);
    } catch (AddressException e) {
      throw new IllegalArgumentException("Invalid email address: " + email, e);
    }
  }

  private File toTempFile(MultipartFile image) throws IOException {
    String suffix =
        image.getOriginalFilename() != null && image.getOriginalFilename().contains(".")
            ? image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf('.'))
            : ".png";
    File tempFile = File.createTempFile("email-image-", suffix);
    image.transferTo(tempFile);
    return tempFile;
  }
}
