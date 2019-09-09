package cellphi;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UtilsTest {
	private Utils utils;
	
	private final String txtFile="test.txt";

	private final String gzFile="test.txt.gz";

	private final String outFile="tmp.out";
	
	@Before
	public void setUp() throws Exception {
		utils = new Utils();	
	}

	@After
	public void tearDown() throws Exception {
		utils=null;
	}

	@Test
	public final void testGunzip() throws FileNotFoundException {
		
		InputStream is = new FileInputStream(gzFile);
		
		OutputStream os = new FileOutputStream(outFile);
		
		try {
			utils.gunzip(is, os);
			
			assertTrue("Hi", compareFiles(txtFile, outFile) == true);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("gunzip Exception");
		}
	}

	@Test
	public final void testGzip() throws FileNotFoundException {
		InputStream is = new FileInputStream(txtFile);
		
		OutputStream os = new FileOutputStream(outFile);
		
		try {
			utils.gzip(is, os);
			
			assertTrue("Hi", compareFiles(gzFile, outFile) == true);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("gunzip Exception");
		}
	}

	private final boolean compareFiles(String f1,String f2){
		return true;
	}
}
