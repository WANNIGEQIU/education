package com.galaxy.course.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.galaxy.course.entity.EduLecturer;
import com.galaxy.course.entity.dto.LecturerDto;
import com.galaxy.common.bean.CourseVo;
import com.galaxy.common.constants.CourseConstant;
import com.galaxy.common.enums.ResultEnum;
import com.galaxy.common.exception.MyException;
import com.galaxy.common.util.ResultCommon;
import com.galaxy.course.entity.EduCourse;
import com.galaxy.course.entity.EduCourseDescription;
import com.galaxy.course.mapper.EduCourseDescriptionMapper;
import com.galaxy.course.mapper.EduCourseMapper;
import com.galaxy.course.mapper.EduLecturerMapper;
import com.galaxy.course.service.EduLecturerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author 玩你个球儿
 * @since
 */
@Service
@Slf4j
public class EduLecturerServiceImpl implements EduLecturerService {

    @Autowired
    private EduLecturerMapper lecturerMapper;

    @Autowired
    private EduCourseMapper courseMapper;

    @Autowired
    private EduCourseDescriptionMapper descriptionMapper;



    /**
     * 查询所有讲师
     */
    @Override
    public ResultCommon<List<EduLecturer>> queryAllLecturer() {

        try {
            List<EduLecturer> list = this.lecturerMapper.selectList(null);
            if (CollectionUtils.isEmpty(list)) {
                throw new MyException(ResultEnum.QUERY_ERROR);
            } else {
                return ResultCommon.resultOk(list);
            }

        } catch (MyException e) {
            log.error(e.getMessage());
            return ResultCommon.resultFail().codeAndMsg(e.getCode(),e.getMessage());
        }

    }

    /**
     * 根据id删除讲师
     *
     * @param id
     * @return
     */
    @Override
    public int deleteId(String id) {
        if (StringUtils.isEmpty(id)) {
            log.error("传递的参数id错误: [{}]",id);
            throw new MyException(ResultEnum.PARAM_ERROR);
        }
       return this.lecturerMapper.deleteById(id);


    }

    /**
     * 分页查询
     *
     * @param page 第几页 设置默认第一页
     * @param size 每页多少条 默认20
     * @return
     */
    @Override
    public ResultCommon pageLecturer(Integer page, Integer size) {
        try {
            Page<EduLecturer> objectPage = new Page<>(page, size);
            Page<EduLecturer> selectPage = this.lecturerMapper.selectPage(objectPage, null);
            if (selectPage == null) {
                throw new MyException(ResultEnum.EXCEPTION.getCode(), "分页查询失败 selectPage 为null");
            }
            return ResultCommon.resultOk(null);
        } catch (MyException e) {
            log.error(e.getMessage());
            return ResultCommon.resultFail();
        }
    }


    @Override
    public ResultCommon queryCondtion(Page p, LecturerDto lecturerDto) {
        try {
            Page<EduLecturer> condtion = this.lecturerMapper.queryCondtion(p, lecturerDto);
            if (condtion == null) {
                throw new MyException(ResultEnum.QUERY_ERROR);
            }
            return ResultCommon.resultOk(condtion);

        } catch (MyException m){
            log.error("接口调用出现异常"+m.getMessage());
            return ResultCommon.resultFail();
        }

    }

    @Override
    public boolean add(LecturerDto lecturerDto) {
        EduLecturer eduLecturer = new EduLecturer();
        try {
            if (StringUtils.isEmpty(lecturerDto.getName())) {
                throw new MyException(ResultEnum.NO_NAME);
            }
            if (lecturerDto.getLevel() == null) {
                throw new MyException(ResultEnum.NO_LEVEL);
            }
            if (StringUtils.isEmpty(lecturerDto.getIntro())) {
                throw new MyException(ResultEnum.NO_INTRODU);
            }
            BeanUtils.copyProperties(lecturerDto, eduLecturer);
            int insert = this.lecturerMapper.insert(eduLecturer);
            if (insert > 0) {
                return true;
            } else {
                throw new MyException(ResultEnum.EXCEPTION.getCode(), "添加失败");
            }

        } catch (MyException m) {
            log.error(m.getMessage() + ">>>>> 输入的参数:[{}]", lecturerDto);
            return false;
        }


    }

    @Override
    public EduLecturer queryid(String id) {

        EduLecturer eduLecturer = this.lecturerMapper.selectById(id);
        return eduLecturer;
    }



    @Override
    public boolean modfiy(String id, LecturerDto lecturerDto) {
        if (StringUtils.isEmpty(id)) {
            log.error("传递的参数id错误: [{}]",id);
            throw new MyException(ResultEnum.PARAM_ERROR);
        }
        EduLecturer eduLecturer = new EduLecturer();
        BeanUtils.copyProperties(lecturerDto, eduLecturer);
        eduLecturer.setId(id);
        int i = this.lecturerMapper.updateById(eduLecturer);
        //return i > 0 ? true : false;
        return i>0;
    }

    @Override
    public ResultCommon queryDeleted(Long page, Long limit) {
        //page = -1L;
        if (page < 0 || limit < 0) {
            log.error("传入的参数有误: [{}],[{}]",page,limit);
            throw new  MyException(ResultEnum.PARAM_ERROR);
        }
        Page<EduLecturer> p = new Page<>(page, limit);
        Page<EduLecturer> eduLecturerPage = this.lecturerMapper.queryDeleted(p);

        return ResultCommon.resultOk(eduLecturerPage);
    }

    @Override
    public boolean realDelete(String id) {
        if (StringUtils.isEmpty(id)) {
            log.error("传递的参数id错误: [{}]",id);
            throw new MyException(ResultEnum.PARAM_ERROR);
        }
        int i = this.lecturerMapper.realDelete(id);
       return i > 0 ? true : false;

    }

    @Override
    public boolean recoverLecturer( String id) {
        HashMap<String, Object> map = new HashMap<>();
        if (StringUtils.isEmpty(id)) {
            log.error("传递的参数id错误: [{}]",id);
            throw new MyException(ResultEnum.PARAM_ERROR);
        }
        map.put("id",id);
        map.put("time",LocalDateTime.now());
        int i = this.lecturerMapper.recoverLecturer(map);
        return i > 0? true :false;


    }

    @Override
    public Map lecturerWebList(Page<EduLecturer> objectPage) {
        Map<String, Object> map = new HashMap<>();
        QueryWrapper<EduLecturer> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("sort");
         lecturerMapper.selectPage(objectPage,wrapper);
         // 讲师列表
        List<EduLecturer> records = objectPage.getRecords();
        // 总记录数
        long total = objectPage.getTotal();
        // 每页记录数
        long size = objectPage.getSize();
        // 总页数
        long pages = objectPage.getPages();
        // 当前页
        long current = objectPage.getCurrent();
        // 是否有下一页
        boolean hasNext = objectPage.hasNext();
        // 是否有上一页
        boolean previous = objectPage.hasPrevious();

        map.put("records",records);
        map.put("total",total);
        map.put("size",size);
        map.put("pages",pages);
        map.put("current",current);
        map.put("hasNext",hasNext);
        map.put("previous",previous);
        return map;
    }

    @Override
    public Map lecturerDetail(String id) {
        Map<String, Object> map = new HashMap<>();
        List<CourseVo> list = new ArrayList<>();
        // 讲师信息
        EduLecturer eduLecturer = this.lecturerMapper.selectById(id);
        // 课程信息
        List<EduCourse> eduCourses = this.courseMapper.selectList(new LambdaQueryWrapper<EduCourse>()
                .eq(EduCourse::getLecturerId, id)
                .eq(EduCourse::getStatus, CourseConstant.COURSE_PUBLISH));
        // 获取课程详情
         eduCourses.forEach(eduCourse ->{
             EduCourseDescription description = this.descriptionMapper.selectById(eduCourse.getId());
             CourseVo courseVo = new CourseVo();
              BeanUtils.copyProperties(eduCourse,courseVo);
              courseVo.setDescription(description.getDescription());
              list.add(courseVo);
         });

         map.put("lecturer",eduLecturer);
         map.put("course",list);

        return map;
    }

    @Override
    @Cacheable(value = "index", key = "'lecturer'")
    public Map queryPopularLecturer() {
        Map<String, Object> hashMap = new HashMap<>();
        List<EduLecturer> lecturerList = lecturerMapper.selectList(new LambdaQueryWrapper<EduLecturer>().eq(EduLecturer::getDid, 0)
                .eq(EduLecturer::getLevel, 3)
                .last("limit 0,8"));
        if (CollectionUtils.isEmpty(lecturerList)) {
            return hashMap;
        }
        hashMap.put("lecturerList",lecturerList);
        return hashMap;
    }

    @Override
    public Map infos(List<String> ids) {
        Map<String, Object> map = new HashMap<>();
        log.info("id:{}", JSON.toJSONString(ids));

        List<EduLecturer> eduLecturers = this.lecturerMapper.selectList(new LambdaQueryWrapper<EduLecturer>()
                .in(EduLecturer::getId, ids));

            eduLecturers.forEach(eduLecturer -> {
                map.put(eduLecturer.getId(),eduLecturer.getName());
            });

        return map;
    }

    @Override
    public EduLecturer getInfo(String cid) {

//        String lid = this.courseClient.queryForLect(cid);
//
//         if (StringUtils.isNotEmpty(lid)){
//             EduLecturer eduLecturer = this.lecturerMapper.selectById(lid);
//             return eduLecturer;
//         }

        return null;
    }


}
