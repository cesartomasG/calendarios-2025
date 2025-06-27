package calendarios;

import calendarios.evento.EventoUnico;
import calendarios.notificacion.Notificacion;
import calendarios.servicios.ShemailLib;
import java.time.Duration;

public class Recordatorio {
  private final Duration anticipacion;
  private final Notificacion notificacion;

  public Recordatorio(Duration anticipacion, Notificacion notificacion) {
    this.anticipacion = anticipacion;
    this.notificacion = notificacion;
  }

  public boolean verificarEnvio(EventoUnico evento, ShemailLib shemailLib) {
    Duration cuantoFalta = evento.cuantoFalta();
    if (cuantoFalta.minus(anticipacion).abs().compareTo(Duration.ofMinutes(1)) < 0) {
      notificacion.notificar(evento, shemailLib);
      return true;
    }
    return false;
  }
}