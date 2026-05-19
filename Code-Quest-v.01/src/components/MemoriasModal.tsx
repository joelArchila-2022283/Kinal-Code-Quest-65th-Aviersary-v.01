import { motion } from 'motion/react';
import { History, X, Calendar } from 'lucide-react';

interface MemoriaProps {
  memoria: {
    year: string;
    description: string;
  };
  onClose: () => void;
}

export const MemoriasModal = ({ memoria, onClose }: MemoriaProps) => {
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-navy/80 backdrop-blur-md">
      <motion.div 
        initial={{ scale: 0.9, opacity: 0 }}
        animate={{ scale: 1, opacity: 1 }}
        className="bg-navy border-2 border-orange-kinal/30 rounded-[2.5rem] p-10 max-w-lg w-full text-center relative overflow-hidden shadow-2xl"
      >
        {/* Background decorative elements */}
        <div className="absolute top-0 left-0 w-full h-1 bg-orange-kinal" />
        <div className="absolute -top-24 -right-24 w-48 h-48 bg-orange-kinal/5 rounded-full blur-3xl" />

        <button 
          onClick={onClose}
          className="absolute top-6 right-6 text-steel hover:text-white transition-colors"
        >
          <X size={24} />
        </button>

        <div className="mb-8 flex justify-center">
          <div className="w-20 h-20 bg-orange-kinal/10 rounded-3xl flex items-center justify-center">
            <History size={40} className="text-orange-kinal" />
          </div>
        </div>

        <div className="inline-flex items-center gap-2 px-4 py-1.5 bg-orange-kinal text-navy font-black rounded-full text-sm mb-6 uppercase tracking-tighter">
          <Calendar size={16} />
          Memoria Técnica {memoria.year}
        </div>

        <h3 className="text-3xl font-black text-white mb-6 uppercase tracking-tight">Hito Histórico Kinal</h3>
        
        <div className="relative p-6 bg-white/5 rounded-3xl border border-white/5 italic text-steel leading-relaxed">
          <div className="absolute -top-3 left-6 px-2 bg-navy text-[10px] font-black text-orange-kinal uppercase tracking-widest">Archivo Digital</div>
          "{memoria.description}"
        </div>

        <button
          onClick={onClose}
          className="mt-10 w-full bg-white text-navy font-black py-4 rounded-2xl hover:bg-orange-kinal transition-all text-sm uppercase tracking-widest"
        >
          Continuar Formación
        </button>
      </motion.div>
    </div>
  );
};
