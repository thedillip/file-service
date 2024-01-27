package com.file.response;

public class ApiEntity<T> extends ApiResponseObject {
	
	private T data;
	
	public ApiEntity(String message, T data) {
		super();
		setMessage(message);
		this.data = data;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
}
