package jadx.plugins.list

import io.github.oshai.kotlinlogging.KotlinLogging
import jadx.plugins.list.ListBuilderCLI.ProcessMode.DELTA
import jadx.plugins.list.ListBuilderCLI.ProcessMode.FULL
import jadx.plugins.tools.JadxPluginsTools
import kotlinx.serialization.json.Json
import java.io.File

class Process(
	private val args: ListBuilderCLI,
) {
	private val log = KotlinLogging.logger {}

	fun exec() {
		val inputs = loadInputs()
		val output =
			when (args.mode) {
				FULL -> processFull(inputs)
				DELTA -> processDelta(inputs)
			}
		Bundle(args.outputZip).save(output)
	}

	private fun processFull(inputs: List<InputMetadata>): List<PluginListEntry> = inputs.map(::resolve)

	private fun processDelta(inputs: List<InputMetadata>): List<PluginListEntry> {
		TODO()
	}

	private fun loadInputs(): List<InputMetadata> {
		val inputDir = File(args.inputDir)
		return inputDir
			.walkTopDown()
			.filter { it.name.endsWith(".json") && it.isFile }
			.sortedBy { it.relativeTo(inputDir).path }
			.map {
				val data = Json.decodeFromString<InputMetadata>(it.readText())
				if (data.pluginId.isBlank()) {
					data.pluginId = it.name.removeSuffix(".json")
				}
				data
			}.toList()
	}

	private fun resolve(input: InputMetadata): PluginListEntry {
		log.debug { "resolving plugin: '${input.pluginId}' from '${input.locationId}'" }
		val metadata = JadxPluginsTools.getInstance().resolveMetadata(input.locationId)
		val pluginEntry = PluginListEntry.convert(metadata)
		verifyEntry(pluginEntry, input)
		processEntry(pluginEntry, input)
		return pluginEntry
	}

	private fun verifyEntry(
		pluginEntry: PluginListEntry,
		input: InputMetadata,
	) {
		if (pluginEntry.pluginId != input.pluginId) {
			throw IllegalArgumentException(
				"Input plugin id: '${input.pluginId}' should be equal to" +
					" plugin id from plugin info: '${pluginEntry.pluginId}'",
			)
		}
	}

	private fun processEntry(
		pluginEntry: PluginListEntry,
		input: InputMetadata,
	) {
		pluginEntry.revision = input.revision
		if (pluginEntry.homepage.isBlank()) {
			pluginEntry.homepage = resolveHomepage(pluginEntry.locationId)
		}
	}

	private fun resolveHomepage(locationId: String): String {
		if (locationId.startsWith("github:")) {
			val split = locationId.split(":")
			if (split.size == 3) {
				return "https://github.com/${split[1]}/${split[2]}"
			}
		}
		return ""
	}
}
