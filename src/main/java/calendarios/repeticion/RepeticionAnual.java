package calendarios.repeticion;

import calendarios.evento.EventoRecurrente;
import calendarios.evento.EventoUnico;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RepeticionAnual implements RepeticionConfig {

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
      actual = actual.plusYears(1);
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
      actual = actual.plusYears(1);
    }
    return Optional.empty();
  }
}