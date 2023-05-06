package com.demo.controller.center;


import com.demo.common.utils.CookieUtils;
import com.demo.common.utils.DateUtil;
import com.demo.common.utils.DemoJsonResult;
import com.demo.common.utils.JsonUtils;
import com.demo.controller.BaseController;
import com.demo.pojo.Users;
import com.demo.pojo.bo.center.CenterUserBO;
import com.demo.resource.FileUpload;
import com.demo.service.center.CenterUserService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("userInfo")
public class CenterUserController extends BaseController {

    @Autowired
    private CenterUserService centerUserService;

    @Autowired
    private FileUpload fileUpload;

    @PostMapping("/update")
    public DemoJsonResult update(@RequestParam String userId,
            @RequestBody @Valid CenterUserBO centerUserBO,
            BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {

        //判断BindingResult中是否存在错误信息
        if (result.hasErrors()) {
            Map<String, String> errorMap = getErrors(result);
            return DemoJsonResult.errorMap(errorMap);
        }

        Users userResult = centerUserService.updateUserInfo(userId, centerUserBO);

        userResult = setNullProperty(userResult);
        //将用户信息加密存储到cookie中
        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(userResult), true);

        // TODO 后续增加令牌token，会整合进redis
        return DemoJsonResult.ok();

    }
    
    @PostMapping("/uploadFace")
    public DemoJsonResult uploadFace(@RequestParam String userId,
            MultipartFile file,
            HttpServletRequest request, HttpServletResponse response) {

        //定义头像保存地址
        //String fileSpace = IMAGE_USER_FACE_LOCATION;
        String fileSpace = fileUpload.getImageUserFaceLocation();
        //在路径上为每一个用户增加userid,用于区分不同用户上传
        String uploadPathPrefix = File.separator + userId;

        //开始文件上传
        if (file != null) {
            FileOutputStream fileOutputStream = null;
            try {
                String fileName = file.getOriginalFilename();
                if (StringUtils.isNotBlank(fileName)) {
                    //文件重命名 imooc.png --> ["imooc","png"]
                    String[] fileNameArr = fileName.split("\\.");

                    //获取文件后缀名
                    String suffix = fileNameArr[fileNameArr.length - 1];

                    if (!suffix.equalsIgnoreCase("png") &&
                            !suffix.equalsIgnoreCase("jpg") &&
                            !suffix.equalsIgnoreCase("jpeg")){
                        return DemoJsonResult.errorMsg("图片格式不正确");
                    }

                    //文件名重组
                    String newFileName = "face-" + userId + "." + suffix;

                    //上传头像最终保存位置
                    String finalPath=  fileSpace + uploadPathPrefix +File.separator+newFileName;

                    //用于提供给web服务的访问地址
                    uploadPathPrefix += ("/" + newFileName);

                    File outFile = new File(finalPath);
                    if (outFile.getParentFile() != null){
                        //创建文件夹
                        outFile.getParentFile().mkdirs();
                    }

                    //文件输出保存到目录
                    fileOutputStream = new FileOutputStream(outFile);
                    InputStream inputStream = file.getInputStream();
                    IOUtils.copy(inputStream,fileOutputStream);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    if (fileOutputStream!=null){
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            return DemoJsonResult.errorMsg("文件不能为空");
        }

        //获取图片服务地址
        String imageServerUrl = fileUpload.getImageServerUrl();

        //由于浏览器可能存在缓存情况，所以我们需要加上时间戳来保证更新后的图片
        String finalUserFaceUrl = imageServerUrl+uploadPathPrefix
                + "?t=" + DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN);
        //更新用户头像到数据库
        Users userResult = centerUserService.updateUserFace(userId, finalUserFaceUrl);

        userResult = setNullProperty(userResult);
        //将用户信息加密存储到cookie中
        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(userResult), true);

        // TODO 后续增加令牌token，会整合进redis

        return DemoJsonResult.ok();
    }



    private Map<String, String> getErrors(BindingResult result) {
        HashMap<String, String> map = new HashMap<>();
        List<FieldError> errorList = result.getFieldErrors();
        for (FieldError error : errorList) {
            //发生验证错误所对应的某一个属性
            String errorField = error.getField();
            //验证错误信息
            String errorMsg = error.getDefaultMessage();

            map.put(errorField, errorMsg);

        }
        return map;
    }

    private Users setNullProperty(Users userResult) {
        userResult.setPassword(null);
        userResult.setMobile(null);
        userResult.setEmail(null);
        userResult.setCreatedTime(null);
        userResult.setUpdatedTime(null);
        userResult.setBirthday(null);
        return userResult;
    }
}
