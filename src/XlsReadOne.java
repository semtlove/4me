import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class XlsReadOne {
	public static void main(String[] args) {
		HSSFRow row;
		HSSFCell cell;

		try {
			String rootPath = System.getProperty("user.dir");
			String strExcelPath = rootPath + File.separator + "DATA" + File.separator + "Rotto2003.xls";
			FileInputStream inputStream = new FileInputStream(strExcelPath);
			POIFSFileSystem fileSystem = new POIFSFileSystem(inputStream);
			HSSFWorkbook workbook = new HSSFWorkbook(fileSystem);
			//sheet수 취득
			int sheetCn = workbook.getNumberOfSheets();
			System.out.println("sheet수 : " + sheetCn);
			
			for(int cn = 0; cn < sheetCn; cn++){
				System.out.println("취득하는 sheet 이름 : " + workbook.getSheetName(cn));
				System.out.println(workbook.getSheetName(cn) + " sheet 데이터 취득 시작");
				
				//cn번째 sheet 정보 취득
				HSSFSheet sheet = workbook.getSheetAt(cn);
				
				//취득된 sheet에서 rows수 취득
				int rows = sheet.getPhysicalNumberOfRows();
				System.out.println(workbook.getSheetName(cn) + " sheet의 row수 : " + rows);
				
				//취득된 row에서 취득대상 cell수 취득
				int cells = sheet.getRow(cn).getPhysicalNumberOfCells(); //
				System.out.println(workbook.getSheetName(cn) + " sheet의 row에 취득대상 cell수 : " + cells);
				
				System.out.println("------------------------------------------------------------------------------");
				
				for (int r = 0; r < rows; r++) {
					
					if ( r < 3 ) {
						continue;
					}
					
					row = sheet.getRow(r);
					if (row != null) {
						for (int c = 0; c < cells; c++) {
							
							if ( ( c == 1 || c == 2 || c >=13 ) == false ) {
								continue;
							}
							
							cell = row.getCell(c);
							if (cell != null) {
								String value = null;
								switch (cell.getCellType()) {
								case HSSFCell.CELL_TYPE_FORMULA:
									value = cell.getCellFormula();
									break;
								case HSSFCell.CELL_TYPE_NUMERIC:
									value = "" + cell.getNumericCellValue();
									break;
								case HSSFCell.CELL_TYPE_STRING:
									value = "" + cell.getStringCellValue();
									break;
								case HSSFCell.CELL_TYPE_BLANK:
									value = "" + cell.getBooleanCellValue();
									break;
								case HSSFCell.CELL_TYPE_ERROR:
									value = "" + cell.getErrorCellValue();
									break;
								default:
									value = "[type?]";
									break;
								}
								System.out.print(value + "\t");
							} else {
								System.out.print("[null]\t");
							} // if
						} // for(c)
						System.out.print("\n");
					} // if
				} // for(r)
				System.out.println(workbook.getSheetName(cn) + " sheet 데이터 취득 종료");
			}// for(cn)
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}