package com.facetime.core.utils;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import org.apache.log4j.Logger;

/**
 * ＪＤＢＣ开发的辅助方法类
 *
 * @author dzb
 */
public abstract class JDBCUtils {
	/**
	 * 安静的关闭连接
	 * @param conn
	 *            the JDBC Connection to close (may be <code>null</code>)
	 */
	public static void closeQuiet(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (Throwable ex) {
			}
		}
	}

	/**
	 * 安静的关闭声明
	 * @param stmt
	 */
	public static void closeQuiet(Statement stmt) {
		if (stmt == null)
			return;
		try {
			stmt.close();
		} catch (Throwable ex) {
		}
	}

	/**
	 * 安静的关闭数据集
	 * @param rs
	 *            the JDBC ResultSet to close (may be <code>null</code>)
	 */
	public static void closeQuiet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Throwable ex) {
			}
		}
	}

	/**
	 * 使用给定的Log记录详细的SQLException信息
	 * @param ex
	 * @param log
	 */
	public static void logSqlError(SQLException ex, Logger logger) {
		System.out.printf("DBERROR OCCURS: \n CODE-%d\n STATE: %s  MESSAGE: %s", +ex.getErrorCode(), ex.getSQLState(),
				ex.getMessage());
	}

	/**
	 * 关闭Statement和ResultSet
	 * @param 
	 * 		stmt 语句
	 * @param rs
	 * 		结果集
	 */
	public static void close(Statement stmt, ResultSet rs) {

		StringBuilder errorText = new StringBuilder();

		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			errorText.append("Error: Could not close ResultSet: " + e.toString() + "\n");
		}

		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (SQLException e) {
			errorText.append("Error: Could not close Statement: " + e.toString() + "\n");
		}

		if (errorText.length() > 0) {
			throw LE.makeThrow(errorText.toString());
		}
	}

	/**
	 * Closes the <code>ResultSet</code>, then the <code>Statement</code> or
	 * <code>Statement</code>, and finally the <code>Connection</code>
	 * in the same sequence as mentioned here. If any of the parameters are null
	 * they will be ignored (not attempted closed).
	 * 
	 * @param connection
	 *            The <code>Connection</code> to close.
	 * @param statement
	 *            The <code>Statement</code> or <code>PreparedStatement</code>
	 *            to close.
	 * @param result
	 *            The <code>ResultSet</code> to close.
	 * @throws RuntimeException
	 *             If one or more SQLExceptions are thrown when closing the
	 *             result set, statement or connection. The error messages from
	 *             all thrown exceptions are collected and included in the one
	 *             PersistenceException that is thrown.
	 */
	public static void close(Connection connection, Statement statement, ResultSet result) {

		StringBuilder errorText = new StringBuilder();

		try {
			if (result != null) {
				result.close();
			}
		} catch (SQLException e) {
			errorText.append("Error: Could not close ResultSet: " + e.toString() + "\n");
		}

		try {
			if (statement != null) {
				statement.close();
			}
		} catch (SQLException e) {
			errorText.append("Error: Could not close Statement: " + e.toString() + "\n");
		}

		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			errorText.append("Error: Could not close Connection: " + e.toString() + "\n");
		}

		if (errorText.length() > 0) {
			throw LE.makeThrow("ERROR occurs on %s ", errorText.toString());
		}
	}

	/**
	 * Inserts all parameters in the array into the
	 * <code>PreparedStatement</code> creating in the sequence their are located
	 * in the array.
	 * 
	 * @param ps
	 *            The <code>PreparedStatement</code> to insert the parameters
	 *            into
	 * @param params
	 *            The parameters to insert.
	 * @throws SQLException
	 *             If anything goes wrong during the insertion of the
	 *             parameters.
	 */
	public static void bind(PreparedStatement ps, Object... params) throws SQLException {

		if (params == null)
			return;

		for (int i = 0; i < params.length; i++) {
			if (params[i] != null) {
				ps.setObject(i + 1, params[i]);
			} else {
				ps.setNull(i + 1, Types.NULL);
			}
		}
	}

	/**
	 * 同上一个方法，但是可以指定类型
	 *
	 * @param ps
	 * @param params
	 * @param types
	 * @throws SQLException
	 */
	public static void bind(PreparedStatement ps, Object[] params, int[] types) throws SQLException {

		if (params == null || types == null)
			return;

		if (params.length > types.length) {
			throw new SQLException("Parameters count can not bigger than parameter types array length");
		}

		for (int i = 0; i < params.length; i++) {
			if (params[i] != null) {
				ps.setObject(i + 1, params[i], types[i]);
			} else {
				ps.setNull(i + 1, types[i]);
			}
		}
	}

	/**
	 * 打印一个数据集到指定的writer
	 *
	 * @param rs
	 */
	public static void printResultSet(ResultSet rs, PrintStream writer) {
		String print = printResultSet(rs);
		writer.append(print);
		writer.flush();
	}

	/**
	 * 打印一个数据集到指定的字符串
	 * 
	 * @param rs
	 * @return
	 */
	public static String printResultSet(ResultSet rs) {
		StringBuilder sb = new StringBuilder();
		try {
			ResultSetMetaData rsMeta = rs.getMetaData();
			int colCount = rsMeta.getColumnCount();
			int count = 0;
			while (rs.next()) {
				sb.append(count++);
				sb.append("-------------------------------------------------------\r\n");
				for (int i = 1; i <= colCount; i++) {
					sb.append(rsMeta.getColumnLabel(i) + " : " + "\t\t\t" + rs.getString(i) + "\r\n");
				}
			}
			sb.append("------------------------------------------------------\r\n");
		} catch (SQLException ex) {

			sb.append("***************************************\r\n");
			sb.append("* SQLException in outputResultSet: " + ex.getMessage() + "\r\n");
			sb.append("* SQLState : " + ex.getSQLState() + "\r\n");
			sb.append("* SQL ERROR CODE " + ex.getErrorCode() + "\r\n");
			sb.append("***************************************\r\n");

		}
		return sb.toString();
	}
}
