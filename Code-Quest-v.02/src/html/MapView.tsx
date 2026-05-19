import { motion } from 'motion/react';
import { History, CheckCircle2, Zap, ChevronRight } from 'lucide-react';
import levelsData from '../data/json/levels.json';
import { GameState } from '../js/types';

interface MapViewProps {
  gameState: GameState;
  accentColor: string;
  onSelectLevel: () => void;
}

export function MapView({ gameState, accentColor, onSelectLevel }: MapViewProps) {
  return (
    <motion.div 
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      exit={{ opacity: 0, y: -20 }}
      className="h-full flex flex-col gap-8"
    >
      <div className="flex justify-between items-center">
        <h1 className="text-4xl font-black italic text-white uppercase tracking-tighter">Sector de <span className={`text-${accentColor}`}>Misiones</span></h1>
        <div className="flex gap-4">
          <div className="glass-panel px-6 py-3 rounded-2xl flex items-center gap-3">
            <History className={`text-${accentColor}`} />
            <span className="text-xl font-black text-white">{gameState.unlockedMemories.length} / 15</span>
          </div>
        </div>
      </div>

      <div className="flex-1 grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6 overflow-y-auto pr-2 custom-scrollbar">
        {levelsData.map((level: any, idx: number) => {
          const isLocked = idx > gameState.currentLevelIndex;
          const isCompleted = idx < gameState.currentLevelIndex;
          
          return (
            <motion.div
              key={level.id}
              whileHover={!isLocked ? { scale: 1.02 } : {}}
              onClick={() => !isLocked && onSelectLevel()}
              className={`
                p-8 rounded-[2rem] border-2 flex flex-col gap-4 relative overflow-hidden transition-all cursor-pointer
                ${isLocked ? 'grayscale opacity-40 border-white/5 bg-white/2 cursor-not-allowed' : 
                  isCompleted ? `border-emerald-500/50 bg-emerald-500/5 shadow-lg shadow-emerald-500/10` : 
                  `border-${accentColor}/50 bg-${accentColor}/5 shadow-2xl shadow-${accentColor}/20 animate-float`}
              `}
            >
              <div className="flex justify-between items-start z-10">
                <div className={`p-3 rounded-2xl ${isCompleted ? 'bg-emerald-500' : isLocked ? 'bg-white/10' : `bg-${accentColor}`}`}>
                  {isCompleted ? <CheckCircle2 className="text-white" /> : <Zap className="text-white" />}
                </div>
                <span className="text-[10px] font-black uppercase tracking-widest text-white/30">Nivel {level.id}</span>
              </div>
              <div className="z-10">
                <h3 className="text-xl font-black text-white mb-2 leading-tight uppercase italic">{level.title}</h3>
                <p className="text-sm text-white/50 line-clamp-2 italic font-serif">"{level.description}"</p>
              </div>
              <div className="mt-auto flex justify-between items-center z-10 border-t border-white/5 pt-4 opacity-60">
                <span className="text-[9px] font-black uppercase tracking-widest">{level.difficulty}</span>
                <ChevronRight className="w-5 h-5" />
              </div>
            </motion.div>
          );
        })}
      </div>
    </motion.div>
  );
}
