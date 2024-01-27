package com.file.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.file.dto.MediaFile;
import com.file.entity.FileDetailsEntity;
import com.file.response.ApiEntity;
import com.file.response.ApiResponseObject;
import com.file.service.FileService;
import com.file.util.ApplicationConstant;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/file-service")
@RequiredArgsConstructor
public class FileController {

	private final FileService fileService;

	@PostMapping("/upload-file")
	public ResponseEntity<ApiResponseObject> uploadFile(@RequestParam(name = "file") final MultipartFile file) {
		String response = fileService.uploadFile(file);
		HttpStatus httpStatus = HttpStatus.OK;
		HttpHeaders httpHeaders = new HttpHeaders();
		String message = null;
		if (response.equals(ApplicationConstant.SUCCESS))
			message = "File uploaded successfully.";
		else
			message = "Something went wrong.";
		return new ResponseEntity<>(new ApiEntity<>(message, response), httpHeaders, httpStatus);
	}

	@GetMapping(value = "/file-details")
	public ResponseEntity<ApiResponseObject> getFileDetails() {
		List<FileDetailsEntity> response = fileService.getFileDetails();
		HttpStatus httpStatus = null;
		HttpHeaders httpHeaders = new HttpHeaders();
		String message = null;
		if (!response.isEmpty()) {
			httpStatus = HttpStatus.OK;
			message = ApplicationConstant.DATA_FOUND;
		} else {
			httpStatus = HttpStatus.NOT_FOUND;
			message = ApplicationConstant.DATA_NOT_FOUND;
		}
		return new ResponseEntity<>(new ApiEntity<>(message, response), httpHeaders, httpStatus);
	}

	@GetMapping(value = "/download-file/{fileId}")
	public ResponseEntity<?> downloadFile(@PathVariable(name = "fileId") final int fileId) {
		MediaFile mediaFile = fileService.downloadFile(fileId);
		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf(mediaFile.getFileContentType()))
				.body(mediaFile.getByteData());
	}

	@DeleteMapping(value = "/remove-file/{fileId}")
	public ResponseEntity<ApiResponseObject> removeFile(@PathVariable(name = "fileId") final int fileId) {
		String response = fileService.deleteFile(fileId);
		HttpStatus httpStatus = HttpStatus.OK;
		HttpHeaders httpHeaders = new HttpHeaders();
		String message = "File deleted successfully.";
		return new ResponseEntity<>(new ApiEntity<>(message, response), httpHeaders, httpStatus);
	}

	@PutMapping(value = "/update-file/{fileId}")
	public ResponseEntity<ApiResponseObject> updateFile(@PathVariable(name = "fileId") final int fileId,
			@RequestParam(name = "file") final MultipartFile file) {
		String response = fileService.updateFile(fileId, file);
		HttpStatus httpStatus = HttpStatus.OK;
		HttpHeaders httpHeaders = new HttpHeaders();
		String message = "File updated successfully.";
		return new ResponseEntity<>(new ApiEntity<>(message, response), httpHeaders, httpStatus);
	}
}
