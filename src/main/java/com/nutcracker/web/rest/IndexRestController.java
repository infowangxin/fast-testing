package com.nutcracker.web.rest;

import com.nutcracker.constant.Constants;
import com.nutcracker.entity.ApiResponse;
import com.nutcracker.entity.sys.SysUser;
import com.nutcracker.service.sys.RedisService;
import com.nutcracker.service.sys.SysUserService;
import com.nutcracker.util.RedisUtils;
import com.nutcracker.util.SecurityUtils;
import jakarta.annotation.Resource;
import c.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * IndexRestController
 *
 * @author 胡桃夹子
 * @date 2022/12/23 10:39
 */
@RestController
public class IndexRestController {

    @Resource
    private RedisService redisService;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private SysUserService sysUserService;

    /**
     * 验证码 宽度
     */
    @Value("${loginCode.width}")
    private Integer width;

    /**
     * 验证码 高度
     */
    @Value("${loginCode.height}")
    private Integer height;

    /**
     * 验证码 运算位数
     */
    @Value("${loginCode.digit}")
    private Integer digit;

    @GetMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        com.wf.captcha.utils.CaptchaUtil.out(request, response);
    }
    /**
     * 获取验证码
     */
    //@GetMapping("/code")
    //public ImgResult getCode() {
    //    // 算术类型 https://gitee.com/whvse/EasyCaptcha
    //    ArithmeticCaptcha captcha = new ArithmeticCaptcha(width, height, digit);
    //    // 获取运算的结果
    //    String result = captcha.text();
    //    String uuid = IdUtil.simpleUUID();
    //    redisService.saveCode(uuid, result);
    //    return new ImgResult(captcha.toBase64(), uuid);
    //}

    @PostMapping("/updatePassword")
    public ApiResponse updatePassword(@RequestParam("oldPass") String oldPass,
                                      @RequestParam("pass") String pass) {
        //获取用户
        Authentication authentication = SecurityUtils.getCurrentUserAuthentication();
        String username = (String) authentication.getPrincipal();
        String usernameRedisKey = Constants.PASSWORD_UPDATE + username;
        // 校验用户是否被锁定
        if (redisUtils.exists(usernameRedisKey)) {
            if (redisUtils.sGetSetSize(usernameRedisKey) >= 3L) {
                return ApiResponse.fail("旧密码错误次数太多了，请稍后重试");
            }
        }
        // 判断旧密码是否正确
        SysUser sysUser = sysUserService.findByName(username);
        if (sysUser != null) {
            if (!new BCryptPasswordEncoder().matches(oldPass, sysUser.getPassword())) {
                redisUtils.sSetAndTime(usernameRedisKey, Constants.PASSWORD_UPDATE_MINUTE, new Date());
                return ApiResponse.fail("旧密码不匹配,还有" + (3 - redisUtils.sGetSetSize(usernameRedisKey)) + "次机会");
            } else {
                //更新密码
                sysUserService.updatePasswordById(new BCryptPasswordEncoder().encode(pass), sysUser.getId());
                if (redisUtils.exists(usernameRedisKey)) {
                    redisUtils.remove(usernameRedisKey);
                }
                return ApiResponse.success("更新成功");
            }
        } else {
            return ApiResponse.fail("获取用户信息出错，请稍后重试");
        }
    }




    
    /*@GetMapping("/getUserInfo")
    public ApiResponse getUserInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        authentication.getName()
    }*/

    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("admin"));
    }
}
