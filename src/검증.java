import java.util.List;

public class 검증 {

	public static void main(String[] args) {
		
		R2 oR2 = new R2();
		List arrOne = oR2.과거1등번호메모리load();
		
		String str결과 = "";
		boolean b결과 = false;
		for (int inx=0; inx < arrOne.size(); inx++ ) {
			String[] dm과거1등번호 = (String[])arrOne.get(inx);
			str결과 = "";
			
			b결과 = oR2.뒷숫자가같은지체크(dm과거1등번호);
			if (b결과 == false) {
				str결과 = "뒷숫자가같은지체크";
			}
		}

	}

}
