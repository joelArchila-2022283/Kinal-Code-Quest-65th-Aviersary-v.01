import { motion } from 'motion/react';
import { Lightbulb } from 'lucide-react';

interface HelloWorldViewProps {
  accentColor: string;
}

export function HelloWorldView({ accentColor }: HelloWorldViewProps) {
  return (
    <motion.div 
      initial={{ opacity: 0 }} animate={{ opacity: 1 }} exit={{ opacity: 0 }}
      className="h-full flex flex-col gap-6"
    >
      <h1 className="text-4xl font-black italic text-white uppercase tracking-tighter">Mi Primer <span className={`text-${accentColor}`}>Hello World</span></h1>
      <div className="grid grid-cols-1 xl:grid-cols-2 gap-8 h-full overflow-hidden">
        <div className="glass-panel p-8 rounded-3xl flex flex-col gap-6 overflow-y-auto custom-scrollbar">
           <div className="bg-white/5 p-6 rounded-2xl border border-white/5 space-y-4">
             <h3 className="text-xl font-black text-white flex items-center gap-3 italic">
               <Lightbulb className={`text-${accentColor}`} />
               Guía Paso a Paso
             </h3>
             <p className="text-white/70 text-sm leading-relaxed font-serif">
               En Java, cada programa comienza con una <b className="text-white">Clase</b>. Imagina que es el "bloque" principal de tu construcción.
             </p>
           </div>
           
           <div className="space-y-6">
             <Step title="1. La Clase Principal" desc="Java requiere una clase con el mismo nombre que el archivo." code={`public class HolaMundo {`} />
             <Step title="2. El Punto de Entrada" desc="El método 'main' es donde la computadora comienza a leer." code={`  public static void main(String[] args) {`} />
             <Step title="3. La Instrucción de Salida" desc="Usamos 'System.out.println' para mostrar texto en pantalla." code={`    System.out.println("¡Hola de Kinal!");`} />
             <Step title="4. Cierre" desc="Cada inicio requiere un final. Cerramos los corchetes {}." code={`  }\n}`} />
           </div>
        </div>

        <div className="flex flex-col gap-6">
          <div className="bg-black/60 p-8 rounded-3xl border border-white/10 shadow-2xl flex-1 flex flex-col">
            <div className="flex items-center justify-between mb-6 pb-4 border-b border-white/5">
              <div className="flex items-center gap-2">
                 <div className="w-3 h-3 rounded-full bg-red-500" />
                 <div className="w-3 h-3 rounded-full bg-yellow-500" />
                 <div className="w-3 h-3 rounded-full bg-green-500" />
              </div>
              <span className="text-[9px] font-black text-white/20 uppercase tracking-widest font-mono">HelloWorld.java</span>
            </div>
            <pre className="text-lg md:text-xl font-mono text-white leading-relaxed flex-1">
              <span className="text-kinal-wine">public class</span> <span className={`text-${accentColor} font-bold`}>HolaMundo</span> {"{"} {"\n"}
              {"  "}<span className="text-kinal-wine">public static void</span> main(String[] args) {"{"} {"\n"}
              {"    "}System.out.println(<span className="text-emerald-400">"¡Hola de Kinal!"</span>); {"\n"}
              {"  "} {"}"} {"\n"}
              {"}"}
            </pre>
          </div>
          <div className="glass-panel p-6 rounded-3xl border-emerald-500/20 bg-emerald-500/5 text-center">
            <p className="text-emerald-400 font-mono text-xs uppercase tracking-widest mb-2 font-black">Consola de Salida</p>
            <div className="text-white text-xl font-black italic">¡Hola de Kinal!</div>
          </div>
        </div>
      </div>
    </motion.div>
  );
}

function Step({ title, desc, code }: { title: string, desc: string, code: string }) {
  return (
    <div className="space-y-2 border-l-2 border-white/5 pl-6 py-2">
      <h4 className="text-white font-black text-sm uppercase italic">{title}</h4>
      <p className="text-xs text-white/40 font-serif mb-2">{desc}</p>
      <code className="block bg-black/40 p-3 rounded-lg text-xs font-mono text-emerald-400 border border-white/5 truncate">{code}</code>
    </div>
  );
}
