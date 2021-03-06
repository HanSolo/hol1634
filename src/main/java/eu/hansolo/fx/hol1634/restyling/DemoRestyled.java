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

package eu.hansolo.fx.hol1634.restyling;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * User: hansolo
 * Date: 12.08.16
 * Time: 11:31
 */
public class DemoRestyled extends Application {
    private CheckBox        control0;
    private RestyledControl control1;

    @Override public void init() {
        control0 = new CheckBox("Material Design");
        control1 = new RestyledControl("Material Design");
    }

    @Override public void start(Stage stage) {
        VBox pane = new VBox(control0, control1);
        pane.setSpacing(24);
        pane.setPadding(new Insets(20));

        Scene scene = new Scene(pane);
        scene.getStylesheets().add(DemoRestyled.class.getResource("restyled.css").toExternalForm());

        stage.setTitle("Restyled Control");
        stage.setScene(scene);
        stage.show();
    }

    @Override public void stop() {
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
