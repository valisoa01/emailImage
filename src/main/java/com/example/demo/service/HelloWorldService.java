package com.example.demo.service;

import com.example.demo.file.bucket.BucketComponent;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;

import static java.io.File.createTempFile;

@Service
@AllArgsConstructor
public class HelloWorldService {
  private final BucketComponent bucketComponent;

  @SneakyThrows
  public String uploadHelloWorldMessage(String name) {
    var fileSuffix = ".txt";
    var filePrefix = "hello-world-" + name;
    var bucketKey = filePrefix + fileSuffix;
    var fileToUpload = createTempFile(filePrefix, fileSuffix);
    writeMessageIntoFile("Hello World " + name + " !", fileToUpload);
    bucketComponent.upload(fileToUpload, bucketKey);
    return bucketComponent.presign(bucketKey, Duration.ofMinutes(5)).toString();
  }

  private void writeMessageIntoFile(String message, File file) throws IOException {
    FileWriter writer = new FileWriter(file);
    writer.write(message);
    writer.close();
  }
}
