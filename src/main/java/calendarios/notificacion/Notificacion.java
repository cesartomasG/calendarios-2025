package calendarios.notificacion;

import calendarios.evento.EventoUnico;
import calendarios.servicios.ShemailLib;

public interface Notificacion {
  void notificar(EventoUnico evento, ShemailLib shemailLib);
}
