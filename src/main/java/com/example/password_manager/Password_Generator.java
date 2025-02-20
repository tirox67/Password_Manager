package com.example.password_manager;

import java.util.Random;

/* A safe password should contain at least one capital letter, one number and on sign like +,- etc. Witch comes to a regex like
   ([a,z]+[A,Z]+[0,9]+[+,-,!,",§...])^10
   I want the passwords to have a fixed length of 10
   110 prefix for letters, then count from 1 to 26
   010 prefix for capital letters, then count from 1 to 26
   001 prefix for sings and then count to 30
*/


public class Password_Generator {
    private static String password = "";
    public static  void createPassword(){
        password = "";//reset the password
        // using a randomizer with upperbound 2 (not included) to create random bit-strings with length 10
       Random rand = new Random();
       int temp = 0;
       for(int j =0; j<20; j++) {
           temp = rand.nextInt(93)+33;
           password += (char) temp;
           }

    }
    public static String getPassword() {
        return password;
    }//end of getPassword
}//end of class
