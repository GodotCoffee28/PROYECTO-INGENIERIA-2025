# PROYECTO-INGENIERIA-2025
# GESCO - Gestión Sistema Comedor
Proyecto basado en el desarrollo de un software de gestion del comedor universitario de la ucv.
# Integrantes
Ramses Cordoba  : aaroncordobarivas@gmail.com   32111454

Gabriel Castillo: gc.gabriel.castillo@gmail.com 33.246.679

Genesis Noriega : genesis.ngo0@gmail.com        32100074

Alejandro Rondón: alejanxuor12.rondo@gmail.com  31.768.764


# Planteamiento

El planteamiento es el desarrollo de una aplicación la cual simule una apliación de control y gestión de un sistema comedor de la universidad central de Venezuela. Esta apliacion software va a sistematizar y automatizar la gestión del comedor universitario que esta siendo administrado de forma manual generando deficiencias en los diferentes procesos para la asignacion de turnos, control de consumos e insumos. Al implementar este sistema se busca facilitar el desarrollo y gestión de estas actividades atraves del sofware que desarrollaremos.

# Dominio
Para un sistema así, hay cierto grupo selecto de personas las cuales van a hacer uso de este software. 

Como:

**-Estudiantes de la UCV**

**-Empleados del comedor de la UCV**

**-Personal de la UCV**

# Sistema

Se sigue el patrón de diseño MVC (Modelo - Vista - Controlador) con el fin de encontrar una separación del sistema y una forma de distribución de actividades necesaria.

## Modelo

El modelo es el encargado de la gestión de los datos y la lógica del negocio. Esto es reflejado con las distintas clases (Ej: Menú, usuario, comensal, etc) que se desarrollan para una modelación adecuada de los objetos pertenecientes del sistema.

## Vista

La vista es la capa de presentación para los distintos usuarios del sistema comedor. El manejo de las distintas interfaces de usuario (distinguido si es Administrador u comensal regular) tiene que ser intuitivo para el mismo para el manejo adecuado de sus distintas actividades dentro del sistema comedor. La presentación de los datos almacenados en el modelo mediante un controlador es vital para el correcto funcionamiento de este sistema.

## Controlador

El controlador es el intermediario entre la vista y el modelo para la muestra de datos, la actualizacion de datos mediante acciones del usuario en la interfaz y almacenamiento de los datos por el modelo.

# Dashboard

Para la implementación de estos apartados del sistema debemos organizarnos como equipo para superar las actividades planteadas, para ello se esta usando la herramienta Trello para la organización de las actividades en el proyecto en un Dashboard.

https://trello.com/invite/b/697c0c6081719cbbc259a0af/ATTIc03601e23a4e4d77c2267fcc02e517057FE02FBA/dashboard-kanban-equipo-13


# Dependencias agregadas
Se usó jcalendar-1.4.jar como librería externa para el uso de calendarios y manejo de fechas en java ya que java no posee el manejo de las mismas y esto resulta de utilidad.

# Requerimientos
Se requiere tener instalado y configurado MAVEN para la compilación de pruebas unitarias.

# ¿Cómo se logró el funcionamiento de las pruebas unitarias?

El funcionamiento de las pruebas unitarias se logró al estandarizar la ejecución con Maven y organizar el proyecto de acuerdo con su estructura esperada.

## 1) Configuración base del entorno
- Se definió Java 17 para asegurar compatibilidad de compilación.
- Se integró JUnit 4.11 para crear y ejecutar pruebas unitarias.
- Se configuró Maven Surefire (2.22.1), que es el plugin encargado de descubrir y correr automáticamente las pruebas.

## 2) Estructura de pruebas
- Las pruebas se ubicaron en `CODIGO/gesco-proyecto/src/test/java`.
- Se siguió la convención de nombres `*Test.java` para que Surefire las detecte sin configuración adicional.
- Se crearon pruebas para distintos módulos.
## 3) Dependencias locales del proyecto
- Las librerías externas usadas por la aplicación (`jcalendar`, `jgoodies-common`, `jgoodies-looks`) se declararon en el `pom.xml` con ruta local dentro de `Lib/`.
- Esto permitió compilar correctamente el código principal durante la fase de test.

## 4) Ejecución y validación
- Desde `CODIGO/gesco-proyecto`, se ejecutan las pruebas con:

```bash
mvn clean test
```

- Los resultados se generan en `target/surefire-reports/`, donde se puede validar qué pruebas pasaron o fallaron.

## Observaciones del Incremento 1 y Ajustes Realizados

De acuerdo con las observaciones realizadas por el profesor en la revisión del Incremento 1, se implementaron las siguientes mejoras y correcciones:

- **Estructura de paquetes y nomenclatura de clases**  
  Se revisó y ajustó la convención de nombres para diferenciar claramente las capas del patrón MVC:  
  - Las clases de **interfaz gráfica** (vistas) conservan el prefijo `Vista` (ej. `VistaInicioSesion`, `VistaRegistro`, `VistaCargaCCB`, etc.) y se ubican en subpaquetes bajo `views/` según su funcionalidad (autentificacion, costos, inicio, menu, otros, Registros, PlantillasViews).  
  - Las clases de **control** (controladores) se nombran según el **caso de uso principal** sin incluir "Vista" (ej. `ControladorInicioSesion`, `ControladorRegistro`, `ControladorCargarCCB`, etc.), ubicadas en `controllers/`.  
  Esto mejora la legibilidad, evita redundancia en los nombres y respeta mejor la separación de responsabilidades.

- **Contenido del README**  
  Se enriqueció significativamente la documentación con las secciones recomendadas:  
  - Misión y Visión del proyecto  
  - Tecnologías utilizadas (detalladas)  
  - Instrucciones claras de compilación, ejecución y pruebas  
  - Explicación de dependencias locales y configuración de Maven  
  - Resumen de las observaciones recibidas y cómo fueron atendidas

- **Otras mejoras realizadas**  
  - Mayor consistencia en la validación de datos (especialmente cédulas, montos y fechas)  
  - Refuerzo de mensajes de error más descriptivos en la interfaz  
  - Mantenimiento de pruebas unitarias básicas en módulos clave (costos, autenticación, menús)  
  - Documentación interna mejorada en clases críticas como `DataBase.java`

Estas correcciones y ampliaciones buscan alinear el proyecto con las buenas prácticas de desarrollo de software indicadas y mejorar tanto la mantenibilidad como la comprensión del sistema por parte de evaluadores y futuros colaboradores.

¡Gracias por el feedback recibido! Seguimos trabajando para entregar una solución más robusta y bien documentada en los próximos incrementos.