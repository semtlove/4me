import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import common.Util;


public class Test {

	public static void main(String[] args) throws IOException {
		System.out.println(Util.getApplcatonPath(Test.class));
		System.out.println(new File(".").getCanonicalPath());
		System.out.println(System.getProperty("user.dir"));
		System.out.println(System.getProperty("user.home"));
		
		SimpleDateFormat df = new SimpleDateFormat("dd");
		Date date = new Date();
        String today = df.format(date);
        System.out.print(today);
	}

}
