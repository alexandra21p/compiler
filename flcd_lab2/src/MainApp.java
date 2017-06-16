
import controller.FiniteAutomatonController;
import controller.GrammarController;
import view.View;

/**
 * Created by Alexandra on 27/11/2016.
 */
public class MainApp {
    public static void main(String[] args){
        GrammarController grCtrl = new GrammarController();
        FiniteAutomatonController faCtrl = new FiniteAutomatonController();
        View view = new View(grCtrl, faCtrl);
        view.run();
    }
}
