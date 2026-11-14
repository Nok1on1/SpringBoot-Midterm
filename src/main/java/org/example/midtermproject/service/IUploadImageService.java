package org.example.midtermproject.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IUploadImageService {

  String store(MultipartFile file);

  Resource load(String fileName);

  void delete(String fileName);
}
