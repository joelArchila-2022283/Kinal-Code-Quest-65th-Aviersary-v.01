import { motion } from 'motion/react';
import { User, Play } from 'lucide-react';

interface IntroViewProps {
  playerName: string;
  setPlayerName: (name: string) => void;
  onStart: () => void;
}

export function IntroView({ playerName, setPlayerName, onStart }: IntroViewProps) {
  return (
    <div className="min-h-screen w-full bg-kinal-navy flex items-center justify-center p-6 relative overflow-hidden">
      <div className="scanline" />
      <motion.div 
        initial={{ opacity: 0, scale: 0.9, y: 30 }}
        animate={{ opacity: 1, scale: 1, y: 0 }}
        className="max-w-xl w-full glass-panel p-8 md:p-12 rounded-[3rem] border-2 border-kinal-wine/30 relative text-center shadow-2xl"
      >
        <div className="absolute top-0 left-1/2 -translate-x-1/2 -translate-y-1/2 bg-kinal-wine px-6 py-2 rounded-full border border-kinal-orange/50 shadow-lg">
          <span className="text-[10px] font-black tracking-[0.4em] uppercase text-white">65 Aniversario</span>
        </div>
        
        <motion.h1 
          initial={{ letterSpacing: '0.1em' }}
          animate={{ letterSpacing: '-0.02em' }}
          className="text-4xl md:text-6xl font-black italic tracking-tighter text-white mb-4 mt-6 uppercase leading-none"
        >
          KINAL CODE <span className="text-kinal-wine">QUEST</span>
        </motion.h1>
        <p className="text-kinal-orange font-mono text-xs mb-10 tracking-[0.3em] uppercase opacity-80">
          Formación Técnica de Excelencia
        </p>

        <div className="space-y-8 max-w-sm mx-auto">
          <div className="text-left space-y-3">
            <label className="text-[10px] uppercase font-black text-white/40 tracking-widest ml-1 text-center block w-full">Identificador del Estudiante</label>
            <div className="relative group">
              <User className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-white/20 group-focus-within:text-kinal-orange transition-colors" />
              <input 
                type="text" 
                value={playerName}
                onChange={(e) => setPlayerName(e.target.value)}
                placeholder="Escribe tu nombre..."
                className="w-full bg-kinal-steel/30 border-2 border-white/5 rounded-2xl px-12 py-5 focus:border-kinal-orange focus:bg-kinal-steel/50 outline-none transition-all text-xl font-bold placeholder:text-white/10"
              />
            </div>
          </div>
          
          <motion.button 
            whileHover={{ scale: 1.02 }}
            whileTap={{ scale: 0.98 }}
            onClick={onStart}
            disabled={!playerName.trim()}
            className="w-full bg-kinal-orange hover:bg-orange-600 disabled:opacity-20 text-kinal-navy font-black py-5 rounded-2xl flex items-center justify-center gap-4 transition-all shadow-2xl shadow-kinal-orange/30 uppercase tracking-[0.2em] text-sm"
          >
            <Play className="w-5 h-5 fill-current" />
            Acceder al Campus
          </motion.button>
        </div>
      </motion.div>
    </div>
  );
}
