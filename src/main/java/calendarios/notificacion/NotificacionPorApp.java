package calendarios.notificacion;

import calendarios.evento.EventoUnico;
import calendarios.servicios.ShemailLib;

public class NotificacionPorApp implements Notificacion {
  @Override
  public void notificar(EventoUnico evento, ShemailLib shemailLib) {
    // Simulado, no hace nada.
    System.out.println("Notificación en la app: " + evento.getNombre());
  }
}