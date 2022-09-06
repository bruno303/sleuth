package com.bso.sleuthservice3.infra.aws.springcloud.listener;

import com.bso.sleuthservice3.infra.aws.OnSpringCloudEnabled;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Component
@OnSpringCloudEnabled
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface SpringCloudComponent {}
