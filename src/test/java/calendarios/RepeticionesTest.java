package calendarios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import calendarios.evento.EventoRecurrente;
import calendarios.evento.EventoUnico;
import calendarios.repeticion.RepeticionAnual;
import calendarios.repeticion.RepeticionCadaCiertasSemanas;
import calendarios.repeticion.RepeticionCadaCiertosDias;
import calendarios.repeticion.RepeticionCadaCiertosMeses;
import calendarios.repeticion.RepeticionConfig;
import calendarios.repeticion.RepeticionDiasFijos;
import org.junit.jupiter.api.Test;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class RepeticionesTest extends BaseTest {

  // 7. Permitir agendar eventos con repeticiones, con una frecuencia diaria, semanal, mensual o anual

  //repiticion cada N dias-----------------

  @Test
  void repeticionCadaNDiasGeneraInstanciasCorrectas() {
    EventoUnico base = new EventoUnico("Clase", LocalDateTime.of(2025, 6, 1, 10, 0),
        LocalDateTime.of(2025, 6, 1, 11, 0), new Ubicacion(0, 0), List.of());
    RepeticionConfig repeticion = new RepeticionCadaCiertosDias(2);
    EventoRecurrente evento = new EventoRecurrente(base, repeticion, LocalDateTime.of(2025, 6, 10, 0, 0));

    List<EventoUnico> instancias = evento.instanciasEntre(
        LocalDateTime.of(2025, 6, 1, 0, 0),
        LocalDateTime.of(2025, 6, 10, 0, 0));

    assertEquals(5, instancias.size());
    assertEquals(LocalDateTime.of(2025, 6, 1, 10, 0), instancias.get(0).getInicio());
    assertEquals(LocalDateTime.of(2025, 6, 9, 10, 0), instancias.get(4).getInicio());
  }

  @Test
  void proximaADevuelveCorrectoCadaNDias() {
    EventoUnico base = new EventoUnico("Clase", LocalDateTime.of(2025, 6, 1, 10, 0),
        LocalDateTime.of(2025, 6, 1, 11, 0), new Ubicacion(0, 0), List.of());
    RepeticionConfig config = new RepeticionCadaCiertosDias(3);
    EventoRecurrente evento = new EventoRecurrente(base, config, LocalDateTime.of(2025, 6, 20, 0, 0));

    Optional<LocalDateTime> proxima = config.proximaA(LocalDateTime.of(2025, 6, 5, 0, 0), evento);
    assertTrue(proxima.isPresent());
    assertEquals(LocalDateTime.of(2025, 6, 7, 10, 0), proxima.get());
  }

  // Repeticion cada dias fijos ------------
  @Test
  void repeticionDiasFijosGeneraInstanciasCorrectas() {
    EventoUnico clase = crearEventoSimpleEnMedrano("Clase DDS",
        LocalDateTime.of(2025, 6, 6, 19, 0), // Viernes
        Duration.ofHours(3)
    );

    RepeticionConfig config = new RepeticionDiasFijos(Set.of(DayOfWeek.FRIDAY));
    EventoRecurrente claseViernes = new EventoRecurrente(clase, config,
        LocalDateTime.of(2025, 6, 28, 0, 0)); // incluye viernes 27

    List<EventoUnico> instancias = config.instanciasEntre(claseViernes,
        LocalDateTime.of(2025, 6, 1, 0, 0),
        LocalDateTime.of(2025, 6, 30, 0, 0));

    assertEquals(4, instancias.size()); // 6, 13, 20, 27
  }

  @Test
  void proximaRepeticionDiasFijosFunciona() {
    EventoUnico clase = crearEventoSimpleEnMedrano("Clase DDS",
        LocalDateTime.of(2025, 6, 6, 19, 0), // Viernes
        Duration.ofHours(3)
    );

    RepeticionConfig config = new RepeticionDiasFijos(Set.of(DayOfWeek.FRIDAY));
    EventoRecurrente claseViernes = new EventoRecurrente(clase, config, LocalDateTime.of(2025, 6, 28, 0, 0));

    //despues de cierta clase
    Optional<LocalDateTime> proxima = config.proximaA(LocalDateTime.of(2025, 6, 20, 22, 0), claseViernes);

    assertTrue(proxima.isPresent());
    assertEquals(LocalDateTime.of(2025, 6, 27, 19, 0), proxima.get()); // próximo viernes
  }


  @Test
  void repeticionSemanalGeneraInstanciasCorrectas() {
    EventoUnico base = new EventoUnico("Clase semanal",
        LocalDateTime.of(2025, 6, 1, 10, 0),
        LocalDateTime.of(2025, 6, 1, 11, 0), new Ubicacion(0, 0), List.of());

    RepeticionConfig config = new RepeticionCadaCiertasSemanas(1);

    EventoRecurrente evento = new EventoRecurrente(base, config,
        LocalDateTime.of(2025, 6, 30, 0, 0)); // fin de mes

    List<EventoUnico> instancias = config.instanciasEntre(evento,
        LocalDateTime.of(2025, 6, 1, 0, 0),
        LocalDateTime.of(2025, 6, 30, 0, 0));

    assertEquals(5, instancias.size());
  }


  @Test
  void proximaRepeticionSemanalFunciona() {
    EventoUnico base = new EventoUnico("Reunión semanal", LocalDateTime.of(2025, 6, 1, 10, 0),
        LocalDateTime.of(2025, 6, 1, 11, 0), new Ubicacion(0, 0), List.of());
    RepeticionConfig config = new RepeticionCadaCiertasSemanas(2);
    EventoRecurrente evento = new EventoRecurrente(base, config, LocalDateTime.of(2025, 7, 30, 0, 0));

    Optional<LocalDateTime> proxima = config.proximaA(LocalDateTime.of(2025, 6, 10, 0, 0), evento);

    assertTrue(proxima.isPresent());
    assertEquals(LocalDateTime.of(2025, 6, 15, 10, 0), proxima.get());
  }


  // Repeitcion cada N meses ---------------

  @Test
  void repeticionMensualGeneraInstanciasCorrectas() {
    EventoUnico base = crearEventoSimple("Pago mensual", LocalDateTime.of(2025, 1, 1, 10, 0),
        LocalDateTime.of(2025, 1, 1, 11, 0), new Ubicacion(0, 0), List.of());

    RepeticionConfig config = new RepeticionCadaCiertosMeses(2);
    EventoRecurrente evento = new EventoRecurrente(base, config, LocalDateTime.of(2025, 9, 1, 23, 59));

    List<EventoUnico> instancias = config.instanciasEntre(evento,
        LocalDateTime.of(2025, 1, 1, 0, 0),
        LocalDateTime.of(2025, 10, 1, 0, 0));

    assertEquals(5, instancias.size()); // meses: Ene, Mar, May, Jul, Sep
  }

  @Test
  void proximaRepeticionMensualFunciona() {
    EventoUnico base = new EventoUnico("Pago mensual", LocalDateTime.of(2025, 1, 1, 10, 0),
        LocalDateTime.of(2025, 1, 1, 11, 0), new Ubicacion(0, 0), List.of());
    RepeticionConfig config = new RepeticionCadaCiertosMeses(3);
    EventoRecurrente evento = new EventoRecurrente(base, config, LocalDateTime.of(2025, 12, 31, 0, 0));

    Optional<LocalDateTime> proxima = config.proximaA(LocalDateTime.of(2025, 7, 1, 0, 0), evento);

    assertTrue(proxima.isPresent());
    assertEquals(LocalDateTime.of(2025, 7, 1, 10, 0), proxima.get());
  }


  // Repeticion Anual ----------------------

  @Test
  void repeticionAnualGeneraInstanciasCorrectas() {
    EventoUnico base = new EventoUnico("Aniversario", LocalDateTime.of(2020, 1, 1, 10, 0),
        LocalDateTime.of(2020, 1, 1, 11, 0), new Ubicacion(0, 0), List.of());
    RepeticionConfig config = new RepeticionAnual();
    EventoRecurrente evento = new EventoRecurrente(base, config, LocalDateTime.of(2025, 1, 1, 0, 0));

    List<EventoUnico> instancias = config.instanciasEntre(evento,
        LocalDateTime.of(2020, 1, 1, 0, 0),
        LocalDateTime.of(2025, 1, 1, 0, 0));

    assertEquals(5, instancias.size());
  }

  @Test
  void proximaRepeticionAnualFunciona() {
    EventoUnico base = new EventoUnico("Aniversario", LocalDateTime.of(2022, 6, 10, 10, 0),
        LocalDateTime.of(2022, 6, 10, 11, 0), new Ubicacion(0, 0), List.of());
    RepeticionConfig config = new RepeticionAnual();
    EventoRecurrente evento = new EventoRecurrente(base, config, LocalDateTime.of(2025, 6, 10, 0, 0));

    Optional<LocalDateTime> proxima = config.proximaA(LocalDateTime.of(2023, 1, 1, 0, 0), evento);

    assertTrue(proxima.isPresent());
    assertEquals(LocalDateTime.of(2023, 6, 10, 10, 0), proxima.get());
  }


}
