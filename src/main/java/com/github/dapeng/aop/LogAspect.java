package com.github.dapeng.aop;

import com.github.dapeng.common.Commons;
import com.github.dapeng.common.Resp;
import com.github.dapeng.entity.system.TOpLog;
import com.github.dapeng.repository.system.OpLogRepository;
import com.github.dapeng.util.DateUtil;
import com.google.gson.Gson;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author with struy.
 * Create by 2018/10/9 17:50
 * email :yq1724555319@gmail.com
 */
public class LogAspect {
    private static Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    OpLogRepository logRepository;

    public Object record(ProceedingJoinPoint pjp) {

        TOpLog log = new TOpLog();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            // 操作人
            log.setOperator(authentication.getName());

            // 操作名(方法名)
            String mname = pjp.getSignature().getName();
            log.setOperName(mname);

            //方法参数
            Object[] args = pjp.getArgs();
            log.setOperParams(new Gson().toJson(args));

            //执行目标方法，返回的是目标方法的返回值
            Object obj = pjp.proceed();
            if (obj != null && obj instanceof ResponseEntity) {
                ResponseEntity responseEntity = (ResponseEntity) obj;
                if (responseEntity.getBody() instanceof Resp) {
                    Resp resp = (Resp) responseEntity.getBody();
                    log.setResultMsg(resp.getMsg());
                    log.setOperResult(String.valueOf(resp.getCode()));
                }
            } else {
                log.setResultMsg("返回为空");
                log.setOperResult(String.valueOf(Commons.ERROR_CODE));
            }
            log.setOperTime(DateUtil.now());
            return obj;
        } catch (Throwable e) {
            log.setOperResult(String.valueOf(Commons.ERROR_CODE));
            log.setResultMsg(e.getMessage());
            return null;
        } finally {
            // 保存操作日志
            try {
                logRepository.save(log);
            } catch (Exception e) {
                LOGGER.error("save log error{}", log, e);
            }
        }
    }
}
