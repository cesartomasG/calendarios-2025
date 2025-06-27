package calendarios;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import calendarios.evento.EventoUnico;
import calendarios.notificacion.NotificacionPorApp;
import calendarios.notificacion.NotificacionPorMail;
import calendarios.servicios.ShemailLib;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class RecordatoriosTest extends BaseTest {
  @Test
  void recordatorioSeEnviaCuandoFaltaElTiempoExacto() {
    ShemailLib mail = mock(ShemailLib.class);
    EventoUnico evento = new EventoUnico("Clase",
        LocalDateTime.now().plusMinutes(10),
        LocalDateTime.now().plusMinutes(70),
        new Ubicacion(0, 0),
        List.of(new Usuario("test@mail.com")));

    Recordatorio r = new Recordatorio(Duration.ofMinutes(10), new NotificacionPorMail());
    evento.agregarRecordatorio(r);

    // debería enviar
    evento.verificarRecordatorios(mail);
    // verificar que se haya invocado el envío
    verify(mail).enviarMailA(eq("test@mail.com"), contains("Clase"), anyString());
  }
  // 9. Permitir asignarle a un evento varios recordatorios, que se enviarán cuando falte un cierto tiempo
  @Test
  void seEnviaCadaRecordatorioCuandoCorresponde() {
    // Arrange
    ShemailLib shemail = mock(ShemailLib.class);
    Usuario invitado = crearUsuario("invitado@gugle.com.ar");

    // Evento en 30 minutos desde ahora
    EventoUnico evento = crearEventoSimple(
        "Charla técnica",
        LocalDateTime.now().plusMinutes(30),
        LocalDateTime.now().plusMinutes(90),
        utnMedrano,
        List.of(invitado)
    );

    // Recordatorio 1: 30 minutos antes (debe activarse)
    Recordatorio r1 = new Recordatorio(Duration.ofMinutes(30), new NotificacionPorMail());

    // Recordatorio 2: 10 minutos antes (no debe activarse todavía)
    Recordatorio r2 = new Recordatorio(Duration.ofMinutes(10), new NotificacionPorApp());

    evento.agregarRecordatorio(r1);
    evento.agregarRecordatorio(r2);

    // Act
    evento.verificarRecordatorios(shemail);

    // Assert
    // Solo el primer recordatorio debería haberse enviado
    verify(shemail).enviarMailA(eq("invitado@gugle.com.ar"), contains("Charla técnica"), anyString());
    verifyNoMoreInteractions(shemail); // solo uno
  }
}
