package com.jaydee.School.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jaydee.School.entity.FileStorage;
import com.jaydee.School.entity.FileStorage.FileCategory;

@Repository
public interface FileStorageRepository extends JpaRepository<FileStorage, Long> {
    List<FileStorage> findByUserId(Long userId);
    Optional<FileStorage> findByUserIdAndFileCategory(Long userId, FileCategory category);
} 