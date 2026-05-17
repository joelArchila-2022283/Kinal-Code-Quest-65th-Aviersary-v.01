export type Rank = 'Novato' | 'Técnico' | 'Especialista' | 'Ingeniero' | 'Maestro';

export interface Level {
  id: number;
  unit: number;
  title: string;
  area: string;
  description: string;
  codeBefore: string;
  codeAfter: string;
  expectedAnswer: string;
  options: string[];
  explanation: string;
  historicalUnlock?: {
    year: string;
    description: string;
  };
  valuesAwarded: {
    responsabilidad: number;
    solidaridad: number;
    laboriosidad: number;
  };
  npcEvent?: {
    name: string;
    description: string;
    choices: Array<{
      text: string;
      solidaridad: number;
      dialog: string;
    }>;
  };
}

export interface PlayerStats {
  name: string;
  avatar: string;
  xp: number;
  level: number;
  responsabilidad: number;
  solidaridad: number;
  laboriosidad: number;
  currentLevelId: number;
  unlockedMemories: number[];
  unlockedCosmetics: string[];
  equippedCosmetics: string[];
}

export interface CosmeticItem {
  id: string;
  name: string;
  icon: string;
  description: string;
  requirement: string;
}
