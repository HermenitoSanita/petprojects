import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class RendererCanvas extends JPanel implements MouseInputListener {
    ArrayList<GenericRenderable> renderList = new ArrayList<>();
    String selected1 = "Nothing";
    String selected2 = "Nothing";
    ArrayList<Integer> logicPattern = new ArrayList<>();

    RendererCanvas() {
        addMouseListener(this);
        addMouseMotionListener(this);
    }


    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;

        this.logicPattern = new ArrayList<>();

        this.renderList.stream().filter(v -> v.type.equals("OUTPUT")).forEach(j -> {
            if (j.connectedAsInput.size() == 1) {
                this.renderList.stream().filter(n -> n.id.equals(j.connectedAsInput.get(0))).forEach(l -> {
                    System.out.println("we are here");
                    l.connectedAsInput.stream().forEach(k -> {
                        this.renderList.stream().filter(h -> h.id.equals(k)).forEach(o -> {
                            this.logicPattern.add(o.value);
                            if (this.logicPattern.size() >= 2) {this.logicPattern = new ArrayList<>(Arrays.asList(this.logicPattern.get(this.logicPattern.size()-2),this.logicPattern.get(this.logicPattern.size()-1)));}
                            System.out.println(this.logicPattern.size());
                            System.out.println(this.logicPattern.toString());
                            j.value = evaluateGate(this.logicPattern,l.logic);
                        });
                    });

                });

            } else if (j.connectedAsInput.size() > 1) {
                j.connectedAsInput = new ArrayList<>(Arrays.asList(j.connectedAsInput.get(j.connectedAsInput.size()-1)));
                this.logicPattern.clear();
            }
        });

        this.logicPattern = new ArrayList<>();

        this.renderList.stream().filter(v -> v.type.equals("GATE")).forEach(j -> {
            if (j.connectedAsInput.size() == j.inputCap && j.connectedAsInput.size() > 0) {
                this.renderList.stream().filter(n -> n.id.equals(j.connectedAsInput.get(0))).forEach(l -> {
                    System.out.println("we are here");
                    l.connectedAsInput.stream().forEach(k -> {
                        this.renderList.stream().filter(h -> h.id.equals(k)).forEach(o -> {
                            this.logicPattern.add(o.value);
                            if (this.logicPattern.size() >= j.inputCap) {this.logicPattern = new ArrayList<>(Arrays.asList(this.logicPattern.get(this.logicPattern.size()-2),this.logicPattern.get(this.logicPattern.size()-1)));}
                            System.out.println(this.logicPattern.size());
                            System.out.println(this.logicPattern.toString());
                            j.value = evaluateGate(this.logicPattern,l.logic);
                        });
                    });

                });

            }
        });

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        this.renderList.stream().filter(v -> v.shape.equals("CIRCLE")).forEach(l -> {
            g2.setColor(Color.GRAY);
            if (l.value == 1) {
                g2.fillArc(l.xPos - l.width/2,l.yPos - l.height/2,l.width,l.height,0,360);
            } else if (l.value == 0) {
                g2.drawArc(l.xPos - l.width/2,l.yPos - l.height/2,l.width,l.height,0,360);
            }
        });
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        this.renderList.stream().filter(v -> v.shape.equals("RECT")).forEach(l -> {
            switch (l.color) {
                case "GREEN" -> g2.setColor(Color.GREEN);
                case "BLUE" -> g2.setColor(Color.BLUE);
                case "RED" -> g2.setColor(Color.RED);
                case "PINK" -> g2.setColor(Color.PINK);
                case "ORANGE" -> g2.setColor(Color.ORANGE);
                default -> g2.setColor(Color.BLACK);
            }
            g2.fillRect(l.xPos - l.width/2,l.yPos - l.height/2,l.width,l.height);
            g2.setColor(Color.WHITE);
            g2.drawString(l.id,l.xPos - l.width/2,l.yPos + l.height/3);
        });
        g2.setColor(Color.BLACK);
        g2.drawString(this.selected1 + " is selected as an input!" , 0 , 30);
        if (!this.selected1.equals("Nothing") && this.selected2.equals("Nothing")) {
          g2.drawString("Now select another component to wire",0,60);
        } else if (!this.selected1.equals("Nothing") && !this.selected2.equals("Nothing")) {
          g2.drawString(this.selected2 + " is selected and " + this.selected1 + " and " + this.selected2 +" are wired hopefully .D", 0 , 60);
        }
        this.renderList.stream().filter(v -> v.connectedAsInput.size() != 0).forEach(l -> l.connectedAsInput.forEach(m -> this.renderList.stream().filter(n -> n.id.equals(m)).forEach(j -> g2.drawLine(l.xPos,l.yPos , j.xPos , j.yPos ))));

    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point p = e.getPoint();
        this.renderList.stream().filter(s -> s.type.equals("INPUT") || s.type.equals("OUTPUT") || s.type.equals("GATE")).forEach(v -> {
            if ((p.x < v.xPos + v.width/2 && p.x > v.xPos - v.width/2) && (p.y < v.yPos + v.height/2 && p.y > v.yPos - v.height/2)) {
                if (v.type.equals("INPUT")) {
                    if (v.value == 0) {
                        v.value = 1;
                    } else { //v.value == 1
                        v.value = 0;
                    }
                }
                if (!this.selected1.equals("Nothing")) {
                    this.selected2 = v.id;
                } else {
                    this.selected1 = v.id;
                }
                if (!this.selected2.equals("Nothing") && !this.selected1.equals("Nothing")) {
                    this.renderList.stream().filter(k -> k.id.equals(this.selected1)).forEach(l -> this.renderList.stream().filter(k -> k.id.equals(this.selected2)).forEach(m -> {
                        if ((m.type.equals("GATE") & l.type.equals("GATE"))  || (l.type.equals("INPUT") & m.type.equals("GATE")) || (l.type.equals("GATE") & m.type.equals("OUTPUT"))) {
                            if (!m.id.equals(l.id)) {
                                l.connectedAsOutput.add(m.id);
                                m.connectedAsInput.add(l.id);
                            }
                        }
                    }));
                    this.selected1 = "Nothing";
                    this.selected2 = "Nothing";
                }
                revalidate();
                repaint();
            }
        });
    }

    public void mouseClicked(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    public void mouseDragged(MouseEvent e) {
        Point p = e.getPoint();
        this.renderList.stream().filter(s -> s.type.equals("INPUT") || s.type.equals("OUTPUT") || s.type.equals("GATE")).forEach(v -> {
            if ((p.x < v.xPos + v.width/2 && p.x > v.xPos - v.width/2) && (p.y < v.yPos + v.height/2 && p.y > v.yPos - v.height/2)) {
                v.xPos = p.x;
                v.yPos = p.y;
            }
        });
        revalidate();
        repaint();
    }

    public void mouseMoved(MouseEvent e) {}

    public static int evaluateGate(ArrayList<Integer> inputs ,ArrayList<ArrayList<Integer>> logic) {
        System.out.println("we reach here also");
        System.out.println(inputs.toString());
        System.out.println(logic.toString());
        for (int i = 0; i < logic.size(); i++) {
            boolean flag = true;
            for (int j = 0; j < inputs.size(); j++) {
                if (!inputs.get(j).equals(logic.get(i).get(j))) {
                    flag = false;
                }
            }
            if (flag) {
                return logic.get(i).get(inputs.size());
            }
        }
        return 0;
    }

}
