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

