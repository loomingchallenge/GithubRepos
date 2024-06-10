package com.githubrepos.framework.datasource.preferences.multiplatform_settings

import java.util.Base64
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

private const val KEY_ALGORITHM = "AES"
private const val KEY_SIZE_BITS = 256
private const val DEFAULT_KEY = "V3glMd9RUOuYaM3kJx5mRO5LdHNMkxR7kfxS8Nw4uFY="

class SecretKeyManager(
    key: String = DEFAULT_KEY,
) {
    var secretKey: SecretKey
        private set

    init {
        secretKey = SecretKeySpec(Base64.getDecoder().decode(key), KEY_ALGORITHM)
    }

    companion object {
        fun generateKey(): SecretKey {
            val keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM)
            keyGenerator.init(KEY_SIZE_BITS)
            return keyGenerator.generateKey()
        }

        fun generatePrintableKey(): String {
            return Base64.getEncoder().encodeToString(generateKey().encoded)
        }
    }

    fun getPrintableKey(): String =
        Base64.getEncoder().encodeToString(secretKey.encoded)
}