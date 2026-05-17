import { useState } from 'react';
import { motion, AnimatePresence } from 'motion/react';
import { ShieldCheck, ArrowRight, Zap, Target, Award } from 'lucide-react';
import { JAVA_INTRO } from '../constants';

interface JavaIntroModalProps {
  onComplete: () => void;
}

export const JavaIntroModal = ({ onComplete }: JavaIntroModalProps) => {
  const [step, setStep] = useState(0);

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-navy/95 backdrop-blur-xl">
      <AnimatePresence mode="wait">
        <motion.div
          key={step}
          initial={{ opacity: 0, scale: 0.9, y: 20 }}
          animate={{ opacity: 1, scale: 1, y: 0 }}
          exit={{ opacity: 0, scale: 0.9, y: -20 }}
          className="bg-navy border border-white/10 rounded-[2.5rem] p-12 max-w-xl w-full relative overflow-hidden shadow-2xl"
        >
          {/* Header */}
          <div className="flex items-center gap-4 mb-8">
            <div className="w-12 h-12 bg-orange-kinal rounded-2xl flex items-center justify-center shadow-lg shadow-orange-kinal/20">
               <ShieldCheck className="text-navy" size={24} />
            </div>
            <div>
              <h2 className="text-white font-black uppercase tracking-tight text-xl">{JAVA_INTRO.title}</h2>
              <p className="text-steel font-mono text-[10px] uppercase tracking-widest">Protocolo de Inducción Técnica</p>
            </div>
          </div>

          {/* Content */}
          <div className="space-y-8 mb-10">
            <div className="p-6 bg-white/5 rounded-3xl border border-white/5">
               <h3 className="text-orange-kinal font-black text-sm uppercase mb-3 tracking-widest flex items-center gap-2">
                 <Zap size={14} /> Misión: {JAVA_INTRO.steps[step].title}
               </h3>
               <p className="text-white/80 leading-relaxed text-sm">
                 {JAVA_INTRO.steps[step].content}
               </p>
            </div>

            <div className="grid grid-cols-2 gap-4">
              <FeatureCard icon={<Target size={16} />} title="Precisión" desc="Un punto y coma puede salvar un sistema." />
              <FeatureCard icon={<Award size={16} />} title="Valores" desc="Ser técnico es ser íntegro." />
            </div>
          </div>

          {/* Action */}
          <button
            onClick={() => step < JAVA_INTRO.steps.length - 1 ? setStep(step + 1) : onComplete()}
            className="w-full bg-white text-navy font-black py-5 rounded-2xl hover:bg-orange-kinal hover:scale-[1.02] active:scale-[0.98] transition-all flex items-center justify-center gap-3 group uppercase tracking-widest text-sm"
          >
            {step < JAVA_INTRO.steps.length - 1 ? 'Siguiente Protocolo' : 'Iniciar Formación'}
            <ArrowRight size={20} className="group-hover:translate-x-1 transition-transform" />
          </button>
        </motion.div>
      </AnimatePresence>
    </div>
  );
};

const FeatureCard = ({ icon, title, desc }: { icon: React.ReactNode, title: string, desc: string }) => (
  <div className="p-4 bg-white/5 rounded-2xl border border-white/5">
    <div className="text-orange-kinal mb-2">{icon}</div>
    <h4 className="text-white font-bold text-xs uppercase mb-1">{title}</h4>
    <p className="text-steel text-[10px] leading-snug">{desc}</p>
  </div>
);
