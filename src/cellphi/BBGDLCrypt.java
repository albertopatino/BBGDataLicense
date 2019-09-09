package cellphi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.concurrent.TimeUnit;

public class BBGDLCrypt {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if ( args.length != 2 ) {
			System.out.println("Usage: crypt file.txt file.out.gz");
			System.exit(0);
		}
		
		try {  
            String inFile=args[0];
            String outFile = args[1];
        	 
            String desKey=new String("CsYDnVFS");
             
            BBGDES bbg = new BBGDES(desKey);

        	Utils utils = new Utils();
        	
        	FileInputStream input = new FileInputStream(inFile);
        	ByteArrayOutputStream temp = new ByteArrayOutputStream();
            utils.gzip( input, temp);
        			
        	ByteArrayInputStream temp2 = new ByteArrayInputStream(temp.toByteArray());
        	
        	FileOutputStream output = new FileOutputStream(outFile);
        	bbg.encrypt(temp2, output, outFile);

        } catch (FileNotFoundException e1) {
        			// TODO Auto-generated catch block
        			e1.printStackTrace();
        } catch (Exception ex) {
        			System.out.println(ex);
        }
 	}
}
