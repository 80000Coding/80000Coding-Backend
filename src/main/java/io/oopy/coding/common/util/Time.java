package io.oopy.coding.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Time {
    public static int getNowUnixTime() {
        return (int) System.currentTimeMillis() / 1000;
    }
}
