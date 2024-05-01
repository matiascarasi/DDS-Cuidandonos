package viajes;

import lombok.Getter;
import reacciones.Reaccion;
import servicios.ServicioDeGeolocalizacion;
import usuarios.Usuario;

import java.util.Timer;
import java.util.List;
import java.util.TimerTask;

public class Viaje {
    private List<Parada> paradas;
    private Usuario transeunte;
    private List<Usuario> cuidadores;
    private Reaccion reaccion;
    private ITipoDeViaje tipoDeViaje;
    @Getter
    private Parada paradaActual;
    @Getter
    private Timer timer;
    @Getter
    private TimerTask taskReaccion;
    private ServicioDeGeolocalizacion servicioDeGeolocalizacion;
    private final double VELOCIDAD_MTSxMIN = 83.3; // 5000 mts por 60 minutos

    public Viaje(List<Parada> paradas, Usuario transeunte, List<Usuario> cuidadores, Reaccion reaccion, ITipoDeViaje tipoDeViaje) {
        this.paradas = paradas;
        this.transeunte = transeunte;
        this.cuidadores = cuidadores;
        this.reaccion = reaccion;
        this.tipoDeViaje = tipoDeViaje;
        this.paradaActual = paradas.get(0);
        this.timer = new Timer();
        this.taskReaccion = new TimerTask() {
            @Override
            public void run() {
                reaccionar();
            }
        };
        this.servicioDeGeolocalizacion = new ServicioDeGeolocalizacion();
    }

    private Parada conseguirSiguienteParada(Parada parada) {
        int index = paradas.indexOf(parada);
        if (index == paradas.size() - 1) return null;
        return paradas.get(index + 1);
    }
    public void cambiarASiguienteParada() {
        paradaActual = conseguirSiguienteParada(paradaActual);
        if(paradaActual == null) {
            finalizar();
            return;
        }
        tipoDeViaje.cambiarASiguienteParada(this);
    }
    private double conseguirDistanciaPorParada(Parada parada) {
        var siguienteParada = conseguirSiguienteParada(parada);
        if(siguienteParada == null) return 0;
        return servicioDeGeolocalizacion.calcularDistancia(
                parada.getDireccion().getCoordenadas(),
                siguienteParada.getDireccion().getCoordenadas()
        );
    }
    public double conseguirDistanciaTotal() {
        return paradas.stream().mapToDouble(this::conseguirDistanciaPorParada).sum();
    }
    public double conseguirTiempoEstimadoPorParada(Parada parada) {
        var siguienteParada = conseguirSiguienteParada(parada);
        if(siguienteParada == null) return 0;
        return conseguirDistanciaPorParada(parada) / VELOCIDAD_MTSxMIN + siguienteParada.getDemora();
    }
    public double conseguirTiempoEstimadoTotal() {
        return paradas.stream().mapToDouble(this::conseguirTiempoEstimadoPorParada).sum();
    }
    public void reaccionar() {
        reaccion.reaccionar(this);
    }
    public void iniciar() {
        tipoDeViaje.iniciarTimer(this);
    }
    public void finalizar() {
        timer.cancel();
        timer.purge();
    }
}