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

package eu.hansolo.fx.hol1634.controlskinbased;

import eu.hansolo.fx.hol1634.controlskinbased.CustomControl.SkinType;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


/**
 * User: hansolo
 * Date: 12.08.16
 * Time: 12:14
 */
public class DemoControlSkinBased extends Application {
    private CustomControl  control0;
    private CustomControl  control1;
    private long           lastTimerCall;
    private AnimationTimer timer;


    @Override public void init() {
        control0 = new CustomControl();
        control0.setOn(true);
        control0.setPrefSize(100, 100);
        control0.setColor(Color.LIME);

        control1 = new CustomControl(SkinType.SWITCH);
        control1.setOn(true);
        control1.setColor(Color.web("#4bd865"));

        lastTimerCall = System.nanoTime();
        timer         = new AnimationTimer() {
            @Override public void handle(long now) {
                if (now > lastTimerCall + 500_000_000) {
                    control0.setOn(!control0.isOn());
                    lastTimerCall = now;
                }
            }
        };
    }

    @Override public void start(Stage stage) {
        VBox pane = new VBox(control0, control1);
        pane.setSpacing(20);
        pane.setPadding(new Insets(20));

        Scene scene = new Scene(pane, 200, 200);
        //scene.getStylesheets().add(DemoControlSkinBased.class.getResource("styles.css").toExternalForm());

        stage.setTitle("Control-Skin Based Control");
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
