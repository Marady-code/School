package com.jaydee.School.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.jaydee.School.entity.FileStorage;
import com.jaydee.School.entity.FileStorage.FileCategory;

public interface FileStorageService {
    FileStorage uploadFile(MultipartFile file, Long userId, FileCategory category) throws IOException;
    FileStorage uploadProfilePicture(MultipartFile file, Long userId) throws IOException;
    FileStorage getFile(Long fileId);
    List<FileStorage> getFilesByUser(Long userId);
    FileStorage getProfilePicture(Long userId);
    void deleteFile(Long fileId) throws IOException;
    void deleteProfilePicture(Long userId) throws IOException;
    String getFileUrl(Long fileId);
} 