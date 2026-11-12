package com.infinifolder.storage

import io.github.gretchycat.infinistream.*
import org.junit.Assert.*
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.util.zip.GZIPOutputStream

class CompressedFormatIdentityTest {
    private fun encoded(text: String) = ByteArrayOutputStream().also {
        GZIPOutputStream(it).use { zip -> zip.write(text.toByteArray()) }
    }.toByteArray()
    private fun handler() = object : CapabilityHandler {
        override val descriptor = HandlerDescriptor("test.compressed-svg", "test", "Test compressed image validator",
            listOf(FormatSupport(setOf("image/svg+xml"), setOf("svgz"))), setOf(Operations.IDENTIFY))
        override fun probe(context: CapabilityProbeContext): HandlerProbe {
            val svg = runCatching { java.util.zip.GZIPInputStream(context.prefix().inputStream()).bufferedReader().use { it.readLine().startsWith("<svg") } }.getOrDefault(false)
            return HandlerProbe(if(svg) MatchConfidence.VERIFIED else MatchConfidence.NONE, if(svg) setOf(Operations.IDENTIFY) else emptySet(),
                metadata=if(svg) mapOf("mimeType" to "image/svg+xml", "format" to "Compressed SVG") else emptyMap())
        }
    }
    @Test fun registeredCompressedImageWinsBeforeGenericGzipInBothEntryPoints() {
        RuntimeCapabilities.installed().register(handler()).use {
            val bytes = encoded("<svg xmlns='http://www.w3.org/2000/svg'/>\n")
            assertEquals("image/svg+xml", FileMagicDetector.detectFromStream("art.svgz", "image/svg+xml", bytes.size.toLong()) { bytes.inputStream() }?.mimeType)
            val file = java.io.File.createTempFile("compressed-image", ".svgz")
            try { file.writeBytes(bytes); assertEquals("image/svg+xml", FileMagicDetector.detect(file)?.mimeType) }
            finally { file.delete() }
        }
    }
    @Test fun unvalidatedCompressedBytesKeepTheirContainerIdentity() {
        RuntimeCapabilities.installed().register(handler()).use {
            val bytes = encoded("ordinary text")
            assertEquals("application/gzip", FileMagicDetector.detectFromStream("fake.svgz", "image/svg+xml", bytes.size.toLong()) { bytes.inputStream() }?.mimeType)
        }
    }
}
