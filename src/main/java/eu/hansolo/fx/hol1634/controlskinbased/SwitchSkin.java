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

import javafx.animation.TranslateTransition;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.util.Duration;


/**
 * Created by hansolo on 12.08.16.
 */
public class SwitchSkin extends SkinBase<CustomControl> implements Skin<CustomControl> {
    private static final double              PREFERRED_WIDTH  = 76;
    private static final double              PREFERRED_HEIGHT = 46;
    private              Region              switchBackground;
    private              Region              thumb;
    private              Pane                pane;
    private              TranslateTransition translate;


    // ******************** Constructors **************************************
    public SwitchSkin(final CustomControl CONTROL) {
        super(CONTROL);
        initGraphics();
        registerListeners();
    }


    // ******************** Initialization ************************************
    private void initGraphics() {
        switchBackground = new Region();
        switchBackground.getStyleClass().add("switch-background");
        switchBackground.setStyle(String.join("", "-color: ", getSkinnable().getColor().toString().replace("0x", "#"), ";"));

        thumb = new Region();
        thumb.getStyleClass().add("thumb");
        if (getSkinnable().isOn()) { thumb.setTranslateX(32); }

        translate = new TranslateTransition(Duration.millis(70), thumb);

        pane = new Pane(switchBackground, thumb);
        getChildren().add(pane);
    }

    private void registerListeners() {
        getSkinnable().colorProperty().addListener(o -> handleControlPropertyChanged("COLOR"));
        getSkinnable().onProperty().addListener(o -> handleControlPropertyChanged("ON") );
        thumb.setOnMousePressed(e -> getSkinnable().setOn(!getSkinnable().isOn()));
    }


    // ******************** Methods *******************************************
    @Override public void layoutChildren(final double X, final double Y, final double WIDTH, final double HEIGHT) {
        super.layoutChildren(X, Y, WIDTH, HEIGHT);
        switchBackground.relocate((WIDTH - PREFERRED_WIDTH) * 0.5, (HEIGHT - PREFERRED_HEIGHT) * 0.5);
        thumb.relocate((WIDTH - PREFERRED_WIDTH) * 0.5, (HEIGHT - PREFERRED_HEIGHT) * 0.5);
    }

    protected void handleControlPropertyChanged(final String PROPERTY) {
        if ("COLOR".equals(PROPERTY)) {
            switchBackground.setStyle(String.join("", "-color: ", getSkinnable().getColor().toString().replace("0x", "#"), ";"));
        } else if ("ON".equals(PROPERTY)) {
            if (getSkinnable().isOn()) {
                // move thumb to the right
                translate.setFromX(2);
                translate.setToX(32);
            } else {
                // move thumb to the left
                translate.setFromX(32);
                translate.setToX(2);
            }
            translate.play();
        }
    }
}
