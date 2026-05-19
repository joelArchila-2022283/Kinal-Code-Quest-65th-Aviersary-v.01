import { motion } from 'motion/react';
import { Palette, ShieldCheck } from 'lucide-react';

interface CustomizationViewProps {
  accentColor: string;
  setAccentColor: (color: string) => void;
  addLog: (msg: string) => void;
}

const colors = [
  { name: 'Naranja Kinal', value: 'kinal-orange' },
  { name: 'Vino Kinal', value: 'kinal-wine' },
  { name: 'Cian Tech', value: 'cyan-400' },
  { name: 'Esmeralda', value: 'emerald-500' },
  { name: 'Oro Real', value: 'yellow-500' }
];

export function CustomizationView({ accentColor, setAccentColor, addLog }: CustomizationViewProps) {
  return (
    <motion.div 
       key="customization"
       initial={{ opacity: 0 }} animate={{ opacity: 1 }} exit={{ opacity: 0 }}
       className="h-full flex flex-col gap-8 max-w-2xl mx-auto items-center justify-center text-center"
    >
      <div className="w-20 h-20 bg-white/5 rounded-full flex items-center justify-center mb-4">
        <Palette className={`w-10 h-10 text-${accentColor}`} />
      </div>
      <h1 className="text-4xl font-black italic text-white uppercase tracking-tighter">Sala de <span className={`text-${accentColor}`}>Diseño</span></h1>
      <p className="text-white/50 italic font-serif">Elige el color de tu interfaz operativa institucional.</p>
      
      <div className="grid grid-cols-2 md:grid-cols-3 gap-6 w-full mt-8">
        {colors.map(c => (
          <button 
            key={c.value}
            onClick={() => { setAccentColor(c.value); addLog(`[SISTEMA]: Interfaz actualizada a ${c.name}.`); }}
            className={`p-6 glass-panel rounded-3xl border-2 transition-all flex flex-col items-center gap-3 hover:scale-105 active:scale-95
              ${accentColor === c.value ? `border-${accentColor} bg-${accentColor}/10` : 'border-white/5 hover:border-white/10'}`}
          >
            <div className={`w-12 h-12 rounded-full bg-${c.value} shadow-lg`} />
            <span className="text-[10px] font-black uppercase tracking-widest text-white/60">{c.name}</span>
          </button>
        ))}
      </div>
      
      <div className="mt-12 glass-panel p-8 rounded-[2rem] w-full flex items-center gap-6">
        <div className={`p-4 bg-${accentColor} rounded-2xl`}>
          <ShieldCheck className="w-8 h-8 text-kinal-navy" />
        </div>
        <div className="text-left">
          <h4 className="text-white font-black uppercase italic">Seguridad de Interfaz</h4>
          <p className="text-white/40 text-xs italic">Los cambios se aplican globalmente al núcleo de Kinal Code Quest.</p>
        </div>
      </div>
    </motion.div>
  );
}
