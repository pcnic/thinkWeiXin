package com.linjj.thinkweixin.util;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/**
 * 提供对String对象进行扩展操作的类<br>
 * Title:提供对String对象进行扩展操作的类 Description: Copyright: Copyright (c) 2004
 * 
 * @author ZRJ 2004-09-17
 * @version 1.0.0.0
 * 
 */

public class ExtString {
	/**
	 * 提供替换字符串的功能,区分大小写
	 * 
	 * @param StringBuffer
	 *            sSrcStr 为要被处理的StringBuffer
	 * @param String
	 *            sStr 为被替换的子字符串
	 * @param String
	 *            sRepStr 为替换进去的子字符串
	 * @return 无
	 */
	public static void replace(StringBuffer sb, String sStr, String sRepStr) {
		try {
			if ((sb == null) || (sStr == null) || (sRepStr == null))
				return;
			if ((sb.length() == 0) || (sStr.length() == 0))
				return;
			int iStartIndex = 0;
			int iLen = sb.length();
			int iEndIndex = 0;
			int iLen2 = sStr.length();
			while (iStartIndex < iLen) {
				if (sb.substring(iStartIndex, iLen2 + iStartIndex).equals(sStr)) {
					sb.replace(iStartIndex, iLen2 + iStartIndex, sRepStr);
					iLen = sb.length();
					iStartIndex = iStartIndex + sRepStr.length();
				} else
					iStartIndex++;
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 提供替换字符串的功能,区分大小写
	 * 
	 * @param String
	 *            sSrcStr 为要被处理的字符串
	 * @param String
	 *            sStr 为被替换的子字符串
	 * @param String
	 *            sRepStr 为替换进去的子字符串
	 * @return String 替换后的字符串,出现异常返回原字符串
	 */
	public static String replace(String sSrcStr, String sStr, String sRepStr) {
		try {
			if ((sSrcStr == null) || (sStr == null) || (sRepStr == null))
				return sSrcStr;
			if ((sSrcStr.length() == 0) || (sStr.length() == 0))
				return sSrcStr;
			StringBuffer sb = new StringBuffer(sSrcStr);
			replace(sb, sStr, sRepStr);
			return new String(sb);
		} catch (Exception e) {
			return sSrcStr;
		}
	}

	/**
	 * 提供字符串分割的功能,区分大小写
	 * 
	 * @param String
	 *            sStr 为要被处理的源字符串
	 * @param String
	 *            sSplitStr 分割串或结束串
	 * @param String
	 *            sEscStr 为转义字符串
	 * @param int
	 *            iSplitnum 为要分割的个数
	 * @param int
	 *            flag
	 * 
	 * flag=1时: 当iSplitnum>0时，严格按其要求分割的个数分割，即最后被分割出来的可能还含有分割符（它们将被忽略） 当iSplitnum<=0时,sSplitStr被认为是结束符，即只有sSplitStr左边的有效。
	 * 
	 * flag=0表示sSplitStr为结束符 当iSplitnum<0时,sSplitStr被认为是结束符，即只有sSplitStr左边的有效。
	 * 当iSplitnum>=0时,sSplitStr被认为分割符，即只有sSplitStr左右边都有效。(但当sSplitStr在最后时候，右边无效)
	 * 
	 * @return 按要求分割后的字符数组，为null表示失败
	 */
	public static String[] split(String sStr, String sSplitStr, String sEscStr,
			int iSplitnum, int flag) {
		try {
			String[] aResult = null;
			Vector vResult = null;

			if (sSplitStr == null)
				sSplitStr = "";
			if (sSplitStr.length() == 0) {
				aResult = new String[1];
				aResult[0] = sStr;
				return aResult;
			}
			if (iSplitnum <= 0)
				iSplitnum = 0;
			if (sEscStr == null)
				sEscStr = "";

			if (iSplitnum > 0)
				aResult = new String[iSplitnum];
			else
				vResult = new Vector();

			int iNum = 0;
			int index = 0;
			int preindex = 0;
			int len = sStr.length();
			int iSplitlen = sSplitStr.length();
			int iEsclen = sEscStr.length();
			String sNosplit = sEscStr + sSplitStr;
			String sTemp = null;
			String sTemp2 = null;

			while (index < len) {
				try {
					sTemp = sStr.substring(index, iSplitlen + index);
				} catch (Exception e) {
					sTemp = "";
				}

				if (sTemp.equals(sSplitStr)) {
					sTemp2 = null;
					try {
						if (iEsclen == 0)
							sTemp2 = sStr.substring(preindex, index);
						else {
							if (index - iEsclen < 0)
								sTemp2 = sStr.substring(preindex, index);
							else if (!sStr.substring(index - iEsclen,
									iSplitlen + index).equals(sNosplit))
								sTemp2 = sStr.substring(preindex, index);
						}
					} catch (Exception e) {
						sTemp2 = "";
					}

					if (sTemp2 != null) {
						if (iEsclen > 0)
							sTemp2 = replace(sTemp2, sNosplit, sSplitStr);
						if (iSplitnum > 0)
							aResult[iNum] = sTemp2;
						else
							vResult.add(iNum, sTemp2);
						iNum++;
						if ((iNum >= iSplitnum) && (iSplitnum > 0))
							break;
						index = iSplitlen + index;
						preindex = index;

						if ((flag == 1) && (iSplitnum - iNum == 1)
								&& (index < len)) {
							sTemp2 = sStr.substring(index);
							if (iEsclen > 0)
								sTemp2 = replace(sTemp2, sNosplit, sSplitStr);
							if (iSplitnum > 0)
								aResult[iNum] = sTemp2;
							else
								vResult.add(iNum, sTemp2);
							iNum++;
							break;
						}
						continue;
					}
				}
				index++;
			}

			if (((flag == 0) && (preindex < index) && (iNum < iSplitnum))
					|| ((iSplitnum == 0) && (preindex < index))) {
				sTemp2 = sStr.substring(preindex, index);
				if (iEsclen > 0)
					sTemp2 = replace(sTemp2, sNosplit, sSplitStr);
				if (iSplitnum > 0)
					aResult[iNum] = sTemp2;
				else
					vResult.add(iNum, sTemp2);
				iNum++;
			}

			if (iNum == 0) {
				aResult = new String[1];
				aResult[0] = sStr;
				return aResult;
			}

			if (iNum < iSplitnum)
				for (int i = iNum; i < iSplitnum; i++)
					if (iSplitnum == 0)
						vResult.add(i, "");
					else
						aResult[i] = "";

			if (iSplitnum == 0) {
				aResult = new String[vResult.size()];
				for (int i = 0; i < aResult.length; i++)
					aResult[i] = (String) (vResult.elementAt(i));
			}

			return aResult;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 提供字符串分割的功能,区分大小写
	 * 
	 * @param String
	 *            sStr 为要被处理的源字符串
	 * @param String
	 *            sSplitStr 结束字符串
	 * @param String
	 *            sEscStr 为转义字符串
	 * @param int
	 *            iSplitnum 为要分割的个数
	 * @return 按要求分割后的字符数组，为null表示失败
	 */
	public static String[] split(String sStr, String sSplitStr, String sEscStr,
			int iSplitnum) {
		try {
			return split(sStr, sSplitStr, sEscStr, iSplitnum, 0);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 提供分割字符串的功能,区分大小写
	 * 
	 * @param String
	 *            sStr 为要被处理的源字符串
	 * @param String
	 *            sSplitStr 分割串
	 * @param String
	 *            sEscStr 为转义字符串
	 * @return 按要求分割后的字符数组，为null表示失败
	 */
	public static String[] split(String sStr, String sSplitStr, String sEscStr) {
		try {
			return split(sStr, sSplitStr, sEscStr, 0, 0);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 提供分割字符串的功能,区分大小写
	 * 
	 * @param String
	 *            sStr 为要被处理的源字符串
	 * @param String
	 *            sFieldStr 字段结束符
	 * @param String
	 *            sLineStr 行结束符
	 * @param String
	 *            sEscStr 为转义字符串
	 * @param int
	 *            iLinenum 行数
	 * @param int
	 *            iFieldnum 列数
	 * @return 按要求分割后的二维字符数组，为null表示失败
	 */
	public static String[][] split(String sStr, String sFieldStr,
			String sLineStr, String sEscStr, int iLinenum, int iFieldnum) {
		try {
			String[][] aaResult = new String[iLinenum][iFieldnum];

			String[] aTemp = split(sStr, sLineStr, sEscStr, iLinenum);
			if (aTemp.length != aaResult.length)
				return null;

			for (int i = 0; i < aaResult.length; i++)
				aaResult[i] = split(aTemp[i], sFieldStr, sEscStr, iFieldnum);

			if (aaResult[0].length != iFieldnum)
				return null;

			return aaResult;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 提供转换为中文字符串的功能
	 * 
	 * @param String
	 *            sStr 为要被处理的源字符串
	 * @return 中文字符串，为null表示失败
	 */

	public static String toChinese(String sStr) {
		try {
			/* TOMCAT中文问题已纠正，故在此处不再进行相应处理 */
			if (sStr == null)
				return "";
			byte[] bytes = sStr.getBytes("ISO-8859-1");
			String sTargetStr = new String(bytes, "utf-8");
			return turnStr(sTargetStr);

			// return turnStr(sStr);
		} catch (Exception ex) {
			return "";
		}
	}

	public static String toChineseUTF8(String sStr) {
		try {
			/* TOMCAT中文问题已纠正，故在此处不再进行相应处理 */
			if (sStr == null)
				return "";
			byte[] bytes = sStr.getBytes("GBK");
			String sTargetStr = new String(bytes, "UTF-8");
			return turnStr(sTargetStr);

			// return turnStr(sStr);
		} catch (Exception ex) {
			return "";
		}
	}

	/**
	 * 判断字符串是否为null，若是，返回“”，否则返回原字符串
	 * 
	 * @param s
	 *            String
	 * @return String
	 */
	public static String turnStr(String s) {
		if (s == null)
			return "";
		return s.trim();
	}

	public static int parseInt(String s) {
		if (s == "" || s.equals("") || s.length() == 0)
			return 0;
		else
			return Integer.parseInt(s);
	}

	/**
	 * 将字符转化为HTML输出格式
	 * 
	 * @param s
	 *            String
	 * @return String
	 */
	public static String transToHtml(String sSourStr) {
		sSourStr = replace(sSourStr, "\r\n", "<br>");
		sSourStr = replace(sSourStr, "\r", "<br>");
		sSourStr = replace(sSourStr, "\n", "<br>");
		return sSourStr;
	}

	/**
	 * 将字符串的回车进行处理
	 * 
	 * @param s
	 *            String
	 * @return String
	 */
	public static String FixedEnter(String sSourStr) {
		sSourStr = transToHtml(sSourStr);
		sSourStr = replace(sSourStr, "<br>", "\\n");
		return sSourStr;
	}

	/**
	 * 对字符串进行加密和解密
	 * 
	 * @param String
	 *            str 原字符串
	 * @param boolean
	 *            bEncry 加密或解密
	 * @return String
	 */

	public static String Encrypt(String str, boolean bEncry) {
		String result = "";
		try {
			byte[] strByte = str.getBytes();
			if (bEncry) {
				for (int i = 0; i < str.length(); i++) {
					if (strByte[i] <= 50)
						result = result + (char) (strByte[i] - 10);
					else if (strByte[i] > 50 && strByte[i] <= 90)
						result = result + (char) (strByte[i] + 30);
					else if (strByte[i] > 90 && strByte[i] <= 128)
						result = result + (char) (strByte[i] - 50);
					else if (strByte[i] > 128)
						result = result + (char) (strByte[i] - 6);
				}
			} else {
				for (int i = 0; i < str.length(); i++) {
					if (strByte[i] <= 40)
						result = result + (char) (strByte[i] + 10);
					else if (strByte[i] > 40 && strByte[i] <= 80)
						result = result + (char) (strByte[i] + 50);
					else if (strByte[i] > 80 && strByte[i] <= 120)
						result = result + (char) (strByte[i] - 30);
					else if (strByte[i] > 120)
						result = result + (char) (strByte[i] + 6);
				}
			}
		} catch (Exception e) {
			System.err.println("操作出错原因=" + e.toString());
			result = "-1";
		}
		// if(DBPoolManage.bDebug) System.err.println("Encrypt="+result);
		return result;
	}

	/**
	 * 当用户查询时，会输入一些特殊字符，这里对特殊字符做了转义操作 目前只对oracle进行转义 查询时特殊的字符有_ / % & ' " value
	 * 是要转义的字符串 return 是返回转义过的字符串 接着要在字符串连接的后面加上 escape '/' 进行转义 例如: String ab =
	 * "'/"; sql +" and name like '%"+SpecialChar(ab)+"%' escape '/' "
	 */
	public static String SpecialChar(String value) {
		if (value == null)
			return "";
		/**
		 * 第一步必须替换' 转义成 '' 第二步必须是/ 转义成 // 剩下的顺序随便 _ % & "
		 */
		value = value.trim();
		value = value.replaceAll("'", "''");
		value = value.replaceAll("/", "//");
		value = value.replaceAll("_", "/_");
		value = value.replaceAll("%", "/%");
		value = value.replaceAll("\"", "'||chr(34)||'");
		// & 的转义字符 ASCⅡ 是38 十进制的
		value = value.replaceAll("&", "'||chr(38)||'");
		return value;
	}

	/**
	 * 判断是否有特殊字符集
	 */
	public static boolean isSpecialChar(String value) {
		String rex = "[^'/_%&\"]*['|/|_|%|&|\"]+[^'/_%&\"]*";
		if (value == null || value.equals(""))
			return false;
		return value.matches(rex);
	}

	public static void main(String[] args) {
		System.out.println(Encrypt("@8G1Q'U", false));
	}

	/**
	 * 由于ext通过ajax传进来的代码都必须经过转码.这边实现一个工具方法用来进行转码.
	 * 
	 * @param request
	 * @param paramName
	 * @return
	 */
	public static String getParameter(HttpServletRequest request,
			String paramName) {
		String returnValue = "";
		if (request.getParameter(paramName) == null) {
			returnValue = "";
		} else {
			returnValue = request.getParameter(paramName);
			if ("XMLHttpRequest".equalsIgnoreCase(request
					.getHeader("x-requested-with"))) // 如果是通过ajax提交的
			{
				try {
					returnValue = new String(returnValue.getBytes("ISO-8859-1"),
							"UTF-8");
					returnValue = URLDecoder.decode(URLEncoder.encode(
							returnValue, "GB2312"), "GB2312");
				} catch (Exception e) {
					returnValue = "";
					;
				}
			}
		}
		return returnValue;
	}
	//处理UTF-8数据库
	public static String getUTF8Parameter(
			String returnValue) {
		
				try {
					returnValue = new String(returnValue.getBytes("ISO-8859-1"),
							"UTF-8");
					returnValue = URLDecoder.decode(URLEncoder.encode(
							returnValue, "GB2312"), "GB2312");
				} catch (Exception e) {
					returnValue = "";
					;
				}
			
		
		return returnValue;
	}
	/**
	 * 字符串处理 传一个字符串，查询这个字符串的第n个位置的下标
	 * 
	 * @param String
	 *            srcStr 源字符串
	 * @param String
	 *            subStr 目标字符串
	 * @param int
	 *            num 存在的第几个位置
	 * @return -2 这个位置字符串不存在，-1 没发现到字符串 n 在这个位置
	 */
	public static int indexOf(String srcStr, String subStr, int num) {

		// 为空的 当做不存在
		if (srcStr == null || subStr == null || num <= 0)
			return -2;
		// 获取存在的个数
		int index = getSubCount(srcStr, subStr);
		// 得到的个数小于等于0 没发现这个字符串
		if (index <= 0)
			return -1;
		// 得到的个数小于需要的位置
		if (index < num)
			return -2;
		Matcher m = Pattern.compile(subStr).matcher(srcStr);
		index = 0;
		while (m.find()) {
			index++;
			if (index == num) {
				index = m.start();
				break;
			}
		}
		return index;
	}

	/**
	 * 查找字串的个数
	 */
	public static int getSubCount(String srcStr, String subStr) {
		// 为空的判断
		if (srcStr == null || subStr == null)
			return 0;
		int count = 0;
		Matcher m = Pattern.compile(subStr).matcher(srcStr);
		while (m.find()) {
			count++;
			// System.out.println(m.start());
		}
		return count;
	}
	/**
	 * 判断字符串是否为空
	 * @date 2010-09-03
	 * @param obj
	 * @return
	 */
	
	public static boolean IsNullOrEmpty(String obj) {

		if (obj == null || "".equals(obj)) {
			return true;
		}
		return false;
	}
	
}
