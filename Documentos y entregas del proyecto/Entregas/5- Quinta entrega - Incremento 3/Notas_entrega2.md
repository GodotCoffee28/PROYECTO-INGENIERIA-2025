* Actualizar registro - Correo, contraseña y CI, lo demás según está almacenado en la BD del usuario. (menos campos para validar - menos rango para errores).
* Bajar el rango de validación de cédulas (8 millones es muy alto, quizás 4 millones).
* La BD ya tiene asignado a alguien como administrador/estudiante, no le parece necesario seleccionar tipo de usuario cuando ya está limitado a un usuario específico. Ejemplo que planteó: Si el admin renuncia y quiere entrar como comensal q pasa¿ Le parece innecesario q esté en la BD y tmb se pueda seleccionar (ya está limitado). Recomendó dejarlo en la BD y q no elija dado que ya tiene un tipo de usuario asignado
* Contraseña de un solo carácter, no está validando bien, mejorar la seguridad de la contraseña.
* El No definido se ve al comensal? está extraño. Mejor manejar el no definido, q el no definido sea para el administrador y no visible para el comensal (o se quita la opción de no definido o se mejora)
* No le gusta lo de los platillos (nombre de los campos) Entiende q cada platillo es una comida completa. Recomienda algo de plato fuerte, blabla. No le gusta eso, pe, que lo lleve a algo más pequeño (ajusta títulos).
* La suma de los costos de los insumos se ve en pantalla, pero no interfiere directamente con los costos variables, ¿para qué se usa? 
* En lo de reiniciar menú limpia raro la BD, no quita todo, cm que deja campos ahí
* Hay un bug debido a q avanza en días automáticamente. Si ya se creó un menú de desayuno el lunes pasa automáticamente al martes y no deja crear ahí mismo el del almuerzo del lunes. No la obliga a pasar al siguiente menú del mismo día (o lo muestra completo o le da la libertad de elegir)
* Le gustaría q al crear el menú se vea si es lunes-martes, blabla
* Le gustaría eliminar un menú específico, dado que ahora solo tenemos reiniciar para toda la semana (eliminar menú específico o poner no disponible, idk)
* Eliminar .txt que no se usen, hay algunos q son copias.
* El admin es un empleado - se le aplica ese CCB
* El dinero está en dólares - Acá manejamos bs, pendiente
* No se validan las referencias, quizás habría que tener una bd aparte que tenga referencias válidas (simulando el banco) para que no coloquen referencias inválidas
* Permite ingresar a la fila si no hay menú, fallo, ¿qué vas a comprar?
* Está aplicando random en el porcentaje q se aplica a cada tipo de usuario, quitar eso y permitir q el administrador lo ajuste - usar validaciones sobre el rango, pendiente, tmb para q solo ingresen números
* Arreglar el botón para regresar en una de las vistas (no recuerdo cuál)
* Q el botón de regreso de InicioAdmin e inicioComensal pregunte antes de cerrar sesión
* Revisar y dividir mejor lo del caja negra, verificar el orden según cómo lo plantea Marcel y Thibisay
* Haces 3 pruebas en un mismo método, incorrecto - un método por clase (cuestión de Marcel) Mejorar nombres de los métodos de pruebas para q reflejen a qué se refieren.
