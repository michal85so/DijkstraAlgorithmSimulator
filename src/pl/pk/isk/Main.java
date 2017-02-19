package pl.pk.isk;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.apache.commons.collections15.Transformer;

import java.awt.*;

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

        GridPane toolbar = getGeneratedGraph(swingNode);

        GridPane panel = new GridPane();
        panel.setPadding(new Insets(10));
        panel.add(toolbar, 0, 0);
        panel.add(group, 1, 0);
        panel.setAlignment(Pos.TOP_LEFT);

        TabPane tabPane = new TabPane();

        Tab generateTab = new Tab();
        generateTab.setText("Generator");
        generateTab.setContent(panel);

        Tab editorTab = new Tab();
        editorTab.setText("Edytor");

        tabPane.getTabs().addAll(generateTab, editorTab);

        return new Scene(tabPane, 1024, 700);
    }

    private GridPane getGeneratedGraph(SwingNode swingNode) {
        GridPane toolbar = new GridPane();
        toolbar.setPadding(new Insets(0,10,0,0));

        Label lblEdgesNumber = new Label("Liczba wierzchołków: ");
        lblEdgesNumber.setPadding(new Insets(0,0,5,0));
        toolbar.add(lblEdgesNumber, 0, 0);
        tfdEdgesNumber = ControlsFactory.createTextField();
        tfdEdgesNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*"))
                tfdEdgesNumber.setText(newValue.replaceAll("[^\\d]", ""));
        });
        toolbar.add(tfdEdgesNumber, 1, 0);

        Label lblConnectionNumber = new Label("Liczba krawędzi dla\nkażdego wierzchołka: ");
        lblConnectionNumber.setPadding(new Insets(0,0,5,0));
        toolbar.add(lblConnectionNumber, 0, 1);
        tfdConnectionNumber = ControlsFactory.createTextField();
        tfdConnectionNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*"))
                tfdConnectionNumber.setText(newValue.replaceAll("[^\\d]", ""));
        });
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
        tfdEdgeFrom = ControlsFactory.createTextField();
        toolbar.add(tfdEdgeFrom, 1, 4);

        Label lblPathTo = new Label("Wierzchołek końcowy: ");
        lblPathTo.setPadding(new Insets(0,0,5,0));
        toolbar.add(lblPathTo, 0, 5);
        tfdEdgeTo = ControlsFactory.createTextField();
        toolbar.add(tfdEdgeTo, 1, 5);

        Button btnSearchPath = new Button("Szukaj");
        btnSearchPath.setOnAction(event -> {
            Transformer<CustomLink, Double> transformer = CustomLink::getWeight;
            DijkstraShortestPath<CustomNode, CustomLink> alg = new DijkstraShortestPath(graph, transformer);
            System.out.println(alg.getPath(graphGenerator.getListOfNodes().get(0), graphGenerator.getListOfNodes().get(6)));
        });
        btnSearchPath.setPadding(new Insets(5,5,5,5));
        toolbar.add(btnSearchPath, 0,6);
        return toolbar;
    }

    private GridPane getPaintedGraph() {
        return null;
    }

    private VisualizationViewer<CustomNode, CustomLink> createVisualizationViewer(String numberOfNodes, String numberOfConnection) {
        Integer nodes = Converters.checkStringAndConvertToInt(numberOfNodes);
        Integer connections = Converters.checkStringAndConvertToInt(numberOfConnection);
        if (validateInputValues(nodes, connections)) return null;

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

    private boolean validateInputValues(Integer nodes, Integer connections) {
        if (nodes == null || nodes.intValue() < 2) {
            Dialogs.showError(Dialogs.Text.numberOfNodesLessThanTwo);
            return true;
        }
        if (connections == null || connections < 1 ||  connections >= nodes) {
            Dialogs.showError(Dialogs.Text.wrongNumberOfConnections);
            return true;
        }
        return false;
    }

}
