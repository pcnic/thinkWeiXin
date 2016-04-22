package com.linjj.thinkweixin.db;

import java.io.BufferedReader;
import java.io.Reader;
import java.sql.*;
import java.util.Vector;

import com.linjj.thinkweixin.util.ExtString;

/**
 * Title: 用来直接执行SQL的类它利用了JDBC Description: Copyright: Copyright (c) 2004
 * Company: salary
 * 
 * @author ZRJ 2004-09-17 created
 * @version 1.0.0.0
 */

public class DBSql {
	private DBConnPool pool = null;

	private String sInput;// 发送的原始参数

	private String sPoolName = "";

	public DBSql() {
		pool = DBPoolManage.getPool("");
		if (pool == null) {
			if (DBPoolManage.bDebug)
				System.err.println("default connect pool is null,to create...");
			if (!DBReadConfig.bRead)
				DBReadConfig.readConfigFile();
			else
				DBReadConfig.restartPool("", false);
			pool = DBPoolManage.getPool("");
		}
	}

	public DBSql(String sPoolName) {
		pool = DBPoolManage.getPool(sPoolName);
		this.sPoolName = sPoolName;
		if (pool == null) {
			if (DBPoolManage.bDebug)
				System.err.println(sPoolName
						+ " connect pool is null...,must first create");
			if (!DBReadConfig.bRead)
				DBReadConfig.readConfigFile();
			else
				DBReadConfig.restartPool(sPoolName, false);
			pool = DBPoolManage.getPool(sPoolName);
			this.sPoolName = sPoolName;
		}
	}

	public DBConnPool getPool() {
		return pool;
	}

	public void setPool(DBConnPool pool) {
		this.pool = pool;
	}

	/**
	 * 获取一个连接对象,本方法只用于当连接池配置指定到外部连接池(WebLogic/Tongweb)上的时候
	 * 
	 * @return
	 */
	public java.sql.Connection getConnect() {
		try {
			return getPool().getConnectByExtern();
		} catch (Exception ex) {
			return null;
		}
	}

	private void afterQueryProcess(Statement st, DBConnObj connobj,
			Connection conn, ResultSet rset, PreparedStatement ps) {
		try {
			if (rset != null)
				rset.close();
		} catch (Exception ex) {
		}
		try {
			if (ps != null)
				ps.close();
		} catch (Exception ex) {
		}
		try {
			if (st != null)
				st.close();
		} catch (Exception ex) {
		}
		try {
			if ((connobj != null) && (connobj.setRef(0) > 0)) {
				if (!bActive(conn)) {
					conn.close();
					connobj.setRef(-3);
				}
				connobj.setRef(-1);
			}
		} catch (Exception ex) {
			if ((connobj != null) && (connobj.setRef(0) > 0))
				connobj.setRef(-1);
		}
	}

	/**
	 * 执行一个增删改动作
	 * 
	 * @param sql
	 * @param iTimeOut
	 * @param aInputObject
	 * @return
	 */
	private Result executeUpdate(String sql, int iTimeOut, Object[] aInputObject) {
		sInput = sql;
		Result result = new Result();
		if (sql == null || sql.equals(""))
			return result;
		Statement st = null;
		DBConnObj connobj = null;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			if (pool == null)
				throw new Exception("Connect Pool Is Null!");

			if (pool.getCloseed()) {// 该连接池已经被关闭
				pool = DBPoolManage.getPool(sPoolName);
			}

			if (pool == null)
				throw new Exception("Connect Pool Is Null!");

			connobj = pool.getConnect();
			if (connobj == null)
				throw new Exception("no way get one connect!");

			if (iTimeOut <= 0)
				iTimeOut = pool.getTimeOut();// 如果为0或小于0,设置成默认SQL执行超时时间

			/* 生成带事务的Sql语句 */
			sql = getTransactionSql(sql);

			if (DBPoolManage.bDebug)
				System.err.println("Execute sql:" + sql);

			conn = connobj.conn;

			if (aInputObject == null) {
				st = conn.createStatement();
				st.executeUpdate(sql);
				st.close();
			} else {
				ps = conn.prepareStatement(sql);
				for (int i = 0; i < aInputObject.length; i++)
					ps.setObject(i + 1, aInputObject[i]);
				ps.executeUpdate();
				ps.close();
			}
			result.iErrorCode = 0;
			result.iFieldCount = 0;
			result.iSum = 0;
			result.iActualNum = 0;
			connobj.setRef(-1);
			return result;
		} catch (Exception ex) {
			afterQueryProcess(st, connobj, conn, null, ps);
			result.iErrorCode = Const.I_ERR_APP_SQL;
			result.sErrorDescriptor = Const.getErrInfo(result.iErrorCode);
			result.sErrorDetail = "executeUpdate()==>" + ex.toString() + "\r\n"
					+ sql;
			System.err.println(result.sErrorDetail);
			return result;
		}
	}

	// 判断连接是否是活动的
	public boolean bActive(Connection conn) {
		Statement st = null;
		try {
			if (conn == null)
				return false;
			if (pool.bUseExternPool)
				return true;
			st = conn.createStatement();
			if (pool.isOracle())
				st.executeQuery("select sysdate from dual");
			if (pool.isSybase())
				st.executeQuery("select getdate()");
			st.close();
			return true;
		} catch (Exception ex) {
			try {
				if (st != null)
					st.close();
			} catch (Exception ex2) {
				st = null;
			}
			return false;
		}

	}

	// 查询数据
	// 从1开始表示第一个记录
	private Result executeQuery(String sql, int iStartIndex, int iEndIndex,
			int iTimeOut, Object[] aInputObject) {
		sInput = sql;
		Result result = new Result();
		if (sql == null || sql.equals(""))
			return result;
		Statement st = null;
		ResultSet rset = null;
		Connection conn = null;
		DBConnObj connobj = null;
		PreparedStatement ps = null;
		try {
			if (pool == null)
				throw new Exception("Connect Pool Is Null!");
			Vector vResult = new Vector();
			result.iErrorCode = 0;

			if (pool.getCloseed()) {// 该连接池已经被关闭
				pool = DBPoolManage.getPool(sPoolName);
			}

			if (pool == null)
				throw new Exception("Connect Pool Is Null!");

			if (iTimeOut <= 0)
				iTimeOut = pool.getTimeOut();// 如果为0或小于0,设置成默认SQL执行超时时间

			connobj = pool.getConnect();
			if (connobj == null)
				throw new Exception("no way get one connect!");
			if (DBPoolManage.bDebug)
				System.err.println("Query sql:" + sql);
			conn = connobj.conn;

			if (pool.isSybase()) {
				sql = "set chained off\n" + sql;
			}

			if (aInputObject == null) {
				st = conn.createStatement();
				rset = st.executeQuery(sql);
				result.rset = rset;
				st.setFetchSize(1000);
			} else {
				ps = conn.prepareStatement(sql);
				for (int i = 0; i < aInputObject.length; i++)
					ps.setObject(i + 1, aInputObject[i]);
				rset = ps.executeQuery();
				ps.setFetchSize(1000);
			}

			ResultSetMetaData meta = rset.getMetaData();
			int iColnums = meta.getColumnCount();
			result.aLineOut = new String[iColnums];
			// 取字段名称
			for (int i = 0; i < iColnums; i++)
				result.aLineOut[i] = meta.getColumnName(i + 1);

			int iNum = 0;
			int iAddNum = 0;
			int iSum = 0;

			for (int i = 1; i < iStartIndex; i++) {
				if (!rset.next())
					break;
				iSum++;
				iNum++;
			}
			while (rset.next()) {
				String[] aTemp = new String[iColnums];
				for (int i = 1; i <= iColnums; i++) {
					// 这些数据要显示在文本域中，可以把一些特殊字符进行过滤 能够正确的显示出来
					// aTemp[i-1]=com.string.strFormat.toHtmlInput(rset.getString(i));

					// aTemp[i - 1] = rset.getString(i);
					if (rset.getObject(i) == null) {
						aTemp[i - 1] = "";
					} else {
						if (!rset.getObject(i).getClass().getName().equals(
								"oracle.sql.CLOB")) {
							aTemp[i - 1] = rset.getString(i);
						} else {
							Clob clob = rset.getClob(i);
							Reader reader = clob.getCharacterStream();
							BufferedReader br = new BufferedReader(reader);
							StringBuffer sb = new StringBuffer();
							String s;
							s = br.readLine();
							if (s != null) {
								sb.append(s);
							}
							while ((s = br.readLine()) != null) {
								sb.append("\n" + s);
							}
							aTemp[i - 1] = sb.toString();
						}
					}
				}

				vResult.add(iAddNum, aTemp);
				iNum++;
				iAddNum++;
				iSum++;
				if ((iEndIndex != -1) && (iEndIndex <= iNum))
					break;
			}

			while (rset.next())
				iSum++;

			rset.close();
			if (st != null)
				st.close();
			if (ps != null)
				ps.close();
			connobj.setRef(-1);

			if ((vResult.size() > 0) && (iColnums > 0)) {
				result.aaMultiLinesOut = new String[vResult.size()][iColnums];
				String[][] aaResult = result.aaMultiLinesOut;
				for (int i = 0; i < aaResult.length; i++)
					aaResult[i] = (String[]) (vResult.elementAt(i));
			}
			result.iActualNum = vResult.size();
			result.iFieldCount = iColnums;
			result.iSum = iSum;
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			afterQueryProcess(st, connobj, conn, rset, ps);
			result.iErrorCode = Const.I_ERR_APP_SQL;
			result.sErrorDescriptor = Const.getErrInfo(result.iErrorCode);
			result.sErrorDetail = "executeQuery()==>" + ex.toString() + "\r\n"
					+ sql;
			System.err.println(result.sErrorDetail);
			return result;
		}

	}

	public String getSendStr() {
		return sInput;
	}

	// 执行SQL语句 flag=1强制执行查询 可带SQL参数
	public Result executeSql(String sql, int iStartIndex, int iEndIndex,
			int iTimeOut, int flag, Object[] aInput) {
		if (sql == null || sql.equals(""))
			return new Result();
		String sHead = "";
		try {
			sHead = sql.trim().substring(0, 7);
		} catch (Exception e) {
			sHead = "";
		}

		if (flag == 1)
			sHead = "select ";// 假如flag==1强制执行查询

		if (sHead.equalsIgnoreCase("select "))
			return executeQuery(sql, iStartIndex, iEndIndex, iTimeOut, aInput);
		else
			return executeUpdate(sql, iTimeOut, aInput);
	}

	// 执行SQL语句 flag=1强制执行查询 不带SQL参数
	public Result executeSql(String sql, int iStartIndex, int iEndIndex,
			int iTimeOut, int flag) {
		return executeSql(sql, iStartIndex, iEndIndex, iTimeOut, flag, null);
	}

	// 执行SQL语句 不带SQL参数
	public Result executeSql(String sql, int iStartIndex, int iEndIndex) {
		return executeSql(sql, iStartIndex, iEndIndex, 0, 0, null);
	}

	// 执行SQL语句 不带SQL参数
	public Result executeSql(String sql, int iTimeOut) {
		return executeSql(sql, -1, -1, iTimeOut, 0, null);
	}

	// 执行SQL语句
	public Result executeSql(String sql) {
		return executeSql(sql, -1, -1, 0, 0, null);
	}

	public Result execQueryPro(String sql) {
		return executeSql(sql, -1, -1, 0, 1, null);
	}

	// 执行SQL语句 带SQL参数
	public Result executeSql(String sql, Object[] aParamObject) {
		return executeSql(sql, -1, -1, 0, 0, aParamObject);
	}

	public Result execUpdatePro(String sql) {
		return executeSql(sql, -1, -1, 0, 0, null);
	}


	public Result execUpdateSql(String sql, Object[] aParamObject) {
		sInput = sql;
		Result result = new Result();
		if (sql == null || sql.equals(""))
			return result;
		Statement st = null;
		DBConnObj connobj = null;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			if (pool == null)
				throw new Exception("Connect Pool Is Null!");

			if (pool.getCloseed()) {// 该连接池已经被关闭
				pool = DBPoolManage.getPool(sPoolName);
			}

			if (pool == null)
				throw new Exception("Connect Pool Is Null!");

			connobj = pool.getConnect();
			if (connobj == null)
				throw new Exception("no way get one connect!");

			/* 生成带事务的Sql语句 */
			sql = getTransactionSql_1(sql);

			if (DBPoolManage.bDebug)
				System.err.println("Execute sql:" + sql);

			conn = connobj.conn;
			if (aParamObject == null) {
				st = conn.createStatement();
				st.executeUpdate(sql);
				st.close();
			} else {
				ps = conn.prepareStatement(sql);
				for (int i = 0; i < aParamObject.length; i++)
					ps.setObject(i + 1, aParamObject[i]);
				ps.executeUpdate();
				ps.close();
			}
			result.iErrorCode = 0;
			result.iFieldCount = 0;
			result.iSum = 0;
			result.iActualNum = 0;
			connobj.setRef(-1);
			return result;
		} catch (Exception ex) {
			afterQueryProcess(st, connobj, conn, null, ps);
			result.iErrorCode = Const.I_ERR_APP_SQL;
			result.sErrorDescriptor = Const.getErrInfo(result.iErrorCode);
			result.sErrorDetail = "executeUpdate()==>" + ex.toString() + "\r\n"
					+ sql;
			System.err.println(result.sErrorDetail);
			return result;
		}
	}

	/**
	 * 函数功能：SQL语句数组为输入参数，输出为符合ORACLE和SYBASE的带事务的SQL语句 params：aSql : SQL语句数组
	 * return：带事务的SQL语句 luojz
	 */
	public static String getTransactionSql(String[] aSql) {
		// 没有SQL语句
		if (aSql == null || (aSql != null && aSql.length < 1)) {
			return "";
		}
		// 从全局配置里取所使用的数据库类型
		String sDbType = "ORACLE"; // 默认为oracle
		StringBuffer sb = new StringBuffer("");
		// 取消\r只用\n
		String sEnter = "\n";
		if (sDbType.equals("ORACLE")) { // 如果是ORACLE
			sb.append("BEGIN" + sEnter);
			for (int i = 0; i < aSql.length; i++) {
				if (aSql[i] == null)
					aSql[i] = "";
				sb.append(aSql[i] + ";");
			}
			sb.append("COMMIT;" + sEnter);
			/*
			 * Modified by lihong on 2003.08.12,加上raise，把异常再抛出
			 * sb.append("END;"+sEnter);
			 */
			sb.append("EXCEPTION WHEN OTHERS THEN  ROLLBACK; RAISE; END;"
					+ sEnter);
		} else if (sDbType.equals("SYBASE")) { // 如果是SYBASE
			sb.append("DECLARE @iErr int" + sEnter);
			sb.append("BEGIN TRANSACTION" + sEnter);
			String sTemp = "SELECT @iErr=@iErr";
			for (int i = 0; i < aSql.length; i++) {
				sb.append(aSql[i] + sEnter);
				if (i == 0) {
					sb.append("SELECT @iErr=@@error" + sEnter);
				} else {
					sTemp += "+@@error";
					sb.append(sTemp + sEnter);
				}
			}
			sb.append("IF @iErr<>0" + sEnter);
			sb.append("ROLLBACK TRANSACTION" + sEnter);
			sb.append("COMMIT TRANSACTION" + sEnter);
		} else { // 其它数据库，暂时没有，返回空
			return "";
		}
		return sb.toString();
	}

	/**
	 * 函数功能：SQL语句数组为输入参数，输出为符合ORACLE和SYBASE的带事务的SQL语句 params：aSql : SQL语句数组
	 * return：带事务的SQL语句 luojz
	 */
	public static String getTransactionSql_1(String aSql) {
		// 没有SQL语句
		if (aSql == null || aSql.trim().equals("")) {
			return "";
		}
		// 从全局配置里取所使用的数据库类型
		String sDbType = "ORACLE"; // 默认为oracle
		StringBuffer sb = new StringBuffer("");
		// 取消\r只用\n
		String sEnter = "\n";
		if (sDbType.equals("ORACLE")) { // 如果是ORACLE
			sb.append("BEGIN" + sEnter);
			sb.append(aSql);
			if (aSql.trim().charAt(aSql.length() - 1) != ';')
				sb.append(";");
			sb.append("COMMIT;" + sEnter);
			/*
			 * Modified by lihong on 2003.08.12,加上raise，把异常再抛出
			 * sb.append("END;"+sEnter);
			 */
			sb.append("EXCEPTION WHEN OTHERS THEN  ROLLBACK; RAISE; END;"
					+ sEnter);
		} else if (sDbType.equals("SYBASE")) { // 如果是SYBASE
			/*sb.append("DECLARE @iErr int" + sEnter);
			sb.append("BEGIN TRANSACTION" + sEnter);
			String sTemp = "SELECT @iErr=@iErr";
				sb.append(aSql + sEnter);
				if (i == 0) {
					sb.append("SELECT @iErr=@@error" + sEnter);
				} else {
					sTemp += "+@@error";
					sb.append(sTemp + sEnter);
				}
			}
			sb.append("IF @iErr<>0" + sEnter);
			sb.append("ROLLBACK TRANSACTION" + sEnter);
			sb.append("COMMIT TRANSACTION" + sEnter);*/
		} else { // 其它数据库，暂时没有，返回空
			return "";
		}
		return sb.toString();
	}

	/**
	 * 函数功能：SQL语句数组为输入参数，输出为符合ORACLE和SYBASE的带事务的SQL语句 params：aSql : SQL语句vector
	 * return：带事务的SQL语句 luojz
	 */
	public static String getTransactionSql(Vector vSql) {
		if (vSql == null || (vSql != null && vSql.size() < 1)) {
			return "";
		}
		String[] aTemp = new String[vSql.size()];
		for (int i = 0; i < aTemp.length; i++) {
			String sTemp = (String) vSql.get(i);
			if (sTemp == null)
				sTemp = "";
			aTemp[i] = sTemp;
		}
		return getTransactionSql(aTemp);
	}

	/**
	 * 函数功能：SQL语句数组为输入参数，输出为符合ORACLE和SYBASE的带事务的SQL语句 params：sSql : SQL语句(以分号分隔)
	 * return：带事务的SQL语句 luojz
	 */
	public static String getTransactionSql(String sSql) {
		String[] aSql = ExtString.split(ExtString.turnStr(sSql), ";", ""); // 分隔字符串
		return getTransactionSql(aSql);
	}

	// 从1开始表示第一个记录
	private Result executeUpdateFromValue(String sql, String fieldName,
			String value, int iTimeOut, Object[] aInputObject) {
		sInput = sql;
		Result result = new Result();
		if (sql == null || sql.equals(""))
			return result;
		Statement st = null;
		DBConnObj connobj = null;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			if (pool == null)
				throw new Exception("Connect Pool Is Null!");

			if (pool.getCloseed()) {// 该连接池已经被关闭
				pool = DBPoolManage.getPool(sPoolName);
			}

			if (pool == null)
				throw new Exception("Connect Pool Is Null!");

			connobj = pool.getConnect();
			if (connobj == null)
				throw new Exception("no way get one connect!");

			if (iTimeOut <= 0)
				iTimeOut = pool.getTimeOut();// 如果为0或小于0,设置成默认SQL执行超时时间

			/* 生成带事务的Sql语句 */
			sql = getTransactionSql(sql);

			if (DBPoolManage.bDebug)
				System.err.println("run sql:" + sql);

			conn = connobj.conn;

			if (aInputObject == null) {
				st = conn.createStatement();
				st.executeUpdate(sql);
				st.close();
			} else {
				ps = conn.prepareStatement(sql);
				for (int i = 0; i < aInputObject.length; i++)
					ps.setObject(i + 1, aInputObject[i]);
				ps.executeUpdate();
				ps.close();
			}

			result.iErrorCode = 0;
			result.iFieldCount = 0;
			result.iSum = 0;
			result.iActualNum = 0;
			connobj.setRef(-1);
			return result;
		} catch (Exception ex) {
			afterQueryProcess(st, connobj, conn, null, ps);
			result.iErrorCode = Const.I_ERR_APP_SQL;
			result.sErrorDescriptor = Const.getErrInfo(result.iErrorCode);
			result.sErrorDetail = "executeUpdate()==>" + ex.toString() + "\r\n"
					+ sql;
			System.err.println(result.sErrorDetail);
			return result;
		}
	}

}
