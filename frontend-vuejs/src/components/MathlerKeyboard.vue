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