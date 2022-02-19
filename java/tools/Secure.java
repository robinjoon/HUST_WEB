package tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Secure {
	
	public static String MD5(String str){
		String MD5 = ""; 
		try{
			MessageDigest md = MessageDigest.getInstance("MD5"); 
			md.update(str.getBytes()); 
			byte byteData[] = md.digest();
			StringBuffer sb = new StringBuffer(); 
			for(int i = 0 ; i < byteData.length ; i++){
				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
			}
			MD5 = sb.toString();
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace(); 
			MD5 = null; 
		}
		return MD5;
	}
	public static String sha256(String str) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
	    md.update(str.getBytes());
		
		return bytes2str(md.digest());
	}
	
	private static String bytes2str(byte[] bytes) {
		StringBuilder builder = new StringBuilder();
	    for (byte b: bytes) {
	      builder.append(String.format("%02x", b));
	    }
	    return builder.toString();
	}
	
	public static String check_input(String str) {
		str = str.replaceAll("\"", "&#034;");
		str = str.replaceAll("'", "&#039;");
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
	    return str;
	}
	
	public static String check_script(String str) {
		str = str.replaceAll("(?i)script", "스크립트");
		return str;
	}
}
