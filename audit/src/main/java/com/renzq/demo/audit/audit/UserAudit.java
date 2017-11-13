package com.renzq.demo.audit.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

/**
 * UserAudit is Created by Renzq
 * Email: renzq@asiainfo.com
 * Date: 2017/11/13 0013
 * Time: 22:29
 * Description:
 */
@Component
public class UserAudit implements AuditorAware<String> {
    @Override
    public String getCurrentAuditor() {
        return "renzq";
    }

//    @Override
//    public String getCurrentAuditor() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return null;
//        }
//
//        return ((User) authentication.getPrincipal()).getUsername();
//    }
}