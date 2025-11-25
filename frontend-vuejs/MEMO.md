# 📚 Mémo Vue.js - Guide de Référence Rapide
**De HTML/JS natif à Vue.js - Tous les concepts essentiels**

---

## 🎯 CONCEPTS FONDAMENTAUX

### **SPA (Single Page Application)**
- **Une seule page HTML** chargée au démarrage
- Navigation gérée par JavaScript (pas de rechargement)
- Vue.js gère le DOM de manière réactive

### **Data Binding**
- Synchronisation automatique données ↔ interface
- Quand `data` change → Vue met à jour le DOM
- Bidirectionnel avec `v-model`

---

## 🧩 STRUCTURE D'UN COMPOSANT VUE

```vue
<template>
  <!-- Structure HTML -->
  <div>{{ message }}</div>
</template>

<script>
export default {
  name: 'MonComposant',
  // ... logique
}
</script>

<style scoped>
/* CSS isolé au composant */
</style>
```

**⚠️ IMPORTANT** : Toujours utiliser `<template>` même si ça semble fonctionner sans !

---

## 📦 PROPS (Parent → Enfant)

### **Déclaration**
```javascript
props: {
  value: String,
  count: {
    type: Number,
    default: 0
  },
  items: {
    type: Array,
    default: () => []  // ⚠️ Fonction pour Array/Object !
  },
  config: {
    type: Object,
    default: () => ({})  // ⚠️ Parenthèses pour retourner objet !
  }
}
```

### **Règles Critiques**
| Type | Default Correct | Default Incorrect |
|------|-----------------|-------------------|
| String/Number/Boolean | `default: 'hello'` | - |
| Array | `default: () => []` | ❌ `default: []` |
| Object | `default: () => ({})` | ❌ `default: () => {}` |

**Pourquoi `() => []` ?**
- `default: []` → Tous les composants partagent la MÊME référence
- `default: () => []` → Chaque composant a sa PROPRE référence

**Pourquoi `() => ({})` ?**
- `() => {}` → Bloc de code vide, retourne `undefined`
- `() => ({})` → Retourne un objet vide `{}`

---

## 🔄 COMMUNICATION COMPOSANTS

### **Parent → Enfant : Props**
```vue
<!-- Parent -->
<Enfant :message="hello" :count="5" />

<!-- Enfant -->
<script>
export default {
  props: ['message', 'count']
}
</script>
```

### **Enfant → Parent : $emit**
```vue
<!-- Enfant -->
<button @click="$emit('clicked', data)">

<!-- Parent -->
<Enfant @clicked="handleClick" />
```

---

## 📊 DATA, COMPUTED, METHODS

### **data() - État Local**
```javascript
data() {
  return {
    count: 0,
    items: []
  }
}
```
- ✅ Toujours **réactif** en Vue 3
- ⚠️ Doit retourner une **fonction**
- ⚠️ Vue 2 : propriétés ajoutées après = non réactives

### **computed - Propriétés Calculées**
```javascript
computed: {
  doubleCount() {
    return this.count * 2
  }
}
```
- ✅ Mise en cache
- ✅ Recalculé seulement si dépendances changent
- ✅ Pas d'arguments

### **methods - Méthodes**
```javascript
methods: {
  increment(amount) {
    this.count += amount
  }
}
```
- ✅ Peut avoir des arguments
- ❌ Pas de cache, recalculé à chaque fois

---

## 🔁 DIRECTIVES VUE

### **v-for - Boucles**
```vue
<div v-for="(item, index) in items" :key="item.id">
  {{ item.name }}
</div>
```
⚠️ **:key est OBLIGATOIRE** pour identifier chaque élément

### **v-if / v-show - Conditions**
```vue
<div v-if="show">Ajouté/retiré du DOM</div>
<div v-show="show">display: none/block</div>
```

### **v-model - Binding Bidirectionnel**
```vue
<input v-model="message">
<!-- Équivalent à : -->
<input :value="message" @input="message = $event.target.value">
```

### **v-bind / : - Binding Attributs**
```vue
<div :class="{ active: isActive }">
<img :src="imageUrl">
```

### **v-on / @ - Événements**
```vue
<button @click="handleClick">
<input @keyup.enter="submit">
```

---

## 🔄 LIFECYCLE HOOKS

```
Création → Montage → Mise à jour → Démontage
```

### **data() - Initialisation**
```javascript
data() {
  return { count: 0 }
}
```
- Appelé **une seule fois** à la création
- Retourne l'état réactif du composant

### **created() - Composant Créé**
```javascript
created() {
  // Composant créé, pas encore dans le DOM
  this.loadConfig()
}
```

### **mounted() - Dans le DOM**
```javascript
mounted() {
  // ✅ DOM disponible
  window.addEventListener('keydown', this.handler)
}
```
Utilisé pour :
- Event listeners
- API calls
- Accès aux éléments DOM

### **unmounted() - Nettoyage**
```javascript
unmounted() {
  // 🧹 CRUCIAL : nettoyer les ressources
  window.removeEventListener('keydown', this.handler)
  clearInterval(this.timer)
}
```

**⚠️ RÈGLE D'OR** : Toute ressource créée dans `mounted()` doit être libérée dans `unmounted()`

---

## 🌐 ASYNC / AWAIT

### **Lifecycle Async**
```javascript
async mounted() {
  // ✅ Attendre une API
  await this.loadData()
}
```

### **Methods Async**
```javascript
methods: {
  async loadData() {
    try {
      const response = await fetch(url)
      const data = await response.json()
      this.items = data
    } catch (error) {
      console.error(error)
    }
  }
}
```

### **Appels Parallèles**
```javascript
// 🚀 Plus rapide
const [user, stats] = await Promise.all([
  api.getUser(),
  api.getStats()
])
```

---

## 🧭 VUE ROUTER

### **Configuration (router/index.js)**
```javascript
import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/game/:id',
      name: 'game',
      component: GameView,
      props: true  // Passer params en props
    }
  ]
})

export default router
```

### **Navigation**
```vue
<!-- Lien -->
<router-link to="/">Accueil</router-link>
<router-link :to="{ name: 'game', params: { id: 1 } }">

<!-- Affichage -->
<router-view />

<!-- JavaScript -->
this.$router.push('/game')
this.$router.push({ name: 'game', params: { id: 1 } })
```

### **Accès aux Params**
```javascript
// Dans le composant
this.$route.params.id

// Ou via props (si props: true dans route)
props: ['id']
```

---

## 🎨 CLASSES ET STYLES DYNAMIQUES

### **Classes**
```vue
<!-- Objet -->
<div :class="{ active: isActive, disabled: !enabled }">

<!-- Tableau -->
<div :class="[baseClass, { active: isActive }]">

<!-- Computed -->
<div :class="classes">
computed: {
  classes() {
    return {
      active: this.isActive,
      'text-bold': this.isBold
    }
  }
}
```

### **Styles**
```vue
<div :style="{ color: textColor, fontSize: size + 'px' }">
```

---

## 🔧 VUE 2 vs VUE 3 - Différences Clés

| Fonctionnalité | Vue 2 | Vue 3 |
|----------------|-------|-------|
| Réactivité propriétés ajoutées | ❌ `this.$set()` | ✅ Automatique |
| Suppression propriétés | `this.$delete()` | `delete obj.key` |
| Fragments multiples | ❌ 1 racine | ✅ Multi-racine |
| Performance | Bon | ⚡ Excellent |
| TypeScript | Support | ✅ Natif |

**En Vue 3** : Plus besoin de `$set` / `$delete` grâce aux Proxies ES6 !

---

## 📁 STRUCTURE PROJET

```
src/
├── components/       # Composants réutilisables
│   ├── MathlerCell.vue
│   └── MathlerGrid.vue
├── views/            # Pages (routes)
│   ├── HomeView.vue
│   └── GameView.vue
├── router/           # Configuration routing
│   └── index.js
├── services/         # Logique API
│   └── mathlerApi.js
├── App.vue           # Composant racine
└── main.js           # Point d'entrée
```

---

## 🎯 ENREGISTREMENT COMPOSANTS

### **Local (recommandé)**
```javascript
import MathlerCell from './MathlerCell.vue'

export default {
  components: { MathlerCell }
}
```

### **Global (main.js)**
```javascript
import { createApp } from 'vue'
import MathlerCell from './components/MathlerCell.vue'

const app = createApp(App)
app.component('MathlerCell', MathlerCell)
```

**⚠️ Pourquoi déclarer dans `components` ?**
- `import` = charger le fichier JS
- `components: {}` = dire à Vue "utilise ce composant dans mon template"
- Sans déclaration, Vue ne fait pas le lien entre `<MathlerCell>` et la classe importée

---

## 🚀 COMMANDES ESSENTIELLES

```bash
# Créer un projet
npm create vue@latest mon-projet

# Installer dépendances
npm install

# Lancer en dev
npm run dev

# Build production
npm run build

# Prévisualiser le build
npm run preview
```

---

## 🐛 PIÈGES COURANTS

### **1. Props Default**
```javascript
// ❌ ERREUR
props: {
  items: {
    type: Array,
    default: []  // Partagé !
  }
}

// ✅ CORRECT
props: {
  items: {
    type: Array,
    default: () => []  // Unique !
  }
}
```

### **2. Arrow Function Object**
```javascript
// ❌ Retourne undefined
const func = () => {}

// ✅ Retourne un objet
const func = () => ({})
```

### **3. Event Listeners**
```javascript
// ✅ Toujours nettoyer
mounted() {
  window.addEventListener('keydown', this.handler)
}
unmounted() {
  window.removeEventListener('keydown', this.handler)
}
```

### **4. v-for sans :key**
```vue
<!-- ❌ Mauvaise performance -->
<div v-for="item in items">

<!-- ✅ Avec key unique -->
<div v-for="item in items" :key="item.id">
```

---

## 📝 CHECKLIST COMPOSANT

Avant de créer un composant, vérifie :

- [ ] Nom en PascalCase (`MathlerCell.vue`)
- [ ] `<template>` présent
- [ ] Props avec validation et default
- [ ] Props Array/Object avec `() => []` / `() => ({})`
- [ ] `data()` retourne une fonction
- [ ] `v-for` a toujours `:key`
- [ ] Event listeners nettoyés dans `unmounted()`
- [ ] `<style scoped>` pour isoler CSS

---

## 🎓 CONCEPTS CLÉS À RETENIR

1. **`<template>`** = toujours utiliser, même si optionnel
2. **Props** = parent → enfant (immutables)
3. **$emit** = enfant → parent
4. **data()** = état réactif (fonction obligatoire)
5. **computed** = calculé + cache
6. **methods** = actions + pas de cache
7. **:key** = obligatoire avec v-for
8. **mounted/unmounted** = setup/cleanup
9. **async/await** = pour API calls
10. **`() => []` vs `[]`** = référence unique vs partagée
11. **`() => ({})` vs `() => {}`** = objet vs undefined
12. **Vue 3** = réactivité automatique (plus de $set)

---

## 🔗 RESSOURCES

- **Documentation** : https://vuejs.org
- **Vue Router** : https://router.vuejs.org
- **Style Guide** : https://vuejs.org/style-guide
- **Vite** : https://vitejs.dev

---

## 💡 ANTISÈCHES RAPIDES

### **Binding**
```vue
:attr="value"    → Attribut
:class="obj"     → Classes dynamiques
:style="obj"     → Styles dynamiques
v-model="data"   → Bidirectionnel
```

### **Events**
```vue
@click="method"
@input="method"
@keyup.enter="method"
@submit.prevent="method"
```

### **Conditions**
```vue
v-if="condition"      → Ajoute/retire du DOM
v-else-if="condition"
v-else
v-show="condition"    → display: none/block
```

### **Listes**
```vue
v-for="item in items" :key="item.id"
v-for="(item, index) in items" :key="index"
v-for="(value, key) in object" :key="key"
```

---

## 🎯 EXEMPLE MINIMAL COMPLET

```vue
<template>
  <div>
    <h1>{{ title }}</h1>
    <button @click="increment">Count: {{ count }}</button>
    <ul>
      <li v-for="item in items" :key="item.id">
        {{ item.name }}
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  name: 'ExampleComponent',
  
  props: {
    title: {
      type: String,
      default: 'Hello'
    }
  },
  
  data() {
    return {
      count: 0,
      items: []
    }
  },
  
  computed: {
    doubleCount() {
      return this.count * 2
    }
  },
  
  async mounted() {
    this.items = await this.loadItems()
  },
  
  methods: {
    increment() {
      this.count++
      this.$emit('changed', this.count)
    },
    
    async loadItems() {
      const res = await fetch('/api/items')
      return res.json()
    }
  }
}
</script>

<style scoped>
h1 { color: blue; }
</style>
```

---

**📌 Version 1.0 - Guide de référence Vue.js pour développeurs HTML/CSS/JS**

*Imprime cette fiche et garde-la à portée de main ! 🚀*