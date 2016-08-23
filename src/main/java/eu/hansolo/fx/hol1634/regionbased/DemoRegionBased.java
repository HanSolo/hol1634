/*
 * Copyright (c) 2016 by Gerrit Grunwald
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.hansolo.fx.hol1634.regionbased;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


/**
 * User: hansolo
 * Date: 12.08.16
 * Time: 12:10
 */
public class DemoRegionBased extends Application {
    private Led            control;
    private long           lastTimerCall;
    private AnimationTimer timer;


    @Override public void init() {
        control = new Led();
        control.setOn(true);
        control.setColor(Color.LIME);

        lastTimerCall = System.nanoTime();
        timer         = new AnimationTimer() {
            @Override public void handle(long now) {
                if (now > lastTimerCall + 500_000_000) {
                    control.setOn(!control.isOn());
                    lastTimerCall = now;
                }
            }
        };
    }

    @Override public void start(Stage stage) {
        StackPane pane = new StackPane(control);
        pane.setPadding(new Insets(20));

        Scene scene = new Scene(pane, 200, 200);
        //scene.getStylesheets().add(DemoRegionBased.class.getResource("styles.css").toExternalForm());

        stage.setTitle("Region Based Control");
        stage.setScene(scene);
        stage.show();

        timer.start();
    }

    @Override public void stop() {
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
