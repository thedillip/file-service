package com.file.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.file.dto.MediaFile;
import com.file.entity.FileDetailsEntity;
import com.file.exception.ResourceNotFoundException;
import com.file.repository.FileRepository;
import com.file.util.ApplicationConstant;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

	private final FileRepository fileRepository;

	@Value("${file.server.path}")
	private String fileServerPath;

	@Override
	public String uploadFile(final MultipartFile file) {
		String message = null;
		String fileName = file.getOriginalFilename();
		String fileType = file.getContentType();
		long fileSize = file.getSize();
		String filePath = fileServerPath + fileName;
		try {
			file.transferTo(new File(filePath));
			FileDetailsEntity fileDetailsEntity = FileDetailsEntity.builder().fileName(fileName).filePath(filePath)
					.fileType(fileType).fileSize(fileSize).build();
			fileRepository.save(fileDetailsEntity);
			message = ApplicationConstant.SUCCESS;
		} catch (Exception e) {
			log.error("Exception occured insise service layer :: Exception = {}", e);
			message = ApplicationConstant.ERROR;
		}
		return message;
	}

	@Override
	public List<FileDetailsEntity> getFileDetails() {
		return fileRepository.findAll();
	}

	@Override
	public MediaFile downloadFile(final int fileId) {
		FileDetailsEntity fileDetailsEntity = fileRepository.findById(fileId).orElseThrow(
				() -> new ResourceNotFoundException("File details not found with given fileId = " + fileId));
		String filePath = fileDetailsEntity.getFilePath();
		byte[] byteData = null;
		try {
			byteData = Files.readAllBytes(new File(filePath).toPath());
		} catch (IOException e) {
			log.error("Exception occured inside service layer :: IOException = {}", e.toString());
		}
		return MediaFile.builder().fileName(fileDetailsEntity.getFileName())
				.fileContentType(fileDetailsEntity.getFileType()).byteData(byteData).build();
	}

	@Override
	public String deleteFile(final int fileId) {
		String response = ApplicationConstant.ERROR;
		boolean isDelete = Boolean.FALSE;

		FileDetailsEntity fileDetailsEntity = fileRepository.findById(fileId).orElseThrow(
				() -> new ResourceNotFoundException("File details not found with given fileId = " + fileId));
		File file = new File(fileDetailsEntity.getFilePath());
		isDelete = file.delete();

		if (isDelete) {
			fileRepository.deleteById(fileDetailsEntity.getFileId());
			response = ApplicationConstant.SUCCESS;
		}

		response = (isDelete && response.equals(ApplicationConstant.SUCCESS)) ? "File Deleated Successfully"
				: "FAILURE";

		return response;
	}

	@Override
	public String updateFile(final int fileId, final MultipartFile multipartFile) {
		String fileName = multipartFile.getOriginalFilename();
		String fileType = multipartFile.getContentType();
		long fileSize = multipartFile.getSize();

		String filePath = fileServerPath + fileName;
		String response = ApplicationConstant.ERROR;
		boolean isDelete = Boolean.FALSE;

		FileDetailsEntity fileDetailsEntity = fileRepository.findById(fileId).orElseThrow(
				() -> new ResourceNotFoundException("File details not found with given fileId = " + fileId));
		File file = new File(fileDetailsEntity.getFilePath());
		isDelete = file.delete();

		if (isDelete) {
			try {
				multipartFile.transferTo(new File(filePath));
			} catch (IllegalStateException e) {
				log.error("Exception occured inside service layer :: IllegalStateException = {}", e.toString());
			} catch (IOException e) {
				log.error("Exception occured inside service layer :: IOException = {}", e.toString());
			}
			fileDetailsEntity.setFileName(fileName);
			fileDetailsEntity.setFilePath(filePath);
			fileDetailsEntity.setFileType(fileType);
			fileDetailsEntity.setFileSize(fileSize);
			fileRepository.save(fileDetailsEntity);
			response = ApplicationConstant.SUCCESS;
		}

		response = (isDelete && response.equals(ApplicationConstant.SUCCESS)) ? "File Updated Successfully" : "FAILURE";
		return response;
	}
}
