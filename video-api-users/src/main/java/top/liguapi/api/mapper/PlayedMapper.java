package top.liguapi.api.mapper;

import org.apache.ibatis.annotations.Param;
import top.liguapi.api.entity.pojo.Played;
import top.liguapi.api.entity.pojo.PlayedExample;

import java.util.List;

public interface PlayedMapper {
    long countByExample(PlayedExample example);

    int deleteByExample(PlayedExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Played record);

    int insertSelective(Played record);

    List<Played> selectByExample(PlayedExample example);

    Played selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Played record, @Param("example") PlayedExample example);

    int updateByExample(@Param("record") Played record, @Param("example") PlayedExample example);

    int updateByPrimaryKeySelective(Played record);

    int updateByPrimaryKey(Played record);
}