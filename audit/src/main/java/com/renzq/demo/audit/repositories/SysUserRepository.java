package com.renzq.demo.audit.repositories;

import com.renzq.demo.audit.domain.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * SysUserRepository is Created by Renzq
 * Email: renzq@asiainfo.com
 * Date: 2017/11/13 0013
 * Time: 22:30
 * Description:
 */
@Repository
public interface SysUserRepository extends JpaRepository<SysUser, Long>{
}