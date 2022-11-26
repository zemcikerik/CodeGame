package dev.zemco.codegame;

/**
 * Entrypoint for fat jar deployed version of CodeGame.
 * <p>
 * While recommended approach for packaging Java applications as of Java 9 is using the module
 * system and tool called jlink, this approach doesn't support linking against automatically generated modules.
 * Currently, this application depends on library called RichTextFX for styleable editable text area used
 * for coding. At this moment, developers of this dependency provide only versions containing automatic modules,
 * which makes the recommended approach not usable by us.
 * <p>
 * As a workaround, we use Maven shade plugin for building the fat jar. This however introduces another issue,
 * where JavaFX checks if the {@code javafx.graphics} module is present when used main method is defined
 * in a class that extends {@link javafx.application.Application}. To circumvent this issue, this class was created
 * to launch the application when launching from a fat jar.
 * TODO: <a href="https://mail.openjdk.org/pipermail/openjfx-dev/2018-June/021977.html"></a>
 *
 * @author Erik Zemčík
 * @see CodeGameApplication
 */
public final class Launcher {

    /**
     * Entrypoint for CodeGame when launched from fat jar.
     * @param args arguments passed to CodeGame application during launch
     */
    public static void main(String[] args) {
        CodeGameApplication.main(args);
    }

    private Launcher() {
        // prevent instantiation of this class
    }

}
