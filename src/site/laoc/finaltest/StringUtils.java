package site.laoc.finaltest;

public class StringUtils {
    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};


    /**
     * byte数组转16进制数据
     * @param bytes
     * @return
     */
    public String bytesToHex(byte[] bytes) {
        // 一个byte为8位，可用两个十六进制位标识
        char[] buf = new char[bytes.length * 2];
        int a = 0;
        int index = 0;
        for(byte b : bytes) { // 使用除与取余进行转换
            if(b < 0) {
                a = 256 + b;
            } else {
                a = b;
            }

            buf[index++] = HEX_CHAR[a / 16];
            buf[index++] = HEX_CHAR[a % 16];
        }

        return new String(buf);
    }

    /**
     * 16进制字符串转byte数组
     * @param hexString
     * @return
     */
    public byte[] hexStringToBytes(String hexString) {
        if(hexString == null || hexString.trim().equals("")) {
            return new byte[0];
        }

        byte[] bytes = new byte[hexString.length() / 2];
        for(int i = 0; i < hexString.length() / 2; i++) {
            String subStr = hexString.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr,16);
        }

        return bytes;
    }

    /**
     * 字符串转10进制int
     * @param str
     * @return
     */
    private Integer strToInt(String str){
        Integer bi = Integer.parseInt(str,16);

        return bi;
    }

    /**
     * 将字符串填满至8个字符，前面填0
     * @param bin
     * @return
     */
    private String appendZeroToEight(String bin){
        int len = 8 - bin.length();

        for(int i = 0;i < len;i++){
            bin = "0" + bin;
        }

        return bin;
    }


    /**
     * 二进制字符串转16进制字符串
     * @param hexString
     * @return
     */
    public String hexToBinaryString(String hexString){
        //判空
        if(hexString == null || hexString.length() == 0) {
            return null;
        }

        //合法性校验
        if(!hexString.matches("[a-fA-F0-9]*") || hexString.length() % 2 != 0) {
            return null;
        }

        StringBuffer sb = new StringBuffer();

        //计算
        int mid = hexString.length() / 2;

        for (int i = 0; i < mid; i++) {
            Integer tmp = strToInt(hexString.substring(i * 2, i * 2 + 2));
            String bin = Integer.toBinaryString(tmp);

            sb.append(appendZeroToEight(bin));
        }

        return sb.toString();
    }



}
