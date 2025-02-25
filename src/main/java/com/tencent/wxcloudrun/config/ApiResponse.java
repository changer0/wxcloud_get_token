package com.tencent.wxcloudrun.config;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public final class ApiResponse {

  private Integer code;
  private String errorMsg;
  private Map<String, Object> data;

  private ApiResponse(int code, String errorMsg, Map<String, Object> data) {
    this.code = code;
    this.errorMsg = errorMsg;
    this.data = data;
  }

  public static ApiResponse ok() {
    return new ApiResponse(0, "", new HashMap<>());
  }

  public static ApiResponse ok(Object data) {
    Map<String, Object> map = new HashMap<>();
    map.put("result", data);
    return new ApiResponse(0, "", map);
  }

  public static ApiResponse error(String errorMsg) {
    return new ApiResponse(1, errorMsg, new HashMap<>());
  }

  // 新增 putData 方法
  public ApiResponse putData(String key, Object value) {
    if (this.data == null) {
      this.data = new HashMap<>();
    }
    this.data.put(key, value);
    return this;
  }
}
