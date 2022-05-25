package top.liguapi.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.liguapi.api.entity.dto.CategoryDTO;
import top.liguapi.api.entity.pojo.Category;
import top.liguapi.api.mapper.CategoryMapper;

import java.util.List;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/18 14:47
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryMapper categoryMapper;

    @Autowired
    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<CategoryDTO> categoryList() {
        return categoryMapper.categoryList();
    }

    @Override
    public CategoryDTO queryByid(Integer id) {
        Category category = categoryMapper.selectByPrimaryKey(id);
        CategoryDTO categoryDTO = new CategoryDTO();
        BeanUtils.copyProperties(category, categoryDTO);
        return categoryDTO;
    }

    @Override
    public String queryNameById(Integer id) {
//        int i = 1 / 0;
        Category category = categoryMapper.selectByPrimaryKey(id);
        return category.getName();
    }
}
