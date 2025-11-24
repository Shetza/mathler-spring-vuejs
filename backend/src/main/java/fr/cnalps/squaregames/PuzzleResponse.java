package fr.cnalps.squaregames;

public class PuzzleResponse {
    
    private int targetValue;
    private int difficulty;
    private int equationLength;
    
    public PuzzleResponse() {}
    
    public PuzzleResponse(int targetValue, int difficulty, int equationLength) {
        this.targetValue = targetValue;
        this.difficulty = difficulty;
        this.equationLength = equationLength;
    }
    
    public int getTargetValue() { return targetValue; }
    public void setTargetValue(int targetValue) { this.targetValue = targetValue; }
    
    public int getDifficulty() { return difficulty; }
    public void setDifficulty(int difficulty) { this.difficulty = difficulty; }
    
    public int getEquationLength() { return equationLength; }
    public void setEquationLength(int equationLength) { this.equationLength = equationLength; }
}