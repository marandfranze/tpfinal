package dominio;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PacienteObraSocial extends Paciente {
	private String obraSocial;
	private double descuento;
	private LinkedList<Internacion> internaciones;

	public PacienteObraSocial(String nombre, Integer nroHistoriaClinica, Fecha nacimiento, String obraSocial,
			double descuento)  {
		super(nombre, nroHistoriaClinica, nacimiento);
		
		Validaciones.validarLenghtString(obraSocial, 50, "La obra social debe estar entre 0 y 50 caracteres");
			Validaciones.validarPorcentaje(descuento, "El descuento debe estar entre 0 y 100 %");
		
		
		this.obraSocial = obraSocial;
		this.descuento = descuento;
		internaciones = new LinkedList<Internacion>();
	}

	public String obtenerObraSocial() {
		return this.obraSocial;
	}

	// o(n)
	private boolean contieneFechaIngreso(Fecha fechaIngreso) {
		boolean alguno = false;
		for (Internacion internacion : internaciones) {
			alguno = alguno || (internacion.fecha.equals(fechaIngreso));
		}
		return alguno;
	}

	// o(1)
	public boolean hayInternacionActiva() {
		if(internaciones.isEmpty()) {
			return false;
		}
		return internaciones.getLast().obtenerFechaAlta() == null;
	}

	public boolean agregarInternacion(String area, Fecha fechaIngreso, double importe, Integer nroHabitacion)  {
			if (!contieneFechaIngreso(fechaIngreso) && !hayInternacionActiva()) {
				area=area.toUpperCase();
				if (area.equals("CARDIOLOGIA") || area.equals("GENERAL") || area.equals("PEDIATRIA")) {
					Internacion internacion = new Internacion(importe, fechaIngreso, obraSocial, area, descuento,
							nroHabitacion);
					internaciones.addLast(internacion);
					return true;
				}
			}
		return false;
	}

	


	public int darAlta(Fecha fechaAlta)  {
		if (!internaciones.isEmpty()) {
			Internacion ultimaInternacion = internaciones.getLast();
			if (hayInternacionActiva() && ultimaInternacion.obtenerFechaIngreso().esMayorIgual(fechaAlta)) {
					ultimaInternacion.agregarFechaAlta(fechaAlta);
				double costoAlta = descuento * ultimaInternacion.obtenerFechaIngreso().obtenerDias(fechaAlta)
						* ultimaInternacion.obtenerImporte();
				ultimaInternacion.agregarImporte(costoAlta);
				modficarSaldo(saldo + costoAlta);
				return ultimaInternacion.obtenerNroHabitacion();
			}
		}
				return -1;
		}

	public void pagarSaldo()  {
		if (!internaciones.isEmpty()) {
			for (Internacion internacion : internaciones) {
				if (internacion.obtenerFechaAlta() != null)
					internacion.pagar();
			}
			modficarSaldo(0.0);
		}
		else {
			throw new RuntimeException("No hay atenciones para pagar");
		}
	}

}
