import { motion, AnimatePresence } from 'motion/react';
import { Circle, CheckCircle2, Lock, Play, Star, BookOpen, Cpu } from 'lucide-react';
import { Level } from '../types';
import { LEVELS } from '../constants';
import { useState } from 'react';
import { QuickGuideModal } from './QuickGuideModal';

interface DashboardProps {
  currentLevelId: number;
  completedLevels: number[];
  onSelectLevel: (id: number) => void;
}

export const Dashboard = ({ currentLevelId, completedLevels, onSelectLevel }: DashboardProps) => {
  const [showGuide, setShowGuide] = useState(false);

  // Group levels by units
  const units = Array.from(new Set(LEVELS.map(l => l.unit))).sort((a, b) => a - b);

  return (
    <div className="flex-1 overflow-y-auto p-12 terminal-scroll bg-navy relative">
      <div className="max-w-4xl mx-auto space-y-24 relative z-10">
        <header className="flex flex-col md:flex-row items-center justify-between gap-8 mb-20 bg-white/5 p-8 rounded-3xl border border-white/10 shadow-2xl">
          <div className="text-center md:text-left space-y-2">
            <h2 className="text-3xl font-black text-white tracking-tighter uppercase">Mapa de Misiones</h2>
            <p className="text-steel font-mono tracking-widest text-[10px] uppercase">Ruta de Formación Técnica Kinal</p>
          </div>
          
          <button
            onClick={() => setShowGuide(true)}
            className="flex items-center gap-3 bg-orange-kinal text-navy font-black px-6 py-3 rounded-xl hover:scale-105 active:scale-95 transition-all shadow-lg shadow-orange-kinal/20"
          >
            <BookOpen size={18} />
            GUÍA DE RESPUESTAS
          </button>
        </header>

        <div className="space-y-32">
          {units.map((unitId) => {
            const unitLevels = LEVELS.filter(l => l.unit === unitId);
            return (
              <div key={unitId} className="space-y-12">
                <div className="flex items-center gap-6">
                  <div className="px-4 py-1.5 bg-terminal/10 border border-terminal/20 rounded-lg">
                    <span className="text-terminal font-black font-mono text-sm uppercase">Módulo 0{unitId}</span>
                  </div>
                  <div className="flex-1 h-px bg-white/5" />
                </div>

                <div className="grid md:grid-cols-2 gap-10">
                  {unitLevels.map((level) => {
                    const isCompleted = completedLevels.includes(level.id);
                    const isLocked = level.id > currentLevelId && !isCompleted;
                    const isCurrent = level.id === currentLevelId;

                    return (
                      <motion.button
                        key={level.id}
                        initial={{ opacity: 0, y: 20 }}
                        whileInView={{ opacity: 1, y: 0 }}
                        viewport={{ once: true }}
                        disabled={isLocked}
                        onClick={() => onSelectLevel(level.id)}
                        className={`
                          group relative flex items-center gap-6 p-6 rounded-2xl border transition-all text-left
                          ${isCompleted ? 'bg-terminal/5 border-terminal/30' : 
                            isCurrent ? 'bg-orange-kinal/10 border-orange-kinal shadow-[0_0_30px_rgba(247,148,29,0.1)]' : 
                            'bg-white/5 border-white/5'}
                          ${isLocked ? 'cursor-not-allowed opacity-40 grayscale' : 'hover:scale-[1.02] active:scale-98'}
                        `}
                      >
                        <div className={`
                          w-14 h-14 rounded-xl shrink-0 flex items-center justify-center transition-all
                          ${isCompleted ? 'bg-terminal text-navy' : 
                            isCurrent ? 'bg-orange-kinal text-navy shadow-lg shadow-orange-kinal/30' : 
                            'bg-white/10 text-white/30'}
                        `}>
                          {isCompleted ? <CheckCircle2 size={24} /> :
                           isLocked ? <Lock size={22} /> :
                           <Play size={22} className="fill-current ml-1" />}
                        </div>

                        <div className="flex-1 min-w-0">
                          <h3 className={`font-bold text-sm uppercase truncate ${isLocked ? 'text-steel' : 'text-white'}`}>
                            {level.title}
                          </h3>
                          <p className={`text-[10px] font-mono uppercase mt-1 ${isLocked ? 'text-steel/50' : 'text-orange-kinal'}`}>
                            {level.area}
                          </p>
                        </div>

                        {isCurrent && (
                          <div className="absolute -top-3 right-6 bg-orange-kinal text-navy text-[9px] font-black px-3 py-1 rounded-full uppercase">
                            Siguiente
                          </div>
                        )}
                      </motion.button>
                    );
                  })}
                </div>
              </div>
            );
          })}
        </div>

        {/* Java Learning Encyclopedia */}
        <section className="pt-20 border-t border-white/5 space-y-12">
          <div className="text-center space-y-4">
            <h2 className="text-3xl font-black text-white tracking-tighter uppercase italic">Manual de Consulta de Java</h2>
            <p className="text-steel font-mono tracking-widest text-[10px] uppercase max-w-xl mx-auto leading-relaxed">
              Base de conocimientos técnicos para cadetes. Consulta estos planos antes de iniciar cualquier reparación lógica.
            </p>
          </div>

          <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
            <ConceptCard 
              icon={<Cpu className="text-orange-kinal" size={24} />}
              title="Variables y Datos"
              content="En Java, usamos 'double' para números con decimales (como medidas de precisión) e 'int' para números enteros (conteo de piezas)."
            />
            <ConceptCard 
              icon={<Circle className="text-terminal" size={24} />}
              title="Lógica Binaria (IF/ELSE)"
              content="Un 'if' evalúa una condición. Si es falsa, el bloque 'else' se activa. Es como un switch de seguridad que elige entre dos caminos."
            />
            <ConceptCard 
              icon={<CheckCircle2 className="text-blue-500" size={24} />}
              title="Automatización (FOR)"
              content="El ciclo 'for' repite instrucciones. El comando '++' incrementa un contador cada vez, permitiendo que el ciclo avance hasta su límite."
            />
            <ConceptCard 
              icon={<Lock className="text-steel" size={24} />}
              title="Reglas de Energía (;)"
              content="Cada instrucción en Java es como una conexión eléctrica. Debe terminar con un punto y coma ';' para que la corriente lógica fluya."
            />
            <ConceptCard 
              icon={<Star className="text-orange-kinal" size={24} />}
              title="Planos Maestros (CLASS)"
              content="Una 'class' es un plano técnico. No es el objeto real, sino las instrucciones de cómo construirlo (ej. el plano de un Motor)."
            />
            <ConceptCard 
              icon={<BookOpen className="text-terminal" size={24} />}
              title="Compilación"
              content="Java 'compila' el código para leer errores. Si falta una pieza de sintaxis, el sistema se detiene para evitar accidentes lógicos."
            />
          </div>
        </section>
      </div>

      <AnimatePresence>
        {showGuide && <QuickGuideModal onClose={() => setShowGuide(false)} />}
      </AnimatePresence>
    </div>
  );
};

const ConceptCard = ({ icon, title, content }: { icon: React.ReactNode, title: string, content: string }) => (
  <motion.div 
    initial={{ opacity: 0, y: 10 }}
    whileInView={{ opacity: 1, y: 0 }}
    className="p-6 bg-white/5 border border-white/5 rounded-2xl space-y-4 hover:border-white/20 transition-all"
  >
    <div className="w-12 h-12 bg-white/5 rounded-xl flex items-center justify-center">
      {icon}
    </div>
    <div className="space-y-2">
      <h3 className="text-white font-bold text-sm uppercase tracking-tight">{title}</h3>
      <p className="text-steel text-[11px] leading-relaxed font-mono">
        {content}
      </p>
    </div>
  </motion.div>
);
