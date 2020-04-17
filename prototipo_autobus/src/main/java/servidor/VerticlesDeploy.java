package servidor;

import java.util.ArrayList;
import java.util.List;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class VerticlesDeploy extends AbstractVerticle {

	
	
	//TODO: Configurar servidor HTTP con un dominio concreto
	public void start(Future<Void> startFuture) {
		
		vertx.createHttpServer().requestHandler(req ->{
			req.response().end("El servidor se ha creado correctamente");
		}).listen(8081, res -> {
			String s = (res.succeeded()) ? "Se recibe algo por el puerto 8081" : res.cause().toString();
			System.out.println(s);
		});
		
		
		//Añadir aquí los verticles necesarios para no tocar más código abajo
		String verticle_principal = "VerticlesDeploy";
		String verticle_API_REST = "DatabaseVerticle"; 
		//String nuevo_verticle = "...";
		
		List<String> lista_verticles_string = new ArrayList<String>();
		lista_verticles_string.add(verticle_principal);
		lista_verticles_string.add(verticle_API_REST);
		//lista_verticles_string.add(nuevo_verticle)
		
		desplegarVerticles(new ArrayList<>());
		
	}
	
	//Cómo se enumera una lista de verticles?
	public void desplegarVerticles(List<String> l_verticles_s) {
		for(String verticle_s : l_verticles_s) {
			vertx.deployVerticle(verticle_s.getClass().getName());
		}
	}
	
	
}
