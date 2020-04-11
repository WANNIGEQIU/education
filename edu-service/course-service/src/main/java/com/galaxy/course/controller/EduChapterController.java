package com.galaxy.course.controller;



import com.galaxy.common.enums.ResultEnum;
import com.galaxy.common.util.ResultCommon;
import com.galaxy.course.entity.EduChapter;
import com.galaxy.course.entity.dto.ChapterDto;
import com.galaxy.course.service.EduChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程章节 前端控制器
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-01-16
 */
@RestController
@RequestMapping("/chapter")
public class EduChapterController {
    @Autowired
    private EduChapterService chapterService;

    /**
     * 查询所有章节和下面所有小节id
     * @param courseId
     * @return
     */
    @GetMapping("/query/{id}")
    public ResultCommon quaryChapter(@PathVariable("id") String courseId) {
        Map<String, List<ChapterDto>> map = chapterService.queryChapter(courseId);

        return ResultCommon.resultOk(map);
    }

    /**
     *  添加章节
      */
    @PostMapping("/create")
    public ResultCommon createChapter(@RequestBody EduChapter chapter) {
        Boolean r = chapterService.createChapter(chapter);
        if (r) {
            return ResultCommon.resultOk();
        }else {
            return ResultCommon.resultFail();
        }
    }


    /**
     * 根据章节id 只查询章节
     * @param id
     * @return
     */
    @GetMapping("one/{id}")
    public ResultCommon selectOne(@PathVariable("id") String id) {
        HashMap<String, Object> one = chapterService.selectOne(id);
         if (one.size() == 0) {
             return ResultCommon.resultFail().codeAndMsg(ResultEnum.EXCEPTION);
         }else {
             return ResultCommon.resultOk(one);
         }

    }

    /**
     * 更新章节
     * @param chapter
     * @return
     */
    @PutMapping("/update")
    public ResultCommon updateChapter(@RequestBody ChapterDto chapter) {
        Integer integer = chapterService.updateChaptet(chapter);
        return ResultCommon.resultOk(integer);

    }

    /**
     * 删除章节
     */
    @DeleteMapping("/delete/{id}")
    public ResultCommon deleteChapter(@PathVariable("id") String chapterId) {
        Boolean r = chapterService.deleteChapter(chapterId);
        if (r) {
            return ResultCommon.resultOk();
        }else {
            return ResultCommon.resultFail();
        }
    }



}

