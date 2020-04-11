package com.galaxy.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.galaxy.course.entity.EduChapter;
import com.galaxy.course.entity.EduVideo;
import com.galaxy.course.mapper.EduChapterMapper;
import com.galaxy.common.enums.ResultEnum;
import com.galaxy.common.exception.MyException;
import com.galaxy.course.entity.dto.ChapterDto;
import com.galaxy.course.entity.dto.VideoDto;
import com.galaxy.course.mapper.EduVideoMapper;
import com.galaxy.course.service.EduChapterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程章节 服务实现类
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-01-16
 */
@Service
@Slf4j
public class EduChapterServiceImpl  implements EduChapterService {

    @Autowired
    private EduChapterMapper chapterMapper;

    @Autowired
    private EduVideoMapper videoMapper;



    @Override
    public Map<String, List<ChapterDto>> queryChapter(String courseId) {
        // 结果
        HashMap<String, List<ChapterDto> >result = new HashMap<>();

        // 查询章节
        QueryWrapper<EduChapter> chatperWrapper = new QueryWrapper<>();
        chatperWrapper.eq("course_id", courseId); // where course_id = #{courseId}
        chatperWrapper.orderByAsc("sort");
        List<EduChapter> chapters = chapterMapper.selectList(chatperWrapper);
         if (CollectionUtils.isEmpty(chapters)) {
             log.info("此课程没有章节  ,课程id :[{}]",courseId);
             return result;
         }
        ArrayList<ChapterDto> chapterDtoArrayList = new ArrayList<>();
        // 查小节
         chapters.forEach(chapter -> {
             ArrayList<VideoDto> videoDtoArrayList = new ArrayList<>();
             ChapterDto chapterDto = new ChapterDto();
             BeanUtils.copyProperties(chapter,chapterDto);
             List<EduVideo> eduVideos = videoMapper.queryListForVideo(chapterDto.getId());
                            eduVideos.forEach(eduVideo -> {
                                VideoDto videoDto = new VideoDto();
                                BeanUtils.copyProperties(eduVideo,videoDto);
                                videoDtoArrayList.add(videoDto);
                            });
                            chapterDto.setChildren(videoDtoArrayList);
                        chapterDtoArrayList.add(chapterDto);
         });

        result.put("all",chapterDtoArrayList);
        return result;





    }

    @Override
    public Boolean createChapter(EduChapter chapter) {
        int insert = chapterMapper.insert(chapter);
        return insert >0;
    }

    @Override
    public HashMap<String, Object> selectOne(String id) {
        HashMap<String, Object> map = new HashMap<>();
        EduChapter eduChapter = chapterMapper.selectById(id);
        if (eduChapter==null) {
             map.put("result","没有此章节");
             return map;
         }
         map.put("result",eduChapter);
        log.info("查询章节成功! ,id: {}",id);
        return map;
    }

    @Override
    public Integer updateChaptet(ChapterDto chapter) {
        EduChapter eduChapter = new EduChapter();
        BeanUtils.copyProperties(chapter,eduChapter);
        Integer update = chapterMapper.updateById(eduChapter);
        if (update == null) {
            throw new MyException(ResultEnum.QUERY_ERROR);
        }

        return update;
    }

    @Override
    public Boolean deleteChapter(String chapterId) {
         //是否存在视频小节
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id",chapterId);
        Integer count = videoMapper.selectCount(wrapper);
        if (count > 0) {
            throw new MyException(ResultEnum.ERROR_CHAPTER_DELETE);
        }
        // 删除章节
        int i = chapterMapper.deleteById(chapterId);

        return i >0;
    }


}
