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

public class 검증_전체확율_SUB {
	
	int i시도횟수 = 0;
	R2 oR2 = new R2();
	public  boolean chk로또(String[] lottoNumbers) throws Exception {

			Arrays.sort(lottoNumbers); // sort
			String rotto = Arrays.toString(lottoNumbers);
			rotto = rotto.replaceAll("\\[", "");
			rotto = rotto.replaceAll("\\]", "");
			String[] dmRotto = rotto.split(",");
			
			for (int inx = 0; inx < dmRotto.length; inx++) {
				dmRotto[inx] = dmRotto[inx].trim();
				dmRotto[inx] = 한자리인경우앞에0을붙임(dmRotto[inx]);
			}

			boolean ok = true;
			
			ok = oR2.체크_포함수포함(dmRotto);		// a
			if (ok == false) return false;
			ok = oR2.체크_제외수포함(dmRotto);		// a
			if (ok == false) {
				return false;
			}
			
			
			ok = oR2.체크_전회차에번호가1개or2개포함(dmRotto); // 66%
			if (ok == false) return false;
			ok = oR2.체크_뒷숫자가같음(dmRotto); // 79%
			if (ok == false) return false;
			ok = oR2.체크_추첨일자포함(dmRotto);		// 83%			
			if (ok == false) return false;
			ok = oR2.체크_홀수짝수가5개쏠림(dmRotto);	// 83% c
			if (ok == false) return false;
				
			
			ok = oR2.체크_전체합계(dmRotto);	// 95%
			if (ok == false) return false;
			ok = oR2.체크_번호사이차(dmRotto);		// 97%
			if (ok == false) return false;
			ok = oR2.체크_특정번호대에서4개이하나옴(dmRotto); // 90%
			if (ok == false) return false;
			ok = oR2.체크_회차(dmRotto);	// 96%
			if (ok == false) return false;
//			ok = oR2.체크_전회차숫자가지수포함(dmRotto); // 90%
//			if (ok == false) return false;	
			ok = oR2.체크_과거1등번호4개이상똑같은경우(dmRotto); // 100%
			if (ok == false) return false;
			ok = oR2.체크_열분석(dmRotto);	// 100%
			if (ok == false) return false;
			ok = oR2.체크_행분석(dmRotto);	// 97%
			if (ok == false) return false;
			
		
		return true;
	}
	
	


	public static String 한자리인경우앞에0을붙임(String s숫자) {
		if (s숫자.length() <= 1) {
			s숫자 = "0" + s숫자;
		}

		return s숫자;
	}

}
