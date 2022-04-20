# Inblue

Inblue es una aplicación movil que permite visualizar el aforo de un establacimiento (colegio, restaurante... etc)
tiene dos modos, uno para usuario y el otro para administrador, esta permite conectarse a un módulo bluethoot (HC-05, HC-06) por
el cual dependiendo el modo escogido tendrá diferentes opciones, en el modo usuario, este podrá conectarse y al momento de pasar
su tarjeta RFID por un circuito ubicado en la entrada del lugar, en la app se podrán ver los datos personales de la persona,
adicionalmente el sistema cuenta con un sensor de temperatura donde en la aplicación se mostrará su temperatura actual y si su temperatura
es normal, el sistema le otorga el permiso de entrada.

En el modo administrador, se realizan las operaciones de administrador, agregar un nuevo usuario, eliminar un usuario y por último permite
generar un excel con el nombre de usuario, la temperatura tomada, la hora de llegada y salida de cada persona que entre al lugar. 

La base de datos utilizada fue Firebase otorgada por google, cuenta con una gran cantidad de servicios en la nube, fácil autenticación 
y control de datos en tiempo real.
