import { useState, useEffect } from 'react';
import { Menu, X } from 'lucide-react';
import { motion, AnimatePresence } from 'motion/react';

// Logic & Types imports (JS folder)
import { useGameState } from './js/useGameState';
import { Level } from './js/types';
import levelsData from './data/json/levels.json';

// View components imports (HTML folder)
import { IntroView } from './html/IntroView';
import { Sidebar } from './html/Sidebar';
import { MapView } from './html/MapView';
import { PlayingView } from './html/PlayingView';
import { HelloWorldView } from './html/HelloWorldView';
import { HistoryView } from './html/HistoryView';
import { CustomizationView } from './html/CustomizationView';
import { SuccessModal } from './html/SuccessModal';

export type ViewState = 'intro' | 'map' | 'playing' | 'helloWorld' | 'history' | 'customization';

export default function App() {
  const [view, setView] = useState<ViewState>('intro');
  const [playerName, setPlayerName] = useState('');
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [accentColor, setAccentColor] = useState('kinal-orange');
  
  const { gameState, setGameState, addLog, completeLevel } = useGameState();

  const [selectedOption, setSelectedOption] = useState<string | null>(null);
  const [feedback, setFeedback] = useState<string | null>(null);
  const [isProcessing, setIsProcessing] = useState(false);
  const [showSuccess, setShowSuccess] = useState<{ memory: string, example: string } | null>(null);

  const currentLevel: Level = (levelsData as Level[])[gameState.currentLevelIndex];

  useEffect(() => {
    setSelectedOption(null);
    setFeedback(null);
  }, [gameState.currentLevelIndex]);

  const handleStartGame = () => {
    if (!playerName.trim()) return;
    setGameState(prev => ({
      ...prev,
      stats: { ...prev.stats, playerName: playerName.trim() },
      logs: [...prev.logs, `[SISTEMA]: Bienvenido, Estudiante ${playerName.trim()}.`]
    }));
    setView('map');
  };

  const handleVerify = async () => {
    if (!selectedOption) return;
    setIsProcessing(true);
    setFeedback(null);

    const isCorrect = selectedOption.trim() === currentLevel.solution.trim();

    if (isCorrect) {
      addLog(`[SISTEMA]: Restaurando sistema... 100%`);
      setTimeout(() => {
        completeLevel(currentLevel);
        setIsProcessing(false);
        setShowSuccess({ 
          memory: `Hito Alcanzado: ${currentLevel.title}`, 
          example: currentLevel.realWorldExample 
        });
      }, 600);
    } else {
      try {
        const response = await fetch('/api/feedback', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            code: selectedOption,
            error: "Opción incorrecta seleccionada",
            levelContext: currentLevel.title,
            values: {
              responsibility: gameState.stats.responsibility,
              solidarity: gameState.stats.solidarity,
              laboriosity: gameState.stats.laboriosity
            }
          })
        });
        const data = await response.json();
        setFeedback(data.feedback);
        addLog(`[ERROR]: Decisión incorrecta en ${currentLevel.title}.`);
      } catch (err) {
        setFeedback("Error de comunicación con el núcleo de Kinal.");
      }
      setIsProcessing(false);
    }
  };

  if (view === 'intro') {
    return <IntroView playerName={playerName} setPlayerName={setPlayerName} onStart={handleStartGame} />;
  }

  return (
    <div className="flex flex-col lg:flex-row h-screen w-full bg-kinal-navy overflow-hidden relative">
      <div className="scanline" />
      
      {/* Mobile Header */}
      <div className="lg:hidden flex items-center justify-between p-4 glass-panel border-b border-white/10 z-30 relative bg-kinal-navy/90 backdrop-blur-md">
        <h1 className="text-xl font-black italic text-white tracking-tighter">
          KINAL <span className="text-kinal-wine">QUEST</span>
        </h1>
        <button 
          onClick={() => setSidebarOpen(!sidebarOpen)}
          className="p-2 bg-kinal-steel/50 rounded-xl text-white"
        >
          {sidebarOpen ? <X className="w-6 h-6" /> : <Menu className="w-6 h-6" />}
        </button>
      </div>

      <AnimatePresence>
        {sidebarOpen && (
          <motion.div 
            initial={{ opacity: 0 }} animate={{ opacity: 1 }} exit={{ opacity: 0 }}
            onClick={() => setSidebarOpen(false)}
            className="fixed inset-0 bg-black/60 backdrop-blur-sm z-40 lg:hidden"
          />
        )}
      </AnimatePresence>

      <Sidebar 
        gameState={gameState} 
        view={view} 
        setView={setView} 
        accentColor={accentColor} 
        sidebarOpen={sidebarOpen} 
        setSidebarOpen={setSidebarOpen} 
      />

      <main className="flex-1 flex flex-col p-4 md:p-8 gap-6 overflow-y-auto lg:overflow-hidden relative z-10">
        <AnimatePresence mode="wait">
          {view === 'map' && (
            <MapView 
              gameState={gameState} 
              accentColor={accentColor} 
              onSelectLevel={() => setView('playing')} 
            />
          )}

          {view === 'playing' && (
            <PlayingView 
              currentLevel={currentLevel} 
              gameState={gameState} 
              accentColor={accentColor} 
              selectedOption={selectedOption} 
              setSelectedOption={setSelectedOption} 
              onVerify={handleVerify} 
              onBack={() => setView('map')} 
              isProcessing={isProcessing}
            />
          )}

          {view === 'helloWorld' && <HelloWorldView accentColor={accentColor} />}
          {view === 'history' && <HistoryView gameState={gameState} accentColor={accentColor} />}
          {view === 'customization' && (
            <CustomizationView 
              accentColor={accentColor} 
              setAccentColor={setAccentColor} 
              addLog={addLog} 
            />
          )}
        </AnimatePresence>
      </main>

      <SuccessModal 
        showSuccess={showSuccess} 
        setShowSuccess={setShowSuccess} 
        accentColor={accentColor} 
      />
    </div>
  );
}
