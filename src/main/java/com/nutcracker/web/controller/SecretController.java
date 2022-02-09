package com.nutcracker.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.nutcracker.enums.SecretStrategyEnum;
import com.nutcracker.enums.SecretTypeEnum;
import com.nutcracker.exception.BusinessException;
import com.nutcracker.service.secret.SecretService;
import com.nutcracker.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Controller
public class SecretController {

    private static final Logger LOG = LoggerFactory.getLogger(SecretController.class);

    @Autowired
    private SecretService secretService;

    private static final String FLAG = "endFlag";

    @GetMapping("secret")
    public String secret() {
        return "secret";
    }

    /**
     * 解析文本内容
     *
     * @param multipartFile 上传txt文件
     * @return 文件类型
     */
    private List<String> readerBody(MultipartFile multipartFile) {
        List<String> list = new ArrayList<>();
        try {
            list = new ArrayList<>();
            Reader reader = new InputStreamReader(multipartFile.getInputStream(), StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(reader);
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 多线程操作，加密解密操作，返回txt文件
     *
     * @param uploadFile     待解密或加密操作的txt文本
     * @param secretType     true=加密,false=解密
     * @param secretStrategy true=集团加解密,false=RSA加解密
     * @param request        HttpServletRequest
     * @return 文本结果
     */
    @PostMapping("batchExecute/{secretType}/{secretStrategy}")
    public ResponseEntity<byte[]> batchExecute(@RequestParam("uploadFile") MultipartFile uploadFile, @PathVariable String secretType, @PathVariable String secretStrategy, HttpServletRequest request) {
        try {
            LOG.info("# secretType={},secretStrategy={}", secretType, secretStrategy);
            //跟踪下载是否完成，种下标记位
            request.getSession().removeAttribute(FLAG);
            //解析内容
            List<String> list = readerBody(uploadFile);

            SecretTypeEnum typeEnum = SecretTypeEnum.getInstance(secretType);
            SecretStrategyEnum strategyEnum = SecretStrategyEnum.getInstance(secretStrategy);

            String result = null;
            if (typeEnum == SecretTypeEnum.encrypt) {
                result = secretService.encrypt(strategyEnum, list);
            } else {
                result = secretService.decrypt(strategyEnum, list);
            }

            if (StringUtils.isBlank(result)) {
                throw new BusinessException("操作失败");
            }
            String ymd = DateUtil.dateToString(Calendar.getInstance().getTime(), "yyyyMMdd_HHmmss_SSS");
            byte[] resp = result.getBytes(StandardCharsets.UTF_8);
            String txtName = strategyEnum.name() + "_" + typeEnum.name() + "_" + ymd + ".txt";
            LOG.info("# txtName={}", txtName);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.add("content-disposition", "attachment;filename*=UTF-8''" + URLEncoder.encode(txtName, "UTF-8"));
            //跟踪下载是否完成，清除标记位
            request.getSession().setAttribute(FLAG, "1");
            return new ResponseEntity<>(resp, headers, HttpStatus.OK);
        } catch (BusinessException e) {
            LOG.error("# {}", e.getErrorMsg());
            request.getSession().setAttribute(FLAG, "服务异常！");
            throw e;
        } catch (Exception e) {
            LOG.error("# {}", e.getLocalizedMessage());
            request.getSession().setAttribute(FLAG, "服务异常！");
            throw new BusinessException("服务异常！");
        }
    }

    /**
     * 加密解密操作
     *
     * @param textString     待解密或加密操文本
     * @param secretType     true=加密,false=解密
     * @param secretStrategy true=集团加解密,false=RSA加解密
     * @return 结果
     */
    @PostMapping("execute/{secretType}/{secretStrategy}")
    @ResponseBody
    public String execute(@RequestParam("textString") String textString, @PathVariable String secretType, @PathVariable String secretStrategy) {
        LOG.info("# textString={},secretType={},secretStrategy={}", textString, secretType, secretStrategy);
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
        LOG.info("{},{}", textString, result);
        return result;
    }

    /**
     * 从session获取结束标识
     */
    @ResponseBody
    @RequestMapping("getEndFlag")
    public JSONObject getEndFlag(HttpServletRequest request) {
        //  跟踪下载情况，获取下载标记位
        Object endFlag = request.getSession().getAttribute(FLAG);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("flag", endFlag);
        LOG.debug("# 下载情况:{}", endFlag);
        return jsonObject;
    }

}
