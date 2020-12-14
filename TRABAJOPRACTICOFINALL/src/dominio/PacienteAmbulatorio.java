package dominio;

import java.util.ArrayList;


public class PacienteAmbulatorio extends Paciente {
	
	private ArrayList<Tratamiento> tratamientos;
	
	
	public PacienteAmbulatorio(String nombre, Integer nroHistoriaClinica, Fecha nacimiento)  {
		super(nombre, nroHistoriaClinica, nacimiento);
		
		tratamientos= new ArrayList<Tratamiento>();
	}
	

	public void agregarTratamiento(Medico medico, String nombreTratamiento)  {
		Validaciones.validarLenghtString(nombreTratamiento, 50, "El tratamiento debe ser mayor a 0 y menor a 50");

			Tratamiento tratamiento = new Tratamiento(nombreTratamiento,medico);
					tratamientos.add(tratamiento);
					modficarSaldo(saldo + medico.obtenerHonorarios());

	}

	public void pagarSaldo()  {
		if (!tratamientos.isEmpty()) {
			for (Tratamiento tratamiento : tratamientos) {
					tratamiento.setPagado( true);
				}
			modficarSaldo(0.0);
		}else {
			throw new RuntimeException("No hay atenciones para pagar");
		}
		
	}



	

}
//