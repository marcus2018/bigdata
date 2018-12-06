package com.immotor.collectData.util;

public class CRCUtils {
    public static  short  updateCrc(char ch,short lpwCrc) {
        ch =(char) (ch^( char)((lpwCrc) & 0x00FF));
        ch = (char)(ch^(ch<<4));
        lpwCrc = (short)(( (lpwCrc >> 8)^(( short)ch << 8))^(short) ((
                short)ch<<3)^(( short)ch>>4));
        return lpwCrc;

    }
}
