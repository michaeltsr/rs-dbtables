package com.michaeltsr.rs_dbtables.util

import java.nio.ByteBuffer

object ByteCollectionUtility {
    fun ByteArray.buffer() : ByteBuffer {
        return ByteBuffer.wrap(this)
    }
    fun ByteBuffer.uByteToInt() : Int {
        return this.get().toInt() and 0xFF
    }

    fun ByteBuffer.uShortToInt() : Int {
        return this.short.toInt() and 0xFFFF
    }

    fun ByteBuffer.smart1or2ToInt() : Int {
        val data = this.get(this.position()).toUInt()
        return if(data < 128u) this.uByteToInt() else this.uShortToInt() - 32768
    }

//    fun ByteBuffer.variableSize() : Int {
//        val value = 0; val bits = 0
//        val read = this.get()
//        while (read > 127) {
//    }
}


