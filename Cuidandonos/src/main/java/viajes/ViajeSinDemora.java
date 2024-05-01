package viajes;
import java.util.TimerTask;

public class ViajeSinDemora implements ITipoDeViaje{
    @Override
    public void cambiarASiguienteParada(Viaje viaje) {
        viaje.getTimer().cancel();
        iniciarTimer(viaje);
    }

    @Override
    public void iniciarTimer(Viaje viaje) {
        viaje.getTimer().schedule(
                viaje.getTaskReaccion(),
                (long) viaje.conseguirTiempoEstimadoPorParada(
                        viaje.getParadaActual()
                ) * 60 * 1000
        );
    }

}
