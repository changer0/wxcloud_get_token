package com.tencent.wxcloudrun.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.CounterRequest;
import com.tencent.wxcloudrun.model.Counter;
import com.tencent.wxcloudrun.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import org.springframework.web.client.RestTemplate;
/**
 * counter控制器
 */
@RestController

public class CounterController {

  final CounterService counterService;
  final Logger logger;
  private final RestTemplate restTemplate;

  public CounterController(@Autowired CounterService counterService, RestTemplate restTemplate) {
    this.counterService = counterService;
    this.restTemplate = restTemplate;
    this.logger = LoggerFactory.getLogger(CounterController.class);
  }


  /**
   * 获取当前计数
   * @return API response json
   */
  @GetMapping(value = "/api/count")
  ApiResponse get() {
    logger.info("/api/count get request");
    Optional<Counter> counter = counterService.getCounter(1);
    Integer count = 0;
    if (counter.isPresent()) {
      count = counter.get().getCount();
    }

    return ApiResponse.ok(count);
  }


  /**
   * 更新计数，自增或者清零
   * @param request {@link CounterRequest}
   * @return API response json
   */
  @PostMapping(value = "/api/count")
  ApiResponse create(@RequestBody CounterRequest request) {
    logger.info("/api/count post request, action: {}", request.getAction());

    Optional<Counter> curCounter = counterService.getCounter(1);
    if (request.getAction().equals("inc")) {
      Integer count = 1;
      if (curCounter.isPresent()) {
        count += curCounter.get().getCount();
      }
      Counter counter = new Counter();
      counter.setId(1);
      counter.setCount(count);
      counterService.upsertCount(counter);
      return ApiResponse.ok(count);
    } else if (request.getAction().equals("clear")) {
      if (!curCounter.isPresent()) {
        return ApiResponse.ok(0);
      }
      counterService.clearCount(1);
      return ApiResponse.ok(0);
    } else {
      return ApiResponse.error("参数action错误");
    }
  }

  /**
   * 获取微信 access_token
   * @param appId 微信应用的 appid
   * @param appSecret 微信应用的 appsecret
   * @return API response json 包含 access_token
   */
  @GetMapping(value = "/api/access_token")
  ApiResponse getAccessToken(@RequestParam String appId, @RequestParam String appSecret) {
    logger.info("/api/access_token get request with appId: {} and appSecret: {}", appId, appSecret);
    return ApiResponse.ok("哈哈哈嗝");
    //    String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret=" + appSecret;
//
//    try {
//      ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
//      ObjectMapper objectMapper = new ObjectMapper();
//      JsonNode rootNode = objectMapper.readTree(response.getBody());
//      String accessToken = rootNode.path("access_token").asText();
//      Integer expiresIn = rootNode.path("expires_in").asInt();
//
//      return ApiResponse.ok().putData("access_token", accessToken).putData("expires_in", expiresIn);
//    } catch (Exception e) {
//      logger.error("Failed to get access token", e);
//      return ApiResponse.error("Failed to get access token");
//    }
  }
}