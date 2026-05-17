import { motion } from 'motion/react';
import { Users, Heart, MessageSquare } from 'lucide-react';

interface NPCEventModalProps {
  event: {
    name: string;
    description: string;
    choices: Array<{
      text: string;
      solidaridad: number;
      dialog: string;
    }>;
  };
  onComplete: (solidaridad: number) => void;
}

export const NPCEventModal = ({ event, onComplete }: NPCEventModalProps) => {
  return (
    <div className="fixed inset-0 z-[60] flex items-center justify-center p-4 bg-black/80 backdrop-blur-sm">
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="bg-navy border border-white/10 rounded-3xl p-8 max-w-md w-full shadow-2xl space-y-8"
      >
        <div className="flex items-center gap-4">
          <div className="w-12 h-12 bg-terminal/10 rounded-2xl flex items-center justify-center">
            <Users className="text-terminal" />
          </div>
          <div>
            <h3 className="text-white font-bold uppercase tracking-tight">{event.name}</h3>
            <p className="text-steel font-mono text-[10px] uppercase tracking-widest text-terminal">Evento de Solidaridad</p>
          </div>
        </div>

        <div className="p-6 bg-white/5 rounded-2xl border border-white/5 italic text-steel/80 text-sm leading-relaxed">
          "{event.description}"
        </div>

        <div className="grid gap-3">
          {event.choices.map((choice, i) => (
            <button
              key={i}
              onClick={() => onComplete(choice.solidaridad)}
              className="group flex items-center justify-between p-4 bg-white/5 hover:bg-terminal/10 border border-white/5 hover:border-terminal/30 rounded-xl transition-all text-left"
            >
              <span className="text-white font-medium text-sm">{choice.text}</span>
              <div className="flex items-center gap-1.5 text-terminal opacity-0 group-hover:opacity-100 transition-opacity">
                <Heart size={14} className="fill-current" />
                <span className="text-[10px] font-black">+{choice.solidaridad} XP</span>
              </div>
            </button>
          ))}
        </div>

        <p className="text-center text-[10px] font-mono text-steel/40 uppercase tracking-widest">
          Tus acciones definen tu carácter técnico
        </p>
      </motion.div>
    </div>
  );
};
