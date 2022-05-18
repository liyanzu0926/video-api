package top.liguapi.api.entity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/18 14:49
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDTO {
    private Integer id;
    private String name;
    private List<CategoryDTO> children;
}
