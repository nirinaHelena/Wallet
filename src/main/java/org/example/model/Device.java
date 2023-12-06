package org.example.model;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Device {
    private UUID deviceId;
    private String deviceName;
    private String code;

    // constructor without id

    public Device(String deviceName, String code) {
        this.deviceName = deviceName;
        this.code = code;
    }
}
