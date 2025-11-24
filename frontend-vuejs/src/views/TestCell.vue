<template>
  <div style="padding: 20px; display: flex; gap: 10px;">
    <MathlerCell value="5" />
    <MathlerCell value="+" state="CORRECT" />
    <MathlerCell value="3" state="PRESENT" />
    <MathlerCell value="9" state="ABSENT" />
  </div>
</template>

<script setup>/* "setup" permet d'éviter l'ajout rébarbatif de : "export default { components: { MathlerCell } }" */
import MathlerCell from '@/components/MathlerCell.vue'
</script>