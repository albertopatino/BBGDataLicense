/*
00000000010000000002000000000300000000040000000005000000000600000000070000000008
*/

package cellphi;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import sun.misc.UUDecoder;
import sun.misc.UUEncoder;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


public class BBGDES {

    static int[] odd_parity = {
            1, 1, 2, 2, 4, 4, 7, 7, 8, 8, 11, 11, 13, 13, 14, 14,
            16, 16, 19, 19, 21, 21, 22, 22, 25, 25, 26, 26, 28, 28, 31, 31,
            32, 32, 35, 35, 37, 37, 38, 38, 41, 41, 42, 42, 44, 44, 47, 47,
            49, 49, 50, 50, 52, 52, 55, 55, 56, 56, 59, 59, 61, 61, 62, 62,
            64, 64, 67, 67, 69, 69, 70, 70, 73, 73, 74, 74, 76, 76, 79, 79,
            81, 81, 82, 82, 84, 84, 87, 87, 88, 88, 91, 91, 93, 93, 94, 94,
            97, 97, 98, 98,100,100,103,103,104,104,107,107,109,109,110,110,
            112,112,115,115,117,117,118,118,121,121,122,122,124,124,127,127,
            128,128,131,131,133,133,134,134,137,137,138,138,140,140,143,143,
            145,145,146,146,148,148,151,151,152,152,155,155,157,157,158,158,
            161,161,162,162,164,164,167,167,168,168,171,171,173,173,174,174,
            176,176,179,179,181,181,182,182,185,185,186,186,188,188,191,191,
            193,193,194,194,196,196,199,199,200,200,203,203,205,205,206,206,
            208,208,211,211,213,213,214,214,217,217,218,218,220,220,223,223,
            224,224,227,227,229,229,230,230,233,233,234,234,236,236,239,239,
            241,241,242,242,244,244,247,247,248,248,251,251,253,253,254,254 };

        final int BUFFER_SIZE = 512;

        static final byte[] zeroIV = { 0, 0, 0, 0, 0, 0, 0, 0 };

        private SecretKey key_;

        public BBGDES(String key) throws Exception
        {
            key_ = makeKey_(key);
        }

        private SecretKey makeKey_(String key) throws Exception
        {
            byte[] ascii = new byte[8];
            byte[] result = new byte[8];

            for(int i=0; i<8; i++) {
                ascii[i] = (byte)(key.charAt(i));
                result[i] = (byte)(ascii[i] << 1);
            }

            setOddParity(result);

            Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");
            IvParameterSpec ips = new IvParameterSpec(result);
            SecretKey secretKey = SecretKeyFactory.getInstance("DES").generateSecret(
                                  new DESKeySpec(result));

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ips);
            result = cipher.doFinal(ascii);

            setOddParity(result);

            return SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(result));
        }
        public static void setOddParity(byte[] key)
        {
            for(int i = 0; i < 8; i++)
            {
                key[i] = (byte)odd_parity[ key[i] & 0xFF];
            }
        }

        public void decrypt(InputStream is, OutputStream os) throws Exception
        {
            UUDecoder uudec = new UUDecoder();
            byte[] decoded = uudec.decodeBuffer(is);

            Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");
            IvParameterSpec ips = new IvParameterSpec(zeroIV);
            cipher.init(Cipher.DECRYPT_MODE, key_, ips);
            ByteArrayInputStream bais = new ByteArrayInputStream(decoded);

            /*byte[] decrypted = cipher.doFinal(decoded);*/
            byte[] inputBuffer = new byte[8];
            int r=bais.read(inputBuffer);
            while ( r != -1 ) {
                byte[] outData = cipher.update(inputBuffer, 0, r);
                os.write( outData, 0, r );
                r=bais.read(inputBuffer);
            }
            byte[] fin=cipher.doFinal();
            os.write( fin );

            os.close();
        }

        public void encrypt(InputStream is, OutputStream os, String bufferName) throws Exception
        {
            UUEncoder uuenc = new UUEncoder(bufferName,600);

            Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");
            IvParameterSpec ips = new IvParameterSpec(zeroIV);
            cipher.init(Cipher.ENCRYPT_MODE, key_, ips);

            InputStream bais=is;

            /*byte[] decrypted = cipher.doFinal(decoded);*/
            byte[] inputBuffer = new byte[BUFFER_SIZE];

            int r=bais.read(inputBuffer);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ( r != -1 ) {
                int mod = r % 8;
                if ( mod != 0 ) {
                    java.util.Random rand = new java.util.Random(new java.util.Date().getTime());
                    for(int i = 7-mod; i>0; i--)
                        inputBuffer[r++]=(byte)(rand.nextInt() & 0xff);
                    inputBuffer[r++] = (byte) mod;
                }
                byte[] outData = cipher.update(inputBuffer, 0, r);
                //uuenc.encodeBuffer(outData, os);
                baos.write(outData);
                r=bais.read(inputBuffer);
            }
            byte[] outData=cipher.doFinal();
            baos.write(outData);
            uuenc.encodeBuffer(new ByteArrayInputStream(baos.toByteArray()), os);
            os.close();
        }
}
