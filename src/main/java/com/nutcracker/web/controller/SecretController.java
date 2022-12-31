package com.nutcracker.web.controller;

import com.nutcracker.enums.SecretStrategyEnum;
import com.nutcracker.enums.SecretTypeEnum;
import com.nutcracker.exception.BusinessException;
import com.nutcracker.service.secret.SecretService;
import com.nutcracker.util.DateUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * SecretController
 *
 * @author 胡桃夹子
 * @date 2022/12/31 13:30
 */
//@Tag(name = "secret", description = "加密与解密相关接口")
@Slf4j
@Controller
@RequestMapping("secret")
public class SecretController {

    @Resource
    private SecretService secretService;

    private static final String FLAG = "endFlag";

    //@Hidden
    @GetMapping("index")
    public String secretIndex() {
        return "secret_index";
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

    //@Operation(description = "多线程操作，加密解密操作，返回txt文件")
    //@Parameters({
    //        @Parameter(name = "uploadFile", description = "待处理的附件(txt格式)"),
    //        @Parameter(name = "secretType", description = "加密或解密的类型枚举：encrypt=加密,decrypt=解密", example = "encrypt"),
    //        @Parameter(name = "secretStrategy", description = "加解密渠道枚举: RSA=RSA加解密;DES=DES加解密;DESede=DESede加解密;", example = "RSA"),
    //})
    //@ApiResponses(value = {@ApiResponse(description = "处理返回的txt文件", content = {@Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE, schema = @Schema(implementation = ResponseEntity.class))})})
    @PostMapping(value = "batchExecute/{secretType}/{secretStrategy}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> batchExecute(@RequestPart MultipartFile uploadFile, @PathVariable String secretType, @PathVariable String secretStrategy, HttpServletRequest request) {
        try {
            log.info("# secretType={},secretStrategy={}", secretType, secretStrategy);
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
            log.info("# txtName={}", txtName);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.add("content-disposition", "attachment;filename*=UTF-8''" + URLEncoder.encode(txtName, "UTF-8"));
            //跟踪下载是否完成，清除标记位
            request.getSession().setAttribute(FLAG, "1");
            return new ResponseEntity<>(resp, headers, HttpStatus.OK);
        } catch (BusinessException e) {
            log.error("# {}", e.getErrorMsg());
            request.getSession().setAttribute(FLAG, "服务异常！");
            throw e;
        } catch (Exception e) {
            log.error("# {}", e.getLocalizedMessage());
            request.getSession().setAttribute(FLAG, "服务异常！");
            throw new BusinessException("服务异常！");
        }
    }


}
