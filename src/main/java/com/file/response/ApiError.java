package com.file.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiError {
	private LocalDateTime timestamp;
	private int status;
	private String error;
	private String errorDescription;
	private String path;
	private boolean success;
}
