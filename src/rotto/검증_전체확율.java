package rotto;

public class 검증_전체확율 {
	public static void main(String[] args) {
		
		System.out.println("--------------- 시작 ---------------");
		
		int[] arr = new int[6];
		combination(arr, 0, 45, 6, 1);
		System.out.println("총가지수 = " + iCnt);
	}

	static long iCnt = 0;
	public static void combination(int[] arr, int index, int n, int r, int target) {
		
		if (r == 0) {
			
			String[] dmRotto = print(arr, index);
			try {
				boolean bOkFail = ( new 검증_전체확율_SUB() ).chk로또(dmRotto);
				
				if (bOkFail == true) {
					iCnt++;
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (target > n) return;
		else {
			arr[index] = target;
			combination(arr, index + 1, n, r - 1, target + 1);
			combination(arr, index, n, r, target + 1);
		}
		
	}//end combination()
	
	public static String[] print(int[] arr, int length) {
		
		String[] dmRotto = new String[6];
		String sValue = "";
		for (int i = 0; i < length; i++) {
			if ( sValue != null && sValue.length() > 0) {
				sValue += ",";
			}
			
			sValue += R2.한자리인경우앞에0을붙임(String.valueOf(arr[i]));
			
			dmRotto[i] = String.valueOf(arr[i]);
		}
		
//		System.out.print(sValue);
//		System.out.println("");
		
		return dmRotto;
	}
	
}	
