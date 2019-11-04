package com.topwulian.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理报文工具类
 * @Author: szz
 * @Date: 2018/10/19 下午11:51
 * @Version 1.0
 */
public class MessageUtil {
    private static final String[] HEX_TABLE = new String[]{"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "0a", "0b", "0c", "0d", "0e", "0f", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "1a", "1b", "1c", "1d", "1e", "1f", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "2a", "2b", "2c", "2d", "2e", "2f", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "3a", "3b", "3c", "3d", "3e", "3f", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "4a", "4b", "4c", "4d", "4e", "4f", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "5a", "5b", "5c", "5d", "5e", "5f", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "6a", "6b", "6c", "6d", "6e", "6f", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "7a", "7b", "7c", "7d", "7e", "7f", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "8a", "8b", "8c", "8d", "8e", "8f", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "9a", "9b", "9c", "9d", "9e", "9f", "a0", "a1", "a2", "a3", "a4", "a5", "a6", "a7", "a8", "a9", "aa", "ab", "ac", "ad", "ae", "af", "b0", "b1", "b2", "b3", "b4", "b5", "b6", "b7", "b8", "b9", "ba", "bb", "bc", "bd", "be", "bf", "c0", "c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8", "c9", "ca", "cb", "cc", "cd", "ce", "cf", "d0", "d1", "d2", "d3", "d4", "d5", "d6", "d7", "d8", "d9", "da", "db", "dc", "dd", "de", "df", "e0", "e1", "e2", "e3", "e4", "e5", "e6", "e7", "e8", "e9", "ea", "eb", "ec", "ed", "ee", "ef", "f0", "f1", "f2", "f3", "f4", "f5", "f6", "f7", "f8", "f9", "fa", "fb", "fc", "fd", "fe", "ff"};

    /**
     * 上报温度传感器和湿度传感器数值报文,温度值为+23.5度，湿度为67%,上报时间是2018.10.10-01:42:50
     * 7b  10  12  01  2b  17  2e  05  23  02  43  40  07  e2  0a  0a  01  2a  21  a0
     *
     */
    public static String[] handleStr2ArrStr(String message){

        //去空格
        message = message.replace(" ", "");

        //先判断数据是否完整,完整可以正确处理,如果不完整尝试进行处理,看是否能解析一部分
        //先转换成char数组
        char[] chars = message.toCharArray();
        List<String> list = new ArrayList<>();
        String tmp = "";
        for (int i = 0; i < chars.length; i++) {
            if (i%2==0&&i>0) {
                //System.out.println(tmp);
                list.add(tmp);
                tmp = "";
            }
            tmp+=chars[i]+"";
            if (i==chars.length-1) {
                list.add(tmp);
            }
        }

        System.out.println();
        String[] strings = list.toArray(new String[list.size()]);

        return strings;

    }

    public static Integer[] handleStr2ArrInt(String[] strings){
        Integer[] integers =new Integer[strings.length];
        for (int i = 0; i < strings.length; i++) {
            integers[i]=Integer.valueOf(strings[i],16);

        }
        return integers;
    }


    /**
     * 将字节流转换成十六进制数据.
     *
     * @param originalData
     * @return
     */
    public static String toChangeByHex(byte[] originalData) {
        byte[] data = originalData;
        StringBuilder rc = new StringBuilder(originalData.length * 2);
        for (int i = 0; i < originalData.length; ++i) {
            rc.append(HEX_TABLE[255 & data[i]]);
        }
        return rc.toString();
    }

    /**
     * // 十六进制转化为十进制，结果140
     */
    public static Integer hex2Dec(String string){

        // 十六进制转化为十进制，结果140。
        Integer num=Integer.parseInt(string,16);
        return num;
    }

    /**
     * // 十进制转化为十六进制
     */
    public static String dec2Hex(Integer num){
        // 十进制转化为十六进制，结果为C8。
        String string = Integer.toHexString(num);

        return string;
    }

    public static void main(String[] args) {
        //System.out.println(hex2Dec("7f8c"));
        //System.out.println(dec2Hex(200));
        //handleStr2ArrStr("7b1012012b172e052302434007e20a0a012a21a0");

        Integer[] integers = handleStr2ArrInt(handleStr2ArrStr("5a5ab02a04100200d8041102029c021702fdf4021802ffff0319020000012002004605120400000000f9"));
        for (Integer integer : integers) {
            System.out.println(Integer.toHexString(integer));
        }

    }

    public static String replaceAction(String userName) {
        String userNameAfterReplaced = "";
        int nameLength = userName.length();
        if(nameLength<3 && nameLength>0){
            if(nameLength==1){
                userNameAfterReplaced = "*";
            }else{
                userNameAfterReplaced = userName.replaceAll(userName, "^.{1,2}");
            }
        }else{
            Integer num1,num2,num3;
            num2=(new Double(Math.ceil(new Double(nameLength)/3))).intValue();
            num1=(new Double(Math.floor(new Double(nameLength)/3))).intValue();
            num3=nameLength-num1-num2;
            String star= StringUtils.repeat("*",num2);
            userNameAfterReplaced = userName.replaceAll("(.{"+num1+"})(.{"+num2+"})(.{"+num3+"})","$1"+star+"$3");
        }
        return userNameAfterReplaced;
    }
}
