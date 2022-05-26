package top.liguapi.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.liguapi.api.entity.dto.CategoryDTO;
import top.liguapi.api.service.CategoryService;

import java.util.List;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/18 14:45
 */
@RestController
@RequestMapping("api")
@Slf4j
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * @Description: 查询类别列表
     * @author: lww
     * @date: 2022/5/26 22:21
     */
    @GetMapping("categories")
    public List<CategoryDTO> categoryList(){
        return categoryService.categoryList();
    }

    /**
     * @Description: 根据类别id，查询类别信息
     * @author: lww
     * @date: 2022/5/26 22:21
     */
    @GetMapping("categories/{id}")
    public CategoryDTO queryByid(@PathVariable("id") Integer id){
        return categoryService.queryByid(id);
    }

    /**
     * @Description: 根据类别id，查询类别名
     * @author: lww
     * @date: 2022/5/26 22:22
     */
    @GetMapping("categories/name/{id}")
    public String queryNameById(@PathVariable("id") Integer id){
        return categoryService.queryNameById(id);
    }
}
