package jadx.plugins.list

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import jadx.core.utils.files.FileUtils
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption.CREATE
import java.nio.file.StandardOpenOption.TRUNCATE_EXISTING
import java.nio.file.StandardOpenOption.WRITE
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class Bundle(
	private val zipPath: String,
) {
	fun save(list: List<PluginListEntry>) {
		val content = prepareGson().toJson(list)
		val zip = Paths.get(zipPath)
		FileUtils.makeDirsForFile(zip)
		ZipOutputStream(
			Files.newOutputStream(zip, WRITE, CREATE, TRUNCATE_EXISTING).buffered(),
		).use { out ->
			out.putNextEntry(ZipEntry("list.json"))
			out.write(content.toByteArray())
		}
	}

	private fun prepareGson(): Gson = GsonBuilder().create()
}
