package com.example.training_symbiocare_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeviceInfo {
    String ipAddress;
    String userAgent;
}
