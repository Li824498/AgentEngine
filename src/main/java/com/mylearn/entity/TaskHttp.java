package com.mylearn.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskHttp extends Task{
    String resultUrl;
    // todo 使http路径可运行，现在先暂时停留在mysql阶段


    public void asyncResult() {

    }
}
