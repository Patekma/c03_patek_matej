package fill;

import model.Edge;
import model.Line;
import model.Point;
import model.Polygon;
import rasterize.LineRasterizer;
import rasterize.PolygonRasterizer;
import transforms.Point2D;

import java.util.ArrayList;
import java.util.Collections;
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
            if (!edge.isHorizontal()) {
                edge.orientate();
                edges.add(edge);
            }
//            else {
//                edges.add(edge);
//            }
        }

        // Najít yMin, yMax
        int yMin = polygon.getPoint(0).getY();
        int yMax = yMin;
        for (Point p : polygon.getPoints()) {
            if (yMin > p.getY()) {
                yMin = p.getY();
            }
            if (yMax < p.getX()) {
                yMax = p.getX();
            }
        }

        // Pro Y od yMin po yMax
        for (int y = yMin; y <= yMax; y++) {

            List<Integer> prusecik = new ArrayList<>();

            for (Edge edge : edges) {
                if (edge.isIntersection(y)) {
                    prusecik.add(edge.getIntersection(y));
                }
            }

            Collections.sort(prusecik);

            for (int i = 0; i < prusecik.size(); i++) {
                lineRasterizer.rasterize(new Line(prusecik.get(i), y, prusecik.get(i+1), y));
            }
        }

    }

}
