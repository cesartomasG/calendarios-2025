package calendarios.repeticion;

import calendarios.evento.EventoRecurrente;
import calendarios.evento.EventoUnico;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RepeticionCadaCiertasSemanas implements RepeticionConfig {
  private final int intervalo;

  public RepeticionCadaCiertasSemanas(int intervalo) {
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
      actual = actual.plusWeeks(intervalo);
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
      actual = actual.plusWeeks(intervalo);
    }
    return Optional.empty();
  }
}