package top.liguapi.api.service;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.liguapi.api.entity.dto.SearchDTO;
import top.liguapi.api.entity.dto.VideoDTO;
import top.liguapi.api.utils.JSONUtils;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/25 21:16
 */
@Service
public class SearchServiceImpl implements SearchService {

    private RestHighLevelClient restHighLevelClient;

    @Autowired
    public SearchServiceImpl(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    @Override
    public SearchDTO searchVideo(String q, Integer page, Integer size) {

        SearchDTO searchDTO = new SearchDTO();
        ArrayList<VideoDTO> videoDTOS = new ArrayList<>();

        // 1.计算分页起始
        int start = (page - 1) * size;

        // 2.创建搜索对象
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.from(start).size(size).query(QueryBuilders.termQuery("title", q));

        // 3.设置搜索索引 video  设置搜索类型video  设置搜索条件
        searchRequest.indices("video").types("video").source(searchSourceBuilder);

        SearchResponse search = null;
        try {
            // 4.执行搜索
            search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 5.获取符合条件的总数
        long totalHits = search.getHits().totalHits;
        searchDTO.setTotal_count(totalHits);

        if (totalHits > 0){
            SearchHit[] hits = search.getHits().getHits();
            // 6.遍历符合条件的数组
            for (SearchHit hit : hits) {
                String sourceAsString = hit.getSourceAsString();
                VideoDTO videoDTO = JSONUtils.readValue(sourceAsString, VideoDTO.class);
                videoDTO.setId(Integer.valueOf(hit.getId()));
                videoDTOS.add(videoDTO);
            }
        }
        searchDTO.setItems(videoDTOS);
        return searchDTO;
    }
}
