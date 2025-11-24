package fr.cnalps.squaregames;

import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class MathlerService {
    
    private final List<Puzzle> puzzles;
    private final int equationLength;
    private final ScriptEngine scriptEngine;
    private Puzzle currentPuzzle;
    
    public MathlerService(PuzzleProperties properties) {
        this.puzzles = loadPuzzles(properties.getPuzzles());
        this.equationLength = properties.getEquation().getLength();
        this.scriptEngine = new ScriptEngineManager().getEngineByName("JavaScript");
        
        if (puzzles.isEmpty()) {
            throw new IllegalStateException("Aucun puzzle configuré !");
        }
    }
    
    private List<Puzzle> loadPuzzles(List<String> puzzleStrings) {
        List<Puzzle> loaded = new ArrayList<>();
        
        for (String puzzleStr : puzzleStrings) {
            try {
                String[] parts = puzzleStr.split(":");
                if (parts.length == 3) {
                    int targetValue = Integer.parseInt(parts[0]);
                    String solution = parts[1];
                    int difficulty = Integer.parseInt(parts[2]);
                    
                    if (solution.length() != 8) {
                        System.err.println("Solution invalide (longueur != 8): " + solution);
                        continue;
                    }
                    
                    loaded.add(new Puzzle(targetValue, solution, difficulty));
                }
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement du puzzle: " + puzzleStr);
            }
        }
        
        return loaded;
    }
    
    public PuzzleResponse getRandomPuzzle() {
        Random random = new Random();
        currentPuzzle = puzzles.get(random.nextInt(puzzles.size()));
        
        return new PuzzleResponse(
            currentPuzzle.getTargetValue(),
            currentPuzzle.getDifficulty(),
            equationLength
        );
    }
    
    public PuzzleResponse getPuzzleByDifficulty(int difficulty) {
        List<Puzzle> filtered = puzzles.stream()
            .filter(p -> p.getDifficulty() == difficulty)
            .toList();
        
        if (filtered.isEmpty()) {
            return getRandomPuzzle();
        }
        
        Random random = new Random();
        currentPuzzle = filtered.get(random.nextInt(filtered.size()));
        
        return new PuzzleResponse(
            currentPuzzle.getTargetValue(),
            currentPuzzle.getDifficulty(),
            equationLength
        );
    }
    
    public GuessResponse checkGuess(GuessRequest request) {
        GuessResponse response = new GuessResponse();
        
        // Vérifier que le puzzle actuel correspond à la cible
        if (currentPuzzle == null || currentPuzzle.getTargetValue() != request.getTargetValue()) {
            // Chercher le puzzle correspondant
            currentPuzzle = puzzles.stream()
                .filter(p -> p.getTargetValue() == request.getTargetValue())
                .findFirst()
                .orElse(null);
            
            if (currentPuzzle == null) {
                response.setValid(false);
                response.setMessage("Aucun puzzle trouvé pour la valeur cible: " + request.getTargetValue());
                response.setStates(Collections.nCopies(equationLength, GameState.ABSENT));
                return response;
            }
        }
        
        // Convertir la liste en string
        String guessStr = String.join("", request.getGuess());
        
        // Évaluer l'équation
        Integer calculatedValue = evaluateEquation(guessStr);
        response.setCalculatedValue(calculatedValue);
        
        // Vérifier si l'équation est valide
        if (calculatedValue == null) {
            response.setValid(false);
            response.setMessage("Équation invalide ou non calculable");
            response.setStates(Collections.nCopies(equationLength, GameState.ABSENT));
            return response;
        }
        
        // Vérifier si le résultat correspond à la cible
        if (!calculatedValue.equals(request.getTargetValue())) {
            response.setValid(false);
            response.setMessage("L'équation donne " + calculatedValue + ", pas " + request.getTargetValue());
            response.setStates(Collections.nCopies(equationLength, GameState.ABSENT));
            return response;
        }
        
        // Calculer les états de chaque caractère
        List<GameState> states = calculateStates(request.getGuess(), currentPuzzle.getSolution());
        response.setStates(states);
        response.setValid(true);
        
        // Vérifier si le puzzle est résolu
        boolean solved = states.stream().allMatch(state -> state == GameState.CORRECT);
        response.setSolved(solved);
        
        if (solved) {
            response.setMessage("Bravo ! Puzzle résolu !");
        }
        
        return response;
    }
    
    private Integer evaluateEquation(String equation) {
        try {
            // Remplacer × par * pour l'évaluation
            String normalized = equation.replace("×", "*");
            
            // Vérifier que l'équation ne contient que des caractères autorisés
            if (!normalized.matches("^[0-9+\\-*/\\s]+$")) {
                return null;
            }
            
            // Évaluer l'expression
            Object result = scriptEngine.eval(normalized);
            
            if (result instanceof Number) {
                return ((Number) result).intValue();
            }
            
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    
    private List<GameState> calculateStates(List<String> guess, String solution) {
        List<GameState> states = new ArrayList<>(Collections.nCopies(equationLength, null));
        char[] solutionArray = solution.toCharArray();
        boolean[] solutionUsed = new boolean[equationLength];
        boolean[] guessProcessed = new boolean[equationLength];
        
        // Premier passage : marquer les CORRECT (bonne position)
        for (int i = 0; i < guess.size(); i++) {
            if (guess.get(i).equals(String.valueOf(solutionArray[i]))) {
                states.set(i, GameState.CORRECT);
                solutionUsed[i] = true;
                guessProcessed[i] = true;
            }
        }
        
        // Deuxième passage : marquer les PRESENT (existe ailleurs)
        for (int i = 0; i < guess.size(); i++) {
            if (guessProcessed[i]) continue;
            
            boolean found = false;
            for (int j = 0; j < solutionArray.length; j++) {
                if (!solutionUsed[j] && guess.get(i).equals(String.valueOf(solutionArray[j]))) {
                    states.set(i, GameState.PRESENT);
                    solutionUsed[j] = true;
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                states.set(i, GameState.ABSENT);
            }
        }
        
        return states;
    }
    
    public List<Puzzle> getAllPuzzles() {
        return new ArrayList<>(puzzles);
    }
}