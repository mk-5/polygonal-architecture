package io.polygonal.verifytask.verifiers;

import io.polygonal.verifytask.parsers.PackageParserFactory;

public class PackageVerifierFactory {
    public static final String JAVA = "java";
    public static final String KOTLIN = "kotlin";

    private PackageVerifierFactory() {

    }

    public static PackageVerifier forLanguage(String language) {
        switch (language) {
            case JAVA:
                return new BasicPackageVerifier(PackageParserFactory.forLanguage(language));
            case KOTLIN:
                return new KotlinPackageVerifier(PackageParserFactory.forLanguage(language));
            default:
                throw new IllegalArgumentException();
        }
    }
}
