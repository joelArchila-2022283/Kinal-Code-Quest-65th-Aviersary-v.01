import { motion } from 'motion/react';
import { Terminal, Shield, Users, Hammer, Award, ShoppingBag } from 'lucide-react';
import { PlayerStats, Rank } from '../types';
import { COSMETICS } from '../constants';

interface StatsProps {
  stats: PlayerStats;
  onEquip: (id: string) => void;
}

const getRank = (xp: number): Rank => {
  if (xp < 300) return 'Novato';
  if (xp < 800) return 'Técnico';
  if (xp < 1500) return 'Especialista';
  if (xp < 2500) return 'Ingeniero';
  return 'Maestro';
};

export const StatsPanel = ({ stats, onEquip }: StatsProps) => {
  const rank = getRank(stats.xp);

  return (
    <div className="w-80 bg-navy border-r border-white/10 p-8 flex flex-col gap-10 overflow-y-auto terminal-scroll">
      {/* Header Profile */}
      <div className="space-y-6">
        <div className="flex items-center gap-4">
          <div className="w-16 h-16 rounded-xl bg-orange-kinal p-0.5 shadow-lg shadow-orange-kinal/20 shrink-0">
            <div className="w-full h-full rounded-[10px] bg-navy flex items-center justify-center overflow-hidden">
                <div className="relative flex flex-col items-center">
                    <div className="w-10 h-10 flex items-center justify-center text-3xl relative">
                        {stats.avatar}
                        {stats.equippedCosmetics.includes('helmet_mech') && (
                          <div className="absolute -top-3 -left-1 text-lg">👷</div>
                        )}
                        {stats.equippedCosmetics.includes('goggles_elec') && (
                          <div className="absolute top-1 -left-1 text-xs">🥽</div>
                        )}
                    </div>
                    {stats.equippedCosmetics.includes('badge_honor') && (
                        <div className="absolute -bottom-1 -right-2 text-xs">🎖️</div>
                    )}
                </div>
            </div>
          </div>
          <div className="min-w-0">
            <h2 className="text-white font-black uppercase tracking-tight text-xl leading-tight truncate">{stats.name}</h2>
            <p className="text-orange-kinal font-mono text-[10px] uppercase tracking-widest mt-1 bg-orange-kinal/10 py-1 px-2 rounded inline-block">{rank}</p>
          </div>
        </div>

        {/* Level Progress */}
        <div className="space-y-2">
          <div className="flex justify-between text-[10px] uppercase font-mono text-steel">
            <span>Nivel {stats.level}</span>
            <span>{stats.xp % 100} / 100 XP</span>
          </div>
          <div className="h-1.5 w-full bg-white/5 rounded-full overflow-hidden">
            <motion.div 
              className="h-full bg-terminal"
              initial={{ width: 0 }}
              animate={{ width: `${stats.xp % 100}%` }}
            />
          </div>
        </div>
      </div>

      {/* Values Stats */}
      <div className="space-y-4">
        <h3 className="text-xs font-bold text-steel uppercase tracking-widest border-b border-white/10 pb-2">Valores Formativos</h3>
        <div className="space-y-4">
          <ValueBar icon={<Shield size={14} />} label="Responsabilidad" value={stats.responsabilidad} color="bg-orange-kinal" />
          <ValueBar icon={<Users size={14} />} label="Solidaridad" value={stats.solidaridad} color="bg-terminal" />
          <ValueBar icon={<Hammer size={14} />} label="Laboriosidad" value={stats.laboriosidad} color="bg-blue-500" />
        </div>
      </div>

      {/* Technical Inventory */}
      <div className="space-y-4">
        <h3 className="text-xs font-bold text-steel uppercase tracking-widest border-b border-white/10 pb-2 flex items-center gap-2">
          <ShoppingBag size={14} /> Inventario Técnico
        </h3>
        <div className="grid grid-cols-4 gap-2">
          {COSMETICS.map(item => {
            const isUnlocked = stats.unlockedCosmetics.includes(item.id);
            const isEquipped = stats.equippedCosmetics.includes(item.id);
            return (
              <button 
                key={item.id} 
                disabled={!isUnlocked}
                onClick={() => onEquip(item.id)}
                className={`w-10 h-10 rounded-lg flex items-center justify-center border transition-all relative ${
                  isUnlocked ? 
                    (isEquipped ? 'bg-terminal border-terminal scale-110 shadow-lg shadow-terminal/30' : 'bg-white/10 border-white/20 hover:border-terminal/50') 
                    : 'bg-black/20 border-white/5 opacity-30 cursor-not-allowed'
                }`}
                title={isUnlocked ? (isEquipped ? `Equipado: ${item.name}` : `Equipar: ${item.name}`) : `Bloqueado: ${item.requirement}`}
              >
                <span className={`text-lg ${isEquipped ? '' : 'grayscale opacity-50'}`}>{item.icon}</span>
                {isEquipped && (
                  <div className="absolute -top-1 -right-1 w-3 h-3 bg-white rounded-full flex items-center justify-center border border-terminal">
                    <div className="w-1.5 h-1.5 bg-terminal rounded-full" />
                  </div>
                )}
              </button>
            );
          })}
        </div>
      </div>
    </div>
  );
};

const ValueBar = ({ icon, label, value, color }: { icon: React.ReactNode, label: string, value: number, color: string }) => (
  <div className="space-y-2">
    <div className="flex items-center gap-2 text-steel">
      {icon}
      <span className="text-[10px] font-mono uppercase tracking-widest">{label}</span>
      <span className="ml-auto text-white font-black">{value}</span>
    </div>
    <div className="h-1 w-full bg-white/5 rounded-full overflow-hidden">
      <motion.div 
        className={`h-full ${color}`}
        initial={{ width: 0 }}
        animate={{ width: `${value}%` }}
      />
    </div>
  </div>
);
