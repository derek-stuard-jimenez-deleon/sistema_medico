# Sistema Médico 2026 — Casos de Uso

Documento consolidado a partir de los archivos .docx originales del proyecto.

## Índice

- [CU-0: Visualización de Portal Web](#cu-0-visualizacion-de-portal-web)
- [CU-1: Mantenimiento de Usuarios](#cu-1-mantenimiento-de-usuarios)
- [CU-2: Registro de Usuarios Externos](#cu-2-registro-de-usuarios-externos)
- [CU-3: Agendar Citas](#cu-3-agendar-citas)
- [CU-4: Pago en Línea](#cu-4-pago-en-linea)
- [CU-5: Recepción y Verificación de Cita](#cu-5-recepcion-y-verificacion-de-cita)
- [CU-6: Cobro de Consulta en Caja](#cu-6-cobro-de-consulta-en-caja)
- [CU-7: Toma de Signos Vitales](#cu-7-toma-de-signos-vitales)
- [CU-8: Consulta Médica](#cu-8-consulta-medica)
- [CU-9: Gestión de Laboratorio](#cu-9-gestion-de-laboratorio)
- [CU-10: Cobro de Laboratorio en Caja](#cu-10-cobro-de-laboratorio-en-caja)
- [CU-11: Despacho de Medicamentos](#cu-11-despacho-de-medicamentos)
- [CU-12: Agendamiento de Cita de Seguimiento](#cu-12-agendamiento-de-cita-de-seguimiento)
- [CU-13: Configuración de Sedes y Especialidades](#cu-13-configuracion-de-sedes-y-especialidades)
- [CU-14: Mantenimiento de Catálogos del Sistema](#cu-14-mantenimiento-de-catalogos-del-sistema)
- [CU-15: Bitácora de Movimientos de Inventario](#cu-15-bitacora-de-movimientos-de-inventario)
- [CU-16: Gestión de Agenda Médica](#cu-16-gestion-de-agenda-medica)
- [Reglas de Negocio Consolidadas](#reglas-de-negocio-consolidadas)

---

## CU-0: Visualización de Portal Web

**Historial Revisiones**

| Nombre           | Fecha      | Descripción del cambio | Versión |
|------------------|------------|------------------------|---------|
| Ing. Edy Ramírez | 12/03/2026 | Planteamiento inicial  | 1.0.0   |
| Ing. Edy Ramírez | 12/03/2026 |                        | 1.0.0   |

# 

# 1. Introducción

**Descripción**

El presente documento describe los pasos que el Usuario Externo deberá
seguir para visualizar los servicios ofrecidos dentro del portal web del
Sistema Informático Hospitalario (HIS) y acceder al proceso de
agendamiento de citas médicas.

**Objetivo**

Brindar un portal web informativo que permita a los usuarios externos
visualizar los servicios ofrecidos por el Hospital, consultar
especialidades disponibles y acceder al proceso de agendamiento de
citas.

# 2. Definición Caso de Uso

## 2.1 Actores

- Usuario Externo (Paciente)

- Sistema informático

## 2.2 Precondiciones

- El sistema debe estar disponible.

- El Usuario Externo debe contar con un dispositivo con acceso a
  internet.

- Catalogos Precargados en cache

## 2.3 Flujo Normal Básico

1.  El Usuario Externo ingresa al portal web a través del enlace del
    sitio.

2.  El sistema muestra la página principal con la información del portal
    web (servicios, especialidades, ubicaciones, horarios de atención).

3.  El Usuario Externo selecciona la opción "Agendar Cita".

4.  El sistema muestra un diálogo modal con título "Verificar Registro"
    y el campo de ingreso de DPI con el mensaje:
    "<span class="mark">Ingrese su número de DPI para verificar si está
    registrado en el sistema.</span>" El campo muestra un contador de
    dígitos en tiempo real (X/13 dígitos).

5.  El Usuario Externo ingresa su número de DPI.

6.  El sistema valida el formato del DPI ingresado. \[RN-GLOBAL-001\]
    \[FA01\]

7.  El Usuario Externo selecciona el botón "Verificar DPI". El sistema
    muestra un indicador de carga con el texto "Verificando...".
    \[FA02\]??? CU 2

8.  El sistema confirma que el Usuario Externo está registrado con rol
    de paciente y lo redirige a la pantalla de inicio de sesión del
    portal, donde deberá autenticarse con su nombre de usuario y
    contraseña para acceder al agendamiento de citas. \[RN-CU00-01\]
    \[FA03\] \[FA04\]

9.  El sistema redirige al Usuario Externo a la pantalla de inicio de
    sesión del portal (“Iniciar Sesión”). El Usuario Externo ingresa su
    nombre de usuario y contraseña y selecciona el botón “Iniciar
    Sesión”. El sistema valida las credenciales. \[FA06\] \[FA07\]
    \[FA08\] \[FA09\]

10. Una vez autenticado exitosamente, el sistema redirige al Usuario
    Externo al dashboard del portal donde puede acceder al formulario de
    agendamiento de cita. \[CU-03\] \[CU-04\]

11. (Paso eliminado: pertenece a CU-04 Pago en Línea.)

12. Fin del caso de uso.

## 2.4 Flujos Alternos

**FA01 Error en la validación del DPI**

1.  El Usuario Externo ingresa su DPI con formato inválido.

2.  El sistema muestra el mensaje de error específico según la
    validación que falló (ver RN-GLOBAL-001).

3.  El Usuario Externo corrige el dato.

4.  Se continúa en el paso 6 del flujo normal básico.

**FA02 El Usuario Externo cancela la operación**

5.  El sistema regresa al paso 2 del flujo normal básico.

**FA03 El Usuario Externo no está registrado en el sistema**

1.  El sistema no encuentra un registro asociado al DPI ingresado.

2.  El sistema muestra el mensaje: "No se encontró un registro asociado
    a este DPI. Será redirigido al formulario de registro."

3.  El sistema redirige al Usuario Externo al formulario de registro.
    \[CU-02\]

4.  Una vez completado el registro, se continúa en el paso 8 del flujo
    normal básico.

**FA04 El DPI pertenece a un usuario interno del sistema**

6.  El sistema detecta que el DPI ingresado pertenece a un usuario
    registrado en el sistema interno (no paciente).

7.  El sistema muestra el mensaje: "Este DPI pertenece a un usuario del
    sistema interno. Por favor, contacte a recepción."

8.  El Usuario Externo permanece en el diálogo de verificación.

**FA05 Error de conexión con el servidor**

9.  El sistema no puede conectarse con el servidor para verificar el
    DPI.

10. El sistema muestra el mensaje: "No se pudo conectar con el servidor.
    Intente de nuevo más tarde."

11. El Usuario Externo puede reintentar la operación.

<!-- -->

1.  **FA06 Credenciales incorrectas**

<!-- -->

1.  El Usuario Externo ingresa un nombre de usuario o contraseña
    incorrectos.

2.  El sistema muestra el mensaje: “Usuario o contraseña incorrectos.
    Intentos restantes: \[N\].” donde N es el número de intentos
    restantes (máximo 5 intentos).
    <span class="mark">\[RN-CU00-03\]</span>

3.  El Usuario Externo corrige sus credenciales y reintenta el inicio de
    sesión. Se continúa en el paso 9 del flujo normal básico.

<!-- -->

2.  **FA07 Cuenta bloqueada por intentos fallidos**

<!-- -->

12. El Usuario Externo alcanza el máximo de 5 intentos fallidos de
    inicio de sesión.

13. El sistema muestra el mensaje: “Cuenta bloqueada temporalmente.
    Intente de nuevo en 15 minutos.” \[RN-CU00-03\]

14. Los campos de usuario y contraseña se deshabilitan junto con el
    botón de inicio de sesión durante el período de bloqueo.

15. El Usuario Externo debe esperar 15 minutos antes de reintentar.

<!-- -->

3.  **FA08 Error de conexión durante el inicio de sesión**

<!-- -->

16. El sistema no puede conectarse con el servidor para validar las
    credenciales.

17. El sistema muestra el mensaje: “No se pudo conectar con el servidor.
    Intente de nuevo más tarde.”

18. El Usuario Externo puede reintentar el inicio de sesión.

<!-- -->

4.  **FA09 Inicio de sesión con rol no autorizado**

<!-- -->

19. El Usuario se autentica exitosamente pero su rol no es “Paciente”
    (por ejemplo, es personal del hospital).

20. El sistema muestra el mensaje: “Este acceso es exclusivo para
    pacientes. Si es personal del hospital, use el panel
    administrativo.”

21. La pantalla de login muestra un enlace “Acceso Panel Administrativo”
    para que el usuario pueda dirigirse al panel interno.

## 2.5 Postcondiciones

- El Usuario Externo visualizó la información del portal web.

- Si el Usuario Externo agendó cita, esta queda registrada en el sistema
  con estado "Pendiente de pago".

- Si el Usuario Externo se registró durante el proceso, su cuenta queda
  activa en el sistema.

**5. Firma y Sello**

| **Nombre** | **Puesto** | **Firma y Sello** |
|------------|------------|-------------------|
|            |            |                   |

---

## CU-1: Mantenimiento de Usuarios

**CU-01 Mantenimiento de Usuarios**

# Historial de Revisiones

| Nombre           | Fecha      | Descripción del cambio | Versión |
|------------------|------------|------------------------|---------|
| Ing. Edy Ramírez | 12/03/2026 | Planteamiento inicial  | 1.0.0   |
| Ing. Edy Ramírez | 12/03/2026 |                        | 1.0.0   |

# 

# 1. Introducción

**Descripción**

El presente documento describe los pasos que el personal con permisos de
administración deberá seguir para la gestión y mantenimiento de cuentas
de usuarios internos del sistema informático hospitalario, incluyendo
búsqueda, creación, actualización y eliminación de cuentas. El módulo
“User” del menú lateral ofrece dos puntos de entrada independientes:
“Listar Usuarios” y “Crear Usuario”.

**Objetivo**

Administrar las cuentas de usuarios internos del sistema informático
para un control eficiente de accesos, roles y estados correspondientes.

# 2. Definición Caso de Uso

## 2.1 Actores

• Usuario Interno (Administrador)

• Sistema informático

## 2.2 Precondiciones

- El sistema debe estar disponible. \[RN-GLOBAL-003\]

- El Usuario Interno debe haber iniciado sesión con permisos de
  administración. \[RN-GLOBAL-007\]

<!-- -->

- Catalogos deben de estar previamente cargado en cache.

## 2.3 Flujo Normal Básico

1.  El Usuario Interno accede al módulo “Usuarios” en el menú lateral
    del sistema.

2.  El sistema despliega las opciones del módulo: “Listar Usuarios” y
    “Crear Usuario”. \[FA01\]

3.  El Usuario Interno selecciona “Listar Usuarios”.

4.  El sistema muestra la pantalla “Listado de Usuarios” con un selector
    desplegable “Filtrar por campo” (opciones: ID, Nombre, Correo
    Electrónico, Rol, Nombre de Usuario, DPI), un campo de texto con
    placeholder “Buscar…” y un botón de búsqueda (🔍). Debajo se muestra
    la tabla de usuarios con paginación.

5.  El Usuario Interno selecciona el campo de filtro deseado e ingresa
    el criterio de búsqueda. \[RN-CU01-01\]

6.  El Usuario Interno presiona el botón de búsqueda (🔍). \[FA02\]

7.  El sistema ejecuta la búsqueda y muestra los resultados en formato
    de tabla paginada con las columnas:

> ID,
>
> Nombre,
>
> Correo Electrónico,
>
> Rol, Nombre de Usuario,
>
> Estado y Acciones.
>
> La paginación muestra “Elementos Por página” (configurable: 10, 25,
> 50) y el conteo de registros. \[RN-CU01-02\] \[FA03\]

8.  El Usuario Interno visualiza la información y puede ejecutar las
    siguientes acciones desde el menú de acciones (⋮) de cada registro:
    Editar \[FA04\], Eliminar \[FA05\].

9.  Fin del caso de uso.

## 2.4 Flujos Alternos

**FA01 – Crear nuevo usuario (entrada directa desde menú)**

1.  El Usuario Interno selecciona “Crear Usuario” desde el menú lateral
    del módulo “User”.

2.  El sistema muestra la pantalla “Crear Usuario” con el formulario de
    creación que contiene los campos:

> Nombre Completo \[RN-CU01-03\],
>
> Correo Electrónico \[RN-CU01-04\],
>
> Nombre de Usuario \[RN-CU01-05\],
>
> Contraseña \[RN-CU01-06\],
>
> Documento de Identificación (opcional) \[RN-CU01-07\],
>
> Número de Teléfono (opcional) \[RN-CU01-08\],
>
> Rol \[RN-CU01-09\],
>
> NIT (opcional) \[RN-CU01-11\],
>
> Número de Seguro (opcional) \[RN-CU01-12\],
>
> Sucursal (opcional) \[RN-CU01-13\],
>
> Especialidad – solo para médicos – (opcional) \[RN-CU01-14\] y
>
> Estado \[RN-CU01-10\].

3.  El Usuario Interno ingresa los datos del nuevo usuario en los campos
    correspondientes.

4.  El Usuario Interno selecciona el botón “Crear”. \[FA06\] \[FA07\]

5.  El sistema valida todos los campos del formulario.

6.  El sistema crea el usuario y muestra el mensaje: “Usuario creado
    correctamente.”

7.  Fin del caso de uso.

**FA02 – Limpiar búsqueda**

1.  El Usuario Interno no ingresa criterio de búsqueda o desea reiniciar
    los filtros.

2.  El sistema muestra la tabla con todos los usuarios registrados sin
    filtro aplicado.

3.  Se continúa en el paso 7 del flujo normal básico.

**FA03 – No se encontró información**

1.  El sistema no encontró información que cumpliera con los filtros de
    búsqueda.

2.  El sistema muestra la tabla vacía con el mensaje en color rojo: “No
    se encontraron datos usuarios.”

3.  El Usuario Interno modifica los filtros de búsqueda.

4.  Se continúa en el paso 5 del flujo normal básico.

**FA04 – Editar usuario**

1.  El Usuario Interno selecciona “Editar” en el menú de acciones (⋮)
    del usuario que desea modificar.

2.  El sistema navega a la pantalla “Editar Usuario” y muestra el
    formulario con los valores actuales precargados en los campos:
    Nombre Completo, Correo Electrónico, Nombre de Usuario, Nueva
    Contraseña (opcional), Documento de Identificación, Número de
    Teléfono, Rol, NIT, Número de Seguro, Sucursal (opcional),
    Especialidad (opcional) y Estado.

3.  El Usuario Interno modifica los campos necesarios.

4.  El Usuario Interno selecciona el botón “Actualizar”. \[FA06\]
    \[FA07\]

5.  El sistema valida los campos modificados.

6.  El sistema muestra el mensaje: “Usuario actualizado correctamente.”

7.  Fin del caso de uso.

**FA05 – Eliminar usuario**

1.  El Usuario Interno selecciona “Eliminar” (texto en color naranja) en
    el menú de acciones (⋮) del usuario que desea eliminar.

2.  El sistema muestra el diálogo modal “Confirmar eliminación” con un
    ícono de advertencia y el mensaje: “¿Está seguro que desea eliminar
    el usuario “\[nombre de usuario\]”? Esta acción no se puede
    deshacer.” Los botones disponibles son “Cancelar” (texto azul) y
    “Eliminar” (botón rojo). \[FA07\]

3.  El Usuario Interno selecciona el botón “Eliminar”.

4.  El sistema elimina el usuario y muestra el mensaje: “El usuario
    \[nombre de usuario\] ha sido eliminado correctamente.”

5.  El sistema actualiza la tabla de usuarios automáticamente.

6.  Fin del caso de uso.

**FA06 – Validación de guardado fallida**

1.  El sistema detecta que uno o más campos no cumplen las reglas de
    negocio (ver RN-CU01-03 a RN-CU01-14).

2.  El sistema muestra los mensajes de error específicos de cada campo
    que falló.

3.  El sistema resalta los campos con error en color rojo.

4.  El Usuario Interno corrige los campos señalados.

5.  Se continúa en el paso de “Guardar/Crear/Actualizar” del flujo
    correspondiente.

**FA07 – Cancelar operación**

1.  El Usuario Interno selecciona el botón “Cancelar” en cualquier
    formulario (creación, edición o diálogo de confirmación de
    eliminación).

2.  Si es formulario de creación o edición: el sistema descarta los
    datos ingresados y redirige al Usuario Interno a la pantalla
    “Listado de Usuarios”.

3.  Si es diálogo de eliminación: el sistema cierra el diálogo modal y
    el usuario permanece en la pantalla “Listado de Usuarios” sin
    cambios.

4.  Fin del caso de uso.

## 2.5 Postcondiciones

1.  Los cambios en usuarios (creación, actualización, eliminación)
    quedan registrados en el sistema.

2.  El sistema registra un log de auditoría con la acción realizada, el
    usuario que la ejecutó y la fecha/hora.

# 

# 

**5. Firma y Sello**

| **Nombre** | **Puesto** | **Firma y Sello** |
|------------|------------|-------------------|
|            |            |                   |

---

## CU-2: Registro de Usuarios Externos

**Historial Revisiones**

| Nombre           | Fecha      | Descripción del cambio | Versión |
|------------------|------------|------------------------|---------|
| Ing. Edy Ramírez | 12/03/2026 | Planteamiento inicial  | 1.0.0   |
| Ing. Edy Ramírez | 12/03/2026 |                        | 1.0.0   |

# 

# 1. Introducción

**Descripción**

El presente documento describe los pasos que el Usuario Externo deberá
seguir para registrarse en el sistema informático hospitalario,
proporcionando sus datos personales, de contacto y de seguro médico.

**Objetivo**

Permitir a los pacientes nuevos registrarse en el sistema del hospital
para habilitar el proceso de agendamiento de citas médicas.

# 2. Definición Caso de Uso

## 2.1 Actores

- Usuario Externo (Paciente)

- Sistema informático

## 2.2 Precondiciones

- El sistema debe estar disponible.

- El Usuario Externo debe haber accedido al portal web del hospital.

## 2.3 Flujo Normal Básico

1.  El sistema muestra en pantalla el formulario de registro con los
    campos requeridos.

2.  El Usuario Externo ingresa su nombre completo. \[RN-CU02-01\]

3.  El Usuario Externo ingresa su número de DPI. \[RN-GLOBAL-001\]

4.  El Usuario Externo ingresa su número de NIT. \[RN-GLOBAL-002\]

5.  El Usuario Externo ingresa su número de teléfono. \[RN-CU02-02\]

6.  El Usuario Externo ingresa su número de afiliado del seguro médico
    (opcional). \[RN-CU02-03\]

7.  El Usuario Externo ingresa su correo electrónico. \[RN-CU02-04\]

8.  El Usuario Externo ingresa un nombre de usuario (entre 8 y 9
    caracteres). \[RN-CU02-05\]

9.  El Usuario Externo ingresa una contraseña (mínimo 12 caracteres).
    \[RN-CU02-06\]

10. El Usuario Externo selecciona el botón "Registrarse". \[FA01\]

11. El sistema valida la información ingresada. \[FA02\] \[FA03\]
    \[FA04\]

12. El sistema registra al Usuario Externo en la base de datos.

13. El sistema muestra el mensaje: "¡Registro exitoso! Su cuenta ha sido
    creada. Ahora puede iniciar sesión con sus credenciales."

14. El sistema envía un correo electrónico de bienvenida al Usuario
    Externo con el asunto: "Bienvenido al Sistema de Citas - Hospital
    \[Nombre\]" y el cuerpo: "Estimado(a) \[Nombre\], su registro ha
    sido completado exitosamente. Ya puede agendar sus citas médicas a
    través de nuestro portal."

15. El sistema redirige al Usuario Externo a la pantalla de inicio de
    sesión para que ingrese con sus nuevas credenciales. \[CU-00, paso
    8\]

16. Fin del caso de uso.

## 2.4 Flujos Alternos

**FA01 El Usuario Externo regresa al portal**

1.  El Usuario Externo selecciona el enlace "Volver al portal".

2.  El sistema descarta los datos del formulario sin mostrar
    confirmación.

3.  El Usuario Externo confirma la cancelación.

4.  El sistema descarta los datos y regresa al portal web. \[CU-00\]

5.  Fin del caso de uso.

**FA02 DPI ya registrado en el sistema**

1.  El sistema detecta que el DPI ingresado ya existe en la base de
    datos.

2.  El sistema muestra el mensaje: "Ya existe una cuenta registrada con
    este número de DPI. Si ya tiene cuenta, inicie sesión."

3.  El Usuario Externo debe dirigirse a la pantalla de inicio de sesión
    para autenticarse con su cuenta existente.

4.  Fin del caso de uso.

**FA03 Correo electrónico ya registrado**

1.  El sistema detecta que el correo electrónico ya está asociado a otra
    cuenta.

2.  El sistema muestra el mensaje: "Ya existe una cuenta registrada con
    este correo electrónico."

3.  El Usuario Externo corrige el correo electrónico.

4.  Se continúa en el paso 8 del flujo normal básico.

**FA04 Validación de campos fallida**

1.  El sistema detecta que uno o más campos no cumplen las reglas de
    negocio.

2.  El sistema muestra los mensajes de error específicos de cada campo
    (ver RN-CU02-01 a RN-CU02-06 y RN-GLOBAL-001, RN-GLOBAL-002).

3.  El sistema resalta los campos con error en color rojo.

4.  El Usuario Externo corrige los campos señalados.

5.  Se continúa en el paso 8 del flujo normal básico.

## 2.5 Postcondiciones

- El Usuario Externo queda registrado en el sistema con estado activo.

- Los datos del paciente quedan disponibles para el proceso de
  agendamiento de citas.

- El Usuario Externo recibe un correo de confirmación de registro.

# 3. Firma y Sello

| **Nombre** | **Puesto** | **Firma y Sello** |
|------------|------------|-------------------|
|            |            |                   |

---

## CU-3: Agendar Citas

**Historial Revisiones**

| Nombre           | Fecha      | Descripción del cambio | Versión |
|------------------|------------|------------------------|---------|
| Ing. Edy Ramírez | 12/03/2026 | Planteamiento inicial  | 1.0.0   |
| Ing. Edy Ramírez | 12/03/2026 |                        | 1.0.0   |

# 

# 1. Introducción

**Descripción**

El presente documento describe los pasos que el Usuario Externo deberá
seguir para agendar una cita médica mediante un asistente de 5 pasos:
selección de sucursal, especialidad, médico, fecha/hora y confirmación
con motivo de consulta.

**Objetivo**

Permitir al paciente registrado agendar citas médicas de forma
eficiente, con selección de especialidad, sucursal y horarios
disponibles.

# 2. Definición Caso de Uso

## 2.1 Actores

- Usuario Externo (Paciente)

- Sistema informático

## 2.2 Precondiciones

- El sistema debe estar disponible.

- El Usuario Externo debe estar registrado en el sistema \[CU-02\].

- Debe existir disponibilidad de horarios según la especialidad
  seleccionada.

- Catálogos deben de estar previamente cargado en cache.

## 2.3 Flujo Normal Básico

1.  El sistema muestra en pantalla el asistente de agendamiento de cita
    con un indicador de progreso de 5 pasos.

2.  Paso 1 – Sucursal: El Usuario Externo selecciona una sucursal de la
    lista de sucursales activas<span class="mark">.
    \[RN-CU03-02\]</span>

3.  Paso 2 – Especialidad: El sistema carga las especialidades
    disponibles en la sucursal seleccionada. El Usuario Externo
    selecciona una especialidad.
    <span class="mark">\[RN-CU03-01\]</span> \[FA02\]

4.  Paso 3 – Médico: El sistema carga los médicos disponibles para la
    especialidad y sucursal seleccionadas. El Usuario Externo selecciona
    un médico. \[RN-CU03-03\]

5.  Paso 4 – Fecha y Hora: El sistema muestra el calendario
    (DynamicCalendar) con la disponibilidad de horarios del médico
    seleccionado. El Usuario Externo selecciona un día y hora
    disponible<span class="mark">. \[RN-CU03-05\]</span> \[FA02\]

6.  Paso 5 – Confirmar: El sistema muestra el resumen de la cita con los
    datos: sucursal, especialidad, médico, fecha y hora seleccionados.

7.  El Usuario Externo ingresa el motivo de la consulta (mínimo 10
    caracteres, máximo 2000). <span class="mark">\[RN-CU03-03\]</span>

8.  El Usuario Externo selecciona el botón "Confirmar Cita". \[FA04\]

9.  El sistema registra la cita con estado "Pendiente de pago" y muestra
    el mensaje: "Su cita ha sido registrada exitosamente. Será
    redirigido al proceso de pago para confirmar la reserva."

10. El sistema redirige al apartado de pago en línea donde se inicia un
    temporizador de 5 minutos para completar el pago. \[CU-04\] \[FA03\]

11. El caso de uso finaliza cuando el Usuario Externo es redirigido a la
    pantalla de pago.

12. Nota: En cada paso del wizard el Usuario Externo puede regresar al
    paso anterior mediante el botón "Volver".

13. Fin del caso de uso.

## 2.4 Flujos Alternos

**FA01 No hay especialidades disponibles en la sucursal**

1.  La sucursal seleccionada no tiene especialidades configuradas.

2.  El sistema muestra el mensaje: "No hay especialidades disponibles
    para la sucursal \[nombre sucursal\]. Seleccione otra sucursal."

3.  El Usuario Externo regresa al paso 1 y selecciona otra sucursal.

4.  Se continúa en el paso 2 del flujo normal básico.

**FA02 No hay médicos o disponibilidad de horarios**

1.  El sistema no encuentra horarios disponibles para la combinación de
    especialidad y sucursal.

2.  El sistema muestra el mensaje: "No se encontraron horarios
    disponibles para la especialidad \[Especialidad\] en la Sede
    \[Sede\]. Por favor, seleccione otra especialidad o sede."

3.  El Usuario Externo modifica la selección.

4.  Se continúa en el paso 2 del flujo normal básico.

**FA03 Tiempo de reserva expirado**

1.  El temporizador llega a cero sin que el Usuario Externo confirme.

2.  El sistema libera el horario reservado temporalmente.

3.  El sistema muestra el mensaje: "El tiempo para confirmar su cita ha
    expirado. El horario seleccionado ha sido liberado. Por favor,
    seleccione un nuevo horario."

4.  El sistema regresa al paso 4 (selección de fecha/hora) del flujo
    normal básico.

**FA04 El Usuario Externo regresa a un paso anterior**

1.  El Usuario Externo selecciona el botón "Volver" en cualquier paso
    del wizard.

2.  El sistema regresa al paso anterior del wizard sin confirmación
    adicional.

3.  Las selecciones de los pasos posteriores se reinician.

4.  El sistema libera el horario reservado temporalmente.

5.  El sistema muestra el paso anterior del wizard y el Usuario Externo
    puede continuar desde allí.

## 2.5 Postcondiciones

- La cita queda registrada en el sistema con estado "Pendiente de pago".

- El horario seleccionado queda reservado hasta completar el pago.

- El motivo de la consulta queda registrado y asociado a la cita.

# 3. Firma y Sello

| **Nombre** | **Puesto** | **Firma y Sello** |
|------------|------------|-------------------|
|            |            |                   |

---

## CU-4: Pago en Línea

**Historial Revisiones**

| Nombre           | Fecha      | Descripción del cambio | Versión |
|------------------|------------|------------------------|---------|
| Ing. Edy Ramírez | 12/03/2026 | Planteamiento inicial  | 1.0.0   |
| Ing. Edy Ramírez | 12/03/2026 |                        | 1.0.0   |

# 

# 1. Introducción

**Descripción**

El presente documento describe los pasos que el Usuario Externo
(paciente) deberá seguir para realizar el pago en línea de su cita
médica mediante tarjeta de crédito o débito.

**Objetivo**

Permitir al paciente completar el pago de su cita médica de forma segura
a través de una pasarela de pago en línea, garantizando la confirmación
automática de la transacción y el envío de comprobante.

# 2. Definición Caso de Uso

## 2.1 Actores

- Usuario Externo (Paciente)

- Sistema informático

- Pasarela de pago

## 2.2 Precondiciones

- El sistema debe estar disponible.

- El paciente debe tener una cita agendada previamente \[CU-03\].

- El paciente debe contar con una tarjeta de crédito o débito válida.

- La pasarela de pago debe estar operativa.

- Catalogos previamente cargados en cache.

## 2.3 Flujo Normal Básico

1.  El sistema muestra la pantalla “Pago de Consulta” con un
    temporizador regresivo de 5 minutos (ReservationTimer) y el resumen
    de la cita: médico, especialidad, Sede, fecha, hora y total a pagar
    en Quetzales. \[FA02\]

2.  El sistema muestra el formulario “Datos de pago” con los campos:
    número de tarjeta, nombre del titular, vencimiento (MM/AA) y CVV.

3.  El paciente ingresa el número de tarjeta (13-19 dígitos, validación
    Luhn). Al perder el foco, el número se enmascara mostrando solo los
    últimos 4 dígitos. \[RN-CU04-01\]

4.  El paciente ingresa el nombre del titular de la tarjeta (mínimo 5,
    máximo 100 caracteres, se convierte a mayúsculas). \[RN-CU04-02\]

5.  El paciente ingresa la fecha de vencimiento en formato MM/AA
    (auto-formateado). \[RN-CU04-03\]

6.  El paciente ingresa el código de seguridad CVV (3-4 dígitos, campo
    tipo password). Nota de seguridad: Tu información de pago está
    protegida. El CVV nunca se almacena ni se envía. \[RN-CU04-04\]

7.  El paciente selecciona el botón “Pagar Q\[monto\]”. \[FA01\]
    \[FA03\]

8.  El sistema valida todos los campos del formulario. Si hay errores,
    muestra el mensaje de validación correspondiente debajo de cada
    campo. \[FA01\]

9.  El botón cambia a estado “Procesando pago...” y se deshabilita para
    evitar doble envío (idempotencia mediante UUID).

10. El sistema envía los datos a la pasarela de pago de forma
    encriptada.

11. La pasarela de pago procesa la transacción y retorna resultado
    exitoso.

12. El sistema confirma el pago y actualiza el estado de la cita a
    "Pagada". \[FA03\]

13. El sistema muestra el mensaje: "¡Pago realizado exitosamente! Número
    de transacción: \[Número\]. Su cita ha sido confirmada."

14. El sistema redirige a la pantalla “¡Pago Exitoso!”
    (ConfirmationPage) que muestra un comprobante de pago con: número de
    transacción, médico, especialidad, sucursal, fecha, hora y monto
    pagado. Además, muestra un aviso de que se ha enviado un correo de
    confirmación al email del paciente. \[RN-CU04-05\]

15. La pantalla de confirmación ofrece dos botones: “Volver al Portal”
    (redirige al dashboard) y “Ver Mis Citas” (redirige al listado de
    citas del paciente). Fin del caso de uso.

## 2.4 Flujos Alternos

**FA01 Validación de campos del formulario fallida**

1.  El sistema detecta que uno o más campos no cumplen las reglas de
    validación (ver RN-CU04-01 a RN-CU04-04).

2.  El sistema muestra los mensajes de error específicos debajo de cada
    campo que falló (ej. “El número de tarjeta no es válido”, “Formato
    inválido. Use MM/AA”, “La tarjeta está vencida”).

3.  El paciente corrige los campos señalados.

4.  Se continúa en el paso 8 del flujo normal básico.

5.  Fin del caso de uso.

**FA02 Temporizador de reserva expirado (5 minutos)**

1.  El temporizador regresivo de 5 minutos llega a cero sin que el
    paciente complete el pago.

2.  El sistema muestra un banner de error con el mensaje: "El tiempo
    para confirmar su cita ha expirado. El horario seleccionado ha sido
    liberado. Por favor, seleccione un nuevo horario. Será redirigido en
    unos segundos..."

3.  El sistema redirige automáticamente al paciente a la pantalla de
    reserva de citas después de 4 segundos.

4.  El paciente debe seleccionar un nuevo horario e iniciar el proceso
    de reserva nuevamente.

**FA03 Pago rechazado por la pasarela**

1.  La pasarela de pago rechaza la transacción.

2.  El sistema muestra un mensaje de error según el tipo de fallo:

3.  \- Rechazo bancario: "La transacción con tarjeta fue rechazada por
    el banco. Por favor, verifique los datos de su tarjeta o intente con
    una tarjeta diferente."

4.  \- Error de procesamiento: "El pago no pudo ser procesado. Por
    favor, intente nuevamente o utilice otra tarjeta."

5.  \- Error de comunicación: "Error de comunicación con la pasarela de
    pago. Intente nuevamente en unos minutos."

6.  El formulario de pago permanece activo y el temporizador de reserva
    sigue corriendo.

7.  El paciente puede corregir los datos y reintentar el pago sin perder
    la reserva del horario.

8.  El sistema permite al paciente reintentar el pago.

9.  Se continúa en el paso 4 del flujo normal básico.

## 2.5 Postcondiciones

- La cita queda registrada con estado "Pagada" en el sistema.

- El paciente recibe un comprobante de pago en su correo electrónico.

# 3. Firma y Sello

| **Nombre** | **Puesto** | **Firma y Sello** |
|------------|------------|-------------------|
|            |            |                   |

---

## CU-5: Recepción y Verificación de Cita

**Historial Revisiones**

| Nombre           | Fecha      | Descripción del cambio | Versión |
|------------------|------------|------------------------|---------|
| Ing. Edy Ramírez | 12/03/2026 | Planteamiento inicial  | 1.0.0   |
| Ing. Edy Ramírez | 12/03/2026 |                        | 1.0.0   |

# 

# 1. Introducción

**Descripción**

El presente documento describe los pasos que el Empleado Interno
(Recepcionista) deberá seguir para verificar la cita del paciente al
momento de su llegada a la clínica, validar su registro en el sistema y
gestionar su ingreso al proceso de atención médica.

**Objetivo**

Garantizar un proceso de recepción ordenado y eficiente que permita
verificar la identidad del paciente, confirmar su cita y el estado de
pago, y registrar su llegada en el sistema.

# 2. Definición Caso de Uso

## 2.1 Actores

- Paciente

- Empleado Interno (Recepcionista)

- Sistema informático

## 2.2 Precondiciones

- El sistema debe estar disponible.

- El Empleado Interno debe haber iniciado sesión en el sistema.

- El paciente debe presentarse en la recepción de la clínica.

## 2.3 Flujo Normal Básico

1.  El paciente llega a la recepción con su número de cita o DPI.
    \[FA01\]

2.  El Empleado Interno solicita el número de cita o DPI del paciente.

3.  El Empleado Interno ingresa el dato en el sistema para buscar la
    cita. \[FA02\] \[RN-CU05-01\]

4.  El sistema muestra los datos de la cita: nombre del paciente,
    estado, prioridad (si aplica), número de cita, especialidad,
    sucursal, fecha, motivo de consulta y hora de llegada (si fue
    registrada). \[RN-CU05-02\]

5.  El sistema confirma que el paciente está registrado y la cita tiene
    estado "Confirmada". \[FA03\] \[FA04\] \[FA05\] \[FA06\]

6.  El Empleado Interno confirma la llegada del paciente seleccionando
    el botón "Registrar llegada".

7.  El sistema cambia el estado de la cita de "Confirmada" a "Paciente
    Presente", registra la hora de llegada y muestra el mensaje: "La
    llegada del paciente \[Nombre\] ha sido registrada exitosamente. El
    paciente debe pasar a la sala de espera." \[FA08\] \[FA09\] La cita
    muestra el indicador "Llegada registrada — esperando llamado de
    enfermería".

8.  El Empleado Interno indica al paciente que pase a la sala de espera
    para la toma de signos vitales.

9.  Fin del caso de uso.

## 2.4 Flujos Alternos

**FA01 Paciente llega por emergencia**

1.  El paciente llega a recepción indicando una emergencia.

2.  El Empleado Interno toma los datos básicos del paciente (nombre,
    DPI).

3.  El Empleado Interno clasifica la prioridad según triaje.

4.  El sistema registra al paciente con indicación de "Emergencia" y
    muestra: "Paciente \[Nombre\] registrado con prioridad de
    EMERGENCIA. El paciente debe pasar directamente a toma de signos
    vitales."

5.  Se continúa directamente al proceso de toma de signos vitales.
    \[CU-07\]

**FA02 Paciente llega sin número de cita**

1.  El paciente llega a recepción sin número de cita.

2.  El Empleado Interno busca al paciente por DPI utilizando el buscador
    con botones de alternancia "Por DPI" / "Por No. Cita".

3.  Si se encuentra cita: el sistema muestra los datos. Se continúa en
    el paso 4 del flujo normal básico.

4.  Si no se encuentra cita: el sistema verifica si el paciente existe
    en la base de datos. Si el paciente no existe, se activa \[FA03\].
    Si el paciente existe pero no tiene citas activas, se activa
    \[FA04\].

**FA03 Paciente no registrado en el sistema**

1.  El sistema verifica la existencia del paciente y determina que no
    existe ningún paciente con ese DPI.

2.  El sistema muestra el mensaje: "No se encontró ningún paciente con
    ese DPI." con el sub-texto "Es necesario registrar al paciente antes
    de continuar."

3.  El sistema presenta únicamente el botón "Registrar Paciente".

4.  El Empleado Interno selecciona "Registrar Paciente" y el sistema lo
    dirige a la pantalla de registro de pacientes. \[CU-02\]

5.  Una vez completado el registro, el Empleado Interno regresa a la
    pantalla de recepción y busca al paciente para continuar con el
    flujo normal básico desde el paso 3.

**FA04 Paciente registrado sin citas activas**

6.  El sistema verifica la existencia del paciente y confirma que sí
    está registrado en el sistema, pero no tiene citas activas.

7.  El sistema muestra el mensaje: "El paciente \[Nombre\] está
    registrado pero no tiene citas activas." con el sub-texto "Puede
    crear una nueva cita para este paciente."

8.  El sistema presenta únicamente el botón "Nueva Cita (Walk-in)". No
    se muestra el botón "Registrar Paciente" ya que el paciente ya
    existe.

9.  El Empleado Interno selecciona "Nueva Cita (Walk-in)" y agenda la
    cita para el paciente. \[CU-03\] Una vez completado, regresa a la
    pantalla de recepción y busca la cita para continuar con el flujo
    normal básico desde el paso 3.

**FA05 Cita sin pago confirmado**

1.  El sistema muestra el mensaje: "La cita del paciente tiene estado
    'Pendiente de pago'. Debe realizar el pago en caja antes de ser
    atendido."

2.  El Empleado Interno indica verbalmente al paciente que debe
    dirigirse a la ventanilla de caja para realizar el pago antes de ser
    atendido. \[CU-06\]

3.  Una vez que el cajero procesa el pago desde su propia pantalla, el
    estado de la cita se actualiza automáticamente a “Confirmada”.

4.  El Empleado Interno continúa atendiendo a los demás pacientes en la
    fila. Cuando el paciente regresa con el pago confirmado, se retoma
    desde el paso 1 del flujo normal básico.

**FA06 Cita cancelada**

1.  El sistema muestra el mensaje: "La cita fue cancelada. El paciente
    debe agendar una nueva cita."

2.  El Empleado Interno informa al paciente y el sistema muestra el
    botón "Nueva Cita" para agendar una nueva cita.

3.  Fin del caso de uso.

**FA07 Reasignación de médico**

1.  La cita tiene estado "Confirmada" o "Paciente Presente" y el
    Empleado Interno necesita cambiar el médico asignado.

2.  El Empleado Interno selecciona el botón "Reasignar Médico" que
    aparece en la tarjeta de la cita con estado "Confirmada" o "Paciente
    Presente".

3.  El sistema dirige al Empleado Interno a la pantalla de reasignación
    de médico. En dicha pantalla se muestra un resumen de la cita
    (paciente, fecha, especialidad, sede, médico actual) y una lista de
    médicos disponibles de la misma sede y especialidad. Opcionalmente
    se puede agregar una nota con el motivo de la reasignación.

4.  El Empleado Interno selecciona el nuevo médico y presiona "Confirmar
    Reasignación". El sistema muestra "Médico reasignado correctamente"
    y redirige a la lista de citas.

**FA08 Cita con prioridad de emergencia**

1.  La cita tiene prioridad de emergencia. El sistema muestra la
    etiqueta "EMERGENCIA" junto al nombre del paciente.

2.  El sistema muestra un botón adicional "Signos Vitales (Urgente)" que
    permite pasar directamente a la toma de signos vitales. \[CU-07\]

3.  Al registrar la llegada de un paciente con emergencia, el sistema
    muestra: "Paciente \[Nombre\] registrado con prioridad de
    EMERGENCIA. El paciente debe pasar directamente a toma de signos
    vitales."

**FA09 Error al registrar llegada**

1.  El cambio de estado de la cita falla (por ejemplo, la cita ya fue
    actualizada por otro usuario).

2.  El sistema muestra el mensaje de error correspondiente (por ejemplo:
    "Operación no permitida") o el mensaje genérico "Error al registrar
    la llegada".

3.  El Empleado Interno puede reintentar la operación o verificar el
    estado actual de la cita.

## 2.5 Postcondiciones

- La llegada del paciente queda registrada en el sistema con la hora
  exacta de llegada.

- El estado de la cita se actualiza a "Paciente Presente".

- El paciente está listo para pasar a la toma de signos vitales.

# 3. Firma y Sello

| **Nombre** | **Puesto** | **Firma y Sello** |
|------------|------------|-------------------|
|            |            |                   |

---

## CU-6: Cobro de Consulta en Caja

**Historial Revisiones**

| Nombre           | Fecha      | Descripción del cambio | Versión |
|------------------|------------|------------------------|---------|
| Ing. Edy Ramírez | 12/03/2026 | Planteamiento inicial  | 1.0.0   |
| Ing. Edy Ramírez | 12/03/2026 |                        | 1.0.0   |

# 1. Introducción

**Descripción**

El presente documento describe los pasos que el Empleado Interno
(Cajero) y el paciente deberán seguir para realizar el cobro presencial
de la consulta médica cuando el pago no fue realizado en línea.

**Objetivo**

Gestionar el cobro presencial de la consulta médica, registrar el pago
en el sistema y emitir el comprobante correspondiente.

# 2. Definición Caso de Uso

## 2.1 Actores

- Paciente

- Empleado Interno (Cajero)

- Sistema informático

## 2.2 Precondiciones

- El sistema debe estar disponible.

- El Empleado Interno debe haber iniciado sesión en el sistema.

- El paciente debe tener una cita registrada con estado "Pendiente de
  pago".

## 2.3 Flujo Normal Básico

1.  El Empleado Interno busca la cita del paciente en el sistema por
    número de cita o DPI. \[RN-CU06-01\]

2.  El sistema muestra el detalle de la cita y el monto a cobrar:
    especialidad, médico, fecha, hora y monto total.

3.  El Empleado Interno informa al paciente el monto total de la
    consulta.

4.  El Empleado Interno selecciona el método de pago. Las opciones
    disponibles son: Efectivo, Visa, Mastercard o Débito. \[FA01\]
    \[FA02\] \[FA03\]

5.  El Empleado Interno recibe el pago en efectivo.

6.  El Empleado Interno ingresa el monto recibido en el campo "Monto
    Recibido" y selecciona el botón "Registrar Pago". Si el monto
    recibido es menor al monto a cobrar, el sistema muestra: "El monto
    recibido (Q\[recibido\]) es menor al monto a cobrar (Q\[total\])" y
    no permite continuar.

7.  El sistema calcula el cambio (si aplica) y muestra: "Monto recibido:
    Q\[monto\]. Cambio a devolver: Q\[cambio\]."

8.  El sistema confirma el pago y actualiza el estado de la cita a
    “Confirmada”. Muestra el mensaje: “¡Pago registrado exitosamente!
    Paciente: \[Nombre\]. La cita ha sido actualizada a estado
    Confirmada.”

9.  El sistema genera y muestra el comprobante de pago (componente
    PaymentReceipt) con los datos de la transacción. \[RN-CU06-03\]

10. El Empleado Interno puede imprimir el comprobante o iniciar un nuevo
    cobro con el botón "Nuevo Cobro".

11. Fin del caso de uso.

## 2.4 Flujos Alternos

**FA01 - Pago con tarjeta de crédito o débito:**

1.  El paciente indica que desea pagar con tarjeta de crédito o débito.

2.  El Empleado Interno selecciona el tipo de tarjeta (Visa, Mastercard
    o Débito) en el sistema.

3.  El Empleado Interno ingresa los últimos 4 dígitos de la tarjeta del
    paciente como referencia.

4.  El Empleado Interno selecciona "Registrar Pago". \[FA04\]

5.  Se continúa en el paso 8 del flujo normal básico.

**FA02 - No se encuentran citas pendientes:**

1.  La búsqueda por DPI o número de cita no encuentra citas con estado
    "Pendiente".

2.  El sistema muestra: "No se encontraron citas pendientes de pago para
    el criterio ingresado."

3.  El Empleado Interno verifica los datos e intenta nuevamente con otro
    criterio de búsqueda.

4.  Se continúa en el paso 1 del flujo normal básico.

**FA03 - El paciente no puede realizar el pago:**

1.  El paciente indica que no puede realizar el pago en ese momento o no
    se presenta en la ventanilla de caja.

2.  El Empleado Interno no procesa el cobro. La cita permanece en estado
    “Pendiente de Pago” y la consulta no podrá realizarse sin el pago
    previo.

3.  El Empleado Interno continúa atendiendo a los demás pacientes en la
    fila.

4.  Si la cita fue agendada por el paciente desde el portal, el sistema
    aplicará la cancelación automática tras 10 minutos sin pago. Las
    citas creadas por personal interno no se cancelan automáticamente.

5.  Fin del caso de uso.

**FA04 - Pago con tarjeta rechazado:**

1.  El sistema rechaza la transacción con tarjeta.

2.  El sistema muestra el mensaje: "La transacción con tarjeta fue
    rechazada por el banco. Solicite al paciente otro método de pago."

3.  El Empleado Interno informa al paciente del rechazo.

4.  El paciente puede intentar con otra tarjeta o cambiar a efectivo.

5.  Se continúa en el paso 4 del flujo normal básico.

## 2.5 Postcondiciones

- El pago queda registrado en el sistema con número de transacción.

- La cita se actualiza a estado “Confirmada”.

- El paciente recibe su comprobante de pago impreso.

# 3. Firma y Sello

| Nombfre | **Puesto** | **Firma y Sello** |
|---------|------------|-------------------|
|         |            |                   |

---

## CU-7: Toma de Signos Vitales

**Historial Revisiones**

| Nombre           | Fecha      | Descripción del cambio | Versión |
|------------------|------------|------------------------|---------|
| Ing. Edy Ramírez | 12/03/2026 | Planteamiento inicial  | 1.0.0   |
| Ing. Edy Ramírez | 12/03/2026 |                        | 1.0.0   |

# 

# 1. Introducción

**Descripción**

El presente documento describe los pasos que el personal de enfermería
deberá seguir para la toma y registro de signos vitales del paciente
previo a la consulta médica.

**Objetivo**

Registrar los signos vitales del paciente de forma precisa y oportuna en
el sistema, asegurando que el médico cuente con esta información al
momento de la consulta.

# 2. Definición Caso de Uso

## 2.1 Actores

- Paciente

- Empleado Interno (Enfermería)

- Sistema informático

## 2.2 Precondiciones

- El sistema debe estar disponible.

- El Empleado de enfermería debe haber iniciado sesión en el sistema.

- El paciente debe tener una cita con estado “Paciente Presente” (la
  llegada ya fue registrada por recepción).

- El paciente debe estar en la sala de espera.

- Catalogos previamente cargados en cache.

## 2.3 Flujo Normal Básico

1.  El sistema muestra automáticamente la lista de pacientes en estado
    “Paciente Presente”, separados de los que ya están en proceso de
    toma de signos. El Empleado de enfermería selecciona un paciente y
    presiona “Llamar y Tomar Signos”. El sistema anuncia por altavoz
    mediante síntesis de voz (TTS): “Turno número \[N\]. Paciente
    \[Nombre\], favor pasar a toma de signos vitales.” y transiciona la
    cita al estado “Signos Vitales”.

2.  El Empleado de enfermería selecciona “Registrar Signos Vitales” en
    la tarjeta del paciente llamado. El sistema abre el formulario de
    captura con el contexto de la cita precargado (nombre del paciente y
    número de cita). \[FA01\]

3.  El formulario muestra el nombre del paciente y número de cita como
    encabezado de contexto. Los campos de ID de cita e ID de enfermero
    se precargan automáticamente y no son visibles para el usuario.

4.  El Empleado de enfermería toma la presión arterial del paciente e
    ingresa los valores de presión sistólica (rango: 60-250 mmHg) y
    presión diastólica (rango: 40-150 mmHg) en campos separados.
    \[RN-CU07-01\]

5.  El Empleado de enfermería toma la temperatura del paciente e ingresa
    el valor (rango: 34-42 °C). \[RN-CU07-02\]

6.  El Empleado de enfermería registra el peso del paciente (rango:
    0.5-300 kg). \[RN-CU07-03\]

7.  El Empleado de enfermería registra la talla del paciente (rango:
    30-250 cm). \[RN-CU07-04\]

8.  El Empleado de enfermería toma la frecuencia cardíaca del paciente e
    ingresa el valor (rango: 30-220 lpm). \[RN-CU07-05\]

9.  El sistema muestra alertas clínicas en tiempo real si algún valor
    ingresado está fuera del rango clínico normal (componente
    VitalSignAlertsDisplay). \[FA03\] El Empleado de enfermería puede
    marcar el indicador "Es Emergencia" si aplica, y selecciona el botón
    "Registrar Signos Vitales".

10. El sistema valida que todos los campos estén completos y dentro de
    los rangos de captura. \[FA02\]

11. El sistema carga los signos vitales al historial del paciente.
    \[FA03\]

12. El sistema muestra el mensaje: “Signos vitales del paciente
    \[Nombre\] registrados correctamente. El paciente puede regresar a
    la sala de espera.” El sistema redirige automáticamente al panel de
    enfermería.

13. El paciente regresa a la sala de espera hasta ser llamado por el
    médico.

14. Fin del caso de uso.

## 2.4 Flujos Alternos

**FA01 - Paciente de emergencia (prioridad):**

1.  El Empleado de enfermería determina que el paciente es una
    emergencia (por indicación de recepción o evaluación directa).
    \[CU-05, FA01\]

2.  El Empleado de enfermería selecciona “Sí” en el indicador
    “¿Emergencia?” del formulario de signos vitales.

3.  El Empleado de enfermería completa los campos de signos vitales y
    registra el formulario con la marca de emergencia activada.

4.  El sistema almacena los signos vitales con el campo isEmergency =
    true.

5.  El sistema muestra: "Signos vitales de emergencia registrados para
    paciente \[Nombre\]. El paciente debe pasar directamente a consulta
    médica."

6.  El sistema redirige al panel de enfermería. El paciente pasa
    directamente a consulta médica sin regresar a la sala de espera.

**FA02 - Valores fuera de rango de captura:**

1.  El sistema detecta que uno o más valores están fuera del rango de
    captura permitido (ver RN01-RN05).

2.  El sistema muestra el mensaje de error específico del campo que
    falló.

3.  El Empleado de enfermería verifica la medición y corrige el valor.

4.  Se continúa en el paso 9 del flujo normal básico.

**FA03 - Valores fuera de rango clínico normal:**

1.  Mientras el Empleado ingresa los valores, el sistema detecta en
    tiempo real valores dentro del rango de captura pero fuera del rango
    clínico normal mediante el hook useVitalSignAlerts. \[RN-CU07-06\]

2.  El componente VitalSignAlertsDisplay muestra alertas visuales en
    tiempo real para cada signo vital fuera del rango normal, indicando
    el valor actual y el rango esperado.

3.  El Empleado de enfermería toma nota para informar al médico.

4.  Se continúa en el paso 12 del flujo normal básico.

## 2.5 Postcondiciones

- Los signos vitales del paciente quedan registrados en su historial
  clínico con fecha y hora.

- El paciente está listo para ser atendido por el médico.

- Si se detectaron valores anormales, la alerta queda registrada en el
  expediente.

# 3. Firma y Sello

| **Nombre** | **Puesto** | **Firma y Sello** |
|------------|------------|-------------------|
|            |            |                   |

---

## CU-8: Consulta Médica

**Historial Revisiones**

| Nombre           | Fecha      | Descripción del cambio | Versión |
|------------------|------------|------------------------|---------|
| Ing. Edy Ramírez | 12/03/2026 | Planteamiento inicial  | 1.0.0   |
| Ing. Edy Ramírez | 12/03/2026 |                        | 1.0.0   |

# 1. Introducción

**Descripción**

El presente documento describe los pasos que el médico deberá seguir
durante la consulta médica, incluyendo la revisión de signos vitales,
evaluación, diagnóstico, tratamiento y posibles derivaciones.

**Objetivo**

Documentar el proceso de consulta médica para garantizar una atención
integral al paciente, registrar diagnóstico y tratamiento, y gestionar
derivaciones.

# 2. Definición Caso de Uso

## 2.1 Actores

- Paciente

- Médico

- Sistema informático

## 2.2 Precondiciones

- El sistema debe estar disponible.

- El médico debe haber iniciado sesión en el sistema.

- El paciente debe tener signos vitales registrados \[CU-07\].

- La cita debe estar en estado “En Espera” (los signos vitales ya fueron
  registrados por enfermería).

## 2.3 Flujo Normal Básico

1.  El sistema muestra automáticamente el panel del médico con las citas
    asignadas, agrupadas en tres secciones: “En Espera de Consulta”, “En
    Consulta Médica” y “Evaluados — Pendiente de cierre”. La lista se
    actualiza automáticamente cada 30 segundos. Cada tarjeta muestra:
    número de cita, nombre del paciente, especialidad, fecha y estado.
    Si el paciente tiene prioridad de emergencia, se muestra la
    indicación “Emergencia”.

2.  El médico selecciona “Iniciar Consulta” en la tarjeta del paciente
    en espera. El sistema transiciona la cita al estado “Consulta
    Médica” y anuncia por altavoz mediante síntesis de voz (TTS): “Turno
    número \[N\]. Paciente \[Nombre\], favor pasar a consulta médica.”
    \[FA06\]

3.  El médico selecciona “Ver / Completar Consulta” en la tarjeta del
    paciente en estado “Consulta Médica”. El sistema abre el formulario
    de consulta médica con el contexto precargado (número de cita y
    nombre del paciente).

4.  El formulario presenta los campos: Motivo de Visita (obligatorio),
    Hallazgos Clínicos, Código CIE-10 (con autocompletado desde
    catálogo), Diagnóstico (obligatorio para finalizar), Plan de
    Tratamiento, Notas Adicionales y Estado de Consulta (En curso /
    Finalizada).

5.  El médico ingresa el motivo de visita del paciente.

6.  El médico realiza la consulta médica (anamnesis, exploración física)
    y registra los hallazgos clínicos en el campo correspondiente.

7.  El médico busca y selecciona el código CIE-10 del diagnóstico
    utilizando el campo de autocompletado. El sistema sugiere códigos en
    tiempo real conforme el médico escribe.

8.  El médico registra el diagnóstico descriptivo y el plan de
    tratamiento en los campos correspondientes. \[FA01\] \[FA02\]
    \[FA04\]

9.  El médico selecciona el estado de consulta como “Finalizada” y
    presiona “Guardar Consulta”. \[FA05\]

10. El sistema muestra: “La consulta ha sido finalizada exitosamente. El
    paciente puede proceder a las siguientes indicaciones médicas.” El
    sistema redirige al panel del médico. La cita aparece ahora en la
    sección “Evaluados — Pendiente de cierre”.

11. Desde la sección “Evaluados”, el médico puede generar órdenes de
    laboratorio \[FA01\], recetas médicas \[FA04\] o agendar citas de
    seguimiento \[FA02\] según corresponda. Cuando todo esté completo,
    el médico selecciona “Finalizar Atención”.

12. El sistema transiciona la cita al estado “Atención Finalizada” y
    muestra: “Atención finalizada para cita \#\[N\].”

13. Fin del caso de uso.

## 2.4 Flujos Alternos

**FA01 - El paciente requiere exámenes de laboratorio:**

1.  Durante la consulta, el médico determina que el paciente requiere
    exámenes de laboratorio.

2.  El médico selecciona la opción "Generar orden de laboratorio".

3.  El sistema muestra el formulario con catálogo de exámenes
    disponibles.

4.  El médico selecciona exámenes requeridos y agrega observaciones.

5.  El médico confirma seleccionando "Guardar orden".

6.  El sistema muestra: "Orden de laboratorio generada exitosamente.
    Número de orden: \[Número\]. Exámenes: \[lista\]. El paciente debe
    dirigirse al área de laboratorio."

7.  El paciente es derivado a laboratorio. \[CU-09\]

8.  Se continúa en el paso 11 del flujo normal básico.

**FA02 - Cita de seguimiento:**

1.  El médico determina que el paciente requiere una cita de seguimiento
    (monitoreo de tratamiento o revisión de resultados de laboratorio).

2.  El médico selecciona “Agendar Seguimiento” desde la sección de
    Evaluados del panel. El sistema navega al formulario de creación de
    cita con el parámetro de seguimiento precargado.

3.  El médico agenda la cita. \[CU-11\]

4.  El sistema muestra: "Cita de seguimiento agendada para el \[fecha\]
    a las \[hora\]. Se enviará notificación al paciente."

5.  Se continúa en el paso 11 del flujo normal básico.

**FA06 - Paciente no asistió:**

1.  El paciente en estado “En Espera” no se presenta cuando es llamado a
    consulta.

2.  El médico selecciona “No Asistió” en la tarjeta del paciente.

3.  El sistema transiciona la cita al estado “No Asistió”.

4.  El sistema muestra: “Cita \#\[N\] marcada como No Asistió.”

5.  La cita queda cerrada. Fin del caso de uso.

**FA04 - El paciente requiere medicamentos:**

1.  El médico selecciona "Generar receta médica".

2.  El sistema muestra formulario con catálogo de medicamentos.

3.  El médico ingresa: medicamento, dosis, frecuencia, duración,
    indicaciones. \[RN-CU08-03\]

4.  El médico confirma seleccionando "Guardar receta".

5.  El sistema muestra: "Receta médica generada exitosamente.
    Medicamentos: \[lista\]. El paciente puede adquirirlos en la
    farmacia de la clínica."

6.  El paciente puede ir a farmacia. \[CU-10\]

7.  Se continúa en el paso 11 del flujo normal básico.

**FA05 - Intento de finalizar consulta sin diagnóstico:**

1.  El médico intenta finalizar sin diagnóstico registrado.

2.  El sistema muestra: "No es posible finalizar la consulta sin
    registrar un diagnóstico. El campo Diagnóstico es obligatorio."

3.  El médico registra el diagnóstico.

4.  Se continúa en el paso 11 del flujo normal básico.

## 2.5 Postcondiciones

- El diagnóstico y tratamiento quedan en el historial clínico.

- Las órdenes de laboratorio (si aplica) quedan generadas.

- Las citas de seguimiento (si aplica) quedan agendadas.

- La receta médica (si aplica) queda disponible para farmacia.

# 3. Firma y Sello

| **Nombre** | **Puesto** | **Firma y Sello** |
|------------|------------|-------------------|
|            |            |                   |

---

## CU-9: Gestión de Laboratorio

**Historial Revisiones**

| Nombre           | Fecha      | Descripción del cambio | Versión |
|------------------|------------|------------------------|---------|
| Ing. Edy Ramírez | 12/03/2026 | Planteamiento inicial  | 1.0.0   |
| Ing. Edy Ramírez | 12/03/2026 |                        | 1.0.0   |

# 1. Introducción

**Descripción**

El presente documento describe los pasos que el personal de laboratorio
deberá seguir para recibir al paciente, procesar los exámenes y
registrar resultados.

**Objetivo**

Gestionar el proceso completo de laboratorio desde la recepción de la
orden hasta el registro de resultados y notificación al médico.

# 2. Definición Caso de Uso

## 2.1 Actores

- Paciente

- Personal de Laboratorio (primario)

- Empleado Interno - Cajero (ver \[CU-16\])

- Médico (secundario - notificado)

- Sistema informático

## 2.2 Precondiciones

- El sistema debe estar disponible.

- El personal de laboratorio debe haber iniciado sesión.

- El médico debe haber generado una orden de laboratorio \[CU-08\].

## 2.3 Flujo Normal Básico

1.  El personal de laboratorio accede a la pantalla “Órdenes de
    Laboratorio” donde el sistema muestra una tabla con todas las
    órdenes registradas, incluyendo filtros por estado, paciente y
    médico. \[FA01\]

2.  El personal selecciona una orden pendiente de la tabla. El sistema
    navega a la pantalla de detalle de la orden.

3.  El sistema muestra el resumen de la orden: número de orden,
    paciente, médico, estado (Pendiente / En proceso / Completada),
    monto total, indicador de orden externa si aplica, y notas. Debajo
    lista cada examen con su nombre y monto individual.

4.  El personal informa verbalmente al paciente los exámenes ordenados y
    el monto total, indicándole que debe realizar el pago en caja antes
    de la toma de muestras.

5.  El paciente se dirige a caja para realizar el pago de la orden de
    laboratorio. El proceso de cobro se detalla en \[CU-16 Cobro de
    Laboratorio en Caja\]. \[RN-CU09-01\]

6.  Una vez completado el cobro, el sistema actualiza el estado de la
    orden a “En proceso” (orderStatus = 1). La toma de muestras puede
    proceder.

7.  El personal de laboratorio verifica en el sistema que la orden tiene
    estado “En proceso” y realiza la toma de muestras al paciente.

8.  El personal procesa los exámenes de laboratorio (análisis, cultivos,
    etc.).

9.  El personal accede al detalle de la orden en el sistema y, para cada
    examen, completa el formulario de resultado con los campos: Valor
    del Resultado, Unidad, Fecha del Resultado, casilla “Fuera de Rango”
    (marcado manual si aplica), y Notas del Resultado. \[FA02\]

10. El personal presiona “Guardar Resultado” para cada examen. El
    sistema valida los campos y muestra: “Resultado guardado
    exitosamente.” \[RN-CU09-02\]

11. Para cada examen con resultado guardado, el personal presiona el
    botón “Publicar resultado” (publicación individual por examen, no
    masiva). El sistema muestra: “Resultado publicado exitosamente.”

12. El sistema marca el examen como publicado (isPublished = true) y
    muestra la etiqueta “Publicado” junto al examen. El botón “Publicar
    resultado” desaparece para ese examen.

13. El personal repite los pasos 10–12 hasta publicar todos los
    resultados de la orden. Nota: actualmente el sistema no envía
    notificación automática al médico; los resultados quedan disponibles
    para consulta en el historial de la orden.

14. Una vez todos los exámenes están publicados, la orden se considera
    completada. El personal puede navegar de regreso a la tabla de
    órdenes mediante el botón “Volver”.

15. Fin del caso de uso.

## 2.4 Flujos Alternos

**FA01 - Orden marcada como externa:**

1.  Al crear la orden de laboratorio (CU-08, paso 11), el médico marca
    la casilla “Orden Externa” en el formulario de creación.

2.  La orden aparece en la tabla de órdenes con la etiqueta “Externa”
    visible en el detalle de la orden.

3.  El paciente realiza los exámenes en un laboratorio externo y
    presenta los resultados al médico en su cita de seguimiento.

4.  El médico agenda seguimiento. \[CU-11\]

5.  Fin del caso de uso.

**FA02 - Resultado marcado fuera de rango:**

1.  Al registrar el resultado de un examen, el personal marca
    manualmente la casilla “Fuera de Rango” si el valor está fuera del
    rango de referencia.

2.  El sistema muestra una alerta visual roja junto al examen: un badge
    con el texto “Fuera de rango” y el rango de referencia entre
    paréntesis si está configurado. Esta alerta es informativa y no
    requiere confirmación adicional.

3.  El personal guarda el resultado normalmente. La alerta permanece
    visible para el médico al consultar los resultados.

4.  Se continúa en el paso 10 para publicar el resultado.

5.  Fin del flujo alterno.

## 2.5 Postcondiciones

- Los resultados quedan registrados y publicados en la orden de
  laboratorio.

- Los resultados están disponibles para consulta por el médico tratante.

- El pago del laboratorio queda registrado y la orden en estado “En
  proceso”.

# 3. Firma y Sello

| **Nombre** | **Puesto** | **Firma y Sello** |
|------------|------------|-------------------|
|            |            |                   |

---

## CU-10: Cobro de Laboratorio en Caja

**Historial Revisiones**

| Nombre           | Fecha      | Descripción del cambio | Versión |
|------------------|------------|------------------------|---------|
| Ing. Edy Ramírez | 12/03/2026 | Planteamiento inicial  | 1.0.0   |
| Ing. Edy Ramírez | 12/03/2026 |                        | 1.0.0   |

# 

# 1. Introducción

**Descripción**

El presente documento describe los pasos que el Empleado Interno
(Cajero) deberá seguir para realizar el cobro de órdenes de laboratorio
pendientes de pago, registrar la transacción y generar el comprobante
correspondiente.

**Objetivo**

Gestionar el cobro presencial de órdenes de laboratorio, registrar el
pago en el sistema, actualizar el estado de la orden y emitir el
comprobante de pago.

# 2. Definición Caso de Uso

## 2.1 Actores

- Paciente

- Empleado Interno (Cajero) — Actor primario

- Sistema informático

## 2.2 Precondiciones

- El sistema debe estar disponible.

- El Empleado Interno debe haber iniciado sesión en el sistema.

- El médico debe haber generado una orden de laboratorio \[CU-08\] con
  estado “Pendiente”.

## 2.3 Flujo Normal Básico

1.  El Empleado Interno accede a la pantalla “Cobro de Laboratorio en
    Caja” desde el Panel de Caja (botón “Cobro Lab”). \[FA01\]

2.  El Empleado Interno selecciona el criterio de búsqueda: “Por DPI”
    (DPI del paciente, 13 dígitos) o “Por No. Orden” (número de orden de
    laboratorio). Ingresa el valor y presiona “Buscar”.
    \[RN-GLOBAL-001\]

3.  El sistema muestra las órdenes de laboratorio pendientes de pago que
    coinciden con la búsqueda. Para cada orden se visualiza: número de
    orden, nombre del paciente, DPI, cantidad de exámenes, fecha de
    creación y monto total. \[FA02\]

4.  El Empleado Interno selecciona la orden correspondiente al paciente.

5.  El sistema muestra el formulario de cobro con el resumen de la
    orden: paciente, DPI, número de orden, cantidad de exámenes y el
    monto total a cobrar.

6.  El Empleado Interno selecciona el método de pago. Las opciones
    disponibles son: Efectivo (Q), Visa, Mastercard o Débito.
    \[RN-GLOBAL-004\] \[FA03\] \[FA04\]

7.  El Empleado Interno recibe el pago en efectivo, ingresa el monto
    recibido en el campo “Monto Recibido (Q)”. El sistema calcula y
    muestra en tiempo real el “Cambio a Devolver”. Si el monto recibido
    es menor al total, el sistema muestra: “El monto recibido
    (Q\[recibido\]) es menor al monto a cobrar (Q\[total\])” y no
    permite continuar.

8.  El Empleado Interno presiona “Confirmar Pago Q\[monto\]”.

9.  El sistema registra el pago, actualiza el estado de la orden de
    laboratorio a “En proceso” (orderStatus = 1), y muestra: “¡Pago de
    laboratorio registrado exitosamente! Paciente: \[Nombre\]. La orden
    ha sido actualizada a estado ‘En proceso’.”

10. El sistema genera y muestra el comprobante de pago (componente
    PaymentReceipt) con los datos de la transacción: sucursal
    “Laboratorio”, paciente, detalle de la orden y monto.
    \[RN-GLOBAL-005\] \[RN-CU09-01\]

11. El Empleado Interno puede imprimir el comprobante o iniciar un nuevo
    cobro con el botón “Nuevo Cobro”.

12. Fin del caso de uso.

## 2.4 Flujos Alternos

**FA01 - No se encuentran órdenes pendientes:**

1.  La búsqueda por DPI o número de orden no encuentra órdenes con
    estado pendiente de pago.

2.  El sistema muestra: “No se encontraron órdenes de laboratorio
    pendientes de pago. Verifique el DPI o número de orden e intente de
    nuevo.”

3.  El Empleado Interno verifica los datos e intenta nuevamente con otro
    criterio de búsqueda.

4.  Se continúa en el paso 2 del flujo normal básico.

**FA02 - El paciente no puede realizar el pago:**

1.  El paciente indica que no puede cubrir el costo de los exámenes en
    ese momento.

2.  El Empleado Interno no procesa el cobro. La orden permanece en
    estado “Pendiente”.

3.  El Empleado Interno puede informar al médico tratante para que
    evalúe modificar o cancelar la orden.

4.  Fin del caso de uso.

**FA03 - Pago con tarjeta de crédito o débito:**

1.  El paciente indica que desea pagar con tarjeta de crédito o débito.

2.  El Empleado Interno selecciona el tipo de tarjeta (Visa, Mastercard
    o Débito) en el selector de método de pago.

3.  El Empleado Interno ingresa los últimos 4 dígitos de la tarjeta del
    paciente en el campo correspondiente. Si no ingresa exactamente 4
    dígitos, el sistema muestra: “Ingrese los últimos 4 dígitos de la
    tarjeta.” \[FA04\]

4.  Se continúa en el paso 8 del flujo normal básico.

**FA04 - Pago con tarjeta rechazado:**

1.  El sistema rechaza la transacción con tarjeta.

2.  El sistema muestra: “La transacción con tarjeta fue rechazada por el
    banco. Solicite al paciente otro método de pago.”

3.  El Empleado Interno informa al paciente del rechazo.

4.  El paciente puede intentar con otra tarjeta o cambiar a efectivo.

5.  Se continúa en el paso 6 del flujo normal básico.

## 2.5 Postcondiciones

- El pago queda registrado en el sistema con número de transacción.

- La orden de laboratorio se actualiza a estado “En proceso”
  (orderStatus = 1).

- El paciente recibe su comprobante de pago.

- La toma de muestras puede proceder \[CU-09\].

# 3. Firma y Sello

| **Nombre** | **Puesto** | **Firma y Sello** |
|------------|------------|-------------------|
|            |            |                   |

---

## CU-11: Despacho de Medicamentos

**Historial Revisiones**

| Nombre           | Fecha      | Descripción del cambio | Versión |
|------------------|------------|------------------------|---------|
| Ing. Edy Ramírez | 12/03/2026 | Planteamiento inicial  | 1.0.0   |
| Ing. Edy Ramírez | 12/03/2026 |                        | 1.0.0   |

# 1. Introducción

1.  **Descripción**

2.  El presente documento describe los pasos que el personal de farmacia
    debe seguir para despachar los medicamentos recetados, incluyendo
    verificación de receta, búsqueda en inventario de farmacia y entrega
    al paciente. El registro de cobro formal queda fuera del alcance de
    esta versión del sistema.

3.  **Objetivo**

4.  Gestionar el despacho de medicamentos de forma segura y controlada,
    garantizando que el paciente reciba los medicamentos correctos según
    la receta, con control de inventario y trazabilidad del despacho.

# 2. Definición Caso de Uso

## 2.1 Actores

5.  Paciente

6.  Personal de Farmacia

7.  Sistema informático

## 2.2 Precondiciones

8.  El sistema debe estar disponible \[RN-GLOBAL-003\].

9.  El personal de farmacia debe haber iniciado sesión
    \[RN-GLOBAL-007\].

10. El médico debe haber registrado una receta médica \[CU-08\].

11. La receta debe estar dentro de su período de validez (máximo 7 días
    desde su emisión) \[RN-CU10-01\].

## 2.3 Flujo Normal Básico

12. El personal de farmacia accede al módulo de Despacho desde el panel
    de farmacia y selecciona “Nuevo Despacho”.

13. El sistema muestra la pantalla de búsqueda de recetas con filtros
    por ID de Receta e ID de Consulta. Solo se muestran recetas activas
    (state=1). El personal ingresa el criterio de búsqueda y el sistema
    muestra las recetas que coinciden, indicando para cada una: ID,
    Consulta, Fecha de Emisión, Vigencia (vigente/vencida con días
    transcurridos) y Notas.

14. El personal selecciona “Despachar” en la receta deseada. Las recetas
    vencidas (más de 7 días) tienen el botón deshabilitado con indicador
    rojo “Vencida”. \[RN-CU10-01\]

15. El sistema valida la vigencia de la receta (máximo 7 días)
    \[RN-CU10-01\]. Si está vencida, muestra: “Receta Vencida. La receta
    \#\[ID\] fue emitida hace \[X\] días y ya no es válida para
    despacho.” Si es válida, muestra el detalle de medicamentos
    recetados: nombre, dosis, cantidad y precio unitario.

16. El sistema consulta el inventario de la sucursal para cada
    medicamento y muestra la disponibilidad. \[FA01\]

17. El personal ajusta las cantidades si es necesario y verifica las
    sustituciones. \[FA02\]

18. El sistema calcula y muestra el monto total del despacho: "Total del
    Despacho: Q\[monto\]."

19. El personal verifica el monto total con el paciente. El cobro físico
    se realiza de forma externa al sistema (efectivo, tarjeta u otro
    medio). \[FA03\]

20. El personal selecciona "Confirmar Despacho" para registrar el
    despacho en el sistema.

21. El sistema crea el registro de despacho, registra cada ítem
    despachado y actualiza el inventario de farmacia automáticamente.
    \[FA04\]

22. El sistema muestra el resumen del despacho con el detalle de
    medicamentos despachados y el monto total calculado.

23. El sistema muestra: "Despacho registrado exitosamente. \[X\]
    medicamento(s) despachado(s). Total: Q\[monto\]."

24. Fin del caso de uso.

## 2.4 Flujos Alternos

25. **FA01 - Medicamento sin inventario disponible:**

26. El sistema indica: "Sin inventario registrado" junto al medicamento
    sin stock.

27. Si el medicamento tiene stock bajo, muestra alerta: "\[Nombre\]:
    Stock bajo — disponible: \[X\] (mínimo: \[Y\])." \[RN-CU10-03\]

28. El personal informa al paciente y evalúa alternativas.

29. Se continúa con los medicamentos disponibles en el paso 6.

30. **FA02 - Sustitución de medicamento:**

31. El personal marca la casilla de sustitución en el medicamento
    correspondiente.

32. El sistema habilita el campo "Razón de sustitución" (obligatorio).

33. El personal ingresa la razón y el medicamento alternativo.

34. El sistema muestra: "Medicamento \[original\] sustituido por
    \[alternativa\]. El médico tratante será notificado de la
    sustitución."

35. Se continúa en el paso 7.

36. **FA03 - El paciente no desea adquirir los medicamentos:**

37. El paciente indica que no desea comprar los medicamentos.

38. El personal cancela el despacho.

39. El sistema registra: "Se ha registrado que el paciente \[Nombre\] no
    adquirió los medicamentos recetados en farmacia interna. Receta:
    \[Número\]."

40. Fin del caso de uso.

41. **FA04 - Stock mínimo alcanzado tras despacho:**

42. Después del despacho, el sistema detecta que uno o más medicamentos
    han alcanzado el nivel de stock mínimo \[RN-CU10-03\].

43. El sistema muestra alerta: "ALERTA: El medicamento \[nombre\] ha
    alcanzado el nivel de stock mínimo (\[cantidad\] unidades
    restantes). Se recomienda generar orden de reabastecimiento."

44. El personal de farmacia toma nota para gestionar reabastecimiento.

## 2.5 Postcondiciones

45. Medicamentos despachados quedan en el historial del paciente.

46. El inventario se actualiza automáticamente.

47. El despacho queda registrado con el detalle de ítems y monto total.
    El cobro físico es responsabilidad del personal de farmacia fuera
    del sistema.

48. Si algún medicamento alcanzó el stock mínimo, la alerta queda
    visible.

# Firma y Sello

| **Nombre** | **Puesto** | **Firma y Sello** |
|------------|------------|-------------------|
|            |            |                   |

#

---

## CU-12: Agendamiento de Cita de Seguimiento

**Historial Revisiones**

| Nombre           | Fecha      | Descripción del cambio | Versión |
|------------------|------------|------------------------|---------|
| Ing. Edy Ramírez | 12/03/2026 | Planteamiento inicial  | 1.0.0   |
| Ing. Edy Ramírez | 12/03/2026 |                        | 1.0.0   |

# 1. Introducción

**Descripción**

El presente documento describe los pasos que el médico y el sistema
deberán seguir para agendar una cita de seguimiento, ya sea para
monitoreo de tratamiento o revisión de resultados de laboratorio.

**Objetivo**

Facilitar el agendamiento de citas de seguimiento desde la consulta
médica, garantizando notificación y recordatorio automáticos.

# 2. Definición Caso de Uso

## 2.1 Actores

- Médico

- Paciente

- Sistema informático

## 2.2 Precondiciones

- El sistema debe estar disponible y el médico debe haber iniciado
  sesión.

- Debe existir una consulta médica activa o recién finalizada \[CU-08\]
  en modo edición.

- El botón “Agendar Seguimiento” debe estar visible en la sección
  “Acciones de la consulta”.

## 2.3 Flujo Normal Básico

1.  El médico selecciona “Agendar Seguimiento” desde la sección
    “Acciones de la consulta” en el formulario de consulta médica
    \[CU-08\].

2.  El sistema navega a la pantalla de creación de cita con parámetros
    de seguimiento (followUp=true, parentConsultationId). El sistema
    consulta la consulta padre y la cita asociada para pre-cargar
    automáticamente: paciente, médico, especialidad y sucursal.

3.  El sistema muestra un banner verde con los datos pre-cargados y un
    selector de tipo de seguimiento con dos opciones: “Monitoreo de
    Tratamiento” y “Revisión de Resultados de Laboratorio”.
    \[RN-CU11-01\]

4.  El médico selecciona el tipo de seguimiento.

5.  El sistema muestra el calendario de disponibilidad del mismo médico
    (Paso 5 del wizard). El médico selecciona fecha y hora disponible.
    \[RN-CU11-02\] \[FA01\]

6.  El médico ingresa el motivo del seguimiento y prioridad en el paso
    de confirmación (Paso 6). \[RN-CU11-03\] \[FA02\]

7.  El médico confirma el agendamiento. El sistema envía la solicitud
    createAppointment incluyendo followUpType y parentConsultationId.

8.  El sistema muestra toast de éxito: “Cita de seguimiento agendada
    exitosamente. Tipo: \[tipo\]. Paciente: \[Nombre\].” y redirige al
    listado de citas.

9.  El backend envía notificación al correo del paciente
    (responsabilidad del servidor). \[RN-CU11-04\]

10. El backend programa el envío de un recordatorio automático
    (responsabilidad del servidor). \[RN-CU11-05\]

11. Fin del caso de uso.

## 2.4 Flujos Alternos

**FA01 – Conflicto de horario (paso 5):**

1.  El médico selecciona un horario que ya fue ocupado entre la
    selección y la confirmación.

2.  El sistema muestra alerta roja: “El horario seleccionado ya no está
    disponible. Por favor, elija otro horario.”

3.  El sistema regresa al paso 5 (selección de horario) automáticamente.

4.  El médico selecciona un nuevo horario disponible. Se continúa en
    paso 6 del FB.

**FA02 – El médico cancela el agendamiento (paso 5):**

1.  El médico selecciona “Volver” en el paso de selección de horario.

2.  El sistema navega de regreso a la pantalla de consulta médica
    (navigate(-1)).

3.  Los datos de seguimiento no se guardan.

4.  El médico puede reiniciar el proceso desde la consulta si lo desea.

5.  Fin del caso de uso.

## 2.5 Postcondiciones

- La cita de seguimiento queda registrada con followUpType y
  parentConsultationId asociados.

- El paciente recibe notificación por correo (backend). \[RN-CU11-04\]

- El recordatorio automático queda programado (backend). \[RN-CU11-05\]

# 3. Firma y Sello

| **Nombre** | **Puesto** | **Firma y Sello** |
|------------|------------|-------------------|
|            |            |                   |

---

## CU-13: Configuración de Sedes y Especialidades

# CU-13 Configuración de

# Sedes y Especialidades

**Historial Revisiones**

| Nombre           | Fecha      | Descripción del cambio | Versión |
|------------------|------------|------------------------|---------|
| Ing. Edy Ramírez | 12/03/2026 | Planteamiento inicial  | 1.0.0   |
| Ing. Edy Ramírez | 12/03/2026 |                        | 1.0.0   |

# 1. Introducción

**Descripción**

El presente documento describe los pasos que el Administrador deberá
seguir para configurar qué especialidades médicas están disponibles en
cada sede/sucursal del hospital. Esta configuración es requisito previo
para el agendamiento de citas \[CU-03\], ya que determina las opciones
que verá el paciente al seleccionar especialidad y sucursal.

**Objetivo**

Permitir al Administrador gestionar la relación entre sedes y
especialidades médicas, garantizando que el portal de citas muestre
únicamente las especialidades disponibles en cada ubicación.

# 2. Definición Caso de Uso

## 2.1 Actores

- Usuario Interno (Administrador) — Actor primario

<!-- -->

- Sistema informático — Actor secundario

## 2.2 Precondiciones

- El sistema debe estar disponible. \[RN-GLOBAL-003\]

- El Administrador debe haber iniciado sesión con permisos de
  administración. \[RN-GLOBAL-007\]

- Deben existir sucursales registradas en el sistema \[CU-15\].

- Deben existir especialidades registradas en el sistema \[CU-15\].

## 2.3 Flujo Normal Básico

1.  El Administrador accede al Dashboard de Administración.

<!-- -->

1.  Selecciona el botón rápido “Especialidades por Sede”; el sistema
    navega a /branch-specialty y muestra el listado en tabla.

2.  El sistema muestra el listado en tabla con las columnas: ID, Sede,
    Especialidad, Estado y Acciones. Incluye filtro por ID y paginación.
    \[FA01\] \[FA02\]

3.  El Administrador selecciona el botón “Asignar Especialidad”. El
    sistema navega a /branch-specialty/create.

4.  El sistema muestra el formulario de asignación con los campos: Sede
    (dropdown) y Especialidad (dropdown). El estado se asigna
    automáticamente como activo. \[RN-CU12-01\]

5.  El Administrador selecciona la Sede y la Especialidad deseada.

6.  El Administrador selecciona el botón “Asignar”. \[FA03\] \[FA04\]

7.  El backend valida que no exista duplicado (misma sede + misma
    especialidad). \[FA05\]

8.  El sistema registra la asignación y muestra: “Especialidad asignada
    a la sede correctamente”.

9.  Fin del caso de uso.

## 2.4 Flujos Alternos

**FA01 – No se encontraron asignaciones**

1.  El sistema no encuentra asignaciones registradas.

<!-- -->

1.  El componente TableServer muestra la tabla vacía con un mensaje
    genérico indicando que no hay registros.

2.  El Administrador puede crear una nueva asignación.

Se continúa en el paso 4 del flujo normal básico.

**FA02 – Eliminar asignación**

1.  En la columna Acciones, el Administrador abre el dropdown y
    selecciona “Eliminar”.

<!-- -->

1.  El sistema muestra un diálogo de confirmación: “¿Confirmar
    eliminación?”

2.  El Administrador confirma la eliminación.

3.  El sistema llama a deleteBranchSpecialty y muestra un toast de éxito
    al eliminar.

4.  La tabla se refresca automáticamente.

Fin del caso de uso.

**FA03 – Validación de campos fallida**

1.  El sistema detecta que faltan campos obligatorios (Sede o
    Especialidad no seleccionados).

<!-- -->

1.  El sistema muestra un mensaje global debajo del formulario: “Debe
    seleccionar una sede.” o “Debe seleccionar una especialidad.” según
    corresponda.

2.  El Administrador corrige los campos.

Se continúa en el paso de guardado del flujo correspondiente.

**FA04 – Cancelar operación**

1.  El Administrador selecciona "Cancelar".

<!-- -->

1.  El sistema descarta los datos y redirige al listado de asignaciones.

Fin del caso de uso.

**FA05 – Asignación duplicada**

1.  El backend detecta que la combinación Sede + Especialidad ya existe
    (validación server-side).

<!-- -->

1.  El frontend muestra el mensaje que devuelve response.message del
    servidor (por ejemplo: “La asignación ya existe.”).

2.  El Administrador selecciona otra combinación.

Se continúa en el paso 7 del flujo normal básico.

2.5 Reglas de Negocio

**RN-CU12-01: Campos de Asignación**

- Sede: Obligatorio. Dropdown con sedes activas. Mensaje: "Debe
  seleccionar una sede."

- Especialidad: Obligatorio. Dropdown con especialidades activas.
  Mensaje: "Debe seleccionar una especialidad."

- Índice único: No puede existir la misma combinación (BranchId,
  SpecialtyId) más de una vez.

## 2.6 Postcondiciones

- La asignación sede-especialidad queda registrada en el sistema.

- Los pacientes verán la especialidad disponible al seleccionar dicha
  sede en el portal de citas \[CU-03\].

- El sistema registra un log de auditoría con la acción realizada.

**3. Firma y Sello**

| **Nombre** | **Puesto** | **Firma y Sello** |
|------------|------------|-------------------|
|            |            |                   |

---

## CU-14: Mantenimiento de Catálogos del Sistema

# CU-14 Mantenimiento de Catálogos

# del Sistema

**Historial Revisiones**

| Nombre           | Fecha      | Descripción del cambio | Versión |
|------------------|------------|------------------------|---------|
| Ing. Edy Ramírez | 12/03/2026 | Planteamiento inicial  | 1.0.0   |
| Ing. Edy Ramírez | 12/03/2026 |                        | 1.0.0   |

# 1. Introducción

## Descripción

El presente documento describe los pasos que el Administrador deberá
seguir para gestionar los catálogos base del sistema: Especialidades,
Sucursales, Estados de Cita, Laboratorios, Exámenes de Laboratorio,
Medicamentos, Roles, Inventario de Medicamentos y Sucursal-Especialidad.
La mayoría de los catálogos siguen el patrón CRUD estándar (Listar,
Crear, Editar, Eliminar), con excepciones documentadas en la tabla 2.3.

## Objetivo

Centralizar la administración de los datos maestros del sistema
hospitalario, garantizando que todos los módulos operativos (citas,
laboratorio, farmacia) cuenten con catálogos actualizados y
consistentes.

# 2. Definición Caso de Uso

## 2.1 Actores

- Usuario Interno (Administrador) — Actor primario

- Sistema informático — Actor secundario

## 2.2 Precondiciones

- El sistema debe estar disponible. \[RN-GLOBAL-003\]

- El usuario debe haber iniciado sesión y su rol debe tener asignadas
  las operaciones granulares correspondientes al catálogo (ej:
  specialty/create, specialty/update, specialty/delete). El sistema
  verifica los permisos por operación específica, no por un permiso
  genérico de administración. \[RN-GLOBAL-007\]

## 2.3 Catálogos Cubiertos

| **Catálogo**               | **Controller**              | **Campos Principales**                                                                                                                                                                                 | **Usado por**              |
|----------------------------|-----------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------|
| Especialidades             | SpecialtyController         | Nombre (200), Descripción (500), Estado                                                                                                                                                                | CU-00, CU-03, CU-12        |
| Sucursales                 | BranchController            | Nombre (100), Teléfono (8), Dirección (500), Descripción (250), Estado                                                                                                                                 | CU-00, CU-03, CU-12, CU-13 |
| Estados de Cita            | AppointmentStatusController | Nombre (50), Descripción (200), Estado                                                                                                                                                                 | CU-03, CU-05               |
| Laboratorios               | LaboratoryController        | Nombre (200), Descripción (500), Estado                                                                                                                                                                | CU-09                      |
| Exámenes Lab               | LabExamController           | Nombre (200), Descripción (500), Precio (10,2), Rango ref., Unidad, LaboratoryId, Estado                                                                                                               | CU-09                      |
| Medicamentos               | MedicineController          | Nombre (200), Descripción (500), Precio (10,2), Unidad, IsControlled, MinimumStock, Estado                                                                                                             | CU-10, CU-13               |
| Roles                      | RolController               | Nombre (200), Descripción (500), Estado                                                                                                                                                                | CU-01                      |
| Inventario de Medicamentos | MedicineInventoryController | MedicineId (FK dropdown), BranchId (FK dropdown), CurrentStock (número), Estado. Incluye alerta de stock bajo (LowStockAlert) y bitácora de movimientos con resumen mensual (MedicineInventorySummary) | CU-10, CU-13               |
| Sucursal-Especialidad      | BranchSpecialtyController   | BranchId (FK dropdown), SpecialtyId (FK dropdown). Solo crear y eliminar (sin edición). Documentado también en CU-12.                                                                                  | CU-03, CU-12               |

## 2.4 Flujo Normal Básico (Genérico)

1.  El Administrador accede al módulo del catálogo deseado desde el menú
    lateral.

2.  El sistema muestra el listado con filtros de búsqueda, paginación y
    la tabla de registros. \[FA01\]

3.  El Administrador selecciona "Crear" para registrar un nuevo
    elemento. \[FA02\]

4.  El sistema muestra el formulario de creación con los campos
    específicos del catálogo (ver tabla 2.3). \[RN-CU15-01\]

5.  El Administrador ingresa los datos requeridos.

6.  El Administrador selecciona "Crear". \[FA03\] \[FA04\]

7.  El sistema valida los campos del formulario. \[FA05\]

8.  El sistema registra el nuevo elemento y muestra: "El registro
    \[nombre\] ha sido creado exitosamente."

9.  Fin del caso de uso.

## 2.5 Flujos Alternos

**FA01 – No se encontraron registros**

1.  El sistema no encuentra registros que coincidan con los filtros.

2.  El sistema muestra la tabla vacía con el mensaje genérico del
    componente TableServer: “No se encontraron resultados.”

El Administrador modifica los filtros o crea un nuevo registro.

**FA02 – Editar registro existente**

1.  El Administrador selecciona "Editar" en el menú de acciones del
    registro.

2.  El sistema muestra el formulario con los valores actuales
    precargados.

3.  El Administrador modifica los campos necesarios.

4.  El Administrador selecciona "Actualizar". \[FA03\] \[FA04\]

5.  El sistema muestra: "El registro \[nombre\] ha sido actualizado
    correctamente."

Fin del caso de uso.

**FA03 – Eliminar registro**

1.  El Administrador selecciona "Eliminar" en el menú de acciones del
    registro.

2.  El sistema muestra confirmación: "¿Está seguro que desea eliminar el
    registro \[nombre\]? Esta acción no se puede deshacer." \[FA04\]

3.  El Administrador confirma la eliminación.

4.  El sistema realiza eliminación lógica (State=0) y muestra: "El
    registro \[nombre\] ha sido eliminado correctamente."

Fin del caso de uso.

**FA04 – Cancelar operación**

1.  El Administrador selecciona "Cancelar" en cualquier formulario o
    diálogo.

2.  El sistema descarta los datos y redirige al listado del catálogo.

Fin del caso de uso.

**FA05 – Validación de campos fallida**

1.  El sistema detecta que uno o más campos no cumplen las reglas de
    negocio (ver RN-CU15-01).

2.  El sistema muestra los mensajes de error específicos y resalta los
    campos en rojo.

3.  El Administrador corrige los campos señalados.

Se continúa en el paso de guardado del flujo correspondiente.

## 2.5 Reglas de Negocio

**RN-CU15-01: Validaciones Comunes por Catálogo**

- Nombre: Obligatorio en todos los catálogos. Longitud máxima según
  catálogo (ver tabla 2.3). Mensaje: “El nombre es obligatorio.”

- Descripción: Obligatoria en Especialidades y Medicamentos, opcional en
  el resto.

- Estado: Obligatorio. Valores: 1=Activo, 0=Inactivo. Default: Activo.

- Nombre único: No pueden existir dos registros activos con el mismo
  nombre en el mismo catálogo. Mensaje: “Ya existe un registro con el
  nombre \[nombre\].” Nota: esta validación se ejecuta del lado del
  servidor (backend); el frontend muestra el mensaje devuelto por la
  API.

**RN-CU15-02: Validaciones Específicas de Medicamentos**

- Precio (DefaultPrice): Obligatorio. Mayor a 0. Precisión 10,2.
  Mensaje: "El precio debe ser mayor a 0."

- Unidad: Obligatorio. Campo de texto libre con etiqueta “Unidad
  (tableta, ml, etc.)”. Máximo 50 caracteres. Mensaje: “La unidad es
  obligatoria.”

- IsControlled: Si es true, el sistema debe aplicar auditoría especial
  en el despacho \[RNF-017\]. Indicador visual obligatorio.

- MinimumStock: Opcional. Si se ingresa, debe ser \>= 0. Usado para
  alertas de stock mínimo \[RN-CU10-03\].

**RN-CU15-03: Validaciones Específicas de Exámenes de Laboratorio**

- Precio (DefaultAmount): Obligatorio. Mayor a 0. Precisión 10,2.
  Mensaje: “El precio base debe ser mayor a 0.”

- Laboratorio (LaboratoryId): Obligatorio. Dropdown con laboratorios
  activos. Mensaje: "Debe seleccionar un laboratorio."

- Rango de referencia y Unidad: Opcionales. Usados para alertas de
  resultados fuera de rango \[CU-09\].

**RN-CU15-04: Validaciones Específicas de Sucursales**

- Teléfono: Opcional. Si se ingresa, debe tener exactamente 8 dígitos
  numéricos. Mensaje: "El teléfono debe tener exactamente 8 dígitos."

- Dirección: Opcional. Máximo 500 caracteres.

## 2.7 Postcondiciones

- Los cambios en el catálogo quedan registrados en el sistema.

- Los módulos que consumen el catálogo reflejan los cambios
  inmediatamente (dropdowns, listados).

- El sistema registra un log de auditoría con la acción realizada.

# 

# 3. Firma y Sello

| **Nombre** | **Puesto** | **Firma y Sello** |
|------------|------------|-------------------|
|            |            |                   |

---

## CU-15: Bitácora de Movimientos de Inventario

# CU-13 Bitácora de Movimientos

# de Inventario

**Historial Revisiones**

| Nombre           | Fecha      | Descripción del cambio | Versión |
|------------------|------------|------------------------|---------|
| Ing. Edy Ramírez | 12/03/2026 | Planteamiento inicial  | 1.0.0   |
| Ing. Edy Ramírez | 12/03/2026 |                        | 1.0.0   |

# 1. Introducción

## Descripción

El presente documento describe los pasos que el personal de farmacia
deberá seguir para registrar y consultar los movimientos de inventario
de medicamentos, incluyendo: Compra (0), Devolución (1), Venta (2),
Reclamo (3), Ajuste+ (4), Ajuste- (5) y Despacho (6, automático). Para
creación manual se excluye Despacho (solo tipos 0–5).

## Objetivo

Garantizar la trazabilidad completa de todos los movimientos de stock en
farmacia, proporcionando una bitácora auditable que complemente el
despacho de medicamentos \[CU-10\] y soporte las alertas de stock
mínimo.

# 2. Definición Caso de Uso

## 2.1 Actores

- Personal de Farmacia — Actor primario

<!-- -->

- Administrador — Actor primario (para ajustes de inventario)

- Sistema informático — Actor secundario

## 2.2 Precondiciones

- El sistema debe estar disponible. \[RN-GLOBAL-003\]

- El usuario debe haber iniciado sesión con permisos de farmacia o
  administración. \[RN-GLOBAL-007\]

- Deben existir medicamentos y sucursales registrados en el sistema
  \[CU-15\].

- Debe existir inventario inicial registrado \[CU-10\].

## 2.3 Flujo Normal Básico

1.  El usuario accede al módulo "Bitácora Inventario" en el menú
    lateral.

<!-- -->

1.  El sistema muestra el listado de movimientos con columnas: Tipo,
    Medicamento, Sucursal, Cantidad, Stock Anterior, Stock Nuevo, Costo,
    Referencia, Usuario, Fecha. Filtros por columna: Tipo de Movimiento,
    Medicamento, Sucursal, Número de Referencia, Usuario y rango de
    fechas. Acciones por fila: Ver (navega a detalle) y
    Desactivar/Activar (toggle de estado). \[FA01\]

2.  El usuario selecciona “Nuevo Movimiento”.

3.  El sistema muestra el formulario con los campos: Medicamento
    (dropdown), Sucursal (dropdown), Tipo de Movimiento (dropdown:
    Compra, Devolución, Venta, Reclamo, Ajuste+, Ajuste-), Cantidad
    (entero positivo), Costo Unitario (Q) (obligatorio solo para Compra,
    \> 0, máx. 2 decimales), Número de Referencia (etiqueta dinámica
    según tipo: Factura/Devolución/Venta/Reclamo), y campo Motivo/Notas
    dinámico según tipo (ver RN-CU13-01). Al seleccionar medicamento +
    sucursal, se muestra un panel informativo de inventario en tiempo
    real: stock actual, stock mínimo, stock proyectado y tipo de
    operación (entrada/salida). \[RN-CU13-01\]

4.  El usuario selecciona el tipo de movimiento e ingresa los datos
    requeridos. \[FA02\]

5.  El usuario selecciona “Registrar Movimiento”. \[FA03\] \[FA04\]

6.  El sistema valida los campos y registra el movimiento.

7.  El sistema actualiza automáticamente el stock en MedicineInventory.
    \[FA05\]

8.  El sistema muestra: "Movimiento registrado exitosamente.
    Medicamento: \[nombre\]. Tipo: \[tipo\]. Cantidad: \[cantidad\].
    Stock actualizado: \[nuevo stock\]."

9.  Fin del caso de uso.

## 2.4 Flujos Alternos

**FA01 – No se encontraron movimientos**

1.  El sistema no encuentra movimientos que coincidan con los filtros.

<!-- -->

1.  El sistema muestra el mensaje genérico del componente TableServer:
    “No se encontraron resultados.”

El usuario modifica los filtros o crea un nuevo movimiento.

**FA02 – Tipo de movimiento es Venta, Reclamo o Ajuste-**

1.  El usuario selecciona tipo “Venta” (2), “Reclamo” (3) o “Ajuste-”
    (5).

<!-- -->

1.  El sistema verifica que el stock actual sea suficiente para la
    cantidad indicada. \[RN-CU13-02\]

2.  Si el stock es insuficiente, el sistema muestra: "Stock
    insuficiente. Stock actual: \[cantidad\]. No se puede registrar una
    salida de \[cantidad solicitada\] unidades."

3.  El usuario corrige la cantidad.

Se continúa en el paso 6 del flujo normal básico.

**FA03 – Validación de campos fallida**

1.  El sistema detecta campos inválidos (ver RN-CU13-01).

<!-- -->

1.  El sistema muestra los mensajes de error y resalta los campos en
    rojo.

2.  El usuario corrige los campos.

Se continúa en el paso 6 del flujo normal básico.

**FA04 – Cancelar operación**

1.  El usuario selecciona "Cancelar".

<!-- -->

1.  El sistema descarta los datos y redirige al listado de movimientos.

Fin del caso de uso.

**FA05 – Alerta preventiva de stock mínimo durante edición del
formulario**

1.  Durante la edición del formulario, el sistema calcula el stock
    proyectado y detecta PREVENTIVAMENTE que caerá por debajo del nivel
    mínimo configurado (antes de guardar). \[RN-CU10-03\]

<!-- -->

1.  El sistema muestra alerta: “\[nombre\]: Stock bajo — disponible:
    \[cantidad\] (mínimo: \[X\])”.

2.  El usuario toma nota para gestionar reabastecimiento.

## 2.5 Reglas de Negocio

**RN-CU13-01: Campos del Movimiento**

- Medicamento: Obligatorio. Mensaje: "Debe seleccionar un medicamento."

- Sucursal: Obligatorio. Mensaje: "Debe seleccionar una sucursal."

- Tipo: Obligatorio. Opciones para creación manual: 0=Compra,
  1=Devolución, 2=Venta, 3=Reclamo, 4=Ajuste+, 5=Ajuste-. El tipo
  6=Despacho es automático (generado por el módulo de despacho de
  medicamentos) y NO se muestra en el formulario de creación. Mensaje:
  “Debe seleccionar el tipo de movimiento.”

- Cantidad: Obligatorio. Entero positivo mayor a 0. Mensaje: “La
  cantidad debe ser un número entero positivo.”

- Costo Unitario (Q): Obligatorio solo para Compra. Debe ser mayor a 0,
  máximo 2 decimales. Mensajes: “El costo unitario es obligatorio para
  compras”, “El costo unitario debe ser mayor a 0”, “El costo unitario
  debe tener máximo 2 decimales”.

- Número de Referencia: Opcional. La etiqueta cambia según el tipo de
  movimiento: “Factura” para Compra, “Devolución” para Devolución,
  “Venta” para Venta, “Reclamo” para Reclamo.

- Motivo/Notas (dinámico según tipo): Ajuste+ y Ajuste-: campo
  “Notas/Justificación” OBLIGATORIO (mínimo 10, máximo 500 caracteres).
  Devolución: campo “Motivo de Devolución” OBLIGATORIO. Reclamo: campo
  “Motivo del Reclamo” OBLIGATORIO. Compra: campo “Notas (opcional)” —
  no obligatorio. Venta: campo NO se muestra. Mensaje: “El motivo debe
  contener entre 10 y 500 caracteres.”

**RN-CU13-02: Validación de Stock Suficiente**

- Para movimientos de tipo Venta (2), Reclamo (3) y Ajuste- (5), la
  cantidad no puede exceder el stock actual.

- El sistema debe usar control de concurrencia optimista (rowVersion)
  para prevenir condiciones de carrera. \[RNF-025\]

**RN-CU13-03: Funcionalidades Adicionales del Frontend**

- Panel informativo de inventario en tiempo real: al seleccionar
  medicamento + sucursal en el formulario, se muestra un panel con stock
  actual, stock mínimo, stock proyectado y tipo de operación
  (entrada/salida).

- Acciones en tabla del listado: Ver (navega a detalle del movimiento) y
  Desactivar/Activar (toggle de estado del registro).

- Columnas de la tabla: Tipo, Medicamento, Sucursal, Cantidad, Stock
  Anterior, Stock Nuevo, Costo, Referencia, Usuario, Fecha.

- Bitácora embebida en MedicineInventoryPage con resumen mensual de
  movimientos.

- El tipo de referencia se asigna automáticamente según el tipo de
  movimiento seleccionado.

## 2.6 Postcondiciones

- El movimiento queda registrado en la bitácora con fecha, hora y
  usuario que lo registró.

- El stock del medicamento en la sucursal correspondiente queda
  actualizado.

- Si aplica, se genera alerta de stock mínimo.

# 3. Firma y Sello

| **Nombre** | **Puesto** | **Firma y Sello** |
|------------|------------|-------------------|
|            |            |                   |

---

## CU-16: Gestión de Agenda Médica

# CU-14 Gestión de

# Agenda Médica

**Historial Revisiones**

| Nombre           | Fecha      | Descripción del cambio | Versión |
|------------------|------------|------------------------|---------|
| Ing. Edy Ramírez | 12/03/2026 | Planteamiento inicial  | 1.0.0   |
| Ing. Edy Ramírez | 12/03/2026 |                        | 1.0.0   |

# 1. Introducción

## Descripción

El presente documento describe los pasos que el Médico deberá seguir
para gestionar su agenda personal, incluyendo la visualización de citas
programadas, creación de eventos personales y gestión de tareas o
recordatorios mediante el panel lateral de tareas (TaskPanel).

## Objetivo

Permitir al médico administrar su agenda de forma autónoma, incluyendo
eventos personales, visualización de citas y gestión de tareas,
garantizando que el sistema de citas \[CU-03\] y seguimientos \[CU-11\]
respete los horarios configurados.

# 2. Definición Caso de Uso

## 2.1 Actores

- Médico

<!-- -->

- Sistema HIS

## 2.2 Precondiciones

- El Médico ha iniciado sesión en el sistema con rol Médico.

- Existe al menos una sede y especialidad configurada \[CU-12\].

- El módulo de Agenda Médica está habilitado para el rol del usuario.

## 2.3 Flujo Normal Básico

1.  El Médico accede a la sección “Agenda Médica” desde el menú
    principal.

<!-- -->

1.  El sistema muestra el calendario mensual/semanal/diario con las
    citas programadas (coloreadas por estado, excluyendo Pendiente, No
    Asistió y Cancelada) y eventos existentes en violeta. \[FA01\]
    \[FA05\] \[FA06\]

2.  El Médico presiona el botón “Nuevo Evento” en la cabecera del
    calendario.

3.  El sistema muestra el formulario de evento con los campos: Título,
    Descripción, Fecha de inicio, Fecha de fin, Tipo de evento, Todo el
    día (checkbox). \[RN-CU14-01\]

4.  El Médico ingresa los datos del evento (ej. “Capacitación en
    congreso médico”).

5.  El sistema valida los datos ingresados. \[RN-CU14-01\] \[FA03\]

6.  El sistema guarda el evento en la base de datos.

7.  El sistema muestra: “Evento creado exitosamente. \[Título\] —
    \[Fecha inicio dd/mm/yyyy\] a \[Fecha fin dd/mm/yyyy\].” (formato
    toLocaleDateString(“es-GT”))

8.  El calendario se actualiza mostrando el nuevo evento en color
    violeta (#8b5cf6).

9.  El Médico presiona el botón toggle para mostrar el panel lateral de
    tareas (TaskPanel) a la derecha del calendario.

## 2.4 Flujos Alternos

**FA01 — Visualizar detalle de evento o cita**

1.  El Médico hace clic en un evento (violeta) o una cita (coloreada por
    estado) en el calendario.

<!-- -->

1.  El sistema muestra un popover con los detalles: Título, Descripción,
    Fecha inicio/fin, Tipo. Para citas, muestra además paciente y
    estado.

2.  El Médico puede editar o eliminar el evento. El flujo regresa al
    paso 2 del FB.

**FA03 — Error de validación**

1.  El sistema detecta datos inválidos (campos obligatorios vacíos,
    fechas inconsistentes).

<!-- -->

1.  El sistema resalta los campos con error y muestra mensajes
    específicos. \[RN-CU14-01\]

2.  El Médico corrige los datos y el flujo regresa al paso 6 del FB.

**FA04 — Eliminar evento**

1.  El Médico selecciona un evento existente y presiona “Eliminar”.

<!-- -->

1.  El sistema solicita confirmación: “¿Está seguro de eliminar este
    evento?”

2.  El Médico confirma. El sistema elimina el evento y actualiza el
    calendario.

**FA05 — Filtrar por vista del calendario**

1.  El Médico presiona los botones “Mes”, “Semana” o “Día” en la
    cabecera del calendario.

<!-- -->

1.  El sistema cambia la vista del calendario al rango seleccionado,
    manteniendo los eventos y citas visibles.

2.  El Médico puede navegar entre periodos con los botones de flecha
    (anterior/siguiente) o volver a “Hoy”.

3.  El flujo regresa al paso 2 del FB.

**FA06 — Gestionar tareas desde el panel lateral**

1.  El Médico presiona el botón toggle para mostrar el panel lateral de
    tareas (TaskPanel).

<!-- -->

1.  El sistema muestra el panel colapsable con las tareas del día
    seleccionado, conteo de tareas, y filtros: Pendientes, Completadas,
    Todas.

2.  Cada tarea muestra: Título, Descripción, Prioridad (con ícono de
    color), Fecha límite, Estado (Pendiente/Completada).

3.  El Médico puede crear una nueva tarea con el botón “Nueva Tarea”,
    completar/descompletar tareas, o eliminarlas.

4.  El flujo regresa al paso 10 del FB.

**FA07 — Editar evento existente**

1.  El Médico hace clic en un evento existente y selecciona “Editar”
    desde el popover.

<!-- -->

1.  El sistema muestra el formulario de evento prellenado con los datos
    actuales.

2.  El Médico modifica los campos deseados y presiona “Guardar”.

3.  El sistema valida, actualiza el evento y muestra: “Evento
    actualizado exitosamente.”

4.  El calendario se refresca mostrando los cambios. El flujo regresa al
    paso 2 del FB.

## 2.5 Reglas de Negocio

**RN-CU14-01: Validación de datos de evento**

- Fecha de inicio: Obligatoria.

- Fecha de fin: Obligatoria. Debe ser posterior a la fecha de inicio.
  Mensaje: “La fecha de fin debe ser posterior a la fecha de inicio.”

- Tipo de evento: Obligatorio. Opciones: Reunión (0), Descanso (1),
  Capacitación (2), Personal (3), Otro (4).

- Descripción: Opcional. Máximo 500 caracteres.

- Todo el día: Opcional (checkbox). Cuando está activo, los campos de
  fecha cambian de datetime-local a date (solo fecha, sin hora).

- Color: No configurable por el usuario. Todos los eventos se muestran
  en violeta (#8b5cf6).

**RN-CU14-02: Validación de datos de tarea**

- Prioridad: Obligatorio. Opciones: Baja (0), Normal (1), Alta (2).

- Fecha límite: Obligatoria.

- Estado: Booleano isCompleted. Pendiente (isCompleted = false, por
  defecto) o Completada (isCompleted = true).

- Descripción: Opcional. Máximo 1000 caracteres.

## 2.6 Postcondiciones

- El evento queda registrado en la agenda del médico.

- Los eventos registrados son visibles en el calendario del médico junto
  con las citas programadas.

- Las tareas creadas quedan asociadas al médico y visibles en su panel
  de tareas.

# 

# 3. Firma y Sello

| **Nombre** | **Puesto** | **Firma y Sello** |
|------------|------------|-------------------|
|            |            |                   |

---

## Reglas de Negocio Consolidadas

**Historial Revisiones**

| Nombre           | Fecha      | Descripción del cambio | Versión |
|------------------|------------|------------------------|---------|
| Ing. Edy Ramírez | 12/03/2026 | Planteamiento inicial  | 1.0.0   |
| Ing. Edy Ramírez | 12/03/2026 |                        | 1.0.0   |

# 

# 1. Reglas de Negocio Globales

**RN-GLOBAL-001: Validación de DPI**

- Es obligatorio. Mensaje: "El campo DPI es obligatorio. Por favor,
  ingrese su número de DPI."

- Exactamente 13 caracteres. Mensaje: "El DPI debe contener exactamente
  13 dígitos. Usted ingresó \[X\] dígitos."

- Numérico (solo dígitos). Mensaje: "El DPI debe contener únicamente
  números. No se permiten letras ni caracteres especiales."

- Aplica a: CU-00, CU-02, CU-05, CU-06, CU-07, CU-09, CU-10, CU-16

**RN-GLOBAL-002: Validación de NIT**

- Obligatorio. Mensaje: "El campo NIT es obligatorio."

- Entre 8 y 9 caracteres. Mensaje: "El NIT debe contener entre 8 y 9
  caracteres. Usted ingresó \[X\] caracteres."

- Alfanumérico. Mensaje: "El NIT debe contener únicamente caracteres
  alfanuméricos."

- Aplica a: CU-01, CU-02

**RN-GLOBAL-003: Disponibilidad del Sistema**

- El sistema debe estar disponible como precondición para todos los
  casos de uso.

- Si no disponible: "El sistema se encuentra en mantenimiento. Por
  favor, intente más tarde. Disculpe las molestias."

- Si módulo específico no disponible: "El módulo \[nombre\] no está
  disponible temporalmente. Los demás servicios continúan operando."

- Aplica a: Todos (CU-00 a CU-16)

**RN-GLOBAL-004: Métodos de Pago Aceptados**

- Efectivo en moneda local (Quetzales - GTQ).

- Tarjeta de crédito: Visa, Mastercard.

- Tarjeta de débito.

- Para pagos en línea (CU-04) solo tarjetas.

- Método no aceptado: "El método de pago seleccionado no está
  disponible. Los métodos aceptados son: efectivo (Quetzales), tarjeta
  de crédito (Visa/Mastercard) o tarjeta de débito."

- Aplica a: CU-04, CU-06, CU-09, CU-10, CU-16

**RN-GLOBAL-005: Contenido de Comprobantes de Pago**

Todo comprobante debe contener:

- Número de transacción único.

- Nombre completo del paciente.

- Monto pagado y forma de pago.

- Fecha y hora de la transacción.

- Detalle del servicio (cita, exámenes o medicamentos).

- Nombre de la sucursal.

- Aplica a: CU-04, CU-06, CU-09, CU-10, CU-16

**RN-GLOBAL-006: Contenido de Notificaciones por Correo**

Toda notificación por correo debe incluir:

- Datos identificativos del paciente y detalle del servicio.

- Pie de correo: "Este es un correo automático del Sistema Informático
  Hospitalario. No responda a este mensaje. Para consultas, comuníquese
  al teléfono \[número\]."

- Si falla el envío: "Error al enviar notificación por correo
  electrónico al paciente \[Nombre\]. Se reintentará automáticamente."

- Aplica a: CU-00, CU-02, CU-04, CU-11

**RN-GLOBAL-007: Autenticación de Usuarios Internos**

- Debe iniciar sesión con credenciales válidas.

- Sesión expirada: "Su sesión ha expirado por inactividad. Por favor,
  inicie sesión nuevamente."

- Credenciales incorrectas: "Las credenciales ingresadas son
  incorrectas. Tiene \[X\] intentos restantes antes del bloqueo
  temporal."

- Cuenta bloqueada: "Su cuenta ha sido bloqueada temporalmente por
  múltiples intentos fallidos. Contacte al administrador del sistema."

- Aplica a: CU-01, CU-05, CU-06, CU-07, CU-08, CU-09, CU-10, CU-11,
  CU-16

# 2. Reglas de Negocio por Caso de Uso

## CU-00: Visualización del Portal Web

**RN-CU00-01: Validación de Cliente Registrado**

- Si existe: "Bienvenido(a), \[Nombre del paciente\]. Será redirigido al
  formulario de agendamiento de cita."

- Si no existe: "No se encontró un registro asociado a este DPI. Será
  redirigido al formulario de registro."

**RN-CU00-02: Contenido de Correo de Confirmación de Cita**

- Incluye: nombre paciente, especialidad, médico, fecha, hora, sucursal.

- Asunto: "Confirmación de Cita Médica - Hospital \[Nombre\]"

- Mensaje en pantalla: "Su cita ha sido agendada exitosamente. Se ha
  enviado una confirmación al correo \[correo\]."

**RN-CU00-03: Bloqueo por Intentos Fallidos de Inicio de Sesión**

- Máximo 5 intentos fallidos de inicio de sesión consecutivos.

- Al alcanzar el límite, bloqueo temporal de 15 minutos.

- Mensaje: “Cuenta bloqueada temporalmente. Intente de nuevo en 15
  minutos.”

- Los campos de usuario, contraseña y botón de inicio de sesión se
  deshabilitan durante el período de bloqueo.

## CU-01: Mantenimiento de Usuarios

**RN-CU01-01: Filtros de Búsqueda**

- Selector de campo (Usuario, Nombre, NIT, Rol, Sucursal).

- Campo de texto: máx 25 caracteres. Mensaje: "El campo de búsqueda no
  puede exceder los 25 caracteres."

- Sin resultados: "No se encontraron resultados para los criterios de
  búsqueda ingresados. Por favor, modifique los filtros e intente
  nuevamente."

**RN-CU01-02: Campos de Resultado**

- Columnas: Usuario (ordenable), Nombre (ordenable), Rol, NIT, Estado,
  Sucursal.

- Paginación: 20 registros/página.

**RN-CU01-03: Catálogo de Roles**

- Obligatorio. Mensaje: "Debe seleccionar un rol para el usuario."

- Opciones: Médico, Enfermero, Recepcionista, Cajero, Laboratorista,
  Farmacéutico, Administrador.

**RN-CU01-04: Nombre**

- Obligatorio. Mensaje: "El campo Nombre es obligatorio."

- 10-100 caracteres. Mensaje: "El nombre debe contener entre 10 y 100
  caracteres. Usted ingresó \[X\] caracteres."

**RN-CU01-05: Credenciales**

- Obligatorio. Mensaje: "El campo Usuario es obligatorio."

- 8-9 caracteres. Mensajes: "El usuario no puede exceder los 9
  caracteres." / "El usuario debe contener al menos 8 caracteres."

- Alfanumérico. Mensaje: "El usuario debe contener únicamente caracteres
  alfanuméricos."

- Único. Mensaje: "El nombre de usuario \[usuario\] ya se encuentra
  registrado. Por favor, elija otro."

**RN-CU01-06: Sucursal**

- Obligatorio. Mensaje: "Debe seleccionar una sucursal para el usuario."

**RN-CU01-07: Documento de Identificación**

- Opcional.

- Si se ingresa: exactamente 13 dígitos numéricos (aplica validación DPI
  según RN-GLOBAL-001).

**RN-CU01-08: Número de Teléfono**

- Opcional.

- Si se ingresa: exactamente 8 dígitos numéricos.

- Mensaje: “El teléfono debe contener exactamente 8 dígitos.”

**RN-CU01-09: Rol del Usuario**

- Obligatorio en formulario de creación.

- Dropdown con catálogo de roles activos del sistema (ver RN-CU01-03).

- Mensaje: “Debe seleccionar un rol para el usuario.”

**RN-CU01-10: Estado del Usuario**

- Obligatorio. Valores: Activo (1), Inactivo (0).

- Default en creación: Activo.

- Mensaje: “Debe seleccionar un estado para el usuario.”

**RN-CU01-11: NIT**

- Opcional.

- Si se ingresa: aplica validación según RN-GLOBAL-002 (8-9 caracteres
  alfanuméricos).

**RN-CU01-12: Número de Seguro**

- Opcional.

- Si se ingresa: entre 5 y 50 caracteres.

- Mensaje: “El número de seguro debe contener entre 5 y 50 caracteres.”

**RN-CU01-13: Sucursal del Usuario**

- Opcional en edición, obligatorio en creación (ver RN-CU01-06).

- Dropdown con catálogo de sucursales activas.

**RN-CU01-14: Especialidad**

- Opcional. Visible únicamente cuando el rol seleccionado es Médico.

- Dropdown con catálogo de especialidades activas.

- Mensaje: “Debe seleccionar una especialidad para el médico.”

## CU-02: Registro de Usuarios Externos

**RN-CU02-01: Nombre Completo**

- Obligatorio.

- 10-100 caracteres.

- Mensaje: "El nombre debe contener entre 10 y 100 caracteres. Usted
  ingresó \[X\] caracteres."

**RN-CU02-02: Teléfono**

- Obligatorio.

- 8 dígitos.

- Mensaje: "El número de teléfono debe contener exactamente 8 dígitos
  numéricos."

**RN-CU02-03: Seguro Médico**

- Opcional.

- 5-50 caracteres si se ingresa.

**RN-CU02-04: Correo Electrónico**

- Obligatorio.

- Formato email válido.

- Mensaje: "El formato del correo electrónico no es válido. Ejemplo:
  usuario@dominio.com"

**RN-CU02-05: Nombre de Usuario**

- Obligatorio.

- Entre 8 y 9 caracteres alfanuméricos.

- Mensaje mínimo: “El usuario debe contener al menos 8 caracteres.”

- Mensaje máximo: “El usuario no puede exceder los 9 caracteres.”

- Único en el sistema. Mensaje: “El nombre de usuario ya se encuentra
  registrado.”

**RN-CU02-06: Contraseña**

- Obligatorio.

- Mínimo 12 caracteres.

- Mensaje: “La contraseña debe contener al menos 12 caracteres.”

## CU-03: Agendar Citas

**RN-CU03-01: Especialidad**

- Obligatorio.

- Mensaje: "Debe seleccionar una especialidad médica para continuar."

**RN-CU03-02: Sucursal**

- Obligatorio.

- Mensaje: "Debe seleccionar una sucursal para continuar."

**RN-CU03-03: Motivo de Visita**

- Obligatorio.

- 10-2000 caracteres.

- Mensaje: "El motivo debe contener entre 10 y 2000 caracteres. Usted
  ingresó \[X\] caracteres."

**RN-CU03-04: Documentos (Opcional)**

- Formato: PDF.

- No puede estar vacío.

- No debe estar encriptado.

- Máximo 2 MB.

- Mensaje si inválido: "El documento debe ser un archivo PDF válido, no
  encriptado y con tamaño máximo de 2 MB."

**RN-CU03-05: Fecha y Hora**

- Obligatorio.

- Debe ser fecha futura.

- Mensaje: "Debe seleccionar una fecha y hora futuras. Las citas no
  pueden agendarse en fechas pasadas o presentes."

## CU-04: Pago en Línea con Tarjeta

**RN-CU04-01: Número de Tarjeta**

- Obligatorio.

- 13-19 dígitos.

- Validación Luhn.

- Mensaje: "El número de tarjeta debe contener entre 13 y 19 dígitos y
  ser válido."

**RN-CU04-02: Nombre Titular**

- Obligatorio.

- 5-100 caracteres alfabéticos.

- Sin caracteres especiales.

- Mensaje: "El nombre del titular debe contener entre 5 y 100 caracteres
  alfabéticos sin especiales."

**RN-CU04-03: Fecha Vencimiento**

- Obligatorio.

- Formato MM/AA.

- Tarjeta no vencida.

- Mensaje: "La fecha de vencimiento debe estar en formato MM/AA y la
  tarjeta no debe estar vencida."

**RN-CU04-04: CVV**

- Obligatorio.

- 3-4 dígitos.

- Mensaje: "El CVV debe contener 3 ó 4 dígitos numéricos."

**RN-CU04-05: Contenido del Comprobante**

- Número de transacción único.

- Monto pagado.

- Fecha y hora de la transacción.

- Detalle de la cita.

- Asunto correo: "Comprobante de Pago - Cita Médica - Hospital
  \[Nombre\]"

**RN-CU04-06: Mensajes de Rechazo de Pasarela**

- Fondos insuficientes: "Su tarjeta fue rechazada por fondos
  insuficientes. Verifique su saldo e intente nuevamente."

- Tarjeta inválida: "Su tarjeta fue rechazada. El número de tarjeta es
  inválido. Verifique los datos e intente nuevamente."

- Tarjeta vencida: "Su tarjeta fue rechazada. La tarjeta está vencida.
  Utilice otra tarjeta de crédito o débito."

- Error comunicación: "Error al procesar el pago. Por favor, intente
  nuevamente o contacte a su banco."

- Sesión expirada: "Su sesión de pago ha expirado. Por favor, inicie el
  proceso de pago nuevamente."

## CU-05: Recepción y Verificación de Cita

**RN-CU05-01: Búsqueda de Cita**

- Búsqueda por número de cita o DPI del paciente.

- Al menos un campo obligatorio.

- Mensaje: "Debe ingresar un número de cita o DPI para buscar."

- Sin resultados: "No se encontró una cita asociada a los parámetros
  ingresados. Verifique los datos e intente nuevamente."

**RN-CU05-02: Estados de Cita**

- Pagada (verde).

- Pendiente de pago (amarillo).

- Cancelada (rojo).

- Mensaje para cada estado según disponibilidad de acciones.

## CU-06: Cobro de Consulta en Caja

**RN-CU06-01: Búsqueda para Cobro**

- Búsqueda por número de cita o DPI.

- Solo muestra citas con estado "Pendiente de pago".

- Sin resultados: "No hay citas pendientes de pago bajo los parámetros
  indicados."

**RN-CU06-02: Formas de Pago**

- Efectivo.

- Tarjeta de crédito.

- Tarjeta de débito.

- Cambio: "Monto recibido: Q\[monto\]. Cambio a devolver: Q\[cambio\]."

**RN-CU06-03: Comprobante**

- Número de transacción único.

- Nombre completo del paciente.

- Monto pagado.

- Forma de pago.

- Fecha y hora de la transacción.

- Detalle de la cita.

- Nombre de la sucursal.

## CU-07: Toma de Signos Vitales

**RN-CU07-01: Presión Arterial**

- Obligatorio.

- Formato: sistólica/diastólica.

- Rango de captura: 60-250/40-150 mmHg.

- Mensaje: "La presión arterial debe ingresarse en formato
  sistólica/diastólica (ej: 120/80) dentro de rangos válidos."

**RN-CU07-02: Temperatura**

- Obligatorio.

- 1 decimal.

- Rango: 34.0-42.0°C.

- Mensaje: "La temperatura debe estar entre 34.0 y 42.0°C con un
  decimal."

**RN-CU07-03: Peso**

- Obligatorio.

- 2 decimales.

- Rango: 0.5-300 kg.

- Mensaje: "El peso debe estar entre 0.5 y 300 kg con dos decimales."

**RN-CU07-04: Talla**

- Obligatorio.

- 2 decimales.

- Rango: 30-250 cm.

- Mensaje: "La talla debe estar entre 30 y 250 cm con dos decimales."

**RN-CU07-05: Frecuencia Cardíaca**

- Obligatorio.

- Entero.

- Rango: 30-220 lpm.

- Mensaje: "La frecuencia cardíaca debe estar entre 30 y 220 latidos por
  minuto."

**RN-CU07-06: Rangos Clínicos para Alertas**

- PA fuera 90/60-140/90: alerta "Presión arterial fuera de rango
  normal."

- Temperatura fuera 36.0-37.5: alerta "Temperatura fuera de rango
  normal."

- FC fuera 60-100: alerta "Frecuencia cardíaca fuera de rango normal."

- El sistema debe alertar visualmente pero permitir continuar con
  registro.

## CU-08: Consulta Médica

**RN-CU08-01: Diagnóstico**

- Obligatorio para cerrar la consulta.

- 10-5000 caracteres.

- CIE-10 opcional.

- Mensaje: "El diagnóstico es obligatorio. Debe contener entre 10 y 5000
  caracteres."

**RN-CU08-02: Registro de Consulta**

- Motivo de consulta (obligatorio).

- Hallazgos clínicos (obligatorio).

- Diagnóstico (obligatorio para cierre).

- Plan de tratamiento (obligatorio).

- Mensaje si incompleto: "Debe completar todos los campos obligatorios
  para cerrar la consulta."

**RN-CU08-03: Receta Médica**

- Medicamento (obligatorio).

- Dosis (obligatorio).

- Frecuencia (obligatorio).

- Duración (obligatorio).

- Indicaciones especiales (opcional).

- Mensaje: "Todos los campos de la receta son obligatorios excepto
  indicaciones especiales."

## CU-09: Gestión de Laboratorio

**RN-CU09-01: Cobro**

- Debe realizarse antes de la toma de muestras.

- Métodos: Efectivo, tarjeta de crédito, tarjeta de débito.

- Comprobante requerido.

- Mensaje: "El pago debe estar completado antes de proceder con la toma
  de muestras."

**RN-CU09-02: Resultados**

- Todos los exámenes solicitados deben tener resultados.

- Campos: nombre examen, valor, unidad de medida, rango de referencia.

- Alerta si está fuera de rango.

- Validación de supervisor antes de publicación.

- Mensaje: "Los resultados están fuera del rango de referencia normal.
  Requiere revisión."

## CU-10: Despacho de Medicamentos

**RN-CU10-01: Verificación de Receta**

- Medicamento debe existir en catálogo.

- Dosis coherente con medicamento.

- Receta no debe exceder 7 días de antigüedad.

- Mensaje: "La receta es inválida. Verifique que el medicamento exista,
  la dosis sea correcta y la receta no sea anterior a 7 días."

**RN-CU10-02: Cobro Integrado en Farmacia**

- El cobro se realiza directamente en el mostrador de farmacia al
  momento del despacho (ver RN-GLOBAL-004 para métodos aceptados).

- El comprobante incluye detalle de medicamentos despachados,
  cantidades, precios y total (ver RN-GLOBAL-005).

- Mensaje de éxito: "Despacho registrado exitosamente. \[X\]
  medicamento(s) despachado(s). Total: Q\[monto\]."

**RN-CU10-03: Stock Mínimo**

- Nivel mínimo configurable por medicamento.

- Alerta automática al alcanzar nivel mínimo.

- Mensaje: "El stock del medicamento \[nombre\] ha alcanzado el nivel
  mínimo. Se requiere reorden."

## CU-11: Agendamiento de Cita de Seguimiento

**RN-CU11-01: Tipo Seguimiento**

- Obligatorio.

- Opciones: Monitoreo de condición / Revisión de resultados.

- Mensaje: "Debe seleccionar el tipo de seguimiento."

**RN-CU11-02: Fecha y Hora**

- Obligatorio.

- Debe ser fecha futura.

- Debe coincidir con horarios disponibles del médico.

- Mensaje: "Seleccione una fecha futura dentro de los horarios
  disponibles del médico."

**RN-CU11-03: Observaciones**

- Obligatorio.

- 10-2000 caracteres.

- Mensaje: "Las observaciones son obligatorias. Deben contener entre 10
  y 2000 caracteres."

**RN-CU11-04: Contenido de Notificación**

- Asunto: "Cita de Seguimiento Agendada - Hospital \[Nombre\]"

- Cuerpo incluye: fecha, hora, tipo de seguimiento, médico, sucursal,
  observaciones.

- Pie de correo estándar (ver RN-GLOBAL-006).

**RN-CU11-05: Contenido de Recordatorio**

- Asunto: "Recordatorio: Su Cita de Seguimiento Mañana"

- Se envía 1-2 días antes de la cita.

- Cuerpo incluye: fecha, hora, tipo seguimiento, médico, sucursal.

- No se envía si la cita fue cancelada.

- Pie de correo estándar (ver RN-GLOBAL-006).

## CU-12: Configuración de Sedes y Especialidades

**RN-CU12-01: Campos de Asignación Sede-Especialidad**

- Sede: Obligatorio. Dropdown con sedes activas. Mensaje: “Debe
  seleccionar una sede.”

- Especialidad: Obligatorio. Dropdown con especialidades activas.
  Mensaje: “Debe seleccionar una especialidad.”

- Índice único: no puede existir la misma combinación (Sede,
  Especialidad) más de una vez.

- Mensaje duplicado: “Esta combinación de sede y especialidad ya existe
  en el sistema.”

## CU-13: Bitácora de Movimientos de Inventario

**RN-CU13-01: Campos del Movimiento de Inventario**

- Medicamento: Obligatorio. Mensaje: “Debe seleccionar un medicamento.”

- Sucursal: Obligatorio. Mensaje: “Debe seleccionar una sucursal.”

- Tipo de Movimiento: Obligatorio. Opciones: Entrada por compra, Salida
  por ajuste, Transferencia entre sucursales, Ajuste por inventario
  físico.

- Cantidad: Obligatorio. Entero positivo mayor a 0.

- Motivo: Obligatorio. Entre 10 y 1000 caracteres.

**RN-CU13-02: Validación de Stock Suficiente**

- Para movimientos de tipo Salida o Transferencia, la cantidad no puede
  exceder el stock actual.

- Se utiliza control de concurrencia optimista (RowVersion) para
  prevenir condiciones de carrera.

- Mensaje: “Stock insuficiente. El stock actual es \[X\] unidades.”

## CU-14: Gestión de Agenda Médica

**RN-CU14-01: Campos del Evento de Agenda**

- Título: Obligatorio. Entre 5 y 200 caracteres.

- Fecha de inicio: Obligatorio. Debe ser fecha futura o actual.

- Fecha de fin: Obligatorio. Debe ser posterior a la fecha de inicio.

- Tipo de evento: Obligatorio. Opciones: Bloqueo de disponibilidad,
  Evento personal, Capacitación, Vacaciones.

- Descripción: Opcional. Máximo 2000 caracteres.

- Color: Opcional. Código hexadecimal válido.

**RN-CU14-02: Campos de Tarea**

- Título: Obligatorio. Entre 5 y 200 caracteres.

- Prioridad: Obligatorio. Opciones: Alta, Media, Baja.

- Fecha límite: Opcional. Si se ingresa, debe ser fecha futura.

- Estado: Pendiente (default), En progreso, Completada.

## CU-15: Mantenimiento de Catálogos del Sistema

**RN-CU15-01: Validaciones Comunes por Catálogo**

- Nombre: Obligatorio en todos los catálogos. Longitud máxima según
  catálogo.

- Descripción: Obligatoria en Especialidades y Medicamentos, opcional en
  el resto.

- Estado: Obligatorio. Valores: 1=Activo, 0=Inactivo. Default: Activo.

- Nombre único: no pueden existir dos registros activos con el mismo
  nombre en el mismo catálogo.

- Mensaje: “Ya existe un registro con el nombre \[nombre\] en este
  catálogo.”

# 3. Requerimientos No Funcionales Consolidados

## 3.1 Rendimiento

- RNF-001: El correo de confirmación de cita debe enviarse en un máximo
  de 5 minutos (CU-00, CU-02, CU-04, CU-11).

- RNF-002: El sistema de búsqueda de citas debe retornar resultados en
  máximo 3 segundos (CU-05).

- RNF-003: El expediente del paciente debe cargar en menos de 2 segundos
  (CU-08).

- RNF-004: El catálogo CIE-10 debe soportar autocompletado con respuesta
  menor a 500 ms (CU-08).

- RNF-005: El portal web debe soportar al menos 100 usuarios
  concurrentes sin degradación (CU-00).

- RNF-006: El tiempo de carga de la página principal no debe exceder 3
  segundos (CU-00).

- RNF-007: La notificación al médico de resultados de laboratorio debe
  enviarse en máximo 2 minutos (CU-09).

- RNF-008: Los signos vitales deben sincronizarse con el expediente en
  máximo 2 segundos (CU-07).

- RNF-009: El dispositivo POS debe responder en máximo 15 segundos por
  transacción (CU-06).

## 3.2 Seguridad

- RNF-010: La comunicación con la pasarela de pago debe usar HTTPS con
  TLS 1.2 o superior (CU-04).

- RNF-011: El sistema no debe almacenar datos sensibles de tarjeta -
  cumplimiento PCI DSS (CU-04).

- RNF-012: El número de tarjeta debe mostrarse enmascarado (solo últimos
  4 dígitos visibles) (CU-04).

- RNF-013: El código CVV debe estar enmascarado visualmente (asteriscos)
  (CU-04).

- RNF-014: Los datos sensibles del paciente (DPI, NIT) deben almacenarse
  encriptados en reposo (CU-02).

- RNF-015: Las contraseñas temporales deben cumplir estándares de
  seguridad (mín 12 caracteres, combinación) (CU-01).

- RNF-016: Se debe implementar idempotency key para evitar cobros
  duplicados (CU-04).

- RNF-017: Los medicamentos controlados deben tener flujo de
  autorización adicional con auditoría (CU-10).

## 3.3 Disponibilidad y Resiliencia

- RNF-018: La sesión de pago en línea debe expirar después de 10 minutos
  de inactividad (CU-04).

- RNF-019: El temporizador de reserva temporal de cita debe ser
  configurable (default 5 min) (CU-03).

- RNF-020: El scheduler de recordatorios debe ser resiliente a reinicios
  del sistema (CU-11).

- RNF-021: La pantalla de recepción debe actualizarse automáticamente
  cuando cambie un estado de cita (CU-05).

- RNF-022: El calendario de disponibilidad debe actualizar horarios en
  tiempo real (CU-03).

## 3.4 Integridad de Datos

- RNF-023: Las operaciones CRUD de usuarios deben registrar log de
  auditoría inmutable (CU-01).

- RNF-024: Los resultados de laboratorio publicados deben ser inmutables
  sin autorización de supervisor (CU-09).

- RNF-025: El inventario de farmacia debe usar control de concurrencia
  optimista (CU-10).

- RNF-026: El historial de consultas debe mantener versionamiento para
  auditoría (CU-08).

- RNF-027: La unicidad de DPI y correo debe validarse a nivel de base de
  datos con índices únicos (CU-02).

- RNF-028: Los documentos adjuntos deben pasar validación antivirus
  antes de almacenarse (CU-03).

## 3.5 Escalabilidad y Futuras Integraciones

- RNF-029: El sistema debe soportar integración con dispositivos de
  medición IoT (CU-07).

- RNF-030: El sistema debe soportar integración con analizadores de
  laboratorio LIS (CU-09).

- RNF-031: El sistema debe implementar firma digital del médico para
  documentos clínicos (CU-08).

- RNF-032: Se debe implementar cola de mensajes para envío asíncrono de
  notificaciones (CU-11).

- RNF-033: Los comprobantes de pago deben poder reimprimirse sin límite
  (CU-06).

# 4. Firma y Sello

| **Nombre** | **Puesto** | **Firma y Sello** |
|------------|------------|-------------------|
|            |            |                   |

---