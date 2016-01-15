/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudserver.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 字符串工具类。
 * 
 * @author lidejun
 * @since 2013-07-17
 */
public abstract class StringUtils {

    private StringUtils() {
    }


    /**
     * 检查指定的字符串是否为空。
     * <ul>
     * <li>SysUtils.isEmpty(null) = true</li>
     * <li>SysUtils.isEmpty("") = true</li>
     * <li>SysUtils.isEmpty(" ") = true</li>
     * <li>SysUtils.isEmpty("abc") = false</li>
     * </ul>
     * 
     * @param value
     *            待检查的字符串
     * @return true/false
     */
    public static boolean isEmpty(String value) {
        int strLen;
        if (value == null || (strLen = value.length()) == 0) {
            return true;
        }
        if("null".equals(value)){
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(value.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 判断Long类型是否有值
     * @param value
     * @return
     */
    public static boolean isEmpty(Long value){
        return (value == null || value <= 0);
    }


    /**
     * 检查对象是否为数字型字符串,包含负数开头的。
     */
    public static boolean isNumeric(Object obj) {
        if (obj == null) {
            return false;
        }
        char[] chars = obj.toString().toCharArray();
        int length = chars.length;
        if (length < 1) {
            return false;
        }

        int i = 0;
        if (length > 1 && chars[0] == '-') {
            i = 1;
        }

        for (; i < length; i++) {
            if (!Character.isDigit(chars[i])) {
                return false;
            }
        }
        return true;
    }


    /**
     * 检查指定的字符串列表是否不为空。
     */
    public static boolean areNotEmpty(String... values) {
        boolean result = true;
        if (values == null || values.length == 0) {
            result = false;
        }
        else {
            for (String value : values) {
                result &= !isEmpty(value);
            }
        }
        return result;
    }


    /**
     * 把通用字符编码的字符串转化为汉字编码。
     */
    public static String unicodeToChinese(String unicode) {
        StringBuilder out = new StringBuilder();
        if (!isEmpty(unicode)) {
            for (int i = 0; i < unicode.length(); i++) {
                out.append(unicode.charAt(i));
            }
        }
        return out.toString();
    }


    /**
     * @since 将userId 转成 user_id
     * @param name
     * @return
     */
    public static String toUnderlineStyle(String name) {
        StringBuilder newName = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i > 0) {
                    newName.append("_");
                }
                newName.append(Character.toLowerCase(c));
            }
            else {
                newName.append(c);
            }
        }
        return newName.toString();
    }


    /**
     * @since 将user_id 转成 userId
     * @param name
     * @return
     */
    public static String toUpperCaseStyle(String name) {
        StringBuilder newName = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (i > 0 && c == '_') {
                if (i < (name.length() - 1)) {
                    newName.append(Character.toUpperCase(name.charAt(++i)));
                }
                else {
                    newName.append(c);
                }
            }
            else {
                newName.append(c);
            }
        }
        return newName.toString();
    }


    public static String convertString(byte[] data, int offset, int length) {
        if (data == null) {
            return null;
        }
        else {
            try {
                return new String(data, offset, length, "UTF-8");
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static byte[] convertBytes(String data) {
        if (data == null) {
            return null;
        }
        else {
            try {
                return data.getBytes("UTF-8");
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static long convertLong(String data) {
        if (data == null) {
            return 0;
        }
        else {
            try {
                return Long.parseLong(data);
            }
            catch (Exception e) {
                return 0;
            }
        }
    }


    public static int convertInt(String data) {
        if (data == null) {
            return 0;
        }
        else {
            try {
                return Integer.parseInt(data);
            }
            catch (Exception e) {
                return 0;
            }
        }
    }


    private static boolean isRegex(String regex, String value) {
        return Pattern.compile(regex).matcher(value).find();
    }


    /**
     * 检测是否为字母数字的组合
     * 
     * @param value
     * @return
     */
    public static boolean isLetterNumber(String value) {
        return isRegex("^[A-Za-z0-9]+$", value);
    }

    public final static int DEFAULT_BUFFER_SIZE = 1024 * 4;


    /**
     * 从流中读字符串
     * 
     * @param in
     * @return
     */
    public static String read(InputStream in) {
        InputStreamReader reader;
        try {
            reader = new InputStreamReader(in, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        return read(reader);
    }


    /**
     * 读取资源中的字符串
     * 
     * @param resource
     * @return
     * @throws IOException
     */
    public static String readFromResource(String resource) throws IOException {
        InputStream in = null;
        try {
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
            if (in == null) {
                in = StringUtils.class.getResourceAsStream(resource);
            }

            if (in == null) {
                return null;
            }

            String text = StringUtils.read(in);
            return text;
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (Exception e) {
                }
            }
        }
    }


    public static byte[] readByteArrayFromResource(String resource) throws IOException {
        InputStream in = null;
        try {
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
            if (in == null) {
                return null;
            }

            return readByteArray(in);
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (Exception e) {
                }
            }
        }
    }


    /**
     * 
     * @param input
     * @return
     * @throws IOException
     */
    public static byte[] readByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        return output.toByteArray();
    }


    /**
     * 将输入流拷贝到输出流
     * 
     * @param input
     * @param output
     * @return
     * @throws IOException
     */
    public static long copy(InputStream input, OutputStream output) throws IOException {
        final int EOF = -1;

        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];

        long count = 0;
        int n = 0;
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }


    public static String read(Reader reader) {
        try {

            StringWriter writer = new StringWriter();

            char[] buffer = new char[DEFAULT_BUFFER_SIZE];
            int n = 0;
            while (-1 != (n = reader.read(buffer))) {
                writer.write(buffer, 0, n);
            }

            return writer.toString();
        }
        catch (IOException ex) {
            throw new IllegalStateException("read error", ex);
        }
    }


    public static String read(Reader reader, int length) {
        try {
            char[] buffer = new char[length];

            int offset = 0;
            int rest = length;
            int len;
            while ((len = reader.read(buffer, offset, rest)) != -1) {
                rest -= len;
                offset += len;

                if (rest == 0) {
                    break;
                }
            }

            return new String(buffer, 0, length - rest);
        }
        catch (IOException ex) {
            throw new IllegalStateException("read error", ex);
        }
    }


    public static String getStackTrace(Throwable ex) {
        StringWriter buf = new StringWriter();
        ex.printStackTrace(new PrintWriter(buf));

        return buf.toString();
    }

    private static Date startTime;


    /**
     * 取系统运行的开始时间
     * 
     * @return
     */
    public final static Date getStartTime() {
        if (startTime == null) {
            startTime = new Date(ManagementFactory.getRuntimeMXBean().getStartTime());
        }
        return startTime;
    }


    /**
     * 生64位hash
     * 
     * @param text
     * @return
     */
    public static long murmurhash2_64(String text) {
        final byte[] bytes = text.getBytes();
        return murmurhash2_64(bytes, bytes.length, 0xe17a1465);
    }


    /**
     * murmur hash 2.0, The murmur hash is a relatively fast hash function from
     * http://murmurhash.googlepages.com/ for platforms with efficient
     * multiplication.
     * 
     * @author Viliam Holub
     */
    public static long murmurhash2_64(final byte[] data, int length, int seed) {
        final long m = 0xc6a4a7935bd1e995L;
        final int r = 47;

        long h = (seed & 0xffffffffl) ^ (length * m);

        int length8 = length / 8;

        for (int i = 0; i < length8; i++) {
            final int i8 = i * 8;
            long k = ((long) data[i8 + 0] & 0xff) //
                    + (((long) data[i8 + 1] & 0xff) << 8) //
                    + (((long) data[i8 + 2] & 0xff) << 16)//
                    + (((long) data[i8 + 3] & 0xff) << 24) //
                    + (((long) data[i8 + 4] & 0xff) << 32)//
                    + (((long) data[i8 + 5] & 0xff) << 40)//
                    + (((long) data[i8 + 6] & 0xff) << 48) //
                    + (((long) data[i8 + 7] & 0xff) << 56);

            k *= m;
            k ^= k >>> r;
            k *= m;

            h ^= k;
            h *= m;
        }

        switch (length % 8) {
        case 7:
            h ^= (long) (data[(length & ~7) + 6] & 0xff) << 48;
        case 6:
            h ^= (long) (data[(length & ~7) + 5] & 0xff) << 40;
        case 5:
            h ^= (long) (data[(length & ~7) + 4] & 0xff) << 32;
        case 4:
            h ^= (long) (data[(length & ~7) + 3] & 0xff) << 24;
        case 3:
            h ^= (long) (data[(length & ~7) + 2] & 0xff) << 16;
        case 2:
            h ^= (long) (data[(length & ~7) + 1] & 0xff) << 8;
        case 1:
            h ^= data[length & ~7] & 0xff;
            h *= m;
        }
        ;

        h ^= h >>> r;
        h *= m;
        h ^= h >>> r;

        return h;
    }


    public static byte[] md5Bytes(String text) {
        MessageDigest msgDigest = null;

        try {
            msgDigest = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("System doesn't support MD5 algorithm.");
        }

        msgDigest.update(text.getBytes());

        byte[] bytes = msgDigest.digest();

        return bytes;
    }


    /**
     * 生成MD5值
     * 
     * @param text
     * @return
     */
    public static String md5(String text) {
        byte[] bytes = md5Bytes(text);
        return HexCode.encode(bytes, false);
    }


    public static BigDecimal toBigDecimal(String value) {
        if (isEmpty(value)) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(value);
        }
        catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }


    public static BigDecimal toBigDecimal(String value, BigDecimal defvalue) {
        if (isEmpty(value)) {
            return defvalue;
        }
        try {
            return new BigDecimal(value);
        }
        catch (Exception e) {
            return defvalue;
        }
    }


    public static BigDecimal toBigDecimal(Long value) {
        if (value != null) {
            return new BigDecimal(value);
        }
        return null;
    }


    public static BigDecimal toBigDecimal(Long value, BigDecimal defvalue) {
        if (value != null) {
            return new BigDecimal(value);
        }
        return defvalue;
    }


    public static BigDecimal toBigDecimal(BigDecimal value, BigDecimal defvalue) {
        if (value != null) {
            return value;
        }
        return defvalue;
    }
    
    public static BigDecimal toBigDecimal(Object value, BigDecimal defvalue) {
        if (value == null) {
            return defvalue;
        }
        
        if(value instanceof BigDecimal){
        	return (BigDecimal)value;
        }
        
        try {
            return new BigDecimal(value.toString());
        }
        catch (Exception e) {
            return defvalue;
        }
    }
    
    public static BigDecimal toBigDecimal(Object value) {
        if (value == null) {
            return null;
        }
        
        if(value instanceof BigDecimal){
        	return (BigDecimal)value;
        }
        
        try {
            return new BigDecimal(value.toString());
        }
        catch (Exception e) {
            return null;
        }
    }


    public static Long toLong(String value) {
        if (isEmpty(value)) {
            return null;
        }
        try {
            return new Long(value);
        }
        catch (Exception e) {
            return null;
        }
    }


    public static Long toLong(String value, Long defvalue) {
        if (isEmpty(value)) {
            return defvalue;
        }
        try {
            return new Long(value);
        }
        catch (Exception e) {
            return defvalue;
        }
    }


    public static String toLong(Long value, String defvalue) {
        if (value != null) {
            return value.toString();
        }
        return defvalue;
    }


    public static String toLong(Long value) {
        if (value != null) {
            return value.toString();
        }
        return null;
    }


    public static Long toLong(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Long) {
            return (Long) value;
        }
        try {
            return new Long(value.toString());
        }
        catch (Exception e) {
            return null;
        }
    }


    public static Long toLong(Object value, Long defvalue) {
        if (value == null) {
            return defvalue;
        }
        if (value instanceof Long) {
            return (Long) value;
        }
        try {
            return new Long(value.toString());
        }
        catch (Exception e) {
            return defvalue;
        }
    }


    public static Boolean toBoolean(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        try {
            return new Boolean(value.toString());
        }
        catch (Exception e) {
            return null;
        }
    }


    public static Boolean toBoolean(Object value, Boolean defvalue) {
        if (value == null) {
            return defvalue;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        try {
            return new Boolean(value.toString());
        }
        catch (Exception e) {
            return defvalue;
        }
    }


    public static Date toDateTime(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Date) {
            return (Date) value;
        }
        try {
            return DateUtils.parseDate(value.toString(), "yyyy-MM-dd HH:mm:ss");
        }
        catch (Exception e) {
            return null;
        }
    }


    public static Integer toInteger(Long value, Integer defvalue) {
        if (value != null) {
            return value.intValue();
        }
        return defvalue;
    }


    public static Integer toInteger(Long value) {
        if (value != null) {
            return value.intValue();
        }
        return null;
    }


    public static Integer toInteger(Integer value, Integer defvalue) {
        if (value != null) {
            return value.intValue();
        }
        return defvalue;
    }


    public static Integer toInteger(Integer value) {
        if (value != null) {
            return value.intValue();
        }
        return null;
    }


    public static Integer toInteger(String value, Integer defvalue) {
        if (value != null && StringUtils.isNumeric(value)) {
            return new Integer(value);
        }
        return defvalue;
    }


    public static Integer toInteger(String value) {
        if (value != null && StringUtils.isNumeric(value)) {
            return new Integer(value);
        }
        return null;
    }
    
    public static Integer toInteger(Object value) {
        if (value != null && StringUtils.isNumeric(value)) {
            return new Integer(value.toString());
        }
        return null;
    }
    
    public static Integer toInteger(Object value, Integer defvalue) {
        if (value != null && StringUtils.isNumeric(value)) {
            return new Integer(value.toString());
        }
        return defvalue;
    }
    
    public static Double toDouble(Object value) {
        if (value != null && StringUtils.isNumeric(value)) {
            return new Double(value.toString());
        }
        return 0.0;
    }
    
    public static Double toDouble(Object value, Double defvalue) {
        if (value != null && StringUtils.isNumeric(value)) {
            return new Double(value.toString());
        }
        return defvalue;
    }


    public static String toArrayLong(List<Long> value) {
        String tmp = "";
        for (Long t : value) {
            if (t == null) {
                continue;
            }
            tmp = tmp.concat(t.toString() + ",");
        }
        if (tmp.length() > 0) {
            tmp = tmp.substring(0, tmp.length() - 1);
        }
        return tmp;
    }


    public static List<Long> toArrayLong(String value) {
        if (value != null) {
            String[] tmp = value.trim().split("\\,");
            if (tmp != null && tmp.length > 0) {
                ArrayList<Long> o = new ArrayList<Long>();
                for (String t : tmp) {
                    if (StringUtils.isNumeric(t.trim())) {
                        o.add(new Long(t.trim()));
                    }
                }
                return o;
            }
        }
        return null;
    }


    public static List<String> toArrayString(String value) {
        if (value != null) {
            String[] tmp = value.trim().split("\\,");
            if (tmp != null && tmp.length > 0) {
                ArrayList<String> o = new ArrayList<String>();
                for (String t : tmp) {
                    if (!StringUtils.isEmpty(t)) {
                        o.add(t.trim());
                    }
                }
                return o;
            }
        }
        return null;
    }


    public static String toArrayString(List<String> value) {
        String tmp = "";
        for (String t : value) {
            if (StringUtils.isEmpty(t)) {
                continue;
            }

            tmp = tmp.concat("'" + t.replace("'", "").replace("\"", "").toString() + "',");
        }
        if (tmp.length() > 0) {
            tmp = tmp.substring(0, tmp.length() - 1);
        }
        return tmp;
    }


    public static String toJsonString(String value) {
    	if (value == null || value.length() <= 0) {
    		return "";
    	}
    	return org.apache.commons.lang.StringEscapeUtils.escapeJava(value);
    }


    public static String toString(Object value) {
        if (value == null) {
            return "";
        }
        else {
            return value.toString().trim();
        }
    }
    
    /**
     * 用于字串拼接，如果参数为非String类型，最好重写toString
     * 
     * @param objs
     * @return
     */
    public static String join(Object... objs) {
        StringBuilder result = new StringBuilder();
        for (Object obj : objs) {
            result.append(obj == null ? "null" : obj.toString());
        }
        return result.toString();
    }
    
    public static String getRandomString(int length) {
        StringBuffer buffer =
                new StringBuffer("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        StringBuffer sb = new StringBuffer();
        Random r = new Random();
        int range = buffer.length();
        for (int i = 0; i < length; i++) {
            sb.append(buffer.charAt(r.nextInt(range)));
        }
        return sb.toString();
    }
    
    public static String getRandomInteger(int length) {
        StringBuffer buffer = new StringBuffer("0123456789");
        StringBuffer sb = new StringBuffer();
        Random r = new Random();
        int range = buffer.length();
        for (int i = 0; i < length; i++) {
            sb.append(buffer.charAt(r.nextInt(range)));
        }
        return sb.toString();
    }
    
    /**
     * 连接字符串，split参数作为分格符
     * 
     * @param split
     * @param arg
     * @return
     */
    public static String joinWithSplit(String split, Object[] arg) {
        StringBuilder sb = new StringBuilder();
        for (Object obj : arg) {
            String str = toString(obj);
            if(isEmpty(str)){
                continue;
            }
            sb.append(obj);
            sb.append(split);
        }
        if(sb.length() > 0){
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }
    
    /**
     * 字符串左边补齐
     * @param value
     * @param length
     * @return
     */
    public static String Lpad(Long value, int length){
        if(value == null){
            value = 0L;
        }
        String v = value +"";
        if(v.length()>=length){
            return v;
        } else {
            return String.format("%1$0"+(length-v.length())+"d",0)+value;
        }
    }
    
    /**
     * 重量校验
     * @param weight
     * @return
     */
    public static boolean isWeight(String weight) {
        Pattern p = Pattern.compile("^[0-9]{1,5}(\\.[0-9]{1,3})?$");
        Matcher m = p.matcher(weight);
        return m.matches();
    }
    
    /**
     * 金额校验
     * @param weight
     * @return
     */
    public static boolean isMoney(String money) {
        Pattern p = Pattern.compile("^[0-9]{1,6}(\\.[0-9]{1,2})?$");
        Matcher m = p.matcher(money);
        return m.matches();
    }
    
    /**
     * 数量校验
     * @param qty
     * @return
     */
    public static boolean isQty(String qty){
        Pattern p = Pattern.compile("^[0-9]{1,6}$");
        Matcher m = p.matcher(qty);
        return m.matches();
    }
    
    /**
     * 匹配是否为手机号
     */
    public static boolean isMobileNumber(String mobiles) {
        if(mobiles.startsWith("86")){
            mobiles = mobiles.substring(2);
        } else if(mobiles.startsWith("+86")){
            mobiles = mobiles.substring(3);
        }
        return testRegex("^1\\d{10}$", mobiles);
    }
    
    /**
     * 判断给定字串是否符合给定正则
     * 
     * @param regex
     * @param str
     * @return
     */
    public static boolean testRegex(String regex, String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        return m.matches();
    }
    
    public static String urlEncode(String str){
        try {
            return URLEncoder.encode(str, "UTF-8");
        }
        catch (Exception e) {
        }
        return str;
    }
    
    public static String urlDecode(String str){
        if(StringUtils.isEmpty(str)){
            return "";
        }
        try {
            return URLDecoder.decode(str, "UTF-8");
        }
        catch (Exception e) {
        }
        return str;
    }
    
}
