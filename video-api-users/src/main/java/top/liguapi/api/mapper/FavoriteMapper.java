package top.liguapi.api.mapper;

import org.apache.ibatis.annotations.Param;
import top.liguapi.api.entity.pojo.Favorite;
import top.liguapi.api.entity.pojo.FavoriteExample;

import java.util.List;

public interface FavoriteMapper {
    long countByExample(FavoriteExample example);

    int deleteByExample(FavoriteExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Favorite record);

    int insertSelective(Favorite record);

    List<Favorite> selectByExample(FavoriteExample example);

    Favorite selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Favorite record, @Param("example") FavoriteExample example);

    int updateByExample(@Param("record") Favorite record, @Param("example") FavoriteExample example);

    int updateByPrimaryKeySelective(Favorite record);

    int updateByPrimaryKey(Favorite record);
}