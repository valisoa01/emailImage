package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.file.bucket.BucketComponent;
import com.example.demo.file.hash.FileHash;
import com.example.demo.file.hash.FileHashAlgorithm;
import java.io.File;
import java.net.URI;
import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HelloWorldServiceTest {

  @Mock private BucketComponent bucketComponent;

  private HelloWorldService helloWorldService;

  @BeforeEach
  void setUp() {
    helloWorldService = new HelloWorldService(bucketComponent);
  }

  @Test
  void uploads_a_file_then_returns_its_presigned_url() throws Exception {
    var presignedUrl = URI.create("https://bucket.example.com/hello-world-John.txt").toURL();

    when(bucketComponent.upload(any(File.class), anyString()))
        .thenReturn(new FileHash(FileHashAlgorithm.SHA256, "checksum"));
    when(bucketComponent.presign(anyString(), any(Duration.class))).thenReturn(presignedUrl);

    String result = helloWorldService.uploadHelloWorldMessage("John");

    assertThat(result).isEqualTo(presignedUrl.toString());
    verify(bucketComponent).upload(any(File.class), anyString());
    verify(bucketComponent).presign(anyString(), any(Duration.class));
  }
}
