// ====================================
// EJEMPLOS DE CÓDIGO PARA TESTEAR LA CONSOLA
// ====================================

// 👉 COPIA Y PEGA ESTOS EJEMPLOS EN LA CONSOLA PARA TESTEAR

// ====================================
// EJEMPLO 1: Hello World - Básico ✅
// ====================================
public class Main {
    public static void main(String[] args) {
        System.out.println("¡Hola desde KinalCodeQuest!");
    }
}


// ====================================
// EJEMPLO 2: Operaciones Matemáticas ✅
// ====================================
public class Calculator {
    public static void main(String[] args) {
        int num1 = 15;
        int num2 = 7;

        System.out.println("Suma: " + (num1 + num2));
        System.out.println("Resta: " + (num1 - num2));
        System.out.println("Multiplicación: " + (num1 * num2));
        System.out.println("División: " + (num1 / num2));
    }
}


// ====================================
// EJEMPLO 3: Bucle For ✅
// ====================================
public class ForLoop {
    public static void main(String[] args) {
        System.out.println("Números del 1 al 5:");
        for (int i = 1; i <= 5; i++) {
            System.out.println("Número: " + i);
        }
    }
}


// ====================================
// EJEMPLO 4: Array y Bucle ✅
// ====================================
public class ArrayExample {
    public static void main(String[] args) {
        int[] numeros = {10, 20, 30, 40, 50};

        System.out.println("Números en el array:");
        for (int num : numeros) {
            System.out.println(num);
        }
    }
}


// ====================================
// EJEMPLO 5: Condicionales If-Else ✅
// ====================================
public class Conditional {
    public static void main(String[] args) {
        int edad = 18;

        if (edad >= 18) {
            System.out.println("Eres mayor de edad");
        } else {
            System.out.println("Eres menor de edad");
        }
    }
}


// ====================================
// EJEMPLO 6: Métodos ✅
// ====================================
public class Methods {

    static int sumar(int a, int b) {
        return a + b;
    }

    static void saludar(String nombre) {
        System.out.println("¡Hola, " + nombre + "!");
    }

    public static void main(String[] args) {
        System.out.println("Resultado: " + sumar(10, 20));
        saludar("Juan");
    }
}


// ====================================
// EJEMPLO 7: Clase con Constructor ✅
// ====================================
public class Person {
    String name;
    int age;

    Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    void display() {
        System.out.println("Nombre: " + name);
        System.out.println("Edad: " + age);
    }

    public static void main(String[] args) {
        Person p = new Person("Carlos", 25);
        p.display();
    }
}


// ====================================
// EJEMPLO 8: While Loop ✅
// ====================================
public class WhileLoop {
    public static void main(String[] args) {
        int contador = 0;

        System.out.println("Contando del 0 al 3:");
        while (contador <= 3) {
            System.out.println("Contador: " + contador);
            contador++;
        }
    }
}


// ====================================
// EJEMPLO 9: Try-Catch ✅
// ====================================
public class ErrorHandling {
    public static void main(String[] args) {
        try {
            int resultado = 100 / 5;
            System.out.println("Resultado: " + resultado);

            String texto = "123";
            int numero = Integer.parseInt(texto);
            System.out.println("Número convertido: " + numero);

        } catch (ArithmeticException e) {
            System.out.println("Error matemático: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error al convertir número: " + e.getMessage());
        }
    }
}


// ====================================
// EJEMPLO 10: ArrayList ✅
// ====================================
import java.util.ArrayList;

public class ListExample {
    public static void main(String[] args) {
        ArrayList<String> frutas = new ArrayList<>();

        frutas.add("Manzana");
        frutas.add("Banana");
        frutas.add("Naranja");

        System.out.println("Frutas:");
        for (String fruta : frutas) {
            System.out.println("- " + fruta);
        }
    }
}


// ====================================
// EJEMPLO 11: Switch Statement ✅
// ====================================
public class SwitchExample {
    public static void main(String[] args) {
        int dia = 3;

        switch (dia) {
            case 1:
                System.out.println("Lunes");
                break;
            case 2:
                System.out.println("Martes");
                break;
            case 3:
                System.out.println("Miércoles");
                break;
            default:
                System.out.println("Otro día");
        }
    }
}


// ====================================
// EJEMPLO 12: Nested Loops ✅
// ====================================
public class NestedLoops {
    public static void main(String[] args) {
        System.out.println("Tabla de multiplicar del 3:");

        for (int i = 1; i <= 5; i++) {
            System.out.println("3 x " + i + " = " + (3 * i));
        }
    }
}


// ====================================
// EJEMPLO 13: String Operations ✅
// ====================================
public class StringOps {
    public static void main(String[] args) {
        String texto = "Kinal Code Quest";

        System.out.println("Texto original: " + texto);
        System.out.println("Mayúsculas: " + texto.toUpperCase());
        System.out.println("Minúsculas: " + texto.toLowerCase());
        System.out.println("Longitud: " + texto.length());
        System.out.println("Contiene 'Code': " + texto.contains("Code"));
    }
}


// ====================================
// EJEMPLO 14: For-Each Loop ✅
// ====================================
public class ForEachLoop {
    public static void main(String[] args) {
        String[] colores = {"Rojo", "Verde", "Azul", "Amarillo"};

        System.out.println("Colores:");
        for (String color : colores) {
            System.out.println("* " + color);
        }
    }
}


// ====================================
// EJEMPLO 15: Recursión ✅
// ====================================
public class Recursion {

    static int factorial(int n) {
        if (n == 1 || n == 0) {
            return 1;
        }
        return n * factorial(n - 1);
    }

    public static void main(String[] args) {
        int num = 5;
        System.out.println("Factorial de " + num + " es: " + factorial(num));
    }
}


// ====================================
// EJEMPLO CON ERROR (Para testear error handling) ❌
// ====================================
public class WithError {
    public static void main(String[] args) {
        // Esta línea causará un error porque falta punto y coma
        System.out.println("Error intencional")
    }
}


// ====================================
// EJEMPLO CON EXCEPCIÓN (Para testear manejo de excepciones) ❌
// ====================================
public class WithException {
    public static void main(String[] args) {
        int[] numeros = {1, 2, 3};
        System.out.println(numeros[10]); // Index out of bounds
    }
}


// ====================================
// CONSEJOS PARA TESTEAR:
// ====================================
// 1. Copia el código completo (incluyendo la clase)
// 2. Pégalo en el editor de la consola
// 3. Haz clic en "EJECUTAR_KINAL_COMPILER"
// 4. Observa el resultado en la terminal
// 5. Verifica los tiempos de ejecución
// 6. Prueba diferentes criterios de validación


// ====================================
// CRITERIOS DE VALIDACIÓN REGEX:
// ====================================
// .*System\.out\.println.* - Contiene println
// .*for.* - Contiene bucle for
// .*if.* - Contiene condicional if
// .*class.* - Contiene definición de clase
// .*import.* - Contiene importación
// .*public.*static.*void.*main.* - Contiene main válido

