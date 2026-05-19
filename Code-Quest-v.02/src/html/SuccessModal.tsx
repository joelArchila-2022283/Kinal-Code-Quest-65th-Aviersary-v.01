import { motion, AnimatePresence } from 'motion/react';
import { Globe, CheckCircle2 } from 'lucide-react';

interface SuccessModalProps {
  showSuccess: { memory: string, example: string } | null;
  setShowSuccess: (v: any) => void;
  accentColor: string;
}

export function SuccessModal({ showSuccess, setShowSuccess, accentColor }: SuccessModalProps) {
  return (
    <AnimatePresence>
      {showSuccess && (
        <motion.div 
          initial={{ opacity: 0 }} animate={{ opacity: 1 }} exit={{ opacity: 0 }}
          className="fixed inset-0 z-[100] flex items-center justify-center bg-black/90 backdrop-blur-3xl p-6"
        >
          <motion.div 
            initial={{ scale: 0.9, y: 30 }} animate={{ scale: 1, y: 0 }}
            className={`max-w-2xl w-full glass-panel p-10 md:p-16 rounded-[4rem] border-4 border-${accentColor} relative text-center shadow-2xl`}
          >
            <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-full h-full opacity-5 pointer-events-none">
              <Globe className={`w-full h-full text-${accentColor}`} />
            </div>

            <div className={`w-20 h-20 bg-${accentColor} rounded-3xl flex items-center justify-center mx-auto mb-8 shadow-2xl shadow-${accentColor}/40`}>
              <CheckCircle2 className="w-10 h-10 text-kinal-navy" />
            </div>
            
            <h2 className="text-4xl font-black italic tracking-tighter mb-4 text-white uppercase italic">SISTEMA RESTAURADO</h2>
            <div className={`w-16 h-1 bg-${accentColor} mx-auto mb-10 rounded-full`} />
            
            <div className="space-y-8 mb-12">
              <div className="space-y-2">
                <span className={`text-[9px] font-black uppercase tracking-widest text-${accentColor}`}>Archivo Histórico</span>
                <p className="text-white font-serif italic text-2xl leading-tight">"{showSuccess.memory}"</p>
              </div>
              <div className="p-6 bg-white/5 rounded-3xl border border-white/5 text-left relative overflow-hidden group">
                <span className={`text-[8px] font-black uppercase tracking-widest text-white/30 block mb-3`}>Impacto en el Mundo Real</span>
                <p className="text-white/80 text-sm leading-relaxed italic font-serif group-hover:text-white transition-colors">{showSuccess.example}</p>
              </div>
            </div>

            <button 
              onClick={() => setShowSuccess(null)}
              className={`w-full bg-${accentColor} text-kinal-navy font-black py-6 rounded-2xl hover:bg-opacity-90 transition-all uppercase tracking-[0.2em] text-xs shadow-2xl active:scale-95`}
            >
              Continuar Restauración
            </button>
          </motion.div>
        </motion.div>
      )}
    </AnimatePresence>
  );
}
