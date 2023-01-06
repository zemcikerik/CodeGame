package dev.zemco.codegame;

import dev.zemco.codegame.presentation.IViewProvider;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class of the CodeGame application handling the application launch.
 *
 * @author Erik Zemčík
 * @see Bootstrap
 */
public class CodeGameApplication extends Application {

    private static final String APP_TITLE = "CodeGame";
    private static final String INITIAL_VIEW_ID = "problem-list";

    private static final int DEFAULT_WIDTH = 640;
    private static final int DEFAULT_HEIGHT = 360;

    private static final int MIN_WIDTH = 470;
    private static final int MIN_HEIGHT = 240;

    /**
     * Entrypoint for CodeGame that launches the application.
     * @param args arguments passed to CodeGame application during launch
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initializes the application and presents the initial view to the user.
     * @param primaryStage primary stage of the application
     */
    @Override
    public void start(Stage primaryStage) {
        IViewProvider viewProvider = Bootstrap.bootstrap(CodeGameApplication.class, primaryStage);

        Parent problemListView = viewProvider.getViewById(INITIAL_VIEW_ID);
        Scene scene = new Scene(problemListView, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        scene.getStylesheets().add("/css/styles.css");

        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);

        primaryStage.setScene(scene);
        primaryStage.setTitle(APP_TITLE);
        primaryStage.show();
    }

}
