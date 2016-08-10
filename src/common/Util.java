package common;

import java.io.File;
import java.net.URISyntaxException;
import java.security.CodeSource;

public class Util {
	
	/*****************************************************************************
     * return application path
     * @return
     *****************************************************************************/
    public static String getApplcatonPath(Class oClass){
        CodeSource codeSource = oClass.getProtectionDomain().getCodeSource();
        File rootPath = null;
        try {
            rootPath = new File(codeSource.getLocation().toURI().getPath());
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }           
        return rootPath.getParentFile().getPath();
    }//end of getApplcatonPath()
	

}
