package database.passwords;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Code from
 * http://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-
 * sha-pbkdf2-bcrypt-examples/
 *
 */
public class ReallyStrongSecuredPassword
{
	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		String originalPassword = "password";
		String generatedSecuredPasswordHash = generateStrongPasswordHash(originalPassword);
		System.out.println(generatedSecuredPasswordHash);

		boolean matched = validatePassword("password", generatedSecuredPasswordHash);
		System.out.println(matched);

		matched = validatePassword("password1", generatedSecuredPasswordHash);
		System.out.println(matched);
	}

	/**
	 * Check the validity of a candidate password
	 * 
	 * @param candidatePassword password to check
	 * @param storedPassword the hash
	 * @return true if candidate matches hash
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	private static boolean validatePassword(String candidatePassword, String storedPassword)
			throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		String[] parts = storedPassword.split(":");
		int iterations = Integer.parseInt(parts[0]);
		byte[] salt = fromHex(parts[1]);
		byte[] hash = fromHex(parts[2]);

		PBEKeySpec spec = new PBEKeySpec(candidatePassword.toCharArray(), salt, iterations, hash.length * 8);

		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

		byte[] testHash = skf.generateSecret(spec).getEncoded();

		//check if all the bits of the two hashes are the same 

		int diff = hash.length ^ testHash.length;

		for (int i = 0; i < hash.length && i < testHash.length; i++)
		{
			diff |= hash[i] ^ testHash[i];
		}

		return diff == 0;
	}

	/**
	 * Generate a new hash
	 * 
	 * @param password string to be hashed and salted
	 * @return the hash of the password
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	private static String generateStrongPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		int iterations = 1000;
		char[] chars = password.toCharArray();
		byte[] salt = getSalt().getBytes();

		PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		byte[] hash = skf.generateSecret(spec).getEncoded();
		return iterations + ":" + toHex(salt) + ":" + toHex(hash);

	}

	/*
	 * Get a salt to protect the hash from being cracked by 'Rainbow Tables'
	 */
	private static String getSalt() throws NoSuchAlgorithmException
	{
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[16];
		sr.nextBytes(salt);
		return salt.toString();
	}

	/**
	 * Translates a byte array to a hex string
	 * 
	 * @param array
	 * @return hex string representation of array
	 * @throws NoSuchAlgorithmException
	 */
	private static String toHex(byte[] array) throws NoSuchAlgorithmException
	{
		BigInteger bi = new BigInteger(1, array);
		String hex = bi.toString(16);
		int paddingLength = (array.length * 2) - hex.length();
		if (paddingLength > 0)
		{
			return String.format("%0" + paddingLength + "d", 0) + hex;
		}
		else
		{
			return hex;
		}
	}

	/**
	 * Translates a hex string to a byte array
	 * 
	 * @param hex
	 * @return byte array representation of hex
	 * @throws NoSuchAlgorithmException
	 */
	private static byte[] fromHex(String hex) throws NoSuchAlgorithmException
	{
		byte[] bytes = new byte[hex.length() / 2];
		for (int i = 0; i < bytes.length; i++)
		{
			bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return bytes;
	}
}
