package jadx.plugins.list

import jadx.plugins.tools.data.JadxPluginListEntry
import jadx.plugins.tools.data.JadxPluginMetadata
import kotlinx.serialization.Serializable

@Serializable
data class InputMetadata(
	var pluginId: String = "",
	val locationId: String,
	val revision: Int = 0,
)

/**
 * Copy of [JadxPluginListEntry]
 */
@Serializable
data class PluginListEntry(
	val pluginId: String,
	val locationId: String,
	val name: String,
	val description: String,
	var homepage: String = "",
	var revision: Int = 0,
) {
	companion object {
		fun convert(metadata: JadxPluginMetadata) =
			PluginListEntry(
				pluginId = metadata.pluginId,
				locationId = metadata.locationId,
				revision = 0,
				name = metadata.name,
				description = metadata.description,
				homepage = metadata.homepage,
			)

		fun convert(metadata: JadxPluginListEntry) =
			PluginListEntry(
				pluginId = metadata.pluginId,
				locationId = metadata.locationId,
				revision = metadata.revision,
				name = metadata.name,
				description = metadata.description,
				homepage = metadata.homepage,
			)
	}
}
