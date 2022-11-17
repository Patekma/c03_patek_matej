package fill;

import model.Edge;
import model.Point;
import model.Polygon;
import rasterize.LineRasterizer;
import rasterize.PolygonRasterizer;

import java.util.ArrayList;
import java.util.List;

public class ScanFiller implements Filler {
    private final LineRasterizer lineRasterizer;
    private final PolygonRasterizer polygonRasterizer;
    private final Polygon polygon;

    public ScanFiller(LineRasterizer lineRasterizer, PolygonRasterizer polygonRasterizer, Polygon polygon) {
        this.lineRasterizer = lineRasterizer;
        this.polygonRasterizer = polygonRasterizer;
        this.polygon = polygon;
    }

    @Override
    public void fill() {
        scanLine();
    }

    private void scanLine() {
        // init seznamu hran
        List<Edge> edges = new ArrayList<>();

        // projdu pointy a vytvořím z nich hrany
        for (int i = 0; i < polygon.getCount(); i++) {
            int nextIndex = (i + 1) % polygon.getCount();
            Point p1 = polygon.getPoint(i);
            Point p2 = polygon.getPoint(nextIndex);
            Edge edge = new Edge(p1.getX(), p1.getY(), p2.getX(), p2.getY());
            // Pokud je horizontální, ignoruju
            if (edge.isHorizontal())
                continue;

            // Přidám hranu do seznamu
            edges.add(edge);
        }

        // Najít yMin, yMax
        int yMin = polygon.getPoint(0).getY();
        int yMax = yMin;
        for (Point p : polygon.getPoints()) {
            // TODO: Najít yMin, yMax
        }

        // Pro Y od yMin po yMax
        for (int y = yMin; y <= yMax; y++) {
            // Vytvořit seznam průsečíků: List<Integer>
            List<Integer> prusecik = new ArrayList<>();
            // Projdu všechny hrany
            for (int i = 0; i < edges.size(); i++) {
                // Zjistim, jestli existuje průsečík s hranou

                // Pokud ano, spočítám a přidám do seznamu průsečíků
            }

            // Seřadit průsečíky
            // TODO: Setřídění průsečíků podle x, např. BubleSort

            // Vykreslit lines mezi lichými a sudými průsečíky
        }

        // Obkreslit polygon

    }
}
