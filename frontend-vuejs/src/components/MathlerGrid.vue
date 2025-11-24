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
import MathlerCell from '@/components/MathlerCell.vue'

export default {
  name: 'MathlerGrid',
  
  components: {
    MathlerCell
  },
  
  props: {
    attempts: {
      type: Array,
      required: true,
  	  default: () => [] // [] permet de partager le même tableau pour toutes les instances de MathlerGrid
      // attempts = [ ['1','2','+','3','4','+','5','4'], ... ]
    },
    states: {
      type: Array,
      default: () => [] // [] permet de partager le même tableau pour toutes les instances de MathlerGrid
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