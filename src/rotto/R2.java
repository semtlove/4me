package rotto;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class R2 {

	String str추첨일 = "";
	String str제외수 = "08,02,03,32"; // 포함수 or 추첨회차는 피해야 함.
	String str포함수 = "05,15"; // ex) : 01,07,22
//	String str포함수 = "";
	String 추첨회차 = "720";	// ex) 714
	int i로또게임갯수 = 5;
	String str전회차당첨번호 = "04,08,13,19,20,43";
	public static String[] dmOne = new String[10]; // 년 ,회차 ,추첨일 ,1 ,2 ,3 ,4 ,5 ,6 ,뽀너스
	public static List arrOne = new ArrayList();

	public static void main(String[] args) {

		R2 oR2 = new R2();
		try {
			oR2.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	int i시도횟수 = 0;
	public void start() throws Exception {

		int[] lottoNumbers = new int[6];
		Random rnd = new Random();
		
		과거1등번호메모리load();
		
		if ( arrOne == null || arrOne.size() <= 5 ) {
			throw new Exception("aaaaaaaa");
		}
		
		//------------------------------------------ 분석 ----------------------------------------
		과거1등분석_숫자사이차();
		과거1등분석_전회차숫자가지수포함();
		과거1등분석_첫숫자가10일때숫자분포분석();
		과거1등분석_행분석();
		과거1등분석_열분석();
		과거1등분석_일자포함분석();

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
			
			
			
//			dmRotto[0] = "04";
//			dmRotto[1] = "11";
//			dmRotto[2] = "20";
//			dmRotto[3] = "23";
//			dmRotto[4] = "32";
//			dmRotto[5] = "39";
//			
//			rotto = rotto.trim();
//			rotto = rotto.replaceAll(" ", "");
//			if ( rotto.equals("4,11,20,23,32,39") ) {
//				System.out.println("\n뽀숑~~~~~");
//			}

			boolean ok = true;
			
			
			ok = 체크_포함수포함(dmRotto);		// a
			if (ok == false) continue;
			ok = 체크_제외수포함(dmRotto);		// a
			if (ok == false) continue;
			
			
			ok = 체크_전회차에번호가1개or2개포함(dmRotto); // 66%
			if (ok == false) continue;
			ok = 체크_뒷숫자가같음(dmRotto); // 79%
			if (ok == false) continue;
			ok = 체크_일자포함(dmRotto);		// 83%			
			if (ok == false) continue;
			ok = 체크_홀수짝수가5개쏠림(dmRotto);	// 83% c
			if (ok == false) continue;
				
			
			ok = 체크_전체합계(dmRotto);	// 95%
			if (ok == false) continue;
			ok = 체크_번호사이차(dmRotto);		// 97%
			if (ok == false) continue;
			ok = 체크_특정번호대에서4개이하나옴(dmRotto); // 90%
			if (ok == false) continue;
			ok = 체크_회차(dmRotto);	// 96%
			if (ok == false) continue;
			ok = 체크_전회차숫자가지수포함(dmRotto); // 90%
			if (ok == false) continue;	
			ok = 체크_과거1등번호4개이상똑같은경우(dmRotto); // 100%
			if (ok == false) continue;
			ok = 체크_열분석(dmRotto);	// 100%
			if (ok == false) continue;
			ok = 체크_행분석(dmRotto);	// 97%
			if (ok == false) continue;
			
			
//			if ( rotto.equals("2,11,19,25,28,32") ) {
//				System.out.println("i시도횟수 =" + i시도횟수);
//				System.out.println(Arrays.toString(dmRotto));
//				break;
//			}
//			else {
//				if ( i시도횟수 % 1000 == 0 ) {
//					System.out.print("->");
//					if ( i시도횟수 % 10000 == 0 ) {
//						System.out.println();
//					}
//				}
//			}
			
			System.out.println(Arrays.toString(dmRotto));
			
			++i시도횟수;
			--i로또게임갯수;
			
			

		}
		
		System.out.println("------------------------------- END ----------------------------");
	}
	
	
	
	public List 과거1등번호메모리load() {
		
		
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
			
			for(int cn = 0; cn < sheetCn-1; cn++) { // <---- -1이 맞는지 확인 필요.
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
		
		Collections.reverse(arrOne);
		
		return arrOne;
		
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
		
		int i차 = 0;
		for (int inx=0; inx < arrOne.size(); inx++ ) {
			String[] dm과거1등번호 = (String[])arrOne.get(inx);
			
			for (int idx=4; idx <= 8; idx++) { // 여긴 4가 맞음.
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
								
				if ( i숫자사이차 < i차 ) {
					i숫자사이차 = i차;
//					System.out.println("i숫자사이차 = " + i숫자사이차);					
				}
			}
			
		}
		
		System.out.println(hmHashMap);
		
	}
	
	
	
	
	
	public void 과거1등분석_첫숫자가10일때숫자분포분석() {
		
		System.out.println("------------------------- 과거1등분석_첫숫자가10일때숫자분포분석 -------------------------");
		
		
		
		int i숫자대 = 0;
		for (int inx=0; inx < arrOne.size(); inx++ ) {
			String[] dm과거1등번호 = (String[])arrOne.get(inx);
			
			int i첫수 = Integer.parseInt(dm과거1등번호[3]);
			
			if ( 10 <= i첫수 && i첫수 <= 19 ) {
				
			}
			else {
				continue;
			}
			
			hmHashMap = new HashMap();
			hmHashMap.put(String.valueOf(10), "0");
			hmHashMap.put(String.valueOf(20), "0");
			hmHashMap.put(String.valueOf(30), "0");
			hmHashMap.put(String.valueOf(40), "0");
			for (int idx=4; idx <= 8; idx++) {
				
				i첫수 = Integer.parseInt(dm과거1등번호[idx]);
				
				if ( 10 <= i첫수 && i첫수 <= 19 ) {
					i숫자대 = 10;
				}
				else if ( 20 <= i첫수 && i첫수 <= 29 ) {
					i숫자대 = 20;
				}
				else if ( 30 <= i첫수 && i첫수 <= 39 ) {
					i숫자대 = 30;
				}
				else if ( 40 <= i첫수 && i첫수 <= 45 ) {
					i숫자대 = 40;
				}
					
				if ( hmHashMap.containsKey(String.valueOf(i숫자대)) == false ) {
					hmHashMap.put(String.valueOf(i숫자대), "1");
				}
				else {
					int iCnt = Integer.parseInt((String)hmHashMap.get(String.valueOf(i숫자대)));
					hmHashMap.put(String.valueOf(i숫자대), String.valueOf(++iCnt));
				}
					
				
			}
			
			System.out.println(dm과거1등번호[1] + "|" + hmHashMap);
			
		}
		
		
		
	}
	
	
	
	
	public void 과거1등분석_행분석() {
		
		System.out.println("------------------------- 과거1등분석_행분석 -------------------------");
		
		String[][] dm과거1등행렬 = new String[8][8]; // 8 ,8인 이유는 0부터 안하기 위해..
		
		int iSuCnt = 0;

		boolean foundout = false;
		HashMap hmHashMap행 = null;
		
		String str회차 = "";
		for (int I회차=0; I회차 < arrOne.size(); I회차++ ) {
			
			String[] dm과거1등번호 = (String[])arrOne.get(I회차);
			
			
			hmHashMap행 = new HashMap();
			for (int I숫자위치=3; I숫자위치 <= 8; I숫자위치++) {
				
				String str선택숫자 = dm과거1등번호[I숫자위치];
				int i선택숫자 = Integer.parseInt(str선택숫자);
				
//				System.out.println("i선택숫자=" + i선택숫자);
				
				foundout = false;
				iSuCnt = 0;
				for (int i행X=1; i행X < dm과거1등행렬.length; i행X++) {
					
					for (int i열Y=1; i열Y < dm과거1등행렬[i행X].length; i열Y++) {	
						
						iSuCnt++;	
						
						if (i선택숫자 == iSuCnt) {
							dm과거1등행렬[i행X][i열Y] = "*";
							
//							System.out.println("i행X=" + i행X);
							if ( hmHashMap행.containsKey(String.valueOf(i행X)) == false ) {
								
								hmHashMap행.put(String.valueOf(1), String.valueOf(0));
								hmHashMap행.put(String.valueOf(2), String.valueOf(0));
								hmHashMap행.put(String.valueOf(3), String.valueOf(0));
								hmHashMap행.put(String.valueOf(4), String.valueOf(0));
								hmHashMap행.put(String.valueOf(5), String.valueOf(0));
								hmHashMap행.put(String.valueOf(6), String.valueOf(0));
								hmHashMap행.put(String.valueOf(7), String.valueOf(0));
								
								hmHashMap행.put(String.valueOf(i행X), "1");
							}
							else {								
								int iCnt = Integer.parseInt((String)hmHashMap행.get(String.valueOf(i행X)));
								hmHashMap행.put(String.valueOf(i행X), String.valueOf(++iCnt));
							}
							
//							iSuCnt = i선택숫자;
//							foundout = true;
//							break;
						}
						
//						if (foundout == true ) {
//							break;
//						}
											
					}
					
					
					
				}
				
			} // end of for (int I숫자위치=3; I숫자위치 <= 8; I숫자위치++)
			
			str회차 = dm과거1등번호[1];
			System.out.println(str회차 + "|" +hmHashMap행 );
			
		}
		
		
System.out.println("------------------------------------끝-----------------------------------------");
		
		
		
	}
	

	public void 과거1등분석_열분석() {
		
		System.out.println("------------------------- 과거1등분석_열분석 -------------------------");
		
		String[][] dm과거1등행렬 = new String[8][8]; // 8 ,8인 이유는 0부터 안하기 위해..
		
		int iSuCnt = 0;

		boolean foundout = false;
		HashMap hmHashMap열 = null;
		
		String str회차 = "";
		for (int I회차=0; I회차 < arrOne.size(); I회차++ ) {
			
			String[] dm과거1등번호 = (String[])arrOne.get(I회차);
			
			
			hmHashMap열 = new HashMap();
			for (int I숫자위치=3; I숫자위치 <= 8; I숫자위치++) {
				
				String str선택숫자 = dm과거1등번호[I숫자위치];
				int i선택숫자 = Integer.parseInt(str선택숫자);
				
//				System.out.println("i선택숫자=" + i선택숫자);
				
				foundout = false;
				iSuCnt = 0;
				for (int i행X=1; i행X < dm과거1등행렬.length; i행X++) {
					
					for (int i열Y=1; i열Y < dm과거1등행렬[i행X].length; i열Y++) {	
						
						iSuCnt++;	
						
						if (i선택숫자 == iSuCnt) {
							dm과거1등행렬[i행X][i열Y] = "*";
							
//							System.out.println("i행X=" + i행X);
							if ( hmHashMap열.containsKey(String.valueOf(i열Y)) == false ) {
								
								hmHashMap열.put(String.valueOf(1), String.valueOf(0));
								hmHashMap열.put(String.valueOf(2), String.valueOf(0));
								hmHashMap열.put(String.valueOf(3), String.valueOf(0));
								hmHashMap열.put(String.valueOf(4), String.valueOf(0));
								hmHashMap열.put(String.valueOf(5), String.valueOf(0));
								hmHashMap열.put(String.valueOf(6), String.valueOf(0));
								hmHashMap열.put(String.valueOf(7), String.valueOf(0));
								
								hmHashMap열.put(String.valueOf(i열Y), "1");
							}
							else {								
								int iCnt = Integer.parseInt((String)hmHashMap열.get(String.valueOf(i열Y)));
								hmHashMap열.put(String.valueOf(i열Y), String.valueOf(++iCnt));
							}
							
//							iSuCnt = i선택숫자;
//							foundout = true;
//							break;
						}
						
//						if (foundout == true ) {
//							break;
//						}
											
					}
					
					
					
				}
				
			} // end of for (int I숫자위치=3; I숫자위치 <= 8; I숫자위치++)
			
			str회차 = dm과거1등번호[1];
			System.out.println(str회차 + "|" +hmHashMap열 );
			
		}
		
		System.out.println("------------------------------------- 끝 ------------------------------------");
				
	}
	
	

	
	public boolean 체크_행분석(String[] dmRotto) {
		
		boolean bRtnValue = false;
		
		String[][] dm과거1등행렬 = new String[8][8]; // 8 ,8인 이유는 0부터 안하기 위해..
		
		int iSuCnt = 0;

		HashMap hmHashMap행 = null;
		
			hmHashMap행 = new HashMap();
			for (int I숫자위치=0; I숫자위치 < dmRotto.length; I숫자위치++) {
				
				String str선택숫자 = dmRotto[I숫자위치];
				int i선택숫자 = Integer.parseInt(str선택숫자);
				
//				System.out.println("i선택숫자=" + i선택숫자);
				
				iSuCnt = 0;
				for (int i행X=1; i행X < dm과거1등행렬.length; i행X++) {
					
					for (int i열Y=1; i열Y < dm과거1등행렬[i행X].length; i열Y++) {	
						
						iSuCnt++;	
						
						if (i선택숫자 == iSuCnt) {
							dm과거1등행렬[i행X][i열Y] = "*";
							
//							System.out.println("i행X=" + i행X);
							if ( hmHashMap행.containsKey(String.valueOf(i행X)) == false ) {
								
								hmHashMap행.put(String.valueOf(1), String.valueOf(0));
								hmHashMap행.put(String.valueOf(2), String.valueOf(0));
								hmHashMap행.put(String.valueOf(3), String.valueOf(0));
								hmHashMap행.put(String.valueOf(4), String.valueOf(0));
								hmHashMap행.put(String.valueOf(5), String.valueOf(0));
								hmHashMap행.put(String.valueOf(6), String.valueOf(0));
								hmHashMap행.put(String.valueOf(7), String.valueOf(0));
								
								hmHashMap행.put(String.valueOf(i행X), "1");
							}
							else {								
								int iCnt = Integer.parseInt((String)hmHashMap행.get(String.valueOf(i행X)));
								hmHashMap행.put(String.valueOf(i행X), String.valueOf(++iCnt));
							}
							
						}
						
											
					}
					
				}
				
			} // end of for (int I숫자위치=3; I숫자위치 <= 8; I숫자위치++)
			
//			System.out.println(str회차 + "|" +hmHashMap열 );
			
			int i1개총2or4개 = 0;
			int i2개총2or1개 = 0;
			
			Set keys = hmHashMap행.keySet();;
			Iterator oIterator = keys.iterator();
			while(oIterator.hasNext()) {
				String str행 = (String)oIterator.next();
				String value = (String)hmHashMap행.get(str행);
				
				if ( Integer.parseInt(value) == 2 ) {
					i2개총2or1개++;
				}
				
				if ( Integer.parseInt(value) == 1 ) { 
					i1개총2or4개++;
				}
				
				
			}
			
			if ( i1개총2or4개 == 3 && i2개총2or1개 == 0 ) {
				bRtnValue = false;
			}
			else if ( i1개총2or4개 == 2 && i2개총2or1개 == 2 ) {
				bRtnValue = true;
			}
			else if ( i1개총2or4개 == 4 && i2개총2or1개 == 1 ) {
				bRtnValue = true;
			}
			else if ( i1개총2or4개 != 2 || i1개총2or4개 != 4  || i1개총2or4개 != 3 ) {
				bRtnValue = true;
			}
			
			
			return bRtnValue;
			
		}
				
	
	
	
	
	
	
	
	public boolean 체크_열분석(String[] dmRotto) {
		
		boolean bRtnValue = false;
		
		String[][] dm과거1등행렬 = new String[8][8]; // 8 ,8인 이유는 0부터 안하기 위해..
		
		int iSuCnt = 0;

		HashMap hmHashMap열 = null;
		
			hmHashMap열 = new HashMap();
			for (int I숫자위치=0; I숫자위치 < dmRotto.length; I숫자위치++) {
				
				String str선택숫자 = dmRotto[I숫자위치];
				int i선택숫자 = Integer.parseInt(str선택숫자);
				
//				System.out.println("i선택숫자=" + i선택숫자);
				
				iSuCnt = 0;
				for (int i행X=1; i행X < dm과거1등행렬.length; i행X++) {
					
					for (int i열Y=1; i열Y < dm과거1등행렬[i행X].length; i열Y++) {	
						
						iSuCnt++;	
						
						if (i선택숫자 == iSuCnt) {
							dm과거1등행렬[i행X][i열Y] = "*";
							
//							System.out.println("i행X=" + i행X);
							if ( hmHashMap열.containsKey(String.valueOf(i열Y)) == false ) {
								
								hmHashMap열.put(String.valueOf(1), String.valueOf(0));
								hmHashMap열.put(String.valueOf(2), String.valueOf(0));
								hmHashMap열.put(String.valueOf(3), String.valueOf(0));
								hmHashMap열.put(String.valueOf(4), String.valueOf(0));
								hmHashMap열.put(String.valueOf(5), String.valueOf(0));
								hmHashMap열.put(String.valueOf(6), String.valueOf(0));
								hmHashMap열.put(String.valueOf(7), String.valueOf(0));
								
								hmHashMap열.put(String.valueOf(i열Y), "1");
							}
							else {								
								int iCnt = Integer.parseInt((String)hmHashMap열.get(String.valueOf(i열Y)));
								hmHashMap열.put(String.valueOf(i열Y), String.valueOf(++iCnt));
							}
							
						}
						
											
					}
					
				}
				
			} // end of for (int I숫자위치=3; I숫자위치 <= 8; I숫자위치++)
			
//			System.out.println(str회차 + "|" +hmHashMap열 );
			
			int i1개총2or4개 = 0;
			int i2개총2or1개 = 0;
			
			Set keys = hmHashMap열.keySet();;
			Iterator oIterator = keys.iterator();
			while(oIterator.hasNext()) {
				String 열 = (String)oIterator.next();
				String value = (String)hmHashMap열.get(열);
				
				if ( Integer.parseInt(value) == 2 ) {
					i2개총2or1개++;
				}
				
				if ( Integer.parseInt(value) == 1 ) { 
					i1개총2or4개++;
				}
				
				
			}
			
			
			if ( i1개총2or4개 == 2 && i2개총2or1개 == 2 ) {
				bRtnValue = true;
			}
			else if ( i1개총2or4개 == 4 && i2개총2or1개 == 1 ) {
				bRtnValue = true;
			}
			else if ( i1개총2or4개 != 2 || i1개총2or4개 != 4 ) {
				bRtnValue = true;
			}
			
			
			return bRtnValue;
			
		}
				
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void 과거1등분석_전회차숫자가지수포함() {
		
		String[] dm과거1등번호_전회차_분해 = null;
		String str과거1등번호_전회차_분해 = "";
		String[] dm과거1등번호_후회차 = null;
		ArrayList arr분해숫자셋 = new ArrayList();
		int iCnt = 0;
		for (int inx=0; inx < arrOne.size()-1 ; inx++ ) {
			dm과거1등번호_전회차_분해 = (String[])arrOne.get(inx);
			
			String strTmp = "";
			str과거1등번호_전회차_분해 = "";
			for (int idx=3; idx <= 9; idx++) {
				
				strTmp = dm과거1등번호_전회차_분해[idx].substring(0, 1);				
				arr분해숫자셋.add(strTmp);
				str과거1등번호_전회차_분해 += "§";
				str과거1등번호_전회차_분해 += strTmp;
				strTmp = dm과거1등번호_전회차_분해[idx].substring(1);		
				arr분해숫자셋.add(strTmp);
				str과거1등번호_전회차_분해 += "§";
				str과거1등번호_전회차_분해 += strTmp;
			}
			
			dm과거1등번호_후회차 = (String[])arrOne.get(inx+1);
			strTmp = "";
			iCnt=0;
			for (int idx=3; idx <= 9; idx++) {
				strTmp = dm과거1등번호_후회차[idx].substring(0, 1);			
				if ( str과거1등번호_전회차_분해.indexOf(strTmp) > -1 ) {
					iCnt++;
				}
				
				strTmp = dm과거1등번호_후회차[idx].substring(1);			
				if ( str과거1등번호_전회차_분해.indexOf(strTmp) > -1 ) {
					iCnt++;
				}
			}
			
			
			System.out.println(dm과거1등번호_후회차[1] + " | " + iCnt);
			
		}
		
	}
	
	
	public void 과거1등분석_일자포함분석() {
		
		System.out.println("------------------------- 과거1등분석_일자포함분석 -------------------------");
		
		
		String[] dm과거1등번호_회차별 = null;
		String str과거1등번호_숫자_분해 = "";
		ArrayList arr분해숫자셋 = new ArrayList();
		String str일자 = "";
		int iCnt = 0;
		for (int inx=0; inx < arrOne.size()-1 ; inx++ ) {
			dm과거1등번호_회차별 = (String[])arrOne.get(inx);
			
			String strTmp = "";
			str과거1등번호_숫자_분해 = "";
			for (int idx=3; idx <= 9; idx++) {
				
				strTmp = dm과거1등번호_회차별[idx].substring(0, 1);				
				arr분해숫자셋.add(strTmp);
				str과거1등번호_숫자_분해 += "§";
				str과거1등번호_숫자_분해 += strTmp;
				strTmp = dm과거1등번호_회차별[idx].substring(1);		
				arr분해숫자셋.add(strTmp);
				str과거1등번호_숫자_분해 += "§";
				str과거1등번호_숫자_분해 += strTmp;
			}
			
			str일자 = dm과거1등번호_회차별[2].split("\\.")[2];
			strTmp = "";
			iCnt=0;
			strTmp = str일자.substring(0, 1);			
			if ( str과거1등번호_숫자_분해.indexOf(strTmp) > -1 ) {
				iCnt++;
			}
			
			strTmp = str일자.substring(1);			
			if ( str과거1등번호_숫자_분해.indexOf(strTmp) > -1 ) {
				iCnt++;
			}
			
//			if ( iCnt <= 0 ) {
//				System.out.println(dm과거1등번호_회차별[2] + " | " + iCnt);
//			}
			
				System.out.println(dm과거1등번호_회차별[2] + " | " 
				+ dm과거1등번호_회차별[3] + " | " 
				+ dm과거1등번호_회차별[4] + " | "
				+ dm과거1등번호_회차별[5] + " | "
				+ dm과거1등번호_회차별[6] + " | "
				+ dm과거1등번호_회차별[7] + " | "
				+ dm과거1등번호_회차별[8] + " | "
				+ dm과거1등번호_회차별[9] + " | " + iCnt);
			
		}
		
		
		System.out.println("------------------------- END 과거1등분석_일자포함분석 -------------------------");
		
	}
	
	
	public boolean 체크_일자포함4검증(String[] dmRotto ,String str추첨일) {
		
		if ( str추첨일 == null || str추첨일.length() <= 0 ) {
			return true; // skip
		}
		
		boolean rValue = true;
		
		String str과거1등번호_숫자_분해 = "";
		String str일자 = "";
		int iCnt = 0;
		
		String strTmp = "";
		str과거1등번호_숫자_분해 = "";
		for (int idx=3; idx < dmRotto.length; idx++) {
			
			strTmp = dmRotto[idx].substring(0, 1);				
			str과거1등번호_숫자_분해 += "§";
			str과거1등번호_숫자_분해 += strTmp;
			strTmp = dmRotto[idx].substring(1);		
			str과거1등번호_숫자_분해 += "§";
			str과거1등번호_숫자_분해 += strTmp;
		}
		
		str일자 = str추첨일;
		strTmp = "";
		iCnt=0;
		strTmp = str일자.substring(0, 1);			
		if ( str과거1등번호_숫자_분해.indexOf(strTmp) > -1 ) {
			iCnt++;
		}
		
		strTmp = str일자.substring(1);			
		if ( str과거1등번호_숫자_분해.indexOf(strTmp) > -1 ) {
			iCnt++;
		}
		
		if ( 1 <= iCnt && iCnt <= 2 ) {
			rValue = true;
		}
		else {
			rValue = false;
		}
			
		return rValue;
	}
	
	
	public boolean 체크_일자포함(String[] dmRotto) {
		
		if ( str추첨일 == null || str추첨일.length() <= 0 ) {
			return true; // skip
		}
		
		boolean rValue = true;
		
		String str과거1등번호_숫자_분해 = "";
		String str일자 = "";
		int iCnt = 0;
		
		String strTmp = "";
		str과거1등번호_숫자_분해 = "";
		for (int idx=3; idx < dmRotto.length; idx++) {
			
			strTmp = dmRotto[idx].substring(0, 1);				
			str과거1등번호_숫자_분해 += "§";
			str과거1등번호_숫자_분해 += strTmp;
			strTmp = dmRotto[idx].substring(1);		
			str과거1등번호_숫자_분해 += "§";
			str과거1등번호_숫자_분해 += strTmp;
		}
		
		str일자 = str추첨일;
		strTmp = "";
		iCnt=0;
		strTmp = str일자.substring(0, 1);			
		if ( str과거1등번호_숫자_분해.indexOf(strTmp) > -1 ) {
			iCnt++;
		}
		
		strTmp = str일자.substring(1);			
		if ( str과거1등번호_숫자_분해.indexOf(strTmp) > -1 ) {
			iCnt++;
		}
		
		if ( 1 <= iCnt && iCnt <= 2 ) {
			rValue = true;
		}
		else {
			rValue = false;
		}
			
		return rValue;
	}
	
	
	
	
	public boolean 체크_전회차숫자가지수포함4검증(String[] dmRotto ,String[] dm과거1등번호_전회차_분해) {
		
		boolean bRtnValue = true;
		
		String str과거1등번호_전회차_분해 = "";
		String[] dm과거1등번호_후회차 = null;
		ArrayList arr분해숫자셋 = new ArrayList();
		int iCnt = 0;
		
		String strTmp = "";
		str과거1등번호_전회차_분해 = "";
		for (int idx=3; idx <= 9; idx++) {
			
			strTmp = dm과거1등번호_전회차_분해[idx].substring(0, 1);				
			arr분해숫자셋.add(strTmp);
			str과거1등번호_전회차_분해 += "§";
			str과거1등번호_전회차_분해 += strTmp;
			strTmp = dm과거1등번호_전회차_분해[idx].substring(1);		
			arr분해숫자셋.add(strTmp);
			str과거1등번호_전회차_분해 += "§";
			str과거1등번호_전회차_분해 += strTmp;
		}
		
		dm과거1등번호_후회차 = dmRotto;
		strTmp = "";
		iCnt=0;
		for (int idx=0; idx < dmRotto.length; idx++) {
			strTmp = dm과거1등번호_후회차[idx].substring(0, 1);			
			if ( str과거1등번호_전회차_분해.indexOf(strTmp) > -1 ) {
				iCnt++;
			}
			
			strTmp = dm과거1등번호_후회차[idx].substring(1);			
			if ( str과거1등번호_전회차_분해.indexOf(strTmp) > -1 ) {
				iCnt++;
			}
		}
		
		
		if ( iCnt <= 9 ) {
			bRtnValue = false;
		}
		
		
		return bRtnValue;
		
	}
	
	
	
	
	
	public boolean 체크_전회차숫자가지수포함(String[] dmRotto) {
		
		boolean bRtnValue = true;
		
		String[] dm과거1등번호_전회차_분해 = null;
		String str과거1등번호_전회차_분해 = "";
		String[] dm과거1등번호_후회차 = null;
		ArrayList arr분해숫자셋 = new ArrayList();
		int iCnt = 0;
		dm과거1등번호_전회차_분해 = (String[])arrOne.get(arrOne.size()-1);
		
		String strTmp = "";
		str과거1등번호_전회차_분해 = "";
		for (int idx=3; idx <= 9; idx++) {
			
			strTmp = dm과거1등번호_전회차_분해[idx].substring(0, 1);				
			arr분해숫자셋.add(strTmp);
			str과거1등번호_전회차_분해 += "§";
			str과거1등번호_전회차_분해 += strTmp;
			strTmp = dm과거1등번호_전회차_분해[idx].substring(1);		
			arr분해숫자셋.add(strTmp);
			str과거1등번호_전회차_분해 += "§";
			str과거1등번호_전회차_분해 += strTmp;
		}
		
		dm과거1등번호_후회차 = dmRotto;
		strTmp = "";
		iCnt=0;
		for (int idx=0; idx < dmRotto.length; idx++) {
			strTmp = dm과거1등번호_후회차[idx].substring(0, 1);			
			if ( str과거1등번호_전회차_분해.indexOf(strTmp) > -1 ) {
				iCnt++;
			}
			
			strTmp = dm과거1등번호_후회차[idx].substring(1);			
			if ( str과거1등번호_전회차_분해.indexOf(strTmp) > -1 ) {
				iCnt++;
			}
		}
		
		
		if ( iCnt <= 9 ) {
			bRtnValue = false;
		}
		
		
		return bRtnValue;
		
	}
	
	
	
	
	
	
	
	public boolean 체크_과거1등번호4개이상똑같은경우(String[] dmRotto) {
		
		boolean bRtnValue = true;
		
		String str과거1등번호 = "";
		String str현재추출번호 = "";
		String strTmp과거1등 = "";
		String strTmp현재추출 = "";
		int i같은갯수 = 0;
		for (int inx=0; inx < arrOne.size(); inx++ ) {
			String[] dm과거1등번호 = (String[])arrOne.get(inx);
			i같은갯수 = 0;
			
			for (int idx=3; idx <= 8; idx++) {
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
	
	public boolean 체크_특정번호대에서4개이하나옴(String[] dmRotto) {
		
		boolean rValue = true;

		int i1번대 = 0;
		int i10번대 =0;
		int i20번대 = 0;
		int i30번대 = 0;
		int i40번대 = 0;
		
		int i정수 = 0;
		for (int inx = 0; inx < dmRotto.length; inx++) {
			i정수 = Integer.parseInt(dmRotto[inx]);
			if ( i정수 <= 10 ) {
				i1번대++;
			}
			else if ( i정수 <= 20 ) {
				i10번대++;
			}
			else if ( i정수 <= 30 ) {
				i20번대++;
			}
			else if ( i정수 <= 40 ) {
				i30번대++;
			}
			else if ( i정수 <= 50 ) {
				i40번대++;
			}
		}
		
		if (i1번대 >= 4 || i10번대 >= 4 || i20번대 >= 4 || i30번대 >= 5 || i40번대 >= 5) {
			rValue = false;
		}
		
		return rValue;
	}
	
	
	
	public boolean 체크_번호사이차(String[] dmRotto) {
		
		boolean rValue = true;

		int i차 = 0;
		for (int inx = 1; inx < dmRotto.length; inx++) {
			
			int i큰수 = Integer.parseInt(dmRotto[inx]);
			int i작은수 = Integer.parseInt(dmRotto[inx-1]);
			i차 = i큰수 - i작은수;
			
			if ( i차  >= 25 ) {
				rValue = false;
				break;
			}
		}
		
		return rValue;
		
	}
	
	
	
	
	
	
	
	public boolean 체크_회차(String[] dmRotto) {
		
		boolean rValue = false;
		
		List lstTmp = new ArrayList(); // 회차 수가 2개 이상 포함 되었다면..

		String sTmp = "";
		for (int inx = 0; inx < dmRotto.length; inx++) {
			sTmp = dmRotto[inx].substring(0, 1);
			if ( "0".equals(sTmp) == false && 추첨회차.indexOf(sTmp) > -1 ) {
				if ( lstTmp.contains(sTmp) == false) {
					lstTmp.add(sTmp);
				}
			}
			
			sTmp = dmRotto[inx].substring(1);
			if ( 추첨회차.indexOf(sTmp) > -1 ) {
				if ( lstTmp.contains(sTmp) == false) {
					lstTmp.add(sTmp);
				}
			}
			
			if (lstTmp.size() >= 1) {
				rValue = true;
				break;
			}
		}
		
		return rValue;
	}
	
	
	
	
	public boolean 체크_회차4검증(String[] dmRotto ,String 추첨회차) {
		
		boolean rValue = false;
		
		List lstTmp = new ArrayList(); // 회차 수가 2개 이상 포함 되었다면..

		String sTmp = "";
		for (int inx = 0; inx < dmRotto.length; inx++) {
			sTmp = dmRotto[inx].substring(0, 1);
			if ( "0".equals(sTmp) == false && 추첨회차.indexOf(sTmp) > -1 ) {
				if ( lstTmp.contains(sTmp) == false) {
					lstTmp.add(sTmp);
				}
			}
			
			sTmp = dmRotto[inx].substring(1);
			if ( 추첨회차.indexOf(sTmp) > -1 ) {
				if ( lstTmp.contains(sTmp) == false) {
					lstTmp.add(sTmp);
				}
			}
			
			if (lstTmp.size() >= 1) {
				rValue = true;
				break;
			}
		}
		
		return rValue;
	}
	
	
	
	
	
	public boolean 체크_전회차에번호가1개or2개포함4검증(String[] dmRotto ,String str전회차당첨번호) {
		
		boolean rValue = true;
		

		int i갯수 = 0;
		for (int inx = 0; inx < dmRotto.length; inx++) {
			if ( str전회차당첨번호.indexOf(dmRotto[inx]) > -1 ) {
				i갯수 ++;
			}
		}
		
		if ( 1 <= i갯수 && i갯수 <= 2 ) {
			rValue = true;
		}
		else {
			rValue = false;
		}
		

		return rValue;
	}
	
	
	
	
	
	
	
	
	
	public boolean 체크_전회차에번호가1개or2개포함(String[] dmRotto) {
		
		boolean rValue = true;
		
		if ( str전회차당첨번호.length() <= 0 ) {
			return rValue;
		}

		int i갯수 = 0;
		for (int inx = 0; inx < dmRotto.length; inx++) {
			if ( str전회차당첨번호.indexOf(dmRotto[inx]) > -1 ) {
				i갯수 ++;
			}
		}
		
		if ( 1 <= i갯수 && i갯수 <= 2 ) {
			rValue = true;
		}
		else {
			rValue = false;
		}
		

		return rValue;
	}
	
	
	
	// http://lottosystem1.tistory.com/entry/%EB%A1%9C%EB%98%90%EC%A1%B0%ED%95%A9%EC%8B%9C%EC%8A%A4%ED%85%9C-%EB%A1%9C%EB%98%90%EB%8B%B9%EC%B2%A8%EB%B2%88%ED%98%B8-%ED%95%A9%EA%B3%84-%EA%B7%B8%EB%9E%98%ED%94%84-%ED%86%B5%EA%B3%84-1553%ED%9A%8C
	public boolean 체크_전체합계(String[] dmRotto) {

		boolean rValue = true;

		int i합계 = 0;
		for (int inx = 0; inx < dmRotto.length; inx++) {
			i합계 += Integer.parseInt(dmRotto[inx]);
		}
		
		if ( 95 <= i합계 && i합계  <= 230 ) {
			rValue = true;
		}
		else {
			rValue = false;
		}

		return rValue;
	}
	
	public boolean 체크_홀수짝수가5개쏠림4검증(String[] dmRotto) {

		boolean rValue = true;
		int i짝수cnt = 0;
		int i홀수cnt = 0;

		for (int inx = 0; inx < dmRotto.length - 1 ; inx++) { // <------------ -1이 추가 됨.
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
	
	
	
	public boolean 체크_홀수짝수가5개쏠림(String[] dmRotto) {

		boolean rValue = true;
		int i짝수cnt = 0;
		int i홀수cnt = 0;

		for (int inx = 0; inx < dmRotto.length ; inx++) {
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
	
	
	
	public boolean 체크_포함수포함(String[] dmRotto) {

		boolean rValue = false;
		
		if ( str포함수.length() <= 0 ) {
			return true;
		}

		int i포함수Cnt = str포함수.split(",").length;
		int i포함수수수수숫Cnt = 0;
		for (int inx = 0; inx < dmRotto.length; inx++) {

			if (str포함수.indexOf(dmRotto[inx]) > -1) {
				i포함수수수수숫Cnt ++;
			}
		}
		
		if ( i포함수Cnt == i포함수수수수숫Cnt ) {
			rValue = true;
		}

		return rValue;
	}
	
	
	

	public boolean 체크_제외수포함(String[] dmRotto) {

		boolean rValue = true;
		
		if ( str제외수.length() <= 0 ) {
			return rValue;
		}

		for (int inx = 0; inx < dmRotto.length; inx++) {

			if (str제외수.indexOf(dmRotto[inx]) > -1) {
				rValue = false;
				break;
			}
		}

		return rValue;
	}

	public boolean 체크_뒷숫자가같음(String[] dmRotto) {

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
		else if ( 1 <= i같은수 && i같은수 <= 2 ) {
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
