import { useState, useCallback } from 'react';
import { motion, AnimatePresence } from 'motion/react';
import { Terminal as TerminalIcon, ShieldAlert, Cpu, CheckCircle2, LayoutDashboard } from 'lucide-react';
import { LEVELS, COSMETICS } from './constants';
import { PlayerStats, CosmeticItem } from './types';
import { StatsPanel } from './components/StatsPanel';
import { CodeTerminal } from './components/CodeTerminal';
import { MemoriasModal } from './components/MemoriasModal';
import { JavaIntroModal } from './components/JavaIntroModal';
import { Dashboard } from './components/Dashboard';
import { SuccessModal } from './components/SuccessModal';
import { NPCEventModal } from './components/NPCEventModal';
import { IntroView } from './components/IntroView';

type GameView = 'intro' | 'java-intro' | 'dashboard' | 'playing' | 'completed';

export default function App() {
  const [stats, setStats] = useState<PlayerStats>({
    name: 'Estudiante',
    avatar: '🤖',
    xp: 0,
    level: 1,
    responsabilidad: 0,
    solidaridad: 0,
    laboriosidad: 0,
    currentLevelId: 1,
    unlockedMemories: [],
    unlockedCosmetics: [],
    equippedCosmetics: []
  });

  const [completedLevels, setCompletedLevels] = useState<number[]>([]);
  const [view, setView] = useState<GameView>('intro');
  const [activeLevelId, setActiveLevelId] = useState<number>(1);
  const [showMemory, setShowMemory] = useState(false);
  const [showSuccess, setShowSuccess] = useState(false);
  const [showNPC, setShowNPC] = useState(false);
  const [lastMissionStats, setLastMissionStats] = useState<{ errors: number, time: number, isPerfect: boolean } | null>(null);
  const [unlockedCosmetic, setUnlockedCosmetic] = useState<CosmeticItem | undefined>(undefined);

  const activeLevel = LEVELS.find(l => l.id === activeLevelId) || LEVELS[0];

  const handleLevelSuccess = useCallback((errors: number, time: number) => {
    const isPerfect = errors === 0;
    const levelAwarded = activeLevel.valuesAwarded;
    
    setLastMissionStats({ errors, time, isPerfect });

    let cosmetic: CosmeticItem | undefined = undefined;
    if (isPerfect) {
      if (activeLevel.id === 1 && !stats.unlockedCosmetics.includes('helmet_mech')) {
        cosmetic = COSMETICS.find(c => c.id === 'helmet_mech');
      } else if (activeLevel.id === 2 && !stats.unlockedCosmetics.includes('goggles_elec')) {
        cosmetic = COSMETICS.find(c => c.id === 'goggles_elec');
      }
    }
    setUnlockedCosmetic(cosmetic);

    setStats(prev => {
      const bonusXP = isPerfect ? 50 : 0;
      const newXp = prev.xp + 100 + bonusXP;
      const newUnlockedCosmetics = [...prev.unlockedCosmetics];
      if (cosmetic) newUnlockedCosmetics.push(cosmetic.id);

      return {
        ...prev,
        xp: newXp,
        level: Math.floor(newXp / 100) + 1,
        responsabilidad: Math.min(prev.responsabilidad + levelAwarded.responsabilidad, 100),
        solidaridad: Math.min(prev.solidaridad + levelAwarded.solidaridad, 100),
        laboriosidad: Math.min(prev.laboriosidad + levelAwarded.laboriosidad, 100),
        currentLevelId: Math.max(prev.currentLevelId, activeLevelId + 1),
        unlockedMemories: activeLevel.historicalUnlock && !prev.unlockedMemories.includes(activeLevel.id) 
          ? [...prev.unlockedMemories, activeLevel.id] 
          : prev.unlockedMemories,
        unlockedCosmetics: newUnlockedCosmetics
      };
    });

    setCompletedLevels(prev => Array.from(new Set([...prev, activeLevelId])));
    setShowSuccess(true);
  }, [activeLevel, activeLevelId, stats.unlockedCosmetics]);

  const handleNPCComplete = (bonusSolidaridad: number) => {
    setStats(prev => {
      const newSolidaridad = Math.min(prev.solidaridad + bonusSolidaridad, 100);
      const newUnlockedCosmetics = [...prev.unlockedCosmetics];
      
      if (newSolidaridad >= 50 && !newUnlockedCosmetics.includes('badge_honor')) {
        newUnlockedCosmetics.push('badge_honor');
      }

      return {
        ...prev,
        solidaridad: newSolidaridad,
        unlockedCosmetics: newUnlockedCosmetics
      };
    });
    setShowNPC(false);
    
    if (activeLevel.historicalUnlock && !stats.unlockedMemories.includes(activeLevel.id)) {
      setShowMemory(true);
    } else {
      if (completedLevels.length === LEVELS.length) {
        setView('completed');
      } else {
        setView('dashboard');
      }
    }
  };

  const handleNextFromSuccess = () => {
    setShowSuccess(false);
    if (activeLevel.npcEvent) {
      setShowNPC(true);
    } else {
      if (activeLevel.historicalUnlock && !stats.unlockedMemories.includes(activeLevel.id)) {
        setShowMemory(true);
      } else {
        if (completedLevels.length === LEVELS.length) {
          setView('completed');
        } else {
          setView('dashboard');
        }
      }
    }
  };

  const handleSelectLevel = (id: number) => {
    setActiveLevelId(id);
    setView('playing');
  };

  const handleEquip = (cosmeticId: string) => {
    setStats(prev => {
      const isEquipped = prev.equippedCosmetics.includes(cosmeticId);
      const newEquipped = isEquipped 
        ? prev.equippedCosmetics.filter(id => id !== cosmeticId)
        : [...prev.equippedCosmetics, cosmeticId];
      
      return { ...prev, equippedCosmetics: newEquipped };
    });
  };

  const handleProfileComplete = (name: string, avatar: string) => {
    setStats(prev => ({ ...prev, name, avatar }));
    setView('java-intro');
  };

  if (view === 'intro') {
    return (
      <IntroView onStart={handleProfileComplete} />
    );
  }

  return (
    <div className="h-screen bg-navy flex overflow-hidden">
      <StatsPanel stats={stats} onEquip={handleEquip} />
      
      <main className="flex-1 flex flex-col relative">
        <header className="h-20 border-b border-white/10 px-8 flex items-center justify-between bg-navy shrink-0">
          <div className="flex items-center gap-4">
            <div className="w-10 h-10 bg-white/5 rounded-xl flex items-center justify-center border border-white/10">
               <TerminalIcon size={18} className="text-terminal" />
            </div>
            <div>
              <h1 className="text-white font-black uppercase tracking-tight text-sm">Java Module: {activeLevel.title}</h1>
              <p className="text-steel font-mono text-[10px] uppercase tracking-widest">{activeLevel.area}</p>
            </div>
          </div>

          <div className="flex items-center gap-4">
             <button 
               onClick={() => setView('dashboard')}
               className="p-2.5 bg-white/5 hover:bg-white/10 rounded-xl text-steel hover:text-white transition-all border border-white/5"
               title="Mapa de Misiones"
             >
                <LayoutDashboard size={20} />
             </button>
             <div className="px-5 py-2 bg-terminal/10 border border-terminal/20 rounded-xl flex items-center gap-3">
                <div className="w-2 h-2 bg-terminal rounded-full animate-pulse" />
                <span className="text-terminal font-mono text-[10px] font-black uppercase tracking-widest">Servidor Online</span>
             </div>
          </div>
        </header>

        <div className="flex-1 overflow-hidden flex flex-col">
          {view === 'dashboard' ? (
            <Dashboard 
              currentLevelId={stats.currentLevelId} 
              completedLevels={completedLevels}
              onSelectLevel={handleSelectLevel}
            />
          ) : (
            <CodeTerminal 
              level={activeLevel}
              onSuccess={handleLevelSuccess}
              onFailure={(msg) => console.log(msg)}
            />
          )}
        </div>
      </main>

       <AnimatePresence>
         {view === 'java-intro' && (
           <JavaIntroModal onComplete={() => setView('dashboard')} />
         )}
         {showMemory && activeLevel.historicalUnlock && (
           <MemoriasModal 
             memoria={activeLevel.historicalUnlock} 
             onClose={() => setShowMemory(false)} 
           />
         )}
         {showSuccess && lastMissionStats && (
           <SuccessModal 
             stats={{
               xp: 100,
               errors: lastMissionStats.errors,
               time: lastMissionStats.time,
               isPerfect: lastMissionStats.isPerfect
             }}
             unlockedCosmetic={unlockedCosmetic}
             onNext={handleNextFromSuccess}
           />
         )}
         {showNPC && activeLevel.npcEvent && (
           <NPCEventModal 
             event={activeLevel.npcEvent}
             onComplete={handleNPCComplete}
           />
         )}
       </AnimatePresence>
    </div>
  );
}
