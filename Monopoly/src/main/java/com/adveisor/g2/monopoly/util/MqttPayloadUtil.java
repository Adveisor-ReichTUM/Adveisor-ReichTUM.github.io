/*
 * Copyright (c) ReichTUM 2022.
 */

package com.adveisor.g2.monopoly.util;

import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttPayloadUtil {

    public static String getByteArrayBinary(MqttMessage mqttMessage) {
        byte[] bytes = mqttMessage.getPayload();
        StringBuilder toReturn = new StringBuilder();
        for (byte b : bytes) {
            toReturn.append(getByteBinary(b));
            toReturn.append('_');
        }
        toReturn.deleteCharAt(toReturn.length()-1);
        return toReturn.toString();
    }

    public static int getInt(MqttMessage mqttMessage) {
        return Integer.parseInt(mqttMessage.toString());
    }

    public static String getByteBinary(byte b) {
        return String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
    }

}
