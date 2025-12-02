package com.aplimovil.rotappmp.data.local

import com.russhwolf.settings.Settings

private class JsInMemorySettings : Settings {
	private val store = mutableMapOf<String, Any?>()

	override val keys: Set<String>
		get() = store.keys

	override val size: Int
		get() = store.size

	override fun clear() {
		store.clear()
	}

	override fun remove(key: String) {
		store.remove(key)
	}

	override fun hasKey(key: String): Boolean = store.containsKey(key)

	override fun putInt(key: String, value: Int) {
		store[key] = value
	}

	override fun getInt(key: String, defaultValue: Int): Int =
		when (val value = store[key]) {
			is Int -> value
			is Number -> value.toInt()
			else -> defaultValue
		}

	override fun getIntOrNull(key: String): Int? =
		when (val value = store[key]) {
			is Int -> value
			is Number -> value.toInt()
			else -> null
		}

	override fun putLong(key: String, value: Long) {
		store[key] = value
	}

	override fun getLong(key: String, defaultValue: Long): Long =
		when (val value = store[key]) {
			is Long -> value
			is Int -> value.toLong()
			is Number -> value.toLong()
			else -> defaultValue
		}

	override fun getLongOrNull(key: String): Long? =
		when (val value = store[key]) {
			is Long -> value
			is Int -> value.toLong()
			is Number -> value.toLong()
			else -> null
		}

	override fun putString(key: String, value: String) {
		store[key] = value
	}

	override fun getString(key: String, defaultValue: String): String =
		(store[key] as? String) ?: defaultValue

	override fun getStringOrNull(key: String): String? = store[key] as? String

	override fun putFloat(key: String, value: Float) {
		store[key] = value
	}

	override fun getFloat(key: String, defaultValue: Float): Float =
		when (val value = store[key]) {
			is Float -> value
			is Double -> value.toFloat()
			is Number -> value.toFloat()
			else -> defaultValue
		}

	override fun getFloatOrNull(key: String): Float? =
		when (val value = store[key]) {
			is Float -> value
			is Double -> value.toFloat()
			is Number -> value.toFloat()
			else -> null
		}

	override fun putDouble(key: String, value: Double) {
		store[key] = value
	}

	override fun getDouble(key: String, defaultValue: Double): Double =
		when (val value = store[key]) {
			is Double -> value
			is Float -> value.toDouble()
			is Number -> value.toDouble()
			else -> defaultValue
		}

	override fun getDoubleOrNull(key: String): Double? =
		when (val value = store[key]) {
			is Double -> value
			is Float -> value.toDouble()
			is Number -> value.toDouble()
			else -> null
		}

	override fun putBoolean(key: String, value: Boolean) {
		store[key] = value
	}

	override fun getBoolean(key: String, defaultValue: Boolean): Boolean =
		when (val value = store[key]) {
			is Boolean -> value
			else -> defaultValue
		}

	override fun getBooleanOrNull(key: String): Boolean? = store[key] as? Boolean
}

actual fun createSettings(): Settings = JsInMemorySettings()
