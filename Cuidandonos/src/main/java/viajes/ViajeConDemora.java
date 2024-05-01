package viajes;

public class ViajeConDemora implements ITipoDeViaje{
    @Override
    public void cambiarASiguienteParada(Viaje viaje) {

    }

    @Override
    public void iniciarTimer(Viaje viaje) {
        viaje.getTimer().schedule(
                viaje.getTaskReaccion(),
                (long) viaje.conseguirTiempoEstimadoTotal() * 60 * 1000
        );
    }
}