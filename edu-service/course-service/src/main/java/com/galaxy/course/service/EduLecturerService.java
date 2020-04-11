package com.galaxy.course.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.galaxy.course.entity.EduLecturer;
import com.galaxy.course.entity.dto.LecturerDto;
import com.galaxy.common.util.ResultCommon;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author 玩你个球儿
 * @since 2019-12-26
 */
public interface EduLecturerService {

    /**
     * 查询所有讲师
     * @return
     */
    ResultCommon<List<EduLecturer>> queryAllLecturer();

    /**
     * 根据id删除讲师
     * @return
     */
    int deleteId(String id);

    /**
     * 分页查询
     * @param page 第几页 设置默认第一页
     * @param size  每页多少条 默认20
     * @return
     */
   ResultCommon<EduLecturer> pageLecturer(Integer page, Integer size);

    ResultCommon queryCondtion(Page p, LecturerDto lecturerDto);

    boolean add(LecturerDto lecturerDto);
    EduLecturer queryid(String id);
    boolean modfiy(String id, LecturerDto lecturerDto);
    ResultCommon queryDeleted(Long page, Long limit);

    boolean realDelete(String id);

    boolean recoverLecturer(String id);

    Map lecturerWebList(Page<EduLecturer> objectPage);

    Map lecturerDetail(String id);

    Map queryPopularLecturer();

    Map infos(List<String> ids);

    EduLecturer getInfo(String cid);
}
