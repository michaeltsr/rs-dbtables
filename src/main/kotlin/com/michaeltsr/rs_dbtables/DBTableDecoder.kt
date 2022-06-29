package com.michaeltsr.rs_dbtables

import com.michaeltsr.rs_dbtables.data.Column
import com.michaeltsr.rs_dbtables.data.Table
import com.displee.cache.CacheLibrary
import com.displee.cache.index.archive.Archive
import com.displee.cache.index.archive.file.File
import com.michaeltsr.rs_dbtables.data.DBRow
import com.michaeltsr.rs_dbtables.util.ByteCollectionUtility.buffer
import com.michaeltsr.rs_dbtables.util.ByteCollectionUtility.smart1or2ToInt
import com.michaeltsr.rs_dbtables.util.ByteCollectionUtility.uByteToInt
import java.io.IOException
import java.nio.ByteBuffer


fun main() {
//    val dataTablePath = CacheLoader::class.java.getResource("/4.bin")
//    val dataRowPath = CacheLoader::class.java.getResource("/2919.bin")
//    val dbTableData = ByteBuffer.wrap(dataTablePath?.readBytes() ?: throw IOException())
//    val dbRowData = ByteBuffer.wrap(dataRowPath?.readBytes() ?: throw IOException())
    val cachePath = DBTableDecoder::class.java.getResource("/runescape/cache/")
    requireNotNull(cachePath) { "Invalid cache path." }
    val dataRowPath = DBTableDecoder::class.java.getResource("/2919.bin")
    val dbRowData = ByteBuffer.wrap(dataRowPath?.readBytes() ?: throw IOException())
    val dbColumnPath = DBTableDecoder::class.java.getResource("/90.bin")
    val dbColumnData = ByteBuffer.wrap(dataRowPath?.readBytes() ?: throw IOException())

//    DBTableDecoder(cachePath.path).developmentTest()
    val columnArchive = Archive(id = 39)
    columnArchive.add(File(id = 4, data = dbColumnData.array()))
    val rowArchive = Archive(id = 38)
    rowArchive.add(data = dbRowData.array())
    val lib = CacheLibrary(cachePath.path)
    lib.index(2).add(columnArchive)
    lib.index(2).add(rowArchive)
    lib.update()
    lib.index(2).archives().forEach { println(it) }
}

class DBTableDecoder(cachePath: String) {
    private val configurationIndex = 2
    private val columnArchive = 69
    private val rowArchive = 69
    private val index = CacheLibrary(cachePath).index(configurationIndex)

    private val columns = decodeColumns(index.archive(columnArchive))
    //private val rows = decodeRows(index.archive(rowArchive))

    fun decode() : Table {
        return TODO()
    }

    private fun decodeColumns(archive: Archive?) : Map<Int, Column> {
        requireNotNull(archive) { "Requested archive returned null." }
        require(archive.files.size != 0) { "Requested archive does not have any files." }
        return archive.files.values.associate {
            val data = requireNotNull(it.data) { "The column data for file id ${it.id} is null." }
            it.id to decodeColumn(data.buffer())
        }
    }

    private fun decodeRows(archive: Archive?) : List<DBRow> {
        requireNotNull(archive) { "Requested row returned null." }
        require(archive.files.size != 0) { "Requested archive does not have any files." }
        return archive.files.values.map {
            val data = requireNotNull(it.data) { "The row data for file id ${it.id} is null." }
            decodeRow(data.buffer())
        }
    }

    fun developmentTest() {
        val dataRowPath = DBTableDecoder::class.java.getResource("/2919.bin")
        val dbRowData = ByteBuffer.wrap(dataRowPath?.readBytes() ?: throw IOException())
        decodeRow(dbRowData)
    }

    private fun decodeRow(data: ByteBuffer): DBRow {
        val opcode = data.uByteToInt()
        var row: DBRow? = null
        var tableId = -1
        if (opcode == 3) row = decodeRowFields(data, columns[tableId] ?: throw NullPointerException("No table with id $tableId was found."))
        if (opcode == 4)  tableId = decodeTableId(data)
        return row ?: throw NullPointerException("Error decoding the current row.")
    }

    private fun decodeRowFields(data: ByteBuffer, column: Column) : DBRow {
        val fieldCount = data.smart1or2ToInt()
        val values = mutableListOf<Any>()
        val types = column.types
        repeat(fieldCount) {

        }
        return TODO()
    }

    private fun decodeTableId(data: ByteBuffer) : Int {
        var value = 0; var bits = 0
        do {
            val num = data.uByteToInt()
            value = value or (num and 0x7F) shl bits
            bits += 7
        } while (num > 127)
        return value
    }
}

fun decodeColumn(data: ByteBuffer?) : Column {
    require(data != null) { "The database column bytebuffer is null." }
    val types = mutableListOf<Int>()
    val columnCount = data.uByteToInt()
    val setting = data.uByteToInt()
    while (setting != 255) {
        val column = setting and 0x7f
        val columnTypesCount = data.uByteToInt()
        repeat(columnTypesCount) {
            val columnType = data.smart1or2ToInt()
            types.add(columnType)
        }
    }
    return TODO()
}
