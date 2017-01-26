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

package eu.hansolo.fx.hol1634.extending;

import com.sun.javafx.scene.control.skin.TextFieldSkin;
import javafx.beans.InvalidationListener;
import javafx.scene.layout.Region;


/**
 * Created by hansolo on 19.08.16.
 */
public class SearchTextFieldSkin extends TextFieldSkin {
    private Region               loupe;
    private double               size;
    private InvalidationListener sizeListener;
    private InvalidationListener focusListener;


    // ******************** Constructors **************************************
    public SearchTextFieldSkin(final SearchTextField CONTROL){
        super(CONTROL);

        sizeListener  = o -> handleControlPropertyChanged("RESIZE");
        focusListener = o -> handleControlPropertyChanged("FOCUSED");

        initGraphics();
        registerListeners();
    }


    // ******************** Initialization ************************************
    private void initGraphics() {
        loupe = new Region();
        loupe.getStyleClass().add("loupe");
        loupe.setFocusTraversable(false);

        getChildren().addAll(loupe);
    }

    private void registerListeners() {
        getSkinnable().heightProperty().addListener(sizeListener);
        getSkinnable().focusedProperty().addListener(focusListener);
    }


    // ******************** Methods *******************************************
    @Override public void layoutChildren(final double X, final double Y, final double WIDTH, final double HEIGHT) {
        super.layoutChildren(X, Y, WIDTH, HEIGHT);
        size = loupe.getMaxWidth() < 0 ? 20.8 : loupe.getWidth();
        loupe.setTranslateX(-WIDTH * 0.5 + size * 0.25);
    }

    protected void handleControlPropertyChanged(final String PROPERTY) {
        if ("RESIZE".equals(PROPERTY)) {
            loupe.setMaxSize(getSkinnable().getHeight() * 0.8, getSkinnable().getHeight() * 0.8);
        } else if ("FOCUSED".equals(PROPERTY)) {
            loupe.setVisible(!getSkinnable().isFocused() && getSkinnable().getText().isEmpty());
        }
    }

    @Override public void dispose() {
        getSkinnable().heightProperty().removeListener(sizeListener);
        getSkinnable().focusedProperty().removeListener(focusListener);
        super.dispose();
    }
}
