package com.supho.model;

import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.supho.utils.EncryptionUtils;

import java.util.Objects;

public class EncodedResponse {
	
	private int status;
	private String message;
	private String data;
	
	public EncodedResponse() {}

	public EncodedResponse(int status, String message, String data) {
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public static EncodedResponse parse(String json) {
		Gson gson = new GsonBuilder().create();
		EncodedResponse response = gson.fromJson(json.trim(), EncodedResponse.class);
		return response;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	public String getDecodedData() {
		byte[] decodeFromBase64 = Base64.decode(data, Base64.DEFAULT);
		Log.d("EncodedResponse" , "response decode : " + Objects.requireNonNull(EncryptionUtils.decryptionDataBlowfish(decodeFromBase64)).trim());
		return Objects.requireNonNull(EncryptionUtils.decryptionDataBlowfish(decodeFromBase64)).trim();
	}

}
