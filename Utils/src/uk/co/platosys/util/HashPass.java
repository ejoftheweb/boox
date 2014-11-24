/*
 * HashPass.java
 *
 * Created on 08 October 2007, 22:33
 *
 * This code is copyright PLATOSYS,
 * to the extent that copyright subsists.
 *
 *
 */

package uk.co.platosys.util;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import org.apache.commons.codec.binary.Base64;


/**
 * This is a rewrite of my earlier class (which just used SHA1, unsalted), using suggestions from Martin Konecki made on Stack Overflow.
 * 
 *
 *
 * @author edward
 */
public class HashPass {
 /*   private static String algorithm = "SHA1";
    private static String encoding = "UTF-8";
    
     [public static String hash(String password){
         return RealmBase.Digest(password, algorithm);
     }]
      public static String hash(char[] password){
        return RealmBase.Digest(new String(password), algorithm);
     }
      public static boolean check (char[] clearPassword, String hashedPassword){
          return check(new String(clearPassword), hashedPassword);
      }
     public static boolean check(String clearPassword, String hashedPassword){
         try{
            if ((RealmBase.Digest(clearPassword, algorithm)).equals(hashedPassword)){
                return true;
            }else{
                return false;
            }
         }catch(Exception e){
             
             return false;}
  }
    
}*/


    // The higher the number of iterations the more 
    // expensive computing the hash is for us
    // and also for a brute force attack.
    private static final int iterations = 646;
    private static final int saltLen = 32;
    private static final int desiredKeyLen = 256;

    /** Computes a salted PBKDF2 hash of given plaintext password
        suitable for storing in a database. 
        Empty passwords are not supported. */
    public static String hash(String password)  {
    	try{
		    byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLen);
		    // store the salt with the password
		    return Base64.encodeBase64String(salt) + "$" + hash(password.toCharArray(), salt);
    	}catch(Exception x){return "";}
    }
    /** Computes a salted PBKDF2 hash of given plaintext password
    suitable for storing in a database. 
    Empty passwords are not supported. */
public static String hash(char[] password)  {
	try{
    byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLen);
    // store the salt with the password
    return Base64.encodeBase64String(salt) + "$" + hash(password, salt);
	}catch(Exception x){return "";}
}

    /** Checks whether given plaintext password corresponds 
        to a stored salted hash of the password. */
    public static boolean check(String password, String stored) {
        String[] saltAndPass = stored.split("\\$");
        if (saltAndPass.length != 2){return false;}
        String hashOfInput = hash(password.toCharArray(), Base64.decodeBase64(saltAndPass[0]));
        return hashOfInput.equals(saltAndPass[1]);
    }
    /** Checks whether given plaintext password corresponds 
    to a stored salted hash of the password. */
    public static boolean check(char[] password, String stored){
    String[] saltAndPass = stored.split("\\$");
    if (saltAndPass.length != 2){return false;}
    
    String hashOfInput = hash(password, Base64.decodeBase64(saltAndPass[0]));
    return hashOfInput.equals(saltAndPass[1]);
}
    // using PBKDF2 from Sun, an alternative is https://github.com/wg/scrypt
    // cf. http://www.unlimitednovelty.com/2012/03/dont-use-bcrypt.html
    private static String hash(char[] password, byte[] salt) {
    	try{
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey key = f.generateSecret(new PBEKeySpec(password, salt, iterations, desiredKeyLen));
        return Base64.encodeBase64String(key.getEncoded());
    	}catch(Exception x){return "";}
    }
}
