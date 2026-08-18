# Reproductor EIA

## Lenguajes y compiladores
## Juan Felipe Atehortua - Thomas Gonzales

## Modos de reproducción

- **Aleatorio:** Lista Ligada Circular Doble.
  Las canciones se mezclan aleatoriamente y se almacenan en una lista ligada circular doble. 
  Cada nodo tiene referencias al siguiente y al anterior, permitiendo avanzar y retroceder 
  por las canciones de forma circular, sin llegar a un final.

- **Orden de llegada:** Cola Simple (FIFO).
  Las canciones se almacenan en una cola siguiendo el principio FIFO (First In, First Out).
  La primera canción agregada es la primera en reproducirse y, al avanzar, se retira de la cola.
  Este modo no permite retroceder.
  
- **Alfabético:** Árbol Binario de Búsqueda (BST).
  Las canciones se almacenan en un árbol binario de búsqueda ordenado por su nombre.
  Mediante un recorrido inorden se obtienen las canciones en orden alfabético, mientras
  que el recorrido inverso permite retroceder por ellas.

  ## Ejecución

Abrir el proyecto en **IntelliJ IDEA** y ejecutar:

```text
src/interfaz/VentanaPrincipal.java
