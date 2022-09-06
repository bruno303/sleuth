package com.bso.sleuthservice2.infra.aws.message.sender.springcloud;

import com.bso.sleuthservice2.infra.aws.OnSpringCloudEnabled;
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
