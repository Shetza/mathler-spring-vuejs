<template>
  <div class="container">
    <h1>🔢 Mathler</h1>
    <div class="target">
      Trouver l'équation = <span>{{ targetNumber }}</span>
    </div>
    
    <div class="api-config">
      <label for="apiUrl">URL de l'API Backend:</label>
      <input 
        v-model="apiUrl" 
        type="text" 
        id="apiUrl"
        placeholder="http://localhost:8080/api/check"
      >
    </div>

    <MathlerGrid
      :attempts="attempts"
      :states="attemptStates"
      :max-attempts="maxAttempts"
      :equation-length="equationLength"
    />

    <MathlerKeyboard
      :key-states="keyStates"
      @key-pressed="handleKey"
    />

    <div 
      v-if="message.show" 
      class="message"
      :class="message.type"
    >
      {{ message.text }}
    </div>
  </div>
</template>

<script>
import MathlerGrid from '@/components/MathlerGrid.vue'
import MathlerKeyboard from '@/components/MathlerKeyboard.vue'
import mathlerApi from '@/services/mathlerApi'

export default {
  name: 'MathlerGame',
  
  components: {
    MathlerGrid,
    MathlerKeyboard
  },
  
  data() {
    return {
      // Configuration
      apiUrl: 'http://localhost:8080/api/check',
      maxAttempts: 6,
      equationLength: 8,
      targetNumber: 100,
      
      // État du jeu
      attempts: [],           // [ ['1','2','+', ...], ... ]
      attemptStates: [],      // [ ['CORRECT', ...], ... ]
      currentGuess: [],       // ['1', '2', '+', ...]
      currentAttempt: 0,
      gameOver: false,
      keyStates: {},          // { '5': 'correct', ... }
      
      // UI
      message: {
        show: false,
        text: '',
        type: ''
      }
    }
  },
  
  async mounted() {
    window.addEventListener('keydown', this.handlePhysicalKey)
    
    // Charger un puzzle au démarrage
    await this.loadPuzzle()
  },
  
  unmounted() {
    // Nettoyer l'écouteur
    window.removeEventListener('keydown', this.handlePhysicalKey)
  },
  
  methods: {
    async loadPuzzle() {
      try {
        const puzzle = await mathlerApi.getRandomPuzzle()
        this.targetNumber = puzzle.targetValue
        this.equationLength = puzzle.equationLength
        // Réinitialiser le jeu
        this.resetGame()
      } catch (error) {
        this.showMessage('Erreur lors du chargement du puzzle', 'error')
      }
    },
    
    async submitGuess() {
      if (this.currentGuess.length !== this.equationLength) {
        this.showMessage('L\'équation doit contenir ' + this.equationLength + ' caractères!', 'error')
        return
      }
      
      try {
        const result = await mathlerApi.checkGuess(this.currentGuess, this.targetNumber)
        
        // ... reste de la logique
      } catch (error) {
        this.showMessage('⚠️ Erreur de connexion à l\'API', 'error')
      }
    },
    
    resetGame() {
      this.attempts = []
      this.attemptStates = []
      this.currentGuess = []
      this.currentAttempt = 0
      this.gameOver = false
      this.keyStates = {}
    },

    handlePhysicalKey(e) {
      if (e.key === 'Enter') {
        this.handleKey('Enter')
      } else if (e.key === 'Backspace') {
        this.handleKey('Backspace')
      } else if (/^[0-9+\-*/]$/.test(e.key)) {
        this.handleKey(e.key)
      }
    },
    
    handleKey(key) {
      if (this.gameOver) return
      
      if (key === 'Enter') {
        this.submitGuess()
      } else if (key === 'Backspace') {
        this.deleteLast()
      } else if (this.currentGuess.length < this.equationLength) {
        this.addCharacter(key)
      }
    },
    
    addCharacter(char) {
      this.currentGuess.push(char)
      this.updateCurrentAttempt()
    },
    
    deleteLast() {
      this.currentGuess.pop()
      this.updateCurrentAttempt()
    },
    
    updateCurrentAttempt() {
      // Mettre à jour l'affichage de la tentative courante
      // ✅ Affectation directe en Vue 3
      // this.$set(this.attempts, this.currentAttempt, [...this.currentGuess])
	  this.attempts[this.currentAttempt] = [...this.currentGuess]
  	},
    
    async submitGuess() {
      if (this.currentGuess.length !== this.equationLength) {
        this.showMessage('L\'équation doit contenir ' + this.equationLength + ' caractères!', 'error')
        return
      }
      
      try {
        // const response = await fetch(this.apiUrl, {
        //   method: 'POST',
        //   headers: {
        //     'Content-Type': 'application/json',
        //   },
        //   body: JSON.stringify({
        //     guess: this.currentGuess,
        //     targetValue: this.targetNumber
        //   })
        // })
        
        // if (!response.ok) {
        //   throw new Error('Erreur API: ' + response.status)
        // }
        
        // const result = await response.json()
        const result = {
        	valid: true,
        	solved: true,
        	states: ["PRESENT", "CORRECT", "CORRECT", "CORRECT", "CORRECT", "CORRECT", "CORRECT", "CORRECT"]
        }
        
        if (!result.valid) {
          this.showMessage(result.message || 'Équation invalide!', 'error')
          return
        }
        
        // Enregistrer les états
        // this.$set(this.attemptStates, this.currentAttempt, result.states)
        this.attemptStates[this.currentAttempt] = result.states
        
        // Mettre à jour les états du clavier
        this.updateKeyStates(result.states)
        
        if (result.solved) {
          this.gameOver = true
          this.showMessage('🎉 Bravo! Vous avez trouvé l\'équation!', 'success')
        } else if (this.currentAttempt === this.maxAttempts - 1) {
          this.gameOver = true
          this.showMessage('😔 Perdu! Plus de tentatives disponibles.', 'error')
        } else {
          this.currentAttempt++
          this.currentGuess = []
        }
        
      } catch (error) {
        console.error('Erreur:', error)
        this.showMessage('⚠️ Erreur de connexion à l\'API', 'error')
      }
    },
    
    updateKeyStates(states) {
      states.forEach((state, index) => {
        const char = this.currentGuess[index]
        const currentState = this.keyStates[char]
        
        // Priorité: correct > present > absent
        if (state === 'CORRECT') {
          // this.$set(this.keyStates, char, 'correct')
          this.keyStates[char] = 'correct'
        } else if (state === 'PRESENT' && currentState !== 'correct') {
          // this.$set(this.keyStates, char, 'present')
          this.keyStates[char] = 'present'
        } else if (state === 'ABSENT' && !currentState) {
          // this.$set(this.keyStates, char, 'absent')
          this.keyStates[char] = 'absent'
        }
      })
    },
    
    showMessage(text, type) {
      this.message = {
        show: true,
        text,
        type
      }
      
      setTimeout(() => {
        this.message.show = false
      }, 3000)
    }
  }
}
</script>

<style scoped>
.container {
  background: white;
  border-radius: 20px;
  padding: 30px;
  box-shadow: 0 20px 60px rgba(0,0,0,0.3);
  max-width: 500px;
  width: 100%;
  margin: 20px auto;
}

h1 {
  text-align: center;
  color: #667eea;
  margin-bottom: 10px;
  font-size: 2.5em;
}

.target {
  text-align: center;
  font-size: 1.2em;
  color: #555;
  margin-bottom: 20px;
  font-weight: bold;
}

.target span {
  color: #667eea;
  font-size: 1.5em;
}

.api-config {
  margin-bottom: 20px;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 8px;
}

.api-config input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 0.9em;
}

.api-config label {
  display: block;
  margin-bottom: 5px;
  font-size: 0.9em;
  color: #666;
}

.message {
  text-align: center;
  margin-top: 20px;
  padding: 15px;
  border-radius: 8px;
  font-weight: bold;
  animation: slideIn 0.3s ease;
}

.message.success {
  background-color: #d4edda;
  color: #155724;
}

.message.error {
  background-color: #f8d7da;
  color: #721c24;
}

@keyframes slideIn {
  from { transform: translateY(-20px); opacity: 0; }
  to { transform: translateY(0); opacity: 1; }
}
</style>