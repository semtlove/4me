package common;

public class Combination {
	public static void main(String[] args) {
		int[] arr = new int[6];
		combination(arr, 0, 45, 6, 1);
		System.out.println("총가지수 = " + iCnt);
	}

	static long iCnt = 0;
	public static void combination(int[] arr, int index, int n, int r, int target) {
		
		if      (r == 0) {
			iCnt++;
			print(arr, index);
		}
		else if (target > n) return;
		else {
			arr[index] = target;
			combination(arr, index + 1, n, r - 1, target + 1);
			combination(arr, index, n, r, target + 1);
		}
	}//end combination()
	
	public static void print(int[] arr, int length) {
		
		String sValue = "";
		for (int i = 0; i < length; i++) {
			if ( sValue != null && sValue.length() > 0) {
				sValue += ",";
			}
			
			sValue += 한자리인경우앞에0을붙임(String.valueOf(arr[i]));
		}
		
		System.out.print(sValue);
		System.out.println("");
	}
	
	
	public static String 한자리인경우앞에0을붙임(String s숫자) {
		if (s숫자.length() <= 1) {
			s숫자 = "0" + s숫자;
		}

		return s숫자;
	}
	
}	
