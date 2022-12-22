package com.nutcracker.web.rest;

import cn.hutool.core.util.IdUtil;
import com.thyme.common.base.ApiResponse;
import com.thyme.common.base.Constants;
import com.thyme.common.utils.RedisUtils;
import com.thyme.common.utils.SecurityUtils;
import com.thyme.system.entity.SysUser;
import com.thyme.system.service.RedisService;
import com.thyme.system.service.SysUserService;
import com.thyme.system.vo.ImgResult;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author thyme
 * @ClassName IndexRestController
 * @Description TODO
 * @Date 2019/12/17 20:18
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IndexRestController {

    private final RedisService redisService;

    private final RedisUtils redisUtils;

    private final SysUserService sysUserService;

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

    /**
     * 获取验证码
     */
    @GetMapping("/code")
    public ImgResult getCode() {
        // 算术类型 https://gitee.com/whvse/EasyCaptcha
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(width, height,digit);
        // 获取运算的结果
        String result = captcha.text();
        String uuid = IdUtil.simpleUUID();
        redisService.saveCode(uuid,result);
        return new ImgResult(captcha.toBase64(),uuid);
    }

    @PostMapping("/updatePassword")
    public ApiResponse updatePassword(@RequestParam("oldPass") String oldPass,
                                      @RequestParam("pass") String pass){
        //获取用户
        Authentication authentication = SecurityUtils.getCurrentUserAuthentication();
        String username = (String)authentication.getPrincipal();
        String usernameRedisKey = Constants.PASSWORD_UPDATE + username;
        // 校验用户是否被锁定
        if (redisUtils.exists(usernameRedisKey)) {
            if (redisUtils.sGetSetSize(usernameRedisKey) >= 3L){
                return ApiResponse.fail("旧密码错误次数太多了，请稍后重试");
            }
        }
        // 判断旧密码是否正确
        SysUser sysUser = sysUserService.findByName(username);
        if (sysUser != null){
            if (!new BCryptPasswordEncoder().matches(oldPass,sysUser.getPassword())){
                redisUtils.sSetAndTime(usernameRedisKey,Constants.PASSWORD_UPDATE_MINUTE, new Date());
                return ApiResponse.fail("旧密码不匹配,还有"+ (3-redisUtils.sGetSetSize(usernameRedisKey)) +"次机会");
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
