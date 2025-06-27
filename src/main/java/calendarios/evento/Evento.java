package calendarios.evento;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public interface Evento {
  List<EventoUnico> instanciasEntre(LocalDateTime desde, LocalDateTime hasta);

  Duration cuantoFalta();
}
