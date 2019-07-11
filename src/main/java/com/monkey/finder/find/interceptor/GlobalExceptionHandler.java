package com.monkey.finder.find.interceptor;


import com.monkey.finder.find.status.FinderResultStateEnum;
import com.monkey.finder.find.status.ResultStateEnum;
import com.monkey.finder.find.status.ResultStatus;
import com.monkey.finder.find.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: 王志伟
 * @date: 2018/3/6 10:27
 * @desc:
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultStatus jsonErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        ThreadLocalUtil.clear();

        ResultStatus resultStatus;
        //无此页面或地址
        if (e instanceof NoHandlerFoundException) {
            //404
            resultStatus = FinderResultStateEnum.NO_HANDLER_FOUND_ERROR.toResultStatus();
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            resultStatus = FinderResultStateEnum.METHOD_NOT_SUPPORTED_ERROR.toResultStatus();

        } else if (e instanceof MissingServletRequestParameterException) {
            //缺少参数
            resultStatus = FinderResultStateEnum.MISSING_PARAMETER_ERROR.toResultStatus();

        } else if (e instanceof MethodArgumentTypeMismatchException) {
            //参数类型不对或无法转换
            resultStatus = FinderResultStateEnum.PARAMETER_TYPE_MISMATCH_ERROR.toResultStatus();
        } else if (e instanceof MethodArgumentNotValidException) {
            //参数校验异常
            resultStatus = FinderResultStateEnum.PARAMETER_TYPE_MISMATCH_ERROR.toResultStatus();
            List<String> errMsgs = new ArrayList<>();
            for (FieldError fieldError : ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors()) {
                errMsgs.add(fieldError.toString());
//                errorResults.add(UtilResult.fail(Integer.parseInt(fieldError.getCode()), fieldError.getDefaultMessage(),
//                        fieldError.getField() + fieldError.getRejectedValue()));
            }
            resultStatus.setInfo(errMsgs);
        } else if (e instanceof MaxUploadSizeExceededException) {

            resultStatus = FinderResultStateEnum.DATA_TOO_LONG_ERROR.toResultStatus();
        } else {
            resultStatus = ResultStateEnum.PROGRAM_EXCEPTION.toResultStatus();
            resultStatus.setInfo(e.getMessage());
        }

        log.error("GlobalExceptionHandler request.getRequestURI():" + req.getRequestURI() + "\nexception:", e);
        log.error("GlobalExceptionHandler response:{}", resultStatus);
        return resultStatus;
    }


}
