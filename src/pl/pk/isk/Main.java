package pl.pk.isk;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.awt.*;
import java.util.Optional;

public class Main extends Application{
    private TextField tfdEdgesNumber;
    private TextField tfdConnectionNumber;
    private TextField tfdEdgeFrom;
    private TextField tfdEdgeTo;
    private GraphGenerator graphGenerator;
    private UndirectedSparseMultigraph<CustomNode, CustomLink> graph;

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
        toolbar.setPadding(new Insets(0,10,0,0));

        Label lblEdgesNumber = new Label("Liczba wierzchłków: ");
        lblEdgesNumber.setPadding(new Insets(0,0,5,0));
        toolbar.add(lblEdgesNumber, 0, 0);
        tfdEdgesNumber = new TextField();
        tfdEdgesNumber.setMaxWidth(50);
        tfdEdgesNumber.setAlignment(Pos.CENTER);
        tfdEdgesNumber.setPadding(new Insets(0,0,5,0));
        toolbar.add(tfdEdgesNumber, 1, 0);

        Label lblConnectionNumber = new Label("Liczba krawędzi dla\nkażdego wierzchołka: ");
        lblConnectionNumber.setPadding(new Insets(0,0,5,0));
        toolbar.add(lblConnectionNumber, 0, 1);
        tfdConnectionNumber = new TextField();
        tfdConnectionNumber.setMaxWidth(50);
        tfdConnectionNumber.setAlignment(Pos.CENTER);
        tfdConnectionNumber.setPadding(new Insets(0,0,5,0));
        toolbar.add(tfdConnectionNumber, 1, 1);

        Button btnGenerateGraph = new Button("Generuj graf");
        btnGenerateGraph.setOnAction(event -> swingNode.setContent(createVisualizationViewer(tfdEdgesNumber.getText(), tfdConnectionNumber.getText())));
        btnGenerateGraph.setPadding(new Insets(5,5,5,5));
        toolbar.add(btnGenerateGraph, 0,2);

        Label lblFindPath = new Label("Wyszukaj ścieżkę: ");
        lblFindPath.setPadding(new Insets(0,0,5,0));
        toolbar.add(lblFindPath, 0, 3);

        Label lblPathFrom = new Label("Wierzchołek początkowy: ");
        lblPathFrom.setPadding(new Insets(0,0,5,0));
        toolbar.add(lblPathFrom, 0, 4);
        tfdEdgeFrom = new TextField();
        tfdEdgeFrom.setMaxWidth(50);
        tfdEdgeFrom.setAlignment(Pos.CENTER);
        tfdEdgeFrom.setPadding(new Insets(0,0,5,0));
        toolbar.add(tfdEdgeFrom, 1, 4);

        Label lblPathTo = new Label("Wierzchołek końcowy: ");
        lblPathTo.setPadding(new Insets(0,0,5,0));
        toolbar.add(lblPathTo, 0, 5);
        tfdEdgeTo = new TextField();
        tfdEdgeTo.setMaxWidth(50);
        tfdEdgeTo.setAlignment(Pos.CENTER);
        tfdEdgeTo.setPadding(new Insets(0,0,5,0));
        toolbar.add(tfdEdgeTo, 1, 5);

        Button btnSearchPath = new Button("Szukaj");
        btnSearchPath.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DijkstraShortestPath<CustomNode, CustomLink> alg = new DijkstraShortestPath(graph);
                System.out.println(alg.getPath(graphGenerator.getListOfNodes().get(0), graphGenerator.getListOfNodes().get(6)));
            }
        });
        toolbar.add(btnSearchPath, 0,6);

        GridPane panel = new GridPane();
        panel.setPadding(new Insets(10));
        panel.add(toolbar, 0, 0);
        panel.add(group, 1, 0);
        panel.setAlignment(Pos.TOP_LEFT);

        return new Scene(panel, 1024, 600);
    }

    private VisualizationViewer<CustomNode, CustomLink> createVisualizationViewer(String numberOfNodes, String numberOfConnection) {
        Integer nodes = checkStringAndConvertToInt(numberOfNodes);
        Integer connections = checkStringAndConvertToInt(numberOfConnection);
        if (nodes == null || nodes.intValue() < 2) {
            Dialogs.showError(Dialogs.Text.numberOfNodesLessThanTwo);
            return null;
        }

        graphGenerator = new GraphGenerator();
        graph = graphGenerator.generateGraph(nodes, connections);
        VisualizationViewer<CustomNode, CustomLink> visualizationViewer =
                new VisualizationViewer<>(new CircleLayout<>(graph), new Dimension(800, 600));
        visualizationViewer.setPreferredSize(new Dimension(800, 600));
        visualizationViewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        visualizationViewer.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());

        DefaultModalGraphMouse defaultModalGraphMouse = new DefaultModalGraphMouse();
        defaultModalGraphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
        visualizationViewer.setGraphMouse(defaultModalGraphMouse);
        return visualizationViewer;
    }

    private Integer checkStringAndConvertToInt(String string) {
        Optional<String> optional = Optional.of(string);
        if (optional.isPresent() && optional.get().length() > 0 && optional.get().matches("\\d+"))
            return Integer.valueOf(optional.get()).intValue();
        return null;
    }

}
