package fr.cnalps.squaregames;

// import jakarta.validation.constraints.NotNull;
// import jakarta.validation.constraints.Size;
import java.util.List;

public class GuessRequest {
    
    // @NotNull(message = "La tentative ne peut pas être nulle")
    // @Size(min = 8, max = 8, message = "La tentative doit contenir exactement 8 caractères")
    private List<String> guess;
    
    // @NotNull(message = "La valeur cible ne peut pas être nulle")
    private Integer targetValue;
    
    public GuessRequest() {}
    
    public GuessRequest(List<String> guess, Integer targetValue) {
        this.guess = guess;
        this.targetValue = targetValue;
    }
    
    public List<String> getGuess() { return guess; }
    public void setGuess(List<String> guess) { this.guess = guess; }
    
    public Integer getTargetValue() { return targetValue; }
    public void setTargetValue(Integer targetValue) { this.targetValue = targetValue; }
}