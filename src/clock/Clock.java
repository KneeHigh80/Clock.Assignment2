package clock;

/**
 * The main class of the Clock application.
 */
public class Clock {
    
    /**
     * The main method to start the application.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Model model = new Model();
        View view = new View(model);
        model.addObserver(view);
        Controller controller = new Controller(model, view);
    }
}