# 🎓 Coaching Vue.js : De HTML/JS natif à Vue.js
## Transformer le jeu Mathler en application Vue.js moderne

---

## 📋 Plan du coaching

1. **Setup & Concepts de base** (SPA, data binding)
2. **Premier composant simple** (MathlerCell)
3. **Composants complexes** (MathlerGrid, MathlerKeyboard)
4. **Gestion d'état & Props**
5. **API & Asynchrone**
6. **Routing & Navigation**
7. **Build & Déploiement (Docker)**

---

## 🚀 ÉTAPE 1 : Setup du projet Vue.js

### Concepts à comprendre

**SPA (Single Page Application)** : 
- Toute l'app charge une seule page HTML
- La navigation se fait via JavaScript (pas de rechargement)
- Vue.js gère le DOM de manière réactive

**Data Binding** :
- Les données et l'interface sont synchronisées automatiquement
- Quand `data` change → Vue met à jour le DOM
- Bidirectionnel avec `v-model`

### 🎯 Action : Créer le projet

```bash
# Installer Node.js (si pas déjà fait)
# Puis créer le projet Vue avec Vite (rapide et moderne)
npm create vue@latest mathler-vue

# Répondre aux questions :
# ✔ TypeScript? No
# ✔ JSX? No
# ✔ Vue Router? Yes 👈 Important pour plus tard
# ✔ Pinia? No (on verra plus tard)
# ✔ Vitest? No
# ✔ ESLint? Yes
# ✔ Prettier? Yes

cd mathler-vue
npm install
npm run dev
```

### 📖 Structure du projet

```
mathler-vue/
├── public/           # Fichiers statiques
├── src/
│   ├── assets/       # Images, CSS globaux
│   ├── components/   # Nos composants Vue
│   ├── views/        # Pages de l'app
│   ├── router/       # Configuration routing
│   ├── App.vue       # Composant racine
│   └── main.js       # Point d'entrée
├── index.html        # Page HTML unique (SPA!)
└── package.json      # Dépendances
```

---

## 🧩 ÉTAPE 2 : Premier composant simple - MathlerCell

### Concepts : Composants Vue

Un composant Vue = **3 sections** :
- `<template>` : Le HTML (structure)
- `<script>` : La logique JavaScript
- `<style>` : Le CSS (peut être scopé au composant)

### 🎯 Action : Créer `src/components/MathlerCell.vue`

```vue
<template>
  <div 
    class="cell" 
    :class="cellClasses"
  >
    {{ value }}
  </div>
</template>

<script>
export default {
  name: 'MathlerCell',
  
  // Props = données reçues du parent
  props: {
    value: {
      type: String,
      default: ''
    },
    state: {
      type: String,
      default: null,
      validator: (value) => {
        return ['CORRECT', 'PRESENT', 'ABSENT', null].includes(value)
      }
    }
  },
  
  // Computed = propriétés calculées (mise à jour auto)
  computed: {
    cellClasses() {
      return {
        filled: this.value !== '',
        correct: this.state === 'CORRECT',
        present: this.state === 'PRESENT',
        absent: this.state === 'ABSENT'
      }
    }
  }
}
</script>

<style scoped>
/* scoped = CSS appliqué uniquement à ce composant */
.cell {
  width: 50px;
  height: 50px;
  border: 2px solid #ddd;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5em;
  font-weight: bold;
  color: #333;
  transition: all 0.3s ease;
}

.cell.filled {
  border-color: #667eea;
  transform: scale(1.05);
}

.cell.correct {
  background-color: #6aaa64;
  border-color: #6aaa64;
  color: white;
  animation: flip 0.5s ease;
}

.cell.present {
  background-color: #c9b458;
  border-color: #c9b458;
  color: white;
  animation: flip 0.5s ease;
}

.cell.absent {
  background-color: #787c7e;
  border-color: #787c7e;
  color: white;
  animation: flip 0.5s ease;
}

@keyframes flip {
  0% { transform: rotateX(0); }
  50% { transform: rotateX(90deg); }
  100% { transform: rotateX(0); }
}
</style>
```

### 💡 Explications clés

**`props`** : 
- Comme les paramètres d'une fonction
- Données transmises du parent → enfant
- **IMMUTABLES** (ne jamais modifier directement)

**`computed`** :
- Propriétés calculées automatiquement
- Se mettent à jour quand leurs dépendances changent
- Mise en cache (performant)

**`:class`** (v-bind:class) :
- Lie dynamiquement des classes CSS
- Peut être un objet, un tableau, une string

**`scoped`** :
- CSS isolé à ce composant uniquement
- Évite les conflits de styles

### 🧪 Test du composant

Créer `src/views/TestCell.vue` :

```vue
<template>
  <div style="padding: 20px; display: flex; gap: 10px;">
    <MathlerCell value="5" />
    <MathlerCell value="+" state="CORRECT" />
    <MathlerCell value="3" state="PRESENT" />
    <MathlerCell value="9" state="ABSENT" />
  </div>
</template>

<script>
import MathlerCell from '@/components/MathlerCell.vue'

export default {
  components: { MathlerCell }
}
</script>
```

---

## 🎮 ÉTAPE 3 : Composant complexe - MathlerGrid

### Concepts : Lists & v-for

**`v-for`** : Boucler sur des tableaux/objets
**`:key`** : Identifiant unique pour chaque élément (performance)

### 🎯 Action : Créer `src/components/MathlerGrid.vue`

```vue
<template>
  <div class="grid">
    <div 
      v-for="(row, rowIndex) in rows" 
      :key="rowIndex"
      class="row"
    >
      <MathlerCell
        v-for="(cell, colIndex) in row"
        :key="colIndex"
        :value="cell.value"
        :state="cell.state"
      />
    </div>
  </div>
</template>

<script>
import MathlerCell from './MathlerCell.vue'

export default {
  name: 'MathlerGrid',
  
  components: {
    MathlerCell
  },
  
  props: {
    attempts: {
      type: Array,
      required: true,
      // attempts = [ ['1','2','+','3','4','+','5','4'], ... ]
    },
    states: {
      type: Array,
      default: () => []
      // states = [ ['CORRECT', 'CORRECT', ...], ... ]
    },
    maxAttempts: {
      type: Number,
      default: 6
    },
    equationLength: {
      type: Number,
      default: 8
    }
  },
  
  computed: {
    rows() {
      const result = []
      
      // Créer les lignes avec les tentatives existantes
      for (let i = 0; i < this.maxAttempts; i++) {
        const row = []
        const attempt = this.attempts[i] || []
        const rowStates = this.states[i] || []
        
        for (let j = 0; j < this.equationLength; j++) {
          row.push({
            value: attempt[j] || '',
            state: rowStates[j] || null
          })
        }
        
        result.push(row)
      }
      
      return result
    }
  }
}
</script>

<style scoped>
.grid {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-bottom: 30px;
}

.row {
  display: flex;
  gap: 8px;
  justify-content: center;
}
</style>
```

### 💡 Explications clés

**`v-for`** :
```vue
<!-- Sur tableau -->
<div v-for="(item, index) in items" :key="index">

<!-- Sur objet -->
<div v-for="(value, key) in object" :key="key">
```

**`:key`** :
- **Obligatoire** avec `v-for`
- Aide Vue à identifier les éléments
- Améliore les performances

**`computed` vs `methods`** :
- `computed` : mis en cache, recalculé seulement si dépendances changent
- `methods` : recalculé à chaque fois

---

## ⌨️ ÉTAPE 4 : MathlerKeyboard avec Events

### Concepts : Events & Emit

**`@click`** (v-on:click) : Écouter des événements
**`$emit`** : Envoyer des événements au parent

### 🎯 Action : Créer `src/components/MathlerKeyboard.vue`

```vue
<template>
  <div class="keyboard">
    <div class="keyboard-row">
      <button
        v-for="key in row1"
        :key="key"
        class="key"
        :class="getKeyClass(key)"
        @click="handleKeyClick(key)"
      >
        {{ key }}
      </button>
    </div>
    
    <div class="keyboard-row">
      <button
        v-for="key in row2"
        :key="key"
        class="key"
        :class="getKeyClass(key)"
        @click="handleKeyClick(key)"
      >
        {{ key }}
      </button>
    </div>
    
    <div class="keyboard-row">
      <button
        class="key wide"
        @click="handleKeyClick('Enter')"
      >
        VALIDER
      </button>
      <button
        class="key wide"
        @click="handleKeyClick('Backspace')"
      >
        ⌫
      </button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'MathlerKeyboard',
  
  props: {
    keyStates: {
      type: Object,
      default: () => ({})
      // { '5': 'correct', '+': 'present', '9': 'absent' }
    }
  },
  
  data() {
    return {
      row1: ['1', '2', '3', '4', '5', '6', '7'],
      row2: ['8', '9', '0', '+', '-', '*', '/']
    }
  },
  
  methods: {
    handleKeyClick(key) {
      // Émettre un événement vers le parent
      this.$emit('key-pressed', key)
    },
    
    getKeyClass(key) {
      const state = this.keyStates[key]
      return state ? state : ''
    }
  }
}
</script>

<style scoped>
.keyboard {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.keyboard-row {
  display: flex;
  gap: 6px;
  justify-content: center;
}

.key {
  min-width: 40px;
  height: 50px;
  background: #d3d6da;
  border: none;
  border-radius: 6px;
  font-size: 1.1em;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.2s;
  padding: 0 12px;
}

.key:hover {
  background: #bbb;
  transform: translateY(-2px);
}

.key.wide {
  min-width: 65px;
}

.key.correct {
  background-color: #6aaa64;
  color: white;
}

.key.present {
  background-color: #c9b458;
  color: white;
}

.key.absent {
  background-color: #787c7e;
  color: white;
}
</style>
```

### 💡 Explications clés

**Communication parent ↔ enfant** :
```
Parent → Enfant : props
Enfant → Parent : $emit
```

**`$emit`** :
```javascript
// Dans l'enfant
this.$emit('nom-evenement', donnees)

// Dans le parent
<Enfant @nom-evenement="maMethode" />
```

**`data()`** :
- État local du composant
- Retourne un objet
- **Réactif** (Vue détecte les changements)

---

## 🎮 ÉTAPE 5 : Composant principal - MathlerGame

### Concepts : Lifecycle Hooks & Reactive State

**Lifecycle Hooks** :
- `created()` : Composant créé (pas encore dans le DOM)
- `mounted()` : Composant dans le DOM
- `updated()` : Composant mis à jour
- `unmounted()` : Composant retiré du DOM

### 🎯 Action : Créer `src/views/MathlerGame.vue`

```vue
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
  
  mounted() {
    // Écouter le clavier physique
    window.addEventListener('keydown', this.handlePhysicalKey)
  },
  
  unmounted() {
    // Nettoyer l'écouteur
    window.removeEventListener('keydown', this.handlePhysicalKey)
  },
  
  methods: {
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
      this.$set(this.attempts, this.currentAttempt, [...this.currentGuess])
    },
    
    async submitGuess() {
      if (this.currentGuess.length !== this.equationLength) {
        this.showMessage('L\'équation doit contenir ' + this.equationLength + ' caractères!', 'error')
        return
      }
      
      try {
        const response = await fetch(this.apiUrl, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            guess: this.currentGuess,
            targetValue: this.targetNumber
          })
        })
        
        if (!response.ok) {
          throw new Error('Erreur API: ' + response.status)
        }
        
        const result = await response.json()
        
        if (!result.valid) {
          this.showMessage(result.message || 'Équation invalide!', 'error')
          return
        }
        
        // Enregistrer les états
        this.$set(this.attemptStates, this.currentAttempt, result.states)
        
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
          this.$set(this.keyStates, char, 'correct')
        } else if (state === 'PRESENT' && currentState !== 'correct') {
          this.$set(this.keyStates, char, 'present')
        } else if (state === 'ABSENT' && !currentState) {
          this.$set(this.keyStates, char, 'absent')
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
```

### 💡 Explications clés

**`v-model`** :
- Bidirectionnel binding
- `<input v-model="apiUrl">` = valeur liée à `data.apiUrl`

**`v-if`** :
- Rendu conditionnel (ajoute/retire du DOM)
- Alternative : `v-show` (change juste `display`)

**`$set`** :
- Pour rendre réactifs les changements dans tableaux/objets
- Vue 3 : plus besoin, réactivité automatique

**`async/await`** :
- Gérer les appels API de manière synchrone
- Plus lisible que les `.then()`

---

## 🔄 ÉTAPE 6 : API Service & Async

### Concepts : Services & Promesses

Séparer la logique API dans un service dédié.

### 🎯 Action : Créer `src/services/mathlerApi.js`

```javascript
const API_BASE_URL = 'http://localhost:8080/api'

export class MathlerApiService {
  constructor(baseUrl = API_BASE_URL) {
    this.baseUrl = baseUrl
  }
  
  /**
   * Récupérer un puzzle aléatoire
   */
  async getRandomPuzzle() {
    try {
      const response = await fetch(`${this.baseUrl}/puzzle`)
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`)
      }
      
      return await response.json()
    } catch (error) {
      console.error('Error fetching puzzle:', error)
      throw error
    }
  }
  
  /**
   * Récupérer un puzzle par difficulté
   */
  async getPuzzleByDifficulty(difficulty) {
    try {
      const response = await fetch(`${this.baseUrl}/puzzle/difficulty/${difficulty}`)
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`)
      }
      
      return await response.json()
    } catch (error) {
      console.error('Error fetching puzzle by difficulty:', error)
      throw error
    }
  }
  
  /**
   * Vérifier une tentative
   */
  async checkGuess(guess, targetValue) {
    try {
      const response = await fetch(`${this.baseUrl}/check`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          guess,
          targetValue
        })
      })
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`)
      }
      
      return await response.json()
    } catch (error) {
      console.error('Error checking guess:', error)
      throw error
    }
  }
}

// Export d'une instance par défaut
export default new MathlerApiService()
```

### 🎯 Utiliser le service dans MathlerGame.vue

```vue
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
      // ... reste du data
    }
  },
  
  async mounted() {
    window.addEventListener('keydown', this.handlePhysicalKey)
    
    // Charger un puzzle au démarrage
    await this.loadPuzzle()
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
    }
    
    // ... autres méthodes
  }
}
</script>
```

---

## 🧭 ÉTAPE 7 : Routing & Navigation

### Concepts : Vue Router

Créer plusieurs pages et naviguer entre elles.

### 🎯 Action : Configurer le router

`src/router/index.js` (déjà créé si tu as choisi Vue Router) :

```javascript
import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import MathlerGame from '../views/MathlerGame.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/game',
      name: 'game',
      component: MathlerGame
    },
    {
      path: '/game/:difficulty',
      name: 'game-difficulty',
      component: MathlerGame,
      props: true // Passer difficulty en prop
    },
    {
      path: '/about',
      name: 'about',
      // Lazy loading (charge le composant seulement quand nécessaire)
      component: () => import('../views/AboutView.vue')
    }
  ]
})

export default router
```

### 🎯 Créer HomeView.vue

```vue
<template>
  <div class="home">
    <h1>🔢 Bienvenue sur Mathler</h1>
    <p>Trouvez l'équation mathématique qui donne le résultat cible !</p>
    
    <div class="difficulty-selector">
      <h2>Choisissez votre difficulté :</h2>
      
      <button 
        class="difficulty-btn easy"
        @click="startGame(1)"
      >
        😊 Facile
      </button>
      
      <button 
        class="difficulty-btn medium"
        @click="startGame(2)"
      >
        🤔 Moyen
      </button>
      
      <button 
        class="difficulty-btn hard"
        @click="startGame(3)"
      >
        😈 Difficile
      </button>
      
      <button 
        class="difficulty-btn random"
        @click="startGame()"
      >
        🎲 Aléatoire
      </button>
    </div>
    
    <router-link to="/about" class="about-link">
      À propos
    </router-link>
  </div>
</template>

<script>
export default {
  name: 'HomeView',
  
  methods: {
    startGame(difficulty) {
      if (difficulty) {
        this.$router.push({ 
          name: 'game-difficulty', 
          params: { difficulty } 
        })
      } else {
        this.$router.push({ name: 'game' })
      }
    }
  }
}
</script>

<style scoped>
.home {
  text-align: center;
  padding: 40px;
  max-width: 600px;
  margin: 0 auto;
}

h1 