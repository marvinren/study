package com.renzq.configure.annotation.beanregister;

import com.renzq.configure.configbean.SimplePerfLog;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * beanRegister is Created by Renzq
 * Email: renzq@asiainfo.com
 * Date: 2017/11/13 0013
 * Time: 14:44
 * Description:
 */
public class PerfLogBeanRegister implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {

//        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata
//                .getAnnotationAttributes(EnableApolloConfig.class.getName()));
//        String[] namespaces = attributes.getStringArray("value");
//        int order = attributes.getNumber("order");
//        PropertySourcesProcessor.addNamespaces(Lists.newArrayList(namespaces), order);
//
//        BeanRegistrationUtil.registerBeanDefinitionIfNotExists(registry, PropertySourcesProcessor.class.getName(),
//                PropertySourcesProcessor.class);
//
//        beanDefinitionRegistry.registerBeanDefinition("simplePerfLog", new SimplePerfLogConfigure);

        BeanDefinition bd = new RootBeanDefinition(SimplePerfLog.class);
        beanDefinitionRegistry.registerBeanDefinition("simplePerfLog", bd);
    }
}