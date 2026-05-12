package com.mxic.oiplus.util;

import java.io.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;


public class RCEncrypt {

	 byte[] salt = { (byte)0xd4, (byte)0xa3, (byte)0xff, (byte)0x9e,
						  (byte)0x12, (byte)0xc7, (byte)0xd0, (byte)0x84 };

	 Cipher c;
	 PBEParameterSpec paramSpec;
	 SecretKey passwordKey;

	 public RCEncrypt(String password) {
	 	  Security.addProvider(new com.sun.crypto.provider.SunJCE());
		  try {
				paramSpec = new PBEParameterSpec( salt, 20 );
				PBEKeySpec keySpec = new PBEKeySpec( password.toCharArray() );
				SecretKeyFactory kf = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
				passwordKey = kf.generateSecret( keySpec );
				c = Cipher.getInstance("PBEWithMD5AndDES");
		  } catch (Exception e) {
			    TDSLogger.println("RCEncrypt() caught exception '" + e +
										 "' with message '" + e.getMessage() + "'.");
				e.printStackTrace();
		  }
	 }

	 public void encrypt(DataInputStream dis, DataOutputStream dos)
		  throws Exception {

		  c.init(Cipher.ENCRYPT_MODE, passwordKey, paramSpec);
		  CipherInputStream cis = new CipherInputStream( dis, c );

		  int i;
		  while ( (i = cis.read()) >= 0 ) {
				dos.write(i);
		  }
	 }

	 public void decrypt(DataInputStream dis, DataOutputStream dos)
		  throws Exception {

		  c.init(Cipher.DECRYPT_MODE, passwordKey, paramSpec);
		  CipherInputStream cis = new CipherInputStream( dis, c );

		  int i;
		  while ( (i = cis.read()) >= 0 ) {
				dos.write(i);
		  }

	 }

	 public static void main(String[] args) {


		  if ((args.length != 4) || (!(args[0].equalsIgnoreCase("-d") || args[0].equalsIgnoreCase("-e"))) )
		  	System.out.println("Example: java RCEncrypt <-e|-d> input_filename output_filename password");
		  else {



		  try {

				FileInputStream fis = new FileInputStream( args[1] );
				DataInputStream dis = new DataInputStream( fis );

				FileOutputStream fos = new FileOutputStream( args[2] );
				DataOutputStream dos = new DataOutputStream( fos );

				RCEncrypt rce = new RCEncrypt( args[3] );

				if ( args[0].equalsIgnoreCase("-e") )
					 rce.encrypt( dis, dos );
				else if ( args[0].equalsIgnoreCase("-d") )
					 rce.decrypt( dis, dos );
				else
					TDSLogger.println("Error, '" + args[0] +
											  "' is an invalid command");

				dos.flush();
				fos.flush();
				fos.close();
				fis.close();
		  } catch (Exception e) {
			    TDSLogger.println("RCEncrypt.main() caught exception '" + e +
										 "' with message '" + e.getMessage() + "'.");
				e.printStackTrace();
		  }
}

	 }


}



