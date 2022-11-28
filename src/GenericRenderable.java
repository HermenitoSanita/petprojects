import java.util.ArrayList;

public class GenericRenderable {
    String type = "";
    String id = "";
    String color = "";
    int xPos;
    int yPos;
    String shape = "";
    int value = 0;
    int width;
    int height;
    ArrayList<ArrayList<Integer>> logic = new ArrayList<>();
    ArrayList<String> connectedAsInput = new ArrayList<>();
    ArrayList<String> connectedAsOutput = new ArrayList<>();

    int inputCap = 0;
    int outputCap = 0;

    GenericRenderable(String type,String id,int inCap,int outCap ,ArrayList<ArrayList<Integer>> logic) {
        this.logic = logic;
        this.type = type;
        this.id = id;
        this.inputCap = inCap;
        this.outputCap = outCap;
        switch (this.type) {
            case "INPUT" -> {
                this.color = "GRAY";
                this.xPos = 100; //FIX! NOT ADAPTIVE
                this.yPos = 360; //FIX! NOT ADAPTIVE
                this.value = 0;
                this.shape = "CIRCLE";
                this.width = 30;
                this.height = 30;
            }
            case "OUTPUT" -> {
                this.color = "GRAY";
                this.xPos = 1300; //FIX! NOT ADAPTIVE
                this.yPos = 360; //FIX! NOT ADAPTIVE
                this.shape = "CIRCLE";
                this.width = 30;
                this.height = 30;
            }
            case "GATE" -> {
                this.xPos = 720; //FIX! NOT ADAPTIVE
                this.yPos = 360; //FIX! NOT ADAPTIVE
                this.shape = "RECT";
                this.height = 30;
                this.width = 50;
            }
            case "BUTTON" -> {

            }
        }
    }

}
