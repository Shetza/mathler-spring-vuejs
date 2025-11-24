package fr.cnalps.squaregames;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "mathler")
public class PuzzleProperties {
    
    private List<String> puzzles;
    private Equation equation = new Equation();
    
    public List<String> getPuzzles() { return puzzles; }
    public void setPuzzles(List<String> puzzles) { this.puzzles = puzzles; }
    
    public Equation getEquation() { return equation; }
    public void setEquation(Equation equation) { this.equation = equation; }
    
    public static class Equation {
        private int length = 8;
        
        public int getLength() { return length; }
        public void setLength(int length) { this.length = length; }
    }
}