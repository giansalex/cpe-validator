package greenter.sunat.validator

import greenter.sunat.validator.command.Validator
import picocli.CommandLine

fun main(args: Array<String>) {
    CommandLine.run(Validator(), System.err, *args)
}