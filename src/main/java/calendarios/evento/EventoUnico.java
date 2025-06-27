package calendarios.evento;

import calendarios.Recordatorio;
import calendarios.Ubicacion;
import calendarios.Usuario;
import calendarios.servicios.ShemailLib;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventoUnico implements Evento {

  private final String nombre;
  private final LocalDateTime inicio;
  private final LocalDateTime fin;
  private final Ubicacion ubicacion;
  private final List<Usuario> invitados;
  private final Duration duracion;

  private final List<Recordatorio> recordatorios = new ArrayList<>();

  public EventoUnico(String nombre, LocalDateTime inicio, LocalDateTime fin,
                     Ubicacion ubicacion, List<Usuario> invitados) {
    this.nombre = nombre;
    this.inicio = inicio;
    this.fin = fin;
    this.ubicacion = ubicacion;
    this.invitados = new ArrayList<>(invitados); // copia defensiva
    duracion = Duration.between(inicio, fin);
  }

  @Override
  public List<EventoUnico> instanciasEntre(LocalDateTime desde, LocalDateTime hasta) {
    if (inicio.isBefore(hasta) && fin.isAfter(desde)) {
      return List.of(this);
    }
    return List.of();
  }

  @Override
  public Duration cuantoFalta() {
    Duration duracionHasta = Duration.between(LocalDateTime.now(), inicio);
    return duracionHasta.isNegative() ? Duration.ZERO : duracionHasta;
  }

  public boolean estaSolapadoCon(EventoUnico otro) {
    return this.inicio.isBefore(otro.fin) && otro.inicio.isBefore(this.fin);
  }

  public boolean estaEntre(LocalDateTime desde, LocalDateTime hasta) {
    return !inicio.isBefore(desde) && inicio.isBefore(hasta);
  }

  public EventoUnico copiarConNuevoInicio(LocalDateTime nuevoInicio) {
    LocalDateTime nuevoFin = nuevoInicio.plus(getDuracion());
    return new EventoUnico(nombre, nuevoInicio, nuevoFin, ubicacion, invitados);
  }

  public void agregarRecordatorio(Recordatorio recordatorio) {
    recordatorios.add(recordatorio);
  }

  public void verificarRecordatorios(ShemailLib shemailLib) {
    recordatorios.removeIf(recordatorio -> recordatorio.verificarEnvio(this, shemailLib));
  }

  public String getNombre() {
    return nombre;
  }

  public LocalDateTime getInicio() {
    return inicio;
  }

  public Duration getDuracion() {
    return duracion;
  }

  public Ubicacion getUbicacion() {
    return ubicacion;
  }

  public List<Usuario> getInvitados() {
    return new ArrayList<>(invitados); // copia defensiva
  }

}
