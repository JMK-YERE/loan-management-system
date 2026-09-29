package com.loanapp.dto;

import java.time.LocalDateTime;

public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public ApiResponse() {}
    public ApiResponse(boolean success, String message, T data, LocalDateTime timestamp) {
        this.success=success; this.message=message; this.data=data; this.timestamp=timestamp;
    }

    public boolean isSuccess(){return success;} public void setSuccess(boolean v){success=v;}
    public String getMessage(){return message;} public void setMessage(String v){message=v;}
    public T getData(){return data;} public void setData(T v){data=v;}
    public LocalDateTime getTimestamp(){return timestamp;} public void setTimestamp(LocalDateTime v){timestamp=v;}

    public static Builder builder(){return new Builder();}
    public static class Builder {
        private boolean success; private String message; private Object data; private LocalDateTime timestamp;
        public Builder success(boolean v){success=v;return this;} public Builder message(String v){message=v;return this;}
        public Builder data(Object v){data=v;return this;} public Builder timestamp(LocalDateTime v){timestamp=v;return this;}
        @SuppressWarnings("unchecked")
        public <T> ApiResponse<T> build(){return new ApiResponse<>(success,message,(T)data,timestamp);}
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder().success(true).message(message).data(data).timestamp(LocalDateTime.now()).build();
    }
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder().success(false).message(message).data(null).timestamp(LocalDateTime.now()).build();
    }
}
