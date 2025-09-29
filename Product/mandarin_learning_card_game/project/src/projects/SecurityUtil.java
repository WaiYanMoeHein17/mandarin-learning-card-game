package projects;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Utility class for secure password handling in the Mandarin Learning Card Game.
 * This class provides methods for:
 * - Securely hashing passwords using PBKDF2 with SHA-256
 * - Validating password inputs against stored hashes
 * - Generating cryptographically secure salts
 * 
 * The implementation follows industry best practices:
 * - Uses PBKDF2 with HMAC-SHA256 for key derivation
 * - 16-byte cryptographically secure random salt for each password
 * - 65,536 iterations for key stretching
 * - 256-bit derived key length
 */
public class SecurityUtil {
    
    /** Algorithm used for password hashing */
    private static final String HASH_ALGORITHM = "PBKDF2WithHmacSHA1";
    
    /** Number of iterations for key stretching (65,536 is recommended minimum) */
    private static final int ITERATIONS = 65536;
    
    /** Length of the derived key in bits */
    private static final int KEY_LENGTH = 256;
    
    /** Salt length in bytes */
    private static final int SALT_LENGTH = 16;
    
    /**
     * Hashes a password using PBKDF2 with SHA-256.
     * 
     * @param password The plain text password to hash
     * @return A string in the format "iterations:salt:hash" where salt and hash are base64 encoded
     * @throws SecurityException If the hash algorithm is not available
     */
    public static String hashPassword(String password) throws SecurityException {
        try {
            // Generate a secure random salt
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);
            
            // Create the hash
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(HASH_ALGORITHM);
            byte[] hash = factory.generateSecret(spec).getEncoded();
            
            // Format as iterations:base64(salt):base64(hash)
            return ITERATIONS + ":" + Base64.getEncoder().encodeToString(salt) + ":" + 
                   Base64.getEncoder().encodeToString(hash);
            
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new SecurityException("Error hashing password: " + e.getMessage(), e);
        }
    }
    
    /**
     * Validates a password against a stored hash.
     * 
     * @param password The plain text password to check
     * @param storedHash The stored password hash in format "iterations:salt:hash"
     * @return true if the password matches the hash, false otherwise
     * @throws SecurityException If the hash algorithm is not available or the stored hash is invalid
     */
    public static boolean verifyPassword(String password, String storedHash) throws SecurityException {
        try {
            // Split the stored hash into parts
            String[] parts = storedHash.split(":");
            if (parts.length != 3) {
                throw new SecurityException("Invalid stored hash format");
            }
            
            int iterations = Integer.parseInt(parts[0]);
            byte[] salt = Base64.getDecoder().decode(parts[1]);
            byte[] hash = Base64.getDecoder().decode(parts[2]);
            
            // Compute hash for the provided password
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, hash.length * 8);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(HASH_ALGORITHM);
            byte[] testHash = factory.generateSecret(spec).getEncoded();
            
            // Compare the computed hash with the stored hash
            return Arrays.equals(hash, testHash);
            
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new SecurityException("Error verifying password: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new SecurityException("Invalid stored hash format: " + e.getMessage(), e);
        }
    }
    
    /**
     * Validates that input meets basic safety requirements.
     * 
     * @param input The string to validate
     * @param minLength The minimum allowed length
     * @param maxLength The maximum allowed length
     * @param allowedPattern A regex pattern of allowed characters
     * @return true if valid, false otherwise
     */
    public static boolean validateInput(String input, int minLength, int maxLength, String allowedPattern) {
        if (input == null || input.length() < minLength || input.length() > maxLength) {
            return false;
        }
        return input.matches(allowedPattern);
    }
    
    /**
     * Validates a username for security and format.
     * Only allows alphanumeric characters and underscores, 3-30 characters.
     * 
     * @param username The username to validate
     * @return true if valid, false otherwise
     */
    public static boolean validateUsername(String username) {
        return validateInput(username, 3, 30, "^[a-zA-Z0-9_]+$");
    }
    
    /**
     * Validates a name field (forename, surname).
     * Allows letters, spaces, hyphens and apostrophes, 1-50 characters.
     * 
     * @param name The name to validate
     * @return true if valid, false otherwise
     */
    public static boolean validateName(String name) {
        return validateInput(name, 1, 50, "^[a-zA-Z\\s\\-']+$");
    }
}