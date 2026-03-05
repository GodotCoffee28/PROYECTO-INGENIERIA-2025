# PROYECTO-INGENIERIA-2025
# GESCO - Gestión Sistema Comedor

Proyecto basado en el desarrollo de un software de gestión del comedor universitario de la UCV.

## Integrantes
* **Ramses Cordoba** : aaroncordobarivas@gmail.com | V-32.111.454
* **Gabriel Castillo**: gc.gabriel.castillo@gmail.com | V-33.246.679
* **Genesis Noriega** : genesis.ngo0@gmail.com | V-32.100.074
* **Alejandro Rondón**: alejanxuor12.rondo@gmail.com | V-31.768.764

---

## Planteamiento y Objetivos

El planteamiento es el desarrollo de una aplicación que simule el control y gestión del sistema de comedor de la Universidad Central de Venezuela (UCV). 

Esta aplicación de software va a sistematizar y automatizar la gestión del comedor universitario, el cual actualmente está siendo administrado de forma manual, generando deficiencias en los diferentes procesos para la asignación de turnos, control de consumos e insumos. Al implementar este sistema se busca facilitar el desarrollo y gestión de estas actividades a través del software desarrollado.

**Objetivos principales de GESCO:**
  * **Sistematizar y automatizar:** Se busca automatizar y simplificar todos los procesos administrativos y organizacionales del comedor.
  * **Controlar las finanzas:** Calcular de forma automática los costes correspondientes a cada tipo de comensal y los costos relacionados al mantenimiento y reabastecimiento del comedor
  * **Facilitar la planificación:** Facilitar la gestion de los menus disponibles, horarios de trabajo, información de gastos y reposición de suministros.


## Dominio y Funcionalidad del Sistema (Roles)

Para un sistema como este, es necesario garantizar la seguridad y el orden, por lo tanto el sistema distingue a los usuarios mediante un modelo de roles estricto. Cada rol tiene diferentes niveles de acceso, permisos, funcionalidades y beneficios:

* **Comensales (Estudiantes, Profesores, Empleados):**
  * **Funciones:** Pueden registrarse (si están en el sistema) para adquirir su propia clave, iniciar sesión, recargar saldo, revisar el menú del día o la semana, entrar y salir de la fila virtual y consultar sus movimientos transaccionales.
  * **Reglas de Negocio:** El costo de su comida (CCB - Costo de la Cesta Básica) varía dependiendo de su rol. Los estudiantes gozan de un mayor porcentaje de subsidio (pagan entre el 20% y 30% del costo), mientras que profesores (pagan entre el 70% y 90% del costo) y empleados (pagan entre el 90% y 110% del costo) pagan porcentajes mayores cercanos al costo real .

* **Administradores (Admin):**
  * **Funciones:** Tienen acceso a la gestión operativa del comedor.
    * Gestión de Menú: Crear, editar y establecer la disponibilidad de platillos e insumos.
    * Costos: Cargar y calcular los Costos Fijos y Variables (CFCV) y el Costo de la Cesta Básica (CCB).
    * Control: Manejan los datos de consumo y registros del comedor e inventario general.

* **Super Administrador (Super Admin):**
  * **Funciones:** Poseen todos los permisos de un Administrador, pero con la capacidad exclusiva de **autorizar a nuevos administradores** mediante la generación de códigos de registro, asegurando el control y seguridad del sistema.

  * **Reglas de Negocio:** El costo parta los Administradores (tanto Administradores comunes como el Super Administrador), no varía caen en la regla por defecto y pagan un 100% fijo.

---

## Manual de Usuario

### Registro en el Sistema
* **Para Comensales (Estudiantes, Empleados, Profesores):**
  1. Dirígete a la pantalla de Registro.
  2. Ingresa tu Nombre, Cédula (solo números, entre 8 millones y 45 millones), Correo Electrónico, Contraseña y selecciona tu Tipo de Usuario.
	**Nota: próximamente se cambiara a 2 millones como mínimo.**
  	**Nota Importante:** El sistema verifica tu cédula contra las de "Secretaría". Si tu cédula no está, el registro será rechazado (Próximamente se adaptara para simplificar el registro).
   
* **Para Administradores:**
  1. Llena los datos básicos en la pantalla de Registro y selecciona "Administrador".
  2. Se te pedirá un **Código de Autorización**. Este código debe ser proporcionado previamente por un Super Administrador para poder crear la cuenta.

### Inicio de Sesión
  1. En la pantalla principal, ingresa tu número de Cédula (sin puntos) y tu Contraseña.
  2. Haz clic en "Iniciar Sesión". El sistema te redirigirá automáticamente a tu panel correspondiente (Comensal o Administración).

### Funcionalidades del Panel de Comensal
Una vez que un **Estudiante, Profesor o Empleado** inicia sesión en el sistema, es redirigido a su panel principal (Vista de Comensal), donde tiene acceso a las siguientes herramientas interactivas:

  1. **Consultar el Menú del Día y la Semana:**
   * El usuario puede visualizar de forma gráfica los platillos disponibles para el día en curso (Desayuno y Almuerzo).
   * Puede navegar por un calendario para revisar qué comida está planificada para los próximos días hábiles de la semana.
  2. **Monedero y Recarga de Saldo:**
   * El sistema cuenta con una billetera virtual. El comensal puede verificar su saldo actual en pantalla.
   * Cuenta con la opción de **"Recargar Saldo"**, donde deberá ingresar un número de referencia de transferencia bancaria, fecha (en un rango máximo de 2 días), el banco de origen y el monto para que los fondos se sumen a su cuenta automáticamente.
  3. **Fila Virtual:**
   * El usuario puede solicitar un turno para el comedor si dispone del saldo suficiente y su comprobación facial es aceptada.
   * El sistema le asignará un número en la fila y mostrara la cantidad de personas que están en la fila actualizada.
   * El usuario tiene la posibilidad de salir de la fila, si es que aun no ha entrado al comedor, rembolsándosele todo el saldo invertido.
  4. **Historial de Consumos y Transacciones:**
   * El comensal puede acceder a un registro donde podrá visualizar sus movimientos de recargas de saldo aprobadas.

### Funcionalidades del Panel de Administración
Los **Administradores** (y Super Administradores) tienen acceso a un panel de control avanzado. Además de que **pueden realizar absolutamente todas las acciones de un comensal regular**, también cuentan con herramientas exclusivas para operar el comedor:

  1. **Gestión y Planificación del Menú:**
   * **Crear Menú:** Pueden armar los platillos diarios (Desayunos y Almuerzos) combinando diferentes ingredientes del almacén.
   * **Editar Menú:** Permite modificar un platillo ya creado si surge algún cambio de último minuto.
   * **Disponibilidad:** Tienen un casilla para marcar un día como "No Disponible" (si el comedor no abrirá por mantenimiento o alguna otra razon).
  2. **Control de Inventario (Insumos):**
   * Pueden ingresar nuevos insumos al sistema especificando el nombre, el tipo nutricional, la cantidad entrante y el costo unitario de compra.
  3. **Gestión Financiera (Cálculo del CCB):**
   * **Cargar CFCV:** Los administradores pueden ingresar/modificar los Costos Fijos y Costos Variables, la merma, el tipo de usuario y el numero de bandejas servidas para calcular el **Costo de la Cesta Básica (CCB)** real de cada plato. Este es el valor base sobre el cual el sistema aplicará los subsidios a los comensales.
  4. **Generar Códigos de Seguridad (Solo Super Administrador):**
   * El Super Administrador tiene un botón adicional de "Autorizar Admin". Aquí genera los códigos correspondientes a cada uno de los administradores del sistema para que puedan registrarse en la plataforma.


---

## Arquitectura del Sistema (MVC)

Se sigue el patrón de diseño MVC (Modelo - Vista - Controlador) con el fin de tener una buena separación del sistema y una buena distribución de actividades.

### Modelos (`com.gesco.models`)
Es el encargado de la gestión de los datos y la lógica del negocio. Reflejado en las distintas clases (Ej: `Menu.java`, `Usuario.java`, `Insumo.java`) que modelan los objetos del sistema.
* **Archivo Clave:** `Usuario.java`. Define la clase base y contiene el enumerador `TipoUsuario` que clasifica a las personas en `COMENSAL`, `ESTUDIANTE`, `PROFESOR`, `EMPLEADO`, `ADMIN`, y `SUPER_ADMIN`.

### Vistas (`com.gesco.views`)
Es la capa gráfica para los usuarios del sistema. El manejo de las distintas interfaces de usuario es intuitivo, distinguiendo vistas específicas dependiendo de si el usuario es un Administrador o un comensal regular (ej. `VistaInicioAdmin` vs `VistaInicioComensal`).
* **Nomenclatura:** Las clases de interfaz gráfica conservan el prefijo `Vista` (ej. `VistaInicioSesion`, `VistaRegistro`) y se ubican en subpaquetes según su funcionalidad (`autentificacion`, `costos`, `inicio`, `menu`, etc.).

### Controladores (`com.gesco.controllers`)
Es el intermediario entre la vista y el modelo. Muestra los datos, los actualiza mediante acciones del usuario y los almacena.
* **Nomenclatura:** Se nombran según el caso de uso usando el prefijo `Controlador` (ej. `ControladorInicioSesion`, `ControladorRegistro`) y se agrupan en subpaquetes funcionales (`autentificacion`, `costos`, `gestion_principal`, etc.).
* **Archivos Clave (Autenticación):** `ControladorInicioSesion.java` y `ControladorRegistro.java`. Conectan la interfaz de usuario con la base de datos, manejando validaciones (ej. el formato de cédulas estrictamente numéricas y sus rangos) y emitiendo alertas en pantalla mediante `JOptionPane`.

---

## Lógica de Persistencia

Este sistema persiste la información de manera local en archivos de texto (`.txt`), controlados de forma centralizada.

### Base de Datos (`DataBase.java`)
Ubicado en `com.gesco.controllers.gestion_principal`, es el núcleo de persistencia del proyecto.
* **Funciones Principales:** * `validarInicioSesion()` y `registrarUsuario()`: Validan credenciales y permisos.
  * `calcularMontoCcbPorTipo()`: Aplica las matemáticas y porcentajes aleatorios/fijos de subsidios según el rol del usuario.
  * `asegurarImagenSecretariaParaCedula()`: Si un usuario no posee foto en el padrón, dibuja programáticamente (usando `Graphics2D`) un avatar por defecto basado en un "hash" numérico de su cédula.

### Estructura de Datos Físicos
Todos los archivos de almacenamiento se encuentran en: `src/main/java/com/gesco/models/data/`
* `usuarios.txt`: Credenciales y saldos de los usuarios registrados.
* `menus.txt` / `ccb.txt` / `cfcv.txt`: Historial de platillos, costos de la cesta básica y costos fijos/variables.
* `secretaria/cedulas_ocupaciones.txt`: Actúa como la lista oficial de la universidad que dicta quién y con qué rol puede registrarse.
* `secretaria/imagenes_rostros/`: Contiene las fotografías de los usuarios para el sistema biométrico (facial).

### Validación Biométrica (Lector Facial)
* **Archivo Clave:** `ValidadorIdentidad.java`.
* **Funcionamiento:** Simula un lector facial. La función `compararImagenes(File img1, File img2)` lee dos archivos de imagen, verifica sus dimensiones y utiliza un bucle anidado para comparar el color RGB exacto (`getRGB(x,y)`) de cada píxel de la imagen capturada contra la guardada en la carpeta de `secretaria`.

---

## Dependencias Agregadas y Requerimientos

* **Librerías Externas:** Se usó `jcalendar-1.4.jar` para el manejo de calendarios y fechas en las interfaces, junto con `jgoodies-common` y `jgoodies-looks` para los componentes visuales. Adicionalmente, se integra `JUnit` para las pruebas unitarias.
* **Configuración Local:** Las librerías están declaradas en el archivo `pom.xml` con una ruta local apuntando directamente a la carpeta `/Lib/` del repositorio.
* **Requerimientos del Entorno:** * Se requiere tener instalado y configurado **Maven** para la compilación del proyecto, resolución de dependencias y la ejecución de pruebas.
  * Se requiere **Java 17 (JDK 17).**

---

## Pruebas Unitarias (Caja negra)

El funcionamiento de las pruebas unitarias se logró al estandarizar la ejecución con Maven y organizar el proyecto de acuerdo con su estructura esperada:

1. **Configuración base del entorno:**
   * Java 17 para asegurar compatibilidad de compilación.
   * JUnit 4.11 para crear y ejecutar pruebas.
   * Maven Surefire (2.22.1) como plugin para correr automáticamente las pruebas sin errores.
2. **Estructura de pruebas:**
   * Ubicadas en `CODIGO/gesco-proyecto/src/test/java`.
   * Tienen la terminacion de nombres `Test.java` para ser detectadas por Surefire automáticamente.
3. **Ejecución y validación:**
   Desde la ruta `CODIGO/gesco-proyecto`, se ejecutan las pruebas mediante el comando:
   ```bash
   mvn clean test