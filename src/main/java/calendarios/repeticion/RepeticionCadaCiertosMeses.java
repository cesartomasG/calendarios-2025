package calendarios.repeticion;

import calendarios.evento.EventoRecurrente;
import calendarios.evento.EventoUnico;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RepeticionCadaCiertosMeses implements RepeticionConfig {
  private final int intervalo;

  public RepeticionCadaCiertosMeses(int intervalo) {
    this.intervalo = intervalo;
  }

  @Override
  public List<EventoUnico> instanciasEntre(EventoRecurrente base,
                                           LocalDateTime desde,
                                           LocalDateTime hasta) {
    List<EventoUnico> instancias = new ArrayList<>();
    LocalDateTime actual = base.getInicioBase();

    while (!actual.isAfter(base.getFinRepeticion()) && actual.isBefore(hasta)) {
      if (!actual.isBefore(desde)) {
        instancias.add(base.getBase().copiarConNuevoInicio(actual));
      }
      actual = actual.plusMonths(intervalo);
    }

    return instancias;
  }

  @Override
  public Optional<LocalDateTime> proximaA(LocalDateTime desde, EventoRecurrente base) {
    LocalDateTime actual = base.getInicioBase();
    while (!actual.isAfter(base.getFinRepeticion())) {
      if (!actual.isBefore(desde)) {
        return Optional.of(actual);
      }
      actual = actual.plusMonths(intervalo);
    }
    return Optional.empty();
  }
}