package calendarios.repeticion;

import calendarios.evento.EventoRecurrente;
import calendarios.evento.EventoUnico;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class RepeticionDiasFijos implements RepeticionConfig {

  private final Set<DayOfWeek> dias;

  public RepeticionDiasFijos(Set<DayOfWeek> dias) {
    // copia inmutable desde Java 10+ (o new HashSet<>(dias) si usás Java 8)
    this.dias = Set.copyOf(dias);
  }

  @Override
  public List<EventoUnico> instanciasEntre(
      EventoRecurrente base,
      LocalDateTime desde,
      LocalDateTime hasta
  ) {
    List<EventoUnico> instancias = new ArrayList<>();
    LocalDateTime actual = base.getInicioBase();

    while (!actual.isAfter(base.getFinRepeticion()) && actual.isBefore(hasta)) {
      if (!actual.isBefore(desde) && dias.contains(actual.getDayOfWeek())) {
        EventoUnico instancia = new EventoUnico(
            base.getNombre(),
            actual,
            actual.plus(base.getDuracion()),
            base.getUbicacion(),
            base.getInvitados()
        );
        instancias.add(instancia);
      }
      actual = actual.plusDays(1);
    }

    return instancias;
  }

  @Override
  public Optional<LocalDateTime> proximaA(LocalDateTime desde, EventoRecurrente base) {
    LocalDateTime actual = desde;

    while (!actual.isAfter(base.getFinRepeticion())) {
      if (dias.contains(actual.getDayOfWeek())) {
        LocalDateTime proxima = actual.withHour(base.getInicioBase().getHour())
            .withMinute(base.getInicioBase().getMinute());

        if (proxima.isAfter(desde)) {
          return Optional.of(proxima);
        }
      }
      actual = actual.plusDays(1);
    }

    return Optional.empty();
  }
}
