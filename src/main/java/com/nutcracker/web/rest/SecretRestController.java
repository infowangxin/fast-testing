package com.nutcracker.web.rest;

import com.alibaba.fastjson2.JSONObject;
import com.nutcracker.enums.SecretStrategyEnum;
import com.nutcracker.enums.SecretTypeEnum;
import com.nutcracker.service.secret.SecretService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * SecretRestController
 *
 * @author 胡桃夹子
 * @date 2022/12/31 13:30
 */
//@Tag(name = "secret", description = "加密与解密相关接口")
@Slf4j
@RestController
@RequestMapping("secret")
public class SecretRestController {

    @Resource
    private SecretService secretService;

    private static final String FLAG = "endFlag";

    //@Operation(description = "加密解密操作")
    //@ApiResponses(value = {@ApiResponse(description = "返回结果")})
    //@Parameters({
    //        @Parameter(name = "textString", description = "待解密或加密操文本"),
    //        @Parameter(name = "secretType", description = "加密或解密的类型枚举：encrypt=加密,decrypt=解密", example = "encrypt"),
    //        @Parameter(name = "secretStrategy", description = "加解密渠道枚举: RSA=RSA加解密;DES=DES加解密;DESede=DESede加解密;", example = "RSA"),
    //})
    @PostMapping("execute/{secretType}/{secretStrategy}")
    public String execute(@RequestParam("textString") String textString, @PathVariable String secretType, @PathVariable String secretStrategy) {
        log.info("# textString={},secretType={},secretStrategy={}", textString, secretType, secretStrategy);
        String result = "";
        try {
            if (StringUtils.isNotBlank(textString)) {
                SecretTypeEnum typeEnum = SecretTypeEnum.getInstance(secretType);
                SecretStrategyEnum strategyEnum = SecretStrategyEnum.getInstance(secretStrategy);
                if (typeEnum == SecretTypeEnum.encrypt) {
                    result = secretService.encrypt(strategyEnum, textString);
                } else {
                    result = secretService.decrypt(strategyEnum, textString);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("{},{}", textString, result);
        return result;
    }

    /**
     * 从session获取结束标识
     */
    //@Hidden
    @RequestMapping("getEndFlag")
    public JSONObject getEndFlag(HttpServletRequest request) {
        //  跟踪下载情况，获取下载标记位
        Object endFlag = request.getSession().getAttribute(FLAG);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("flag", endFlag);
        log.debug("# 下载情况:{}", endFlag);
        return jsonObject;
    }

}
