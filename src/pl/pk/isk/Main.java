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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.apache.commons.collections15.Transformer;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class Main extends Application{
    private TextField tfdEdgesNumber;
    private TextField tfdConnectionNumber;
    private TextField tfdEdgeFrom;
    private TextField tfdEdgeTo;
    private GraphGenerator graphGenerator;
    private UndirectedSparseMultigraph<CustomNode, CustomLink> graph;
    private List<CustomLink> path;
    private VisualizationViewer<CustomNode, CustomLink> visualizationViewer;

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

        GridPane toolbar = createGeneratedGraph(swingNode);

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

    private GridPane createGeneratedGraph(SwingNode swingNode) {
        GridPane toolbar = new GridPane();
        toolbar.setPadding(new Insets(0,10,0,0));

        toolbar.add(ControlsFactory.createLabel("Liczba wierzchołków: "), 0, 0);
        tfdEdgesNumber = ControlsFactory.createTextField();
        tfdEdgesNumber.textProperty().addListener(Validators.createStringValidatorChangeListener(tfdEdgesNumber));
        toolbar.add(tfdEdgesNumber, 1, 0);

        toolbar.add(ControlsFactory.createLabel("Liczba krawędzi dla\nkażdego wierzchołka: "), 0, 1);
        tfdConnectionNumber = ControlsFactory.createTextField();
        tfdConnectionNumber.textProperty().addListener(Validators.createStringValidatorChangeListener(tfdConnectionNumber));
        toolbar.add(tfdConnectionNumber, 1, 1);

        Button btnGenerateGraph = new Button("Generuj graf");
        btnGenerateGraph.setOnAction(event -> swingNode.setContent(createVisualizationViewer(tfdEdgesNumber.getText(), tfdConnectionNumber.getText())));
        btnGenerateGraph.setPadding(new Insets(5,5,5,5));
        toolbar.add(btnGenerateGraph, 0,2);

        toolbar.add(ControlsFactory.createLabel("Wyszukaj ścieżkę: "), 0, 3);

        toolbar.add(ControlsFactory.createLabel("Wierzchołek początkowy: "), 0, 4);
        tfdEdgeFrom = ControlsFactory.createTextField();
        tfdEdgeFrom.textProperty().addListener(Validators.createStringValidatorChangeListener(tfdEdgeFrom));
        toolbar.add(tfdEdgeFrom, 1, 4);

        toolbar.add(ControlsFactory.createLabel("Wierzchołek końcowy: "), 0, 5);
        tfdEdgeTo = ControlsFactory.createTextField();
        tfdEdgeTo.textProperty().addListener(Validators.createStringValidatorChangeListener(tfdEdgeTo));
        toolbar.add(tfdEdgeTo, 1, 5);

        Button btnSearchPath = new Button("Szukaj");
        btnSearchPath.setOnAction(event -> {
            Transformer<CustomLink, Double> transformer = CustomLink::getWeight;
            DijkstraShortestPath<CustomNode, CustomLink> alg = new DijkstraShortestPath(graph, transformer);
            path = alg.getPath(graphGenerator.getListOfNodes().get(0), graphGenerator.getListOfNodes().get(1));

            System.out.println(path);
            if (visualizationViewer != null)
                visualizationViewer.repaint();
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
        if (Validators.validateInputValues(nodes, connections)) return null;

        graphGenerator = new GraphGenerator();
        graph = graphGenerator.generateGraph(nodes, connections);

        visualizationViewer = new VisualizationViewer<>(new CircleLayout<>(graph), new Dimension(800, 600));
        visualizationViewer.setPreferredSize(new Dimension(800, 600));
        visualizationViewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        visualizationViewer.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        visualizationViewer.getRenderContext().setArrowFillPaintTransformer(colorTransformer());
        visualizationViewer.getRenderContext().setArrowDrawPaintTransformer(colorTransformer());
        visualizationViewer.getRenderContext().setEdgeDrawPaintTransformer(colorTransformer());

        DefaultModalGraphMouse defaultModalGraphMouse = new DefaultModalGraphMouse();
        defaultModalGraphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
        visualizationViewer.setGraphMouse(defaultModalGraphMouse);
        return visualizationViewer;
    }

    private Transformer<CustomLink, Paint> colorTransformer() {
        return e -> {
            Long collect = null;
            if (path != null)
                collect = path.stream().map(i -> i.toString()).filter(i -> i.equals(e.toString())).collect(Collectors.counting());
            if (collect != null && collect > 0)
            {
                return Color.GREEN;
            }
            return Color.BLACK;
        };
    }

}
