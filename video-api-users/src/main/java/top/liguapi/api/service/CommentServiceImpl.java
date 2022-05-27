package top.liguapi.api.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.liguapi.api.entity.dto.ItemsDTO;
import top.liguapi.api.entity.pojo.Comment;
import top.liguapi.api.mapper.CommentMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Description
 * @Author lww
 * @Date 2022/5/27 20:55
 */
@Service
public class CommentServiceImpl implements CommentService {

    private CommentMapper commentMapper;

    @Autowired
    public CommentServiceImpl(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    @Override
    public void insert(Comment comment) {
        commentMapper.insertSelective(comment);
    }

    @Override
    public Map<String, Object> getComments(Integer video_id, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<ItemsDTO> itemsDTOS = commentMapper.getCommentsByVideoId(video_id);
        PageInfo<ItemsDTO> itemsDTOPageInfo = new PageInfo<>(itemsDTOS);
        long total = itemsDTOPageInfo.getTotal();
        HashMap<String, Object> res = new HashMap<>();
        res.put("total_count", total);
        res.put("items", itemsDTOS);
        return res;
    }

}
