import { Level, CosmeticItem } from './types';

export const JAVA_INTRO = {
  title: "Kinal Code Quest: 65 Años de Excelencia",
  steps: [
    {
      title: "La Estructura Técnica",
      content: "En Kinal, cada pieza de código es como un componente en un motor industrial. Java es el lenguaje que mueve el mundo, desde sensores simples hasta grandes sistemas de manufactura."
    },
    {
      title: "Inducción al Taller Digital",
      content: "Como estudiante de Kinal, tu misión es reparar los módulos de software dañados en los talleres de Mecánica, Electricidad y Computación."
    },
    {
      title: "Protocolo de Compilación",
      content: "Analiza el código, identifica el error y aplica el parche lógico correcto. Un error puede detener toda la línea de producción."
    }
  ]
};

export const AVATARS = [
  { id: 'tech_a', icon: '🤖', name: 'Bot Kinal-25' },
  { id: 'tech_b', icon: '🚀', name: 'Piloto Espacial' },
  { id: 'tech_c', icon: '⚡', name: 'Ingeniero Eléctrico' },
  { id: 'tech_d', icon: '🛠️', name: 'Mecánico Pro' },
];

export const QUICK_GUIDE = [
  {
    unit: 1,
    title: "Módulo 1: Fundamentos Industriales",
    hints: [
      { mission: "Control de Calidad", answer: "Usa 'else' para definir el camino alternativo cuando el 'if' no se cumple." },
      { mission: "Variables de Potencia", answer: "Escribe 'double' para números con precisión decimal en mediciones." }
    ]
  },
  {
    unit: 2,
    title: "Módulo 2: Lógica de Control",
    hints: [
      { mission: "Ciclo de Producción", answer: "Usa un ciclo 'for' para repetir tareas automáticas un número fijo de veces." },
      { mission: "Integridad de Código", answer: "Toda instrucción en Java debe cerrarse con un ';' para que el compilador la procese." }
    ]
  },
  {
    unit: 3,
    title: "Módulo 3: Arquitectura POO",
    hints: [
      { mission: "Planos Técnicos", answer: "La palabra clave 'class' define la plantilla para crear objetos en Java." }
    ]
  }
];

export const COSMETICS: CosmeticItem[] = [
  { id: 'helmet_mech', name: 'Casco de Mecánica', icon: '👷', description: 'Protección para el taller.', requirement: 'Completa Nivel 1 perfecto' },
  { id: 'goggles_elec', name: 'Gafas de Seguridad', icon: '🥽', description: 'Visión técnica mejorada.', requirement: 'Completa Nivel 2 perfecto' },
  { id: 'badge_honor', name: 'Insignia de Honor', icon: '🎖️', description: 'Otorgado por alta solidaridad.', requirement: 'Solidaridad >= 50' },
  { id: 'master_chip', name: 'Microchip Maestro', icon: '🧬', description: 'Dominio total de Java.', requirement: 'Termina todos los niveles' }
];

export const LEVELS: Level[] = [
  {
    id: 1,
    unit: 1,
    title: "Control de Calidad (Sintaxis)",
    area: "Taller de Mecánica",
    description: "LOG: Error de estructura detectado. El sistema evalúa una pieza, pero no sabe qué hacer si NO cumple la calidad. Completa el bloque de cierre condicional.",
    codeBefore: "if (piezaCalidad >= 80) {\n    System.out.println(\"Pieza aprobada\");\n}\n",
    codeAfter: " {\n    System.out.println(\"Pieza defectuosa\");\n}",
    expectedAnswer: "else",
    options: ["else", "then", "switch", "catch"],
    explanation: "Usamos 'else' para definir la acción alternativa cuando el 'if' es falso. Es vital para la toma de decisiones binarias.",
    valuesAwarded: { responsabilidad: 25, solidaridad: 0, laboriosidad: 10 },
    npcEvent: {
      name: "Juan, Estudiante de Mecánica",
      description: "Juan está confundido sobre por qué su programa no funciona. ¿Le explicas qué es un 'else'?",
      choices: [
        { text: "Explicar amablemente", solidaridad: 15, dialog: "¡Gracias! Ahora entiendo que el else es para el caso contrario." },
        { text: "Ignorar y seguir", solidaridad: 0, dialog: "..." }
      ]
    },
    historicalUnlock: {
      year: "1961",
      description: "Fundación de Kinal: El inicio de un sueño educativo para la formación técnica en Guatemala.",
    }
  },
  {
    id: 2,
    unit: 1,
    title: "Variables de Potencia",
    area: "Laboratorio de Electricidad",
    description: "LOG: Sensor de voltaje offline. El sistema requiere definir una variable que acepte decimales (Precisión técnica).",
    codeBefore: "____ voltaje = 120.5;\nSystem.out.println(\"Voltaje actual: \" + voltaje);",
    codeAfter: "",
    expectedAnswer: "double",
    options: ["double", "int", "String", "boolean"],
    explanation: "El tipo 'double' permite almacenar números reales con decimales, esencial para mediciones eléctricas precisas.",
    valuesAwarded: { responsabilidad: 20, solidaridad: 5, laboriosidad: 15 },
    npcEvent: {
      name: "Instructor Pérez",
      description: "El instructor necesita ayuda organizando los multímetros. ¿Te ofreces?",
      choices: [
        { text: "Ayudar a ordenar", solidaridad: 20, dialog: "Excelente espíritu de servicio, joven." },
        { text: "Tengo prisa", solidaridad: 0, dialog: "Entiendo, el deber llama." }
      ]
    },
    historicalUnlock: {
      year: "1970",
      description: "Primeros talleres técnicos: Se inauguran las áreas de mecánica y electricidad industrial.",
    }
  },
  {
    id: 3,
    unit: 2,
    title: "Ciclo de Producción",
    area: "Línea de Ensamblaje AI",
    description: "LOG: Proceso de soldadura interrumpido. Necesitamos automatizar la repetición del brazo robótico para 10 componentes.",
    codeBefore: "for (int i = 0; i < 10; i____) {\n    soldarComponente();\n}",
    codeAfter: "",
    expectedAnswer: "++",
    options: ["++", "--", "+1", "+="],
    explanation: "El operador de incremento '++' aumenta el contador en 1 cada vuelta, permitiendo que el ciclo avance.",
    valuesAwarded: { responsabilidad: 15, solidaridad: 5, laboriosidad: 30 },
    historicalUnlock: {
      year: "1985",
      description: "Expansión Kinal: Se inicia la carrera de Electrónica, impulsando la automatización en el país.",
    }
  },
  {
    id: 4,
    unit: 2,
    title: "Integridad de Código",
    area: "Servidor Central Kinal",
    description: "LOG: Error de compilación en el protocolo de red. Falta el terminador de instrucción reglamentario.",
    codeBefore: "System.out.println(\"Enlace Estable\")____",
    codeAfter: "",
    expectedAnswer: ";",
    options: [";", ":", ".", ","],
    explanation: "El punto y coma ';' actúa como finalizador de sentencias en Java, separando las órdenes técnicas.",
    valuesAwarded: { responsabilidad: 20, solidaridad: 10, laboriosidad: 10 },
    npcEvent: {
      name: "Encargado de Sistemas",
      description: "El servidor está saturado de archivos basura. ¿Ayudas a limpiar el log?",
      choices: [
        { text: "Ejecutar limpieza", solidaridad: 10, dialog: "¡Excelente! Has optimizado el rendimiento del servidor." },
        { text: "Reportar y seguir", solidaridad: 5, dialog: "Correcto, seguir el protocolo es importante." }
      ]
    },
    historicalUnlock: {
      year: "1994",
      description: "Era Digital: Kinal introduce las primeras redes de computadoras para estudiantes.",
    }
  },
  {
    id: 5,
    unit: 3,
    title: "Planos Técnicos (Clases)",
    area: "Estudio de Diseño CAD",
    description: "LOG: Objeto 'Motor' sin definición base. El sistema requiere la palabra clave para declarar una nueva plantilla técnica.",
    codeBefore: "public ____ Motor {\n    int caballosFuerza;\n}",
    codeAfter: "",
    expectedAnswer: "class",
    options: ["class", "object", "struct", "void"],
    explanation: "Usamos 'class' para definir una clase, que es el 'plano' a partir del cual creamos objetos en POO.",
    valuesAwarded: { responsabilidad: 30, solidaridad: 20, laboriosidad: 20 },
    historicalUnlock: {
      year: "2011",
      description: "Celebración 50 años: Kinal se consolida como referente de educación técnica y valores.",
    }
  }
];
