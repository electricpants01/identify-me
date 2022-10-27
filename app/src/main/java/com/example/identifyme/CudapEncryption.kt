package com.example.identifyme

import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.util.encoders.Base64
import java.nio.charset.StandardCharsets
import java.security.DigestException
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Arrays
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object CudapEncryption {
    // Cudap Encryption is base on cryptoJS from javascript
    // for more info check this library -> https://cryptojs.gitbook.io/docs/
    // npm library -> https://www.npmjs.com/package/crypto-js

    fun encrypt(strToEncrypt: String): String? {
        return try {
            val passPhrase = "5isR3c4P"
            val sr = SecureRandom()
            val salt = ByteArray(8)
            sr.nextBytes(salt)
            val keyAndIV = generateKeyAndIV(
                32, 16, 1, salt, passPhrase.toByteArray(
                    StandardCharsets.UTF_8
                ),
                MessageDigest.getInstance("MD5")
            )
            val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", BouncyCastleProvider())
            cipher.init(
                Cipher.ENCRYPT_MODE, SecretKeySpec(keyAndIV[0], "AES"), IvParameterSpec(
                    keyAndIV[1]
                )
            )
            val encryptedData = cipher.doFinal(strToEncrypt.toByteArray(StandardCharsets.UTF_8))
            val prefixAndSaltAndEncryptedData = ByteArray(16 + encryptedData.size)
            // Copy prefix (0-th to 7-th bytes)
            System.arraycopy(
                "Salted__".toByteArray(StandardCharsets.UTF_8),
                0,
                prefixAndSaltAndEncryptedData,
                0,
                8
            )
            // Copy salt (8-th to 15-th bytes)
            System.arraycopy(salt, 0, prefixAndSaltAndEncryptedData, 8, 8)
            // Copy encrypted data (16-th byte and onwards)
            System.arraycopy(
                encryptedData,
                0,
                prefixAndSaltAndEncryptedData,
                16,
                encryptedData.size
            )
            return Base64.toBase64String(prefixAndSaltAndEncryptedData)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    private fun generateKeyAndIV(
        keyLength: Int,
        ivLength: Int,
        iterations: Int,
        salt: ByteArray?,
        password: ByteArray?,
        md: MessageDigest
    ): Array<ByteArray?> {
        val digestLength = md.digestLength
        val requiredLength = (keyLength + ivLength + digestLength - 1) / digestLength * digestLength
        val generatedData = ByteArray(requiredLength)
        var generatedLength = 0
        return try {
            md.reset()

            // Repeat process until sufficient data has been generated
            while (generatedLength < keyLength + ivLength) {

                // Digest data (last digest if available, password data, salt if available)
                if (generatedLength > 0) md.update(
                    generatedData,
                    generatedLength - digestLength,
                    digestLength
                )
                md.update(password)
                if (salt != null) md.update(salt, 0, 8)
                md.digest(generatedData, generatedLength, digestLength)

                // additional rounds
                for (i in 1 until iterations) {
                    md.update(generatedData, generatedLength, digestLength)
                    md.digest(generatedData, generatedLength, digestLength)
                }
                generatedLength += digestLength
            }

            // Copy key and IV into separate byte arrays
            val result = arrayOfNulls<ByteArray>(2)
            result[0] = Arrays.copyOfRange(generatedData, 0, keyLength)
            if (ivLength > 0) result[1] =
                Arrays.copyOfRange(generatedData, keyLength, keyLength + ivLength)
            result
        } catch (e: DigestException) {
            throw RuntimeException(e)
        } finally {
            // Clean out temporary data
            Arrays.fill(generatedData, 0.toByte())
        }
    }
}