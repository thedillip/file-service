package com.file.response;

import java.time.LocalDateTime;

public class ApiResponseObject {
	private LocalDateTime timestamp;
	private String message;

	public ApiResponseObject() {
		super();
		this.timestamp = LocalDateTime.now();
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
