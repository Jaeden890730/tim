package com.mxic.oiplus.util;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.io.*;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.util.StringTokenizer;
import java.util.Vector;

//import com.mxic.tdsplus.ba.*;

public class StringUtil {

    public StringUtil() {
    }

    public static void main(String[] args) {

    }

    public static String SqlInRange(String data) {
        String tmp = null;
        int j = data.lastIndexOf(",");
        tmp = data.substring(0, j);
        return tmp;
    }


    public static String strFormat(String str, int len) {
        StringBuffer bf = new StringBuffer(str);
        for (int i = bf.length(); i < len; i++) {
            bf.append("z");
        }
        return bf.toString();
    }


    public static String replace(String orig, String strReplace, String strWith,
                                 boolean all) {
        if (orig == null || strReplace == null || strReplace.length() == 0) {
            throw new IllegalArgumentException("Null or zero-length arguments.");
        }
        StringBuffer buffOrig = new StringBuffer(orig);
        if (strWith == null) {
            strWith = "";
        }
        int i = 0;

        while (i + strReplace.length() <= buffOrig.length()) {
            if (buffOrig.substring(i,
                                   i + strReplace.length()).equals(strReplace)) {
                buffOrig.replace(i, i + strReplace.length(), strWith);
                if (!all) {
                    break;
                } else {
                    i += strWith.length();
                }
            } else {
                i++;
            }
        }
        return buffOrig.toString();
    }

    public static Object[] parse2Strings(String tobeparsed, String delim) {
        Vector rr = new Vector();
        if (tobeparsed == null) {
            tobeparsed = "";
        }
        StringTokenizer dd = new StringTokenizer(tobeparsed, delim);
        while (dd.hasMoreTokens()) {
            rr.add(dd.nextToken());
        }
        return rr.toArray();
    }

    public static String[] parse2StringsStr(String tobeparsed, String delim) {
        Vector rr = new Vector();
        if (tobeparsed == null) {
            tobeparsed = "";
        }
        StringTokenizer dd = new StringTokenizer(tobeparsed, delim);
        while (dd.hasMoreTokens()) {
            rr.add(dd.nextToken());
        }
        String[] tmp = new String[rr.size()];
        for (int i = 0; i < rr.size(); i++) {
            tmp[i] = (String) rr.get(i);
        }
        return tmp;
    }

    public static Object[] parse2Strings(String tobeparsed, String delim,
                                         boolean returnDelim) {
        Vector rr = new Vector();
        String token = "";
        if (tobeparsed == null) {
            tobeparsed = "";
        }
        int i = 0;
        int j = 0;
        int len = tobeparsed.length();
        while (len > 0) {
            j = tobeparsed.indexOf(delim, i);
            if (j < 0 && i <= len) {
                j = len;
                len = -1;
            }
            token = tobeparsed.substring(i, j);
            i = j + 1;
            rr.add(token);
        }
        return rr.toArray();
    }

    public static String FormatData(String[] data, String pattern) {
        StringBuffer tmp = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            if (!data[i].equals("")) {
                if (i > 0) {
                    tmp.append(pattern + data[i]);
                } else {
                    tmp.append(data[i]);
                }
            }
        }
        return tmp.toString();
    }

    public static String FormatData(String data) {
        StringTokenizer st = new StringTokenizer(data, "#");
        String tmp = "";
        while (st.hasMoreTokens()) {
            if (tmp.equals("")) {
                tmp = "'" + st.nextToken() + "'";
            } else {
                tmp = tmp + ",'" + st.nextToken() + "'";
            }
        }
        return tmp;
    }

    /*The following function
     is to withdraw the string
     before the character of "#" */

    public static String FormatData3(String data) {
        int i = data.indexOf("#");
        StringBuffer s = new StringBuffer();

        for (int j = 0; j < i; j++) {
            s.append(data.charAt(j));
        }
        return s.toString();
    }


    public static String FormatNum(double num, String pattern) {
        StringBuffer strbur = new StringBuffer();
        DecimalFormat nf = new DecimalFormat(pattern);
        FieldPosition fp = new FieldPosition(0);
        try {
            return nf.format(num, strbur, fp).toString();
        } catch (Exception e) {}
        return "0";
    }


    public static String FormatCrit(String data, String logic) {
        StringTokenizer st = new StringTokenizer(data, ";");
        String tmp = "";
        while (st.hasMoreTokens()) {
            if (tmp.equals("")) {
                tmp = st.nextToken();
            } else {
                tmp = tmp + logic + st.nextToken();
            }
        }
        return tmp;
    }

    public static String removeMilliSeconds(String date_string) {
        String new_date = "";
        try {
            if (date_string != null) {
                int len = date_string.lastIndexOf(".");
                if (len >= 0) {
                    new_date = date_string.substring(0, len);
                } else {
                    new_date = date_string;
                }
            }
        } catch (Exception e) {}
        return new_date;
    }

    public static String makeStackTrace(Exception exception) {
        StringWriter stringwriter = new StringWriter();
        exception.printStackTrace(new PrintWriter(stringwriter));
        return stringwriter.toString();
    }

    public static Object formatNull(Object format) {
        if (format == null || format.equals("null")) {
            return "";
        }
        return format;
    }

    public static int formatInt(Object format) {
        if (format == null || format.equals("null")) {
            return 0;
        }
        int num = 0;
        try {
            num = Integer.parseInt((String) format);
        } catch (Exception ex) {
            num = 0;
        }
        return num;
    }

    public static double formatDouble(Object format) {
        if (format == null || format.equals("null")) {
            return 0;
        }
        double num = 0;
        try {
            num = Double.parseDouble((String) format);
        } catch (Exception ex) {
            num = 0;
        }
        return num;
    }

    public static String formatRightLeft(Object format) {
        if (format == null || format.equals("null")) {
            return "right";
        }
        double num = 0;
        try {
            num = Double.parseDouble((String) format);
        } catch (Exception ex) {
            return "right";
        }
        return "left";
    }

    public static String Big5ToUtf8(String val) {
        try {
            if (val != null) {
                val = new String(val.getBytes("Big5"), "ISO8859_1");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            TDSLogger.println(ex.toString());
        } finally {
            return val;
        }
    }

    public static String Utf8ToBig5(String val) {
    	/*lai_mark-20100901 jsp(UTF8 format), don't transfer
        try {
            if (val != null) {
                val = new String(val.getBytes("ISO8859_1"), "Big5");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            TDSLogger.println(ex.toString());
        } finally {
            return val;
        }*/
        return val;
    }
    public static String Utf8ToBig5_new(String val) {
    	try {
            if (val != null) {
                val = new String(val.getBytes("ISO8859_1"), "Big5");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            TDSLogger.println(ex.toString());
        } finally {
            return val;
        }
        
    }

    public static String DoubleDigitNum(int i) {
        String tmp = null;
        if (i > 0 && i < 10) {
            tmp = "0" + String.valueOf(i);
        }else{
          tmp =  String.valueOf(i);
        }
        return tmp;
    }

    public static String NullConvert(String str) {
        if (str == null)
            return " ";
        else
            return str;
    }

    public synchronized static String unicodeToBig5(String s) {
        try {
            return new String(s.getBytes("Big5"), "ISO8859_1");
        } catch (UnsupportedEncodingException uee) {
            return s;
        }
    }

    public static String[] copySplitArray(String[] splitArray, int minLength)
    {
		String[] resultArray = new String[minLength];					
		System.arraycopy(splitArray, 0, resultArray, 0, splitArray.length);
		for (int i = splitArray.length; i < minLength; i++) {
			resultArray[i] = "";
		}
    	return resultArray;
    }
}
