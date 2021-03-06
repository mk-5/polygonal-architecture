package io.polygonal.verifytask.parsers

import io.polygonal.verifytask.dto.PackageInformation
import spock.lang.Specification
import spock.lang.Unroll

import java.nio.file.Files

class KotlinSourceCodeParserTest extends Specification {

    def parser = new KotlinSourceCodeParser()

    @Unroll
    def "should get package information #testFile"(String testFile,
                                                   int publicObjects,
                                                   int packagePrivateObjects,
                                                   int protectedObjects,
                                                   int internalObjects,
                                                   int classes,
                                                   int abstractClasses,
                                                   int interfaces,
                                                   int enums,
                                                   int dataClasses,
                                                   int openClasses
    ) {
        given:
        def file = new File(getClass()
                .getClassLoader()
                .getResource("kotlin/${testFile}")
                .toURI())
        def information = new PackageInformation()

        when:
        parser.processSingleFile(Files.newBufferedReader(file.toPath()), information)

        then:
        information.publicObjects == publicObjects
        information.classes == classes
        information.internalObjects == internalObjects
        information.protectedObjects == protectedObjects
        information.interfaces == interfaces
        information.abstractClasses == abstractClasses
        information.enums == enums
        information.internalObjects == internalObjects

        where:
        testFile                      | publicObjects | packagePrivateObjects | protectedObjects | internalObjects | classes | abstractClasses | interfaces | enums | dataClasses | openClasses
        'open-class.txt'              | 1             | 0                     | 0                | 0               | 0       | 0               | 0          | 0     | 0           | 1
        'data-class.txt'              | 1             | 0                     | 0                | 0               | 0       | 0               | 0          | 0     | 1           | 0
        'internal-enum.txt'           | 0             | 0                     | 0                | 1               | 0       | 0               | 0          | 1     | 0           | 0
        'public-class.txt'            | 1             | 0                     | 0                | 0               | 1       | 0               | 0          | 0     | 0           | 0
        'public-interface.txt'        | 1             | 0                     | 0                | 0               | 0       | 0               | 1          | 0     | 0           | 0
        'internal-abstract-class.txt' | 0             | 0                     | 0                | 1               | 0       | 1               | 0          | 0     | 0           | 0
    }
}
