package com.infinifolder.storage

import com.infinifolder.model.*
import java.io.IOException

class InvalidTransferCheckpointException(message: String) : IOException(message)
/** Failure before any checkpoint payload bytes were submitted to storage. */
class CheckpointWriteRejectedException(message: String, cause: Exception) : IOException(message, cause)

/** Small companion state, separate from the data file and ordinary file caches. */
interface TransferCheckpointStore : java.io.Closeable {
    override fun close() {}
    fun read(): ByteArray?
    fun write(bytes: ByteArray)
    fun fingerprint(): String?
    fun delete()
}

