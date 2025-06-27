package calendarios;

import static java.time.temporal.ChronoUnit.HOURS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import calendarios.evento.EventoUnico;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class EventoUnicoTest extends BaseTest {

  @Test
  void unEventoPuedeTenerMultiplesInvitados() {
    Usuario juan = crearUsuario("juan@gugle.com.ar");
    Usuario ana = crearUsuario("ana@gugle.com.ar");

    EventoUnico evento = crearEventoSimple("Reunión", LocalDateTime.now(), LocalDateTime.now().plusHours(1), utnMedrano, List.of(juan, ana));

    assertEquals(2, evento.getInvitados().size());
    assertTrue(evento.getInvitados().contains(juan));
    assertTrue(evento.getInvitados().contains(ana));
  }

  // 5. Permitir saber cuánto falta para un cierto calendarios.evento (por ejemplo, 15 horas)

  @Test
  void unEventoSabeCuantoFalta() {
    LocalDateTime inicio = LocalDateTime.now().plusDays(60);
    EventoUnico parcialDds = crearEventoSimpleEnMedrano("Parcial DDS", inicio, Duration.of(2,  HOURS));

    assertTrue(parcialDds.cuantoFalta().compareTo(Duration.of(60, ChronoUnit.DAYS)) <= 0);
    assertTrue(parcialDds.cuantoFalta().compareTo(Duration.of(59, ChronoUnit.DAYS)) >= 0);
  }

  // 6. Permitir saber si dos eventos están solapado, y en tal caso, con qué otros eventos del calendario

  @Test
  void sePuedeSaberSiUnEventoEstaSolapadoCuandoEstaParcialmenteIncluido() {
    //esto es opcional pero probablemente ayuda a implementar el requerimiento principal
    EventoUnico recuperatorioSistemasDeGestion = crearEventoSimpleEnMedrano("Recuperatorio Sistemas de Gestion", LocalDateTime.of(2025, 6, 19, 9, 0), Duration.of(2, HOURS));
    EventoUnico tpOperativos = crearEventoSimpleEnMedrano("Entrega de Operativos", LocalDateTime.of(2025, 6, 19, 10, 0), Duration.of(2, HOURS));

    assertTrue(recuperatorioSistemasDeGestion.estaSolapadoCon(tpOperativos));
    assertTrue(tpOperativos.estaSolapadoCon(recuperatorioSistemasDeGestion));
  }

  @Test
  void sePuedeSaberSiUnEventoEstaSolapadoCuandoEstaTotalmenteIncluido() {
    // esto es opcional pero probablemente ayuda a implementar el requerimiento principal
    EventoUnico recuperatorioSistemasDeGestion = crearEventoSimpleEnMedrano("Recuperatorio Sistemas de Gestion", LocalDateTime.of(2025, 6, 19, 9, 0), Duration.of(4, HOURS));
    EventoUnico tpOperativos = crearEventoSimpleEnMedrano("Entrega de Operativos", LocalDateTime.of(2025, 6, 19, 10, 0), Duration.of(2, HOURS));

    assertTrue(recuperatorioSistemasDeGestion.estaSolapadoCon(tpOperativos));
    assertTrue(tpOperativos.estaSolapadoCon(recuperatorioSistemasDeGestion));
  }

  @Test
  void sePuedeSaberSiUnEventoEstaSolapadoCuandoNoEstaSolapado() {
    // esto es opcional pero probablemente ayuda a implementar el requerimiento principal
    EventoUnico recuperatorioSistemasDeGestion = crearEventoSimpleEnMedrano("Recuperatorio Sistemas de Gestion", LocalDateTime.of(2025, 6, 19, 9, 0), Duration.of(3, HOURS));
    EventoUnico tpOperativos = crearEventoSimpleEnMedrano("Entrega de Operativos", LocalDateTime.of(2025, 6, 19, 18, 0), Duration.of(2, HOURS));

    assertFalse(recuperatorioSistemasDeGestion.estaSolapadoCon(tpOperativos));
    assertFalse(tpOperativos.estaSolapadoCon(recuperatorioSistemasDeGestion));
  }
}
