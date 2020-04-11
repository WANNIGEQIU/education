package com.galaxy.course.controller;



import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.galaxy.course.entity.EduLecturer;
import com.galaxy.course.entity.dto.LecturerDto;
import com.galaxy.course.service.EduLecturerService;
import com.galaxy.common.enums.ResultEnum;
import com.galaxy.common.exception.MyException;
import com.galaxy.common.util.ResultCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author 玩你个球儿
 * @since
 */

@RestController
@RequestMapping("/lecturer")

public class EduLecturerController {


    @Autowired
    private EduLecturerService lecturerService;

    @GetMapping("/queryAll")
    public ResultCommon<List<EduLecturer>> queryAll() {
        return this.lecturerService.queryAllLecturer();
    }

    /**
     * 根据id删除讲师
     * @param id
     * @return
     */
   @DeleteMapping("{id}")
    public ResultCommon deleteId(@PathVariable("id") String id) {

        int i = this.lecturerService.deleteId(id);
         return ResultCommon.resultOk(i);

    }

    @PostMapping("/real/{page}/{limit}")
    public ResultCommon queryDeleted(@PathVariable("page") Long page,@PathVariable("limit") Long limit){

        return this.lecturerService.queryDeleted(page,limit);
    }



    /**
     * 条件分页查询
     * @param lecturerDto
     * @return
     */
    @PostMapping("/condtion/{page}/{limit}")
    public ResultCommon queryCondtion(@PathVariable("page") Long page,
                                      @PathVariable("limit") Long limit,@RequestBody(required = false) LecturerDto lecturerDto) {
        if (page < 0 || limit <0) {
            throw new MyException(ResultEnum.PARAM_ERROR);
        }
        Page<EduLecturer> p = new Page<>(page,limit);
       return this.lecturerService.queryCondtion(p,lecturerDto);
    }

 @PostMapping("/save")
    public ResultCommon save(@RequestBody LecturerDto lecturerDto) {
        boolean add = this.lecturerService.add(lecturerDto);
        if (add) {
            return ResultCommon.resultOk();
        } else {
            return ResultCommon.resultFail().data("添加失败啦啊 笨蛋");
        }
    }

    @DeleteMapping("/real/{id}")
    public ResultCommon realDelete(@PathVariable("id") String id) {
        boolean b = this.lecturerService.realDelete(id);
        if (b) {
            return ResultCommon.resultOk();
        }else {
            return ResultCommon.resultFail();
        }
    }

    @PutMapping("/recoverLecturer/{id}")
    public ResultCommon recoverLecturer(@PathVariable("id") String id) {
        boolean b = this.lecturerService.recoverLecturer(id);
        if (b) {
            return ResultCommon.resultOk(true);
        }else {
            return ResultCommon.resultFail().data(false);
        }
    }

    @GetMapping("/queryid/{id}")
    public ResultCommon<EduLecturer> queryid(@PathVariable String id) {
        EduLecturer queryid = this.lecturerService.queryid(id);
        return ResultCommon.resultOk(queryid);

    }

    @PutMapping("/modfiy/{id}")
    public ResultCommon modfiy(@PathVariable String id, @RequestBody LecturerDto lecturerDto) {
        boolean b = this.lecturerService.modfiy(id, lecturerDto);
        if (b) {
            return ResultCommon.resultOk(true);
        }else {
            return ResultCommon.resultFail().msg("修改失败了奥").data(false);
        }
    }

    @GetMapping("/getinfo")
    public ResultCommon getInfo(String cid) {

        EduLecturer info = this.lecturerService.getInfo(cid);
        return ResultCommon.resultOk(info);
    }

}

