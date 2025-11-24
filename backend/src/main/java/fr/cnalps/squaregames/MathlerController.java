package fr.cnalps.squaregames;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MathlerController 
{    
    @PostMapping("/check")
    public String[] checkGuess(@RequestBody GuessRequest request) {
        // Tester le tableau guess
        return new String[]{"PRESENT", "ABSENT", "CORRECT", "PRESENT", "ABSENT", "CORRECT", "ABSENT", "PRESENT"};
    }
}