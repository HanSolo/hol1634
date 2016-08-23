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

package eu.hansolo.fx.hol1634.cssbasics;

import javafx.geometry.VPos;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;


/**
 * User: hansolo
 * Date: 15.08.16
 * Time: 15:58
 */
public class AerithButton extends Region {
    private static final double WIDTH  = 116;
    private static final double HEIGHT = 35;
    private              Text   text;


    // ******************** Constructors **************************************
    public AerithButton() {
        this("");
    }
    public AerithButton(final String TEXT) {
        getStylesheets().add(AerithButton.class.getResource("aerith-button.css").toExternalForm());
        text = new Text(TEXT);
        init();
        initGraphics();
        registerListeners();
    }


    // ******************** Initialization ************************************
    private void init() {
        setPrefSize(WIDTH, HEIGHT);
        setMinSize(WIDTH, HEIGHT);
        setMaxSize(WIDTH, HEIGHT);
    }

    private void initGraphics() {
        getStyleClass().add("my-button");

        text.setTextOrigin(VPos.CENTER);
        text.getStyleClass().add("text");

        getChildren().setAll(text);
    }

    private void registerListeners() {
        widthProperty().addListener(o -> resize());
        heightProperty().addListener(o -> resize());
    }


    // ******************** Resizing ******************************************
    private void resize() {
        text.setX((WIDTH - text.getLayoutBounds().getWidth()) * 0.5);
        text.setY(HEIGHT * 0.5);
    }
}
