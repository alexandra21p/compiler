import controller.Controller;
import view.View;

/**
 * Created by Alexandra on 17/01/2017.
 */
public class MainApp {
    public static void main(String[] args){
        Controller grCtrl = new Controller();
        View view = new View(grCtrl);
        view.run();
    }
}
