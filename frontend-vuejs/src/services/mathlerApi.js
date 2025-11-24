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