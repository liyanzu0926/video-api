package top.liguapi.api.entity.dto;

import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/25 21:12
 */
@Data
public class SearchDTO {

    private Long total_count;

    private List<VideoDTO> items;
}
