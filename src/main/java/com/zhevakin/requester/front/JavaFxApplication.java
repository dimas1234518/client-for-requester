package com.zhevakin.requester.front;

import com.zhevakin.requester.SpringBootExampleApplication;
import com.zhevakin.requester.front.controller.EnvironmentController;
import com.zhevakin.requester.front.controller.MainController;
import com.zhevakin.requester.front.generator.GridGenerator;
import com.zhevakin.requester.front.init.InitController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class JavaFxApplication extends Application {

    private ConfigurableApplicationContext applicationContext;

    private final String APPLICATION_NAME = "Requester";

    @Override
    public void init() {
        String[] args = getParameters().getRaw().toArray(new String[0]);

        this.applicationContext = new SpringApplicationBuilder()
                .sources(SpringBootExampleApplication.class)
                .run(args);
    }

    @Override
    public void start(Stage stage) {

        FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
        MainController controller = fxWeaver.loadController(MainController.class);
        EnvironmentController environmentController = fxWeaver.loadController(EnvironmentController.class);
        GridGenerator gridGenerator = applicationContext.getBean(GridGenerator.class);
        Parent root = fxWeaver.loadView(controller.getClass());
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(APPLICATION_NAME);
        stage.setMaximized(true);
        stage.show();
        controller.setEnvironmentController(environmentController);
        controller.setFxWeaver(fxWeaver);
        InitController.start(controller, gridGenerator, environmentController);
        controller.start();
    }

    @Override
    public void stop() {
        this.applicationContext.close();
        Platform.exit();
    }

}
