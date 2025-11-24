import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
import MathlerGame from '@/views/MathlerGame.vue'

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
      component: () => import('@/views/AboutView.vue')
    }
  ]
})

export default router