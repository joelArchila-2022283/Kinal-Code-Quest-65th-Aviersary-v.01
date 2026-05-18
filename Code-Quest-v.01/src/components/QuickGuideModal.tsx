import { motion } from 'motion/react';
import { X, Lightbulb, CheckCircle2 } from 'lucide-react';
import { QUICK_GUIDE } from '../constants';

interface QuickGuideModalProps {
  onClose: () => void;
}

export const QuickGuideModal = ({ onClose }: QuickGuideModalProps) => {
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-navy/90 backdrop-blur-md">
      <motion.div
        initial={{ opacity: 0, scale: 0.95 }}
        animate={{ opacity: 1, scale: 1 }}
        className="bg-navy border border-white/10 rounded-3xl max-w-2xl w-full max-h-[85vh] overflow-hidden flex flex-col shadow-2xl"
      >
        <div className="p-6 border-b border-white/5 flex items-center justify-between shrink-0 bg-white/5">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 bg-orange-kinal rounded-xl flex items-center justify-center">
              <Lightbulb size={20} className="text-navy" />
            </div>
            <div>
              <h2 className="text-xl font-black text-white uppercase tracking-tight">Guía de Respuestas Rápidas</h2>
              <p className="text-steel font-mono text-[10px] uppercase tracking-widest">Protocolo de Asistencia Técnica</p>
            </div>
          </div>
          <button 
            onClick={onClose}
            className="p-2 hover:bg-white/5 rounded-full text-steel transition-colors"
          >
            <X size={24} />
          </button>
        </div>

        <div className="flex-1 overflow-y-auto p-8 space-y-10 terminal-scroll">
          {QUICK_GUIDE.map((module) => (
            <div key={module.unit} className="space-y-4">
              <div className="flex items-center gap-4">
                <span className="text-orange-kinal font-black text-lg">0{module.unit}</span>
                <h3 className="text-white font-bold uppercase tracking-widest text-sm">{module.title}</h3>
                <div className="flex-1 h-px bg-white/5" />
              </div>
              
              <div className="grid gap-3">
                {module.hints.map((hint, i) => (
                  <div key={i} className="p-4 bg-white/5 rounded-xl border border-white/5 group hover:border-orange-kinal/30 transition-colors">
                    <p className="text-[10px] font-mono text-steel uppercase mb-1">{hint.mission}</p>
                    <p className="text-white font-medium text-sm flex items-center gap-2">
                       <CheckCircle2 size={14} className="text-terminal" />
                       {hint.answer}
                    </p>
                  </div>
                ))}
              </div>
            </div>
          ))}
        </div>

        <div className="p-6 border-t border-white/5 bg-black/20 text-center shrink-0">
           <p className="text-steel/50 text-[10px] uppercase tracking-widest font-mono">
             Usa esta guía solo si los sistemas fallan críticamente. La excelencia requiere esfuerzo.
           </p>
        </div>
      </motion.div>
    </div>
  );
};
