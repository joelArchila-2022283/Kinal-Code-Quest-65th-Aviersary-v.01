import { motion } from 'motion/react';
import { Trophy, Clock, Zap, Star, ShieldCheck } from 'lucide-react';
import { CosmeticItem } from '../types';

interface SuccessModalProps {
  stats: {
    xp: number;
    errors: number;
    time: number;
    isPerfect: boolean;
  };
  unlockedCosmetic?: CosmeticItem;
  onNext: () => void;
}

export const SuccessModal = ({ stats, unlockedCosmetic, onNext }: SuccessModalProps) => {
  return (
    <div className="fixed inset-0 z-[70] flex items-center justify-center p-4 bg-navy/95 backdrop-blur-xl">
      <motion.div
        initial={{ opacity: 0, scale: 0.9 }}
        animate={{ opacity: 1, scale: 1 }}
        className="bg-black/50 border border-white/10 rounded-[2.5rem] p-10 max-w-lg w-full text-center shadow-[0_0_50px_rgba(34,197,94,0.1)] space-y-10"
      >
        <header className="space-y-4">
          <motion.div
            initial={{ y: -20 }}
            animate={{ y: 0 }}
            className="w-20 h-20 bg-terminal/10 rounded-3xl mx-auto flex items-center justify-center border border-terminal/30"
          >
            <Trophy className="text-terminal" size={40} />
          </motion.div>
          <h2 className="text-4xl font-black text-white uppercase tracking-tighter">Misión Cumplida</h2>
          <p className="text-terminal font-mono text-xs uppercase tracking-widest">Sistemas Restaurados Exitosamente</p>
        </header>

        <div className="grid grid-cols-2 gap-4">
          <div className="p-6 bg-white/5 rounded-3xl border border-white/5 space-y-2">
            <Clock size={20} className="text-steel mx-auto" />
            <p className="text-2xl font-black text-white">{stats.time.toFixed(1)}s</p>
            <p className="text-[10px] font-mono text-steel uppercase">Tiempo</p>
          </div>
          <div className="p-6 bg-white/5 rounded-3xl border border-white/5 space-y-2">
            <Zap size={20} className="text-orange-kinal mx-auto" />
            <p className="text-2xl font-black text-white">{stats.errors}</p>
            <p className="text-[10px] font-mono text-steel uppercase">Errores</p>
          </div>
        </div>

        {stats.isPerfect && (
          <motion.div 
            initial={{ opacity: 0, x: -20 }}
            animate={{ opacity: 1, x: 0 }}
            className="bg-orange-kinal/10 border border-orange-kinal/30 p-4 rounded-2xl flex items-center gap-4"
          >
            <div className="w-10 h-10 bg-orange-kinal rounded-xl flex items-center justify-center shrink-0">
              <Star size={20} className="text-navy fill-current" />
            </div>
            <div className="text-left">
              <p className="text-orange-kinal font-black text-xs uppercase">Bono de Perfección</p>
              <p className="text-white/70 text-[10px] font-mono">0 Errores detectados + XP Adicional</p>
            </div>
            <div className="ml-auto text-orange-kinal font-black">
              +50 XP
            </div>
          </motion.div>
        )}

        {unlockedCosmetic && (
          <motion.div 
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            className="p-6 bg-terminal/10 border border-terminal shadow-[0_0_20px_rgba(34,197,94,0.2)] rounded-3xl flex items-center gap-6"
          >
            <div className="text-5xl">{unlockedCosmetic.icon}</div>
            <div className="text-left">
              <p className="text-terminal font-black text-sm uppercase">¡Nuevo Equipo!</p>
              <h4 className="text-white font-bold text-lg">{unlockedCosmetic.name}</h4>
              <p className="text-white/60 text-[10px] font-mono">{unlockedCosmetic.description}</p>
            </div>
          </motion.div>
        )}

        <button
          onClick={onNext}
          className="w-full bg-white text-navy font-black py-5 rounded-2xl hover:bg-terminal transition-all text-sm uppercase tracking-widest flex items-center justify-center gap-3"
        >
          Siguiente Misión
          <ShieldCheck size={20} />
        </button>
      </motion.div>
    </div>
  );
};
