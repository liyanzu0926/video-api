package top.liguapi.api;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/25 21:51
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiSearchApp.class)
public class ESTest {
    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Test
    public void videoTest() {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("video");
        createIndexRequest.mapping("{\n" +
                "      \"properties\": {\n" +
                "        \"title\": {\n" +
                "          \"type\": \"text\",\n" +
                "          \"analyzer\": \"ik_max_word\"\n" +
                "        },\n" +
                "        \"cover\": {\n" +
                "          \"type\": \"keyword\"\n" +
                "        },\n" +
                "        \"likes\": {\n" +
                "          \"type\": \"integer\"\n" +
                "        },\n" +
                "        \"uploader\": {\n" +
                "          \"type\": \"keyword\"\n" +
                "        },\n" +
                "        \"created_at\": {\n" +
                "          \"type\": \"date\"\n" +
                "        }\n" +
                "      }\n" +
                "    }", XContentType.JSON);
        try {
            restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2(){
        Integer a = 1;
        System.out.println(a.toString());
    }
}
