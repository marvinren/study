package com.renzq.demo.audit;

import com.renzq.demo.audit.domain.SysUser;
import com.renzq.demo.audit.repositories.SysUserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * AuditTest is Created by Renzq
 * Email: renzq@asiainfo.com
 * Date: 2017/11/13 0013
 * Time: 22:32
 * Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AuditTest {

    final transient private static Logger log = LoggerFactory.getLogger(AuditTest.class);

    @Autowired
    private SysUserRepository sysUserRepository;

    @Test
    public void TestAudit(){
            SysUser user = new SysUser();
            this.sysUserRepository.save(user);
            log.info("{}", user.getCreateAt());
            log.info("{}", user.getModifiedAt());
            log.info(user.getCreateBy());
            log.info(user.getModifiedBy());
            log.info("{}", user.getVersion());

    }

}