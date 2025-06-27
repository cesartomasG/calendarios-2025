package calendarios;

import calendarios.evento.EventoUnico;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Calendario {

  private final List<EventoUnico> eventos = new ArrayList<>();

  public void agendar(EventoUnico evento) {
    eventos.add(evento);
  }

  public boolean estaAgendado(EventoUnico evento) {
    return eventos.contains(evento);
  }

  public List<EventoUnico> eventosEntreFechas(LocalDateTime inicio, LocalDateTime fin) {
    return eventos.stream()
        .filter(e -> e.estaEntre(inicio, fin))
        .collect(Collectors.toList());
  }

  public List<EventoUnico> eventosSolapadosCon(EventoUnico evento) {
    return eventos.stream()
        .filter(e -> e.estaSolapadoCon(evento))
        .collect(Collectors.toList());
  }
}
