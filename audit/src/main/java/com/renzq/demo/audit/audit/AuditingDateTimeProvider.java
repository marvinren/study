package com.renzq.demo.audit.audit;

import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * AuditingDateTimeProvider is Created by Renzq
 * Email: renzq@asiainfo.com
 * Date: 2017/11/13 0013
 * Time: 22:44
 * Description:
 */
@Component
public class AuditingDateTimeProvider implements DateTimeProvider {
    @Override
    public Calendar getNow() {
        Calendar  ca = Calendar.getInstance();
        ca.set(2000, 1, 1);
        return ca;
//        return GregorianCalendar.from(ZonedDateTime.now());
    }
}