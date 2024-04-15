package ru.feryafox.FeryaASM.utils;

import ru.feryafox.FeryaASM.enums.Register;

public class RegisterUtils {
    public static short getUpperByte(short value) {
        return (short) ((value >> 8) & 0xFF);
    }

    public static short getLowerByte(short value) {
        return (short) (value & 0xFF);
    }

    public static String getUpperByteBinary(short value) {
        return String.format("%8s", Integer.toBinaryString(getUpperByte(value) & 0xFF)).replace(' ', '0');
    }

    public static String getLowerByteBinary(short value) {
        return String.format("%8s", Integer.toBinaryString(getLowerByte(value) & 0xFF)).replace(' ', '0');
    }

    public static short setUpperByte(short originalValue, short newUpperByte) {
        return (short) (((newUpperByte & 0xFF) << 8) | (originalValue & 0xFF));
    }

    public static short setLowerByte(short originalValue, short newLowerByte) {
        return (short) ((originalValue & 0xFF00) | (newLowerByte & 0xFF));
    }

    public static String toBinaryString(short value) {
        return String.format("%16s", Integer.toBinaryString(value & 0xFFFF)).replace(' ', '0');
    }
}
