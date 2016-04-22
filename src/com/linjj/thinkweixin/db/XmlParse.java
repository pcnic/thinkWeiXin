package com.linjj.thinkweixin.db;

import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;

/**
 * <p>
 * Title: 处理分析XML
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: salary
 * </p>
 * 
 * @author ZRJ 2004-09-17 created
 * @version 1.0.0.0
 */

public class XmlParse {

	private StringBuffer sDebug = new StringBuffer();
	private DataOutputStream out = null;
	private File file = null;

	public Document doc = null;
	public Element Root = null;
	public String ErrMsg = "";
	public String sFileName = "";

	public XmlParse() {
	}

	private String filtStr(NodeList children) {
		try {
			String sResult = "";
			int k = 0;
			int len = children.getLength();
			String stemp = "";

			while (k < len) {
				stemp = children.item(k).getNodeValue();
				k++;
				if (stemp != null)
					stemp = stemp.trim();
				else
					continue;
				if ((k == 1) && (stemp.equals("\r\n")))
					continue;
				if ((k == 1) && (stemp.equals("\r")))
					continue;
				if ((k == 1) && (stemp.equals("\n")))
					continue;

				if ((k == len) && (stemp.equals("\r\n")))
					break;
				if ((k == len) && (stemp.equals("\r")))
					break;
				if ((k == len) && (stemp.equals("\n")))
					break;
				sResult = sResult + stemp;
			}
			if (sResult == null)
				sResult = "";
			return sResult;
		} catch (Exception e) {
			return "";
		}
	}

	private void CloseFile() {
		try {
			if (out != null) {
				out.close();
				out = null;
			}
		} catch (Exception ex) {
			ErrMsg = "Close File Err" + ex.toString();
		}
	}

	/**
	 * 根据输入的字符串生成进行XML分析
	 * 
	 * @param sXmlStr
	 * @return
	 */
	public boolean bGetXML(String sXmlStr) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder DB = dbf.newDocumentBuilder();
			doc = DB.parse(new ByteArrayInputStream(sXmlStr.trim().getBytes()));
			Root = doc.getDocumentElement();
			sFileName = sXmlStr;
			return true;
		} catch (Exception ex) {
			ErrMsg = "Input XML String Err:" + ex.toString();
			return false;
		}
	}

	/**
	 * 根据输入的文件进行XML分析
	 * 
	 * @param FileName
	 * @return
	 */
	public boolean bGetXMLFile(String FileName) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder DB = dbf.newDocumentBuilder();
			doc = DB.parse(new File(FileName));

			Root = doc.getDocumentElement();
			sFileName = FileName;
			return true;
		} catch (Exception ex) {
			ErrMsg = "Open File Err:" + ex.toString();
			return false;
		}
	}

	/**
	 * 根据输入的文件进行XML分析
	 * 
	 * @param file
	 * @return
	 */
	public boolean bGetXMLFile(File file) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder DB = dbf.newDocumentBuilder();

			doc = DB.parse(file);
			Root = doc.getDocumentElement();
			sFileName = file.getName();
			return true;
		} catch (Exception ex) {
			ErrMsg = "Open File Err:" + ex.toString();
			return false;
		}
	}

	private void GetXmlTree(Node node, int iBlankNum) {
		try {
			if (node == null)
				return;
			if (node.getNodeType() != node.ELEMENT_NODE) {
				sDebug.append(node.getNodeValue());
				return;
			}
			for (int j = 0; j < iBlankNum; j++)
				sDebug.append(" ");
			sDebug.append("<" + node.getNodeName() + ">");
			NodeList children = node.getChildNodes();
			boolean bAddBlank = false;
			if (children != null) {
				int len = children.getLength();
				for (int i = 0; i < len; i++) {
					if ((children.item(i).getFirstChild() != null)
							|| (children.item(i).getNodeType() == Node.ELEMENT_NODE)) {
						if (!bAddBlank) {
							bAddBlank = true;
							sDebug.append("\r\n");
						}
						GetXmlTree(children.item(i), iBlankNum + 3);
					} else if ((children.item(i).getNodeValue() != null)
							&& (children.item(i).getNodeValue().trim().length() > 0)) {
						if (children.item(i).getNodeType() != Root.CDATA_SECTION_NODE) {
							if (children.item(i).getNodeValue().trim().equals(
									"\r\n"))
								continue;
							if (children.item(i).getNodeValue().trim().equals(
									"\n"))
								continue;
							if (children.item(i).getNodeValue().trim().equals(
									"\r"))
								continue;
						}
						if (len > 1) {
							if (!bAddBlank) {
								sDebug.append("\r\n");
								bAddBlank = true;
							}
							for (int j = 0; j < iBlankNum + 3; j++)
								sDebug.append(" ");
						}
						if (children.item(i).getNodeType() == Root.CDATA_SECTION_NODE)
							sDebug.append("<![CDATA[");
						if (children.item(i).getNodeType() == Root.CDATA_SECTION_NODE)
							sDebug.append(((CDATASection) (children.item(i)))
									.getData());
						else
							sDebug.append(children.item(i).getNodeValue());
						if (children.item(i).getNodeType() == Root.CDATA_SECTION_NODE)
							sDebug.append("]]>");
						if (len > 1)
							sDebug.append("\r\n");
					}
				}
			}
			if (bAddBlank)
				for (int j = 0; j < iBlankNum; j++)
					sDebug.append(" ");
			sDebug.append("</" + node.getNodeName() + ">\r\n");
		} catch (Exception ex) {
			ErrMsg = "Creat Xml Tree Err:" + ex.toString();
		}
	}

	/**
	 * 将当前的XML树保存成文件
	 * 
	 * @param FileName
	 * @return
	 */
	public boolean bWriteFile(String FileName) {
		try {
			ErrMsg = "";

			if (FileName == null)
				throw new Exception("FileName is null!");
			file = new File(FileName);
			out = new DataOutputStream(new FileOutputStream(file));
			sDebug = new StringBuffer();
			sDebug.append("<?xml version=\"1.0\" encoding=\"gb2312\" ?>\r\n");

			GetXmlTree(Root, 0);
			if (ErrMsg.length() > 0)
				return false;
			String ss = new String(sDebug);
			byte[] aByte = ss.getBytes("GB2312");

			out.write(aByte, 0, aByte.length);
			/*
			 * 和读出来一样的内容写进去 ss=Root.toString(); aByte=ss.getBytes("GB2312");
			 * out.write(aByte,0,aByte.length);
			 */

			CloseFile();
			return true;
		} catch (Exception ex) {
			ErrMsg = "Write XML File Err:" + ex.toString();
			return false;
		}
	}

	// 获取当前分析的XML文件字符串
	public String GetXmlStr() {
		sDebug = new StringBuffer();
		GetXmlTree(Root, 0);
		return new String(sDebug);
	}

	/**
	 * 根据输入的节点名称(节点名Name)获取它对应的第iNum个值
	 * 
	 * @param Name
	 * @param iNum
	 * @return
	 */
	public String sGetValueByName(String Name, int iNum) {
		try {
			NodeList NL = doc.getElementsByTagName(Name);
			if ((NL != null) && (NL.item(iNum) != null)) {
				Node node = NL.item(iNum);
				NodeList children = node.getChildNodes();
				if ((children != null) && (children.getLength() > 0)) {
					return filtStr(children);
				} else
					throw new Exception("Not Get Value!");

			} else
				throw new Exception("Not Get Value!");
		} catch (Exception ex) {
			ErrMsg = "Get Value Err:" + ex.toString();
			return "";
		}
	}

	/**
	 * 根据输入的节点名称(节点名Name)获取它对应的第iNum个值
	 * 
	 * @param Name
	 * @return
	 */
	public String sGetValueByName(String Name) {
		try {
			return sGetValueByName(Name, 0);
		} catch (Exception ex) {
			ErrMsg = "Get Value Err:" + ex.toString();
			return "";
		}
	}

	/**
	 * 获取某个以sName命名的结点个数
	 * 
	 * @param sName
	 * @return
	 */
	public int GetNameNum(String sName) {
		try {
			if ((sName != null) && (sName.length() > 0)) {
				NodeList NL = doc.getElementsByTagName(sName);
				if (NL != null)
					return NL.getLength();
			}
			return 0;
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 按递归的方式得出某个节点(node)内名称为Name的节点,Name在此范围内必须为唯一的
	 * 
	 * @param node
	 * @param Name
	 * @return
	 */
	private Node getNode(Node node, String Name) {
		try {
			Node Result = null;
			NodeList children = node.getChildNodes();
			if ((children == null) || (children.getLength() == 0))
				return null;
			int i;
			for (i = 0; i < children.getLength(); i++) {
				if (children.item(i).getNodeName().equalsIgnoreCase(Name))
					return children.item(i);
				else {
					Result = getNode(children.item(i), Name);
					if (Result != null)
						return Result;
				}
			}
			return null;
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 取某个节点(节点名为BeginName)内某个节点(节点名为Name)对应的值
	 * 
	 * @param BeginName
	 * @param Name
	 * @return
	 */
	public String sGetValueByName(String BeginName, String Name) {
		try {
			NodeList NL = doc.getElementsByTagName(BeginName);
			if (NL != null) {
				Node node = null;
				int iKp = 0;
				while (node == null) {
					NL.item(iKp);
					node = getNode(NL.item(iKp), Name);
					iKp++;
					if (iKp == NL.getLength())
						break;
				}
				NodeList children = null;
				if (node != null)
					children = node.getChildNodes();

				if ((children != null) && (children.getLength() > 0)) {
					return filtStr(children);
				} else
					throw new Exception("Not Get Value!");
			} else
				throw new Exception("Not Get Value!");
		} catch (Exception ex) {
			ErrMsg = "Get Value Err:" + ex.toString();
			return "";
		}
	}

	/**
	 * 取某个节点(节点名为BeginName)内某个节点(节点名为Name)对应的值
	 * 
	 * @param BeginName
	 * @param Name
	 * @return
	 */
	public String sGetValueByName1(String BeginName, String Name) {
		try {
			NodeList NL = doc.getElementsByTagName(BeginName);
			if (NL != null) {
				Node node = null;
				int iKp = 0;
				while (node == null) {
					NL.item(iKp);
					node = NL.item(iKp).getFirstChild();
					while (node != null) {
						if (!node.getNodeName().equalsIgnoreCase("field")) {
							node = node.getFirstChild();
						} else {
							node = node.getNextSibling();
						}
						if (node != null)
							System.err
									.println("NodeName=" + node.getNodeName());
					}

					node = getNode(NL.item(iKp), Name);
					iKp++;
					if (iKp == NL.getLength())
						break;
				}
				NodeList children = null;
				if (node != null)
					children = node.getChildNodes();

				if ((children != null) && (children.getLength() > 0)) {
					return filtStr(children);
				} else
					throw new Exception("Not Get Value!");
			} else
				throw new Exception("Not Get Value!");
		} catch (Exception ex) {
			ErrMsg = "Get Value Err:" + ex.toString();
			return "";
		}
	}

}
