package usuarios;

import utils.Direccion;
import utils.Sexo;

import java.time.LocalDate;

public class Usuario {
    private String nombre;
    private String apellido;
    private Direccion direccion;
    private LocalDate fechaDeNacimiento;
    private Sexo sexo;
    private ITipoDeUsuario tipoDeUsuario; 
}
