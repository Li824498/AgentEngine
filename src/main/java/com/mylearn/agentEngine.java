package com.mylearn;

import com.mylearn.entity.Task;
import com.mylearn.entity.TaskHttp;
import com.mylearn.exception.AgentEngineException;
import okhttp3.*;
import okhttp3.internal.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class agentEngine {
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 全同步模式下的交互，套壳okhttp3
     * @param requestEntity 封装好的请求体
     * @param url 请求url
     * @return 原始返回数据
     */
    public ResponseEntity<Map> syncTransfer(HttpEntity<?> requestEntity, String url){
        ResponseEntity<Map> response = null;
        try {
            response = restTemplate.exchange(url, org.springframework.http.HttpMethod.POST, requestEntity, Map.class);
            if (response.getStatusCode().isError()) {
                throw new AgentEngineException("模型调用失败，状态码：" + response.getStatusCode());
            }
        } catch (RestClientException e) {
            throw new AgentEngineException("调用AI服务异常", e);
        }
        return response;
    }


    BlockingQueue<Task> rawTaskQueue = new ArrayBlockingQueue<>(100);
    BlockingQueue<Task> ripeTaskQueue = new ArrayBlockingQueue<>(100);

    private final OkHttpClient okHttpClient = new OkHttpClient();


    public void startWork() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Task task = rawTaskQueue.poll();

                    ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
                    executorService.submit(new Runnable() {
                        @Override
                        public void run() {
                            asyncWork(task);
                        }
                    });
                }
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                Task task = ripeTaskQueue.poll();

                ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        asyncResult(task);
                    }
                });
            }
        }).start();
    }

    // stage1:接受消息方法

    public void asyncTransferToHttp(HttpEntity<?> requestEntity, String url, String resultUrl) {
        TaskHttp task = new TaskHttp();
        task.setUrl(url);
        task.setRequestEntity(requestEntity);
        task.setResultUrl(resultUrl);

        rawTaskQueue.add(task);
    }

    // todo
    public void asyncTransferToRPC(HttpEntity<?> requestEntity, String url) {

    }

    // todo
    public void asyncTransferToMysql(HttpEntity<?> requestEntity, String url) {

    }

    // todo
    public void asyncTransferToRedis(HttpEntity<?> requestEntity, String url) {

    }

    // stage2:工作方法:大模型调用方法

    /**
     * 工作方法，大模型调用方法
     */
    public void asyncWork(Task task) {
        ResponseEntity<Map> responseEntity = syncTransfer(task.getRequestEntity(), task.getUrl());
        task.setResponseEntity(responseEntity);

        ripeTaskQueue.add(task);
    }


    // stage3:工作方法:根绝任务类型分发

    public void asyncResult(Task task) {
        task.asyncResult();
    }
}
