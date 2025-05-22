# SIGAM_ProyectoFinal_DiploJava16_MilnerFlores
Sistema de Gestión Académica para Maestros (SIGAM) - Diplomado 16 Java - Milner Flores

Dentro de este repositorio, se encuentra el código fuente para la aplicación SIGAM.
Esta aplicación fue desarrollada con [Spring Boot 3.3.9] y [JDK 17].

Es un proyecto creado con el propósito de auxiliar a los docentes con su administración académica personal a través de una aplicación web fácil de utilizar y que presente resultados inmediatos.

A través de su módulo de seguridad, SIGAM garantiza que la información de cada usuario permanezca accesible y visible sólo para el propietario de ésta. Esto, combinado con sus funcionalidades, permiten crear un entorno personalizado que se adapte a las necesidades de seguimiento académico para cada docente.

## Tecnologías utilizadas
- Spring boot 3.3.9
- Spring security 6
- Thymeleaf
- OpenPDF 1.3.30
- Lombok (Ver *nota* dentro de requisitos del sistema)
- Spring Data JPA
- Driver MariaDB
- JJWT 0.11.5
- SLF4j 2.0.9

## Requisitos del sistema y compilación

- Oracle JDK - 17 o superior
- MariaDB - 10.6.7 o superior
- Maven - 3.9.0 o superior
- Lombok - *nota. Si se utiliza el IDE IntelliJ IDEA, es necesario habilitar manualmente el procesamiento de anotaciones.*

### Clonación del repositorio

Para obtener una copia local del proyecto, sigue estos pasos:

1. Asegúrate de tener [Git](https://git-scm.com/) instalado en tu sistema.
2. Abre una terminal y ejecuta: 

git clone <https://github.com/SoyMilner/SIGAM_ProyectoFinal_DiploJava16_MilnerFlores.git>

Dentro del archivo: src/main/resources/application.properties, asegúrate de configurar los siguientes parámetros según tus configuraciones.

*Establece el puerto local donde desplegarás el sistema. (Asegúrate de utilizar alguno disponible)*

server.port=8080

*La URL utiliza el nombre de la base de datos establecida por defecto dentro del script crear_la_BD.sql*

spring.datasource.url=jdbc:mariadb://localhost:3306/sigam_database?useSSL=false&serverTimezone=UTC

spring.datasource.username=[Tu usuario]

spring.datasource.password=[Tu Contraseña]

spring.datasource.driver-class-name=org.mariadb.jdbc.Driver

### Creación de la BD

Para crear la Base de Datos y llenarla con información útil para realizar pruebas, ejecuta los siguientes scripts dentro de tu entorno mariadb
src/main/resources/schema_db/crear_la_bd.sql
src/main/resources/schema_db/data_final.sql

Una vez que el proyecto ha sido configurado y la Base de Datos está disponible, 
#### ¡Puedes utilizar SIGAM!

## Manual de utilización

1. **Iniciar Sesión**

Cuando accedas a la aplicación, observarás una pantalla de login que sólo te permitirá acceder si eres un usuario registrado dentro de la base de datos.

La codificación de las contraseñas utiliza BCrypt con fuerza de 11.

El sistema permitirá autenticar con algún correo electrónico dentro del sistema y su respectiva contraseña.

Por el momento, los usuarios principales con los que podrás acceder a SIGAM es:

User: milner.flores@email.com | Password: password

User: irma.perez@email.com | Password: password

User: dalina.lopez@email.com | Password: password

2. **Barra de Navegación**

La barra lateral de navegación te dejará acceder a la página principal, a la página de gestión de trabajos, a todos tus grupos y a una página de perfil.

3. **Grupos**

- Dentro de la pantalla principal, observarás tu lista de grupos. 
- Agregar un nuevo grupo:

Aquí podrás crear nuevos grupos utilizando el botón con el símbolo de +. 

Esto te presentará un formulario con los datos correspondientes al grupo.

Dentro de los campos a llenar, puedes observar un catálogo de asignaturas que servirán para clasificar a tus grupos. Si no encuentras tu asignatura, eres libre de crear tu propia asignatura.
- También podrás editar o eliminar grupos según tus necesidades.

- Información de los grupos:

Al hacer clic sobre un grupo, podrás acceder a información relevante del grupo: Gestión de estudiantes pertenecientes al grupo y trabajos asignados al grupo.

3. **Gestión de estudiantes**

Si te encuentras dentro de la pestaña de Gestión de estudiantes podrás crear, modificar y eliminar estudiantes.

Si deseas ver más información sobre un estudiante en particular, puedes hacer clic al icono que se encuentra a la izquierda de cada estudiante.

Dentro de la información del estudiante, tendrás acceso a todos sus trabajos, junto con sus calificaciones y a un *historial de calificaciones* que se comentará más adelante.

4. **Gestión de trabajos**

Antes de pasar a los trabajos asignados, es necesario saber cómo asignar un trabajo. Para esto, utilizaremos la barra de navegación y abriremos la página "Gestión de trabajos".

Podrás crear, editar y eliminar un trabajo de forma similar a la creación de los Grupos y Alumnos.

Aquí, al igual que con el catálogo de asignaturas existente para los grupos, tendrás un catálogo para seleccionar el tipo de trabajo que dejarás: Puede ser un examen, una tarea, un proyecto o lo que se adapte a tus necesidades (Por ejemplo, una práctica de campo). Sólo debes añadirla de la misma forma que se con las asignaturas.

Es importante mencionar que el catálogo de grupos sólo te permitirá asignar un trabajo a tus grupos existentes. Desde este formulario no podrás crear un grupo. Una vez que hagas un nuevo grupo, será mostrado dentro de tu catálogo.

La pestaña de gestión de trabajos también te permitirá filtrar tus trabajos por si necesitas modificar o eliminar alguno para un grupo en específico.

5. **Trabajos Asignados**

Regresando a la información de un grupo en específico, podrás acceder a la pestaña de la derecha "Ver Trabajos".

Dentro de esta pestaña, podrás observar la lista de todos los trabajos que generaste desde la gestión de trabajos y fueron asignados a este grupo.

Aquí, podrás calificar dicho trabajo haciendo clic en el botón de la izquierda mediante una práctica lista que muestra a todos los estudiantes a los que fue asignado ese grupo. Podrás poner la fecha en la cual entregaron el trabajo y algún comentario que te sirva para llevar un seguimiento.

Abajo a la derecha, se observa un botón similar al que se utiliza para agregar un nuevo grupo, trabajo o estudiante, sin embargo, del mismo color verde que el del botón para calificar.

6. **Generar Reportes**

Ya que asignaste una calificación para todos tus trabajos y todos tus estudiantes, podrás generar un reporte para ver cómo va el grupo hasta ahora, al finalizar un periodo, semestre o si simplemente necesitas probar cómo le irá a tu grupo si utilizas cierta ponderación para sus calificaciones.

Dentro del formulario del reporte, tendrás que asignar un porcentaje para cada tipo de trabajo que fuiste dejando a lo largo del periodo y, si quieres prescindir de alguno, simplemente debes dejarlo con valor de 0.

Es importante que la suma de éstos sea 100%.

Una vez que quieras generar el reporte, puedes proceder y observar un PDF descargable que contendrá todas las calificaciones de tus estudiantes, ordenadas por el tipo de trabajo y podrás ver cuál es el porcentaje que asignaste para cada categoría.

7. **Historial Reportes**

Regresando al apartado de la información del estudiante, podrás ahora observar su *historial de calificaciones* que fue llenado a través del reporte que generaste y podrás ver qué calificación habría obtenido cada alumno con esa ponderación.

Si lo sientes necesario, también podrás añadir un comentario para este registro particular y llevar un seguimiento más cercano y accesible para ti, con tus estudiantes.

8. **Cerrar la sesión**

Dentro de la barra de navegación, tendrás un botón para acceder a tu perfil y aquí podrás cerrar sesión.

## Contenido Adicional

- La aplicación utiliza spring security y un token jwt para asegurar que en todo momento, el maestro sólo tenga acceso a su información y nadie más pueda acceder o visualizar información que no le pertenece.

## Derechos de Autor / Licencia

Proyecto académico desarrollado bajo la supervisión de la Universidad Nacional Autónoma de México y la Dirección General de Cómputo y de Tecnologías de Información y Comunicación como una propuesta de solución para la administración académica personal. Todos los derechos están reservados a su autor: Milner Ushuaía Flores Pérez y la Universidad. No se permite su uso para fines comerciales sin previa autorización.