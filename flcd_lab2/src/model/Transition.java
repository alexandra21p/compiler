package model;

/**
 * Created by Alexandra on 06/12/2016.
 */
public class Transition {
    String currentState;
    String symbol;

    public Transition(String currentState, String symbol) {
        this.currentState = currentState;
        this.symbol = symbol;
    }
    public String getCurrentState() { return currentState; }
    public String getSymbol() { return symbol; }

    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj instanceof Transition) {
            Transition t = (Transition)obj;
            return currentState.equals(t.currentState) && symbol.equals(t.symbol);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (currentState + symbol).hashCode();
    }
}
