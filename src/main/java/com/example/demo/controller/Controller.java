package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.constant.ApplicationConstants;
import com.example.demo.dto.ResponseDto;
import com.example.demo.service.SnsPublisherService;

@RestController
public class Controller {

    @Autowired
    SnsPublisherService snsPublisherService;

    @PostMapping("/publish/valid")
    public ResponseDto publishValidMessageToSns() {
        snsPublisherService.publishMessage(ApplicationConstants.BONUS_POINT_EVENT_VALID);
        return new ResponseDto("SUCCESS");
    }

    @PostMapping("/publish/invalid")
    public ResponseDto publishInvalidMessageToSns() {
        snsPublisherService.publishMessage(ApplicationConstants.BONUS_POINT_EVENT_INVALID);
        return new ResponseDto("SUCCESS");
    }

}
