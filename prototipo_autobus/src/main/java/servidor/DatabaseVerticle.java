package servidor;

import clases.Administrador;
import clases.DispositivoAutobus;
import clases.DispositivoParada;
import clases.LineaAutobus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.mysqlclient.MySQLClient;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;

public class DatabaseVerticle extends AbstractVerticle {

	private MySQLPool mySQLPool;

	public void start(Promise<Void> startPromise) {
		// ===CREACION DE LA CONEXION A BASE DE DATOS===
		MySQLConnectOptions mySQLConnectOptions = new MySQLConnectOptions().setPort(3306).setHost("localhost")
				.setDatabase("daddatabase").setUser("root").setPassword("devgel204");
		// Gestiona todas las conexiones en paralelo
		PoolOptions poolOptions = new PoolOptions().setMaxSize(5);

		mySQLPool = MySQLPool.pool(vertx, mySQLConnectOptions, poolOptions);

		Router router = Router.router(vertx);
		router.route().handler(BodyHandler.create());
		// router se encargar de gestionar las peticiones
		vertx.createHttpServer().requestHandler(router::handle).listen(8080, result -> {
			if (result.succeeded()) {
				startPromise.complete();
			} else {
				startPromise.fail(result.cause());
			}
		});

		// ===FUNCIONES DE 'ADMINISTRADOR'===
		router.get("/tussam_api/administradores").handler(this::getAllAdministradores);
		router.get("/tussam_api/administradores/:idadministrador").handler(this::getAdministrador);
		router.put("/tussam_api/administradores/").handler(this::putAdministrador);
		router.delete("/tussam_api/administradores").handler(this::deleteAdministrador);

		// dispositivo_autobus -> tabla relacion autobus-linea (añadir esta inserción en
		// los métodos)
		// ===FUNCIONES DE 'DISPOSITIVOAUTOBUS'===
		router.get("/tussam_api/dispositivos_autobus").handler(this::getAllDispositivosAutobus);
		router.get("/tussam_api/dispositivos_autobus/:iddispositivo_autobus").handler(this::getDispositivoAutobus);
		router.put("/tussam_api/dispositivos_autobus").handler(this::putDispositivoAutobus);
		router.delete("/tussam_api/dispositivos_autobus").handler(this::deleteDispositivoAutobus);

		// ===FUNCIONES DE 'LINEA'===
		router.get("/tussam_api/lineas").handler(this::getAllLineas);
		router.get("/tussam_api/lineas/:idlinea").handler(this::getLinea);
		router.put("/tussam_api/lineas").handler(this::putLinea);
		router.delete("/tussam_api/lineas").handler(this::deleteLinea);

		// ===FUNCIONES DE 'SENSORES'===
		router.get("/tussam_api/sensores").handler(this::getAllSensores);
		router.get("/tussam_api/sensores/:tipo/:sensor").handler(this::getSensor);
		router.put("/tussam_api/sensores").handler(this::putSensor);
		router.delete("/tussam_api/sensores").handler(this::deleteSensor);

		// parada-> tabla relación parada-linea (añadir en los métodos)
		// ===FUNCIONES DE 'PARADA'===
		router.get("/tussam_api/dispositivos_parada").handler(this::getAllParadas);
		router.get("/tussam_api/dispositivos_parada/:idparada").handler(this::getParada);
		router.put("/tussam_api/dispositivos_parada").handler(this::putParada);
		router.delete("/tussam_api/dispositivos_parada").handler(this::deleteParada);

	}

// ===============================================================================================================================
	// ===IMPLEMENTACIÓN DE FUNCIONES DE 'ADMINISTRADOR' ===
	public void getAllAdministradores(RoutingContext rc) {
		mySQLPool.query("SELECT * FROM proyectotussam.administrador", res -> {
			if (res.succeeded()) {
				RowSet<Row> resultSet = res.result();
				System.out.println("El número de elementos obtenidos es " + resultSet.size());

				JsonArray result = new JsonArray();
				for (Row row : resultSet) {
					// IMPORTANTE: Sacar los valores de las filas segun el orden del constructor de
					// la clase DispositivoAutobus
					result.add(JsonObject
							.mapFrom(new Administrador(row.getInteger("idAdministrador"), row.getString("dni"),
									row.getString("nombre"), row.getString("apellidos"), row.getLong("timestamp"))));
				}

				rc.response().setStatusCode(200).putHeader("content-type", "application/json")
						.end(result.encodePrettily());
			} else {
				rc.response().setStatusCode(401).putHeader("content-type", "application/json")
						.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
			}
		});
	}

	private void getAdministrador(RoutingContext rc) {
		mySQLPool.query("SELECT * FROM proyectotussam.administrador WHERE idadministrador = "
				+ rc.request().getParam("idadministrador"), res -> {
					if (res.succeeded()) {
						RowSet<Row> resultSet = res.result();
						JsonArray result = new JsonArray();
						for (Row row : resultSet) {
							// IMPORTANTE: Sacar los valores de las filas, con los atributos de la
							// BBDD,segun el orden del constructor
							// de la clase DispositivoAutobus
							result.add(JsonObject.mapFrom(new Administrador(row.getInteger("idAdministrador"),
									row.getString("dni"), row.getString("nombre"), row.getString("apellidos"),
									row.getLong("timestamp"))));
						}

						rc.response().setStatusCode(200).putHeader("content-type", "application/json")
								.end(result.encodePrettily());
					} else {
						rc.response().setStatusCode(401).putHeader("content-type", "application/json")
								.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
					}
				});
	}

	private void putAdministrador(RoutingContext routingContext) {
		Administrador administrador = Json.decodeValue(routingContext.getBodyAsString(), Administrador.class);
		mySQLPool.preparedQuery(
				// Aqui si que utilizamos los nombres dados en la BBDD
				"INSERT INTO administrador (idadministrador, dni, nombre, apellidos,fecha_creacion) VALUES (?,?,?,?,?,?)",
				Tuple.of(administrador.getIdAdministrador(), administrador.getDni(), administrador.getNombre(),
						administrador.getApellidos(), administrador.getfecha_creacion()),
				handler -> {
					if (handler.succeeded()) {

						long id = handler.result().property(MySQLClient.LAST_INSERTED_ID);
						administrador.setIdAdministrador((int) id);

						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
								.end(JsonObject.mapFrom(administrador).encodePrettily());
					} else {
						System.out.println(handler.cause().toString());
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
								.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
					}
				});
	}

	private void deleteAdministrador(RoutingContext routingContext) {
		mySQLPool.query("DELETE FROM proyectotussam.administrador WHERE idadministrador = "
				+ routingContext.request().getParam("idadministrador"), res -> {
					if (res.succeeded()) {

						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
								.end("<h2>Tu operacion se ha realizado correctamente</h2>");
					} else {
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
								.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
					}
				});
	}

	// ===IMPLEMENTACIÓN DE FUNCIONES DE 'DISPOSITIVOAUTOBUS' ===
	private void getAllDispositivosAutobus(RoutingContext rc) {
		mySQLPool.query("SELECT * FROM proyectotussam.dispositivo_autobus", res -> {
			if (res.succeeded()) {
				RowSet<Row> resultSet = res.result();
				System.out.println("El número de elementos obtenidos es " + resultSet.size());

				JsonArray json_res = new JsonArray();
				for (Row row : resultSet) {
					DispositivoAutobus dispositivo_autobus = new DispositivoAutobus(
							row.getInteger("iddispositivo_autobus"), row.getString("identificador_autobus"),
							row.getInteger("fecha_creacion"), row.getInteger("capacidad"), row.getInteger("ocupacion"),
							row.getInteger("idadministrador"), row.getInteger("idlinea"));
					json_res.add(dispositivo_autobus);
				}

				rc.response().setStatusCode(200).putHeader("content-type", "application/json")
						.end(json_res.encodePrettily());
			} else {
				rc.response().setStatusCode(401).putHeader("content-type", "application/json")
						.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
			}
		});
	}

	private void getDispositivoAutobus(RoutingContext rc) {
		mySQLPool.query(
				"SELECT dispositivo_autobus FROM proyectotussam.dispositivo_autobus WHERE iddispositivo_autobus = "
						+ rc.request().getParam("iddispositivo_autobus"),
				res -> {
					if (res.succeeded()) {
						RowSet<Row> resultSet = res.result();
						JsonArray json_res = new JsonArray();
						for (Row row : resultSet) { // Aquí solo debería añadir una linea
							DispositivoAutobus dispositivo_autobus = new DispositivoAutobus(
									row.getInteger("iddispositivo_autobus"), row.getString("identificador_autobus"),
									row.getInteger("fecha_creacion"), row.getInteger("capacidad"),
									row.getInteger("ocupacion"), row.getInteger("idadministrador"),
									row.getInteger("idlinea"));
							json_res.add(dispositivo_autobus);
						}

						rc.response().setStatusCode(200).putHeader("content-type", "application/json")
								.end(json_res.encodePrettily());
					} else {
						rc.response().setStatusCode(401).putHeader("content-type", "application/json")
								.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
					}
				});
	}

	private void putDispositivoAutobus(RoutingContext routingContext) {
		DispositivoAutobus dispositivoAutobus = Json.decodeValue(routingContext.getBodyAsString(),
				DispositivoAutobus.class);
		mySQLPool.preparedQuery(
				// Aqui si que utilizamos los nombres dados en la BBDD
				"INSERT INTO dispositivo_autobus (identificador_autobus, fecha_creacion, capacidad, ocupacion,idadministrador,idlinea) VALUES (?,?,?,?,?,?)",
				Tuple.of(dispositivoAutobus.getIdentificador_autobus(), dispositivoAutobus.getFecha_creacion(),
						dispositivoAutobus.getCapacidad(), dispositivoAutobus.getOcupacion(),
						dispositivoAutobus.getIdadministrador(), dispositivoAutobus.getIdlinea_autobus()),
				handler -> {
					if (handler.succeeded()) {
						System.out.println(handler.result().rowCount());

						// Se devuleve el objeto que ha introducido el usario pero modificado, con el id
						// que la bbdd
						// ha genereado para el
						long id = handler.result().property(MySQLClient.LAST_INSERTED_ID);
						dispositivoAutobus.setIddispositivo_autobus((int) id);

						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
								.end(JsonObject.mapFrom(dispositivoAutobus).encodePrettily());
					} else {
						System.out.println(handler.cause().toString());
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
								.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
					}
				});
	}

	private void deleteDispositivoAutobus(RoutingContext routingContext) {
		mySQLPool.query("DELETE FROM proyectotussam.dispositivo_autobus WHERE iddispositivo_autobus = "
				+ routingContext.request().getParam("iddispositivo_autobus"), res -> {
					if (res.succeeded()) {

						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
								.end("<h2>Tu operacion se ha realizado correctamente</h2>");
					} else {
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
								.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
					}
				});
	}

	// ===IMPLEMENTACIÓN DE FUNCIONES DE 'LINEA' ===
	public void getAllLineas(RoutingContext rc) {
		mySQLPool.query("SELECT * FROM proyectotussam.linea", res -> {
			if (res.succeeded()) {
				RowSet<Row> tuplas = res.result();
				System.out.println("Nº elementos devueltos = " + tuplas.size());
				JsonArray json_res = new JsonArray();
				for (Row tupla : tuplas) {
					LineaAutobus linea = new LineaAutobus(tupla.getInteger("idlinea"), tupla.getString("nombre"));
					json_res.add(linea);
				}
				rc.response().setStatusCode(200).putHeader("content-type", "application/json")
						.end(json_res.encodePrettily());
			} else {
				rc.response().setStatusCode(200).putHeader("content-type", "application/json")
						.end(JsonObject.mapFrom(res.cause()).encodePrettily());
			}
		});
	}

	public void getLinea(RoutingContext rc) {
		mySQLPool.query("SELECT linea FROM proyectotussam.linea WHERE idlinea =" + rc.request().getParam("idlinea"),
				res -> {
					if (res.succeeded()) {
						RowSet<Row> tuplas = res.result();
						// Tamaño correcto = 1; tamaño = 0 es que no existe; >0 debería ser un error
						System.out.println("Nº elementos devueltos = " + tuplas.size());
						JsonArray json_res = new JsonArray();
						for (Row tupla : tuplas) {
							LineaAutobus linea = new LineaAutobus(tupla.getInteger("idlinea"),
									tupla.getString("nombre"));
							json_res.add(linea);
						}
						rc.response().setStatusCode(200).putHeader("content-type", "application/json")
								.end(json_res.encodePrettily());
					} else {
						rc.response().setStatusCode(200).putHeader("content-type", "application/json")
								.end(JsonObject.mapFrom(res.cause()).encodePrettily());
					}
				});
	}

	public void putLinea(RoutingContext rc) {
		LineaAutobus linea_autobus = Json.decodeValue(rc.getBodyAsString(), LineaAutobus.class);
		mySQLPool.preparedQuery("INSERT INTO linea (idlinea, nombre) VALUES (?,?)",
				Tuple.of(linea_autobus.getIdlinea_autobus(), linea_autobus.getNombre()), handler -> {
					if (handler.succeeded()) {
						System.out.println(handler.result().rowCount());

						long id = handler.result().property(MySQLClient.LAST_INSERTED_ID);
						linea_autobus.setIdlinea_autobus((int) id);

						rc.response().setStatusCode(200).putHeader("content-type", "application/json")
								.end(JsonObject.mapFrom(linea_autobus).encodePrettily());
					} else {
						System.out.println(handler.cause().toString());
						rc.response().setStatusCode(401).putHeader("content-type", "application/json")
								.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
					}
				});
	}

	public void deleteLinea(RoutingContext rc) {
		String id_string = rc.request().getParam("idlinea");
		mySQLPool.query(
				"DELETE linea FROM proyectotussam.dispositivo_autobus WHERE iddispositivo_autobus = " + id_string,
				res -> {
					if (res.succeeded()) {
						rc.response().setStatusCode(200).putHeader("content-type", "application/json")
								.end("<h2>Se ha borrado el objeto con id: " + id_string + "</h2>");
					} else {
						rc.response().setStatusCode(401).putHeader("content-type", "application/json")
								.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
					}
				});
	}

	// ===IMPLEMENTACIÓN DE FUNCIONES DE 'ADMINISTRADOR' ===
	public void getAllSensores(RoutingContext rc) {
		//TODO:
	}

	public void getSensor(RoutingContext rc) {
		//TODO:
	}

	public void putSensor(RoutingContext rc) {
		//TODO:
	}

	public void deleteSensor(RoutingContext rc) {
		//TODO:
	}

	// ===IMPLEMENTACIÓN DE FUNCIONES DE 'DISPOSITIVOPARADA' ===
	public void getAllParadas(RoutingContext rc) {
		mySQLPool.query("SELECT * FROM proyectotussam.dispositivo_parada", res -> {
			if (res.succeeded()) {
				RowSet<Row> tuplas = res.result();
				System.out.println("Nº elementos devueltos = " + tuplas.size());
				JsonArray json_res = new JsonArray();
				for (Row tupla : tuplas) {
					DispositivoParada dispositivo_parada = new DispositivoParada(
							tupla.getInteger("iddispositivo_parada"), tupla.getLong("fecha_creacion"),
							tupla.getInteger("numero_parada"), tupla.getFloat("longitud"), tupla.getFloat("latitud"));
					json_res.add(dispositivo_parada);
				}
				rc.response().setStatusCode(200).putHeader("content-type", "application/json")
						.end(json_res.encodePrettily());
			} else {
				rc.response().setStatusCode(200).putHeader("content-type", "application/json")
						.end(JsonObject.mapFrom(res.cause()).encodePrettily());
			}
		});
	}

	public void getParada(RoutingContext rc) {
		mySQLPool.query("SELECT dispositivo_parada FROM proyectotussam.dispositivo_parada WHERE iddispositivo_parada ="
				+ rc.request().getParam("iddispositivo_parada"), res -> {
					if (res.succeeded()) {
						RowSet<Row> tuplas = res.result();
						System.out.println("Nº elementos devueltos = " + tuplas.size());
						JsonArray json_res = new JsonArray();
						for (Row tupla : tuplas) {
							DispositivoParada dispositivo_parada = new DispositivoParada(
									tupla.getInteger("iddispositivo_parada"), tupla.getLong("fecha_creacion"),
									tupla.getInteger("numero_parada"), tupla.getFloat("longitud"),
									tupla.getFloat("latitud"));
							json_res.add(dispositivo_parada);
						}
						rc.response().setStatusCode(200).putHeader("content-type", "application/json")
								.end(json_res.encodePrettily());
					} else {
						rc.response().setStatusCode(200).putHeader("content-type", "application/json")
								.end(JsonObject.mapFrom(res.cause()).encodePrettily());
					}
				});
	}

	public void putParada(RoutingContext rc) {
		// int idDispositivoParada, long fechaCreacion, int numeroParada, float
		// longitud, float latitud
		DispositivoParada dispositivo_parada = Json.decodeValue(rc.getBodyAsString(), DispositivoParada.class);
		mySQLPool.preparedQuery(
				"INSERT INTO linea (iddispositivo_parada, fecha_creacion, numero_parada, longitud, latitud)"
						+ " VALUES (?, ?, ?, ?, ?)",
				Tuple.of(dispositivo_parada.getIdDispositivoParada(), dispositivo_parada.getFechaCreacion(),
						dispositivo_parada.getNumeroParada(), dispositivo_parada.getLongitud(),
						dispositivo_parada.getLatitud()),
				handler -> {
					if (handler.succeeded()) {
						System.out.println(handler.result().rowCount());

						long id = handler.result().property(MySQLClient.LAST_INSERTED_ID);
						dispositivo_parada.setIdDispositivoParada((int) id);

						rc.response().setStatusCode(200).putHeader("content-type", "application/json")
								.end(JsonObject.mapFrom(dispositivo_parada).encodePrettily());
					} else {
						System.out.println(handler.cause().toString());
						rc.response().setStatusCode(401).putHeader("content-type", "application/json")
								.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
					}
				});
	}

	public void deleteParada(RoutingContext rc) {
		String id_string = rc.request().getParam("iddispositivo_parada");
		mySQLPool.query("DELETE dispositivo_parada FROM proyectotussam.dispositivo_parada WHERE iddispositivo_parada = "
				+ id_string, res -> {
					if (res.succeeded()) {
						rc.response().setStatusCode(200).putHeader("content-type", "application/json")
								.end("<h2>Se ha borrado el objeto con id: " + id_string + "</h2>");
					} else {
						rc.response().setStatusCode(401).putHeader("content-type", "application/json")
								.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
					}
				});
	}

}
