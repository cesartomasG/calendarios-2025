package calendarios.repeticion;

import calendarios.evento.EventoRecurrente;
import calendarios.evento.EventoUnico;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RepeticionConfig {
  List<EventoUnico> instanciasEntre(EventoRecurrente base,
                                    LocalDateTime desde,
                                    LocalDateTime hasta);

  Optional<LocalDateTime> proximaA(LocalDateTime desde, EventoRecurrente base);
}
