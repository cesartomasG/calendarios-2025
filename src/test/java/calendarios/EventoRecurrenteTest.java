package calendarios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import calendarios.evento.EventoRecurrente;
import calendarios.evento.EventoUnico;
import calendarios.repeticion.RepeticionCadaCiertosDias;
import calendarios.repeticion.RepeticionConfig;
import calendarios.repeticion.RepeticionDiasFijos;
import org.junit.jupiter.api.Test;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

public class EventoRecurrenteTest extends BaseTest {

  @Test
  void sePuedenAgendarYListarEventosRecurrentes() {
    Usuario rene = crearUsuario("rene@gugle.com.ar");
    Calendario calendario = crearCalendarioVacio();
    rene.agregarCalendario(calendario);

    EventoUnico claseBase = crearEventoSimpleEnMedrano("Clase DDS",
        LocalDateTime.of(2025, 9, 5, 19, 0), // viernes
        Duration.ofMinutes(45));

    RepeticionConfig config = new RepeticionDiasFijos(Set.of(DayOfWeek.FRIDAY));
    EventoRecurrente claseDDS = new EventoRecurrente(claseBase, config,
        LocalDateTime.of(2025, 9, 26, 23, 59)); // repite hasta el 26

    // Generar las instancias manualmente y agendarlas en el calendario
    List<EventoUnico> instancias = claseDDS.instanciasEntre(
        LocalDateTime.of(2025, 9, 1, 0, 0),
        LocalDateTime.of(2025, 9, 30, 0, 0));

    instancias.forEach(calendario::agendar);

    // Ahora se consultan desde el usuario
    List<EventoUnico> eventosListados = rene.eventosEntreFechas(
        LocalDateTime.of(2025, 9, 1, 0, 0),
        LocalDateTime.of(2025, 9, 30, 0, 0));

    assertEquals(4, eventosListados.size());
    assertTrue(eventosListados.containsAll(instancias));
  }


  @Test
  void eventoRecurrenteGeneraInstanciasCorrectasEntreDosFechas() {
    EventoUnico claseBase = crearEventoSimpleEnMedrano("Clase DDS", LocalDateTime.of(2020, 9, 15, 19, 0), Duration.ofMinutes(45));
    EventoRecurrente claseDDS = new EventoRecurrente(
        claseBase,
        new RepeticionDiasFijos(Set.of(DayOfWeek.TUESDAY)),
        LocalDateTime.of(2025, 9, 29, 0, 0)
    );
    List<EventoUnico> instancias = claseDDS.instanciasEntre(
        LocalDateTime.of(2025, 9, 14, 9, 0),
        LocalDateTime.of(2025, 9, 28, 21, 0)
    );

    assertEquals(2, instancias.size());
  }

  @Test
  void unEventoRecurrenteSabeCuantoFaltaParaSuProximaRepeticion() {
    EventoUnico clasePresencial = crearEventoSimpleEnCampus("Clase de DDS PRESENCIAL", LocalDateTime.now().minusHours(1), Duration.ofHours(3));
    RepeticionConfig repeticionPorQuincena = new RepeticionCadaCiertosDias(15);

    //crear un evento recurrente que se repita, a partir de hoy, cada 15 días, y arranque una hora antes de la hora actual
    EventoRecurrente eventoRecurrente = new EventoRecurrente(clasePresencial, repeticionPorQuincena,LocalDateTime.now().plusYears(1));

    assertTrue(eventoRecurrente.cuantoFalta().compareTo(Duration.of(15, ChronoUnit.DAYS)) <= 0);
    assertTrue(eventoRecurrente.cuantoFalta().compareTo(Duration.of(14, ChronoUnit.DAYS)) >= 0);
  }
}
