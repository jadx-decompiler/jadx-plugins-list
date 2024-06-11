package jadx.plugins.list

import jadx.plugins.tools.data.JadxPluginMetadata
import kotlinx.serialization.Serializable

@Serializable
data class InputMetadata(
	var pluginId: String = "",
	val locationId: String,
	val revision: Int = 0,
)

/**
 * Share most fields with [JadxPluginMetadata]
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
		fun convert(metadata: JadxPluginMetadata) = PluginListEntry(
			pluginId = metadata.pluginId,
			locationId = metadata.locationId,
			name = metadata.name,
			description = metadata.description,
			homepage = metadata.homepage,
		)
	}
}
