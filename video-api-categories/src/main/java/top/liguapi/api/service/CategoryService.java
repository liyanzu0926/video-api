package top.liguapi.api.service;

import top.liguapi.api.entity.dto.CategoryDTO;

import java.util.List;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/18 14:46
 */
public interface CategoryService {
    List<CategoryDTO> categoryList();

    CategoryDTO queryByid(Integer id);

    String queryNameById(Integer id);
}
