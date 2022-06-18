/*
 * Copyright (c) ReichTUM 2022.
 */

package com.adveisor.g2.monopoly.controller;

import com.adveisor.g2.monopoly.engine.service.GameService;
import com.adveisor.g2.monopoly.engine.service.model.mqtt.MqttPublishModel;
import lombok.Getter;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/mqtt")
public class MqttController {

    @Autowired
    GameService gameService;

    @PostMapping("publish")
    public void publishMessage(@RequestBody @Valid MqttPublishModel messagePublishModel,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "MQTT ERROR: Some parameter(s) invalid");
        }

        GameService.mqttPublishMessageApi(messagePublishModel);
    }

    @GetMapping("subscribe")
    public void subscribeChannel(@RequestParam(value = "topic") String topic) {
        gameService.mqttSubscribeTopic(topic);
    }

    @GetMapping("/subscribe-all")
    public void subscribeAllTopicss() {
        gameService.subscribeAllTopics();
    }


}
