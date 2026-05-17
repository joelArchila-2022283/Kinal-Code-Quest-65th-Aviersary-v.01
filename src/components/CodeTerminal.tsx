import { useState, useEffect, useRef } from 'react';
import { motion } from 'motion/react';
import { Terminal as TerminalIcon, Send, AlertTriangle, Info, Book } from 'lucide-react';
import { Level } from '../types';

interface TerminalProps {
  level: Level;
  onSuccess: (errors: number, time: number) => void;
  onFailure: (msg: string) => void;
}

export const CodeTerminal = ({ level, onSuccess, onFailure }: TerminalProps) => {
  const [input, setInput] = useState('');
  const [shuffledOptions, setShuffledOptions] = useState<string[]>([]);
  const [logs, setLogs] = useState<{ type: 'info' | 'error' | 'success', text: string }[]>([]);
  const [showManual, setShowManual] = useState(false);
  const [errorCount, setErrorCount] = useState(0);
  const [startTime] = useState(Date.now());
  const scrollRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    setLogs([
      { type: 'info', text: `SYS_INITIALIZE: ACCEDIENDO A ${level.area.toUpperCase()}...` },
      { type: 'info', text: `IDENTIFICADO: NIVEL_${level.id} - ${level.title.toUpperCase()}` },
      { type: 'info', text: `DESCRIPCIÓN: ${level.description}` }
    ]);
    setInput('');
    setShowManual(false);
    setErrorCount(0);
    setShuffledOptions([...level.options].sort(() => Math.random() - 0.5));
  }, [level]);

  useEffect(() => {
    if (scrollRef.current) {
      scrollRef.current.scrollTop = scrollRef.current.scrollHeight;
    }
  }, [logs]);

  const handleCompile = (val: string) => {
    setInput(val);
    const isCorrect = val.trim().toLowerCase() === level.expectedAnswer.toLowerCase();

    setLogs(prev => [...prev, { type: 'info', text: `> Running: javac SystemModule.java (Patch: ${val})` }]);

    setTimeout(() => {
      if (isCorrect) {
        const timeElapsed = (Date.now() - startTime) / 1000;
        setLogs(prev => [...prev, 
          { type: 'success', text: 'STATUS [SUCCESS]: BUILD COMPLETED WITHOUT ERRORS.' },
          { type: 'success', text: `PRECISIÓN: ${errorCount === 0 ? 'PERFECTA (100%)' : 'RESTAURADA'}` },
          { type: 'info', text: `SISTEMA: ${level.explanation}` }
        ]);
        onSuccess(errorCount, timeElapsed);
      } else {
        setErrorCount(prev => prev + 1);
        setLogs(prev => [...prev, 
          { type: 'error', text: 'CRITICAL ERROR [SYNTAX]: COMPILATION FAILED.' },
          { type: 'error', text: 'TIP TÉCNICO: ¿Has revisado el Manual de Campo?' }
        ]);
        onFailure('Error de compilación detectado.');
      }
    }, 800);
  };

  return (
    <div className="flex-1 bg-black/40 flex flex-col font-mono text-sm overflow-hidden">
      {/* Terminal Output */}
      <div ref={scrollRef} className="flex-1 p-8 overflow-y-auto space-y-3 terminal-scroll">
        {logs.map((log, i) => (
          <div 
            key={i} 
            className={`flex gap-3 ${
              log.type === 'error' ? 'text-red-400' : 
              log.type === 'success' ? 'text-terminal' : 
              'text-steel'
            }`}
          >
            <span className="shrink-0">{log.type === 'error' ? '[!]' : '[+]'}</span>
            <span className="leading-relaxed whitespace-pre-wrap">{log.text}</span>
          </div>
        ))}
      </div>

      {/* Manual Popover */}
      {showManual && (
        <motion.div 
          initial={{ y: 20, opacity: 0 }}
          animate={{ y: 0, opacity: 1 }}
          className="mx-8 mb-4 p-4 bg-navy border border-orange-kinal/30 rounded-lg shadow-xl"
        >
          <div className="flex items-center gap-2 text-orange-kinal mb-2 font-black text-[10px] uppercase tracking-widest">
            <AlertTriangle size={14} /> Protocolo de Emergencia
          </div>
          <p className="text-white/80 text-xs leading-relaxed italic">
            "{level.explanation}"
          </p>
        </motion.div>
      )}

      {/* Editor Area */}
      <div className="p-6 bg-black/20 border-t border-white/10 space-y-6">
        <div className="bg-navy/40 p-6 rounded-lg border border-white/5 font-mono text-lg relative overflow-hidden">
          <div className="absolute top-0 left-0 w-1 h-full bg-terminal" />
          <pre className="text-steel/50 mb-1 leading-relaxed whitespace-pre-wrap">{level.codeBefore}</pre>
          <div className="flex items-center gap-2">
            <span className={`inline-block px-4 py-1 rounded border-2 border-dashed transition-all ${input ? 'border-terminal text-terminal bg-terminal/10' : 'border-white/20 text-white/5'}`}>
               {input || '____'}
            </span>
            <span className="text-steel/50">{level.codeAfter}</span>
          </div>
        </div>

        {/* Options Grid */}
        <div className="space-y-4">
           <div className="flex items-center justify-between">
              <span className="text-[10px] font-mono text-steel uppercase tracking-widest">Selecciona el parche lógico:</span>
           </div>
           <div className="grid grid-cols-2 gap-3">
              {shuffledOptions.map((opt) => (
                <motion.button
                  key={opt}
                  whileHover={{ scale: 1.02 }}
                  whileTap={{ scale: 0.98 }}
                  onClick={() => handleCompile(opt)}
                  className={`
                    py-4 px-6 rounded-xl font-mono text-sm transition-all border uppercase tracking-widest
                    ${input === opt ? 
                      'bg-terminal border-terminal text-navy shadow-lg shadow-terminal/20' : 
                      'bg-white/5 border-white/10 text-white hover:bg-white/10 hover:border-white/20'}
                  `}
                >
                  {opt}
                </motion.button>
              ))}
           </div>
        </div>
      </div>
    </div>
  );
};
