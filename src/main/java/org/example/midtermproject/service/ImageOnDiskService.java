package org.example.midtermproject.service;

import jakarta.annotation.Nonnull;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageOnDiskService implements IUploadImageService {

  private static final Map<String, String> ALLOWED_TYPES = Map.of(
      "image/jpeg", ".jpg",
      "image/png", ".png"
  );

  private static final String allowedExts = String.join(", ", ALLOWED_TYPES.values());

  private final Path uploadDir;

  public ImageOnDiskService(@Value("${app.upload.dir}") String dir) throws IOException {
    uploadDir = Paths.get(dir).toAbsolutePath().normalize();
    Files.createDirectories(this.uploadDir);
  }

  @Override
  public String store(@Nonnull MultipartFile file) {
    if (file.isEmpty()) {
      throw new IllegalArgumentException("File is Empty");
    }
    String extension = ALLOWED_TYPES.get(file.getContentType());
    if (extension == null) {

      throw new IllegalArgumentException("Allowed Extensions Are: " + allowedExts);
    }

    String fileName = UUID.randomUUID() + extension;

    try {
      Files.copy(file.getInputStream(), uploadDir.resolve(fileName).normalize(),
          StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      throw new RuntimeException("Could not store file" + e);
    }
    return fileName;
  }

  @Override
  public Resource load(String fileName) {
    Path file = uploadDir.resolve(fileName).normalize();
    if (!file.startsWith(uploadDir)) {
      throw new IllegalArgumentException("Invalid file name");
    }
    Resource resource = null;
    try {
      resource = new UrlResource(file.toUri());

      if (!resource.exists()) {
        throw new FileNotFoundException("File not found: " + fileName);
      }
    } catch (FileNotFoundException | MalformedURLException e) {
      throw new RuntimeException(e);
    }

    return resource;
  }

  @Override
  public void delete(String fileName) {
    if (fileName == null) {
      return;
    }
    try {
      Files.deleteIfExists(uploadDir.resolve(fileName).normalize());
    } catch (IOException ignored) {
    }
  }
}
