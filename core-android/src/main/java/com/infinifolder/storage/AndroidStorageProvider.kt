package com.infinifolder.storage

import android.content.Context
import android.net.Uri
import com.infinifolder.model.*

class AndroidStorageProvider(context: Context) : StorageProvider {
    private val local = LocalStorageProvider(context)
    private val documents = SafProvider(context.contentResolver)
    override val id = "android"
    private fun backend(ref: ResourceRef): StorageProvider = if (ref.providerId == local.id) local else documents
    override fun refForUri(uri: Uri): ResourceRef? = local.refForUri(uri) ?: documents.refForUri(uri)
    override fun stat(ref: ResourceRef) = backend(ref).stat(ref)
    override fun capabilities(ref: ResourceRef) = backend(ref).capabilities(ref)
    override fun parent(ref: ResourceRef) = backend(ref).parent(ref)
    override fun list(parent: ResourceRef) = backend(parent).list(parent)
    override fun openRead(ref: ResourceRef) = backend(ref).openRead(ref)
    override fun openWrite(ref: ResourceRef) = backend(ref).openWrite(ref)
    override fun openSeekable(ref: ResourceRef, writable: Boolean) = backend(ref).openSeekable(ref, writable)
    override fun replaceContent(ref: ResourceRef, data: ByteArray) = backend(ref).replaceContent(ref, data)
    override fun createDirectory(parent: ResourceRef, name: String) = backend(parent).createDirectory(parent, name)
    override fun createFile(parent: ResourceRef, name: String, mimeType: String) = backend(parent).createFile(parent, name, mimeType)
    override fun rename(ref: ResourceRef, name: String) = backend(ref).rename(ref, name)
    override fun delete(ref: ResourceRef) = backend(ref).delete(ref)
    override fun copy(source: ResourceRef, destination: ResourceRef) = backend(source).copy(source, destination)
    override fun move(source: ResourceRef, destination: ResourceRef) = backend(source).move(source, destination)
}
