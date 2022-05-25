package top.liguapi.api.service;

import top.liguapi.api.entity.dto.SearchDTO;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/25 21:15
 */
public interface SearchService {
    SearchDTO searchVideo(String q, Integer page, Integer size);
}
