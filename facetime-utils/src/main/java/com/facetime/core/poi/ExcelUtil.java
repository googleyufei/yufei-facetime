package com.facetime.core.poi;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * excel导入导出from模板
 */
public class ExcelUtil {

	public static final ExcelUtil instance = new ExcelUtil();

	private ExcelUtil() {
		super();
	}

	public static final ExcelUtil getInstance() {
		return instance;
	}

	/**
	 * 获取excel模板
	 * 
	 * @param modelName
	 *            模板路径
	 * @return HSSFWorkbook 返回模板excel
	 * @throws Exception
	 */
	public HSSFWorkbook InitExcelModel(String modelName) throws Exception {
		HSSFWorkbook templatewb = null;
		try {
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(
					modelName));
			templatewb = new HSSFWorkbook(fs);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return templatewb;
	}

	/**
	 * 报表插入title/也可用于插入其他字段，如日期等
	 * 
	 * @param templatewb
	 *            报表模板workbook，来自InitExcelModel函数返回值
	 * @param sheet
	 *            工作表序列-从0开始
	 * @param title
	 *            报表标题
	 * @param titlestylerow
	 *            报表模板标题样式行，若为-1则无样式-从0开始
	 * @param titlestylecolume
	 *            报表模板标题样式列，从0开始
	 * @param i
	 *            行-从0开始
	 * @param j
	 *            列-从0开始
	 * @return HSSFWorkbook 返回模板excel
	 * @throws Exception
	 */
	public HSSFWorkbook InsertExcelTitle(HSSFWorkbook templatewb, int sheet,
			int titlestylerow, int titlestylecolume, String title, int i, int j)
			throws Exception {
		try {
			HSSFSheet templateSheet = templatewb.getSheetAt(sheet);
			if (titlestylerow == -1) {
				HSSFRow hssfRow = templateSheet.createRow(i);
				HSSFCell cell = hssfRow.createCell((short) j);
				// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(title);
			} else {
				HSSFRow templateRow = templateSheet.getRow(titlestylerow);
				HSSFCell templateCelldate = templateRow
						.getCell((short) titlestylecolume);
				HSSFRow hssfRow = templateSheet.createRow(i);
				HSSFCell cell = hssfRow.createCell((short) j);
				cell.setCellStyle(templateCelldate.getCellStyle());
				cell.setCellValue(title);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return templatewb;
	}

	/**
	 * 报表更新title/也可用于更新其他字段，如日期等 调用该方法设置的列必须带有样式
	 * 
	 * @param templatewb
	 *            报表模板workbook，来自InitExcelModel函数返回值
	 * @param sheet
	 *            工作表序列-从0开始
	 * @param title
	 *            报表标题
	 * @param i
	 *            行-从0开始-注意模板该行已有相应的style，否则请使用insert
	 * @param j
	 *            列-从0开始-注意模板该列已有相应的style，否则请使用insert
	 * @return HSSFWorkbook 返回模板excel
	 * @throws Exception
	 */
	public HSSFWorkbook UpdateExcelTitle(HSSFWorkbook templatewb, int sheet,
			String title, int i, int j) throws Exception {
		try {
			HSSFSheet templateSheet = templatewb.getSheetAt(sheet);
			HSSFRow templateRow = templateSheet.getRow(i);
			HSSFCell templateCelldate = templateRow.getCell((short) j);
			// System.out.println("aaaa"+i+j);
			// templateCelldate.setEncoding(HSSFCell.ENCODING_UTF_16);
			templateCelldate.setCellValue(title);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return templatewb;
	}

	/**
	 * 新sheet中插入列表栏
	 * 
	 * @param rowspecify
	 *            列表说明栏行数，在新sheet的data值上将自动复制columespecify做为列表说明栏
	 *            若columespecify为-1，则不自动插入列表说明栏，请使用InsertExcelTitle自行插入列表说明栏
	 * @param columespecify
	 *            列表说明栏起始列-列数应和数据列数和样式列数相等
	 */
	public HSSFWorkbook InsertColumeSpecify(HSSFWorkbook templatewb,
			int stylesheet, int tosheet, int rowspecify, int columespecify,
			int i, int j) {
		try {
			HSSFSheet templateSheet = templatewb.getSheetAt(stylesheet);
			HSSFRow templateRow = templateSheet.getRow(rowspecify);
			HSSFSheet sheet = templatewb.getSheetAt(0);
			if (tosheet != 0) {
				sheet = templatewb.createSheet("sheet"
						+ String.valueOf(tosheet + 1));
			}
			for (int columnId = j; columnId < (j + templateRow
					.getPhysicalNumberOfCells()); columnId++) {
				HSSFCell templateCell = templateRow
						.getCell((short) (columnId - j));
				HSSFRow hssfRow = sheet.createRow(i);
				HSSFCell cell = hssfRow.createCell((short) columnId);
				// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellStyle(templateCell.getCellStyle());
				cell.setCellValue(templateCell.getStringCellValue());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return templatewb;
	}

	public HSSFCellStyle[] getDataCellStyle(HSSFWorkbook templatewb,
			int stylesheet, int stylerow, int stylecolume) {
		HSSFSheet templateSheet = templatewb.getSheetAt(stylesheet);
		HSSFRow templateRow = templateSheet.getRow(stylerow);
		// System.out.println("style="+stylerow);
		HSSFCellStyle[] styles = new HSSFCellStyle[templateRow
				.getPhysicalNumberOfCells()];
		// System.out.println(templateRow.getPhysicalNumberOfCells());
		for (int i = stylecolume; i < templateRow.getPhysicalNumberOfCells(); i++) {
			styles[i] = templateRow.getCell((short) i).getCellStyle();
		}
		return styles;
	}

	public HSSFCellStyle[] getDataCellStyleAddcash(HSSFWorkbook templatewb,
			int stylesheet, int stylerow, int stylecolume) {
		HSSFSheet templateSheet = templatewb.getSheetAt(stylesheet);
		HSSFRow templateRow = templateSheet.getRow(stylerow);
		// System.out.println("style="+stylerow);
		HSSFCellStyle[] styles = new HSSFCellStyle[templateRow
				.getPhysicalNumberOfCells()];
		// System.out.println(templateRow.getPhysicalNumberOfCells());
		for (int i = stylecolume; i < templateRow.getPhysicalNumberOfCells(); i++) {
			styles[i] = templateRow.getCell((short) i).getCellStyle();
		}
		return styles;
	}

	/**
	 * 报表更新data
	 * 
	 * @param templatewb
	 *            报表模板workbook，来自InitExcelModel函数返回值
	 * @param sheet
	 *            工作表序列-从0开始-注意此工作表已存在
	 * @param styles
	 *            数据列表样式，来自getDataCellStyle函数返回值
	 * @param stylerow
	 *            样式数据行-数据栏在样式工作表中的行数
	 * @param stylecolume
	 *            样式数据列-数据栏在样式工作表中的起始列数，注意样式列数应与数据列数相等
	 * @param dataList
	 *            数据list
	 * @param i
	 *            行-从0开始
	 * @param j
	 *            列-从0开始
	 * @return HSSFWorkbook 返回模板excel
	 * @throws Exception
	 */
	private HSSFWorkbook UpdateExcelData(HSSFWorkbook templatewb,
			HSSFCellStyle[] styles, int sheet, List dataList, int i, int j)
			throws Exception {
		try {
			HSSFSheet tosheet = templatewb.getSheetAt(sheet);
			int size = dataList.size();
			if (size > 30000)
				size = 30000 - i;
			for (int rowId = 1; rowId <= size; rowId++) {
				Object[] valueList = (Object[]) dataList.get(rowId - 1);
				for (int columnId = j; columnId < (j + valueList.length); columnId++) {
					String dataValue = String.valueOf(valueList[columnId]);
					HSSFRow hssfRow = tosheet.createRow(rowId + i - 1);
					hssfRow.setHeightInPoints(25); // 设置行高
					HSSFCell cell = hssfRow.createCell((short) columnId);
					// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellStyle(styles[columnId - j]);
					if (valueList[columnId] != null
							&& valueList[columnId] instanceof Integer)
						cell.setCellValue(Integer.valueOf(dataValue));
					else if (valueList[columnId] != null
							&& valueList[columnId] instanceof Long)
						cell.setCellValue(Long.valueOf(dataValue));
					else if (valueList[columnId] != null
							&& valueList[columnId] instanceof Double)
						cell.setCellValue(Double.valueOf(dataValue));
					else
						cell.setCellValue(dataValue);
				}
			}
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			System.out.println("Data Exceeded!");
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return templatewb;
	}

	/**
	 * 输出excel
	 * 
	 * @param templatewb
	 *            workbook
	 * @param os
	 *            输出文件流
	 */
	public void OutputExcel(HSSFWorkbook templatewb, OutputStream os) {
		try {
			templatewb.write(os);
			os.flush();
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	/**
	 * 输出excel
	 * 
	 * @param templatewb
	 *            workbook
	 * @param outputfile
	 *            输出文件路径
	 */
	public void OutputExcelFile(HSSFWorkbook templatewb, String outputfile) {
		try {
			FileOutputStream fOut = new FileOutputStream(outputfile);
			templatewb.write(fOut);
			fOut.flush();
			fOut.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 根据现有需求封装的输出标准excel报表函数，若有特殊要求，请重写该函数
	 * 注意，模板建立时请至少建立六个或以上空白sheet，其中第一个sheet要包含如下格式：
	 * 第一行，标题行，设置好相应的样式，包括字体、颜色、有无边框、几列合并等 第二行，制表日期，设置好相应的样式 第三行，列表栏，设置好相应的样式
	 * 第四行，数据样式栏，设置好相应的样式，导出数据后，此行会被填充值，以后的数据行将复制此行的样式
	 * 
	 * @param modelName
	 *            模板路径
	 * @param outputFile
	 *            输出文件路径
	 * @param dataList
	 *            数据list
	 * @param startLine
	 *            数据起始行-注意模板的该起始行已有样式，且该起始行的上一行已有列表说明栏，默认从第0列开始插数据
	 * @param title
	 *            标题内容
	 * @param titlerow
	 *            标题行-注意模板的标题行已有样式，默认从第0列开始插数据
	 * @param date
	 *            制表日期
	 * @param daterow
	 *            日期行-注意模板的日期行已有样式，默认从第2列开始插数据
	 * @throws Exception
	 */
	public void CreateStandardExcelFromModel(String modelName, OutputStream os,
			List dataList, int startLine, String title, int titlerow,
			String date, int daterow) throws Exception {
		HSSFWorkbook templatewb = this.InitExcelModel(modelName);
		templatewb = this.UpdateExcelTitle(templatewb, 0, title, titlerow, 0);
		templatewb = this.UpdateExcelTitle(templatewb, 0, date, daterow, 2);
		List templist = dataList;
		int sheetnum = dataList.size() / 30000;
		if (sheetnum * 30000 < dataList.size())
			sheetnum += 1;
		if (sheetnum != 0) {
			HSSFCellStyle[] styles = this.getDataCellStyle(templatewb, 0,
					startLine, 0);
			for (int m = 0; m < sheetnum; m++) {
				if (m > 1) {
					for (int n = 0; n < 30000; n++) {
						templist.remove(0);
					}
					// System.out.println("m="+m+":"+templist.size());
					templatewb = this.InsertColumeSpecify(templatewb, 0, m, 2,
							0, 0, 0);
					if (templatewb == null)
						break;
					templatewb = this.UpdateExcelData(templatewb, styles, m,
							templist, 1, 0);
					if (templatewb == null)
						break;
				} else if (m == 1) {
					for (int n = 0; n < 30000 - startLine; n++) {
						templist.remove(0);
					}
					// System.out.println("m="+m+":"+templist.size());
					templatewb = this.InsertColumeSpecify(templatewb, 0, m, 2,
							0, 0, 0);
					if (templatewb == null)
						break;
					templatewb = this.UpdateExcelData(templatewb, styles, m,
							templist, 1, 0);
					if (templatewb == null)
						break;
				} else {
					// System.out.println("m="+m+":"+templist.size());
					templatewb = this.UpdateExcelData(templatewb, styles, 0,
							templist, startLine, 0);
					if (templatewb == null)
						break;
				}
			}
			if (templatewb != null) {
				for (int m = sheetnum; m < templatewb.getNumberOfSheets(); m++)
					templatewb.removeSheetAt(sheetnum);
				this.OutputExcel(templatewb, os);
			} else {
				templatewb = this.InitExcelModel(modelName);
				// System.out.println(templatewb);
				// System.out.println(templatewb.getSheetAt(0).getRow(0).getCell((short)0).getStringCellValue());
				for (int i = 1; i < templatewb.getSheetAt(0)
						.getPhysicalNumberOfRows(); i++) {
					HSSFRow hssfRow = templatewb.getSheetAt(0).getRow(i);
					// System.out.println(i);
					if (hssfRow != null)
						templatewb.getSheetAt(0).removeRow(hssfRow);
				}
				templatewb.getSheetAt(0).removeMergedRegion(0);
				templatewb = this.InsertExcelTitle(templatewb, 0, -1, 0,
						"数据超出限制，请缩小导出范围！", 0, 0);
				this.OutputExcel(templatewb, os);
			}
		} else {
			this.OutputExcel(templatewb, os);
		}
		// this.OutputExcelFile(templatewb,
		// "D:\\vss\\FEELView4.0_PR1\\feelview\\feelview\\pages\\basereport\\testFile.xls");
	}

	/**
	 * 根据现有需求封装的输出标准excel报表函数，若有特殊要求，请重写该函数
	 * 注意，模板建立时请至少建立六个或以上空白sheet，其中第一个sheet要包含如下格式：
	 * 第一行，标题行，设置好相应的样式，包括字体、颜色、有无边框、几列合并等 第二行，制表日期，设置好相应的样式 第三行，列表栏，设置好相应的样式
	 * 第四行，数据样式栏，设置好相应的样式，导出数据后，此行会被填充值，以后的数据行将复制此行的样式
	 * 
	 * @param modelName
	 *            模板路径
	 * @param outputFile
	 *            输出文件路径
	 * @param dataList
	 *            数据list
	 * @param startLine
	 *            数据起始行-注意模板的该起始行已有样式，且该起始行的上一行已有列表说明栏，默认从第0列开始插数据
	 * @throws Exception
	 */
	public void CreateStandardExcelFromModel(String modelName, OutputStream os,
			List dataList, int startLine) throws Exception {
		HSSFWorkbook templatewb = this.createExcelFromModel(modelName,
				dataList, startLine);
		this.OutputExcel(templatewb, os);
	}

	public HSSFWorkbook createExcelFromModel(String modelName,
			List<?> dataList, int startLine) throws Exception {
		HSSFWorkbook templatewb = this.InitExcelModel(modelName);
		int sheetnum = dataList.size() / 30000;
		if (sheetnum * 30000 < dataList.size())
			sheetnum += 1;
		if (sheetnum != 0) {
			HSSFCellStyle[] styles = this.getDataCellStyle(templatewb, 0,
					startLine, 0);
			for (int m = 0; m < sheetnum; m++) {
				if (m > 1) {
					for (int n = 0; n < 30000; n++) {
						dataList.remove(0);
					}
					templatewb = this.InsertColumeSpecify(templatewb, 0, m, 2,
							0, 0, 0);
					if (templatewb == null)
						break;
					templatewb = this.UpdateExcelData(templatewb, styles, m,
							dataList, 1, 0);
					if (templatewb == null)
						break;
				} else if (m == 1) {
					for (int n = 0; n < 30000 - startLine; n++) {
						dataList.remove(0);
					}
					templatewb = this.InsertColumeSpecify(templatewb, 0, m, 2,
							0, 0, 0);
					if (templatewb == null)
						break;
					templatewb = this.UpdateExcelData(templatewb, styles, m,
							dataList, 1, 0);
					if (templatewb == null)
						break;
				} else {
					templatewb = this.UpdateExcelData(templatewb, styles, 0,
							dataList, startLine, 0);
					if (templatewb == null)
						break;
				}
			}
			if (templatewb != null) {
				for (int m = sheetnum; m < templatewb.getNumberOfSheets(); m++) {
					templatewb.removeSheetAt(sheetnum);
				}
			} else {
				templatewb = this.InitExcelModel(modelName);
				for (int i = 1; i < templatewb.getSheetAt(0)
						.getPhysicalNumberOfRows(); i++) {
					HSSFRow hssfRow = templatewb.getSheetAt(0).getRow(i);
					if (hssfRow != null)
						templatewb.getSheetAt(0).removeRow(hssfRow);
				}
				templatewb.getSheetAt(0).removeMergedRegion(0);
				templatewb = this.InsertExcelTitle(templatewb, 0, -1, 0,
						"数据超出限制，请缩小导出范围！", 0, 0);
			}
		}
		return templatewb;
	}

	/**
	 * 根据现有需求封装的输出标准excel报表函数2，若有特殊要求，请重写该函数
	 * 注意，模板建立时请至少建立六个或以上空白sheet，其中第一个sheet要包含如下格式：
	 * 第一行，标题行，设置好相应的样式，包括字体、颜色、有无边框、几列合并等 第二行，制表日期，设置好相应的样式 第三行，列表栏，设置好相应的样式
	 * 第四行，数据样式栏，设置好相应的样式，导出数据后，此行会被填充值，以后的数据行将复制此行的样式
	 * 
	 * @param modelName
	 *            模板路径
	 * @param outputFile
	 *            输出文件路径
	 * @param dataList
	 *            数据list
	 * @param startLine
	 *            数据起始行-注意模板的该起始行已有样式，且该起始行的上一行已有列表说明栏，默认从第0列开始插数据
	 * @param title
	 *            标题内容
	 * @param titlerow
	 *            标题行-注意模板的标题行已有样式，默认从第0列开始插数据
	 * @param date
	 *            制表日期
	 * @param daterow
	 *            日期行-注意模板的日期行已有样式，默认从第2列开始插数据
	 * @param othercontent
	 *            制表日期后需要加入新的信息
	 * @param other
	 *            加入的列
	 * @throws Exception
	 */
	public void CreateStandardExcelFromModel(String modelName, OutputStream os,
			List dataList, int startLine, String title, int titlerow,
			String date, int daterow, String othercontent, int other)
			throws Exception {
		HSSFWorkbook templatewb = this.InitExcelModel(modelName);
		if (title != null) {
			templatewb = this.UpdateExcelTitle(templatewb, 0, title, titlerow,
					0);
		}
		// System.out.println("123");
		templatewb = this.UpdateExcelTitle(templatewb, 0, date, daterow, 2);
		// System.out.println("456");
		templatewb = this.UpdateExcelTitle(templatewb, 0, othercontent,
				daterow, other);
		// System.out.println("other="+othercontent);
		List templist = dataList;
		int sheetnum = dataList.size() / 30000;
		if (sheetnum * 30000 < dataList.size())
			sheetnum += 1;
		if (sheetnum != 0) {
			HSSFCellStyle[] styles = this.getDataCellStyle(templatewb, 0,
					startLine, 0);
			for (int m = 0; m < sheetnum; m++) {
				if (m > 1) {
					for (int n = 0; n < 30000; n++) {
						templist.remove(0);
					}
					// System.out.println("m="+m+":"+templist.size());
					templatewb = this.InsertColumeSpecify(templatewb, 0, m, 2,
							0, 0, 0);
					if (templatewb == null)
						break;
					templatewb = this.UpdateExcelData(templatewb, styles, m,
							templist, 1, 0);
					if (templatewb == null)
						break;
				} else if (m == 1) {
					for (int n = 0; n < 30000 - startLine; n++) {
						templist.remove(0);
					}
					// System.out.println("m="+m+":"+templist.size());
					templatewb = this.InsertColumeSpecify(templatewb, 0, m, 2,
							0, 0, 0);
					if (templatewb == null)
						break;
					templatewb = this.UpdateExcelData(templatewb, styles, m,
							templist, 1, 0);
					if (templatewb == null)
						break;
				} else {
					// System.out.println("m="+m+":"+templist.size());
					templatewb = this.UpdateExcelData(templatewb, styles, 0,
							templist, startLine, 0);
					if (templatewb == null)
						break;
				}
			}
			if (templatewb != null) {
				for (int m = sheetnum; m < templatewb.getNumberOfSheets(); m++)
					templatewb.removeSheetAt(sheetnum);
				this.OutputExcel(templatewb, os);
			} else {
				templatewb = this.InitExcelModel(modelName);
				// System.out.println(templatewb);
				// System.out.println(templatewb.getSheetAt(0).getRow(0).getCell((short)0).getStringCellValue());
				for (int i = 1; i < templatewb.getSheetAt(0)
						.getPhysicalNumberOfRows(); i++) {
					HSSFRow hssfRow = templatewb.getSheetAt(0).getRow(i);
					// System.out.println(i);
					if (hssfRow != null)
						templatewb.getSheetAt(0).removeRow(hssfRow);
				}
				templatewb.getSheetAt(0).removeMergedRegion(0);
				templatewb = this.InsertExcelTitle(templatewb, 0, -1, 0,
						"数据超出限制，请缩小导出范围！", 0, 0);
				this.OutputExcel(templatewb, os);
			}
		} else {
			this.OutputExcel(templatewb, os);
		}
		// this.OutputExcelFile(templatewb,
		// "D:\\vss\\FEELView4.0_PR1\\feelview\\feelview\\pages\\basereport\\testFile.xls");
	}

	// 此方法中用于交易明细查询时加上统计信息：总笔数、总交易金额 zwing 长江商行
	public void CreateTradeStandardExcelFromModel(String modelName,
			OutputStream os, List dataList, int startLine, String title,
			int titlerow, String date, int daterow, String othercontent,
			int other) throws Exception {
		HSSFWorkbook templatewb = this.InitExcelModel(modelName);
		templatewb = this.UpdateExcelTitle(templatewb, 0, title, titlerow, 0);
		// System.out.println("123");
		templatewb = this.UpdateExcelTitle(templatewb, 0, date, daterow, 2);
		// System.out.println("456");
		templatewb = this.UpdateExcelTitle(templatewb, 0, othercontent,
				daterow, other);
		// System.out.println("other="+othercontent);
		List templist = dataList;
		int sheetnum = dataList.size() / 30000;
		if (sheetnum * 30000 < dataList.size())
			sheetnum += 1;
		if (sheetnum != 0) {
			int num = 0;
			HSSFCellStyle[] styles = this.getDataCellStyle(templatewb, 0,
					startLine, 0);
			for (int m = 0; m < sheetnum; m++) {
				num = m;
				if (m > 1) {
					for (int n = 0; n < 30000; n++) {
						templist.remove(0);
					}
					// System.out.println("m="+m+":"+templist.size());
					templatewb = this.InsertColumeSpecify(templatewb, 0, m, 2,
							0, 0, 0);
					if (templatewb == null)
						break;
					templatewb = this.UpdateExcelData(templatewb, styles, m,
							templist, 1, 0);
					if (templatewb == null)
						break;
				} else if (m == 1) {
					for (int n = 0; n < 30000 - startLine; n++) {
						templist.remove(0);
					}
					// System.out.println("m="+m+":"+templist.size());
					templatewb = this.InsertColumeSpecify(templatewb, 0, m, 2,
							0, 0, 0);
					if (templatewb == null)
						break;
					templatewb = this.UpdateExcelData(templatewb, styles, m,
							templist, 1, 0);
					if (templatewb == null)
						break;
				} else {
					// System.out.println("m="+m+":"+templist.size());
					templatewb = this.UpdateExcelData(templatewb, styles, 0,
							templist, startLine, 0);
					if (templatewb == null)
						break;
				}

			}
			// 最后一行加上汇总信息 daterow：行 other：列 othercontent：要填入的内容
			// sheetnum：sheet表号，从0开始
			this.InsertExcelTitle(templatewb, num, 2, 0, "总记录条数：",
					templist.size() + 5, 2);
			this.InsertExcelTitle(templatewb, num, -1, 0,
					String.valueOf(templist.size()), templist.size() + 5, 3);
			this.InsertExcelTitle(templatewb, num, 2, 0, "总金额：",
					templist.size() + 5, 5);
			double l = 0;
			for (ListIterator it = dataList.listIterator(); it.hasNext();) {
				Object[] obj = (Object[]) it.next();
				if (obj[6] != null)
					l += Double.parseDouble((String.valueOf(obj[6])));
			}
			this.InsertExcelTitle(templatewb, num, -1, 0, String.valueOf(l),
					templist.size() + 5, 6);

			if (templatewb != null) {
				for (int m = sheetnum; m < templatewb.getNumberOfSheets(); m++)
					templatewb.removeSheetAt(sheetnum);
				this.OutputExcel(templatewb, os);
			} else {
				templatewb = this.InitExcelModel(modelName);
				// System.out.println(templatewb);
				// System.out.println(templatewb.getSheetAt(0).getRow(0).getCell((short)0).getStringCellValue());
				for (int i = 1; i < templatewb.getSheetAt(0)
						.getPhysicalNumberOfRows(); i++) {
					HSSFRow hssfRow = templatewb.getSheetAt(0).getRow(i);
					// System.out.println(i);
					if (hssfRow != null)
						templatewb.getSheetAt(0).removeRow(hssfRow);
				}
				templatewb.getSheetAt(0).removeMergedRegion(0);
				templatewb = this.InsertExcelTitle(templatewb, 0, -1, 0,
						"数据超出限制，请缩小导出范围！", 0, 0);
				this.OutputExcel(templatewb, os);
			}
		} else {
			this.OutputExcel(templatewb, os);
		}
	}

	public void close(Closeable... closeables) {
		for (Closeable closeable : closeables) {
			if (closeable != null) {
				try {
					closeable.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public Map<String, String> getZhCn2EnMap(Map<String, String> source) {
		Map<String, String> dest = new HashMap<String, String>();
		Set<String> keySet = source.keySet();
		Iterator<String> iter = keySet.iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			dest.put(source.get(key), key);
		}
		return dest;
	}

	public static void main(String[] args) throws Exception {
		ArrayList list = new ArrayList();

		for (int i = 0; i < 150000; i++) {
			String[] data = new String[17];
			data[0] = "GOOD!";
			data[1] = "BAD!";
			data[2] = "JUST SO SO!";
			data[3] = "";
			data[4] = "";
			data[5] = "";
			data[6] = "";
			data[7] = "";
			data[8] = "";
			data[9] = "";
			data[10] = "";
			data[11] = "";
			data[12] = "";
			data[13] = "";
			data[14] = "";
			data[15] = "";
			data[16] = "";
			list.add(data);
			// System.out.println(i);
		}
		ExcelUtil test = new ExcelUtil();
		try {
			test.CreateStandardExcelFromModel(
					"D:\\vss\\feelview4.0PR1\\feelview\\feelview\\pages\\basereport\\excel\\ATMTradeAnalyze.xlt",
					null, list, 3, "自 助 设 备 交 易 情 况 分 析（月报）", 0, "2009-01-15",
					1);
		} catch (OutOfMemoryError ex) {
			// ex.printStackTrace();
			System.out.println("Data Exceed Limit");
		}
	}
}