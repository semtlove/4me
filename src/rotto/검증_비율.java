package rotto;
import java.util.List;

public class 검증_비율 {

	public static void main(String[] args) {
		
		R2 oR2 = new R2();
		List arrOne = oR2.과거1등번호메모리load();
		
		String str결과 = "";
		boolean b결과 = false;
		String[] dm과거1등번호보정 = null;
		int iok갯수 = 0;
		int i비율 = 0;
		String str로또번호 = "";
		String str전회차당첨번호 = "";
		for (int inx=arrOne.size()-1; inx >= 0; inx-- ) {
			
			dm과거1등번호보정 = new String[7];
			str로또번호 = "";
			
			String[] dm과거1등번호 = (String[])arrOne.get(inx);
			str결과 += "\n";
			str결과 += String.format("%5s", dm과거1등번호[1]); // 회차
			
			
			for (int idx=3; idx <= 9; idx++) {
				dm과거1등번호보정[idx-3] = dm과거1등번호[idx];
				str로또번호 += dm과거1등번호[idx];
				str로또번호 += ",";
			}
			
			str결과 += "|" + str로또번호;
			
			//------------------------------------------------------ 79%
			
			str결과 += "|뒷숫자가같은지체크";
			b결과 = oR2.체크_뒷숫자가같음(dm과거1등번호보정);
			if (b결과 == false) {
				str결과 += String.format("%5s" ,"|FL");
			}
			else {
				str결과 += String.format("%5s" ,"|OK");
//				iok갯수++;
			}
			
			//------------------------------------------------------ 83%
			
			String str추첨일 = dm과거1등번호[2].split("\\.")[2];
			
			str결과 += "|체크_일자포함";
			b결과 = oR2.체크_일자포함4검증(dm과거1등번호보정 ,str추첨일);
			if (b결과 == false) {
				str결과 += String.format("%5s" ,"|FL");
			}
			else {
				str결과 += String.format("%5s" ,"|OK");
//				iok갯수++;
			}			
			
			//------------------------------------------------------ 90% - 보정
			
			str결과 += "|체크_체크_특정번호대에서4개이하나옴";
			b결과 = oR2.체크_특정번호대에서4개이하나옴(dm과거1등번호보정);
			if (b결과 == false) {
				str결과 += String.format("%5s" ,"|FL");
			}
			else {
				str결과 += String.format("%5s" ,"|OK");
//				iok갯수++;
			}
			
			
			
			//------------------------------------------------------ 96%
			String str회차 = dm과거1등번호[1].split("\\.")[0];
			
			str결과 += "|체크_회차4검증";
			b결과 = oR2.체크_회차4검증(dm과거1등번호보정 ,str회차);
			if (b결과 == false) {
				str결과 += String.format("%5s" ,"|FL");
			}
			else {
				str결과 += String.format("%5s" ,"|OK");
//				iok갯수++;
			}
			
			
			//------------------------------------------------------ 100%
			
			str결과 += "|체크_열분석";
			b결과 = oR2.체크_열분석(dm과거1등번호보정);
			if (b결과 == false) {
				str결과 += String.format("%5s" ,"|FL");
			}
			else {
				str결과 += String.format("%5s" ,"|OK");
//				iok갯수++;
			}
			
			//------------------------------------------------------ 97%
			
			
			str결과 += "|체크_행분석";
			b결과 = oR2.체크_행분석(dm과거1등번호보정);
			if (b결과 == false) {
				str결과 += String.format("%5s" ,"|FL");
			}
			else {
				str결과 += String.format("%5s" ,"|OK");
//				iok갯수++;
			}
			
			//------------------------------------------------------ 83% (보너스번호포함하면 59%임)
			
			
			str결과 += "|체크_홀수짝수가5개쏠림";
			b결과 = oR2.체크_홀수짝수가5개쏠림4검증(dm과거1등번호보정);
			if (b결과 == false) {
				str결과 += String.format("%5s" ,"|FL");
			}
			else {
				str결과 += String.format("%5s" ,"|OK");
//				iok갯수++;
			}
			
			//------------------------------------------------------ 0% (정상임.)
			
			
			str결과 += "|체크_과거1등번호4개이상똑같은경우";
			b결과 = oR2.체크_과거1등번호4개이상똑같은경우(dm과거1등번호보정);
			if (b결과 == false) {
				str결과 += String.format("%5s" ,"|FL");
			}
			else {
				str결과 += String.format("%5s" ,"|OK");
//				iok갯수++;
			}
			
			//------------------------------------------------------ 97% 
			
			str결과 += "|체크_번호사이차";
			b결과 = oR2.체크_번호사이차(dm과거1등번호보정);
			if (b결과 == false) {
				str결과 += String.format("%5s" ,"|FL");
			}
			else {
				str결과 += String.format("%5s" ,"|OK");
//				iok갯수++;
			}
			
			
			
			//------------------------------------------------------ 95% - 보정
						
						str결과 += "|체크_전체합계";
						b결과 = oR2.체크_전체합계(dm과거1등번호보정);
						if (b결과 == false) {
							str결과 += String.format("%5s" ,"|FL");
						}
						else {
							str결과 += String.format("%5s" ,"|OK");
							iok갯수++;
						}
			
			
			
			//------------------------------------------------------ 66%
			
			
			if ( inx > 0 ) {
				
				String[] dm과거1등번호_전회차_분해 = (String[])arrOne.get(inx-1);
				
				str전회차당첨번호 = "";
				
				for (int idx=3; idx <= 9; idx++) {
					dm과거1등번호_전회차_분해[idx] = R2.한자리인경우앞에0을붙임(dm과거1등번호_전회차_분해[idx]);
					str전회차당첨번호 += dm과거1등번호_전회차_분해[idx];
					str전회차당첨번호 += ",";
				}
				
				
				str결과 += "|체크_전회차에번호가1개or2개포함";
				b결과 = oR2.체크_전회차에번호가1개or2개포함4검증(dm과거1등번호보정 ,str전회차당첨번호);
				if (b결과 == false) {
					str결과 += String.format("%5s" ,"|FL");
				}
				else {
					str결과 += String.format("%5s" ,"|OK");
//					iok갯수++;
				}
			
			}	
			
			
			
			//------------------------------------------------------ 90%
			
			
			if ( inx > 0 ) {
				
				String[] dm과거1등번호_전회차_분해 = (String[])arrOne.get(inx-1);
				
				
				str결과 += "|체크_전회차숫자가지수포함4검증";
				b결과 = oR2.체크_전회차숫자가지수포함4검증(dm과거1등번호보정 ,dm과거1등번호_전회차_분해);
				if (b결과 == false) {
					str결과 += String.format("%5s" ,"|FL");
				}
				else {
					str결과 += String.format("%5s" ,"|OK");
//					iok갯수++;
				}
			
			}			
			
		}
		
		System.out.println(str결과);
		float f = (float)iok갯수/(float)arrOne.size();
		System.out.println("비율=" + (int)(f*100));

	}

}
