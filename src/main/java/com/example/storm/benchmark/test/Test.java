package com.example.storm.benchmark.test;

public class Test {
    public static void main(String[] args) {
        String str="[01,02,01,02,86,10,15,5D,8C,95,85,C2,6A,00,00,19,08,28,10,41,52,80,00,00,01,FF,FF,FF,FF,02,00,1E,03,7A,14,45,75,30,02,AD,00,02,02,A9,00,01,0F,A8,00,03,0F,1B,00,0C,0B,B8,02,EE,03,C0,00,31,31,7D,37,EB,00,5F,1C,E6,00,00,00,00,00,00,00,00,00,00,00,00,02,AE,02,AE,02,AF,02,A6,02,A9,02,AD,0F,9E,0F,A3,0F,A8,0F,A1,0F,A3,0F,A1,0F,A1,0F,A6,FF,FF,0F,A3,0F,9E,0F,1B,0F,A0,FF,FF,0F,A1,FF,FF,14,AA,00,00,00,00,00,00,00,82,00,3C,8B]";
        System.out.println(str.split(",").length);
    }
}
