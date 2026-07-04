// Inicialización global del editor CodeMirror
var textareaEditor = document.getElementById('editorCodigo');

var editor = CodeMirror.fromTextArea(textareaEditor, {
    lineNumbers: true,
    mode: "text/x-java",
    theme: "dracula",
    lineWrapping: true,
    matchBrackets: true,
    indentUnit: 4,
    tabSize: 4,
    indentWithTabs: false,
    smartIndent: true
});

editor.setSize(null, "465px");

/**
 * Muestra el modal personalizado estilo Cyberpunk reemplazando al alert nativo
 * @param {string} mensaje
 * @param {'SUCCESS' | 'ERROR'} tipo
 */
function mostrarCyberAlert(mensaje, tipo) {
    const modal = document.getElementById('cyber-alert');
    const card = modal.querySelector('.cyber-modal-card');
    const title = document.getElementById('cyber-alert-title');
    const icon = document.getElementById('cyber-alert-icon');
    const msgContainer = document.getElementById('cyber-alert-message');

    card.className = "cyber-modal-card";

    if (tipo === 'SUCCESS') {
        card.classList.add('alert-success-cyber');
        title.innerText = '[COMPILACIÓN_EXITOSA]';
        icon.innerHTML = '🎉';
    } else {
        card.classList.add('alert-error-cyber');
        title.innerText = '[CORE_COMPILE_ERROR]';
        icon.innerHTML = '❌';
    }

    msgContainer.innerText = mensaje;
    modal.classList.remove('d-none');
}

function cerrarCyberAlert() {
    document.getElementById('cyber-alert').classList.add('d-none');
}

// Escuchador de envío del formulario de compilación principal
document.getElementById("compilerForm").addEventListener("submit", function(event) {
    event.preventDefault();
    editor.save(); // Guarda el estado actual de CodeMirror en el textarea

    const form = event.target;
    const formData = new FormData(form);
    const btn = document.getElementById("btnEjecutar");
    const btnSpan = btn.querySelector("span");
    const btnIcon = btn.querySelector("i");

    btn.disabled = true;
    btnSpan.innerText = "COMPILANDO_SISTEMA...";
    btnIcon.className = "";
    btnIcon.innerHTML = '<span class="loading-spinner"></span>';

    const urlEndpoint = form.getAttribute("action");
    const consoleOutput = document.getElementById("textoConsola");
    const scrollConsola = document.getElementById("scrollConsola");

    consoleOutput.innerText = "C:\\KinalCodeQuest\\compiler> Inicializando compilador...\nEnviando fuentes del sistema a la JVM remota...\n";

    fetch(urlEndpoint, {
        method: "POST",
        body: new URLSearchParams(formData),
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
            "X-Requested-With": "XMLHttpRequest"
        }
    })
        .then(response => {
            if (!response.ok) throw new Error("Respuesta inválida del servidor HTTP.");
            return response.json();
        })
        .then(data => {
            consoleOutput.innerHTML = '';

            const lines = data.consolaResult.split('\n');
            lines.forEach(line => {
                if (line.trim() !== '') {
                    const span = document.createElement('span');
                    span.className = 'console-line';

                    if (line.includes('[SUCCESS]') || line.includes('PROCESS_FINISHED_OK')) {
                        span.className += ' console-success';
                    } else if (line.includes('[ERROR]') || line.includes('[CRITICAL ERROR]') || line.includes('PROCESS_FAILED')) {
                        span.className += ' console-error';
                    } else if (line.includes('[INFO]') || line.includes('[WARNING]')) {
                        span.className += ' console-info';
                    } else if (line.includes('>>>') || line.includes('Exception')) {
                        span.className += ' console-error';
                    } else if (line.includes('--') || line.includes('JVM')) {
                        span.className += ' console-info';
                    }

                    span.textContent = line + '\n';
                    consoleOutput.appendChild(span);
                }
            });

            const badge = document.getElementById("badgeStatus");
            const statusIcon = document.getElementById("statusIcon");
            const errorIcon = document.getElementById("errorIcon");
            const timeValue = document.getElementById("timeValue");
            const badgeTime = document.getElementById("badgeTime");

            timeValue.textContent = data.tiempoEjecucion;
            badgeTime.style.display = 'inline-block';

            if (data.success) {
                consoleOutput.classList.add('console-success');
                consoleOutput.classList.remove('console-error');
                badge.className = "badge font-monospace text-uppercase bg-success bg-opacity-25 text-success border border-success";
                badge.textContent = "SUCCESS ✓";
                statusIcon.style.display = 'inline-block';
                errorIcon.style.display = 'none';
            } else if (data.executionSuccess) {
                consoleOutput.classList.remove('console-success');
                consoleOutput.classList.remove('console-error');
                badge.className = "badge font-monospace text-uppercase bg-warning bg-opacity-25 text-warning border border-warning";
                badge.textContent = "EXECUTION_OK - CRITERIA_NOT_MET";
                statusIcon.style.display = 'none';
                errorIcon.style.display = 'inline-block';
            } else {
                consoleOutput.classList.remove('console-success');
                consoleOutput.classList.add('console-error');
                badge.className = "badge font-monospace text-uppercase bg-danger bg-opacity-25 text-danger border border-danger";
                badge.textContent = "FAILED ✗";
                statusIcon.style.display = 'none';
                errorIcon.style.display = 'inline-block';
            }

            document.getElementById("contadorIntentos").innerText = data.intentos;
            document.getElementById("debugInfo").textContent =
                (data.success ? "✓ Misión completada" : "✗ Compilación fallida") + " | Tiempo: " + data.tiempoEjecucion + "ms";

            scrollConsola.scrollTop = scrollConsola.scrollHeight;
        })
        .catch(err => {
            consoleOutput.innerHTML = '<span class="console-error">[CRITICAL ERROR] No se pudo establecer conexión con el compilador.\n' + err.message + '</span>';
            const badge = document.getElementById("badgeStatus");
            badge.className = "badge font-monospace text-uppercase bg-danger bg-opacity-25 text-danger border border-danger";
            badge.textContent = "CONNECTION_ERROR";
            console.error(err);
        })
        .finally(() => {
            btn.disabled = false;
            btnSpan.innerText = "EJECUTAR_KINAL_COMPILER";
            btnIcon.innerHTML = '';
            btnIcon.className = "bi bi-play-fill fs-5";
        });
});

// Shortcuts de teclado para compilación rápida
editor.setOption("extraKeys", {
    "Ctrl-Enter": function(cm) {
        document.getElementById("compilerForm").requestSubmit();
    },
    "Cmd-Enter": function(cm) {
        document.getElementById("compilerForm").requestSubmit();
    }
});

/**
 * Envía la solución del mini-ejercicio actual al backend para validarla
 * @param {number} idEjercicio
 * @param {HTMLButtonElement} btn
 */
function completarEjercicio(idEjercicio, btn) {
    if (typeof editor === 'undefined') {
        mostrarCyberAlert("🚨 Error: No se pudo detectar el editor de código CodeMirror.", "ERROR");
        return;
    }

    const codigoFormulario = editor.getValue();
    const textoOriginal = btn.innerText;

    btn.disabled = true;
    btn.innerText = "VALIDANDO...";

    fetch('/game/ejercicio/' + idEjercicio + '/completar', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-Requested-With': 'XMLHttpRequest'
        },
        body: JSON.stringify({ codigo: codigoFormulario })
    })
        .then(r => {
            if (!r.ok) throw new Error('Error en la comunicación con el servidor.');
            return r.json();
        })
        .then(data => {
            if (data.awarded) {
                mostrarCyberAlert(`¡Excelente trabajo! ${data.mensaje || 'Ejercicio validado con éxito.'} (+10 Ptos de Laboriosidad)`, "SUCCESS");

                const badgeLaboriosidad = document.getElementById("badgeLaboriosidad");
                if (badgeLaboriosidad && data.nuevosPuntosLaboriosidad !== undefined && data.nuevosPuntosLaboriosidad !== null) {
                    badgeLaboriosidad.innerText = data.nuevosPuntosLaboriosidad;
                }

                btn.innerText = "COMPLETADO";
                btn.classList.remove("btn-outline-success");
                btn.classList.add("btn-success");
                btn.disabled = true;
            } else {
                mostrarCyberAlert(`Error: ${data.mensaje || 'El código no cumple los criterios requeridos.'}`, "ERROR");
                btn.disabled = false;
                btn.innerText = textoOriginal;
            }
        })
        .catch(err => {
            console.error(err);
            mostrarCyberAlert('🚨 No se pudo establecer la conexión para completar el ejercicio.', "ERROR");
            btn.disabled = false;
            btn.innerText = textoOriginal;
        });
}

// Funciones de colapsar logs laterales
function toggleLog() {
    var content = document.getElementById('logContent');
    var icon = document.getElementById('toggleIcon');
    if (!content) return;
    content.classList.toggle('d-none');
    if (icon) {
        icon.className = content.classList.contains('d-none') ? 'bi bi-chevron-right' : 'bi bi-chevron-down';
    }
}

function toggleMissionCode(id, btn) {
    var el = document.getElementById(id);
    if (!el) return;
    el.classList.toggle('d-none');
    if (btn) {
        btn.innerText = el.classList.contains('d-none') ? 'Ver código' : 'Ocultar código';
    }
}

function toggleExerciseCode(id, btn) {
    var el = document.getElementById(id);
    if (!el) return;
    el.classList.toggle('d-none');
    if (btn) {
        btn.innerText = el.classList.contains('d-none') ? 'Ver plantilla' : 'Ocultar plantilla';
    }
}

// Funciones utilitarias para portapapeles y carga rápida en consola
function copyMissionCode(id) {
    var el = document.querySelector('#' + id + ' pre');
    if (!el) return;
    var text = el.innerText || el.textContent;
    if (navigator.clipboard) {
        navigator.clipboard.writeText(text).then(function(){ mostrarCyberAlert('Código de misión copiado al portapapeles con éxito.', 'SUCCESS'); });
    } else {
        var ta = document.createElement('textarea'); ta.value = text; document.body.appendChild(ta); ta.select(); try { document.execCommand('copy'); mostrarCyberAlert('Código de misión copiado al portapapeles con éxito.', 'SUCCESS'); } catch(e){ mostrarCyberAlert('No se pudo copiar el código.', 'ERROR'); } ta.remove();
    }
}

function copyExerciseCode(id) {
    var el = document.querySelector('#' + id + ' pre');
    if (!el) return;
    var text = el.innerText || el.textContent;
    if (navigator.clipboard) {
        navigator.clipboard.writeText(text).then(function(){ mostrarCyberAlert('Plantilla de ejercicio copiada al portapapeles con éxito.', 'SUCCESS'); });
    } else {
        var ta = document.createElement('textarea'); ta.value = text; document.body.appendChild(ta); ta.select(); try { document.execCommand('copy'); mostrarCyberAlert('Plantilla de ejercicio copiada al portapapeles con éxito.', 'SUCCESS'); } catch(e){ mostrarCyberAlert('No se pudo copiar el código.', 'ERROR'); } ta.remove();
    }
}

function loadMissionIntoEditor(missionId) {
    var pre = document.querySelector('#mission-code-' + missionId + ' pre');
    if (!pre || typeof editor === 'undefined') return;
    editor.setValue(pre.innerText || pre.textContent);
    editor.focus();
}

function loadExerciseIntoEditor(exerciseId) {
    var pre = document.querySelector('#' + exerciseId + ' pre');
    if (!pre || typeof editor === 'undefined') return;
    editor.setValue(pre.innerText || pre.textContent);
    editor.focus();
}