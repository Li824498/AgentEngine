package com.mylearn.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class TaskHttp extends Task{
    String resultUrl;


    public void asyncResult() {
        RestTemplate restTemplate = new RestTemplate();

        try {
            restTemplate.exchange(this.resultUrl, HttpMethod.POST, this.responseEntity, Map.class);
        } catch (RestClientException e) {
//            throw new RuntimeException(e);
            log.error("HttpTask-ERROR:下游服务通知异常");
        }
    }
}
