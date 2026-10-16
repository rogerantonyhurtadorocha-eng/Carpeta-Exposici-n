import java.util.Scanner;

class Fraccion {

    long numerador;
    long denominador;

    Fraccion(long numerador, long denominador) {
        if (denominador < 0) {
            numerador = -numerador;
            denominador = -denominador;
        }
        long g = mcd(Math.abs(numerador), denominador);
        if (g == 0) {
            g = 1;
        }
        this.numerador = numerador / g;
        this.denominador = denominador / g;
    }

    static long mcd(long a, long b) {
        while (b != 0) {
            long resto = a % b;
            a = b;
            b = resto;
        }
        return a;
    }

    Fraccion restar(Fraccion otra) {
        return new Fraccion(
                numerador * otra.denominador - otra.numerador * denominador,
                denominador * otra.denominador);
    }

    Fraccion multiplicar(Fraccion otra) {
        return new Fraccion(
                numerador * otra.numerador,
                denominador * otra.denominador);
    }

    Fraccion dividir(Fraccion otra) {
        return new Fraccion(
                numerador * otra.denominador,
                denominador * otra.numerador);
    }

    boolean esCero() {
        return numerador == 0;
    }

    @Override
    public String toString() {
        if (denominador == 1) {
            return String.valueOf(numerador);
        }
        return numerador + "/" + denominador;
    }
}

public class EjercicioJava {

    static Fraccion ingresarNumero(Scanner sc, String mensaje) {

        while (true) {

            System.out.print(mensaje);
            String dato = sc.nextLine().trim();

            try {

                if (dato.contains("/")) {

                    String[] partes = dato.split("/");

                    if (partes.length != 2) {
                        throw new NumberFormatException();
                    }

                    long numerador = Long.parseLong(partes[0].trim());
                    long denominador = Long.parseLong(partes[1].trim());

                    if (denominador == 0) {
                        System.out.print("Error: el denominador no puede ser cero.");
                        continue;
                    }

                    return new Fraccion(numerador, denominador);
                }

                else if (dato.contains(".")) {

                    int decimales = dato.length() - dato.indexOf('.') - 1;
                    long numerador = Long.parseLong(dato.replace(".", ""));
                    long denominador = (long) Math.pow(10, decimales);

                    return new Fraccion(numerador, denominador);
                }

                else {
                    return new Fraccion(Long.parseLong(dato), 1);
                }

            } catch (NumberFormatException e) {

                System.out.println();
                System.out.print("Dato incorrecto.");
                System.out.print("Ingrese un número entero, decimal o fracción.");
                System.out.print("Ejemplos: 5, 2.5, 3/4, -7/3");
                System.out.println();
            }
        }
    }

    static void mostrarMatriz(String nombre, Fraccion[][] matriz) {

        System.out.println();
        System.out.println(nombre);

        for (int i = 0; i < 3; i++) {
            System.out.print("[ ");
            for (int j = 0; j < 3; j++) {
                System.out.print(matriz[i][j] + " ");
            }
            System.out.println("]");
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("======================================");
        System.out.print("   FACTORIZACIÓN LU DE UNA MATRIZ 3x3");
        System.out.print("           MÉTODO DE DOOLITTLE");
        System.out.print("======================================");
        System.out.println();
        System.out.print("Ejemplos de datos válidos:");
        System.out.print("Entero:     5");
        System.out.print("Decimal:    2.5");
        System.out.print("Fracción:   3/4");
        System.out.print("Fracción:  -5/2");
        System.out.println();


        System.out.print("--------------------------------------");

        Fraccion[][] A = new Fraccion[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                A[i][j] = ingresarNumero(sc, "Ingrese a" + (i + 1) + (j + 1) + ": ");
            }
        }


        Fraccion[][] L = new Fraccion[3][3];
        Fraccion[][] U = new Fraccion[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                L[i][j] = new Fraccion(i == j ? 1 : 0, 1);
                U[i][j] = new Fraccion(0, 1);
            }
        }

        U[0][0] = A[0][0];
        U[0][1] = A[0][1];
        U[0][2] = A[0][2];

        if (U[0][0].esCero()) {
            System.out.println();
            System.out.print("No se puede realizar la factorización LU.");
            System.out.print("El primer pivote es cero.");
            return;
        }

        L[1][0] = A[1][0].dividir(U[0][0]);
        L[2][0] = A[2][0].dividir(U[0][0]);


        U[1][1] = A[1][1].restar(L[1][0].multiplicar(U[0][1]));
        U[1][2] = A[1][2].restar(L[1][0].multiplicar(U[0][2]));

        if (U[1][1].esCero()) {
            System.out.println();
            System.out.print("No se puede realizar la factorización LU.");
            System.out.print("El segundo pivote es cero.");
            return;
        }

        L[2][1] = A[2][1].restar(L[2][0].multiplicar(U[0][1])).dividir(U[1][1]);

        U[2][2] = A[2][2]
                .restar(L[2][0].multiplicar(U[0][2]))
                .restar(L[2][1].multiplicar(U[1][2]));

        mostrarMatriz("MATRIZ A:", A);
        mostrarMatriz("MATRIZ L:", L);
        mostrarMatriz("MATRIZ U:", U);

        System.out.println();
        System.out.print("======================================");
        System.out.print("FACTORIZACIÓN:");
        System.out.print("A = L × U");
        System.out.print("======================================");
    }
}