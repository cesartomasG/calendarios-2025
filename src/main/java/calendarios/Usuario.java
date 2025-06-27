package calendarios;

import calendarios.evento.EventoUnico;
import calendarios.servicios.GugleMapas;
import calendarios.servicios.PositionService;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Usuario {
  String email;

  private final List<Calendario> calendarios = new ArrayList<>();

  public Usuario(String email) {
    this.email = Objects.requireNonNull(email, "El email no puede ser null");

    if (email.isBlank()) {
      throw new IllegalArgumentException("El email no puede estar vacío");
    }

    if (!esEmailValido(email)) {
      throw new IllegalArgumentException("El email ingresado no es válido");
    }

    this.email = email;
  }

  //regex basica para validacion de correo
  private boolean esEmailValido(String email) {
    return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
  }

  public void agregarCalendario(Calendario calendario) {
    calendarios.add(calendario);
  }

  public List<EventoUnico> eventosEntreFechas(LocalDateTime inicio, LocalDateTime fin) {
    return calendarios.stream()
        .flatMap(calendario -> calendario.eventosEntreFechas(inicio, fin).stream())
        .collect(Collectors.toList());
  }

  public boolean llegaEnTiempoAlProximoEvento(GugleMapas gugleMapas,
                                              PositionService positionService) {
    return calendarios.stream()
        .flatMap(c -> c.eventosEntreFechas(LocalDateTime.now(), LocalDateTime.MAX).stream())
        .min(Comparator.comparing(EventoUnico::getInicio))
        .map(evento -> {
          Duration viaje = gugleMapas.tiempoEstimadoHasta(
              positionService.ubicacionActual(email),
              evento.getUbicacion());
          return LocalDateTime.now().plus(viaje).isBefore(evento.getInicio());
        }).orElse(true); // si no hay eventos, llega a tiempo por default
  }

  public boolean tieneCalendario(Calendario calendario) {
    return calendarios.contains(calendario);
  }

  public String getEmail() {
    return email;
  }
}
