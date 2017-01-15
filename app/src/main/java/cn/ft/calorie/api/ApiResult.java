package cn.ft.calorie.api;

import java.io.Serializable;

public class ApiResult<T> implements Serializable {
	private String result;
	private T data;

	public ApiResult(String result, T data) {
		super();
		this.result = result;
		this.data = data;
	}
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}

}
