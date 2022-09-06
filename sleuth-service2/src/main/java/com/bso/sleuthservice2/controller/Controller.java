package com.bso.sleuthservice2.controller;

import com.bso.sleuthservice2.model.MyObject;
import com.bso.sleuthservice2.sqs.SqsSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/object")
@RequiredArgsConstructor
@Slf4j
public class Controller {

    private final SqsSender sqsSender;

    @PostMapping
    public void request(@RequestBody MyObject myObject) {
        log.info("[{}] Received", myObject.id());
        sqsSender.send(myObject, "my-queue");
    }
}
