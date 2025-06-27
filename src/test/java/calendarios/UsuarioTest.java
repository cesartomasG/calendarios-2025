package calendarios;

import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.MINUTES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import calendarios.evento.EventoUnico;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UsuarioTest extends BaseTest {

  // 1. Permitir que une usuario tenga muchos calendarios
  @Test
  void unUsuarioTieneMuchosCalendarios() {
    Usuario rene = crearUsuario("rene@gugle.com.ar");
    Calendario calendario = crearCalendarioVacio();

    rene.agregarCalendario(calendario);

    assertTrue(rene.tieneCalendario(calendario));
  }

  @Test
  void sePuedeListarUnEventoEntreDosFechasParaUnUsuario() {
    Usuario rene = crearUsuario("rene@gugle.com.ar");
    Calendario calendario = new Calendario();
    rene.agregarCalendario(calendario);

    EventoUnico tpRedes = crearEventoSimpleEnMedrano("TP de Redes", LocalDateTime.of(2020, 4, 3, 16, 0), Duration.of(2,  HOURS));

    calendario.agendar(tpRedes);

    List<EventoUnico> eventos = rene.eventosEntreFechas(
        LocalDate.of(2020, 4, 1).atStartOfDay(),
        LocalDate.of(2020, 4, 4).atStartOfDay());

    assertEquals(eventos, Collections.singletonList(tpRedes));
  }

  @Test
  void noSeListaUnEventoSiNoEstaEntreLasFechasIndicadasParaUneUsuario() {
    Usuario dani = crearUsuario("dani@gugle.com.ar");
    Calendario calendario = new Calendario();
    dani.agregarCalendario(calendario);

    EventoUnico tpRedes = crearEventoSimpleEnMedrano("TP de Redes", LocalDateTime.of(2020, 4, 3, 16, 0), Duration.of(1, HOURS));

    calendario.agendar(tpRedes);

    List<EventoUnico> eventos = dani.eventosEntreFechas(
        LocalDate.of(2020, 5, 8).atStartOfDay(),
        LocalDate.of(2020, 5, 16).atStartOfDay());

    assertTrue(eventos.isEmpty());
  }

  @Test
  void sePuedenListarMultiplesEventoEntreDosFechasParaUneUsuarioConCoincidenciaParcial() {
    Usuario usuario = crearUsuario("rene@gugle.com.ar");
    Calendario calendario = new Calendario();
    usuario.agregarCalendario(calendario);

    EventoUnico tpRedes = crearEventoSimpleEnMedrano("TP de Redes", LocalDateTime.of(2020, 4, 3, 16, 0), Duration.of(2,  HOURS));
    EventoUnico tpDeGestion = crearEventoSimpleEnMedrano("TP de Gestión", LocalDateTime.of(2020, 4, 5, 18, 30), Duration.of(2,  HOURS));
    EventoUnico tpDeDds = crearEventoSimpleEnMedrano("TP de DDS", LocalDateTime.of(2020, 4, 12, 16, 0), Duration.of(2,  HOURS));

    calendario.agendar(tpRedes);
    calendario.agendar(tpDeGestion);
    calendario.agendar(tpDeDds);

    List<EventoUnico> eventos = usuario.eventosEntreFechas(
        LocalDate.of(2020, 4, 1).atStartOfDay(),
        LocalDate.of(2020, 4, 6).atStartOfDay());

    assertEquals(eventos, Arrays.asList(tpRedes, tpDeGestion));
  }

  @Test
  void sePuedenListarMultiplesEventoEntreDosFechasParaUneUsuarioConCoincidenciaTotal() {
    Usuario juli = crearUsuario("juli@gugle.com.ar");
    Calendario calendario = new Calendario();
    juli.agregarCalendario(calendario);

    EventoUnico tpRedes = crearEventoSimpleEnMedrano("TP de Redes", LocalDateTime.of(2020, 4, 3, 16, 0), Duration.of(2, HOURS));
    EventoUnico tpDeGestion = crearEventoSimpleEnMedrano("TP de Gestión", LocalDateTime.of(2020, 4, 5, 18, 30), Duration.of(30, MINUTES));
    EventoUnico tpDeDds = crearEventoSimpleEnMedrano("TP de DDS", LocalDateTime.of(2020, 4, 12, 16, 0), Duration.of(1, HOURS));

    calendario.agendar(tpRedes);
    calendario.agendar(tpDeGestion);
    calendario.agendar(tpDeDds);

    List<EventoUnico> eventos = juli.eventosEntreFechas(
        LocalDate.of(2020, 4, 1).atStartOfDay(),
        LocalDateTime.of(2020, 4, 12, 21, 0));

    assertEquals(eventos, Arrays.asList(tpRedes, tpDeGestion, tpDeDds));
  }

  @Test
  void sePuedenListarEventosDeMultiplesCalendarios() {
    Usuario juli = crearUsuario("juli@gugle.com.ar");

    Calendario calendarioFacultad = new Calendario();
    juli.agregarCalendario(calendarioFacultad);

    Calendario calendarioLaboral = new Calendario();
    juli.agregarCalendario(calendarioLaboral);

    EventoUnico examenFinal= crearEventoSimpleEnCampus("examenFinalFacultad",
        LocalDateTime.of(2025, 7, 20, 7, 0),
        Duration.of(2, HOURS));

    EventoUnico eventoTrabajo = crearEventoSimple("turnoTrabajoLaboral",
        LocalDateTime.of(2025, 7, 20, 9, 0),
        LocalDateTime.of(2025, 7, 20, 5 , 0),
        new Ubicacion(-34.69077741886949, -58.419839232655995),
        List.of(juli));

    calendarioLaboral.agendar(eventoTrabajo);

    calendarioFacultad.agendar(examenFinal);

    List<EventoUnico> eventosJuli = juli.eventosEntreFechas(LocalDateTime.of(2025, 7, 19, 23, 59),
        LocalDateTime.of(2025, 7, 21, 0, 0));

    int cantidadEventos = eventosJuli.size();

    assertEquals(2, cantidadEventos);
    assertTrue(eventosJuli.contains(examenFinal));
    assertTrue(eventosJuli.contains(eventoTrabajo));

  }

  @Test
  void llegaEnTiempoAlProximoEventoCuandoNoHayEventos() {
    Usuario feli = crearUsuario("feli@gugle.com.ar");
    assertTrue(feli.llegaEnTiempoAlProximoEvento(gugleMapas, positionService));
  }

  // 8. Permitir saber si el usuario llega al evento más próximo a tiempo, tomando en cuenta la ubicación actual del usuario y destino.
  @Test
  void llegaEnTiempoAlProximoEventoCuandoHayUnEventoCercano() {
    Usuario feli = crearUsuario("feli@gugle.com.ar");
    Calendario calendario = crearCalendarioVacio();
    feli.agregarCalendario(calendario);

    when(positionService.ubicacionActual("feli@gugle.com.ar")).thenReturn(utnMedrano);
    when(gugleMapas.tiempoEstimadoHasta(utnMedrano, utnMedrano)).thenReturn(Duration.ofMinutes(0));

    calendario.agendar(crearEventoSimpleEnMedrano("Parcial", LocalDateTime.now().plusMinutes(30), Duration.of(2, HOURS)));

    assertTrue(feli.llegaEnTiempoAlProximoEvento(gugleMapas, positionService));
  }

  @Test
  void noLlegaEnTiempoAlProximoEventoCuandoHayUnEventoFisicamenteLejano() {
    Usuario feli = crearUsuario("feli@gugle.com.ar");
    Calendario calendario = crearCalendarioVacio();
    feli.agregarCalendario(calendario);

    when(positionService.ubicacionActual("feli@gugle.com.ar")).thenReturn(utnMedrano);
    when(gugleMapas.tiempoEstimadoHasta(utnMedrano, utnCampus)).thenReturn(Duration.ofMinutes(60));

    calendario.agendar(crearEventoSimpleEnCampus("Parcial", LocalDateTime.now().plusMinutes(30), Duration.of(2, HOURS)));

    assertFalse(feli.llegaEnTiempoAlProximoEvento(gugleMapas, positionService));
  }

  @Test
  void llegaEnTiempoAlProximoEventoCuandoHayUnEventoCercanoAunqueAlSiguienteNoLlegue() {
    Usuario feli = crearUsuario("feli@gugle.com.ar");
    Calendario calendario = crearCalendarioVacio();
    feli.agregarCalendario(calendario);

    when(positionService.ubicacionActual("feli@gugle.com.ar")).thenReturn(utnMedrano);
    when(gugleMapas.tiempoEstimadoHasta(utnMedrano, utnMedrano)).thenReturn(Duration.ofMinutes(0));
    when(gugleMapas.tiempoEstimadoHasta(utnMedrano, utnCampus)).thenReturn(Duration.ofMinutes(90));

    calendario.agendar(crearEventoSimpleEnMedrano("Parcial", LocalDateTime.now().plusMinutes(30), Duration.of(3, HOURS)));
    calendario.agendar(crearEventoSimpleEnCampus("Final", LocalDateTime.now().plusMinutes(45), Duration.of(1, HOURS)));

    assertTrue(feli.llegaEnTiempoAlProximoEvento(gugleMapas, positionService));
  }
}
