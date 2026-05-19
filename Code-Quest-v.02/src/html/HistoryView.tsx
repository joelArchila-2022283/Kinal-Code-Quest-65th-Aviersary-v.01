import { motion } from 'motion/react';
import { CheckCircle2 } from 'lucide-react';
import { GameState } from '../js/types';

interface HistoryViewProps {
  gameState: GameState;
  accentColor: string;
}

export function HistoryView({ gameState, accentColor }: HistoryViewProps) {
  return (
    <motion.div 
      key="history"
      initial={{ opacity: 0 }} animate={{ opacity: 1 }} exit={{ opacity: 0 }}
      className="h-full flex flex-col gap-8"
    >
      <div className="flex flex-col gap-2">
        <h1 className="text-4xl font-black italic text-white uppercase tracking-tighter">Archivo <span className={`text-${accentColor}`}>Histórico</span></h1>
        <p className="text-white/30 text-sm italic font-serif">65 años de excelencia, restaurados por ti.</p>
      </div>

      <div className="flex-1 overflow-y-auto pr-4 custom-scrollbar space-y-6">
        <HistoryBlock year="1961" title="La Semilla" desc="Fundación de Kinal para brindar capacitación técnica a jóvenes guatemaltecos." />
        <HistoryBlock year="1970" title="Consolidación" desc="Expansión de talleres y profesionalización de las carreras técnicas." />
        <HistoryBlock year="1990" title="Era Digital" desc="Introducción de las primeras computadoras y laboratorios de informática." />
        <HistoryBlock year="2021" title="Sesenta Aniversario" desc="Referente nacional en formación integral y tecnológica." />
        
        <div className="mt-12 pt-8 border-t border-white/5">
           <h3 className="text-xl font-black text-white mb-6 uppercase tracking-tight">Tus Logros Recientes</h3>
           <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
             {gameState.unlockedMemories.length > 0 ? (
               gameState.unlockedMemories.map((m, i) => (
                <div key={i} className="p-5 glass-panel rounded-2xl flex items-center gap-4 relative overflow-hidden border-emerald-500/20">
                  <div className="absolute top-0 left-0 w-1 h-full bg-emerald-500" />
                  <CheckCircle2 className="text-emerald-500 w-6 h-6" />
                  <span className="text-white font-bold text-sm italic">{m}</span>
                </div>
               ))
             ) : (
              <div className="col-span-full py-12 text-center opacity-30 italic">No has completado misiones aún.</div>
             )}
           </div>
        </div>
      </div>
    </motion.div>
  );
}

function HistoryBlock({ year, title, desc }: { year: string, title: string, desc: string }) {
  return (
    <div className="flex gap-6 group">
      <div className="flex flex-col items-center">
        <div className="text-xs font-black text-white/30 rotate-90 w-12 h-12 flex items-center justify-center font-mono">{year}</div>
        <div className="w-0.5 flex-1 bg-white/5" />
      </div>
      <div className="flex-1 glass-panel p-6 rounded-3xl border-white/5 group-hover:border-kinal-wine/30 transition-all">
        <h4 className="text-xl font-black text-white italic uppercase mb-2">{title}</h4>
        <p className="text-sm text-white/50 leading-relaxed font-serif italic">"{desc}"</p>
      </div>
    </div>
  );
}
