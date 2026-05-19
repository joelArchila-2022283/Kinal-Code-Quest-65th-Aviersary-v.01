import { motion } from 'motion/react';
import { User, Map as MapIcon, Coffee, History, Palette } from 'lucide-react';
import { GameState } from '../js/types';

interface SidebarProps {
  gameState: GameState;
  view: string;
  setView: (view: any) => void;
  accentColor: string;
  sidebarOpen: boolean;
  setSidebarOpen: (open: boolean) => void;
}

export function Sidebar({ gameState, view, setView, accentColor, sidebarOpen, setSidebarOpen }: SidebarProps) {
  return (
    <aside className={`
      fixed lg:static inset-y-0 left-0 z-50
      w-80 glass-panel p-6 flex flex-col gap-6 border-r border-white/10 h-full overflow-y-auto transition-transform duration-500
      ${sidebarOpen ? 'translate-x-0' : '-translate-x-full lg:translate-x-0'}
      bg-kinal-navy lg:bg-transparent
    `}>
      <div className="flex flex-col items-center gap-4 mt-2">
         <motion.div 
          whileHover={{ rotate: 5, scale: 1.05 }}
          className={`w-28 h-28 rounded-3xl border-4 border-${accentColor} p-1 bg-kinal-steel/50 shadow-2xl rotate-3`}
         >
           <div className="w-full h-full rounded-2xl bg-kinal-navy flex items-center justify-center">
             <User className={`w-14 h-14 text-${accentColor}`} />
           </div>
         </motion.div>
         <div className="text-center">
          <h2 className="text-xl font-black text-white uppercase italic">{gameState.stats.playerName}</h2>
          <span className={`inline-block px-3 py-1 bg-${accentColor}/10 border border-${accentColor}/30 text-${accentColor} text-[8px] font-black uppercase tracking-widest rounded-full`}>
             {gameState.stats.rank}
          </span>
         </div>
      </div>

      <nav className="flex flex-col gap-2">
        <SidebarLink icon={<MapIcon />} label="Mapa de Misiones" active={view === 'map'} onClick={() => { setView('map'); setSidebarOpen(false); }} />
        <SidebarLink icon={<Coffee />} label="Mi Primer Java" active={view === 'helloWorld'} onClick={() => { setView('helloWorld'); setSidebarOpen(false); }} />
        <SidebarLink icon={<History />} label="Archivo Histórico" active={view === 'history'} onClick={() => { setView('history'); setSidebarOpen(false); }} />
        <SidebarLink icon={<Palette />} label="Personalización" active={view === 'customization'} onClick={() => { setView('customization'); setSidebarOpen(false); }} />
      </nav>

      <div className="mt-auto space-y-6 pt-4 border-t border-white/5">
        <div className="space-y-4 bg-black/20 p-4 rounded-2xl border border-white/5">
          <div className="flex justify-between text-[9px] font-black text-white/40 tracking-widest uppercase">
            <span>NIVEL {gameState.stats.level}</span>
            <span className={`text-${accentColor}`}>{gameState.stats.xp} XP</span>
          </div>
          <div className="h-1.5 bg-kinal-steel/50 rounded-full overflow-hidden">
            <motion.div 
              className={`h-full bg-${accentColor}`}
              initial={{ width: 0 }}
              animate={{ width: `${(gameState.stats.xp % 500) / 5}%` }}
            />
          </div>
        </div>
        
        <div className="font-mono text-[8px] space-y-2 opacity-40 px-2 italic leading-tight">
          {gameState.logs.slice(-4).map((log, i) => (
            <div key={i} className="truncate border-l-2 border-white/10 pl-3">
              {log}
            </div>
          ))}
        </div>
      </div>
    </aside>
  );
}

function SidebarLink({ icon, label, active, onClick }: { icon: any, label: string, active: boolean, onClick: () => void }) {
  return (
    <button 
      onClick={onClick}
      className={`w-full flex items-center gap-4 px-5 py-4 rounded-2xl transition-all group relative overflow-hidden
        ${active ? 'bg-kinal-wine/20 text-white shadow-lg' : 'text-white/40 hover:text-white hover:bg-white/5'}`}
    >
      {active && <motion.div layoutId="activeNav" className="absolute left-0 top-1/2 -translate-y-1/2 w-1.5 h-6 bg-kinal-wine rounded-full" />}
      <div className={`transition-transform duration-300 group-hover:scale-110 ${active ? 'text-kinal-wine' : 'opacity-50'}`}>{icon}</div>
      <span className="text-[10px] font-black uppercase tracking-widest text-left leading-none">{label}</span>
    </button>
  );
}
