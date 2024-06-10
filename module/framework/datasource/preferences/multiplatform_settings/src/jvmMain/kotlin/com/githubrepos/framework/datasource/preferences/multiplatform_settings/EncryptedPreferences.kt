package com.githubrepos.framework.datasource.preferences.multiplatform_settings

import java.io.OutputStream
import java.util.Base64
import java.util.prefs.NodeChangeListener
import java.util.prefs.PreferenceChangeListener
import java.util.prefs.Preferences
import javax.crypto.Cipher
import javax.crypto.SecretKey

private const val ENCRYPTION_ALGORITHM = "AES/ECB/PKCS5Padding"

class EncryptedPreferences(
    private val preferences: Preferences,
    secretKey: SecretKey
) : Preferences() {

    private val encryptCipher: Cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM)
    private val decryptCipher: Cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM)

    init {
        encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey)
        decryptCipher.init(Cipher.DECRYPT_MODE, secretKey)
    }

    private fun encrypt(data: String): String {
        val encryptedBytes = encryptCipher.doFinal(data.toByteArray())
        return Base64.getEncoder().encodeToString(encryptedBytes)
    }

    private fun decrypt(encryptedData: String): String {
        val encryptedBytes = Base64.getDecoder().decode(encryptedData)
        val decryptedBytes = decryptCipher.doFinal(encryptedBytes)
        return String(decryptedBytes)
    }

    override fun toString(): String {
        return preferences.toString()
    }

    override fun put(key: String, value: String) {
        val encryptedValue = encrypt(value)
        preferences.put(key, encryptedValue)
    }

    override fun get(key: String, def: String?): String? {
        val encryptedValue = preferences.get(key, null) ?: return def
        return decrypt(encryptedValue)
    }

    override fun putInt(key: String, value: Int) {
        put(key, value.toString())
    }

    override fun getInt(key: String, def: Int): Int {
        val stringValue = get(key, null)
        return stringValue?.toIntOrNull() ?: def
    }

    override fun putLong(key: String, value: Long) {
        put(key, value.toString())
    }

    override fun getLong(key: String, def: Long): Long {
        val stringValue = get(key, null)
        return stringValue?.toLongOrNull() ?: def
    }

    override fun putBoolean(key: String, value: Boolean) {
        put(key, value.toString())
    }

    override fun getBoolean(key: String, def: Boolean): Boolean {
        val stringValue = get(key, null)
        return stringValue?.toBoolean() ?: def
    }

    override fun putFloat(key: String, value: Float) {
        put(key, value.toString())
    }

    override fun getFloat(key: String, def: Float): Float {
        val stringValue = get(key, null)
        return stringValue?.toFloatOrNull() ?: def
    }

    override fun putDouble(key: String, value: Double) {
        put(key, value.toString())
    }

    override fun getDouble(key: String, def: Double): Double {
        val stringValue = get(key, null)
        return stringValue?.toDoubleOrNull() ?: def
    }

    override fun putByteArray(key: String, value: ByteArray) {
        put(key, value.toString())
    }

    override fun getByteArray(key: String, def: ByteArray): ByteArray {
        val stringValue = get(key, null)
        return stringValue?.toByteArray() ?: def
    }

    override fun remove(key: String) {
        preferences.remove(key)
    }

    override fun clear() {
        preferences.clear()
    }

    override fun keys(): Array<String> {
        return preferences.keys()
    }

    override fun childrenNames(): Array<String> {
        return preferences.childrenNames()
    }

    override fun parent(): Preferences {
        return preferences.parent()
    }

    override fun node(pathName: String): Preferences {
        return preferences.node(pathName)
    }

    override fun nodeExists(pathName: String): Boolean {
        return preferences.nodeExists(pathName)
    }

    override fun removeNode() {
        return preferences.removeNode()
    }

    override fun name(): String {
        return preferences.name()
    }

    override fun absolutePath(): String {
        return preferences.absolutePath()
    }

    override fun isUserNode(): Boolean {
        return preferences.isUserNode
    }

    override fun flush() {
        return preferences.flush()
    }

    override fun sync() {
        preferences.sync()
    }

    override fun addPreferenceChangeListener(listener: PreferenceChangeListener) {
        preferences.addPreferenceChangeListener(listener)
    }

    override fun removePreferenceChangeListener(listener: PreferenceChangeListener) {
        preferences.removePreferenceChangeListener(listener)
    }

    override fun addNodeChangeListener(listener: NodeChangeListener) {
        preferences.addNodeChangeListener(listener)
    }

    override fun removeNodeChangeListener(listener: NodeChangeListener?) {
        preferences.removeNodeChangeListener(listener)
    }

    override fun exportNode(stream: OutputStream) {
        preferences.exportNode(stream)
    }

    override fun exportSubtree(stream: OutputStream) {
        preferences.exportSubtree(stream)
    }
}