package top.liguapi.api.mq;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.liguapi.api.entity.dto.VideoDTO;
import top.liguapi.api.utils.JSONUtils;

import java.io.IOException;

/**
 * @Description rabbitmq消息消费者
 * @Author lww
 * @Date 2022/5/25 21:19
 */
@Component
@Slf4j
public class VideoConsumer {

    private RestHighLevelClient restHighLevelClient;

    @Autowired
    public VideoConsumer(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    /**
     * @Description: 接收消息，并新增es文档
     * @author: lww
     * @date: 2022/5/26 22:20
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue,
            exchange = @Exchange(name = "video", type = "fanout")
    ))
    public void receive(String message) {
        log.info("rabbitmq接收到的信息为：{}", message);
        // 1.json消息转对象
        VideoDTO videoDTO = JSONUtils.readValue(message, VideoDTO.class);

        // 2.创建ES中索引请求对象  参数1:操作索引  参数2:操作类型  参数3:文档id
        IndexRequest indexRequest = new IndexRequest("video", "_doc", videoDTO.getId().toString());

        // 3.设置ES文档的内容
        indexRequest.source(message, XContentType.JSON);
        try {
            // 4.插入索引
            IndexResponse index = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            log.info("video信息插入状态：{}", index.status());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
