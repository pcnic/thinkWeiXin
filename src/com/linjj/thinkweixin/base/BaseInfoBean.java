/**
* Copyright 2004 MapleBook Sci.&Tech.Co.,Ltd
* All right reserved.
* 提供基本数据信息读取的模块
* V1.0.0   ZRJ    2004-09-17  Created
*/
package com.linjj.thinkweixin.base;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.linjj.thinkweixin.common.MyDateBean;
import com.linjj.thinkweixin.db.DBPoolManage;
import com.linjj.thinkweixin.db.DBSql;
import com.linjj.thinkweixin.db.Result;
import com.linjj.thinkweixin.util.ExtString;
import com.linjj.thinkweixin.util.strFormat;

public class BaseInfoBean {

	public BaseInfoBean() {

	}

	// ============================================================================
	/**
	 * 获取序列号
	 * 
	 * @param1 sTableName 表名
	 * @retrun 序列号
	 */
	public synchronized static String getSequenceId(String sTableName) {
		/* 设置查询Sql */
		String sSql = "select nvl(Max(SEQVALUE),0)+1 from Z_SEQUENCE where Upper(TNAME)='"
				+ sTableName.toUpperCase() + "'";
		String sPKId = "-1";
		Result result = (new DBSql()).executeSql(sSql);

		/* 判断更新数据 */
		if (result.iErrorCode == 0 && result.aaMultiLinesOut != null) {
			sPKId = result.aaMultiLinesOut[0][0];
			if (sPKId.equals("1")) { // 数据库中没有记录,则插入
				sSql = "insert into Z_SEQUENCE(SEQVALUE,TNAME) Values(" + sPKId
						+ ",'" + sTableName.toUpperCase() + "');";
			} else { // 数据库中已有记录,则更新
				sSql = "update Z_SEQUENCE set SEQVALUE=" + sPKId
						+ " where Upper(TNAME)='" + sTableName.toUpperCase()
						+ "'";
			}
			result = (new DBSql()).executeSql(sSql);
			if (result.iErrorCode != 0) {
				sPKId = "-1";
			}
		}
		return sPKId;
	}

	/**
	 * 写入日志
	 * 
	 * @param1 modCode 模块编码
	 * @param2 oprType 操作类型
	 * @param1 logMsg 日志信息
	 * @param2 request 网页信息
	 * @retrun 序列号
	 */
	public static String writeToOprLog(String modCode, String oprType,
			String logMsg, String empCode) {
		if (logMsg == null || logMsg.trim().equals("")) {
			return "0";
		}
		// 判断内容
		if (logMsg.getBytes().length > 500)
			logMsg = logMsg.substring(0,
					499 - (logMsg.getBytes().length - logMsg.length()));

		/* 设置查询Sql */
		String sSql = "";
		String OPRID = "-1";
		Result result = null;

		MyDateBean systemDate = new MyDateBean();
		String sSysDate = systemDate.Format("yyyy-MM-dd HH:mm:ss");
		/* 判断更新数据 */
		try {
			OPRID = getSequenceId("Z_OPRLOG");
			sSql = "insert into Z_OPRLOG(OPRID,MODCODE,OPRTYPE,OPRIP,EMPCODE,OPRTIME,DETAIL) Values("
					+ OPRID
					+ ",'"
					+ modCode
					+ "','"
					+ oprType
					+ "','127.0.0.1','"
					+ empCode
					+ "','"
					+ sSysDate
					+ "','"
					+ strFormat.toSql(logMsg) + "');";
			result = (new DBSql()).executeSql(sSql);
			if (result.iErrorCode != 0) {
				OPRID = "-1";
			}
		} catch (Exception e) {
			return "-1";
		}
		return OPRID;
	}

	/**
	 * 写入日志
	 * 
	 * @param1 modCode 模块编码
	 * @param2 oprType 操作类型
	 * @param1 logMsg 日志信息
	 * @param2 request 网页信息
	 * @retrun 序列号
	 */
	public static String writeToOprLog(String modCode, String oprType,
			String logMsg, HttpServletRequest request) {
		if (logMsg == null || logMsg.trim().equals("")) {
			return "0";
		}
		// 判断内容
		String oprIp = request.getRemoteAddr();
		if (logMsg.getBytes().length > 500){
			if(logMsg.getBytes().length - logMsg.length()>250){
				logMsg = logMsg.substring(0,247)+"...";
			}else{
				logMsg = logMsg.substring(0,
						499 - (logMsg.getBytes().length - logMsg.length()));
			}
		}

		/* 设置查询Sql */
		String sSql = "";
		String OPRID = "-1";
		Result result = null;
		Map userLogin = (Map) request.getSession().getAttribute("userLogin");
		if (userLogin == null) {
			request.setAttribute("ERROR_MESSAGE", "操作失败，原因：用户未登录，未取到用户信息");
			return "-1";
		}

		MyDateBean systemDate = new MyDateBean();
		String sSysDate = systemDate.Format("yyyy-MM-dd HH:mm:ss");
		/* 判断更新数据 */
		try {
			OPRID = getSequenceId("Z_OPRLOG");
			sSql = "insert into Z_OPRLOG(OPRID,MODCODE,OPRTYPE,OPRIP,EMPCODE,OPRTIME,DETAIL) Values("
					+ OPRID
					+ ",'"
					+ modCode
					+ "','"
					+ oprType
					+ "','"
					+ oprIp
					+ "','"
					+ ExtString.turnStr((String) userLogin.get("EMPCODE"))
					+ "','"
					+ sSysDate
					+ "','"
					+ strFormat.toSql(logMsg)
					+ "');";
			result = (new DBSql()).executeSql(sSql);
			if (result.iErrorCode != 0) {
				OPRID = "-1";
				request.setAttribute("ERROR_MESSAGE", "操作失败，原因2："
						+ result.sErrorDetail);
			}
		} catch (Exception e) {
			request.setAttribute("ERROR_MESSAGE", "操作失败，原因3：" + e.toString());
			return "-1";
		}
		return OPRID;
	}

	/**
	 * 写入日志
	 * 
	 * @param1 modCode 模块编码
	 * @param2 oprType 操作类型
	 * @param1 logMsg 日志信息
	 * @param2 session 网页信息
	 * @retrun 序列号
	 */
	public static String writeToOprLog(String modCode, String oprType,
			String logMsg, HttpSession session) {
		if (logMsg == null || logMsg.trim().equals("")) {
			return "0";
		}
		if (session == null)
			return "-1";
		// 判断内容
		if (logMsg.getBytes().length > 500)
			logMsg = logMsg.substring(0,
					499 - (logMsg.getBytes().length - logMsg.length()));

		/* 设置查询Sql */
		String sSql = "";
		String OPRID = "-1";
		Result result = null;
		Map userLogin = (Map) session.getAttribute("userLogin");
		if (userLogin == null) {
			return "-1";
		}

		MyDateBean systemDate = new MyDateBean();
		String sSysDate = systemDate.Format("yyyy-MM-dd HH:mm:ss");
		String oprIp = userLogin.get("IP").toString();
		/* 判断更新数据 */
		try {
			OPRID = getSequenceId("Z_OPRLOG");
			sSql = "insert into Z_OPRLOG(OPRID,MODCODE,OPRTYPE,OPRIP,EMPCODE,OPRTIME,DETAIL) Values("
					+ OPRID
					+ ",'"
					+ modCode
					+ "','"
					+ oprType
					+ "','"
					+ oprIp
					+ "','"
					+ ExtString.turnStr((String) userLogin.get("EMPCODE"))
					+ "','"
					+ sSysDate
					+ "','"
					+ strFormat.toSql(logMsg)
					+ "');";
			result = (new DBSql()).executeSql(sSql);
			if (result.iErrorCode != 0) {
				OPRID = "-1";
				System.out.println("操作失败，原因2：" + result.sErrorDetail);
			}
		} catch (Exception e) {
			System.out.println("操作失败，原因3：" + e.toString());
			return "-1";
		}
		return OPRID;
	}

	// 使用说明:str要加密的字符串 bEncry :true时为加密 false为解密
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
	 * 删除文件
	 * 
	 * @param sFile
	 *            String 带全路径的文件名
	 * @return boolean
	 */
	public static boolean DeleteFile(String sFile) {
		try {
			java.io.File file = new java.io.File(sFile);
			if (file.exists())
				return file.delete();
			else
				return true;
		} catch (Exception e) {
			if (DBPoolManage.bDebug)
				System.err.println("删除文件不成功,文件:" + sFile);
			return false;
		}
	}

	/**
	 * 创建目录
	 * 
	 * @param path
	 *            String
	 * @return String
	 */
	public static String Mkdir(String path) {
		String msg = "";
		java.io.File dir;

		// 新建文件对象
		dir = new java.io.File(path);
		if (dir == null) {
			msg = "错误原因:＜BR＞对不起，不能创建空目录！";
			return msg;
		}
		if (dir.isFile()) {
			msg = "错误原因:＜BR＞已有同名文件＜B＞" + dir.getAbsolutePath() + "＜/B＞存在。";
			return msg;
		}
		if (!dir.exists()) {
			boolean result = dir.mkdirs();
			if (result == false) {
				msg = "错误原因:＜BR＞目录＜b＞" + dir.getAbsolutePath()
						+ "＜/B＞创建失败，原因不明！";
				return msg;
			}
			// 同时创建一个index.html文件,使它不会被直接查看
			if (DBPoolManage.bDebug)
				System.err.println("在目录下写index.html文件:" + path);
			WriteFile(path + "/index.html",
					"<html>\r<head>\r</head>\r<body>\r</body>\r</html>");
			return msg;
		}
		return msg;
	}

	/**
	 * 得到文件名
	 * 
	 * @param pSer
	 *            String //通过系列号得到的数据
	 * @param pAdd
	 *            String //附加文件名
	 * @return String //返回固定格式的文件名
	 */
	public static String GetFileName(String pSer, String pAdd) {
		switch (pSer.length()) {
		case 1:
			return pAdd + "000000000" + pSer;
		case 2:
			return pAdd + "00000000" + pSer;
		case 3:
			return pAdd + "0000000" + pSer;
		case 4:
			return pAdd + "000000" + pSer;
		case 5:
			return pAdd + "00000" + pSer;
		case 6:
			return pAdd + "0000" + pSer;
		case 7:
			return pAdd + "000" + pSer;
		case 8:
			return pAdd + "00" + pSer;
		case 9:
			return pAdd + "0" + pSer;
		default:
			return pSer;
		}
	}

	/**
	 * 取文件后缀
	 * 
	 * @param pFile
	 *            String
	 * @return String
	 */
	public static String GetFileExe(String pFile) {
		if (!pFile.equals("") && (pFile.lastIndexOf('.') != -1))
			return pFile.substring(pFile.lastIndexOf('.'));
		else
			return "";
	}

	/**
	 * 显示HINT
	 * 
	 * @param pFile
	 *            String
	 * @return String
	 */
	public static String GetHint(String pStr, int pNum) {
		if ((pStr == null) || pStr.equals(""))
			return "";

		String lStr = ExtString.replace(pStr, "\r\n", "");

		String lStr1 = "";
		int i = 1;
		while (true) {
			if ((i * pNum < lStr.length())
					&& ((lStr.length() - (i - 1) * pNum) >= pNum)) {
				lStr1 = lStr1 + lStr.substring((i - 1) * pNum, i * pNum)
						+ "\r\n";
			} else
				break;
			i++;
		}
		lStr1 = lStr1 + lStr.substring((i - 1) * pNum, lStr.length());
		return lStr1;
	}

	/**
	 * 把内容写到文件里
	 * 
	 * @param pFile
	 *            String
	 * @param pContent
	 *            String
	 * @return boolean
	 */
	public static boolean WriteFile(String pFile, String pContent) {
		try {
			java.io.File file = new java.io.File(pFile);
			file.createNewFile();
			try {
				byte pData[] = pContent.getBytes();
				FileOutputStream fileOut = new FileOutputStream(file);
				fileOut.write(pData, 0, pData.length);
				fileOut.close();
			} catch (Exception e) {
				if (DBPoolManage.bDebug)
					System.err.println("写文件出错=" + pFile);
				return false;
			}
		} catch (IOException e) {
			if (DBPoolManage.bDebug)
				System.err.println("打开文件出错=" + pFile);
			return false;
		}
		return true;
	}

	/**
	 * 从文件里读数据
	 * 
	 * @param pFile
	 *            String
	 * @return String
	 */
	public static String ReadFile(String pFile) {
		String lContent = "";
		java.io.File file = new java.io.File(pFile);
		try {
			byte[] pData = new byte[(int) file.length()];
			FileInputStream fileIn = new FileInputStream(file);
			fileIn.read(pData, 0, (int) file.length());
			lContent = new String(pData);
			fileIn.close();
		} catch (Exception e) {
			if (DBPoolManage.bDebug)
				System.err.println("读文件出错=" + pFile);
			return lContent;
		}

		return lContent;
	}

	// 取得本机的IP地址
	public static InetAddress getLocalIP() {
		InetAddress LocalIP = null;
		try {
			LocalIP = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
		}
		return (LocalIP);
	}

	// 取得 远程主机 的IP地址
	public static InetAddress getServerIP(String pServer) {
		InetAddress ServerIP = null;
		try {
			ServerIP = InetAddress.getByName(pServer);
		} catch (UnknownHostException e) {
		}
		return (ServerIP);
	}

	/**
	 * 由于内网和外网IP的区别,要根据实登陆情况进行转换
	 * 
	 * @param pVal
	 *            String
	 * @return String
	 */
	public static String ChangeIP(HttpServletRequest request, String pVal) {
		// 内容
		String lVal = pVal;
		// 如果是外网,则地址为www.sunnada.net 218.5.2.232
		InetAddress lAddress = getLocalIP();
		if (lAddress == null)
			return lVal;
		// 得到登陆IE的地址
		String lIP = request.getServerName();
		// 服务端IP和机器名
		String lIP1 = lAddress.getHostAddress();
		String lHo1 = lAddress.getHostName();
		// 客户端IP
		String lIP2 = request.getRemoteAddr();
		// 外网登陆
		if (lIP.equalsIgnoreCase("www.sunnada.net")
				|| lIP.equalsIgnoreCase("218.5.2.232")) {
			// 把127.0.0.1的转化为外网地址
			lVal = ExtString.replace(lVal, "127.0.0.1", "www.sunnada.net");
			// 把localhost的转化为外网地址
			lVal = ExtString.replace(lVal, "localhost", "www.sunnada.net");
			// 把本机IP转化为外网地址
			lVal = ExtString.replace(lVal, lIP1, "www.sunnada.net");
			// 把本机机器名转化为外网地址
			lVal = ExtString.replace(lVal, lHo1, "www.sunnada.net");
		}
		// 内网登陆
		else {
			// 本机等陆
			if (lIP1.equalsIgnoreCase(lIP2)) {
				lVal = ExtString.replace(lVal, "www.sunnada.net", "127.0.0.1");
				lVal = ExtString.replace(lVal, "218.5.2.232", "127.0.0.1");
			}
			// 其他机登陆
			else {
				lVal = ExtString.replace(lVal, "www.sunnada.net", lIP1);
				lVal = ExtString.replace(lVal, "218.5.2.232", lIP1);
				// 把127.0.0.1的转化为外网地址
				lVal = ExtString.replace(lVal, "127.0.0.1", lIP1);
				// 把localhost的转化为外网地址
				lVal = ExtString.replace(lVal, "localhost", lIP1);
			}
		}
		return lVal;
	}

	/**
	 * 
	 * @param eventType
	 *            String
	 * @return Map
	 */
	public static Map getSqlStrFromXmlFile(String eventType) {
		Map result = new HashMap();
		File V_TempFile = null;
		Document V_Document = null;
		Element rootEle = null;
		String P_SystemConfigFile = "dbcfn.xml";
		NodeList subNodeList = null;
		NodeList children = null;
		Node V_Node = null;
		StringBuffer sqlStrBuf = new StringBuffer();
		String sqlStr = "";
		List titleList = new ArrayList();// 每一维放中文名称，第二维放长度
		String prefix = "";
		String fieldName = "";
		String name = "";
		NamedNodeMap attMap = null;
		try {
			System.err.println("eventType in xml=" + eventType);
			DocumentBuilderFactory V_Factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder V_Builder = V_Factory.newDocumentBuilder();
			V_TempFile = new File(P_SystemConfigFile);
			if (!V_TempFile.canRead()) {
				result.put("error", "无法读取系统配置文件:" + P_SystemConfigFile);
				return result;
			}
			V_Document = V_Builder.parse(P_SystemConfigFile);
			rootEle = V_Document.getDocumentElement();
			V_Node = rootEle.getFirstChild();
			while (V_Node != null) {
				if (!V_Node.getNodeName().equalsIgnoreCase("field")) {
					V_Node = V_Node.getFirstChild();
				} else {
					V_Node = V_Node.getNextSibling();
				}
				if (V_Node != null)
					System.err.println("NodeName=" + V_Node.getNodeName());
			}
			// V_Document.getElementsByTagName()
			// 得到相应的工作流定义结点
			subNodeList = V_Document.getDocumentElement().getElementsByTagName(
					eventType.toLowerCase());
			// subNodeList.item(0).
			// 构造select的sql语句
			if (subNodeList.getLength() <= 0) {
				result.put("error", "没有找到对应的工作流节点:" + eventType);
				return result;
			}
			// 得到列结点上的字段列表
			children = subNodeList.item(0).getFirstChild().getChildNodes();
			if (children.getLength() == 0) {
				result.put("error", "没有找到对应的工作流节点的选择列信息" + eventType);
				return result;
			}

			sqlStr = "Select ";
			for (int i = 0; i < children.getLength() - 1; i++) {
				V_Node = children.item(i);
				System.err.println("V_Node=" + V_Node);
				// if (V_Node.getNodeType())
				prefix = ExtString.toChinese(V_Node.getChildNodes().item(0)
						.getNodeValue());
				name = ExtString.toChinese(V_Node.getChildNodes().item(1)
						.getNodeValue());
				fieldName = ExtString.toChinese(V_Node.getChildNodes().item(2)
						.getNodeValue());
				titleList.add(ExtString.toChinese(V_Node.getChildNodes()
						.item(3).getNodeValue()));
				titleList.add(ExtString.toChinese(V_Node.getChildNodes()
						.item(4).getNodeValue()));
				if (fieldName.compareTo("") == 0) {
					sqlStr = sqlStr + prefix + "." + name + ",";
				} else {
					sqlStr = sqlStr + prefix + "." + fieldName + " As " + name
							+ ",";
				}
				// V_Node = V_Node.getNextSibling();
				sqlStrBuf.append(sqlStr.substring(1, sqlStr.length() - 1));
			}
			sqlStr = " From ";
			System.err.println("Select Sql Str=" + sqlStr);
			// 构造From的sql语句
			V_Node = subNodeList.item(1).getFirstChild();
			while (V_Node != null) {
				attMap = V_Node.getAttributes();
				System.err.println("attMap=" + attMap);
				if (attMap != null) {
					name = ExtString.toChinese(attMap.getNamedItem("name")
							.getNodeValue());
					prefix = ExtString.toChinese(attMap.getNamedItem("prefix")
							.getNodeValue());
					sqlStr = sqlStr + name + " " + prefix + ",";
				}
				V_Node = V_Node.getNextSibling();
			}
			System.err.println("From Sql Str=" + sqlStr);
			sqlStrBuf.append(sqlStr.substring(1, sqlStr.length() - 1));
			// 构造关联sql语句
			sqlStr = " where 1=1 ";
			if (subNodeList.item(3) != null) {
				V_Node = subNodeList.item(3).getFirstChild();
				while (V_Node != null) {
					attMap = V_Node.getAttributes();
					System.err.println("attMap=" + attMap);
					if (attMap != null) {
						name = ExtString.toChinese(attMap.getNamedItem(
								"leftField").getNodeValue());
						fieldName = ExtString.toChinese(attMap.getNamedItem(
								"rightField").getNodeValue());
						prefix = ExtString.toChinese(attMap.getNamedItem(
								"ref-optional").getNodeValue());
						if (prefix.toLowerCase().compareTo("true") == 0) {
							sqlStr = sqlStr + "and " + name + "=" + fieldName
									+ prefix + " ";
						} else
							sqlStr = sqlStr + "and " + name + "=" + fieldName
									+ " ";
					}
					V_Node = V_Node.getNextSibling();
				}
				if (DBPoolManage.bDebug)
					System.err.println("View-link Sql Str=" + sqlStr);
				sqlStrBuf.append(sqlStr.substring(1, sqlStr.length() - 1));
			}
			result.put("sqlStr", sqlStrBuf.toString());
			result.put("titleList", titleList);
		} catch (Exception e) {
			result.put("error", "操作出错原因:" + e.toString());
			System.err.println("操作出错原因:" + e.toString());
		}
		return result;
	}

}
