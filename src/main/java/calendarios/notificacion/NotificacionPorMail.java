package calendarios.notificacion;

import calendarios.Usuario;
import calendarios.evento.EventoUnico;
import calendarios.servicios.ShemailLib;


public class NotificacionPorMail implements Notificacion {
  @Override
  public void notificar(EventoUnico evento, ShemailLib shemailLib) {
    for (Usuario invitado : evento.getInvitados()) {
      shemailLib.enviarMailA(
          invitado.getEmail(),
          "Recordatorio: " + evento.getNombre(),
          "Tu evento comienza a las " + evento.getInicio()
      );
    }
  }
}