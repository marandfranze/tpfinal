package dominio;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.lang.Exception;

public class Centro {

	private HashMap<Integer, Medico> medicos;
	private HashMap<String, Double> especialidades;
	private HashMap<Integer, Paciente> pacientes;
	HashSet<String> obrasSociales;
	
	private ArrayList<Boolean> habitaciones;
	
	private String cuit;
	private String nombre;
	private Double costoDiaInternacion;

	public Centro(String nombre, String cuit, double costoDiaInternacion)  {
	
		Validaciones.validarLenghtString(nombre, 50, "El nombre del centro medico debe estar entre 0 y 50 caracteres ");
			Validaciones.validarPrecio(costoDiaInternacion, 100000.0,
					"El costo del dia de internacion debe estar entre 0 y 100000");
		
		if (cuit.length() == 13) {
			this.cuit = cuit;
		} else {
			throw new RuntimeException("El cuit debe ser de 13 caracteres incluyendo guiones");
		}
		this.nombre = nombre;
		this.costoDiaInternacion = costoDiaInternacion;

		medicos = new HashMap<Integer, Medico>();
		pacientes = new HashMap<Integer, Paciente>();
		especialidades = new HashMap<String, Double>();
		obrasSociales = new HashSet<String>();
		habitaciones = new ArrayList<Boolean>();
		crearHabitaciones(100);
	}

	public void agregarEspecialidad(String nombre, double valor)  {
		Validaciones.validarLenghtString(nombre, 50, "La especialidad tiene que estar entre 0 y 50 caracteres");
			Validaciones.validarPrecio(valor, 100000.0, "El valor de la especialidad debe estar entre 0 y 100000");
	
		if (!especialidades.containsKey(nombre)) {
			especialidades.put(nombre, valor);
		
		}
		else {
			throw new RuntimeException("null");
		}
	}
	public void agregarMedico(String nombre, int matricula, String nomEspecialidad, double honorarios)
			 {
			Validaciones.validarNumeroPositivo(matricula);
		
		
		if (!medicos.containsKey(matricula) && especialidades.containsKey(nomEspecialidad)) {
			Medico medico = new Medico(nombre, matricula, nomEspecialidad, honorarios);
			medicos.put(matricula, medico);
		}
		else { 
			throw new RuntimeException("error");
	}
	}

	public void agregarPacientePrivado(String nombre, Integer historiaClinica, Fecha nac)  {
			Validaciones.validarNumeroPositivo(historiaClinica);
		
		
		if (!pacientes.containsKey(historiaClinica)) {
			PacientePrivado paciente = new PacientePrivado(nombre, historiaClinica, nac);
			pacientes.put(historiaClinica, paciente);
			
		}
		else { 
			throw new RuntimeException("error");
	}
	}

	public void agregarPacienteAmbulatorio(String nombre, Integer nroHistoriaClinica, Fecha nac) {
			Validaciones.validarNumeroPositivo(nroHistoriaClinica);
												
		
		if (!pacientes.containsKey(nroHistoriaClinica)) {
			PacienteAmbulatorio paciente = new PacienteAmbulatorio(nombre, nroHistoriaClinica, nac);
			pacientes.put(nroHistoriaClinica, paciente);
		
		}
		else { 
			throw new RuntimeException("error");
	}
	}

	public void agregarPacienteObraSocial(String nombre, Integer historiaClinica, Fecha nac, String obraSocial,
			double descuento)  {
		
			Validaciones.validarNumeroPositivo(historiaClinica);
		
			Validaciones.validarPorcentaje(descuento, "El descuento debe estar entre 0 y 100 %");
		
		if (!pacientes.containsKey(historiaClinica)) {
			obrasSociales.add(obraSocial);
			PacienteObraSocial paciente = new PacienteObraSocial(nombre, historiaClinica, nac, obraSocial, descuento);
			pacientes.put(historiaClinica, paciente);
		
		}
		else { 
			throw new RuntimeException("error");
	}
	}

	// atencion consultorio
	public void agregarAtencion(Integer historiaClinica, Fecha fecha, Integer matricula)  {
		Paciente pacienteAux = pacientes.get(historiaClinica);
		if (pacienteAux!= null && pacienteAux instanceof PacientePrivado) {
			PacientePrivado paciente = (PacientePrivado) pacienteAux;
				if (medicos.containsKey(matricula)) {
					Medico medico = medicos.get(matricula);
					if (especialidades.containsKey(medico.obtenerEspecialidad())) {
						Double importe = especialidades.get(medico.obtenerEspecialidad());
						 paciente.agregarAtencionConsultorio(fecha, medico, importe);
					}
				}
			}
		else { 
			throw new RuntimeException("error");
			}
		}
		
	

	// atencion guardia
	public void agregarAtencion(int historiaClinica, Fecha fecha) {
		Paciente pacienteAux = pacientes.get(historiaClinica);
		if (pacienteAux!= null && pacienteAux instanceof PacientePrivado) {
				PacientePrivado paciente = (PacientePrivado) pacienteAux;
				 paciente.agregarAtencionGuardia(fecha);		
		}else { 
		throw new RuntimeException("error");
		}
	}
		

	private void crearHabitaciones(Integer cantidad)  {
		
		if (habitaciones.isEmpty()) {
			for (int i = 1; i < cantidad; i++) {
				habitaciones.add(false);
			}
		} else {
			throw new RuntimeException("fallo al crear habitaciones");
		}

	}

	private Integer obtenerHabitacionVacia()  {
		if (!habitaciones.isEmpty()) {
		for(Integer i=1 ; i<=habitaciones.size(); i++) {
			if(habitaciones.get(i) == false) {
				return i;
			}
		  }				
		}		
		return -1;
	}		
	

	// atencion internacion
	public void agregarInternacion(Integer historiaClinica, String area, Fecha fechaIngreso) {
		if (pacientes.containsKey(historiaClinica) && pacientes.get(historiaClinica) instanceof PacienteObraSocial) { 	
				PacienteObraSocial paciente = (PacienteObraSocial) pacientes.get(historiaClinica);
					paciente.agregarInternacion(area, fechaIngreso, costoDiaInternacion, obtenerHabitacionVacia());			
			}
			else {
		throw new RuntimeException("error");
			}
	}
	

	public void altaInternacion(Integer historiaClinica, Fecha fechaAlta)  {
		if (pacientes.containsKey(historiaClinica) && pacientes.get(historiaClinica) instanceof PacienteObraSocial) {
				PacienteObraSocial paciente = (PacienteObraSocial) pacientes.get(historiaClinica);
				int posicion= paciente.darAlta(fechaAlta);
					if(posicion == -1) {
						throw new RuntimeException("no existe una habitacion para dar el alta");
					}
					habitaciones.set(posicion,false);
			}
			else {
				throw new RuntimeException("no existe un paciente con esa historia clinica");
			}
	}

	public void agregarTratamiento(Integer historiaClinica, Integer matricula, String tratamiento)  {
				if (pacientes.containsKey(historiaClinica)  && pacientes.get(historiaClinica) instanceof PacienteAmbulatorio 
						&& medicos.containsKey(matricula)) { 
				PacienteAmbulatorio paciente = (PacienteAmbulatorio) pacientes.get(historiaClinica);
				 paciente.agregarTratamiento(medicos.get(matricula), tratamiento);
			}
		else {
		throw new RuntimeException("error");	
		}
	}

	public Double getSaldo(Integer historiaClinica)  {
		if (pacientes.containsKey(historiaClinica)) {
			Paciente paciente = pacientes.get(historiaClinica);
			return paciente.obtenerSaldo();
		}else {
		throw new RuntimeException("No existe el paciente");
		}
	}

	public void pagarSaldo(Integer historiaClinica) {
		if (pacientes.containsKey(historiaClinica)) {
			Paciente paciente = pacientes.get(historiaClinica);
			paciente.pagarSaldo();
		} else {
			throw new RuntimeException("No existe el paciente");
		}
	}

	public List<Integer> listaInternacion()  {
		if (pacientes.isEmpty()) {
			throw new RuntimeException("no hay pacientes");
		}
		List<Integer> list = new ArrayList<Integer>();
		for (Paciente paciente : pacientes.values()) {
			if (paciente instanceof PacienteObraSocial) {
				PacienteObraSocial pacienteOS = (PacienteObraSocial) paciente;
				if (pacienteOS.hayInternacionActiva()) {
					list.add(paciente.nroHistoriaClinica);
				}
			}
		}
		return list;
	}

	// <fecha,especialidad>
	public Map<Fecha, String> atencionesEnConsultorio(Integer historiaClinica) {
		Map<Fecha, String> atencionesConsultorio = new HashMap<Fecha, String>();
		if (pacientes.containsKey(historiaClinica)) {
			if(pacientes.get(historiaClinica) instanceof PacientePrivado) {
				PacientePrivado paciente = (PacientePrivado) pacientes.get(historiaClinica);
				atencionesConsultorio = paciente.obtenerAtencionesConsultorio();
			}
		}
		return atencionesConsultorio;
	}

	

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
			
				sb.append("MEDICOS : [");
				medicos.forEach((Integer,Medico) -> sb.append(Medico.toString() + "\n"));
				sb.append("  ] ");
				sb.append("\n");
				sb.append("ESPECIALIDADES  :  [");
				especialidades.forEach((String,Double)->sb.append(String + "  :   " +  Double +",   " + "\n"));
				sb.append("  ] ");
				sb.append("\n");
				sb.append("PACIENTES : [");
				pacientes.forEach((Integer,Paciente) -> sb.append(Paciente.toString() + "\n") );
				sb.append("  ] ");
				sb.append("\n");
				sb.append("OBRAS SOCIALES : [");
				 Iterator<String> value = obrasSociales.iterator(); 
					while (value.hasNext()) { 
						sb.append("\n");
						sb.append(value.next());			        
				}
				sb.append("  ] ");
				sb.append("HABITACIONES : [");
				sb.append("\n");
				for(int i=1; i<habitaciones.size(); i++) {
					sb.append("NUMERO : ");
					sb.append(",");
					sb.append(i);
					sb.append(",  ");
					sb.append(habitaciones.get(i));
					sb.append("\n");
				}
				sb.append("  ] ");

				return "centro medico   " + sb;
				//}
		
}
	
	
	}
	// AGREGAR VALIDACION EXISTENCIA DE COLECCIONES EN LOS METODOS
