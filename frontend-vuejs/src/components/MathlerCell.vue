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