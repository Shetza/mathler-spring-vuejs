package fr.cnalps.squaregames;

public class Puzzle {
    private int targetValue;
    private String solution;
    private int difficulty;
    
    public Puzzle() {}
    
    public Puzzle(int targetValue, String solution, int difficulty) {
        this.targetValue = targetValue;
        this.solution = solution;
        this.difficulty = difficulty;
    }
    
    public int getTargetValue() { return targetValue; }
    public void setTargetValue(int targetValue) { this.targetValue = targetValue; }
    
    public String getSolution() { return solution; }
    public void setSolution(String solution) { this.solution = solution; }
    
    public int getDifficulty() { return difficulty; }
    public void setDifficulty(int difficulty) { this.difficulty = difficulty; }
}
