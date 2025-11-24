package fr.cnalps.squaregames;

import java.util.List;

public class GuessResponse {
    
    private List<GameState> states;
    private boolean valid;
    private boolean solved;
    private String message;
    private Integer calculatedValue;
    
    public GuessResponse() {}
    
    public GuessResponse(List<GameState> states, boolean valid, boolean solved) {
        this.states = states;
        this.valid = valid;
        this.solved = solved;
    }
    
    public List<GameState> getStates() { return states; }
    public void setStates(List<GameState> states) { this.states = states; }
    
    public boolean isValid() { return valid; }
    public void setValid(boolean valid) { this.valid = valid; }
    
    public boolean isSolved() { return solved; }
    public void setSolved(boolean solved) { this.solved = solved; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public Integer getCalculatedValue() { return calculatedValue; }
    public void setCalculatedValue(Integer calculatedValue) { this.calculatedValue = calculatedValue; }
}