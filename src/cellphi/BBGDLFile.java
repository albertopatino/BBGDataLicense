package cellphi;
import java.io.ByteArrayInputStream;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class BBGDLFile {
	
	private String BBGRequestFile;

	private String DESKey;

	public BBGDLFile(String file, String key) {
		this.BBGRequestFile = file;
		this.DESKey=key;
	}
	
	public void setDesKey(String key) {
		DESKey = key;
	}

	public String getDesKey() {
		return DESKey;
	}

	public void processRequest() {

		BBGFileRequest bbgFile=new BBGFileRequest(BBGRequestFile);

		try {
			bbgFile.processBBGRequestFile();

			BBGDES bbg = new BBGDES(getDesKey());

			Utils utils = new Utils();

			FileInputStream input = new FileInputStream(bbgFile.getOutputName());
			ByteArrayOutputStream temp = new ByteArrayOutputStream();
            utils.gzip( input, temp);
			
			ByteArrayInputStream temp2 = new ByteArrayInputStream(temp.toByteArray());
			String outFileName=bbgFile.getOutputName()+".gz";
			FileOutputStream output = new FileOutputStream(outFileName);
			bbg.encrypt(temp2, output, outFileName);

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
}