package calendarios.evento;

import calendarios.Ubicacion;
import calendarios.Usuario;
import calendarios.repeticion.RepeticionConfig;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class EventoRecurrente implements Evento {

  private final EventoUnico base; // Primera instancia, con todos los datos
  private final RepeticionConfig repeticion; // Lógica de repetición
  private final LocalDateTime finRepeticion; // Hasta cuándo repite

  public EventoRecurrente(
      EventoUnico base,
      RepeticionConfig repeticion,
      LocalDateTime finRepeticion
  ) {
    this.base = base;
    this.repeticion = repeticion;
    this.finRepeticion = finRepeticion;
  }

  @Override
  public List<EventoUnico> instanciasEntre(LocalDateTime desde, LocalDateTime hasta) {
    return repeticion.instanciasEntre(this, desde, hasta);
  }

  @Override
  public Duration cuantoFalta() {
    return repeticion.proximaA(LocalDateTime.now(), this)
        .map(prox -> Duration.between(LocalDateTime.now(), prox))
        .orElse(Duration.ZERO);
  }

  public LocalDateTime getInicioBase() {
    return base.getInicio();
  }

  public EventoUnico getBase() {
    return base;
  }

  public String getNombre() {
    return base.getNombre();
  }

  public Duration getDuracion() {
    return base.getDuracion();
  }

  public Ubicacion getUbicacion() {
    return base.getUbicacion();
  }

  public List<Usuario> getInvitados() {
    return base.getInvitados();
  }

  public LocalDateTime getFinRepeticion() {
    return finRepeticion;
  }
}
