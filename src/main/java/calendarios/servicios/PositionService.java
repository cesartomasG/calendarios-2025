package calendarios.servicios;

import calendarios.Ubicacion;

/**
 * Obtiene la {@code Ubicacion} actual de un usuario a partir de su email
 * (que es el identificador en el sistema externo).
 */
public interface PositionService {
  Ubicacion ubicacionActual(String email);
}
