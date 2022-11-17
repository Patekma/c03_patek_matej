import fill.Filler;
import fill.ScanFiller;
import fill.SeedFiller;
import model.Clip;
import model.Point;
import model.Polygon;
import rasterize.*;
import model.Line;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class Canvas {
    private final JFrame frame;
    private final JPanel panel;
    private RasterBufferImage raster;
    private LineRasterizer lineRasterizer;
    private Polygon polygon;
    private Polygon clipPolygon;
    private PolygonRasterizer polygonRasterizer;

    private TriangleRasterizer triangleRasterizer;

    private int x1;
    private int y1;
    private int x2;
    private int y2;

    //kontrola v jakem modu se program nachazi
    private boolean triangleMode = false;
    private boolean lineMode = false;
    private boolean clipMode = false;

    public Canvas(int width, int height) {
        frame = new JFrame();

        frame.setLayout(new BorderLayout());
        frame.setTitle("PGRF1");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        raster = new RasterBufferImage(800, 600);
        lineRasterizer = new LineRasterizerGraphics(raster);
        polygonRasterizer = new PolygonRasterizer(lineRasterizer);
        triangleRasterizer = new TriangleRasterizer(lineRasterizer);
        polygon = new Polygon();
        clipPolygon = new Polygon();

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                raster.present(g);
            }
        };

        panel.setPreferredSize(new Dimension(width, height));

        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        panel.requestFocus();
        panel.requestFocusInWindow();

        //vykreslení linky pomoci 2 bodu

        //press: zadani 1. bodu
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!lineMode) {
                    return;
                }
                if (e.getButton() == MouseEvent.BUTTON1) {
                    super.mousePressed(e);
                    raster.clear();
                    x1 = e.getX();
                    y1 = e.getY();
                }
            }

            //release: zadani 2. bodu
            @Override
            public void mouseReleased(MouseEvent e) {
                if (!lineMode) {
                    return;
                }
                if (e.getButton() == MouseEvent.BUTTON1) {
                    super.mouseReleased(e);
                    raster.clear();
                    x2 = e.getX();
                    y2 = e.getY();
                    Line line = new Line(x1, y1, x2, y2);
                    lineRasterizer.rasterize(line);
                    panel.repaint();
                }
            }
        });

        //vykreslovani táhnutim myši
        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!lineMode) {
                    return;
                }
                super.mouseDragged(e);
                raster.clear();
                Line line = new Line(x1, y1, e.getX(), e.getY());
                lineRasterizer.rasterize(line);
                panel.repaint();
            }
        });


        //polygon
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (triangleMode) {
                    return;
                }
                if (lineMode) {
                    return;
                }
                if (clipMode) {
                    return;
                }
                raster.clear();
                if (e.getButton() == MouseEvent.BUTTON1) {
                    polygon.addPoint(new Point(e.getX(), e.getY()));
                }
                if (polygon.getCount() >= 2) {
                    panel.addMouseMotionListener(new MouseAdapter() {
                        @Override
                        public void mouseDragged(MouseEvent e) {
                            super.mouseDragged(e);
                            if (triangleMode) {
                                return;
                            }
                            if (lineMode) {
                                return;
                            }
                            if (clipMode) {
                                return;
                            }
                            raster.clear();
                            for (int i = 0; i < polygon.getCount() - 1; i++) {
                                Line line1 = new Line(polygon.getPoint(i).getX(), polygon.getPoint(i).getY(), polygon.getPoint(i + 1).getX(), polygon.getPoint(i + 1).getY());
                                lineRasterizer.rasterize(line1);
                                Line line2 = new Line(polygon.getPoint(polygon.getCount() - 1).getX(), polygon.getPoint(polygon.getCount() - 1).getY(), e.getX(), e.getY());
                                lineRasterizer.rasterize(line2);
                                Line line3 = new Line(polygon.getPoint(0).getX(), polygon.getPoint(0).getY(), e.getX(), e.getY());
                                lineRasterizer.rasterize(line3);
                                panel.repaint();
                            }
                        }
                    });
                    polygonRasterizer.rasterize(polygon);
                    if (e.getButton() == MouseEvent.BUTTON2) {
                        Filler seedFiller = new SeedFiller(raster, e.getX(), e.getY(),
                                0xff0000,
                                Color.black.getRGB());
                        seedFiller.fill();
                    }
                    panel.repaint();
                }
            }
        });

        //funkce pomoci klaves
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);

                //prepinani na vykreslovani linky pomoci klavesy L
                if (e.getKeyCode() == KeyEvent.VK_L) {
                    triangleMode = false;
                    clipMode = false;
                    lineMode = !lineMode;
                    polygon.clearPoints();
                    raster.clear();
                    panel.repaint();
                    System.out.println("lineMode: " + lineMode);
                }

                //trojuhelnik pomoci klavesy T
                if (e.getKeyCode() == KeyEvent.VK_T) {
                    lineMode = false;
                    clipMode = false;
                    triangleMode = !triangleMode;
                    polygon.clearPoints();
                    raster.clear();
                    panel.repaint();
                    System.out.println("triangleMode: " + triangleMode);
                }

                //orezavani pomoci klavesy O
                if (e.getKeyCode() == KeyEvent.VK_O) {
                    lineMode = false;
                    triangleMode = false;
                    clipMode = !clipMode;
                    clipPolygon.clearPoints();
                    raster.clear();
                    panel.repaint();
                    polygonRasterizer.rasterize(polygon);
                    System.out.println("clipMode: " + clipMode);
                }

                //smazani platna pomoci klavesy C
                if (e.getKeyCode() == KeyEvent.VK_C) {
                    polygon.clearPoints();
                    clipPolygon.clearPoints();
                    raster.clear();
                    panel.repaint();
                }

                if (e.getKeyCode() == KeyEvent.VK_I) {
                    Clip clip = new Clip(polygon);
                    Polygon clippedPolygon = new Polygon(clip.clipPolygon(clipPolygon));
                    Filler scanFiller = new ScanFiller(lineRasterizer, polygonRasterizer, clippedPolygon);
                    scanFiller.fill();
                    panel.repaint();
                }
            }
        });

        //vykresleni rovnoramenneho trojuhelniku
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (!triangleMode) {
                    return;
                }
                raster.clear();
                if (e.getButton() == MouseEvent.BUTTON1) {
                    polygon.addPoint(new Point(e.getX(), e.getY()));
                }
                triangleRasterizer.rasterize(polygon);
                panel.repaint();
            }
        });

        //orezavaci trojuhelnik
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (!clipMode) {
                    return;
                }
                raster.clear();
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (clipPolygon.getCount() >= 3) {
                        return;
                    }
                    clipPolygon.addPoint(new Point(e.getX(), e.getY()));
                }
                if (clipPolygon.getCount() >= 2) {
                    polygonRasterizer.rasterize(clipPolygon);
                    polygonRasterizer.rasterize(polygon);
                    panel.repaint();
                }
            }
        });
    }

    public void start() {
        raster.clear();
        panel.repaint();
    }
}




