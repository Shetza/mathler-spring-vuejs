// Mock API Server pour tester le front-end Mathler en local
// À lancer avec Node.js : node mock-server.js

const http = require('http');

const PORT = 8080;

// Fonction pour évaluer une équation mathématique de manière sécurisée
function evaluateEquation(chars) {
    try {
        const equation = chars.join('').replace(/×/g, '*');
        // Vérifier que l'équation ne contient que des caractères autorisés
        if (!/^[0-9+\-*/\s]+$/.test(equation)) {
            return null;
        }
        // Utiliser Function au lieu d'eval pour plus de sécurité
        const result = Function('"use strict"; return (' + equation + ')')();
        return result;
    } catch (error) {
        return null;
    }
}

// Fonction pour vérifier l'équation et générer les états
function checkGuess(guess, targetValue, solution) {
    const states = [];
    const solutionArray = solution.split('');
    const guessArray = [...guess];
    
    // Tableau pour tracker les caractères déjà utilisés dans la solution
    const solutionUsed = new Array(solutionArray.length).fill(false);
    const guessProcessed = new Array(guessArray.length).fill(false);
    
    // Premier passage : marquer les CORRECT (bonne position)
    for (let i = 0; i < guessArray.length; i++) {
        if (guessArray[i] === solutionArray[i]) {
            states[i] = 'CORRECT';
            solutionUsed[i] = true;
            guessProcessed[i] = true;
        }
    }
    
    // Deuxième passage : marquer les PRESENT (existe ailleurs)
    for (let i = 0; i < guessArray.length; i++) {
        if (guessProcessed[i]) continue;
        
        let found = false;
        for (let j = 0; j < solutionArray.length; j++) {
            if (!solutionUsed[j] && guessArray[i] === solutionArray[j]) {
                states[i] = 'PRESENT';
                solutionUsed[j] = true;
                found = true;
                break;
            }
        }
        
        if (!found) {
            states[i] = 'ABSENT';
        }
    }
    
    return states;
}

const server = http.createServer((req, res) => {
    // CORS headers
    res.setHeader('Access-Control-Allow-Origin', '*');
    res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS');
    res.setHeader('Access-Control-Allow-Headers', 'Content-Type');
    
    // Gérer les requêtes OPTIONS (preflight)
    if (req.method === 'OPTIONS') {
        res.writeHead(200);
        res.end();
        return;
    }
    
    if (req.url === '/api/check' && req.method === 'POST') {
        let body = '';
        
        req.on('data', chunk => {
            body += chunk.toString();
        });
        
        req.on('end', () => {
            try {
                const data = JSON.parse(body);
                const { guess, targetValue } = data;
                
                console.log('Requête reçue:', { guess, targetValue });
                
                // Valider que guess est un tableau
                if (!Array.isArray(guess)) {
                    res.writeHead(400, { 'Content-Type': 'application/json' });
                    res.end(JSON.stringify({ error: 'guess doit être un tableau' }));
                    return;
                }
                
                // Évaluer l'équation
                const result = evaluateEquation(guess);
                
                // Vérifier si l'équation est valide
                if (result === null || isNaN(result)) {
                    const response = {
                        states: new Array(guess.length).fill('ABSENT'),
                        valid: false,
                        solved: false,
                        message: 'Équation invalide'
                    };
                    res.writeHead(200, { 'Content-Type': 'application/json' });
                    res.end(JSON.stringify(response));
                    return;
                }
                
                // Vérifier si le résultat correspond à la cible
                if (result !== targetValue) {
                    const response = {
                        states: new Array(guess.length).fill('ABSENT'),
                        valid: false,
                        solved: false,
                        message: `L'équation donne ${result}, pas ${targetValue}`
                    };
                    res.writeHead(200, { 'Content-Type': 'application/json' });
                    res.end(JSON.stringify(response));
                    return;
                }
                
                // Solution exemple pour targetValue = 100
                // Toutes les solutions font exactement 8 caractères
                const solutions = {
                    100: '12+34+54',
                    42: '7*6+0+0+',
                    20: '10+5+2+3'
                };
                
                const solution = solutions[targetValue] || '12+34+54';
                
                // Vérifier que la solution fait bien 8 caractères
                if (solution.length !== 8) {
                    console.error(`Erreur: la solution "${solution}" ne fait pas 8 caractères`);
                }
                
                // Générer les états
                const states = checkGuess(guess, targetValue, solution);
                
                // Vérifier si l'équation est résolue (tous CORRECT)
                const solved = states.every(state => state === 'CORRECT');
                
                const response = {
                    states: states,
                    valid: true,
                    solved: solved,
                    calculatedValue: result
                };
                
                console.log('Réponse:', response);
                
                res.writeHead(200, { 'Content-Type': 'application/json' });
                res.end(JSON.stringify(response));
                
            } catch (error) {
                console.error('Erreur:', error);
                res.writeHead(500, { 'Content-Type': 'application/json' });
                res.end(JSON.stringify({ error: 'Erreur serveur' }));
            }
        });
    } else {
        res.writeHead(404, { 'Content-Type': 'application/json' });
        res.end(JSON.stringify({ error: 'Route non trouvée' }));
    }
});

server.listen(PORT, () => {
    console.log(`🚀 Mock API Server lancé sur http://localhost:${PORT}`);
    console.log(`📍 Endpoint: POST http://localhost:${PORT}/api/check`);
    console.log('\n📝 Solutions à 8 caractères:');
    console.log('   - Pour 100: 12+34+54');
    console.log('   - Pour 42: 7*6+0+0+');
    console.log('   - Pour 20: 10+5+2+3');
});