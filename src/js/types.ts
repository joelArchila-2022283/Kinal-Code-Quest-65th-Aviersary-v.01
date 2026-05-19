export enum OperationType {
  CREATE = 'create',
  UPDATE = 'update',
  DELETE = 'delete',
  LIST = 'list',
  GET = 'get',
  WRITE = 'write',
}

export interface PlayerStats {
  playerName: string;
  xp: number;
  level: number;
  responsibility: number;
  solidarity: number;
  laboriosity: number;
  rank: string;
}

export interface Reward {
  xp: number;
  responsibility?: number;
  solidarity?: number;
  laboriosity?: number;
}

export interface Level {
  id: number;
  title: string;
  description: string;
  context: string;
  codeTemplate: string;
  solution: string;
  options: string[];
  lesson: string;
  realWorldExample: string;
  difficulty: 'Easy' | 'Medium' | 'Hard';
  reward: Reward;
}

export interface GameState {
  currentLevelIndex: number;
  stats: PlayerStats;
  logs: string[];
  unlockedMemories: string[];
}
