import { useState } from 'react';
import { motion, AnimatePresence } from 'motion/react';
import { Cpu, ArrowRight, User, Palette } from 'lucide-react';
import { AVATARS } from '../constants';

interface IntroViewProps {
  onStart: (name: string, avatar: string) => void;
}

export const IntroView = ({ onStart }: IntroViewProps) => {
  const [step, setStep] = useState(0);
  const [name, setName] = useState('');
  const [selectedAvatar, setSelectedAvatar] = useState(AVATARS[0]);

  const handleNext = () => {
    if (step === 0) setStep(1);
    else if (step === 1 && name.trim()) setStep(2);
    else if (step === 2) onStart(name, selectedAvatar.icon);
  };

  return (
    <div className="min-h-screen bg-navy flex items-center justify-center p-8 bg-[radial-gradient(circle_at_center,rgba(247,148,29,0.1)_0%,transparent_70%)]">
      <AnimatePresence mode="wait">
        {step === 0 ? (
          <motion.div 
            key="start"
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            exit={{ opacity: 0, y: -20 }}
            className="text-center space-y-12 max-w-2xl px-4"
          >
            <div className="space-y-4">
              <div className="w-24 h-24 bg-orange-kinal mx-auto rounded-[2.5rem] flex items-center justify-center shadow-2xl shadow-orange-kinal/20 relative overflow-hidden group">
                 <Cpu className="text-navy" size={48} />
                 <div className="absolute inset-x-0 bottom-0 h-1 bg-white/30" />
              </div>
              <h1 className="text-7xl font-black text-white tracking-tighter uppercase italic leading-none">
                Kinal Code
                <span className="block text-orange-kinal not-italic text-3xl mt-2">Quest 65th Edition</span>
              </h1>
            </div>
            
            <div className="space-y-6">
              <p className="text-steel font-mono text-xs uppercase tracking-[0.3em] font-bold">Iniciando Protocolo Educativo v6.5</p>
              <button 
                onClick={handleNext}
                className="bg-white text-navy font-black px-12 py-5 rounded-2xl hover:bg-orange-kinal hover:scale-105 active:scale-95 transition-all text-sm uppercase tracking-widest shadow-xl flex items-center gap-3 mx-auto"
              >
                Ingresar al Sistema
                <ArrowRight size={20} />
              </button>
            </div>
          </motion.div>
        ) : step === 1 ? (
          <motion.div 
            key="name"
            initial={{ opacity: 0, x: 50 }}
            animate={{ opacity: 1, x: 0 }}
            exit={{ opacity: 0, x: -50 }}
            className="w-full max-w-md space-y-8"
          >
            <div className="flex items-center gap-4 mb-10">
              <div className="w-12 h-12 bg-orange-kinal rounded-xl flex items-center justify-center">
                <User className="text-navy" size={24} />
              </div>
              <div>
                <h2 className="text-2xl font-black text-white uppercase tracking-tight">Registro de Cadete</h2>
                <p className="text-steel font-mono text-[10px] uppercase tracking-widest">Identificación de Usuario</p>
              </div>
            </div>

            <div className="space-y-4">
              <label className="text-[10px] font-mono text-steel uppercase tracking-widest block ml-2">Nombre del Estudiante:</label>
              <input 
                type="text"
                autoFocus
                value={name}
                onChange={(e) => setName(e.target.value)}
                placeholder="Escribe tu nombre..."
                className="w-full bg-white/5 border-2 border-white/10 rounded-2xl p-5 text-white font-bold outline-none focus:border-orange-kinal transition-all text-lg"
              />
            </div>

            <button 
              disabled={!name.trim()}
              onClick={handleNext}
              className="w-full bg-white disabled:opacity-20 text-navy font-black py-5 rounded-2xl hover:bg-orange-kinal transition-all text-sm uppercase tracking-widest shadow-xl flex items-center justify-center gap-3"
            >
              Confirmar Identidad
              <ArrowRight size={20} />
            </button>
          </motion.div>
        ) : (
          <motion.div 
            key="avatar"
            initial={{ opacity: 0, x: 50 }}
            animate={{ opacity: 1, x: 0 }}
            className="w-full max-w-2xl space-y-10"
          >
            <div className="flex items-center gap-4 mb-10">
              <div className="w-12 h-12 bg-orange-kinal rounded-xl flex items-center justify-center">
                <Palette className="text-navy" size={24} />
              </div>
              <div>
                <h2 className="text-2xl font-black text-white uppercase tracking-tight">Selección de Perfil</h2>
                <p className="text-steel font-mono text-[10px] uppercase tracking-widest">Apariencia Técnica</p>
              </div>
            </div>

            <div className="grid grid-cols-2 md:grid-cols-4 gap-6">
              {AVATARS.map((av) => (
                <button
                  key={av.id}
                  onClick={() => setSelectedAvatar(av)}
                  className={`
                    p-8 rounded-[2.5rem] border-2 transition-all group relative
                    ${selectedAvatar.id === av.id ? 'bg-orange-kinal border-orange-kinal scale-110 shadow-2xl' : 'bg-white/5 border-white/10 hover:border-white/30'}
                  `}
                >
                  <span className="text-6xl mb-4 block group-hover:scale-110 transition-transform">{av.icon}</span>
                  <p className={`text-[10px] font-mono uppercase tracking-widest font-black ${selectedAvatar.id === av.id ? 'text-navy' : 'text-steel'}`}>
                    {av.name}
                  </p>
                  {selectedAvatar.id === av.id && (
                    <motion.div layoutId="glow" className="absolute inset-0 bg-white/20 blur-xl -z-10 rounded-full" />
                  )}
                </button>
              ))}
            </div>

            <div className="flex justify-center pt-10">
              <button 
                onClick={handleNext}
                className="w-full max-w-sm bg-white text-navy font-black py-5 rounded-2xl hover:bg-orange-kinal transition-all text-sm uppercase tracking-widest shadow-xl flex items-center justify-center gap-3"
              >
                Comenzar Misión
                <ArrowRight size={20} />
              </button>
            </div>
          </motion.div>
        )}
      </AnimatePresence>
    </div>
  );
};
