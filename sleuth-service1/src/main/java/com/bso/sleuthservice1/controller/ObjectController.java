package com.bso.sleuthservice1.controller;

import com.bso.sleuthservice1.infra.feign.Service2Feign;
import com.bso.sleuthservice1.model.MyObject;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/object")
public class ObjectController {

    private final Service2Feign service2;

    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectController.class);

    @PostMapping
    public void request(@RequestBody MyObject myObject) {
        LOGGER.info("[{}] Requesting", myObject.id());
        service2.sendToService2(myObject);
    }
}
