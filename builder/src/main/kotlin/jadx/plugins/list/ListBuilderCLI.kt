package jadx.plugins.list

import io.github.oshai.kotlinlogging.KotlinLogging
import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import java.util.concurrent.Callable
import kotlin.system.exitProcess

@Command(
	name = "jadx-plugins-list-builder",
	description = ["Build jadx plugins list bundle"],
	mixinStandardHelpOptions = true,
	sortOptions = false,
)
class ListBuilderCLI : Callable<Int> {
	private val log = KotlinLogging.logger {}

	@Option(names = ["-i", "--input"], description = ["input directory"], required = true)
	lateinit var inputDir: String

	@Option(names = ["-o", "--output"], description = ["output zip file"], required = true)
	lateinit var outputZip: String

	enum class ProcessMode {
		FULL,
		DELTA,
	}

	@Option(names = ["-m", "--mode"], description = ["process mode: \${COMPLETION-CANDIDATES}"], required = true)
	lateinit var mode: ProcessMode

	override fun call(): Int = try {
		Process(this).exec()
		0
	} catch (e: Throwable) {
		log.error(e) { "Failed to build plugins list" }
		1
	}

	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			exitProcess(CommandLine(ListBuilderCLI()).execute(*args))
		}
	}
}
