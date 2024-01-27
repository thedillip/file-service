package com.file.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.file.dto.MediaFile;
import com.file.entity.FileDetailsEntity;

public interface FileService {
	String uploadFile(MultipartFile file);

	List<FileDetailsEntity> getFileDetails();

	MediaFile downloadFile(int fileId);

	String deleteFile(int fileId);

	String updateFile(int fileId, MultipartFile file);
}
