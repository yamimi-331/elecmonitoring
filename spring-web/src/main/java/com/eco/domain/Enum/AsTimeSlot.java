package com.eco.domain.Enum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum AsTimeSlot {
	SLOT_09("09:00"),
    SLOT_10("10:00"),
    SLOT_11("11:00"),
    SLOT_12("12:00"),
    SLOT_13("13:00"),
    SLOT_14("14:00"),
    SLOT_15("15:00"),
    SLOT_16("16:00"),
    SLOT_17("17:00");;

    private final String time;

    AsTimeSlot(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public static List<String> getAllSlots() {
        return Arrays.stream(AsTimeSlot.values())
                .map(AsTimeSlot::getTime)
                .collect(Collectors.toList());  
    }
}
