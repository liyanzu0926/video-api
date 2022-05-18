package top.liguapi.api.mapper;

import org.apache.ibatis.annotations.Param;
import top.liguapi.api.entity.dto.CategoryDTO;
import top.liguapi.api.entity.pojo.Category;
import top.liguapi.api.entity.pojo.CategoryExample;

import java.util.List;

public interface CategoryMapper {
    long countByExample(CategoryExample example);

    int deleteByExample(CategoryExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    List<Category> selectByExample(CategoryExample example);

    Category selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Category record, @Param("example") CategoryExample example);

    int updateByExample(@Param("record") Category record, @Param("example") CategoryExample example);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    List<CategoryDTO> categoryList();
}