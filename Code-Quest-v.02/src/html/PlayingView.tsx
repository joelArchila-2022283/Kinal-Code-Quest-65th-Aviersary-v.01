import { motion } from 'motion/react';
import { ArrowLeft, Lightbulb, Zap, ShieldCheck, Heart, Wrench } from 'lucide-react';
import { Level, GameState } from '../js/types';

interface PlayingViewProps {
  currentLevel: Level;
  gameState: GameState;
  accentColor: string;
  selectedOption: string | null;
  setSelectedOption: (opt: string) => void;
  onVerify: () => void;
  onBack: () => void;
  isProcessing: boolean;
}

export function PlayingView({ currentLevel, gameState, accentColor, selectedOption, setSelectedOption, onVerify, onBack, isProcessing }: PlayingViewProps) {
  return (
    <motion.div 
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      exit={{ opacity: 0 }}
      className="h-full flex flex-col gap-6"
    >
      <div className="flex items-center justify-between">
        <button onClick={onBack} className="flex items-center gap-2 text-white/40 hover:text-white transition-colors group">
          <ArrowLeft className="w-5 h-5 group-hover:-translate-x-1 transition-transform" />
          <span className="text-[10px] font-black uppercase tracking-widest">Abandonar Misión</span>
        </button>
        <div className="flex items-center gap-3">
           <div className={`w-3 h-3 rounded-full bg-${accentColor} animate-pulse`} />
           <span className="text-[10px] font-black text-white uppercase tracking-widest">En Línea // Restauración</span>
        </div>
      </div>

      <div className="flex-1 flex flex-col xl:flex-row gap-6 overflow-hidden">
        <div className="flex-1 flex flex-col gap-6 overflow-y-auto lg:overflow-hidden pr-2 custom-scrollbar">
          <div className={`glass-panel p-6 rounded-3xl border-l-4 border-${accentColor} shadow-lg`}>
            <div className="flex items-center gap-3 mb-3">
              <Lightbulb className={`w-5 h-5 text-${accentColor}`} />
              <h4 className="text-[10px] font-black uppercase tracking-widest text-white/60">Lección del Mentor</h4>
            </div>
            <p className="text-white/80 italic font-serif text-sm leading-relaxed">{currentLevel.lesson}</p>
          </div>

          <div className="glass-panel flex-1 rounded-3xl flex flex-col border-white/5 overflow-hidden">
            <div className="p-6 md:p-8 flex-1 overflow-y-auto">
               <div className="bg-black/40 rounded-2xl p-6 mb-6 font-mono text-sm border border-white/5 shadow-inner">
                  <pre className="text-white/60 whitespace-pre-wrap">{currentLevel.codeTemplate.split('____')[0]}</pre>
                  <div className={`my-4 py-3 px-5 border-l-4 border-${accentColor} bg-${accentColor}/5 rounded-r-xl`}>
                    {selectedOption ? (
                      <span className={`text-${accentColor} font-black terminal-glow`}>{selectedOption}</span>
                    ) : (
                      <span className="text-white/10 animate-pulse italic">Esperando instrucción...</span>
                    )}
                  </div>
                  <pre className="text-white/60 whitespace-pre-wrap">{currentLevel.codeTemplate.split('____')[1]}</pre>
               </div>

               <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                {currentLevel.options.map((opt, i) => (
                  <button
                    key={i}
                    onClick={() => setSelectedOption(opt)}
                    className={`p-4 rounded-xl text-left font-mono text-xs border-2 transition-all group
                      ${selectedOption === opt ? `border-${accentColor} bg-${accentColor}/10 text-white` : 'border-white/5 bg-white/5 text-white/40 hover:border-white/10'}`}
                  >
                    <span className="opacity-30 mr-2">0{i+1}.</span> {opt}
                  </button>
                ))}
               </div>
            </div>
            <div className="p-6 bg-white/2 border-t border-white/5 flex justify-end">
              <button 
                onClick={onVerify}
                disabled={!selectedOption || isProcessing}
                className={`bg-${accentColor} text-kinal-navy font-black px-10 py-4 rounded-xl flex items-center gap-3 uppercase tracking-widest text-xs shadow-xl active:scale-95 disabled:opacity-20`}
              >
                 {isProcessing ? <div className="w-4 h-4 border-2 border-kinal-navy border-t-transparent rounded-full animate-spin" /> : <Zap className="w-5 h-5 fill-current" />}
                 Ejecutar Código
              </button>
            </div>
          </div>
        </div>

        <div className="w-full xl:w-72 flex flex-col gap-6">
          <div className="glass-panel p-6 rounded-3xl border-white/5">
            <h3 className="text-[9px] font-black text-white/30 uppercase tracking-widest mb-4">Contexto de Misión</h3>
            <h4 className="text-xl font-black text-white mb-2 italic leading-tight uppercase">{currentLevel.title}</h4>
            <p className="text-xs text-white/50 leading-relaxed font-serif italic">{currentLevel.context}</p>
          </div>
          
          <div className="flex-1 glass-panel p-6 rounded-3xl border-white/5 flex flex-col">
            <h3 className="text-[9px] font-black text-white/30 uppercase tracking-widest mb-4">Progreso de Valores</h3>
            <div className="space-y-4">
              <CompactValue icon={<ShieldCheck />} label="Respon." value={gameState.stats.responsibility} color="emerald-500" />
              <CompactValue icon={<Heart />} label="Solida." value={gameState.stats.solidarity} color="kinal-wine" />
              <CompactValue icon={<Wrench />} label="Labori." value={gameState.stats.laboriosity} color={accentColor} />
            </div>
          </div>
        </div>
      </div>
    </motion.div>
  );
}

function CompactValue({ icon, label, value, color }: { icon: any, label: string, value: number, color: string }) {
  return (
    <div className="space-y-2">
      <div className="flex justify-between items-center text-[8px] font-black uppercase tracking-widest">
        <div className="flex items-center gap-2 opacity-30">{icon} {label}</div>
        <span className="text-white">{value}%</span>
      </div>
      <div className="h-1 bg-white/5 rounded-full overflow-hidden">
        <motion.div initial={{ width: 0 }} animate={{ width: `${value}%` }} className={`h-full bg-${color}`} />
      </div>
    </div>
  );
}
