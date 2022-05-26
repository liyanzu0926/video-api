package top.liguapi.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.liguapi.api.entity.dto.SearchDTO;
import top.liguapi.api.service.SearchService;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/25 21:14
 */
@RestController
@Slf4j
@RequestMapping("api")
public class SearchController {

    private SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    /**
     * @Description: 视频搜索
     * @author: lww
     * @date: 2022/5/26 22:18
     */
    @GetMapping("search/videos")
    public SearchDTO searchVideo(String q, Integer page, @RequestParam("per_page") Integer size) {
        return searchService.searchVideo(q, page, size);
    }
}
