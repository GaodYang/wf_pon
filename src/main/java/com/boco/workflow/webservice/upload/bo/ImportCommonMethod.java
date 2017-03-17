package com.boco.workflow.webservice.upload.bo;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.boco.common.util.debug.LogHome;
import com.boco.core.bean.SpringContextUtil;
import com.boco.core.utils.lang.TimeFormatHelper;


@SuppressWarnings({"rawtypes","unused","unchecked","null","deprecation","finally"})
public class ImportCommonMethod {

	
	private static final Map map = null;
	static AnOltManageBO oltManageBO = getAnOltManageBO();
	static TLogicVlanBO tLogicVlanBO = getTLogicVlanBO();

	private static AnOltManageBO getAnOltManageBO() {
		return (AnOltManageBO) SpringContextUtil
				.getBean(AnmsBoName.AnOltManageBO);
	}

	private static TLogicVlanBO getTLogicVlanBO() {
		return (TLogicVlanBO) SpringContextUtil
				.getBean(AnmsBoName.TLogicVlanBO);
	}

	private static String[] dateFormat = new String[] { "yyyy-MM-dd",
			"yyyy-MM-dd HH:mm:ss", "yyyy-M-d", "yyyy-M-d H:m:s", "yyyy/MM/dd",
			"yyyy/MM/dd HH:mm:ss", "yyyy/M/d", "yyyy/M/d H:m:s" };

	/*
	 * 校验设备上联端口与上联设备的关联关系
	 */
	protected static Map verificationRelatedPosPort(Workbook writeWorkBook,
			Sheet writeSheet, String posCuid, int relatedPosCuid,
			int relatedPosPortCuid, int i, int lastColumns, String constantValue)
			throws Exception {
		try {
			Row xRow = writeSheet.getRow(i);
			Cell relatedPosObj = xRow.getCell(relatedPosCuid);
			Cell relatedPosPortObj = xRow.getCell(relatedPosPortCuid);
			if (relatedPosObj != null && relatedPosPortObj != null) {
				List<Map> list = oltManageBO.getPtpByLabelCnAndNeCuid(getCellValue(relatedPosPortObj), posCuid);
				if (list.isEmpty()) {
					updateExcelFormat(writeWorkBook, writeSheet, i,relatedPosPortCuid);
					// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
					Row initRow = writeSheet.getRow(0);
					Object initContant1 = initRow.getCell(relatedPosPortCuid);
					Object initContant2 = initRow.getCell(relatedPosCuid);
					Cell xCell = xRow.getCell(lastColumns);
					if (xCell == null) {
						xCell = xRow.createCell(lastColumns);
						xCell.setCellValue("[" + initContant1.toString() + "]"
								+ "与[" + initContant2.toString()
								+ "]不存在关联关系!\r\n");
					} else {
						String strValue = xCell.getStringCellValue();
						strValue = strValue + "[" + initContant1.toString()
								+ "]" + "与[" + initContant2.toString()
								+ "]不存在关联关系!\r\n";
						xCell.setCellValue(strValue);
					}
				} else {
					return list.get(0);
				}
			}
		} catch (Exception e) {
			LogHome.getLog().error("verificationEmpty校验导入属性字段是否为空出错!", e);
			throw new Exception(e);
		}
		return null;
	}

	/*
	 * 校验设备与端口的关联关系
	 */
	protected static Map verificationRelatedPortByNameAndNeCuid(
			Workbook writeWorkBook, Sheet writeSheet, String neCuid,
			int relatedNeCuid, int relatedPortCuid, int i, int lastColumns,
			String constantValue) throws Exception {
		try {
			Row xRow = writeSheet.getRow(i);
			Cell relatedPosPortObj = xRow.getCell(relatedPortCuid);
			if (!ImportCommonMethod.isEmpty(neCuid)
					&& relatedPosPortObj != null) {
				List<Map> list = oltManageBO.getPtpByLabelCnAndNeCuid(getCellValue(relatedPosPortObj), neCuid);
				if (list == null || list.size() == 0) {
					updateExcelFormat(writeWorkBook, writeSheet, i,
							relatedPortCuid);
					// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
					Row initRow = writeSheet.getRow(0);
					Object initContant1 = initRow.getCell(relatedPortCuid);
					Object initContant2 = initRow.getCell(relatedNeCuid);
					Cell xCell = xRow.getCell(lastColumns);
					if (xCell == null) {
						xCell = xRow.createCell(lastColumns);
						xCell.setCellValue("[" + initContant1.toString() + "]"
								+ "与[" + initContant2.toString()
								+ "]不存在关联关系!\r\n");
					} else {
						String strValue = xCell.getStringCellValue();
						strValue = strValue + "[" + initContant1.toString()
								+ "]" + "与[" + initContant2.toString()
								+ "]不存在关联关系!\r\n";
						xCell.setCellValue(strValue);
					}
				} else {
					return list.get(0);
				}
			}
		} catch (Exception e) {
			LogHome.getLog().error("verificationEmpty校验导入属性字段是否为空出错!", e);
			throw new Exception(e);
		}
		return null;
	}

	/*
	 * 校验OLT导入中BRAS名称是否存在
	 */
	protected static void verificationAportSame(Workbook writeWorkBook,
			Sheet writeSheet, int aport1, int aport2, int i, int lastColumns,
			String constantValue) throws Exception {
		try {
			Row xRow = writeSheet.getRow(i);
			Object cellContant1 = xRow.getCell(aport1);
			Object cellContant2 = xRow.getCell(aport2);
			if (cellContant1.toString().equals(cellContant2.toString())) {
				updateExcelFormat(writeWorkBook, writeSheet, i, aport2);
				// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
				Row initRow = writeSheet.getRow(0);
				Object initContant = initRow.getCell(aport2);
				Cell xCell = xRow.getCell(lastColumns);
				if (xCell == null) {
					xCell = xRow.createCell(lastColumns);
					// 设置最后一列记录异常信息单元格的列宽度
					// writeSheet.setColumnWidth(lastColumns,
					// writeSheet.getColumnWidth(lastColumns)*3);
					xCell.setCellValue("[" + initContant.toString() + "]"
							+ "不能与[OLT侧上行端口1]相同 \r\n");
				} else {
					String strValue = xCell.getStringCellValue();
					strValue = strValue + "[" + initContant.toString() + "]"
							+ "不能与[OLT侧上行端口1]相同  \r\n";
					xCell.setCellValue(strValue);
				}
			}
		} catch (Exception e) {
			LogHome.getLog().error("verificationEmpty校验导入属性字段是否为空出错!", e);
			throw new Exception(e);
		}
	}

	/*
	 * 校验OLT导入中BRAS名称是否存在
	 */
	protected static String verificationBrasName(Workbook writeWorkBook,
			Sheet writeSheet, int j, int i, int lastColumns,
			String constantValue) throws Exception {
		try {
			Row xRow = writeSheet.getRow(i);
			Object cellContant = xRow.getCell(j);
			String brasCuid = oltManageBO.isBrasNameExist(xRow.getCell(j)
					.toString());
			if (StringUtils.isEmpty(brasCuid)) {
				updateExcelFormat(writeWorkBook, writeSheet, i, j);
				// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
				Row initRow = writeSheet.getRow(0);
				Object initContant = initRow.getCell(j);
				Cell xCell = xRow.getCell(lastColumns);
				if (xCell == null) {
					xCell = xRow.createCell(lastColumns);
					// 设置最后一列记录异常信息单元格的列宽度
					// writeSheet.setColumnWidth(lastColumns,
					// writeSheet.getColumnWidth(lastColumns)*3);
					xCell.setCellValue("[" + initContant.toString() + "]"
							+ "在数据库中不存在 \r\n");
				} else {
					String strValue = xCell.getStringCellValue();
					strValue = strValue + "[" + initContant.toString() + "]"
							+ "在数据库中不存在  \r\n";
					xCell.setCellValue(strValue);
				}
			}
			return brasCuid;
		} catch (Exception e) {
			LogHome.getLog().error("verificationEmpty校验导入属性字段是否为空出错!", e);
			throw new Exception(e);
		}
	}

	/*
	 * 校验OLT导入中OLT所属BRAS端口是否存在
	 */
	protected static String verificationBrasPort(Workbook writeWorkBook,
			Sheet writeSheet, int zdevice1, int zport1, int i, int lastColumns,
			String constantValue) throws Exception {
		try {
			Row xRow = writeSheet.getRow(i);
			Object cellContant = xRow.getCell(zport1);
			String brasPortCuid = oltManageBO.isBrasPortExist(
					xRow.getCell(zdevice1).toString(), xRow.getCell(zport1)
							.toString());
			if (StringUtils.isEmpty(brasPortCuid)) {
				updateExcelFormat(writeWorkBook, writeSheet, i, zport1);
				// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
				Row initRow = writeSheet.getRow(0);
				Object initContant = initRow.getCell(zport1);
				Cell xCell = xRow.getCell(lastColumns);
				if (xCell == null) {
					xCell = xRow.createCell(lastColumns);
					// 设置最后一列记录异常信息单元格的列宽度
					// writeSheet.setColumnWidth(lastColumns,
					// writeSheet.getColumnWidth(lastColumns)*3);
					xCell.setCellValue("[" + initContant.toString() + "]"
							+ "与BRAS关联关系不存在 \r\n");
				} else {
					String strValue = xCell.getStringCellValue();
					strValue = strValue + "[" + initContant.toString() + "]"
							+ "与BRAS关联关系不存在  \r\n";
					xCell.setCellValue(strValue);
				}
			}
			return brasPortCuid;
		} catch (Exception e) {
			LogHome.getLog().error("verificationEmpty校验导入属性字段是否为空出错!", e);
			throw new Exception(e);
		}
	}

	protected static void verificationTopoName(Workbook writeWorkBook,
			Sheet writeSheet, int j, int i, int lastColumns, String message)
			throws Exception {
		try {
			updateExcelFormat(writeWorkBook, writeSheet, i, j);
			// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
			Row xRow = writeSheet.getRow(i);
			Cell xCell = xRow.getCell(lastColumns);
			if (xCell == null) {
				xCell = xRow.createCell(lastColumns);
				// 设置最后一列记录异常信息单元格的列宽度
				// //writeSheet.setColumnWidth(lastColumns,
				// writeSheet.getColumnWidth(lastColumns)*3);
				xCell.setCellValue(" " + message + "\r\n");
			} else {
				String strValue = xCell.getStringCellValue();
				strValue = strValue + " " + message + "\r\n";
				xCell.setCellValue(strValue);
			}
		} catch (Exception e) {
			LogHome.getLog().error("", e);
			throw new Exception(e);
		}
	}

	/**
	 * 验证两个都不能为空的方法
	 * 
	 * @param ws
	 *            打开的的副本ws环境可以对单元格进行操作的
	 * @param sheet
	 *            第一个sheet表
	 * @param i
	 *            当前第几行
	 * @param j
	 *            当前第几列
	 * @param lastColumns
	 *            总列数
	 * @throws Exception
	 *             验证Label的类型是否为空，不正确将错误打印到最后一列中去
	 */
	protected static void verificationEither(Workbook writeWorkBook,
			Sheet writeSheet, int j, int eitherj, int i, int lastColumns,
			String constantValue2, String constantValue) throws Exception {
		try {
			Row xRow = writeSheet.getRow(i);
			updateExcelFormat(writeWorkBook, writeSheet, i, j);
			updateExcelFormat(writeWorkBook, writeSheet, i, eitherj);
			// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
			Cell xCell = xRow.getCell(lastColumns);
			if (xCell == null) {
				xCell = xRow.createCell(lastColumns);
				// 设置最后一列记录异常信息单元格的列宽度
				// writeSheet.setColumnWidth(lastColumns,
				// writeSheet.getColumnWidth(lastColumns)*3);
				xCell.setCellValue("[" + constantValue + "]与[" + constantValue2
						+ "]属性值不能同时为空 \r\n");
			} else {
				String strValue = xCell.getStringCellValue();
				strValue = strValue + "[" + constantValue + "]与["
						+ constantValue2 + "]属性值不能同时为空 \r\n";
				xCell.setCellValue(strValue);
			}
		} catch (Exception e) {
			LogHome.getLog().error("验证两个属性不能同时为空出错，请您核实您需要校验的信息", e);
			throw new Exception("验证两个属性不能同时为空出错，请您核实您需要校验的信息！");
		}

	}

	/**
	 * 提示所属EMS信息为空
	 */
	protected static void verificationEmsEmpty(Workbook writeWorkBook,
			Sheet writeSheet, int j, int i, int lastColumns) throws Exception {
		try {
			Row xRow = writeSheet.getRow(i);
			Object cellContant = xRow.getCell(j);
			if (cellContant == null
					|| StringUtils.isEmpty(cellContant.toString())) {
				updateExcelFormat(writeWorkBook, writeSheet, i, j);
				// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
				Row initRow = writeSheet.getRow(0);
				Object initContant = initRow.getCell(j);
				Cell xCell = xRow.getCell(lastColumns);
				if (xCell == null) {
					xCell = xRow.createCell(lastColumns);
					// 设置最后一列记录异常信息单元格的列宽度
					// writeSheet.setColumnWidth(lastColumns,
					// writeSheet.getColumnWidth(lastColumns)*3);
					xCell.setCellValue("[" + initContant.toString()
							+ "]的所属EMS信息不能为空 \r\n");
				} else {
					String strValue = xCell.getStringCellValue();
					strValue = strValue + "[" + initContant.toString()
							+ "]的所属EMS信息不能为空 \r\n";
					xCell.setCellValue(strValue);
				}
			}
		} catch (Exception e) {
			LogHome.getLog().error("verificationEmpty校验EMS是否为空出错!", e);
			throw new Exception(e);
		}
	}

	/**
	 * 提示FDN信息为空
	 */
	protected static void verificationFdnEmpty(Workbook writeWorkBook,
			Sheet writeSheet, int j, int i, int lastColumns) throws Exception {
		try {
			Row xRow = writeSheet.getRow(i);
			Object cellContant = xRow.getCell(j);
			if (cellContant == null
					|| StringUtils.isEmpty(cellContant.toString())) {
				updateExcelFormat(writeWorkBook, writeSheet, i, j);
				// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
				Row initRow = writeSheet.getRow(0);
				Object initContant = initRow.getCell(j);
				Cell xCell = xRow.getCell(lastColumns);
				if (xCell == null) {
					xCell = xRow.createCell(lastColumns);
					// 设置最后一列记录异常信息单元格的列宽度
					// writeSheet.setColumnWidth(lastColumns,
					// writeSheet.getColumnWidth(lastColumns)*3);
					xCell.setCellValue("[" + initContant.toString()
							+ "]的FDN信息不能为空 \r\n");
				} else {
					String strValue = xCell.getStringCellValue();
					strValue = strValue + "[" + initContant.toString()
							+ "]的FDN信息不能为空 \r\n";
					xCell.setCellValue(strValue);
				}
			}
		} catch (Exception e) {
			LogHome.getLog().error("verificationEmpty校验FDN是否为空出错!", e);
			throw new Exception(e);
		}
	}

	/**
	 * 验证Label的类型是否为空，不正确将错误打印到最后一列中去
	 * 
	 * @param ws
	 *            打开的的副本ws环境可以对单元格进行操作的
	 * @param sheet
	 *            第一个sheet表
	 * @param i
	 *            当前第几行
	 * @param j
	 *            当前第几列
	 * @param lastColumns
	 *            总列数
	 * @throws Exception
	 *             验证Label的类型是否为空，不正确将错误打印到最后一列中去
	 */
	protected static void verificationEmpty(Workbook writeWorkBook,
			Sheet writeSheet, int j, int i, int lastColumns,
			String constantValue) throws Exception {
		try {
			Row xRow = writeSheet.getRow(i);
			Object cellContant = xRow.getCell(j);
			if (cellContant == null
					|| StringUtils.isEmpty(cellContant.toString())) {
				updateExcelFormat(writeWorkBook, writeSheet, i, j);
				// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
				Row initRow = writeSheet.getRow(0);
				Object initContant = initRow.getCell(j);
				Cell xCell = xRow.getCell(lastColumns);
				if (xCell == null) {
					xCell = xRow.createCell(lastColumns);
					// 设置最后一列记录异常信息单元格的列宽度
					// writeSheet.setColumnWidth(lastColumns,
					// writeSheet.getColumnWidth(lastColumns)*3);
					xCell.setCellValue("[" + initContant.toString() + "]"
							+ "不能为空 \r\n");
				} else {
					String strValue = xCell.getStringCellValue();
					strValue = strValue + "[" + initContant.toString() + "]"
							+ "不能为空 \r\n";
					xCell.setCellValue(strValue);
				}
			}
		} catch (Exception e) {
			LogHome.getLog().error("verificationEmpty校验导入属性字段是否为空出错!", e);
			throw new Exception(e);
		}
	}

	/*
	 * 查看改vlan在表中是否被占用
	 */
	protected static void verificationVlan(Workbook writeWorkBook,
			Sheet writeSheet, int j, int i, int lastColumns,
			String constantValue, String excelName) throws Exception {
		try {
			Row xRow = writeSheet.getRow(i);
			Object cellContant = xRow.getCell(j);
			if (cellContant != null
					|| !StringUtils.isEmpty(cellContant.toString())) {
				String tableName = "";
				if (excelName.equals(Constant.OLTPORTNAME)) {
					tableName = "olt";
				} else if (excelName.equals(Constant.ONUPORTNAME)) {
					tableName = "onu";
				}
				if (!StringUtils.isEmpty(tableName)) {
					String vlanStr = cellContant.toString();
					if (isDouble(vlanStr)) {
						vlanStr = getValue(vlanStr);
					}
					if (isInteger(vlanStr)) {
						boolean isExist = oltManageBO.isVlanNameExist(
								Integer.parseInt(vlanStr), tableName);
						if (isExist) {
							updateExcelFormat(writeWorkBook, writeSheet, i, j);
							// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
							Row initRow = writeSheet.getRow(0);
							Object initContant = initRow.getCell(j);
							Cell xCell = xRow.getCell(lastColumns);
							if (xCell == null) {
								xCell = xRow.createCell(lastColumns);
								// 设置最后一列记录异常信息单元格的列宽度
								// writeSheet.setColumnWidth(lastColumns,
								// writeSheet.getColumnWidth(lastColumns)*3);
								xCell.setCellValue("[" + initContant.toString()
										+ "]" + "已经被占用！\r\n");
							} else {
								String strValue = xCell.getStringCellValue();
								strValue = strValue + "["
										+ initContant.toString() + "]"
										+ "已经被占用！ \r\n";
								xCell.setCellValue(strValue);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			LogHome.getLog().error("板卡端口校验vlan是否存在报错!", e);
			throw new Exception(e);
		}
	}

	/**
	 * 相同的网元名称下板卡的架框槽(FDN)相同
	 */
	protected static boolean verificationSameCardFdn(Workbook writeWorkBook,
			Sheet writeSheet, int j, int i, int lastColumns,
			String constantValue) throws Exception {
		try {
			Row xRow = writeSheet.getRow(i);
			updateExcelFormat(writeWorkBook, writeSheet, i, j);
			// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
			Row initRow = writeSheet.getRow(0);
			Cell xCell = xRow.getCell(lastColumns);
			if (xCell == null) {
				xCell = xRow.createCell(lastColumns);
				xCell.setCellValue("同一网元下相同板卡的所属机槽编号必须相同\r\n");
				return false;
			} else {
				String strValue = xCell.getStringCellValue();
				strValue = strValue + "同一网元下相同板卡的所属机槽编号必须相同 \r\n";
				xCell.setCellValue(strValue);
				return false;
			}
		} catch (Exception e) {
			LogHome.getLog().error("", e);
			throw new Exception(e);
		}
	}

	/**
	 * 相同的网元名称下板卡的架框槽(FDN)相同
	 * 
	 * @param ws
	 *            打开的的副本ws环境可以对单元格进行操作的
	 * @param sheet
	 *            第一个sheet表
	 * @param i
	 *            当前第几行
	 * @param j
	 *            当前第几列
	 * @param lastColumns
	 *            总列数
	 * @throws Exception
	 *             验证Label的类型是否为空，不正确将错误打印到最后一列中去
	 */
	protected static boolean verificationSameFdnCard(Workbook writeWorkBook,
			Sheet writeSheet, int j, int i, int lastColumns,
			String constantValue) throws Exception {
		try {
			Row xRow = writeSheet.getRow(i);
			Object cellContant = xRow.getCell(j);
			updateExcelFormat(writeWorkBook, writeSheet, i, j);
			// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
			Row initRow = writeSheet.getRow(0);
			Object initContant = initRow.getCell(j);
			Cell xCell = xRow.getCell(lastColumns);
			if (xCell == null) {
				xCell = xRow.createCell(lastColumns);
				xCell.setCellValue("同一网元下相同所属机槽编号的板卡必须相同\r\n");
				return false;
			} else {
				String strValue = xCell.getStringCellValue();
				strValue = strValue + "同一网元下相同所属机槽编号的板卡必须相同 \r\n";
				xCell.setCellValue(strValue);
				return false;
			}
		} catch (Exception e) {
			LogHome.getLog().error("", e);
			throw new Exception(e);
		}
	}

	/**
	 * 同一板卡名称下端口的槽位(FDN)相同
	 * 
	 * @param ws
	 *            打开的的副本ws环境可以对单元格进行操作的
	 * @param sheet
	 *            第一个sheet表
	 * @param i
	 *            当前第几行
	 * @param j
	 *            当前第几列
	 * @param lastColumns
	 *            总列数
	 * @throws Exception
	 *             验证Label的类型是否为空，不正确将错误打印到最后一列中去
	 */
	protected static boolean verificationSamePortFdn(Workbook writeWorkBook,
			Sheet writeSheet, int j, int i, int lastColumns,
			String constantValue) throws Exception {
		try {
			Row xRow = writeSheet.getRow(i);
			/**
			 * 
			 * 此处，貌似有三行无用代码入参也有无用传参 问： 敢删么? 答：不敢 问：为何 答：怂
			 */
			Object cellContant = xRow.getCell(j);
			updateExcelFormat(writeWorkBook, writeSheet, i, j);
			// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
			Row initRow = writeSheet.getRow(0);
			Object initContant = initRow.getCell(j);
			Cell xCell = xRow.getCell(lastColumns);
			if (xCell == null) {
				xCell = xRow.createCell(lastColumns);
				xCell.setCellValue("同一板卡名称下,端口编号不能相同 \r\n");
				return false;
			} else {
				String strValue = xCell.getStringCellValue();
				strValue = strValue + "同一网元名称下,端口编号不能相同 \r\n";
				xCell.setCellValue(strValue);
				return false;
			}
		} catch (Exception e) {
			LogHome.getLog().error("", e);
			throw new Exception(e);
		}
	}

	protected static boolean isSamePortFdn(Workbook writeWorkBook,
			Sheet writeSheet, String neName, String cardName, String ptpStr,
			String ptpnNo, int portNoInt, int i, int lastColumns,
			String constantValue, String neCuid) throws Exception {
		try {
			Row xRow = writeSheet.getRow(i);
			Cell cellContant = xRow.getCell(portNoInt);
			List list = oltManageBO
					.isSameCardPtpExist(cardName, ptpnNo, neCuid);
			if (!list.isEmpty()) {
				String labelCn = ((Map) list.get(0)).get(Ptp.AttrName.labelCn)
						.toString();
				if (!labelCn.equals(ptpStr)) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			LogHome.getLog().error("", e);
			throw new Exception(e);
		}
	}

	protected static boolean isSamePortFdn(Workbook writeWorkBook,
			Sheet writeSheet, String strCardName, int portName, int portNoInt,
			int i, int lastColumns, String constantValue, String neCuid)
			throws Exception {
		try {
			Row xRow = writeSheet.getRow(i);
			Cell cellContant = xRow.getCell(portNoInt);
			Cell cellportName = xRow.getCell(portName);
			List list = oltManageBO.isSameCardPtpExist(strCardName,
					getCellValue(cellContant), neCuid);
			if (!list.isEmpty()) {
				String labelCn = ((Map) list.get(0)).get(Ptp.AttrName.labelCn)
						.toString();
				if (!labelCn.equals(cellportName.toString())) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			LogHome.getLog().error("", e);
			throw new Exception(e);
		}
	}

	/**
	 * 验证Number的类型是否正确，不正确将错误打印到最后一列中去
	 * 
	 * @param ws
	 *            打开的的副本ws环境可以对单元格进行操作的
	 * @param sheet
	 *            第一个sheet表
	 * @param i
	 *            当前第几行
	 * @param j
	 *            当前第几列
	 * @param lastColumns
	 *            总列数
	 * @throws Exception
	 *             验证Number的类型是否正确，不正确将错误打印到最后一列中去
	 */
	protected static void verificationNumber(Workbook writeWorkBook,
			Sheet writeSheet, int j, int i, int lastColumns,
			String constantValue) throws Exception {
		boolean flag = false;
		try {
			Row xRow = writeSheet.getRow(i);
			Cell xCell = xRow.getCell(j);
			if (xCell != null && !StringUtils.isEmpty(xCell.toString())) {
				if (xCell.getCellType() == Cell.CELL_TYPE_STRING) {
					if (isNumber(xCell.getStringCellValue())) {
						flag = true;
					}
				} else if (xCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
					String dblContent = Double.toString(xCell
							.getNumericCellValue());
					if (isNumber(dblContent)) {
						flag = true;
					}
				}
				if (!flag) {
					updateExcelFormat(writeWorkBook, writeSheet, i, j);
					// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
					Cell Cell = xRow.getCell(lastColumns);
					if (Cell == null) {
						Cell = xRow.createCell(lastColumns);
						// 设置最后一列记录异常信息单元格的列宽度
						// writeSheet.setColumnWidth(lastColumns,
						// writeSheet.getColumnWidth(lastColumns)*3);
						Cell.setCellValue("[" + xCell.toString() + "]"
								+ "属性值必须是数字类型 \r\n");
					} else {
						String strValue = Cell.getStringCellValue();
						strValue = strValue + "[" + xCell.toString() + "]"
								+ "属性值必须是数字类型\r\n";
						Cell.setCellValue(strValue);
					}

				}
			}// 无值不操作

		} catch (Exception e) {
			LogHome.getLog().error("验证数字的类型出错，请您仔细核实一下您输入的信息", e);
			throw new Exception("验证数字的类型出错，请您仔细核实一下您输入的信息！");
		}

	}

	/**
	 * 验证Number的类型是否正确，不正确将错误打印到最后一列中去(VLAN:有可能是以逗号隔开的数字组成的字符串)
	 * 
	 * @param ws
	 *            打开的的副本ws环境可以对单元格进行操作的
	 * @param sheet
	 *            第一个sheet表
	 * @param i
	 *            当前第几行
	 * @param j
	 *            当前第几列
	 * @param lastColumns
	 *            总列数
	 * @throws Exception
	 *             验证Number的类型是否正确，不正确将错误打印到最后一列中去
	 */
	protected static void verificationVLANNumber(Workbook writeWorkBook,
			Sheet writeSheet, int j, int i, int lastColumns,
			String constantValue) throws Exception {
		boolean flag = false;
		try {
			Row xRow = writeSheet.getRow(i);
			Cell xCell = xRow.getCell(j);
			if (xCell != null && !StringUtils.isEmpty(xCell.toString())) {
				if (xCell.getCellType() == Cell.CELL_TYPE_STRING) {
					flag = checkVlanValue(xCell.getStringCellValue());
					/*
					 * if(isNumber(xCell.getStringCellValue())){ flag=true; }
					 */
				} else if (xCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
					String dblContent = Double.toString(xCell
							.getNumericCellValue());
					if (isNumber(dblContent)) {
						flag = true;
					}
				}
				if (!flag) {
					updateExcelFormat(writeWorkBook, writeSheet, i, j);
					// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
					Cell Cell = xRow.getCell(lastColumns);
					if (Cell == null) {
						Cell = xRow.createCell(lastColumns);
						// 设置最后一列记录异常信息单元格的列宽度
						// writeSheet.setColumnWidth(lastColumns,
						// writeSheet.getColumnWidth(lastColumns)*3);
						Cell.setCellValue("[" + xCell.toString() + "]"
								+ "属性值必须是数字类型 \r\n");
					} else {
						String strValue = Cell.getStringCellValue();
						strValue = strValue + "[" + xCell.toString() + "]"
								+ "属性值必须是数字类型\r\n";
						Cell.setCellValue(strValue);
					}

				}
			}// 无值不操作

		} catch (Exception e) {
			LogHome.getLog().error("验证数字的类型出错，请您仔细核实一下您输入的信息", e);
			throw new Exception("验证数字的类型出错，请您仔细核实一下您输入的信息！");
		}

	}

	private static boolean checkVlanValue(String vlanValue) {
		boolean vlanflag = true;
		String[] vlanValueArray = vlanValue.split(",");
		for (int i = 0; i < vlanValueArray.length; i++) {
			if (!isNumber(vlanValueArray[i])) {
				vlanflag = false;
				break;
			}
		}
		return vlanflag;
	}

	/**
	 * 判断字符串是否是整数
	 */
	protected static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * 判断字符串是否是浮点数
	 */
	public static boolean isDouble(String value) {
		try {
			Double.parseDouble(value);
			if (value.contains(".")) {
				return true;
			} else {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * 判断字符串是否是数字
	 */
	public static boolean isNumber(String value) {
		return isInteger(value) || isDouble(value);
	}

	// 判断cell的类型
	public static String getCellValue(Cell c) {
		String value = "";
		if (c != null) {
			if (c.getCellType() == HSSFCell.CELL_TYPE_STRING) {
				value = c.getStringCellValue().trim();
			} else if (c.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
				if (DateUtil.isCellDateFormatted(c)) {
					Date d = c.getDateCellValue();
					SimpleDateFormat resFormat = null;
					if (d.getHours() > 0) {
						resFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					} else {
						resFormat = new SimpleDateFormat("yyyy-MM-dd");
					}
					value = resFormat.format(d);
				} else {
					c.setCellType(HSSFCell.CELL_TYPE_STRING);
					String temp = c.getStringCellValue();
					if (temp.indexOf(".") > -1) {
						value = String.valueOf(new Double(temp)).trim();
					} else {
						value = temp.trim();
					}
				}
			} else if (c.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
				value = Boolean.toString(c.getBooleanCellValue());
			} else if (c.getCellType() == HSSFCell.CELL_TYPE_ERROR) {
				value = "";
			} else if (c.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
				value = "";
			} else if (c.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
				c.setCellType(HSSFCell.CELL_TYPE_STRING);
				value = c.getStringCellValue();
				if (value != null) {
					value = value.replaceAll("#N/A", "").trim();
				}
			} else {
				value = c.toString().trim();
			}
		}
		return value;
	}

	// 将科学计数法还原【正则表达式】
	public static String getValue(String account) {
		String regex = "^((\\d+.?\\d+)[Ee]{1}(\\d+))$";
		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(account);
		boolean b = m.matches();
		if (b) {
			DecimalFormat df = new DecimalFormat("#");
			account = df.format(Double.parseDouble(account));
		}
		return String.valueOf((int) Double.parseDouble(account));
	}

	/**
	 * 验证Date的类型是否正确，不正确将错误打印到最后一列中去
	 * 
	 * 打开的的副本ws环境可以对单元格进行操作的
	 * 
	 * @param sheet
	 *            第一个sheet表
	 * @param i
	 *            当前第几行
	 * @param j
	 *            当前第几列
	 * @param lastColumns
	 *            总列数
	 * @throws Exception
	 *             验证Date的类型是否正确，不正确将错误打印到最后一列中去
	 */
	protected static void verificationDate(Workbook writeWorkBook,
			Sheet writeSheet, int j, int i, int lastColumns,
			String constantValue) throws Exception {
		Row xRow = writeSheet.getRow(i);
		Cell xCell = xRow.getCell(j);
		if (xCell == null) {
			return;
		}
		try {
			String val = "";
			boolean flag = false;
			if (xCell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
				SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
				Date dt = HSSFDateUtil.getJavaDate(xCell.getNumericCellValue());
				val = dateformat.format(dt);
			} else {
				val = xRow.getCell((short) j).getStringCellValue();
			}
			Date datResult = TimeFormatHelper.checkIsDate(val);
			if (datResult != null) {
				flag = true;
			}
			if (!flag) {
				updateExcelFormat(writeWorkBook, writeSheet, i, j);

				// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
				Cell cell = xRow.getCell(lastColumns);
				if (cell == null) {
					cell = xRow.createCell(lastColumns);
					// 设置最后一列记录异常信息单元格的列宽度
					// writeSheet.setColumnWidth(lastColumns,
					// writeSheet.getColumnWidth(lastColumns)*3);
					if (!StringUtils.isEmpty(val)) {
						cell.setCellValue(xCell.toString() + "日期格式不正确\r\n");
					}
				} else {
					if (!StringUtils.isEmpty(val)) {
						cell.setCellValue(cell.getStringCellValue()
								+ xCell.toString() + "日期格式不正确\r\n");
					}
				}
			}
		} catch (Exception e) {
			LogHome.getLog().error("验证日期类型出错", e);
			throw new Exception("验证日期类型出错");
		}

	}

	/**
	 * 将list转换成map1
	 * 
	 * @param list
	 * @return
	 */
	protected static Map changeListToMap(List list) {
		Map map = new HashMap();
		if (list != null && !list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Map subMap = (Map) list.get(i);
				Object labelCnObj = subMap.get("LABEL_CN");
				// 如果是二级分光则删除前面添加的把二级分光添加进去
				if (subMap.get("POSTYPE") != null
						&& map.containsKey(labelCnObj)) {
					if (subMap.get("POSTYPE").toString().equals("2")) {
						map.remove(labelCnObj);
					} else {
						subMap = (Map) map.get(labelCnObj);
					}
				}
				map.put(labelCnObj, subMap);
			}
		}
		return map;
	}

	/**
	 * 覆盖范围将list转换成map
	 * 
	 * @param list
	 * @return
	 */
	protected static Map newChangeListToMap(List list) {
		Map map = new HashMap();
		for (int i = 0; i < list.size(); i++) {
			Map subMap = (Map) list.get(i);
			map.put(subMap.get("RELATED_NE_CUID").toString()
					+ subMap.get("COVER_RANGE"), subMap);
		}
		return map;
	}

	/**
	 * 将list转换成map2//此方法，与上个方法相同，但是这个是转换成两个参数，都是String【将查处的cuid和label_cn进行组合】
	 * 
	 * @param list
	 * @return
	 */
	protected static Map changeListToMap2(List list) {
		Map map = new HashMap();
		for (int i = 0; i < list.size(); i++) {
			Map subMap = (Map) list.get(i);
			String name = (String) subMap.get("LABEL_CN");
			String cuid = (String) subMap.get("CUID");
			map.put(name, cuid);
		}
		return map;
	}

	/**
	 * 批量修改OLT的设备维护单和线路维护单位
	 */
	protected static void saveExcelData(String flag, List dataList,
			String excelName, ImportResultDO importResultDO) throws Exception {
		try {
			if ("UPDATE_OLT_MAINTAIN".equals(flag)) {
				if (!dataList.isEmpty()) {
					getImportBasicDataBO().importOltBatchUpdate(dataList,
							excelName);
					importResultDO.setInfo("更新数据:" + dataList.size() + "条");
				}
			}
			importResultDO.setSuccess(true);
		} catch (Exception e) {
			LogHome.getLog().error(excelName + "导入数据库入库失败", e);
			throw new Exception(excelName + "导入数据库入库失败，请联系管理员！");
		}
	}

	/**
	 * 批量添加和修改card与ptp, 更新板卡时是否动态更新端口名称control
	 */
	protected static void ImportPonCardPtpExcelToDB(List<Map> objList,
			Map<String, Map> cardMaps, String excelName, String control,
			ImportResultDO importResultDO) throws Exception {
		try {
			//List<Map<String, String>> cuidList = new ArrayList<Map<String, String>>();
			//POS和ONU端口导入时，不新增板卡 2016年4月19日09:50:47--为什么不创建？？？
			/**
			 * 2016.4.25  bug:导入端口后,card未能生成
			 * 
			 * 吉林：导入PORT CARD 未生成 原因分析
			 * 导入模板中取消了  POS的虚拟CARD信息
			 * 而 POS的 PORT 是挂在  虚拟的 CARD上
			 * 的, 在做 excel解析的时候,CODE中对于 创建CARD 的
			 * labelCn 为空,而且只保存OLT的板卡,
			 * 顾在做PORT的Datas 固化的时候,并未创建对应
			 * PORT的CARD
			 * 
			 * 
			 * 解决方案：
			 * 1.提取所有PORT对应的POS的LIST
			 * 2.迭代LIST查询其下若有虚拟板卡则返回板卡信息,若无,则创建虚拟CARD返回
			 * 3.根据返回的CARD信息 ,插入PORT数据
			 * 注意：ONU也存在类似问题
			 */
			//Set<String>   canImportPortTypes = new HashSet<String>(); 这块代码，完全没必要加
			//canImportPortTypes.addAll(Arrays.asList(new String[]{Constant.OLTPORTNAME, Constant.POSPORTNAME, Constant.ONUPORTNAME})) ;
			//if(canImportPortTypes.contains(excelName)){
			
			if (cardMaps != null && cardMaps.size() > 0) {
				List updateCardList = new ArrayList();
				List updateCardList1 = new ArrayList();
				List insertCardList = new ArrayList();
				for (String k : cardMaps.keySet()) {
					Map card = cardMaps.get(k);
					String pcType = card.get("TYPE") + "";
					if ("update".equals(pcType)) {
						if(excelName.equals(Constant.OLTPORTNAME)){
							updateCardList.add(card);
						}else{
							updateCardList1.add(card);
						}
					}
					if ("add".equals(pcType)) {
						insertCardList.add(card);
					}
				}
				if (updateCardList != null && updateCardList.size() > 0) {
					getImportBasicDataBO().importCardBatchUpdate( updateCardList, excelName);
//					if ("updatePtpName".equals(control)) {
//						getImportBasicDataBO().updatePtpNameByCard( updateCardList, excelName);
//					}
				}
				if (updateCardList1 != null && updateCardList1.size() > 0) {
					getImportBasicDataBO().importCardBatchUpdate1(updateCardList1, excelName);
//					if ("updatePtpName".equals(control)) {
//						getImportBasicDataBO().updatePtpNameByCard( updateCardList, excelName);
//					}
				}
				importResultDO.setInfo("更新板卡数据:" + (updateCardList.size()+updateCardList1.size())
						+ "条");
				if (insertCardList != null && insertCardList.size() > 0) {
					getImportBasicDataBO().importCardBatchInsert(
							insertCardList, excelName);
					importResultDO.setInfo("新增板卡数据:" + insertCardList.size()
							+ "条");
				}
			}
			//}
			List updatePtpList = new ArrayList();
			List insertPtpList = new ArrayList();
			for (Map ptp : objList) {
				String pcType = ptp.get("TYPE") + "";
				if ("update".equals(pcType)) {
					updatePtpList.add(ptp);
				}
				if ("add".equals(pcType)) {
					insertPtpList.add(ptp);
				}
			}
			if (updatePtpList != null && updatePtpList.size() > 0) {
				getImportBasicDataBO().importPtpBatchUpdate(updatePtpList,
						excelName);
				importResultDO.setInfo("更新端口数据:" + updatePtpList.size() + "条");
			}
			if (insertPtpList != null && insertPtpList.size() > 0) {
				getImportBasicDataBO().importPtpBatchInsert(insertPtpList,
						excelName);
				importResultDO.setInfo("新增端口数据:" + insertPtpList.size() + "条");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogHome.getLog().error(excelName + "导入数据库入库失败", e);
			throw new Exception(excelName + "导入数据库入库失败，请联系管理员！");
		}
	}

	/**
	 * 批量添加和修改card与ptp
	 * 
	 * @param returnList
	 * @param originalList
	 * @param excelName
	 * @param control
	 * @param resultMap
	 * @return
	 * @throws Exception
	 */
	protected static void ImportExcelToDB(Map returnMap, List originalList,
			String excelName, String control, ImportResultDO importResultDO)
			throws Exception {
		try {
			String tableName = "";
			String tableStr = "";
			if (excelName.contains("*")) {
				String[] name = excelName.split("\\*");
				tableName = name[1];
				excelName = name[0];
			}
			List updateList = new ArrayList();
			List addList = new ArrayList();
			List<Map<String, String>> vlanList = new ArrayList<Map<String, String>>();
			for (int k = 0; k < originalList.size(); k++) {
				Map map = (Map) originalList.get(k);
				if (map.get(Ptp.AttrName.vlan) != null
						&& !StringUtils.isEmpty(map.get(Ptp.AttrName.vlan)
								.toString())) {
					// map.put(Ptp.AttrName.vlan,getValue(map.get(Ptp.AttrName.vlan).toString()));
					// map.put(Ptp.AttrName.vlan,ObjectUtils.toString(Ptp.AttrName.vlan));
				}
				String cuid = (String) map.get("CUID");
				if (cuid != null &&  !StringUtils.isEmpty(cuid.toString())) {
					updateList.add(map);
				} else {
					/* 针对 AN_POS,AN_ONU表中的设备CUID新增为空的BUG进行修改 2016-11-09 @auth guoxiaoqiang */
					cuid = AnmsUtilBO.getCuidByClassName(TransElement.CLASS_NAME);
					map.put(AnPos.AttrName.cuid, cuid);
					addList.add(map);
				}
				if (excelName.equals(Constant.ONUNAME)) {
					// 对ONU链路以及所属OLT，FDN信息进行更新
					oltManageBO.updateOnuPosTopo(map);
				}
				if (excelName.equals(Constant.POSNAME)) {
					// 对POS链路以及FDN信息进行更新
					oltManageBO.updatePosTopo(map);
				}
			}
			
			List<String> newPortList = new ArrayList<String>();
			List<String> oldPortList = new ArrayList<String>();
			
			if (!addList.isEmpty()) {
				//更新覆盖范围信息
				//checkTRofhFullAddressInfo(excelName, addList);
				//批量新增信息
				getImportBasicDataBO().importBatchInsert(addList, excelName);
				if(excelName.equals(Constant.ONUNAME)||excelName.equals(Constant.POSNAME)){
					//如果是POS设备或ONU设备导入新增，则更新板卡信息
					for (int i = 0; i < addList.size(); i++) {
						Map map = (Map) addList.get(i);
						String neCuid = ObjectUtils.toString(map.get("CUID"));
						String neFdn = ObjectUtils.toString(map.get("FDN"));
						if (excelName.equals(Constant.ONUNAME)) {
							getAnOltManageBO().createCardInfo(neCuid, neFdn, "ONU", 2);
							newPortList.add(ObjectUtils.toString(map.get("RELATED_POS_PORT_CUID")));
						}
						if (excelName.equals(Constant.POSNAME)) {
							getAnOltManageBO().createCardInfo(neCuid, neFdn, "POS", 3);
							newPortList.add(ObjectUtils.toString(map.get("RELATED_UPNE_PORT_CUID")));
						}
					}
				}
			}
			if (!updateList.isEmpty()) {
				//更新覆盖范围信息
				//checkTRofhFullAddressInfo(excelName, updateList);
				//批量更新信息
				getImportBasicDataBO().importOltBatchUpdate(updateList,excelName);
				if(excelName.equals(Constant.ONUNAME)||excelName.equals(Constant.POSNAME)){
					//如果是POS设备或ONU设备导入修改，则更新板卡信息
					for (int i = 0; i < updateList.size(); i++) {
						Map map = (Map) updateList.get(i);
						String neCuid = ObjectUtils.toString(map.get("CUID"));
						String neFdn = ObjectUtils.toString(map.get("FDN"));
						// 根据需求，在页面新增onu的时候，不新建板卡
						/*if (excelName.equals(Constant.ONUNAME)) {
							getAnOltManageBO().createCardInfo(neCuid, neFdn, "ONU", 2);
						}*/
						if (excelName.equals(Constant.POSNAME)) {
							getAnOltManageBO().createCardInfo(neCuid, neFdn, "POS", 3);
						}
						
						//更新端口状态
						String portName = excelName.equals(Constant.ONUNAME) ? "RELATED_POS_PORT_CUID":"RELATED_UPNE_PORT_CUID";
						String newPort = ObjectUtils.toString(map.get(portName));
						String oldPort = String.valueOf(((Map)returnMap.get(map.get("LABEL_CN"))).get(portName));
						if(!newPort.equals(oldPort)){
							newPortList.add(newPort);
							oldPortList.add(oldPort);
						}
					}
					
				}
				//更新端口状态
				//TODO:如果新端口中有重复的，需要提示用户端口被占用
				if(!newPortList.isEmpty()){
					getImportBasicDataBO().updatePortState(newPortList, "2");
				}
				if(!oldPortList.isEmpty()){
					getImportBasicDataBO().updatePortState(oldPortList, "1");
				}
			}
			importResultDO.setInfo("添加数据:" + addList.size() + "条");
			importResultDO.setInfo("更新数据:" + updateList.size() + "条");
			importResultDO.setSuccess(true);
		} catch (Exception e) {
			LogHome.getLog().error(excelName + "导入数据库入库失败", e);
			throw new Exception(excelName + "导入数据库入库失败，请联系管理员！");
		}

	}
	
	/**
	 * 根据设备安装地址自动生成标准地址 取消自动生成标准地址和业务区，只生成覆盖范围2015-08-28
	 */
	private static void checkTRofhFullAddressInfo(String tableName,
			List dataList) {
		try {
			if (dataList != null && !dataList.isEmpty()) {
				String devTable = "";
				if (tableName.equals(Constant.ONUNAME)) {
					devTable = "onu";
				} else if (tableName.equals(Constant.POSNAME)) {
					devTable = "pos";
				}
				if (!StringUtils.isEmpty(devTable)) {
					int length = dataList.size();
					for (int i = 0; i < length; i++) {
						boolean isAddGponCover = false;
						Map map = (Map) dataList.get(i);
						if (devTable.equals("onu")
								&& "1".equals(map.get(AnOnu.AttrName.fttx)
										.toString())
								&& ("2".equals(map.get(AnOnu.AttrName.accessType) + "")
										|| "3".equals(map.get(AnOnu.AttrName.accessType) + "") || "5".equals(map.get(AnOnu.AttrName.accessType) + ""))) {
							isAddGponCover = true;
						}
						if (devTable.equals("pos")
								&& "1".equals(map.get("CAN_ALLOCATE_TO_USER")
										.toString())
								&& ("2".equals(map
										.get(AnPos.AttrName.accessType) + "")
										|| "3".equals(map
												.get(AnPos.AttrName.accessType)
												+ "") || "5".equals(map
										.get(AnPos.AttrName.accessType) + ""))) {
							isAddGponCover = true;
						}
						getTRofhFullAddressBO().checkTRofhFullAddress(map,isAddGponCover);
					}
				}
			}
		} catch (Exception ex) {
			LogHome.getLog().error("", ex);
		}
	}

	private static TRofhFullAddressBO getTRofhFullAddressBO() {
		return (TRofhFullAddressBO) SpringContextUtil
				.getBean(AnmsBoName.TRofhFullAddressBO);
	}

	private static void checkVlan(String tableName, List dataList, String type) {
		try {
			if (dataList != null && !dataList.isEmpty()) {
				String devTable = "";
				if (tableName.equals(Constant.ONUPORTNAME)) {
					devTable = "onu";
				} else if (tableName.equals(Constant.OLTPORTNAME)) {
					devTable = "olt";
				}
				if (!StringUtils.isEmpty(devTable)) {
					int length = dataList.size();
					for (int i = 0; i < length; i++) {
						Map map = (Map) dataList.get(i);
						if (map.get(Ptp.AttrName.vlan) != null
								&& map.get(Ptp.AttrName.relatedNeCuid) != null
								&& map.get(Ptp.AttrName.cuid) != null) {
							String vlan = map.get(Ptp.AttrName.vlan).toString();
							String relatedNeCuid = map.get(
									Ptp.AttrName.relatedNeCuid).toString();
							String portCuid = map.get(Ptp.AttrName.cuid)
									.toString();
							tLogicVlanBO.checkVlan(vlan, devTable,
									relatedNeCuid, portCuid, type);
						}
					}
				}
			}
		} catch (Exception ex) {
			LogHome.getLog().error("", ex);
		}
	}

	/*
	 * 添加数据时创建拓扑关系
	 */
	private static void makeInsertTopoRelation(String excelName, Map map)
			throws Exception {
		if (excelName.equals(Constant.OLTNAME)) {
			// OLT导入之前添加GPON_DEVICE_LINK表OLT与BRAS拓扑连接关系，PTP表判断添加数据
			// String chose = "1";//是导入的是1BRAS设备还是2BARS设备
			// try {
			// //判断添加PTP数据
			// String oltPortCuid = oltManageBO.getOltPortCuid(map,chose);
			// //添加GPON_DEVICE_LINK表
			// if(!StringUtils.isEmpty(oltPortCuid)){
			// oltManageBO.addBras(map,oltPortCuid,chose);
			// }
			// if(map.get("APORT2") != null &&
			// !StringUtils.isEmpty(map.get("APORT2").toString())){
			// String chose2 = "2";
			// //判断添加PTP数据
			// String oltPortCuid2 = oltManageBO.getOltPortCuid(map,chose2);
			// //添加GPON_DEVICE_LINK表
			// if(!StringUtils.isEmpty(oltPortCuid2)){
			// oltManageBO.addBras(map,oltPortCuid2,chose2);
			// }
			// }
			// } catch (Exception e) {
			// LogHome.getLog().error("导入OLT添加数据中创建OLT上联端口和拓扑关系报错！");
			// }
		} else if (excelName.equals(Constant.ONUNAME)) {
			// ONU导入判断上联端口添加板卡
			String upPortName = "RELATED_UP_PORT_CUID";
			String onuUpPortCuid = oltManageBO.makePortAndCard(map, upPortName,
					"onu");
			if (!StringUtils.isEmpty(onuUpPortCuid)) {
				// 创建ONU-POS拓扑关系
				oltManageBO.makeOnuPosTopo(map, onuUpPortCuid);
			}
		} else if (excelName.equals(Constant.POSNAME)) {
			String upPortName = "RELATED_OLT_PORT_CUID";
			String posUpPortCuid = oltManageBO.makePortAndCard(map, upPortName,
					"pos");
			if (!StringUtils.isEmpty(posUpPortCuid)) {
				// 创建ONU-POS拓扑关系
				oltManageBO.makePosPosTopo(map, posUpPortCuid);
			}
		}
	}

	/*
	 * 更新数据时创建拓扑关系
	 */
	private static void makeUplatedTopoRelation(String excelName, Map map)
			throws Exception {
		// OLT导入之前添加GPON_DEVICE_LINK表OLT与BRAS拓扑连接关系，PTP表判断添加数据
		if (excelName.equals(Constant.ONUNAME)) {
			// ONU导入判断上联端口添加板卡
			try {
				if (map != null && map.get("CUID") != null) {
					List<Map> topoInfo = oltManageBO
							.getPonTopoLinkInfoByZCuid(map.get("CUID")
									.toString());
					if (topoInfo != null && topoInfo.size() > 0) {
						Map topoMap = topoInfo.get(0);

					}
				}
				String upPortName = "RELATED_UP_PORT_CUID";
				String onuUpPortCuid = oltManageBO.makePortAndCard(map,
						upPortName, "onu");
				if (!StringUtils.isEmpty(onuUpPortCuid)) {
					// 创建ONU-POS拓扑关系
					oltManageBO.makeOnuPosTopo(map, onuUpPortCuid);
				}
			} catch (Exception e) {
				LogHome.getLog().error("导入ONU更新数据中创建ONU上联端口和拓扑关系报错！");
			}
		} else if (excelName.equals(Constant.POSNAME)) {
			String upPortName = "RELATED_OLT_PORT_CUID";
			String posUpPortCuid = oltManageBO.makePortAndCard(map, upPortName,
					"pos");
			if (!StringUtils.isEmpty(posUpPortCuid)) {
				// 创建ONU-POS拓扑关系
				oltManageBO.makePosPosTopo(map, posUpPortCuid);
			}
		}
	}

	/**
	 * 批量添加和修改覆盖范围
	 * 
	 * @param returnList
	 * @param originalList
	 * @param excelName
	 * @param control
	 * @param resultMap
	 * @return
	 * @throws Exception
	 */
	protected static void coverageImportExcelToDB(Map returnMap,
			List originalList, String excelName, String control,
			ImportResultDO importResultDO) throws Exception {
		try {
			List updateList = new ArrayList();
			List addList = new ArrayList();
			for (int k = 0; k < originalList.size(); k++) {
				Map map = (Map) originalList.get(k);
				String originalName = (String) map.get("RELATED_NE_CUID");
				String labelCn = (String) map.get("COVER_RANGE");
				if (returnMap.containsKey(originalName + labelCn)) {
					Map subMap = (Map) returnMap.get(originalName + labelCn);
					map.put("CUID", subMap.get("CUID"));
					map.put("OBJECTID", subMap.get("OBJECTID"));
					map.put("RELATED_NE_CUID", subMap.get("RELATED_NE_CUID"));
					// 如果策略为更新原有数据，则根据表格中的数据UPDATA,否则不更新数据库中的数据
					if (Constant.CONTROLUPADE.equals(control)) {
						updateList.add(map);
					}
				} else {
					addList.add(map);
				}
			}
			if (!addList.isEmpty()) {
				getImportBasicDataBO().importBatchInsert(addList, excelName);
			}

			if (!updateList.isEmpty()) {
				getImportBasicDataBO().importOltBatchUpdate(updateList,
						excelName);
			}
			importResultDO.setInfo("添加数据:" + addList.size() + "条");
			importResultDO.setInfo("更新数据:" + updateList.size() + "条");
			importResultDO.setSuccess(true);
		} catch (Exception e) {
			LogHome.getLog().error(excelName + "导入数据库入库失败", e);
			throw new Exception(excelName + "导入数据库入库失败，请联系管理员！");
		}
	}

	/**
	 * 得到Excel的第一行，进行表头验证
	 * 
	 * @param headingMap
	 * @param colName
	 * @return
	 */
	protected static int getColNumberByHeadingName(Map headingMap,
			String colName) {
		int colNum = Constant.MAXCOLUMN;
		try {
			colNum = Integer.valueOf((String) headingMap.get(colName));
		} catch (Exception e) {
			LogHome.getLog().error("", e);
			throw e;
		} finally {
			return colNum;
		}
	}

	/**
	 * 
	 * @param sheet
	 *            Sheet
	 * @param headingRow
	 *            int 标题所在行数
	 * @param errorlist
	 *            List
	 * @param headingDescript
	 *            String[] 标题描述
	 * @return Map key:标题描述 value:标题所在列数
	 */
	protected static Map getHeadingColumn(Sheet sheet, int headingRow,
			List<String> errorlist, String[] headName, int lastColumns) {
		Map returnMap = new HashMap();
		for (int i = 0; i < headName.length; i++) {
			returnMap.put(headName[i], "empty");
		}
		Row xRow = (Row) sheet.getRow(0);
		for (int i = 1; i < (lastColumns); i++) {
			Cell xCell = xRow.getCell(i);
			if (xCell != null) {
				String headingName = xCell.toString();
				if (returnMap.get(headingName) != null) {
					if (((String) returnMap.get(headingName)).equals("empty")) {
						returnMap.put(headingName, i + "");
					} else {
						errorlist.add("第 " + (headingRow + 1)
								+ " 行标题出现重复的标题[ \"" + headingName
								+ "\" ],请检查导入文件");
					}
				}
			} else {
				errorlist.add("导入文件的表头为空");
			}

		}
		for (int i = 0; i < headName.length; i++) {
			String headingCol = (String) returnMap.get(headName[i]);
			if (headingCol.equals("empty")) {
				errorlist.add("导入文件缺少[ \"" + headName[i] + "\" ]列");
			}
		}
		return returnMap;
	}

	/**
	 * 校验外键接入点的值是否存在
	 * 
	 * @param writeWorkBook
	 * @param writeSheet
	 * @param j
	 * @param lastColumns
	 * @param nameSet
	 * @param originalList
	 * @return
	 * @throws Exception
	 */
	protected static List verificationAccessPoint(Workbook writeWorkBook,
			Sheet writeSheet, int j, int lastColumns, Set nameSet,
			List originalList) throws Exception {
		boolean flagIsExist = false;
		try {
			List accesspointResultList = getImportBasicDataBO().isExistSetName(
					nameSet, Constant.RELATEDACCESSPOINT);
			Map accesspointResultMap = changeListToMap2(accesspointResultList);
			int length = originalList.size();
			for (int m = 0; m < length; m++) {
				flagIsExist = false;
				String accesspointCuid = "";
				Map mapSubList = (Map) originalList.get(m);
				String strAccesspointCuid = (String) mapSubList
						.get("RELATED_ACCESS_POINT");
				if (strAccesspointCuid != null
						&& !"".equals(strAccesspointCuid)) {
					if (accesspointResultMap.containsKey(strAccesspointCuid)) {
						flagIsExist = true;
						accesspointCuid = (String) accesspointResultMap
								.get(strAccesspointCuid);
					}
					if (flagIsExist) {
						// 把名称替换成CUID
						mapSubList.put("RELATED_ACCESS_POINT", accesspointCuid);
					} else {
						verificationForeign(writeWorkBook, writeSheet, j,
								m + 5, lastColumns, Constant.RELATEDACCESSPOINT);
					}
				}
			}
		} catch (Exception e) {
			LogHome.getLog().error("", e);
			throw new Exception(e);
		}
		return originalList;
	}

	/**
	 * 校验所属PON口的cuid库中是否存在
	 */
	protected static List verificationRelatedOltPtpCuid(Workbook writeWorkBook,
			Sheet writeSheet, int j, int lastColumns, Set nameSet,
			List originalList) throws Exception {
		boolean flagIsExist = false;
		try {
			List oltPortResultList = getImportBasicDataBO().isExistSetName(
					nameSet, Constant.RELATEDOLTPORT);
			Map oltPortResultMap = changeListToMap2(oltPortResultList);
			int length = originalList.size();
			if (oltPortResultMap.size() > 0) {
				for (int m = 0; m < length; m++) {
					flagIsExist = false;
					String relatedOltPortCuid = "";
					Map mapSubList = (Map) originalList.get(m);
					Object strOltPort = mapSubList.get("RELATED_OLT_PORT_CUID");
					if (strOltPort != null
							&& !StringUtils.isEmpty(strOltPort.toString())) {
						if (oltPortResultMap.containsKey(strOltPort.toString())) {
							flagIsExist = true;
							relatedOltPortCuid = (String) oltPortResultMap
									.get(strOltPort.toString());
						}
						if (flagIsExist) {
							// 把名称替换成CUID
							mapSubList.put("RELATED_OLT_PORT_CUID",
									oltPortResultMap.get(strOltPort));
						} else {
							verificationForeign(writeWorkBook, writeSheet, j,
									m + 5, lastColumns, Constant.RELATEDOLTPORT);
						}
					}
				}
			}
		} catch (Exception e) {
			LogHome.getLog().error("", e);
			throw new Exception(e);
		}
		return originalList;
	}

	/**
	 * 验证IP的是否符合ip的格式，不正确将错误打印到最后一列中去
	 * 
	 * @param ws
	 *            打开的的副本ws环境可以对单元格进行操作的
	 * @param sheet
	 *            第一个sheet表
	 * @param i
	 *            当前第几行
	 * @param j
	 *            当前第几列
	 * @param lastColumns
	 *            总列数
	 * @throws Exception
	 *             验证IP的是否符合ip的格式，不正确将错误打印到最后一列中去
	 */
	protected static void verificationIp(Workbook writeWorkBook,
			Sheet writeSheet, int j, int i, int lastColumns,
			String constantValue) throws Exception {
		try {
			Row xRow = writeSheet.getRow(i);
			Object cellContant = xRow.getCell(j);
			if (cellContant != null && !"".equals(cellContant.toString())) {
				// 定义正则表达式，假如ip符合ip的格式要求，就返回true，不匹配就返回false
				String regExp = "\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b";
				Pattern pattern = Pattern.compile(regExp);
				Matcher matcher = pattern.matcher(cellContant.toString());
				if (!matcher.find()) {
					updateExcelFormat(writeWorkBook, writeSheet, i, j);
					// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
					Cell xCell = xRow.getCell(lastColumns);
					if (xCell == null) {
						xCell = xRow.createCell(lastColumns);
						// 设置最后一列记录异常信息单元格的列宽度
						// writeSheet.setColumnWidth(lastColumns,
						// writeSheet.getColumnWidth(lastColumns)*3);
						xCell.setCellValue(cellContant.toString()
								+ "Ip的格式不正确 \r\n");
					} else {
						String strValue = xCell.getStringCellValue();
						strValue = strValue + cellContant.toString()
								+ "Ip的格式不正确 \r\n";
						xCell.setCellValue(strValue);
					}
				}
			}
		} catch (Exception e) {
			LogHome.getLog().error("验证IP格式出错，请您核实一下您的IP格式！", e);
			throw new Exception("验证IP格式出错，请您核实一下您的IP格式！");
		}
	}

	/**
	 * 验证枚举值是否存在，不正确将错误打印到最后一列中去
	 * 
	 * @param ws
	 *            打开的的副本ws环境可以对单元格进行操作的
	 * @param sheet
	 *            第一个sheet表
	 * @param i
	 *            当前第几行
	 * @param j
	 *            当前第几列
	 * @param lastColumns
	 *            总列数
	 * @throws Exception
	 *             验证枚举值是否存在，不正确将错误打印到最后一列中去
	 */
	protected static void verificationEnum(Workbook writeWorkBook,
			Sheet writeSheet, int j, int i, int lastColumns,
			String constantValue) throws Exception {
		try {
			Row xRow = writeSheet.getRow(i);
			Object cellContant = xRow.getCell(j);
			if (cellContant != null
					&& !StringUtils.isEmpty(cellContant.toString())) {
				if (!isEnum(constantValue, cellContant.toString())) {
					updateExcelFormat(writeWorkBook, writeSheet, i, j);
					// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
					Cell xCell = xRow.getCell(lastColumns);
					if (xCell == null) {
						xCell = xRow.createCell(lastColumns);
						// 设置最后一列记录异常信息单元格的列宽度
						// writeSheet.setColumnWidth(lastColumns,
						// writeSheet.getColumnWidth(lastColumns)*3);
						xCell.setCellValue("[" + constantValue + "]枚举值不存在 \r\n");
					} else {
						String strValue = xCell.getStringCellValue();
						strValue = strValue + "[" + cellContant.toString()
								+ "]枚举值不存在 \r\n";
						xCell.setCellValue(strValue);
					}
				}
			}
		} catch (Exception e) {
			LogHome.getLog().error("验证枚举值存在失败，请您仔细核实一下您的数据是否正确！", e);
			throw new Exception("验证枚举值存在失败，请您仔细核实一下您的数据是否正确！");
		}
	}

	/**
	 * 验证枚举值是否存在，一般的是必须存在的，否则就会出现不存在的值，true是存在，false是不存在
	 * 
	 * @param condition
	 *            条件（Excel的表名）
	 * @param name
	 *            单元格的内容
	 * @throws Exception
	 *             验证枚举值是否存在，一般的是必须存在的，否则就会出现不存在的值，true是存在，false是不存在
	 */
	protected static boolean isEnum(String condition, String name) {
		boolean flag = false;
		Map subEnum = new HashMap();
		if (Constant.LIVECYCLE.equals(condition)) {
			subEnum = ElementEnum.LIVE_CYCLE.getAllEnum();
		} else if (Constant.OWNERSHIP.equals(condition)) {
			subEnum = NetEnum.OWNER_SHIP_CH.getAllEnum();
		} else if (Constant.OWNERSHIPMAN.equals(condition)) {
			subEnum = AnmsConst.OWNERSHIP_MAN.getAllEnum();
		} else if (Constant.MAINTMODE.equals(condition)) {
			subEnum = ElementEnum.ELEMENT_MAINT_MODE.getAllEnum();
		} else if (Constant.DIRECTION.equals(condition)) {
			subEnum = ElementEnum.DIRECTION.getAllEnum();
		} else if (Constant.FTTX.equals(condition)) {
			//subEnum = ElementEnum.FTTX_TYPE.getAllEnum();
			subEnum.put(1L, "FTTB");
			subEnum.put(2L, "FTTH");
			subEnum.put(3L, "LAN");
			subEnum.put(4L, "无线");
			
		} else if (Constant.ONUTYPE.equals(condition)) {
			subEnum = ElementEnum.ONU_TYPE.getAllEnum();
			//onu类型根据现场临时添加
			subEnum.put(3L, "HGU");
			subEnum.put(4L, "SFU");
		} else if (Constant.PORTRATE.equals(condition)) {
			subEnum = RackEnum.RATR_TYPE.getAllEnum();
			subEnum.put(48L, "20M");
		} else if (Constant.PORTSUBTYPE.equals(condition)) {
			subEnum = RackEnum.PORT_SUB_TYPE.getAllEnum();
		} else if (Constant.AUTHTYPE.equals(condition)) {
			// subEnum = AnmsConst.AUTH_TYPE.getAllEnum();
			subEnum.put("1", "SN");
			subEnum.put("2", "MAC");
			subEnum.put("3", "PASSWORD");
			subEnum.put("4", "LOGICID");
		} else if (Constant.PORTTYPE.equals(condition)) {
			subEnum = RackEnum.PORT_TYPE.getAllEnum();
		} else if (Constant.PORTSTATE.equals(condition)) {
			subEnum = RackEnum.PORT_STATE.getAllEnum();
		}else if(Constant.ACCESSTYPE.equals(condition)){
			subEnum = AnmsConst.ACCESS_TYPE.getAllEnum();
		}
		if (subEnum.containsValue(name)) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 根据综资要求校验设备安装地址 地址至少5级，最多9级，地址中不能有空格，地址间以"|"分隔 前三级地址数据库中必须存在
	 * 格式：省份|地市|区县|街道乡镇/路巷弄行政村/门牌/村组|小区/单位/自然村/组|楼号办公楼房|单元号|楼层|房号
	 * 
	 * */
	private static String checkAddress(String addressStr) {
		String errorMessage = "";
		try {
			if (!StringUtils.isEmpty(addressStr)) {
				String regex = "^(^([^\\|\\s]+)\\|([^\\|\\s]+)\\|([^\\|\\s]+)\\|"
						+ "([^\\|\\s]+)\\|([^\\|\\s]+)\\|([^\\|\\s]+)\\|([^\\|\\s]+)\\|"
						+ "([^\\|\\s]+)\\|([^\\|\\s]+)$)|(^([^\\|\\s]+)\\|([^\\|\\s]+)\\|"
						+ "([^\\|\\s]+)\\|([^\\|\\s]+)\\|([^\\|\\s]+)\\|([^\\|\\s]+)\\|"
						+ "([^\\|\\s]+)\\|([^\\|\\s]+)$)|(^([^\\|\\s]+)\\|([^\\|\\s]+)\\|"
						+ "([^\\|\\s]+)\\|([^\\|\\s]+)\\|([^\\|\\s]+)\\|([^\\|\\s]+)\\|([^\\|\\s]+)$)|"
						+ "(^([^\\|\\s]+)\\|([^\\|\\s]+)\\|([^\\|\\s]+)\\|([^\\|\\s]+)\\|([^\\|\\s]+)\\|"
						+ "([^\\|\\s]+)$)|(^([^\\|\\s]+)\\|([^\\|\\s]+)\\|([^\\|\\s]+)\\|([^\\|\\s]+)\\|([^\\|\\s]+)$)$";
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(addressStr);
				if (matcher.find()) {
					// 如果可以匹配，则地址中至少为5级，判断前3级是否在数据库中存在
					String[] addressArray = addressStr.split("\\|");
					if (addressArray != null && addressArray.length >= 3) {
						List<String> districtList = new ArrayList<String>();
						districtList.add(addressArray[0]);
						districtList.add(addressArray[1]);
						districtList.add(addressArray[2]);
						List<String> notExistDistrictList = oltManageBO
								.checkDevLocation(districtList);
						if (notExistDistrictList != null
								&& !notExistDistrictList.isEmpty()) {
							// 地址中的区域信息由在数据中不存在的
							for (String districtName : notExistDistrictList) {
								errorMessage = errorMessage + ","
										+ districtName;
							}
							if (!StringUtils.isEmpty(errorMessage)) {
								errorMessage = errorMessage + ",在数据库中不存在\r\n";
							}
						} else {
							// 地址格式无误，且前三级地址均在数据库中存在
						}
					}
				} else {
					// 地址非正确格式
					errorMessage = "地址的录入格式不正确\r\n"
							+ "地址至少5级，最多9级，地址中不能有空格，地址间以|分隔\r\n"
							+ "正确格式：省份|地市|区县|街道乡镇/路巷弄行政村/门牌/村组|小区/单位/自然村/组|楼号办公楼房|单元号|楼层|房号\r\n";
				}
			} else {
				// 地址为空
				errorMessage = "地址为空\r\n";
			}
		} catch (Exception ex) {
			LogHome.getLog().error("", ex);
		}
		return errorMessage;
	}

	protected static void verificationAddressNew(Workbook writeWorkBook,
			Sheet writeSheet, int j, int i, int lastColumns,
			String constantValue) throws Exception {
		try {
			Row xRow = writeSheet.getRow(i);
			Object cellContant = xRow.getCell(j);
			if (cellContant != null
					&& !StringUtils.isEmpty(cellContant.toString())) {
				// 检查地址是否在数据库中存在
				List<Map> addressList = oltManageBO
						.selectFullAddressByLabelCn(cellContant.toString());
				if (addressList == null || addressList.size() == 0) {
					updateExcelFormat(writeWorkBook, writeSheet, i, j);
					Cell xCell = xRow.getCell(lastColumns);
					if (xCell == null) {
						xCell = xRow.createCell(lastColumns);
						xCell.setCellValue(cellContant.toString()
								+ ",在数据库中不存在\r\n");
					} else {
						String strValue = xCell.getStringCellValue();
						strValue = strValue + cellContant.toString()
								+ ",在数据库中不存在\r\n";
						xCell.setCellValue(strValue);
					}
				}
			}
		} catch (Exception e) {
			LogHome.getLog().error("", e);
		}
	}

	protected static void verificationAddress(Workbook writeWorkBook,
			Sheet writeSheet, int j, int i, int lastColumns,
			String constantValue) throws Exception {
		try {
			Row xRow = writeSheet.getRow(i);
			Object cellContant = xRow.getCell(j);
			if (cellContant != null
					&& !StringUtils.isEmpty(cellContant.toString())) {
				// 检查地址格式以及前三级地址是否在数据库中存在
				String checkAddressMsg = checkAddress(cellContant.toString());
				if (!StringUtils.isEmpty(checkAddressMsg)) {
					updateExcelFormat(writeWorkBook, writeSheet, i, j);
					Cell xCell = xRow.getCell(lastColumns);
					if (xCell == null) {
						xCell = xRow.createCell(lastColumns);
						xCell.setCellValue(cellContant.toString()
								+ checkAddressMsg);
					} else {
						String strValue = xCell.getStringCellValue();
						strValue = strValue + cellContant.toString()
								+ checkAddressMsg;
						xCell.setCellValue(strValue);
					}
				}
			}
		} catch (Exception e) {
			LogHome.getLog().error("", e);
		}
	}

	/**
	 * 判断名称是否连续出现
	 * 
	 * @param ws
	 *            打开的的副本ws环境可以对单元格进行操作的
	 * @param sheet
	 *            第一个sheet表
	 * @param i当前第几行
	 * @param j当前第几列
	 * @param lastColumns总列数
	 * @throws Exception
	 * @param lastRows
	 * @param lastColumns
	 * @param constantValue
	 * @param nameList
	 * @return
	 * @return
	 */
	protected static void VerificationNameRepeat(Workbook writeWorkBook,
			Sheet writeSheet, int j, int i, int lastColumns, Map deviceNameMap)
			throws Exception {
		Row xRow = writeSheet.getRow(i);
		Object cellContant = xRow.getCell(j);
		Row xTitRow = writeSheet.getRow(0);
		Object titleName = xTitRow.getCell(j);
		String deviceName = cellContant.toString();
		if (deviceNameMap.containsKey(deviceName)) {
			updateExcelFormat(writeWorkBook, writeSheet, i, j);
			// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
			Cell xCell = xRow.getCell(lastColumns);
			if (xCell == null) {
				xCell = xRow.createCell(lastColumns);
				xCell.setCellValue("[" + titleName.toString() + "]出现重复值!\r\n");
			} else {
				String strValue = xCell.getStringCellValue();
				strValue = strValue + "[" + titleName.toString()
						+ "]出现重复值! \r\n";
				xCell.setCellValue(strValue);
			}
		} else {
			deviceNameMap.put(deviceName, i);
		}
	}
	/**
	 * 判断OLT关联端口端口名称是否重复出现
	 * 
	 * @param ws
	 *            打开的的副本ws环境可以对单元格进行操作的
	 * @param sheet
	 *            第一个sheet表
	 * @param i当前第几行
	 * @param j当前第几列
	 * @param k 所属设备列
	 * @param lastColumns总列数
	 * @throws Exception
	 * @param lastRows
	 * @param lastColumns
	 * @param constantValue
	 * @param nameList
	 * @return
	 * @return
	 */
	protected static void VerificationPorNametRepeat(Workbook writeWorkBook,
			Sheet writeSheet, int j, int i,int k,int m,int lastColumns, Map deviceNameMap)
			throws Exception {
		Row xRow = writeSheet.getRow(i);
		Object cellContant = xRow.getCell(j);
		Object xrelatedNeCuid =xRow.getCell(k);
		Object xcardName =xRow.getCell(m);
		Row xTitRow = writeSheet.getRow(0);
		Object titleName = xTitRow.getCell(j);
		String relatedNeCuid = xrelatedNeCuid.toString();
		String cardName = xcardName.toString();
		String deviceName = cellContant.toString()+"-"+relatedNeCuid+"-"+cardName;
		if (deviceNameMap.containsKey(deviceName)) {
			updateExcelFormat(writeWorkBook, writeSheet, i, j);
			// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
			Cell xCell = xRow.getCell(lastColumns);
			if (xCell == null) {
				xCell = xRow.createCell(lastColumns);
				xCell.setCellValue("[" + titleName.toString() + "]出现重复值!\r\n");
			} else {
				String strValue = xCell.getStringCellValue();
				strValue = strValue + "[" + titleName.toString()
						+ "]出现重复值! \r\n";
				xCell.setCellValue(strValue);
			}
		} else {
			deviceNameMap.put(deviceName.toString(), i);
		}
	}

	/**
	 * 判断板卡端口导入时板卡名称是否连续出现
	 * 
	 * @param ws
	 *            打开的的副本ws环境可以对单元格进行操作的
	 * @param sheet
	 *            第一个sheet表
	 * @param i当前第几行
	 * @param j当前第几列
	 * @param lastColumns总列数
	 * @throws Exception
	 * @param lastRows
	 * @param lastColumns
	 * @param constantValue
	 * @param nameList
	 * @return
	 * @return
	 */
	protected static void VerificationCardNameRepeat(Workbook writeWorkBook,
			Sheet writeSheet, int j, int i, int lastColumns) throws Exception {
		Row xRow = writeSheet.getRow(i);
		Object cellContant = xRow.getCell(j);
		Row xTitRow = writeSheet.getRow(0);
		Object titleName = xTitRow.getCell(j);
		String deviceName = cellContant.toString();
		updateExcelFormat(writeWorkBook, writeSheet, i, j);
		// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
		Cell xCell = xRow.getCell(lastColumns);
		if (xCell == null) {
			xCell = xRow.createCell(lastColumns);
			xCell.setCellValue(titleName.toString() + "页面出现重复值!\r\n");
		} else {
			String strValue = xCell.getStringCellValue();
			strValue = strValue + titleName.toString() + "页面出现重复值! \r\n";
			xCell.setCellValue(strValue);
		}
	}

	/**
	 * 验证板卡是否是连续的，加入不是连续的二次重复出现为错误
	 * 
	 * @param ws
	 *            打开的的副本ws环境可以对单元格进行操作的
	 * @param sheet
	 *            第一个sheet表
	 * @param i
	 *            当前第几行
	 * @param j
	 *            当前第几列
	 * @param lastColumns
	 *            总列数
	 * @throws Exception
	 * 
	 */
	protected static void verificationCategoryFalse(Workbook writeWorkBook,
			Sheet writeSheet, int j, int i, int lastColumns,
			String constantValue) throws Exception {

		try {
			updateExcelFormat(writeWorkBook, writeSheet, i, j);
			// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
			Row xRow = writeSheet.getRow(i);
			Cell xCell = xRow.getCell(lastColumns);
			Cell jCell = xRow.getCell(j);
			if (xCell == null) {
				xCell = xRow.createCell(lastColumns);
				// 设置最后一列记录异常信息单元格的列宽度
				// writeSheet.setColumnWidth(lastColumns,
				// writeSheet.getColumnWidth(lastColumns)*3);
				xCell.setCellValue(jCell.toString() + "属性已经存在，请把这些出现的写成连续的\r\n");
			} else {
				String strValue = xCell.getStringCellValue();
				strValue = strValue + jCell.toString()
						+ "属性已经存在，请把这些出现的写成连续的\r\n";
				xCell.setCellValue(strValue);
			}

		} catch (Exception e) {
			LogHome.getLog().error("属性已经存在，请把这些出现的写成连续的出现错误，请您将您的数据仔细规划一下！", e);
			throw new Exception("属性已经存在，请把这些出现的写成连续的出现错误，请您将您的数据仔细规划一下！");
		}

	}

	/**
	 * 验证所属网元是否存在
	 * 
	 * @param ws
	 *            打开的的副本ws环境可以对单元格进行操作的
	 * @param sheet
	 *            第一个sheet表
	 * @param i
	 *            当前第几行
	 * @param j
	 *            当前第几列
	 * @param lastColumns
	 *            总列数
	 * @throws Exception
	 * 
	 */
	protected static void verificationNeName(Workbook writeWorkBook,
			Sheet writeSheet, int j, int i, int lastColumns,
			String constantValue) throws Exception {
		try {
			String showmessage = null;
			if (constantValue != null && constantValue.equals("coverage")) {
				showmessage = "网元名称在该网元类型的数据库中不存在\r\n";
			} else {
				showmessage = "网元名称在数据库中不存在\r\n";
			}

			updateExcelFormat(writeWorkBook, writeSheet, i, j);
			// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
			Row xRow = writeSheet.getRow(i);
			Cell xCell = xRow.getCell(lastColumns);
			// Cell jCell=xRow.getCell(j);
			if (xCell == null) {
				xCell = xRow.createCell(lastColumns);
				// 设置最后一列记录异常信息单元格的列宽度
				// writeSheet.setColumnWidth(lastColumns,
				// writeSheet.getColumnWidth(lastColumns)*3);
				xCell.setCellValue(showmessage);
			} else {
				String strValue = xCell.getStringCellValue();
				strValue = strValue + showmessage;
				xCell.setCellValue(strValue);
			}

		} catch (Exception e) {
			LogHome.getLog().error("验证关联字段是否存在，请您仔细核实一下您的外键是否存在！", e);
			throw new Exception("验证关联字段是否存在，请您仔细核实一下您的外键是否存在！");
		}

	}

	/**
	 * 判断名称是否连续出现
	 * 
	 * @param ws
	 *            打开的的副本ws环境可以对单元格进行操作的
	 * @param sheet
	 *            第一个sheet表
	 * @param i当前第几行
	 * @param j当前第几列
	 * @param lastColumns总列数
	 * @throws Exception
	 * @param lastRows
	 * @param lastColumns
	 * @param constantValue
	 * @param nameList
	 * @return
	 * @return
	 */
	public static void VerificationDeviceNameRepeat(Workbook writeWorkBook,
			Sheet writeSheet, Row xRow, int j, int i, int lastColumns,
			Map nameMap) throws Exception {
		String name = xRow.getCell(j).toString();
		// int lastcol=writeSheet.getRow(i).getLastCellNum();
		if (nameMap.containsKey(name)) {
			int rowNum = (Integer) nameMap.get(name);
			if (rowNum != i) {
				Row headRow = writeSheet.getRow(0);
				updateExcelFormat(writeWorkBook, writeSheet, i, j);
				Cell xCell = xRow.getCell(lastColumns);
				if (xCell == null) {
					xCell = xRow.createCell(lastColumns);
					xCell.setCellValue("模板中第" + (i + 1) + "行与第" + (rowNum + 1)
							+ "行" + headRow.getCell(j) + "重复！\r\n");
				} else {
					String strValue = xCell.getStringCellValue();
					strValue = strValue + "模板中第" + (i + 1) + "行与第"
							+ (rowNum + 1) + "行" + headRow.getCell(j)
							+ "重复！\r\n";
					xCell.setCellValue(strValue);
				}
			}
		} else {
			nameMap.put(name, i);
		}
	}

	/**
	 * 判断导入模板字段是否必填
	 * 
	 * @throws Exception
	 */
	public static void decideFields(Workbook writeWorkBook, Sheet writeSheet,
			int i, int lastColumns) throws Exception {
		Row proRow = writeSheet.getRow(2);
		Row cellRow = writeSheet.getRow(i);
		for (int m = 1; m < lastColumns; m++) {
			if (proRow.getCell(m).toString().indexOf("非空") != -1
					|| proRow.getCell(m).toString().indexOf("必填") != -1) {
				Object obj = cellRow.getCell(m);
				if (obj == null || "".equals(obj.toString())
						|| "null".equalsIgnoreCase(obj.toString())) {
					updateExcelFormat(writeWorkBook, writeSheet, i, m);
					Cell xCell = cellRow.getCell(lastColumns);
					if (xCell == null) {
						xCell = cellRow.createCell(lastColumns);
						xCell.setCellValue("【"
								+ writeSheet.getRow(0).getCell(m) + "】"
								+ "列不能为空\r\n");
					} else {
						String strValue = xCell.getStringCellValue();
						strValue = strValue + "【"
								+ writeSheet.getRow(0).getCell(m) + "】"
								+ "列不能为空\r\n";
						xCell.setCellValue(strValue);
					}
				}
			}
		}
	}

	/**
	 * 验证所属网元是否存在
	 * 
	 * @param ws
	 *            打开的的副本ws环境可以对单元格进行操作的
	 * @param sheet
	 *            第一个sheet表
	 * @param i
	 *            当前第几行
	 * @param j
	 *            当前第几列
	 * @param lastColumns
	 *            总列数
	 * @throws Exception
	 * 
	 */
	protected static Map verificationAllNeName(Workbook writeWorkBook,
			Sheet writeSheet, int relatedNeCuidInt, int i, int lastColumns,
			String constantValue) throws Exception {
		Map res = null;
		try {
			String sheetName = writeSheet.getSheetName();
			Row xRow = writeSheet.getRow(i);
			Cell xCell = xRow.getCell(relatedNeCuidInt);
			if (xCell != null && !StringUtils.isEmpty(xCell.toString())) {
				List<Map> list = new ArrayList<Map>();
				if(sheetName.indexOf("OLT端口")>=0){
					list = oltManageBO.getOLTCuidByLabelCn(xCell.toString());
				}else if(sheetName.indexOf("ONU端口")>=0){
					list = oltManageBO.getONUCuidByLabelCn(xCell.toString());
				}else if(sheetName.indexOf("POS端口")>=0){
					list = oltManageBO.getPOSCuidByLabelCn(xCell.toString());
				}else{
					list = oltManageBO.getAllNeCuidByLabelCn(xCell.toString());
				}
				if (list.isEmpty()) {
					String showmessage = "网元名称在数据库中不存在\r\n";
					updateExcelFormat(writeWorkBook, writeSheet, i,
							relatedNeCuidInt);
					// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
					Cell lastXCell = xRow.getCell(lastColumns);
					// Cell jCell=xRow.getCell(j);
					if (lastXCell == null) {
						lastXCell = xRow.createCell(lastColumns);
						// 设置最后一列记录异常信息单元格的列宽度
						// writeSheet.setColumnWidth(lastColumns,
						// writeSheet.getColumnWidth(lastColumns)*3);
						lastXCell.setCellValue(showmessage);
					} else {
						String strValue = lastXCell.getStringCellValue();
						strValue = strValue + showmessage;
						lastXCell.setCellValue(strValue);
					}
				} else {
					res = list.get(0);
				}
			}
		} catch (Exception e) {
			LogHome.getLog().error("验证关联字段是否存在，请您仔细核实一下您的外键是否存在！", e);
			throw new Exception("验证关联字段是否存在，请您仔细核实一下您的外键是否存在！");
		}
		return res;
	}

	/**
	 * 验证所属网元是否存在
	 * 
	 * @param ws
	 *            打开的的副本ws环境可以对单元格进行操作的
	 * @param sheet
	 *            第一个sheet表
	 * @param i
	 *            当前第几行
	 * @param j
	 *            当前第几列
	 * @param lastColumns
	 *            总列数
	 * @throws Exception
	 * 
	 */
	protected static Map verificationAllNeCardName(Workbook writeWorkBook,
			Sheet writeSheet, int relatedNeCuidInt, int i, int lastColumns,
			String constantValue) throws Exception {
		Map res = null;
		try {
			Row xRow = writeSheet.getRow(i);
			Cell xCell = xRow.getCell(relatedNeCuidInt);
			if (xCell != null && !StringUtils.isEmpty(xCell.toString())) {
				List<Map> list = oltManageBO.getAllNeCuidByLabelCn(xCell
						.toString());
				if (list.isEmpty()) {
					String showmessage = "网元名称在数据库中不存在\r\n";
					updateExcelFormat(writeWorkBook, writeSheet, i,
							relatedNeCuidInt);
					// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
					Cell lastXCell = xRow.getCell(lastColumns);
					// Cell jCell=xRow.getCell(j);
					if (lastXCell == null) {
						lastXCell = xRow.createCell(lastColumns);
						// 设置最后一列记录异常信息单元格的列宽度
						// writeSheet.setColumnWidth(lastColumns,
						// writeSheet.getColumnWidth(lastColumns)*3);
						lastXCell.setCellValue(showmessage);
					} else {
						String strValue = lastXCell.getStringCellValue();
						strValue = strValue + showmessage;
						lastXCell.setCellValue(strValue);
					}
				} else {
					res = list.get(0);
				}
			}
		} catch (Exception e) {
			LogHome.getLog().error("验证关联字段是否存在，请您仔细核实一下您的外键是否存在！", e);
			throw new Exception("验证关联字段是否存在，请您仔细核实一下您的外键是否存在！");
		}
		return res;
	}

	/**
	 * 提示板卡的FDN数据里里已经存在
	 * 
	 * @param sheet
	 *            第一个sheet表
	 * @param i
	 *            当前第几行
	 * @param j
	 *            当前第几列
	 * @param lastColumns
	 *            总列数
	 * @throws Exception
	 * 
	 */
	protected static void promptCardFdn(Workbook writeWorkBook,
			Sheet writeSheet, int neName, int cardFdn, int i, int lastColumns,
			String constantValue) throws Exception {
		try {
			updateExcelFormat(writeWorkBook, writeSheet, i, cardFdn);
			// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
			Row xRow = writeSheet.getRow(i);
			Cell xCell = xRow.getCell(lastColumns);
			if (xCell == null) {
				xCell = xRow.createCell(lastColumns);
				// 设置最后一列记录异常信息单元格的列宽度
				// writeSheet.setColumnWidth(lastColumns,
				// writeSheet.getColumnWidth(lastColumns)*3);
				xCell.setCellValue("该网元下的机槽编号已被占用,请重新填写!\r\n");
			} else {
				String strValue = xCell.getStringCellValue();
				strValue = strValue + "该网元下的机槽编号已被占用,请重新填写!\r\n";
				xCell.setCellValue(strValue);
			}

		} catch (Exception e) {
			LogHome.getLog().error("提示板卡的FDN数据里里已经存在,方法出现异常!", e);
		}

	}

	/**
	 * 提示端口的FDN数据里里已经存在
	 * 
	 * @param sheet
	 *            第一个sheet表
	 * @param i
	 *            当前第几行 * @param j 当前第几列
	 * @param lastColumns
	 *            总列数
	 * @throws Exception
	 */
	protected static void promptPtpFdn(Workbook writeWorkBook,
			Sheet writeSheet, int cardFdn, int i, int lastColumns,
			String constantValue) throws Exception {
		try {
			updateExcelFormat(writeWorkBook, writeSheet, i, cardFdn); // 操作最后一行单元格：通用的获取cell值的方式,返回字符串
			Row xRow = writeSheet.getRow(i);
			Cell xCell = xRow.getCell(lastColumns);
			Cell jCell = xRow.getCell(cardFdn);
			if (xCell == null) {
				xCell = xRow.createCell(lastColumns);
				xCell.setCellValue("该板卡下的端口编号已经存在,请重新填写!\r\n");
			} else {
				String strValue = xCell.getStringCellValue();
				strValue = strValue + "该板卡下的端口编号已经存在,请重新填写!\r\n";
				xCell.setCellValue(strValue);
			}
		} catch (Exception e) {
			LogHome.getLog().error("提示板卡的FDN数据里里已经存在,方法出现异常!", e);
		}
	}

	/**
	 * 验证关联字段是否存在，与上边的是不一样的，这个是和数据库校验的结果，哪一个是map中的结果
	 * 
	 * @param ws
	 *            打开的的副本ws环境可以对单元格进行操作的
	 * @param sheet
	 *            第一个sheet表
	 * @param i
	 *            当前第几行
	 * @param j
	 *            当前第几列
	 * @param lastColumns
	 *            总列数
	 * @throws Exception
	 * 
	 */
	protected static void verificationForeign(Workbook writeWorkBook,
			Sheet writeSheet, int j, int i, int lastColumns,
			String constantValue) throws Exception {
		try {
			updateExcelFormat(writeWorkBook, writeSheet, i, j);
			// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
			Row xRow = writeSheet.getRow(i);
			Cell xCell = xRow.getCell(lastColumns);
			Cell jCell = xRow.getCell(j);
			if (xCell == null) {
				xCell = xRow.createCell(lastColumns);
				// 设置最后一列记录异常信息单元格的列宽度
				// writeSheet.setColumnWidth(lastColumns,
				// writeSheet.getColumnWidth(lastColumns)*3);
				xCell.setCellValue(jCell.toString() + " 数据库中不存在\r\n");
			} else {
				String strValue = xCell.getStringCellValue();
				strValue = strValue + jCell.toString() + " 数据库中不存在\r\n";
				xCell.setCellValue(strValue);
			}

		} catch (Exception e) {
			LogHome.getLog().error("", e);
			throw new Exception(e);
		}

	}

	/**
	 * 验证两个关联字段，关联的关系是否存在
	 * 
	 * @param ws
	 *            打开的的副本ws环境可以对单元格进行操作的
	 * @param sheet
	 *            第一个sheet表
	 * @param i
	 *            当前第几行
	 * @param j
	 *            当前第几列
	 * @param lastColumns
	 *            总列数
	 * @throws Exception
	 * 
	 */
	protected static void verificationForeignRelation(Workbook writeWorkBook,
			Sheet writeSheet, int j, int jj, int i, int lastColumns,
			String constantValue, String constantValue2) throws Exception {
		try {
			updateExcelFormat(writeWorkBook, writeSheet, i, j);
			updateExcelFormat(writeWorkBook, writeSheet, i, jj);
			// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
			Row xRow = writeSheet.getRow(i);
			Cell xCell = xRow.getCell(lastColumns);
			Cell jCell = xRow.getCell(j);
			Cell jjCell = xRow.getCell(jj);

			if (xCell == null) {
				xCell = xRow.createCell(lastColumns);
				// 设置最后一列记录异常信息单元格的列宽度
				// writeSheet.setColumnWidth(lastColumns,
				// writeSheet.getColumnWidth(lastColumns)*3);
				xCell.setCellValue(jCell.toString() + " 与 " + jjCell.toString()
						+ "关联关系不存在\r\n");
			} else {
				String strValue = xCell.getStringCellValue();
				strValue = strValue + jCell.toString() + " 与 "
						+ jjCell.toString() + "关联关系不存在\r\n";
				xCell.setCellValue(strValue);
			}

		} catch (Exception e) {
			LogHome.getLog().error("验证关联关系失败，请您确认您的关联关系正确！", e);
			throw new Exception("验证关联关系失败，请您确认您的关联关系正确！");
		}

	}

	/**
	 * 验证区域存在的时候，站点必须属于区域。
	 * 
	 * @param ws
	 *            打开的的副本ws环境可以对单元格进行操作的
	 * @param sheet
	 *            第一个sheet表
	 * @param i
	 *            当前第几行
	 * @param j
	 *            当前第几列
	 * @param lastColumns
	 *            总列数
	 * @throws Exception
	 *             验证地址符合*|*|*，|与|之间必须有值的存在否则就报错，不正确将错误打印到最后一列中去
	 */
	protected static void verificationSite(Workbook writeWorkBook,
			Sheet writeSheet, int j, int i, int lastColumns,
			String constantValue) throws Exception {

		try {
			updateExcelFormat(writeWorkBook, writeSheet, i, j);
			// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
			Row xRow = writeSheet.getRow(i);
			Cell xCell = xRow.getCell(lastColumns);
			Cell jCell = xRow.getCell(j);
			if (xCell == null) {
				xCell = xRow.createCell(lastColumns);
				// 设置最后一列记录异常信息单元格的列宽度
				// writeSheet.setColumnWidth(lastColumns,
				// writeSheet.getColumnWidth(lastColumns)*3);
				xCell.setCellValue(jCell.toString() + "不在这个区域\r\n");
			} else {
				String strValue = xCell.getStringCellValue();
				strValue = strValue + jCell.toString() + "不在这个区域 \r\n";
				xCell.setCellValue(strValue);
			}

		} catch (Exception e) {
			LogHome.getLog().error("验证枚举值是否存在失败，请您仔细核实一下枚举值的类型！", e);
			throw new Exception("验证枚举值是否存在失败，请您仔细核实一下枚举值的类型！");
		}

	}

	/**
	 * 验证card的fdn 架—框-槽格式是否符合要求
	 * 
	 * @param writeWorkBook
	 * @param writeSheet
	 * @param j
	 * @param i
	 * @param lastColumns
	 * @param constantValue
	 * @throws Exception
	 */
	protected static boolean verificationCardFdn(Workbook writeWorkBook,
			Sheet writeSheet, int j, int i, int lastColumns,
			String constantValue) throws Exception {
		boolean res = true;
		try {
			Row xRow = writeSheet.getRow(i);
			Object cellContant = xRow.getCell(j);
			if (cellContant != null
					&& !StringUtils.isEmpty(cellContant.toString())) {
				// 假如判断返回false说明该字符串中正确，假如是true说明该字符串中存在上述问题
				if (!isBaseFdn(cellContant.toString())) {
					res = false;
					updateExcelFormat(writeWorkBook, writeSheet, i, j);
					// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
					Cell xCell = xRow.getCell(lastColumns);
					if (xCell == null) {
						xCell = xRow.createCell(lastColumns);
						// 设置最后一列记录异常信息单元格的列宽度
						// writeSheet.setColumnWidth(lastColumns,
						// writeSheet.getColumnWidth(lastColumns)*3);
						xCell.setCellValue("[" + constantValue
								+ "]架—框-槽不符合要求\r\n");
					} else {
						String strValue = xCell.getStringCellValue();
						strValue = strValue + "[" + constantValue
								+ "]架—框-槽不符合要求 \r\n";
						xCell.setCellValue(strValue);
					}
				}
			}
		} catch (Exception e) {
			LogHome.getLog().error(
					"验证架—框-槽符合*-*-*要求，请您仔细核实一下您的架—框-槽格式是否符合条件格式：*—*-*", e);
			throw new Exception(
					"验证架—框-槽符合*-*-*要求，请您仔细核实一下您的架—框-槽格式是否符合条件格式：*—*-*");
		}
		return res;
	}

	protected static boolean isBaseFdn(String name) {
		// 1-1-13,NA-2-1,1-1-NTA
		String[] subAddress = name.split("\\-");
		String reg1 = "^([0-9]+|NA)$";
		String reg2 = "^([0-9]+)$";
		String reg3 = "^([0-9]+|[A-Za-z]+)$";
		Pattern pattern1 = Pattern.compile(reg1);
		Pattern pattern2 = Pattern.compile(reg2);
		Pattern pattern3 = Pattern.compile(reg3);
		boolean flag = false;
		if (subAddress.length == 3) {
			boolean bln0 = pattern1.matcher(subAddress[0]).find();
			boolean bln1 = pattern2.matcher(subAddress[1]).find();
			boolean bln2 = pattern3.matcher(subAddress[2]).find();
			if (bln0 && bln1 && bln2) {
				flag = true;
			} else {
				flag = false;
			}
		} else {
			flag = false;
		}
		return flag;
	}

	/**
	 * 判断地址是否是|与|间隔之间是否存在之，是的话返回false，假如不存在值就返回true
	 * 
	 * @param ws
	 *            打开的的副本ws环境可以对单元格进行操作的
	 * @param sheet
	 *            第一个sheet表
	 * @param i
	 *            当前第几行
	 * @param j
	 *            当前第几列
	 * @param lastColumns
	 *            总列数
	 * @throws Exception
	 *             判断地址是否是|与|间隔之间是否存在之，是的话返回false，假如不存在值就返回true
	 */
	protected static boolean isAddress(String name) {
		String[] subAddress = name.split("\\|");
		for (int i = 0; i < subAddress.length; i++) {
			if (subAddress[i] != null && !StringUtils.isEmpty(subAddress[i])) {
				return true;
			}// 无值返回false
		}
		return false;
	}

	// 截取字符串，取得返回的路径和文件
	protected String loadPath(String pathname) {
		int length = pathname.length();
		int startLength = pathname.indexOf("webapps\\");
		String loadPath = pathname.substring(startLength + 7, length);
		return loadPath;
	}

	/**
	 * 将出错的单元格，更新成红色背景色并且在最后一列的单元格上打印错误信息
	 * 
	 * @param 打开的sheet进行修改和修饰
	 * @param 当前第几行
	 * @param 当前第几列
	 * @param 最后一列
	 * @throws Exception
	 */
	protected static void updateExcelFormat(Workbook writeWorkBook,
			Sheet writeSheet, int i, int j) throws Exception {
		try {
			CellStyle style = writeWorkBook.createCellStyle();
			style.setFillForegroundColor(IndexedColors.RED.getIndex());
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			Row xRow = writeSheet.getRow(i);
			Cell xCell = xRow.getCell(j);
			if (xCell == null) {
				xCell = xRow.createCell(j);
				xCell.setCellStyle(style);

			} else {
				xCell.setCellStyle(style);
			}

		} catch (Exception e) {
			LogHome.getLog().error("", e);
		}

	}

	/**
	 * 将出错的单元格，更新成红色背景色并且在最后一列的单元格上打印错误信息
	 */
	protected static void updateExcelFormat(Workbook writeWorkBook, Row xRow,
			int j) throws Exception {
		try {
			CellStyle style = writeWorkBook.createCellStyle();
			style.setFillForegroundColor(IndexedColors.RED.getIndex());
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			Cell xCell = xRow.getCell(j);
			if (xCell == null) {
				xCell = xRow.createCell(j);
				xCell.setCellStyle(style);

			} else {
				xCell.setCellStyle(style);
			}

		} catch (Exception e) {
			LogHome.getLog().error("", e);
		}

	}

	public static void verificationRelated(Workbook writeWorkBook, Row xRow,
			int j, int lastColumns, String constantValue, Map cardKindMap)
			throws Exception {
		try {
			Object cellContant = xRow.getCell(j);
			if (cellContant != null
					&& !StringUtils.isEmpty(cellContant.toString())) {
				if (constantValue.equals(Constant.SINGLEMODEL)) {
					if (!cardKindMap.containsKey(cellContant.toString())) {
						ImportCommonMethod.updateExcelFormat(writeWorkBook,
								xRow, j);
						// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
						Cell xCell = xRow.getCell(lastColumns);
						if (xCell == null) {
							xCell = xRow.createCell(lastColumns);
							// writeSheet.setColumnWidth(lastColumns,
							// writeSheet.getColumnWidth(lastColumns)*3);
							xCell.setCellValue("[" + Constant.SINGLEMODEL
									+ "]数据库中不存在!\r\n");
						} else {
							String strValue = xCell.getStringCellValue();
							strValue = strValue + "[" + Constant.SINGLEMODEL
									+ "]数据库中不存在! \r\n";
							xCell.setCellValue(strValue);
						}
					}
				}
			}
		} catch (Exception e) {
			LogHome.getLog().error("验证关联关系是否存在时出错，请您核实一下您的关联关系！", e);
			throw new Exception("验证关联关系是否存在时出错，请您核实一下您的关联关系！");
		}

	}

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0 || "".equals(str)
				|| "null".equalsIgnoreCase(str)
				|| "undefined".equalsIgnoreCase(str);
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 * 判断Excel中是否存在相同的关键字
	 * 
	 * @param ws
	 *            打开的的副本ws环境可以对单元格进行操作的
	 * @param sheet
	 *            第一个sheet表
	 * @param i
	 *            当前第几行
	 * @param j
	 *            当前第几列
	 * @param lastColumns
	 *            总列数
	 * @throws Exception
	 *             判断Excel中是否存在于此单元格是否相同的关键字
	 */
	protected static void isExcelExist(Workbook writeWorkBook,
			Sheet writeSheet, int j, int i, int lastColumns, int lastRows,
			Map nameMap) throws Exception {
		boolean flag = false;
		String verificationrow = "";
		try {
			Row xRow = writeSheet.getRow(i);
			Object cellContant = xRow.getCell(j);
			if (cellContant != null
					&& !StringUtils.isEmpty(cellContant.toString())) {
				if (nameMap.containsKey(cellContant.toString())) {
					flag = true;
					int k = Integer.parseInt(nameMap
							.get(cellContant.toString()).toString());
					verificationrow = verificationrow + "," + k;
				}// else说明值不存在
				if (!"".equals(verificationrow)) {
					verificationrow = verificationrow.substring(1);
				}// else说明值不存在
					// 假如判断返回false说明该字符串中正确，假如是true说明该字符串中存在上述问题
				if (flag) {
					updateExcelFormat(writeWorkBook, writeSheet, i, j);
					// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
					Cell xCell = xRow.getCell(lastColumns);
					if (xCell == null) {
						xCell = xRow.createCell(lastColumns);
						// 设置最后一列记录异常信息单元格的列宽度
						// writeSheet.setColumnWidth(lastColumns,
						// writeSheet.getColumnWidth(lastColumns)*3);
						xCell.setCellValue(verificationrow + "行 与 本行 ["
								+ cellContant.toString() + "]重复\r\n");
					} else {
						String strValue = xCell.getStringCellValue();
						strValue = strValue + verificationrow + "行 与 本行["
								+ cellContant.toString() + "]重复 \r\n";
						xCell.setCellValue(strValue);
					}
				}// else说明值不存在
			}// else直接推出校验

		} catch (Exception e) {
			LogHome.getLog().error("", e);
			throw new Exception(e);
		}
	}

	/**
	 * 判断是否存在最后一列是否存在错误，假如最有一列部位空说明里面存在之，就让其返回true。
	 * 
	 * @param ws
	 *            打开的的副本ws环境可以对单元格进行操作的
	 * @param sheet
	 *            第一个sheet表
	 * @throws Exception
	 *             判断Excel中是否存在于此单元格是否相同的关键字
	 */
	protected static int isExistFalse(Workbook writeWorkBook, Sheet writeSheet,
			int lastColumns, int lastRows) throws Exception {
		int flaseNumber = 0;
		int length = lastRows + 1;
		for (int k = 5; k < length; k++) {
			Row xRow = writeSheet.getRow(k);
			if (xRow != null) {
				if (xRow.getCell(lastColumns) != null
						&& !StringUtils.isEmpty(xRow.getCell(lastColumns).toString())) {
					flaseNumber++;
				}
			}
		}
		return flaseNumber;
	}

	/**
	 * 判断覆盖范围，用户等模板是否存有问题
	 * 
	 * @param ws
	 *            打开的的副本ws环境可以对单元格进行操作的
	 * @param sheet
	 *            第一个sheet表
	 * @throws Exception
	 *             判断Excel中是否存在于此单元格是否相同的关键字
	 */
	protected static int isNewExistFalse(Workbook writeWorkBook,
			Sheet writeSheet, int lastColumns, int lastRows) throws Exception {
		int flaseNumber = 0;
		int length = lastRows + 1;
		for (int k = 3; k < length; k++) {
			Row xRow = writeSheet.getRow(k);
			if (xRow.getCell(lastColumns) != null) {
				flaseNumber++;
			}
		}
		return flaseNumber;
	}

	/**
	 * //字符串强转Timestamp
	 */
	protected static Timestamp getTimestame(String time) {
		try {
			if (!StringUtils.isEmpty(time)) {
				Date date= TimeFormatHelper.checkIsDate(time);
				String timeFormat = "yyyy-MM-dd HH:mm:ss";
				
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat);
				String resStr = simpleDateFormat.format(date);
				return Timestamp.valueOf(resStr);
			}
			return null;
		}		
		catch (Exception ex) {
			LogHome.getLog().error("时间转换异常", ex);
		}
		return null;
	}

	/**
	 * 返回操作导入相应的BO
	 * 
	 * @return
	 */
	private static ImportBasicDataBO getImportBasicDataBO() {
		return (ImportBasicDataBO) SpringContextUtil
				.getBean(AnmsBoName.ImportBasicDataBO);
	}

	/**
	 * 验证网元名称是否存在数据库中(覆盖范围)
	 */
	protected static void checkNeNameIsExist(Workbook writeWorkBook,
			Sheet writeSheet, int j, int i, int lastColumns, String neNameStr,
			Map nameMap, String neTypeStr, Map neNameIsExistMap)
			throws Exception {
		if (neTypeStr != null && !StringUtils.isEmpty(neTypeStr)) {
			if (!nameMap.containsKey(neNameStr)) {
				Map neMap = (Map) getImportBasicDataBO().checkNeName(neNameStr,
						neTypeStr);
				if (neMap != null && neMap.get(GenericDO.AttrName.cuid) != null) {
					nameMap.put(neNameStr, neMap.get(GenericDO.AttrName.cuid));
					neNameIsExistMap.put(neNameStr, j);
				} else {
					if (neTypeStr != null && neTypeStr.equals("客户")) {
						nameMap.put(neNameStr, j);
						neNameIsExistMap.put(neNameStr, j);
					} else {
						verificationNeName(writeWorkBook, writeSheet, j, i,
								lastColumns, "coverage");
					}
				}
			}
		}
	}

	/**
	 * 验证区域是否存在数据库中(覆盖范围)
	 */
	protected static void checkDistrictIsExist(Workbook writeWorkBook,
			Sheet writeSheet, int j, int i, int lastColumns,
			String districtStr, Map districtMap) throws Exception {
		if (!districtMap.containsKey(districtStr)) {
			String districtCuid = getImportBasicDataBO()
					.queryDistrictByLabelcn(districtStr);
			if (districtCuid != null && !StringUtils.isEmpty(districtCuid)) {
				districtMap.put(districtStr, districtCuid);
			} else {
				verificationForeign(writeWorkBook, writeSheet, j, i,
						lastColumns, null);
			}
		}
	}

	/**
	 * 判断设备FDN信息格式是否正确
	 */
	public static boolean verificationNeFdnInfo(String neFdn, String devType) {
		if (!ImportCommonMethod.isEmpty(neFdn)) {
			if ("olt".equals(devType) && neFdn.indexOf("EMS=") != -1
					&& neFdn.indexOf(":ManagedElement=") != -1) {
				return true;
			} else if ("pos".equals(devType) && neFdn.indexOf("EMS=") != -1
					&& neFdn.indexOf(":ManagedElement=") != -1
					&& neFdn.indexOf(":POS=") != -1) {
				return true;
			} else if ("onu".equals(devType) && neFdn.indexOf("EMS=") != -1
					&& neFdn.indexOf(":ManagedElement=") != -1
					&& neFdn.indexOf(":ONU=") != -1) {
				return true;
			} else if ("card".equals(devType) && neFdn.indexOf("EMS=") != -1
					&& neFdn.indexOf(":ManagedElement=") != -1
					&& neFdn.indexOf(":EquipmentHolder=") != -1
					&& neFdn.indexOf(":Equipment=") != -1) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 校验最后一行是否有错误信息
	 * */
	public static boolean isRowExistError(Row row, int lastColumns)
			throws Exception {
		Cell cell = row.getCell(lastColumns);
		if (cell != null) {
			String cellVal = ImportCommonMethod.getCellValue(row
					.getCell(lastColumns));
			if (!ImportCommonMethod.isEmpty(cellVal)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 提示错误信息
	 */
	public static void printErrorInfo(Workbook writeWorkBook, Sheet writeSheet,
			int i, int j, int lastColumns, String msg) throws Exception {
		try {
			Row xRow = writeSheet.getRow(i);
			updateExcelFormat(writeWorkBook, writeSheet, i, j);
			// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
			Row initRow = writeSheet.getRow(0);
			Object initContant = initRow.getCell(j);
			Cell xCell = xRow.getCell(lastColumns);
			if (xCell == null) {
				xCell = xRow.createCell(lastColumns);
				xCell.setCellValue("[" + initContant.toString() + "]" + msg
						+ " \r\n");
			} else {
				String strValue = xCell.getStringCellValue();
				strValue = strValue + "[" + initContant.toString() + "]" + msg
						+ " \r\n";
				xCell.setCellValue(strValue);
			}
		} catch (Exception e) {
			LogHome.getLog().error("提示错误信息!" + msg, e);
			throw new Exception(e);
		}
	}

	/**
	 * 提示错误信息
	 */
	public static void printOnlyErrorInfo(Workbook writeWorkBook,
			Sheet writeSheet, int i, int j, int lastColumns, String msg)
			throws Exception {
		try {
			Row xRow = writeSheet.getRow(i);
			updateExcelFormat(writeWorkBook, writeSheet, i, j);
			// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
			Cell xCell = xRow.getCell(lastColumns);
			if (xCell == null) {
				xCell = xRow.createCell(lastColumns);
				xCell.setCellValue(msg + " \r\n");
			} else {
				String strValue = xCell.getStringCellValue();
				strValue = strValue + msg + " \r\n";
				xCell.setCellValue(strValue);
			}
		} catch (Exception e) {
			LogHome.getLog().error("提示错误信息!" + msg, e);
			throw new Exception(e);
		}
	}
	/**
	 * 批量添加和修改card与ptp, 更新板卡时是否动态更新端口名称control
	 */
	public static void importAddressToDB(List<Map<String, Object>> dataList,
			String excelName, ImportResultDO importResultDO) throws Exception {
		try {
			List<Map<String, Object>> insertDataList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> updateDataList = new ArrayList<Map<String, Object>>();
			List<Map<String, String>> cuidList = new ArrayList<Map<String, String>>();
			if (dataList != null && dataList.size() > 0) {
				for (Map<String, Object> m : dataList) {
					String pcType = m.get("TYPE") + "";
					if ("UPDATE".equals(pcType)) {
						updateDataList.add(m);
					}
					if ("INSERT".equals(pcType)) {
						insertDataList.add(m);
					}
				}
			}
			if (updateDataList != null && updateDataList.size() > 0) {
				getImportBasicDataBO().importAddressBatchUpdate(updateDataList,
						excelName);
				importResultDO.setInfo("更新标准地址数据:" + updateDataList.size()
						+ "条");
			}
			if (insertDataList != null && insertDataList.size() > 0) {
				getImportBasicDataBO().importAddressBatchInsert(insertDataList,
						excelName);
				importResultDO.setInfo("新增标准地址数据:" + insertDataList.size()
						+ "条");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogHome.getLog().error(excelName + "导入数据库入库失败", e);
			throw new Exception(excelName + "导入数据库入库失败，请联系管理员！");
		}
	} 
	/**
	 * 批量添加和修改PON拓扑连接,
	 */
	public static void importPonTopoLinkToDB (List<Map<String,Object>> dataList,String excelName,ImportResultDO importResultDO) throws Exception{
		try {
			List<Map<String,Object>> insertDataList = new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> updateDataList = new ArrayList<Map<String,Object>>();
			if(dataList!=null&&dataList.size()>0){
				for(Map<String,Object> m:dataList){
					String pcType = m.get("TYPE")+"";
					if("UPDATE".equals(pcType)){
						updateDataList.add(m);
					}
					if("INSERT".equals(pcType)){
						insertDataList.add(m);
					}
				}
			}
			if(updateDataList!=null&&updateDataList.size()>0){
				getImportBasicDataBO().importPonTopoLinkBatchUpdate(updateDataList,excelName);
				//级联更新链路其他信息
				for(Map<String,Object> map:updateDataList){
					getPonTopoLinkManageBo().changePortState(map, "UPDATE");
				}
				importResultDO.setInfo("更新PON拓扑连接数据:" + updateDataList.size() + "条");
			}
			if(insertDataList!=null&&insertDataList.size()>0){
				getImportBasicDataBO().importPonTopoLinkBatchInsert(insertDataList,excelName);
				//级联更新链路其他信息
				for(Map<String,Object> map:insertDataList){
					getPonTopoLinkManageBo().changePortState(map, "INSERT");
				}
				importResultDO.setInfo("新增PON拓扑连接数据:" + insertDataList.size() + "条");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogHome.getLog().error(excelName+"导入数据库入库失败",e);
			throw new Exception(excelName+"导入数据库入库失败，请联系管理员！");
		}
	} 
	public static void importGponCoverToDB(List<Map<String, Object>> dataList,
			String excelName, ImportResultDO importResultDO) throws Exception {
		try {
			List<Map<String, Object>> insertDataList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> updateDataList = new ArrayList<Map<String, Object>>();
			List<Map<String, String>> cuidList = new ArrayList<Map<String, String>>();
			if (dataList != null && dataList.size() > 0) {
				for (Map<String, Object> m : dataList) {
					String pcType = m.get("TYPE") + "";
					if ("UPDATE".equals(pcType)) {
						updateDataList.add(m);
					}
					if ("INSERT".equals(pcType)) {
						insertDataList.add(m);
					}
				}
			}
			if (updateDataList != null && updateDataList.size() > 0) {
				getImportBasicDataBO().importGponCoverBatchUpdate(
						updateDataList, excelName);
				importResultDO.setInfo("更新覆盖范围数据:" + updateDataList.size()
						+ "条");
			}
			if (insertDataList != null && insertDataList.size() > 0) {
				getImportBasicDataBO().importGponCoverBatchInsert(
						insertDataList, excelName);
				importResultDO.setInfo("新增覆盖范围数据:" + insertDataList.size()
						+ "条");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogHome.getLog().error(excelName + "导入数据库入库失败", e);
			throw new Exception(excelName + "导入数据库入库失败，请联系管理员！");
		}
	}
		
		/**
		 * EXCEL导入代维驻点数据.
		 * @param dataList
		 * @param excelName
		 * @param importResultDO
		 * @throws Exception
		 */
		public static void importRofhSiteToDB (List<Map<String,Object>> dataList,String excelName,ImportResultDO importResultDO) throws Exception{
			try {
				List<Map<String,Object>> insertDataList = new ArrayList<Map<String,Object>>();
				List<Map<String,Object>> updateDataList = new ArrayList<Map<String,Object>>();
				if(dataList!=null&&dataList.size()>0){
					for(Map<String,Object> m:dataList){
						String pcType = m.get("TYPE")+"";
						if("UPDATE".equals(pcType)){
							updateDataList.add(m);
						}
						if("INSERT".equals(pcType)){
							insertDataList.add(m);
						}
					}
				}
				if(updateDataList!=null&&updateDataList.size()>0){
					getImportBasicDataBO().importRofhSiteBatchUpdate(updateDataList,excelName);
					importResultDO.setInfo("更新代维驻点数据:" + updateDataList.size() + "条");
				}
				if(insertDataList!=null&&insertDataList.size()>0){
					getImportBasicDataBO().importRofhSiteBatchInsert(insertDataList,excelName);
					importResultDO.setInfo("新增代维驻点数据:" + insertDataList.size() + "条");
				}
			} catch (Exception e) {
				e.printStackTrace();
				LogHome.getLog().error(excelName+"导入数据库入库失败",e);
				throw new Exception(excelName+"导入数据库入库失败，请联系管理员！");
			}
		}
		
		/**
		 * EXCEL导入代维公司数据.
		 * @param dataList
		 * @param excelName
		 * @param importResultDO
		 * @throws Exception
		 */
		public static void importRofhMaintainDeptToDB (List<Map<String,Object>> dataList,String excelName,ImportResultDO importResultDO) throws Exception{
			try {
				List<Map<String,Object>> insertDataList = new ArrayList<Map<String,Object>>();
				List<Map<String,Object>> updateDataList = new ArrayList<Map<String,Object>>();
				if(dataList!=null&&dataList.size()>0){
					for(Map<String,Object> m:dataList){
						String pcType = m.get("TYPE")+"";
						if("UPDATE".equals(pcType)){
							updateDataList.add(m);
						}
						if("INSERT".equals(pcType)){
							insertDataList.add(m);
						}
					}
				}
				if(updateDataList!=null&&updateDataList.size()>0){
					getImportBasicDataBO().importRofhMaintainDeptBatchUpdate(updateDataList,excelName);
					importResultDO.setInfo("更新代维公司数据:" + updateDataList.size() + "条");
				}
				if(insertDataList!=null&&insertDataList.size()>0){
					getImportBasicDataBO().importRofhMaintainDeptBatchInsert(insertDataList,excelName);
					importResultDO.setInfo("新增代维公司数据:" + insertDataList.size() + "条");
				}
			} catch (Exception e) {
				e.printStackTrace();
				LogHome.getLog().error(excelName+"导入数据库入库失败",e);
				throw new Exception(excelName+"导入数据库入库失败，请联系管理员！");
			}
		}
	public static void importBusinessCommunityToDB(
			List<Map<String, Object>> dataList, String excelName,
			ImportResultDO importResultDO) throws Exception {
		try {
			List<Map<String, Object>> insertDataList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> updateDataList = new ArrayList<Map<String, Object>>();
			List<Map<String, String>> cuidList = new ArrayList<Map<String, String>>();
			if (dataList != null && dataList.size() > 0) {
				for (Map<String, Object> m : dataList) {
					String pcType = m.get("TYPE") + "";
					if ("UPDATE".equals(pcType)) {
						updateDataList.add(m);
					}
					if ("INSERT".equals(pcType)) {
						insertDataList.add(m);
					}
				}
			}
			if (updateDataList != null && updateDataList.size() > 0) {
				getImportBasicDataBO().importBusinessCommunityBatchUpdate(
						updateDataList, excelName);
				importResultDO
						.setInfo("更新业务区数据:" + updateDataList.size() + "条");
			}
			if (insertDataList != null && insertDataList.size() > 0) {
				getImportBasicDataBO().importBusinessCommunityBatchInsert(
						insertDataList, excelName);
				importResultDO
						.setInfo("新增业务区数据:" + insertDataList.size() + "条");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogHome.getLog().error(excelName + "导入数据库入库失败", e);
			throw new Exception(excelName + "导入数据库入库失败，请联系管理员！");
		}
	}

	/**
	 * 判断字符串是否是自然数
	 */
	public static boolean isNaturalNumber(String value) {
		try {
			int v = Integer.parseInt(value);
			if (v > 0) {
				return true;
			} else {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * 判断字符串是否正整数
	 */
	public static Integer isIntegerNumber(String value) {
		Integer res = null;
		try {
			int v = Integer.parseInt(value);
			if (v >= 0) {
				res = v;
			}
		} catch (NumberFormatException e) {
			res = null;
		}
		return res;
	}

	/**
	 * 判断字符串是否日期
	 */
	public static Timestamp isDate(String value) {
		Timestamp res = null;
		SimpleDateFormat sdf = null;
		Date dd = null;
		if (ImportCommonMethod.isNotEmpty(value)) {
			for (int i = 0; i < dateFormat.length; i++) {
				sdf = new SimpleDateFormat(dateFormat[i]);
				try {
					dd = sdf.parse(value);
					if (dd != null && sdf.format(dd).equals(value)) {
						break;
					}
				} catch (Exception e) {
					dd = null;
				}
				System.out.println(i);
			}
			if (dd != null) {
				res = new Timestamp(dd.getTime());
			}
		}
		return res;
	}

	public static String getCardPashFdn(String cardFdn) {
		String[] split = cardFdn.split("-");
		List<String> list = new ArrayList();
		String test = "^(0|[1-9][0-9]*)$";
		if (split.length == 3) {
			for (int i = 0; i < split.length; i++) {
				if (!split[i].matches(test)) {
					if ("NA".equals(split[i])) {
						list.add("-1");
					} else {
						list.add(split[i]);
					}
				} else {
					list.add(split[i]);
				}
			}
			return AnFdnHelper.fdnSpliterC + AnFdnHelper.RackHeader
					+ list.get(0) + AnFdnHelper.fdnSpliterC
					+ AnFdnHelper.ShelfHeader + list.get(1)
					+ AnFdnHelper.fdnSpliterC + AnFdnHelper.SlotHeader
					+ list.get(2);
		} else {
			return null;
		}
	}
}
