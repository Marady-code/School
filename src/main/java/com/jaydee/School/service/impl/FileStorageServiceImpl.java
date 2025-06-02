package com.jaydee.School.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.jaydee.School.Exception.ResourceNotFound;
import com.jaydee.School.entity.FileStorage;
import com.jaydee.School.entity.FileStorage.FileCategory;
import com.jaydee.School.entity.User;
import com.jaydee.School.repository.FileStorageRepository;
import com.jaydee.School.repository.UserRepository;
import com.jaydee.School.service.FileStorageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

	private final FileStorageRepository fileStorageRepository;
	private final UserRepository userRepository;

	@Value("${file.upload-dir}")
	private String uploadDir;

	@Override
	@Transactional
	public FileStorage uploadFile(MultipartFile file, Long userId, FileCategory category) throws IOException {
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFound("User", userId));

		String fileName = generateUniqueFileName(file.getOriginalFilename());
		Path filePath = Paths.get(uploadDir, fileName);
		Files.copy(file.getInputStream(), filePath);

		FileStorage fileStorage = new FileStorage();
		fileStorage.setFileName(fileName);
		fileStorage.setFileType(file.getContentType());
		fileStorage.setFileSize(file.getSize());
		fileStorage.setFilePath(filePath.toString());
		fileStorage.setFileCategory(category);
		fileStorage.setUser(user);

		return fileStorageRepository.save(fileStorage);
	}

	@Override
	@Transactional
	public FileStorage uploadProfilePicture(MultipartFile file, Long userId) throws IOException {
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFound("User", userId));

		// Delete existing profile picture if exists
		if (user.getProfilePicture() != null) {
			deleteProfilePicture(userId);
		}

		FileStorage profilePicture = uploadFile(file, userId, FileCategory.PROFILE_PICTURE);
		user.setProfilePicture(profilePicture);
		userRepository.save(user);

		return profilePicture;
	}

	@Override
	@Transactional(readOnly = true)
	public FileStorage getFile(Long fileId) {
		return fileStorageRepository.findById(fileId).orElseThrow(() -> new ResourceNotFound("File", fileId));
	}

	@Override
	@Transactional(readOnly = true)
	public List<FileStorage> getFilesByUser(Long userId) {
		return fileStorageRepository.findByUserId(userId);
	}

	@Override
	@Transactional(readOnly = true)
	public FileStorage getProfilePicture(Long userId) {
		return fileStorageRepository.findByUserIdAndFileCategory(userId, FileCategory.PROFILE_PICTURE).orElse(null);
	}

	@Override
	@Transactional
	public void deleteFile(Long fileId) throws IOException {
		FileStorage file = getFile(fileId);
		Path filePath = Paths.get(file.getFilePath());
		Files.deleteIfExists(filePath);
		fileStorageRepository.delete(file);
	}

	@Override
	@Transactional
	public void deleteProfilePicture(Long userId) throws IOException {
		FileStorage profilePicture = getProfilePicture(userId);
		if (profilePicture != null) {
			deleteFile(profilePicture.getId());
		}
	}

	@Override
	public String getFileUrl(Long fileId) {
		FileStorage file = getFile(fileId);
		return "/api/files/" + file.getId();
	}

	private String generateUniqueFileName(String originalFileName) {
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
		return UUID.randomUUID().toString() + extension;
	}
}