/*
 * Copyright (c) ReichTUM 2022.
 */

package com.adveisor.g2.monopoly.engine.service.model.mqtt;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class MqttPublishModel {

    @NotNull
    @Size(min = 1, max = 255)
    private String topic;

    @NotNull
    @Size(min = 1, max = 255)
    private String message;

    @NotNull
    private Boolean retained;

    @NotNull
    private Integer qos;
}