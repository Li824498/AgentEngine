package com.mylearn.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    HttpEntity<?> requestEntity;
    String url;
    ResponseEntity<Map> responseEntity;


    public void asyncResult() {}
}
