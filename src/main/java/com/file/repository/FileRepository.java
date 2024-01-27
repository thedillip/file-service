package com.file.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.file.entity.FileDetailsEntity;

public interface FileRepository extends JpaRepository<FileDetailsEntity, Integer> {

}
