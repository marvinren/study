package com.renzq.configure.annotation.impselect;

import com.renzq.configure.config.CorePerfLogConfigure;
import com.renzq.configure.config.SimplePerfLogConfigure;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * AutoSelectorLog is Created by Renzq
 * Email: renzq@asiainfo.com
 * Date: 2017/11/13 0013
 * Time: 14:09
 * Description:
 */
public class AutoLogSelector implements ImportSelector{


    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        Class<?> annotationType = EnablePerfLogSelector.class;
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(
                annotationType.getName(), false));
        String policy = attributes.getString("mode");
        if ("core".equals(policy)) {
            return new String[]{CorePerfLogConfigure.class.getName()};
        } else {
            return new String[]{SimplePerfLogConfigure.class.getName()};
        }

    }

}