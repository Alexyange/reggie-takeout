package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.CommonsEmail;
import com.itheima.reggie.common.RespData;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.service.UserService;
import com.itheima.reggie.utils.RandomNames;
import com.itheima.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName UserController.java
 * @Description TODO
 * @createTime 2022年01月28日 18:57:00
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * @throws
     * @title 完善功能10 修改个人信息
     * @description
     * @author Alex
     * @updateTime 2022/1/30 16:05
     */
    @PutMapping("/info")
    public RespData<String> updateIdNum(@RequestBody User user) {
        LambdaQueryWrapper<User> lam = new LambdaQueryWrapper<>();
        if (user.getId() != null) {
            lam.eq(User::getId, user.getId());
            userService.update(user, lam);
            return RespData.success("修改信息成功");
        }
        lam.eq(user != null, User::getPhone, user.getPhone());
        userService.update(user, lam);
        return RespData.success("修改信息成功");
    }

    @PutMapping("/pwd")
    public RespData<String> updatePwd(@RequestBody User user) {
        LambdaQueryWrapper<User> lam = new LambdaQueryWrapper<>();
        lam.eq(User::getPhone, user.getPhone());
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        userService.update(user, lam);
        return RespData.success("修改信息成功");
    }

    /**
     * 发送手机短信验证码
     *
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public RespData<String> sendMsg(@RequestBody User user, HttpSession session) throws Exception {
        //设置发送邮箱主题和内容
        String strTitle = "瑞吉外卖登录或者注册用验证码";
        String strText = "您好！您的验证码是";
        //获取手机号
        //String phone = user.getPhone();
        String email = user.getEmail();
        if (StringUtils.isNotEmpty(email)) {
            //生成随机的4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}", code);

            //调用阿里云提供的短信服务API完成发送短信
            //SMSUtils.sendMessage("瑞吉外卖","",phone,code);
            //调用相关工具类  实现 邮箱验证码
            CommonsEmail.sendTextMail(email, strTitle, strText + code);
            //需要将生成的验证码保存到Session
            //session.setAttribute(email, code);
            redisTemplate.opsForValue().set(email, code);
            return RespData.success(code);
        }
        return RespData.error("短信发送失败");
    }

    /**
     * 移动端用户登录
     *
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public RespData<User> login(@RequestBody Map map, HttpSession session) {
        log.info(map.toString());
        //获取手机号
        String email = map.get("email").toString();

        String phone = map.get("phone").toString();

        if ("1".equals(email)) {
            String password = map.get("password").toString();
            password = DigestUtils.md5DigestAsHex(password.getBytes());
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);
            if (null != user) {
                session.setAttribute("user", user.getId());
                return RespData.success(user);
            } else {
                return RespData.error("用户不存在，请使用邮箱登录");
            }
        }
        //获取验证码
        String code = map.get("code").toString();
        //从Session中获取保存的验证码
        //Object codeInSession = (String) session.getAttribute(email);
        Object codeInSession = redisTemplate.opsForValue().get(email);
        //进行验证码的比对（页面提交的验证码和Session中保存的验证码比对）
        if (codeInSession.equals(code)) {
            //if(codeInSession != null && codeInSession.equals(code)){
            //如果能够比对成功，说明登录成功
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);
            if (user == null) {
                //判断当前手机号对应的用户是否为新用户，如果是新用户就自动完成注册
                user = new User();
                user.setEmail(email);
                user.setPhone(phone);
                user.setName(RandomNames.getRandomJianHan(4));
                user.setSex("1");
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());
            redisTemplate.delete(email);
            return RespData.success(user);
        }
        return RespData.error("登录失败");
    }

    @GetMapping("/getid")
    public RespData getid(HttpSession session) {
        String user = session.getAttribute("user").toString();
        return RespData.success(user);
    }

    @PostMapping("/loginout")
    public RespData<String> logout(HttpSession session) {
        session.removeAttribute("user");
        return RespData.success("登出成功");
    }

    //通过手机号查询用户信息
    @GetMapping("/phone")
    public RespData<User> getByPhone(String phone) {
        log.info("查询用户数据接口");
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(phone != null, User::getPhone, phone);
        User user = userService.getOne(lambdaQueryWrapper);
        return RespData.success(user);
    }
}
