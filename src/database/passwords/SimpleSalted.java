package database.passwords;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

/**
 * Code from
 * http://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-
 * sha-pbkdf2-bcrypt-examples/
 *
 */
public class SimpleSalted
{
	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException
	{
		String passwordToHash = "password";
		String salt = getSalt();
		String algorithm = "MD5"; // SHA-512 stronger than MD5
		String securePassword = getSecurePassword(passwordToHash, salt, algorithm);

		System.out.println(securePassword);

		String regeneratedPasswordToVerify = getSecurePassword(passwordToHash, salt, algorithm);

		System.out.println(regeneratedPasswordToVerify);
	}

	/**
	 * Generate a password with stated algorithm
	 * 
	 * @param passwordToHash
	 * @param salt
	 * @param algorithm The algorithm to use (e.g. MD5, SHA-512)
	 * @return
	 */
	private static String getSecurePassword(String passwordToHash, String salt, String algorithm)
	{
		String generatedPassword = null;
		try
		{
			// Create MessageDigest instance for algorithm
			MessageDigest md = MessageDigest.getInstance(algorithm);
			//Add password bytes to digest
			md.update(salt.getBytes());
			//Get the hash's bytes 
			byte[] bytes = md.digest(passwordToHash.getBytes());
			//This bytes[] has bytes in decimal format;
			//Convert it to hexadecimal format
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++)
			{
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			//Get complete hashed password in hex format
			generatedPassword = sb.toString();
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		return generatedPassword;
	}

	/*
	 * Get a salt to protect the hash from being cracked by 'Rainbow Tables'
	 */
	private static String getSalt() throws NoSuchAlgorithmException, NoSuchProviderException
	{
		//Always use a SecureRandom generator
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		//Create array for salt
		byte[] salt = new byte[16];
		//Get a random salt
		sr.nextBytes(salt);
		//return salt
		return salt.toString();
	}
}
