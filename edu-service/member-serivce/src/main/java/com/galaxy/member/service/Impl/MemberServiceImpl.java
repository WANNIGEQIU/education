package com.galaxy.member.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.galaxy.common.bean.PageVo;
import com.galaxy.common.bean.UserBean;
import com.galaxy.common.enums.ResultEnum;
import com.galaxy.common.enums.UserEnum;
import com.galaxy.common.exception.MyException;
import com.galaxy.common.util.JwtUtil;
import com.galaxy.common.util.MD5Utils;
import com.galaxy.common.util.MyString;
import com.galaxy.common.util.VerifyCodeUtils;
import com.galaxy.member.config.MemberConfig;
import com.galaxy.member.entity.EduUser;
import com.galaxy.member.entity.dto.UserDto;
import com.galaxy.member.mapper.MemberMapper;
import com.galaxy.member.service.MemberService;
import com.galaxy.base.memberapi.MemberBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Slf4j
@Service
public class MemberServiceImpl implements MemberService {


    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private MemberConfig memberConfig;


    @Override
    public String userLogin(String account, String password) {
        UserBean userBean = new UserBean();
        // true 手机号登录
        if (isInteger(account)) {
            EduUser eduUser = this.memberMapper.selectOne(new LambdaQueryWrapper<EduUser>()
                    .eq(EduUser::getMobile, account));
            String tokenSecret = getToken(password, eduUser);
            return tokenSecret;
        } else {
            //用户名登录
            EduUser eduUser = this.memberMapper.selectOne(new LambdaQueryWrapper<EduUser>()
                    .eq(EduUser::getUsername, account));
            String token = getToken(password, eduUser);

            return token;
    }




}



    @Override
    public PageVo getUserList(Page<EduUser> objectPage, UserDto dto) {
        LambdaQueryWrapper<EduUser> wrapper = null;
        if (dto != null) {
             wrapper = new LambdaQueryWrapper<EduUser>()
                    .eq(StringUtils.isNotBlank(dto.getUsername()), EduUser::getUsername, dto.getUsername())
                    .eq(StringUtils.isNotBlank(dto.getBeginTime()), EduUser::getEduCreate, dto.getBeginTime())
                    .eq(StringUtils.isNotBlank(dto.getEndTime()), EduUser::getEduCreate, dto.getEndTime())
                    .eq(dto.getProhibit() != null, EduUser::getProhibit, dto.getProhibit());
        }
        Page<EduUser> eduUserPage = this.memberMapper.selectPage(objectPage, wrapper);


        return new PageVo(eduUserPage);    }

    @Override
    public boolean deleteUser(String id) {
        int i = this.memberMapper.deleteById(id);
        return i > 0 ? true : false;
    }

    @Override
    public boolean prohibitUser(String id) {
        EduUser eduUser = new EduUser().setId(id).setProhibit(1);
        int i = this.memberMapper.updateById(eduUser);
        return i > 0 ? true : false;
    }

    @Override
    public boolean recoveryUser(String id) {
        EduUser eduUser = new EduUser().setId(id).setProhibit(0);
        int i = this.memberMapper.updateById(eduUser);

        return i > 0 ? true : false;
    }

    @Override
    public Page<EduUser> deleteUserList(Page<EduUser> objectPage) {
        Page<EduUser> page = this.memberMapper.deleteUserList(objectPage);

        return page;
    }

    @Override
    public EduUser getUserInfo(String username) {
        QueryWrapper<EduUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        EduUser eduUser = this.memberMapper.selectOne(wrapper);
        if (eduUser == null) {
            throw new MyException(ResultEnum.QUERY_ERROR);
        } else {
            return eduUser;
        }
    }

    @Override
    public Integer updatePoints(String username, String amount) {
        QueryWrapper<EduUser> query = new QueryWrapper<>();
        query.eq("username", username);
        EduUser eduUser = this.memberMapper.selectOne(query);
        Integer i1 = Integer.valueOf(eduUser.getPoints());
        Integer i2 = Integer.valueOf(amount.substring(0, amount.indexOf(".")));
        Integer i3 = i1 + i2;
        String resultPoint = i3.toString();
        eduUser.setPoints(resultPoint);
        int update = this.memberMapper.update(eduUser, query);
        return update;
    }

    @Override
    public Integer queryNums(String day) {

        return memberMapper.queryNums(day);
    }


    @Override
    public void sendVerifyCode(String mobile) {
        if (StringUtils.isEmpty(mobile)) {
            throw new MyException(ResultEnum.PARAM_ERROR);
        }
        //是否注册
        Integer count = this.memberMapper.selectCount(new LambdaQueryWrapper<EduUser>()
                .eq(EduUser::getMobile, mobile)
                .eq(EduUser::getProhibit, UserEnum.USER_IS_USERD.getCode()));
        if (count != 0) {
            throw new MyException(ResultEnum.USER_PHONE_ISEXIST);
        }
        // 验证码
        String verifyCode = VerifyCodeUtils.getVerifyCode();

        // 发消息 sms mq
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("mobile", mobile);
        hashMap.put("verifyCode", verifyCode);
        this.rabbitTemplate.convertAndSend("heimaoSms", "sms", hashMap);
        log.info("发送到mq: {},{}", mobile, verifyCode);
        // 验证存到redis
        this.redisTemplate.opsForValue().set("user" + mobile, verifyCode, 5, TimeUnit.MINUTES);

    }

    @Override
    public int UserRegister(UserDto eduUser, String verifyCode) {

        //校验密码
        if (!MyString.stringValue(eduUser.getPassword(), eduUser.getCheckPassword())) {
            throw new MyException(ResultEnum.CHECK_PASSWORD_ERROR);
        }

        // 获取redis 存的验证码 key (user + mobile)
        String code = this.redisTemplate.opsForValue().get("user" + eduUser.getMobile());
        if ((verifyCode.equals(code)) == false) {
            throw new MyException(ResultEnum.VERFYICODE_IS_ERROR);
        }
        // 是否存在用户
        EduUser one = this.memberMapper.selectOne(new LambdaQueryWrapper<EduUser>()
                .eq(EduUser::getUsername, eduUser.getUsername()));

        EduUser eduUser1 = this.memberMapper.selectOne(new LambdaQueryWrapper<EduUser>()
                .eq(EduUser::getMobile, eduUser.getMobile()));

        if (one != null) {
            throw new MyException(ResultEnum.USER_IS_EXIST);
        }
        if (eduUser1 != null) {
            throw new MyException(ResultEnum.USER_PHONE_ISEXIST);
        }

        EduUser user = new EduUser();
        BeanUtils.copyProperties(eduUser, user);


        log.info("用户账号密码: {},{}", eduUser.getUsername(), eduUser.getPassword());
        // 获取盐
        String salt = MD5Utils.saltRandom();
        user.setSalt(salt);
        // 密码加密
        String md5Password = MD5Utils.MD5EncodeUtf8(eduUser.getPassword(), salt);
        user.setPassword(md5Password);
        // 默认头像
        user.setAvatar(memberConfig.getAvatar());
        int insert = this.memberMapper.insert(user);
        // 获取插入的id
        return insert > 0 ? insert : 0;


    }

    @Override
    public void lossCode(String mobile) {
        //此手机号是否注册
        QueryWrapper<EduUser> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        wrapper.eq("prohibit", 0);
        Integer integer = this.memberMapper.selectCount(wrapper);
        if (integer == 0) {
            throw new MyException(9833, "该手机号没有注册，请注册");
        }
        //验证码
        String verifyCode = VerifyCodeUtils.getVerifyCode();
        //发送mq
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("mobile", mobile);
        hashMap.put("verifyCode", verifyCode);
        this.rabbitTemplate.convertAndSend("loss", "loss", hashMap);

        //验证码存入redis
        this.redisTemplate.opsForValue().set("loss" + mobile, verifyCode, 5, TimeUnit.MINUTES);

    }

    @Override
    public Integer lossPassword1(UserDto dto) {

        String code = this.redisTemplate.opsForValue().get("loss" + dto.getMobile());
        if (StringUtils.isEmpty(code)) {
            throw new MyException(ResultEnum.VERFYRCODE_IS_NULL);
        }
        if (code.equals(dto.getVerifyCode())) {
            return 1;
        } else {

            throw new MyException(ResultEnum.VERFYICODE_IS_ERROR);
        }

    }

    @Override
    public boolean lossPassword2(String s1, String s2, String s3) {
        if (s1.equals(s2)) {
            EduUser user = new EduUser();
            QueryWrapper<EduUser> wrapper = new QueryWrapper<>();
            wrapper.eq("mobile", s3);
            String salt = MD5Utils.saltRandom();
            String password = MD5Utils.MD5EncodeUtf8(s1, salt);
            user.setSalt(salt);
            user.setPassword(password);
            int update = this.memberMapper.update(user, wrapper);
            return update > 0 ? true : false;
        } else {
            throw new MyException(9820, "两次输入的密码不一致");
        }
    }

    @Override
    public boolean updateUser(UserDto dto) {
        EduUser eduUser = new EduUser();
        if (StringUtils.isNotBlank(dto.getId())) {
            eduUser.setId(dto.getId());
        }
        if (StringUtils.isNotBlank(dto.getMobile())) {
            eduUser.setMobile(dto.getMobile());
        }
        if (dto.getAge() != null) {
            eduUser.setAge(dto.getAge());
        }
        if (dto.getSex() != null) {
            eduUser.setSex(dto.getSex());
        }

        int i = this.memberMapper.updateById(eduUser);


        return i > 0 ? true : false;
    }

    @Override
    public int upAvatar(String id, String head) {
        EduUser eduUser = new EduUser();
        eduUser.setAvatar(head).setId(id);
        int update = this.memberMapper.updateById(eduUser);


        return update > 0? update : 0;
    }

    @Override
    public MemberBean getMember(String memberid) {

        EduUser eduUser = this.memberMapper.selectById(memberid);
        if (eduUser !=null){
            MemberBean memberBean = new MemberBean();
            BeanUtils.copyProperties(eduUser,memberBean);
            return memberBean;
        }

        return null;
    }

    @Override
    public Integer UpdateIntegral(String userId, String integral) {

        EduUser eduUser = this.memberMapper.selectById(userId);
         if (eduUser == null){
             return -1;
         }
        EduUser put = new EduUser();
         put.setId(userId).setPoints(integral);
        return this.memberMapper.updateById(put);
    }

    @Override
    public EduUser memberInfo(String id) {


        return this.memberMapper.selectById(id);
    }


    private String getToken(String password, EduUser eduUser) {
        if (eduUser == null) {
            throw new MyException(ResultEnum.USER_NOT_EXIST);
        }
        //密码判断
        String salt = eduUser.getSalt();
        String passwordDb = eduUser.getPassword();
        if (!MD5Utils.MD5EncodeUtf8(password, salt).equals(passwordDb)) {
            throw new MyException(ResultEnum.USER_PASSWORD_ERROR);
        }
        // jwt
        return JwtUtil.getTokenSecret(eduUser.getId(), eduUser.getUsername(), eduUser.getMobile(),eduUser.getAvatar());
    }

    public static boolean isInteger(String string) {
        Pattern compile = Pattern.compile("^[-\\+]?[\\d]*$");
        return compile.matcher(string).matches();
    }
}
