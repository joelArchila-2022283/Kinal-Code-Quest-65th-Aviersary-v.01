import express from "express";
import path from "path";
import { createServer as createViteServer } from "vite";
import { GoogleGenAI } from "@google/genai";
import dotenv from "dotenv";

dotenv.config();

const app = express();
const PORT = 3000;

app.use(express.json());

// Initialize Gemini API
const genAI = new GoogleGenAI({
  apiKey: process.env.GEMINI_API_KEY || "",
  httpOptions: {
    headers: {
      "User-Agent": "aistudio-build",
    },
  },
});

const model = "gemini-3-flash-preview";

// API Endpoints
app.post("/api/feedback", async (req, res) => {
  const { code, error, levelContext, values } = req.body;

  if (!process.env.GEMINI_API_KEY) {
    return res.status(500).json({ error: "Gemini API key not configured." });
  }

  try {
    const prompt = `Actúa como un mentor experto del Centro Educativo Técnico Laboral Kinal. 
Un estudiante está resolviendo un desafío de Java en el juego "Kinal Code Quest".
Nivel: ${levelContext}
Código del estudiante: ${code}
Error/Problema: ${error}
Valores actuales del estudiante: Responsabilidad(${values.responsibility}), Solidaridad(${values.solidarity}), Laboriosidad(${values.laboriosity}).

Genera un mensaje motivador y pedagógico en español que:
1. Use un enfoque basado en los valores de Kinal (Laboriosidad, Solidaridad, Responsabilidad).
2. No dé la respuesta directa, sino pistas técnicas precisas.
3. Tenga un tono profesional e institucional.
4. Mencione que "En Kinal, la precisión técnica garantiza la excelencia".`;

    const result = await genAI.models.generateContent({
      model: model,
      contents: prompt,
    });

    res.json({ feedback: result.text });
  } catch (err) {
    console.error("Gemini Error:", err);
    res.status(500).json({ error: "Error procesando el feedback." });
  }
});

// Vite Middleware
async function startServer() {
  if (process.env.NODE_ENV !== "production") {
    const vite = await createViteServer({
      server: { middlewareMode: true },
      appType: "spa",
    });
    app.use(vite.middlewares);
  } else {
    const distPath = path.join(process.cwd(), "dist");
    app.use(express.static(distPath));
    app.get("*", (req, res) => {
      res.sendFile(path.join(distPath, "index.html"));
    });
  }

  app.listen(PORT, "0.0.0.0", () => {
    console.log(`Server running on http://localhost:${PORT}`);
  });
}

startServer();
