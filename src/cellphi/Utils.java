package cellphi;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Utils {
    
    final int BUFFER_SIZE=512;
    
    public void gunzip(InputStream is, OutputStream os) throws Exception {
    
        GZIPInputStream zipStream = new GZIPInputStream(is, BUFFER_SIZE);
        //BufferedInputStream biStream = new BufferedInputStream(zip);

        byte[] buffer = new byte[BUFFER_SIZE];
        int n;

        while ( (n = zipStream.read(buffer)) != -1) {
            os.write(buffer,0,n);
        }
        zipStream.close();
        os.close();
    }   

    public void gzip(InputStream is, OutputStream os) throws Exception {
    
        GZIPOutputStream zipStream = new GZIPOutputStream(os);
        //BufferedInputStream biStream = new BufferedInputStream(zip);

        byte[] buffer = new byte[BUFFER_SIZE];
        int n;

        while ( (n = is.read(buffer)) != -1) {
            zipStream.write(buffer,0,n);
        }
        is.close();
        zipStream.close();
    }   
}
