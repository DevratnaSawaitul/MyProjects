package com.pms.util;

import java.util.Arrays;
import java.util.List;

public class NormalEncrp {
	
	private static final List<Character> ENCRYPTION_KEY = Arrays.asList('T', 'h', 'e', 'B', 'e', 's', 't', 'K', 'e', 'y');

    // Encrypt method
    public static String encrypt(String input) {
        StringBuilder encrypted = new StringBuilder();
        int keyLength = ENCRYPTION_KEY.size();
        for (int i = 0; i < input.length(); i++) {
            char encryptedChar = (char) (input.charAt(i) + ENCRYPTION_KEY.get(i % keyLength));
            encrypted.append(encryptedChar);
        }
        return encrypted.toString();
    }

    // Decrypt method
    public static String decrypt(String input) {
        StringBuilder decrypted = new StringBuilder();
        int keyLength = ENCRYPTION_KEY.size();
        for (int i = 0; i < input.length(); i++) {
            char decryptedChar = (char) (input.charAt(i) - ENCRYPTION_KEY.get(i % keyLength));
            decrypted.append(decryptedChar);
        }
        return decrypted.toString();
    }

	public static void main(String args[]) throws Exception {
		System.out.println("The password is: " + encrypt("superadmin"));
		System.out.println("The password is: " + decrypt("ÇÝÕ§×ÔØ¸Îç"));
	}

}
