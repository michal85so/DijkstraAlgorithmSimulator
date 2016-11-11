package pl.pk.isk;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.awt.*;

public class Main extends Application{
    private TextField tfdEdgesNumber;

    public static void main(String[] args) { launch(args);}

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Dijkstra Algorithm Simulator - Author Michał Policht");
        primaryStage.setScene(createScene());
        primaryStage.show();
    }

    private Scene createScene() {
        SwingNode swingNode = new SwingNode();

        Group group = new Group();
        group.getChildren().add(swingNode);

        GridPane toolbar = new GridPane();

        toolbar.add(new Label("Podaj liczbę wierzchłków: "), 0, 0);

        tfdEdgesNumber = new TextField();
        tfdEdgesNumber.setMaxWidth(50);
        toolbar.add(tfdEdgesNumber, 1, 0);

        Button btnGenerateGraph = new Button("Generuj graf");
        btnGenerateGraph.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                swingNode.setContent(createVisualizationViewer());
            }
        });
        toolbar.add(btnGenerateGraph, 0,1);

        BorderPane panel = new BorderPane();
        panel.setRight(toolbar);
        panel.getChildren().add(group);

        return new Scene(panel, 1024, 600);
    }

    private VisualizationViewer<CustomNode, CustomLink> createVisualizationViewer() {
        GraphGenerator graphGenerator = new GraphGenerator();
        VisualizationViewer<CustomNode, CustomLink> visualizationViewer =
                new VisualizationViewer<>(new CircleLayout<>(graphGenerator.generateGraph(10, 3)), new Dimension(800, 600));
        visualizationViewer.setPreferredSize(new Dimension(800, 600));
        visualizationViewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        visualizationViewer.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());

        DefaultModalGraphMouse defaultModalGraphMouse = new DefaultModalGraphMouse();
        defaultModalGraphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
        visualizationViewer.setGraphMouse(defaultModalGraphMouse);
        return visualizationViewer;
    }

}
