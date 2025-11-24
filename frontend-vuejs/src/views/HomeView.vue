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
