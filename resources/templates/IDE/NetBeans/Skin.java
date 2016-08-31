<#if package?? && package != "">
package ${package};

import javafx.geometry.Insets;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;


/**
 * User: ${user}
 * Date: ${date}
 * Time: ${time}
 */
public class ${name} extends SkinBase<CONTROL_CLASS implements Skin<CONTROL_CLASS> {
    private static final double  PREFERRED_WIDTH  = 250;
    private static final double  PREFERRED_HEIGHT = 250;
    private static final double  MINIMUM_WIDTH    = 50;
    private static final double  MINIMUM_HEIGHT   = 50;
    private static final double  MAXIMUM_WIDTH    = 1024;
    private static final double  MAXIMUM_HEIGHT   = 1024;
    private static       double  aspectRatio;
    private              boolean keepAspect;
    private              double  size;
    private              double  width;
    private              double  height;
    private              Region  node;
    private              Pane    pane;
    private              Paint   backgroundPaint;
    private              Paint   borderPaint;
    private              double  borderWidth;


    // ******************** Constructors **************************************
    public ${name}(final CONTROL_CLASS CONTROL) {
        super(CONTROL);
        aspectRatio     = PREFERRED_HEIGHT / PREFERRED_WIDTH;
        keepAspect      = true;
        backgroundPaint = Color.TRANSPARENT;
        borderPaint     = Color.TRANSPARENT;
        borderWidth     = 0d;
        initGraphics();
        registerListeners();
    }


    // ******************** Initialization ************************************
    private void initGraphics() {
        if (Double.compare(getSkinnable().getPrefWidth(), 0.0) <= 0 || Double.compare(getSkinnable().getPrefHeight(), 0.0) <= 0 ||
            Double.compare(getSkinnable().getWidth(), 0.0) <= 0 || Double.compare(getSkinnable().getHeight(), 0.0) <= 0) {
            if (getSkinnable().getPrefWidth() > 0 && getSkinnable().getPrefHeight() > 0) {
                getSkinnable().setPrefSize(getSkinnable().getPrefWidth(), getSkinnable().getPrefHeight());
            } else {
                getSkinnable().setPrefSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);
            }
        }

        node = new Region();
        node.getStyleClass().setAll("node");

        pane = new Pane(node);
        pane.setBackground(new Background(new BackgroundFill(backgroundPaint, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setBorder(new Border(new BorderStroke(borderPaint, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(borderWidth))));

        getChildren().setAll(pane);
    }

    private void registerListeners() {
        getSkinnable().widthProperty().addListener(o -> handleControlPropertyChanged("RESIZE") );
        getSkinnable().heightProperty().addListener(o -> handleControlPropertyChanged("RESIZE") );
        getSkinnable().colorProperty().addListener(o -> handleControlPropertyChanged("COLOR"));
        //getSkinnable().onProperty().addListener(o -> handleControlPropertyChanged("ON") );
        //getSkinnable().valueProperty().addListener(o -> handleControlPropertyChanged("VALUE"));
    }


    // ******************** Methods *******************************************
    @Override public void layoutChildren(final double X, final double Y, final double WIDTH, final double HEIGHT) {
        super.layoutChildren(X, Y, WIDTH, HEIGHT);
    }

    @Override protected double computeMinWidth(final double HEIGHT, final double TOP, final double RIGHT, final double BOTTOM, final double LEFT)  { return MINIMUM_WIDTH; }
    @Override protected double computeMinHeight(final double WIDTH, final double TOP, final double RIGHT, final double BOTTOM, final double LEFT)  { return MINIMUM_HEIGHT; }
    @Override protected double computePrefWidth(final double HEIGHT, final double TOP, final double RIGHT, final double BOTTOM, final double LEFT) { return super.computePrefWidth(HEIGHT, TOP, RIGHT, BOTTOM, LEFT); }
    @Override protected double computePrefHeight(final double WIDTH, final double TOP, final double RIGHT, final double BOTTOM, final double LEFT) { return super.computePrefHeight(WIDTH, TOP, RIGHT, BOTTOM, LEFT); }
    @Override protected double computeMaxWidth(final double HEIGHT, final double TOP, final double RIGHT, final double BOTTOM, final double LEFT)  { return MAXIMUM_WIDTH; }
    @Override protected double computeMaxHeight(final double WIDTH, final double TOP, final double RIGHT, final double BOTTOM, final double LEFT)  { return MAXIMUM_HEIGHT; }

    protected void handleControlPropertyChanged(final String PROPERTY) {
        if ("RESIZE".equals(PROPERTY)) {
            resize();
        } else if ("COLOR".equals(PROPERTY)) {
            node.setStyle(String.join("", "-color: ", (getSkinnable().getColor()).toString().replace("0x", "#"), ";"));
            resize();
        } else if ("ON".equals(PROPERTY)) {
            
        } else if ("VALUE".equals(PROPERTY)) {

        }
    }


    // ******************** Private Methods ***********************************
    private void resize() {
        width  = getSkinnable().getWidth() - getSkinnable().getInsets().getLeft() - getSkinnable().getInsets().getRight();
        height = getSkinnable().getHeight() - getSkinnable().getInsets().getTop() - getSkinnable().getInsets().getBottom();
        size   = width < height ? width : height;

        if (width > 0 && height > 0) {
            // Use for square controls where width == height
            pane.setMaxSize(size, size);
            pane.setPrefSize(size, size);
            pane.relocate((getSkinnable().getWidth() - size) * 0.5, (getSkinnable().getHeight() - size) * 0.5);

            // Use for rectangular controls width != height
            pane.setMaxSize(width, height);
            pane.setPrefSize(width, height);
            pane.relocate((getSkinnable().getWidth() - width) * 0.5, (getSkinnable().getHeight() - height) * 0.5);

            node.setPrefSize(size * 0.5, size * 0.5);

            redraw();
        }
    }

    private void redraw() {
        pane.setBackground(new Background(new BackgroundFill(backgroundPaint, new CornerRadii(1024), Insets.EMPTY)));
        pane.setBorder(new Border(new BorderStroke(borderPaint, BorderStrokeStyle.SOLID, new CornerRadii(1024), new BorderWidths(borderWidth / PREFERRED_WIDTH * size))));
    }
}
