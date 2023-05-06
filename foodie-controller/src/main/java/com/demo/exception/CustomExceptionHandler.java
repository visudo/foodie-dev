package com.demo.exception;


import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.demo.common.utils.DemoJsonResult;

@RestControllerAdvice
public class CustomExceptionHandler {

    //上传文件超过500k捕获异常
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public DemoJsonResult handlerMaxUploadFile(){
        return DemoJsonResult.errorMsg("文件上传大小不能超过500kB，请压缩图片或者降低图片质量");
    }
}
