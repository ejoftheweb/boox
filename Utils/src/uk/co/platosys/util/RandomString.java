/*
 *  Returns a random string. Note this implementation does 
 *  not do so securely. The getRandomKey method, which returns a 
 *  random long formatted as a hex string, is marginally more 
 *  secure. None of this code should be used in
 *  a security-critical application.
 */

package uk.co.platosys.util;

import java.security.SecureRandom;

/**
 *
 * @author edward
 */
public class RandomString  {
    /**
     * returns a random string of the given length.
     * @param length
     * @return
     */
 public static String getRandomString(int length){

    char[] pw = new char[length];
    int c=0;
    int  range = 0;
    for (int i=0; i < length; i++)
    {
      range = (int)(Math.random() * 3);
      switch(range) {
        case 0: c = '0' +  (int)(Math.random() * 10); break;
        case 1: c = 'a' +  (int)(Math.random() * 26); break;
        case 2: c = 'A' +  (int)(Math.random() * 26); break;
      }
      pw[i] = (char)c;
    }
    return new String(pw);
  }


/**
 * This returns a SecureRandom long, formatted as a hex string.
 */
public static String getRandomKey(){
    SecureRandom sr = new SecureRandom();
    long l = sr.nextLong();
    return Long.toHexString(l);
}
}
