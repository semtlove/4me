import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class R2 {

//	String str제외수 = "";
	String 추첨회차 = "707";
	String str제외수 = "04,05,19,07,12,42,6,32,45";
	int i로또게임갯수 = 10;
	String str전회차당첨번호 = "01,07,22,33,37,40,20";
	public static String[] dmOne = new String[10]; // 년 ,회차 ,추첨일 ,1 ,2 ,3 ,4 ,5 ,6 ,뽀너스
	public static List arrOne = new ArrayList();

	public static void main(String[] args) {

		R2 oR2 = new R2();
		oR2.start();
	}

	public void start() {

		int[] lottoNumbers = new int[6];
		Random rnd = new Random();
		
		과거1등번호메모리load();
		과거1등분석_숫자사이차();

		while (i로또게임갯수 > 0) {

			for (int i = 0; i < lottoNumbers.length; i++) {
				lottoNumbers[i] = rnd.nextInt(45) + 1;

				// 중복된 값이 있으면 다시 랜덤값 구하기 위해 확인해서 index 값 줄여준다.
				for (int j = 0; j < i; j++) {
					if (lottoNumbers[i] == lottoNumbers[j]) {
						i--;
						break;
					}
				}
			}

			Arrays.sort(lottoNumbers); // sort
			// System.out.println(Arrays.toString(lottoNumbers)); // stringify &
			// print
			String rotto = Arrays.toString(lottoNumbers);
			rotto = rotto.replaceAll("\\[", "");
			rotto = rotto.replaceAll("\\]", "");
			String[] dmRotto = rotto.split(",");
			
			for (int inx = 0; inx < dmRotto.length; inx++) {
				dmRotto[inx] = dmRotto[inx].trim();
				dmRotto[inx] = 한자리인경우앞에0을붙임(dmRotto[inx]);
			}

			boolean ok = true;
			
			
			ok = 번호사이차(dmRotto);
			if (ok == false) continue;
			ok = 특정번호대에서4개이하나옴(dmRotto);
			if (ok == false) continue;
			ok = 회차체크(dmRotto);
			if (ok == false) continue;
			ok = 전회차에번호가1개or2개포함인치체크(dmRotto);
			if (ok == false) continue;
			ok = 전체합계체크(dmRotto);
			if (ok == false) continue;
			ok = 홀수짝수가5개쏠림이없는지체크(dmRotto);
			if (ok == false) continue;
			ok = 과거1등번호4개이상똑같은경우체크(dmRotto);
			if (ok == false) continue;
			ok = 뒷숫자가같은지체크(dmRotto);
			if (ok == false) continue;
			ok = 제외수포함체크(dmRotto);
			if (ok == false) continue;
			
			System.out.println(Arrays.toString(dmRotto));
			
			--i로또게임갯수;

		}
	}
	
	
	
	public void 과거1등번호메모리load() {
		
		
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
				
				int iCellPos = 1;
				
				for (int r = 0; r < rows; r++) {
					
					if ( r < 3 ) {
						continue;
					}
					
					String value = null;
					iCellPos = 1;
					dmOne = new String[10];
					
					row = sheet.getRow(r);
					if (row != null) {
						for (int c = 0; c < cells; c++) {
							
							if ( ( c == 1 || c == 2 || c >=13 ) == false ) {
								continue;
							}
							
							cell = row.getCell(c);
							if (cell != null) {
								
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
							
							if ( c >=13 ) {
								float f = Float.parseFloat(value);
								int i = (int)f;
								value = String.valueOf(i);
							}
							
							value = R2.한자리인경우앞에0을붙임(value);
							dmOne[iCellPos++] = value;							
							
						} // for(c)
						
						arrOne.add(dmOne);
						
						System.out.print("\n");
					} // if
				} // for(r)
				System.out.println(workbook.getSheetName(cn) + " sheet 데이터 취득 종료");
			}// for(cn)
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
//	int i차 = 0;
//	if (iCellPos >= 4) {
//		int i전 = Integer.parseInt(dmOne[iCellPos-1]);
//		i차 = Integer.parseInt(dmOne[iCellPos]) - i전;
//	}
	
	int i숫자사이차 = -9999;
	int i숫자사이1Cnt = 0;
	int i숫자사이2Cnt = 0;
	int i숫자사이4Cnt = 0;
	int i숫자사이18Cnt = 0;
	int i숫자사이20Cnt = 0;
	int i숫자사이22Cnt = 0;
	int i숫자사이27Cnt = 0;
	int i숫자사이30Cnt = 0;
	int i숫자사이33Cnt = 0;
	HashMap hmHashMap = new HashMap();
	public void 과거1등분석_숫자사이차() {
		
		int 회차 = 0;
		int i차 = 0;
		for (int inx=0; inx < arrOne.size(); inx++ ) {
			String[] dm과거1등번호 = (String[])arrOne.get(inx);
			
			for (int idx=4; idx < 9; idx++) {
				회차 = (int)Float.parseFloat(dm과거1등번호[1]);
				int i큰수 = Integer.parseInt(dm과거1등번호[idx]);
				int i작은수 = Integer.parseInt(dm과거1등번호[idx-1]);
				i차 = i큰수 - i작은수;
				
				if ( hmHashMap.containsKey(String.valueOf(i차)) == false ) {
					hmHashMap.put(String.valueOf(i차), "1");
				}
				else {
					int iCnt = Integer.parseInt((String)hmHashMap.get(String.valueOf(i차)));
					hmHashMap.put(String.valueOf(i차), String.valueOf(++iCnt));
				}
				
//				if ( 회차 == 534 ) {
//					System.out.println();
//				}
//				
//				if ( i차 == 1) {
//					i숫자사이1Cnt++;
//				}
//				else if ( i차 == 2) {
//					i숫자사이2Cnt++;
//				}
//				else if ( i차 == 4) {
//					i숫자사이4Cnt++;
//				}
//				else if ( i차 == 18) {
//					i숫자사이18Cnt++;
//				}
//				else if ( i차 == 20) {
//					i숫자사이20Cnt++;
//				}
//				else if ( i차 == 22) {
//					i숫자사이22Cnt++;
//				}
//				else if ( i차 == 27) {
//					i숫자사이27Cnt++;
//				}
//				else if ( i차 == 30) {
//					i숫자사이30Cnt++;
//				}
//				else if ( i차 == 33) {
//					i숫자사이33Cnt++;
//				}
				
				if ( i숫자사이차 < i차 ) {
					i숫자사이차 = i차;
					System.out.println("i숫자사이차 = " + i숫자사이차);					
				}
			}
			
		}
		
		System.out.println(hmHashMap);
		
	}
	
	
	public boolean 과거1등번호4개이상똑같은경우체크(String[] dmRotto) {
		
		boolean bRtnValue = true;
		
		String str과거1등번호 = "";
		String str현재추출번호 = "";
		String strTmp과거1등 = "";
		String strTmp현재추출 = "";
		int i같은갯수 = 0;
		for (int inx=0; inx < arrOne.size(); inx++ ) {
			String[] dm과거1등번호 = (String[])arrOne.get(inx);
			i같은갯수 = 0;
			
			for (int idx=3; idx < 9; idx++) {
				strTmp과거1등 = dm과거1등번호[idx];
				
				for (int idxx=0; idxx < 6; idxx++) {
					strTmp현재추출 = dmRotto[idxx];
					
					if ( strTmp과거1등.equals(strTmp현재추출) == true ) {
						++i같은갯수;
					}
				}
				
			}
			
			
			if ( i같은갯수 >= 4 ) {
				bRtnValue = false;
				break;
			}
		}
		
		return bRtnValue;
	}
	
	
	
	
	public boolean 특정번호대에서4개이하나옴(String[] dmRotto) {
		
		boolean rValue = true;

		int i1번대 = 0;
		int i10번대 =0;
		int i20번대 = 0;
		int i30번대 = 0;
		int i40번대 = 0;
		
		int i정수 = 0;
		for (int inx = 0; inx < dmRotto.length; inx++) {
			i정수 = Integer.parseInt(dmRotto[inx]);
			if ( i정수 <= 9 ) {
				i1번대++;
			}
			else if ( i정수 <= 19 ) {
				i10번대++;
			}
			else if ( i정수 <= 29 ) {
				i20번대++;
			}
			else if ( i정수 <= 39 ) {
				i30번대++;
			}
			else if ( i정수 <= 45 ) {
				i40번대++;
			}
		}
		
		if (i1번대 >= 4 || i10번대 >= 4 || i20번대 >= 4 || i30번대 >= 4 || i40번대 >= 4) {
			rValue = false;
		}
		
		return rValue;
	}
	
	
	
	public boolean 번호사이차(String[] dmRotto) {
		
		boolean rValue = true;

		int i차 = 0;
		for (int inx = 1; inx < dmRotto.length; inx++) {
			
			int i큰수 = Integer.parseInt(dmRotto[inx]);
			int i작은수 = Integer.parseInt(dmRotto[inx-1]);
			i차 = i큰수 - i작은수;
			
			if ( i차  > 9 ) {
				rValue = false;
				break;
			}
		}
		
		return rValue;
		
	}
	
	
	
	
	
	
	
	public boolean 회차체크(String[] dmRotto) {
		
		boolean rValue = false;

		String sTmp = "";
		for (int inx = 0; inx < dmRotto.length; inx++) {
			sTmp = dmRotto[inx].substring(0, 1);
			if ( "0".equals(sTmp) == false && 추첨회차.indexOf(sTmp) > -1 ) {
				rValue = true;
				break;
			}
			
			sTmp = dmRotto[inx].substring(1);
			if ( 추첨회차.indexOf(sTmp) > -1 ) {
				rValue = true;
				break;
			}
		}
		
		return rValue;
	}
	
	
	
	
	
	
	
	
	
	
	public boolean 전회차에번호가1개or2개포함인치체크(String[] dmRotto) {
		
		boolean rValue = true;

		int i갯수 = 0;
		for (int inx = 0; inx < dmRotto.length; inx++) {
			if ( str전회차당첨번호.indexOf(dmRotto[inx]) > -1 ) {
				i갯수 ++;
			}
		}
		
		if ( i갯수 == 1 ) {
			rValue = true;
		}
		else {
			rValue = false;
		}
		

		return rValue;
	}
	
	
	
	// http://lottosystem1.tistory.com/entry/%EB%A1%9C%EB%98%90%EC%A1%B0%ED%95%A9%EC%8B%9C%EC%8A%A4%ED%85%9C-%EB%A1%9C%EB%98%90%EB%8B%B9%EC%B2%A8%EB%B2%88%ED%98%B8-%ED%95%A9%EA%B3%84-%EA%B7%B8%EB%9E%98%ED%94%84-%ED%86%B5%EA%B3%84-1553%ED%9A%8C
	public boolean 전체합계체크(String[] dmRotto) {

		boolean rValue = true;

		int i합계 = 0;
		for (int inx = 0; inx < dmRotto.length; inx++) {
			i합계 += Integer.parseInt(dmRotto[inx]);
		}
		
		if ( 90 < i합계 && i합계  < 180 ) {
			rValue = true;
		}
		else {
			rValue = false;
		}

		return rValue;
	}
	
	
	
	
	
	public boolean 홀수짝수가5개쏠림이없는지체크(String[] dmRotto) {

		boolean rValue = true;
		int i짝수cnt = 0;
		int i홀수cnt = 0;

		for (int inx = 0; inx < dmRotto.length; inx++) {
			int i정수 = Integer.parseInt(dmRotto[inx]);
			
			if ( i정수 % 2 == 0 ) {
				i짝수cnt ++;
			}
			else {
				i홀수cnt ++;
			}
		}
		
		if ( i짝수cnt >= 5 || i홀수cnt >= 5 ) {
			rValue = false;
		}

		return rValue;
	}
	
	
	

	public boolean 제외수포함체크(String[] dmRotto) {

		boolean rValue = true;

		for (int inx = 0; inx < dmRotto.length; inx++) {

			if (str제외수.indexOf(dmRotto[inx]) > -1) {
				rValue = false;
				break;
			}
		}

		return rValue;
	}

	public boolean 뒷숫자가같은지체크(String[] dmRotto) {

		String str뒷자리모음 = "";
		int i같은수 = 0;
		for (int inx = 0; inx < dmRotto.length; inx++) {
			String 한자리 = dmRotto[inx].substring(1, 2);

			if (str뒷자리모음.indexOf(한자리) > -1) {

				++i같은수;
			}

			str뒷자리모음 += 한자리;
			str뒷자리모음 += "|";

		}

		boolean rValue = false;

		if (i같은수 >= 3) {
			rValue = false;
		}
		else if (i같은수 <= 2 ) {
			rValue = true;
		}

		return rValue;
	}

	public static String 한자리인경우앞에0을붙임(String s숫자) {
		if (s숫자.length() <= 1) {
			s숫자 = "0" + s숫자;
		}

		return s숫자;
	}

}
