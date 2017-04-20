package com.pwcprac.dto;

public class ResultData<T> {
	private boolean result;
	private T data;
	private String msg;

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public ResultData(T data, String msg) {
		this.data = data;
		this.msg = msg;
	}

	public ResultData(String msg) {
		this.msg = msg;
	}

	public ResultData() {

	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
