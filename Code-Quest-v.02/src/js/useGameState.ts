import { useState, useEffect } from 'react';
import levelsData from '../data/json/levels.json';
import { Level, GameState, PlayerStats } from './types';

export function useGameState() {
  const [gameState, setGameState] = useState<GameState>({
    currentLevelIndex: 0,
    stats: {
      playerName: '',
      xp: 0,
      level: 1,
      responsibility: 0,
      solidarity: 0,
      laboriosity: 0,
      rank: 'Aprendiz Técnico'
    },
    logs: ['[SISTEMA]: Inicializando terminal de restauración técnica...'],
    unlockedMemories: []
  });

  const updateRank = (level: number) => {
    if (level >= 30) return 'Maestro Tecnológico';
    if (level >= 20) return 'Ingeniero Kinal';
    if (level >= 10) return 'Especialista Java';
    if (level >= 5) return 'Operador de Sistemas';
    return 'Aprendiz Técnico';
  };

  const addLog = (msg: string) => {
    setGameState(prev => ({ ...prev, logs: [...prev.logs, msg].slice(-10) }));
  };

  const completeLevel = (level: Level) => {
    const newXp = gameState.stats.xp + level.reward.xp;
    const newLevel = Math.floor(newXp / 500) + 1;
    
    const newStats: PlayerStats = {
      ...gameState.stats,
      xp: newXp,
      level: newLevel,
      rank: updateRank(newLevel),
      responsibility: gameState.stats.responsibility + (level.reward.responsibility || 0),
      solidarity: gameState.stats.solidarity + (level.reward.solidarity || 0),
      laboriosity: gameState.stats.laboriosity + (level.reward.laboriosity || 0)
    };

    const newLogs = [
      ...gameState.logs,
      `[ÉXITO]: Nivel "${level.title}" completado.`,
      `[RECOMPENSA]: +${level.reward.xp} XP.`
    ];

    const historyEntry = `${level.title}: Restaurado exitosamente.`;
    const newMemories = [...gameState.unlockedMemories, historyEntry];

    setGameState(prev => ({
      ...prev,
      stats: newStats,
      logs: newLogs.slice(-10),
      unlockedMemories: newMemories,
      currentLevelIndex: Math.min(prev.currentLevelIndex + 1, levelsData.length - 1)
    }));
  };

  return {
    gameState,
    setGameState,
    addLog,
    completeLevel
  };
}
