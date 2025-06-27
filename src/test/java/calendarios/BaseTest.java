package calendarios;

import calendarios.evento.EventoUnico;
import calendarios.servicios.GugleMapas;
import calendarios.servicios.PositionService;
import org.junit.jupiter.api.BeforeEach;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;

public abstract class BaseTest {

  protected PositionService positionService;
  protected GugleMapas gugleMapas;

  protected Ubicacion utnMedrano = new Ubicacion(-34.5984902, -58.4201843);
  protected Ubicacion utnCampus = new Ubicacion(-34.6591644, -58.4694862);

  @BeforeEach
  void initMocks() {
    positionService = mock(PositionService.class);
    gugleMapas = mock(GugleMapas.class);
  }

  protected Usuario crearUsuario(String email) {
    return new Usuario(email);
  }

  protected Calendario crearCalendarioVacio() {
    return new Calendario();
  }

  protected EventoUnico crearEventoSimpleEnMedrano(String nombre, LocalDateTime inicio, Duration duracion) {
    return crearEventoSimple(nombre, inicio, inicio.plus(duracion), utnMedrano, Collections.emptyList());
  }

  protected EventoUnico crearEventoSimpleEnCampus(String nombre, LocalDateTime inicio, Duration duracion) {
    return crearEventoSimple(nombre, inicio, inicio.plus(duracion), utnCampus, Collections.emptyList());
  }

  protected EventoUnico crearEventoSimple(String nombre, LocalDateTime inicio, LocalDateTime fin, Ubicacion ubicacion, List<Usuario> invitados) {
    return new EventoUnico(nombre, inicio, fin, ubicacion, invitados);
  }
}
