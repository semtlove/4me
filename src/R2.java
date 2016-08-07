import java.util.Arrays;
import java.util.Random;

public class R2 {

//	String str제외수 = "";
	String str제외수 = "04,05,19,07,12,42,6,32,45";
	int i로또게임갯수 = 20;

	public static void main(String[] args) {

		R2 oR2 = new R2();
		oR2.start();
	}

	public void start() {

		int[] lottoNumbers = new int[6];
		Random rnd = new Random();

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

			boolean ok = true;
			ok = 뒷숫자가같은지체크(dmRotto);
			if (ok == false) continue;
			ok = 제외수포함체크(dmRotto);
			if (ok == false) continue;
			
			System.out.println(Arrays.toString(dmRotto));
			
			--i로또게임갯수;

		}
	}

	public boolean 제외수포함체크(String[] dmRotto) {

		boolean rValue = true;

		for (int inx = 0; inx < dmRotto.length; inx++) {
			dmRotto[inx] = 한자리인경우앞에0을붙임(dmRotto[inx]);

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
			dmRotto[inx] = 한자리인경우앞에0을붙임(dmRotto[inx]);
			String 한자리 = dmRotto[inx].substring(1, 2);

			if (str뒷자리모음.indexOf(한자리) > -1) {

				++i같은수;
			}

			str뒷자리모음 += 한자리;
			str뒷자리모음 += "|";

		}

		boolean rValue = false;

		if (i같은수 > 0) {
			rValue = true;
		}

		return rValue;
	}

	public String 한자리인경우앞에0을붙임(String s숫자) {
		if (s숫자.length() <= 1) {
			s숫자 = "0" + s숫자;
		}

		return s숫자;
	}

}
