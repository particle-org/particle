package com.particle.api.utils;

public interface IOnlineMessageCheck {
    CheckResult checkMessageSync(String message);

    public enum CheckResult {
        PASS, HIDE, FORBIDDEN
    }
}
