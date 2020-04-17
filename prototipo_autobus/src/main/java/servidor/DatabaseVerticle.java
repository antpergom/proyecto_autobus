package servidor;

import clases.Administrador;
import clases.DispositivoAutobus;
import clases.DispositivoParada;
import clases.Dispositivoparadadispositivoautobus;
import clases.Dispositivoparadalinea;
import clases.GPS;
import clases.LineaAutobus;
import clases.SensorHumedad;
import clases.SensorProximidad;
import clases.SensorTemperatura;
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
				.setDatabase("daddatabase").setUser("root").setPassword("root");	//TODO: cambiar la contraseña para acceder
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
		router.put("/tussam_api/administradores").handler(this::putAdministrador);
		router.delete("/tussam_api/administradores/:idadministrador").handler(this::deleteAdministrador);

		// dispositivo_autobus -> tabla relacion autobus-linea (añadir esta inserción en
		// los métodos)
		// ===FUNCIONES DE 'DISPOSITIVOAUTOBUS'===
		router.get("/tussam_api/dispositivos_autobus").handler(this::getAllDispositivosAutobus);
		router.get("/tussam_api/dispositivos_autobus/:iddispositivo_autobus").handler(this::getDispositivoAutobus);
		router.put("/tussam_api/dispositivos_autobus").handler(this::putDispositivoAutobus);
		router.delete("/tussam_api/dispositivos_autobus/:iddispositivoautobus").handler(this::deleteDispositivoAutobus);

		// ===FUNCIONES DE 'LINEA'===
		router.get("/tussam_api/lineas").handler(this::getAllLineas);
		router.get("/tussam_api/lineas/:idlinea").handler(this::getLinea);
		router.put("/tussam_api/lineas").handler(this::putLinea);
		router.delete("/tussam_api/lineas/:idlinea").handler(this::deleteLinea);

		// ===FUNCIONES DE 'Sensores'===
		router.get("/tussam_api/sensores/:tipo").handler(this::getAllSensoresTipo);
		router.get("/tussam_api/sensores/:tipo/:idsensor").handler(this::getSensor);
		router.put("/tussam_api/sensores/:tipo").handler(this::putSensor);
		router.delete("/tussam_api/sensores/:tipo/:idsensor").handler(this::deleteSensor);

		// parada-> tabla relación parada-linea (añadir en los métodos)
		// ===FUNCIONES DE 'PARADA'===
		router.get("/tussam_api/dispositivos_parada").handler(this::getAllParadas);
		router.get("/tussam_api/dispositivos_parada/:idparada").handler(this::getParada);
		router.put("/tussam_api/dispositivos_parada").handler(this::putParada);
		router.delete("/tussam_api/dispositivos_parada/:iddispositivo_parada").handler(this::deleteParada);

		// ===FUNCIONES DE 'PARADA-LINEA'===
		router.get("/tussam_api/dispositivosparada-linea").handler(this::getAllDispositivosparadalinea);
		router.get("/tussam_api/dispositivosparada-linea/:iddispositivo_parada-linea_autobus")
				.handler(this::getDispositivoparadalinea);
		router.put("/tussam_api/dispositivosparada-linea/").handler(this::putDispositivoparadalinea);
		router.delete("/tussam_api/dispositivosparada-linea/:iddispositivosparadalinea")
				.handler(this::deleteDispositivoparadalinea);

		// ===FUNCIONES DE 'DISPOSITIVOPARADA-ISPOSITIVOAUTOBUS'===
		router.get("/tussam_api/dispositivosparada-dispositivosautobus")
				.handler(this::getAllDispositivosparadadispositivosautobus);
		router.get("/tussam_api/dispositivosparada-dispositivosautobus/:iddispositivo_paradaDispositivo_autobus")
				.handler(this::getDispositivoparadadispositivoautobus);
		router.put("/tussam_api/dispositivosparada-dispositivosautobus/")
				.handler(this::putDispositivoparadadispositivoautobus);
		router.delete("/tussam_api/dispositivosparada-dispositivosautobus/:iddispositivosparada-dispositivosautobus")
				.handler(this::deleteDispositivoparadadispositivoautobus);
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
					result.add(JsonObject.mapFrom(new Administrador(row.getInteger("idadministrador"),
							row.getString("dni"), row.getString("nombre"), row.getString("apellidos"),
							row.getLong("fecha_creacion"))));
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
							result.add(JsonObject.mapFrom(new Administrador(row.getInteger("idadministrador"),
									row.getString("dni"), row.getString("nombre"), row.getString("apellidos"),
									row.getLong("fecha_creacion"))));
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
				"INSERT INTO administrador (dni, nombre, apellidos,fecha_creacion) VALUES (?,?,?,?)",
				Tuple.of(administrador.getDni(), administrador.getNombre(), administrador.getApellidos(),
						administrador.getfecha_creacion()),
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
		mySQLPool.preparedQuery("INSERT INTO linea (nombre) VALUES (?)", Tuple.of(linea_autobus.getNombre()),
				handler -> {
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

	// ===IMPLEMENTACIÓN DE FUNCIONES DE 'SENSOR' ===
	public void getAllSensoresTipo(RoutingContext rc) {
		String tipo_sensor = rc.request().getParam("tipo");
		switch (tipo_sensor) {
		case ("DISTANCIA"):
			mySQLPool.query("SELECT * FROM proyectotussam.sensor_proximidad", res -> {
				if (res.succeeded()) {
					RowSet<Row> resultSet = res.result();
					JsonArray result = new JsonArray();
					for (Row row : resultSet) {
						// IMPORTANTE: Sacar los valores de las filas, con los atributos de la
						// BBDD,segun el orden del constructor
						// de la clase DispositivoAutobus
						result.add(JsonObject.mapFrom(new SensorProximidad(row.getInteger("idsensor_proximidad"),
								row.getString("nombre_modelo"), row.getFloat("valor"), row.getFloat("precision"),
								row.getInteger("iddispositivo_autobus"))));
					}

					rc.response().setStatusCode(200).putHeader("content-type", "application/json")
							.end(result.encodePrettily());
				} else {
					rc.response().setStatusCode(401).putHeader("content-type", "application/json")
							.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
				}
			});
			break;
		case ("TEMPERATURA"):
			mySQLPool.query("SELECT * FROM proyectotussam.sensor_temperatura ", res -> {
				if (res.succeeded()) {
					RowSet<Row> resultSet = res.result();
					JsonArray result = new JsonArray();
					for (Row row : resultSet) {
						// IMPORTANTE: Sacar los valores de las filas, con los atributos de la
						// BBDD,segun el orden del constructor
						// de la clase DispositivoAutobus
						result.add(JsonObject.mapFrom(new SensorTemperatura(row.getInteger("idsensor_temperatura"),
								row.getString("nombre_modelo"), row.getFloat("valor"), row.getFloat("precision"),
								row.getInteger("iddispositivo_autobus"))));
					}

					rc.response().setStatusCode(200).putHeader("content-type", "application/json")
							.end(result.encodePrettily());
				} else {
					rc.response().setStatusCode(401).putHeader("content-type", "application/json")
							.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
				}
			});

			break;

		case ("HUMEDAD"):
			mySQLPool.query("SELECT * FROM proyectotussam.sensor_humedad ", res -> {
				if (res.succeeded()) {
					RowSet<Row> resultSet = res.result();
					JsonArray result = new JsonArray();
					for (Row row : resultSet) {
						// IMPORTANTE: Sacar los valores de las filas, con los atributos de la
						// BBDD,segun el orden del constructor
						// de la clase DispositivoAutobus
						result.add(JsonObject.mapFrom(new SensorHumedad(row.getInteger("idsensor_humedad"),
								row.getString("nombre_modelo"), row.getFloat("valor"), row.getFloat("precision"),
								row.getInteger("iddispositivo_autobus"))));
					}

					rc.response().setStatusCode(200).putHeader("content-type", "application/json")
							.end(result.encodePrettily());
				} else {
					rc.response().setStatusCode(401).putHeader("content-type", "application/json")
							.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
				}
			});

			break;
		case ("GPS"):
			mySQLPool.query("SELECT * FROM proyectotussam.sensor_gps ", res -> {
				if (res.succeeded()) {
					RowSet<Row> resultSet = res.result();
					JsonArray result = new JsonArray();
					for (Row row : resultSet) {
						// IMPORTANTE: Sacar los valores de las filas, con los atributos de la
						// BBDD,segun el orden del constructor
						// de la clase DispositivoAutobus
						result.add(JsonObject
								.mapFrom(new GPS(row.getInteger("idsensor_gps"), row.getString("nombre_modelo"),
										row.getFloat("valor_latitud"), row.getFloat("valor_longitud"),
										row.getFloat("precision"), row.getInteger("iddispositivo_autobus"))));
					}

					rc.response().setStatusCode(200).putHeader("content-type", "application/json")
							.end(result.encodePrettily());
				} else {
					rc.response().setStatusCode(401).putHeader("content-type", "application/json")
							.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
				}
			});
			break;
		default:
			rc.response().setStatusCode(401).putHeader("content-type", "application/json")
					.end(" <h2>Ha introducido mal el parametro tipo");
			break;
		}

	}

	public void getSensor(RoutingContext rc) {
		String tipo_sensor = rc.request().getParam("tipo");
		switch (tipo_sensor) {
		case ("DISTANCIA"):
			mySQLPool.query("SELECT * FROM proyectotussam.sensor_proximidad WHERE idsensor_proximidad = "
					+ rc.request().getParam("idsensor"), res -> {
						if (res.succeeded()) {
							RowSet<Row> resultSet = res.result();
							JsonArray result = new JsonArray();
							for (Row row : resultSet) {
								// IMPORTANTE: Sacar los valores de las filas, con los atributos de la
								// BBDD,segun el orden del constructor
								// de la clase DispositivoAutobus
								result.add(
										JsonObject.mapFrom(new SensorProximidad(row.getInteger("idsensor_proximidad"),
												row.getString("nombre_modelo"), row.getFloat("valor"),
												row.getFloat("precision"), row.getInteger("iddispositivo_autobus"))));
							}

							rc.response().setStatusCode(200).putHeader("content-type", "application/json")
									.end(result.encodePrettily());
						} else {
							rc.response().setStatusCode(401).putHeader("content-type", "application/json")
									.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
						}
					});
			break;
		case ("TEMPERATURA"):
			mySQLPool.query("SELECT * FROM proyectotussam.sensor_temperatura WHERE idsensor_temperatura = "
					+ rc.request().getParam("idsensor"), res -> {
						if (res.succeeded()) {
							RowSet<Row> resultSet = res.result();
							JsonArray result = new JsonArray();
							for (Row row : resultSet) {
								// IMPORTANTE: Sacar los valores de las filas, con los atributos de la
								// BBDD,segun el orden del constructor
								// de la clase DispositivoAutobus
								result.add(
										JsonObject.mapFrom(new SensorTemperatura(row.getInteger("idsensor_temperatura"),
												row.getString("nombre_modelo"), row.getFloat("valor"),
												row.getFloat("precision"), row.getInteger("iddispositivo_autobus"))));
							}

							rc.response().setStatusCode(200).putHeader("content-type", "application/json")
									.end(result.encodePrettily());
						} else {
							rc.response().setStatusCode(401).putHeader("content-type", "application/json")
									.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
						}
					});

			break;

		case ("HUMEDAD"):
			mySQLPool.query("SELECT * FROM proyectotussam.sensor_humedad WHERE idsensor_humedad = "
					+ rc.request().getParam("idsensor"), res -> {
						if (res.succeeded()) {
							RowSet<Row> resultSet = res.result();
							JsonArray result = new JsonArray();
							for (Row row : resultSet) {
								// IMPORTANTE: Sacar los valores de las filas, con los atributos de la
								// BBDD,segun el orden del constructor
								// de la clase DispositivoAutobus
								result.add(JsonObject.mapFrom(new SensorHumedad(row.getInteger("idsensor_humedad"),
										row.getString("nombre_modelo"), row.getFloat("valor"),
										row.getFloat("precision"), row.getInteger("iddispositivo_autobus"))));
							}

							rc.response().setStatusCode(200).putHeader("content-type", "application/json")
									.end(result.encodePrettily());
						} else {
							rc.response().setStatusCode(401).putHeader("content-type", "application/json")
									.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
						}
					});

			break;
		case ("GPS"):
			mySQLPool.query(
					"SELECT * FROM proyectotussam.sensor_gps WHERE idsensor_gps = " + rc.request().getParam("idsensor"),
					res -> {
						if (res.succeeded()) {
							RowSet<Row> resultSet = res.result();
							JsonArray result = new JsonArray();
							for (Row row : resultSet) {
								// IMPORTANTE: Sacar los valores de las filas, con los atributos de la
								// BBDD,segun el orden del constructor
								// de la clase DispositivoAutobus
								result.add(JsonObject
										.mapFrom(new GPS(row.getInteger("idsensor_gps"), row.getString("nombre_modelo"),
												row.getFloat("valor_latitud"), row.getFloat("valor_longitud"),
												row.getFloat("precision"), row.getInteger("iddispositivo_autobus"))));
							}

							rc.response().setStatusCode(200).putHeader("content-type", "application/json")
									.end(result.encodePrettily());
						} else {
							rc.response().setStatusCode(401).putHeader("content-type", "application/json")
									.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
						}
					});
			break;
		default:
			rc.response().setStatusCode(401).putHeader("content-type", "application/json")
					.end(" <h2>Ha introducido mal el parametro tipo");
			break;
		}
	}

	public void putSensor(RoutingContext rc) {
		String tipo_sensor = rc.request().getParam("tipo");
		switch (tipo_sensor) {
		case ("DISTANCIA"):
			SensorProximidad sensorProximidad = Json.decodeValue(rc.getBodyAsString(), SensorProximidad.class);
			mySQLPool.preparedQuery(
					// Aqui si que utilizamos los nombres dados en la BBDD
					"INSERT INTO sensor_proximidad (nombre_modelo, valor, precision,iddispositivo_autobus) VALUES (?,?,?,?)",
					Tuple.of(sensorProximidad.getNombre(), sensorProximidad.getValor(), sensorProximidad.getPrecision(),
							sensorProximidad.getIdDispositivoAutobus()),
					handler -> {
						if (handler.succeeded()) {
							System.out.println(handler.result().rowCount());

							// Se devuleve el objeto que ha introducido el usario pero modificado, con el id
							// que la bbdd
							// ha genereado para el
							long id = handler.result().property(MySQLClient.LAST_INSERTED_ID);
							sensorProximidad.setIdsensorproximidad((int) id);

							rc.response().setStatusCode(200).putHeader("content-type", "application/json")
									.end(JsonObject.mapFrom(sensorProximidad).encodePrettily());
						} else {
							System.out.println(handler.cause().toString());
							rc.response().setStatusCode(401).putHeader("content-type", "application/json")
									.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
						}
					});
			break;
		case ("TEMPERATURA"):
			SensorTemperatura sensorTemperatura = Json.decodeValue(rc.getBodyAsString(), SensorTemperatura.class);
			mySQLPool.preparedQuery(
					// Aqui si que utilizamos los nombres dados en la BBDD
					"INSERT INTO sensor_temperatura (nombre_modelo, valor, precision,iddispositivo_autobus) VALUES (?,?,?,?)",
					Tuple.of(sensorTemperatura.getNombre(), sensorTemperatura.getValor(),
							sensorTemperatura.getPrecision(), sensorTemperatura.getIdDispositivoAutobus()),
					handler -> {
						if (handler.succeeded()) {
							System.out.println(handler.result().rowCount());

							// Se devuleve el objeto que ha introducido el usario pero modificado, con el id
							// que la bbdd
							// ha genereado para el
							long id = handler.result().property(MySQLClient.LAST_INSERTED_ID);
							sensorTemperatura.setIdsensortemperatura((int) id);

							rc.response().setStatusCode(200).putHeader("content-type", "application/json")
									.end(JsonObject.mapFrom(sensorTemperatura).encodePrettily());
						} else {
							System.out.println(handler.cause().toString());
							rc.response().setStatusCode(401).putHeader("content-type", "application/json")
									.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
						}
					});
			break;
		case ("HUMEDAD"):
			SensorHumedad sensorHumedad = Json.decodeValue(rc.getBodyAsString(), SensorHumedad.class);
			mySQLPool.preparedQuery(
					// Aqui si que utilizamos los nombres dados en la BBDD
					"INSERT INTO sensor_humedad (nombre_modelo, valor, precision,iddispositivo_autobus) VALUES (?,?,?,?)",
					Tuple.of(sensorHumedad.getNombre(), sensorHumedad.getValor(), sensorHumedad.getPrecision(),
							sensorHumedad.getIdDispositivoAutobus()),
					handler -> {
						if (handler.succeeded()) {
							System.out.println(handler.result().rowCount());

							// Se devuleve el objeto que ha introducido el usario pero modificado, con el id
							// que la bbdd
							// ha genereado para el
							long id = handler.result().property(MySQLClient.LAST_INSERTED_ID);
							sensorHumedad.setIdsensorhumedad((int) id);

							rc.response().setStatusCode(200).putHeader("content-type", "application/json")
									.end(JsonObject.mapFrom(sensorHumedad).encodePrettily());
						} else {
							System.out.println(handler.cause().toString());
							rc.response().setStatusCode(401).putHeader("content-type", "application/json")
									.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
						}
					});
			break;
		case ("GPS"):
			GPS gps = Json.decodeValue(rc.getBodyAsString(), GPS.class);
			mySQLPool.preparedQuery(
					// Aqui si que utilizamos los nombres dados en la BBDD
					"INSERT INTO sensor_gps (nombre_modelo, valor_latitud, valor_longitud ,precision,iddispositivo_autobus) VALUES (?,?,?,?,?)",
					Tuple.of(gps.getNombre(), gps.getValorLatitud(), gps.getValorLongitud(), gps.getPrecision(),
							gps.getIdDispositivoAutobus()),
					handler -> {
						if (handler.succeeded()) {
							System.out.println(handler.result().rowCount());

							// Se devuleve el objeto que ha introducido el usario pero modificado, con el id
							// que la bbdd
							// ha genereado para el
							long id = handler.result().property(MySQLClient.LAST_INSERTED_ID);
							gps.setIdsensorgps((int) id);

							rc.response().setStatusCode(200).putHeader("content-type", "application/json")
									.end(JsonObject.mapFrom(gps).encodePrettily());
						} else {
							System.out.println(handler.cause().toString());
							rc.response().setStatusCode(401).putHeader("content-type", "application/json")
									.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
						}
					});

		}
	}

	public void deleteSensor(RoutingContext rc) {
		String tipo_sensor = rc.request().getParam("tipo");
		switch (tipo_sensor) {
		case ("DISTANCIA"):
			mySQLPool.query("DELETE FROM proyectotussam.sensor_proximidad WHERE idsensor_proximidad = "
					+ rc.request().getParam("idsensor"), res -> {
						if (res.succeeded()) {

							rc.response().setStatusCode(200).putHeader("content-type", "application/json")
									.end("<h2>Tu operacion se ha realizado correctamente</h2>");
						} else {
							rc.response().setStatusCode(401).putHeader("content-type", "application/json")
									.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
						}
					});
			break;
		case ("TEMPERATURA"):
			mySQLPool.query("DELETE FROM proyectotussam.sensor_temperatura WHERE idsensor_temperatura = "
					+ rc.request().getParam("idsensor"), res -> {
						if (res.succeeded()) {

							rc.response().setStatusCode(200).putHeader("content-type", "application/json")
									.end("<h2>Tu operacion se ha realizado correctamente</h2>");
						} else {
							rc.response().setStatusCode(401).putHeader("content-type", "application/json")
									.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
						}
					});
			break;
		case ("HUMEDAD"):
			mySQLPool.query("DELETE FROM proyectotussam.sensor_humedad WHERE idsensor_humedad = "
					+ rc.request().getParam("idsensor"), res -> {
						if (res.succeeded()) {

							rc.response().setStatusCode(200).putHeader("content-type", "application/json")
									.end("<h2>Tu operacion se ha realizado correctamente</h2>");
						} else {
							rc.response().setStatusCode(401).putHeader("content-type", "application/json")
									.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
						}
					});
			break;
		case ("GPS"):
			mySQLPool.query(
					"DELETE FROM proyectotussam.sensor_gps WHERE idsensor_gps = " + rc.request().getParam("idsensor"),
					res -> {
						if (res.succeeded()) {

							rc.response().setStatusCode(200).putHeader("content-type", "application/json")
									.end("<h2>Tu operacion se ha realizado correctamente</h2>");
						} else {
							rc.response().setStatusCode(401).putHeader("content-type", "application/json")
									.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
						}
					});
			break;
		}
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
		/*
		 * int idDispositivoParada, long fechaCreacion, int numeroParada, float
		 * longitud, float latitud
		 * 
		 */ DispositivoParada dispositivo_parada = Json.decodeValue(rc.getBodyAsString(), DispositivoParada.class);
		mySQLPool.preparedQuery(
				"INSERT INTO linea (fecha_creacion, numero_parada, longitud, latitud)" + " VALUES (?, ?, ?, ?, ?)",
				Tuple.of(dispositivo_parada.getFechaCreacion(), dispositivo_parada.getNumeroParada(),
						dispositivo_parada.getLongitud(), dispositivo_parada.getLatitud()),
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

	// ===IMPLEMENTACIÓN DE FUNCIONES DE 'DISPOSITIVOPARADA-LINEA' ===
	public void getAllDispositivosparadalinea(RoutingContext rc) {
		mySQLPool.query("SELECT * FROM proyectotussam.dispositivo_parada-linea", res -> {
			if (res.succeeded()) {
				RowSet<Row> resultSet = res.result();
				System.out.println("El número de elementos obtenidos es " + resultSet.size());

				JsonArray result = new JsonArray();
				for (Row row : resultSet) {
					// IMPORTANTE: Sacar los valores de las filas segun el orden del constructor de
					// la clase DispositivoAutobus
					result.add(JsonObject.mapFrom(new Dispositivoparadalinea(
							row.getInteger("iddispositivo_parada-linea_autobus"), row.getLong("fecha_creacion"),
							row.getInteger("iddispositivo_parada"), row.getInteger("idlinea"))));
				}

				rc.response().setStatusCode(200).putHeader("content-type", "application/json")
						.end(result.encodePrettily());
			} else {
				rc.response().setStatusCode(401).putHeader("content-type", "application/json")
						.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
			}
		});
	}

	private void getDispositivoparadalinea(RoutingContext rc) {
		mySQLPool.query(
				"SELECT * FROM proyectotussam.dispositivo_parada-linea WHERE iddispositivo_parada-linea_autobus = "
						+ rc.request().getParam("iddispositivo_parada-linea_autobus"),
				res -> {
					if (res.succeeded()) {
						RowSet<Row> resultSet = res.result();
						JsonArray result = new JsonArray();
						for (Row row : resultSet) {
							// IMPORTANTE: Sacar los valores de las filas, con los atributos de la
							// BBDD,segun el orden del constructor
							// de la clase DispositivoAutobus
							result.add(JsonObject.mapFrom(new Dispositivoparadalinea(
									row.getInteger("iddispositivo_parada-linea_autobus"), row.getLong("fecha_creacion"),
									row.getInteger("iddispositivo_parada"), row.getInteger("idlinea"))));
						}

						rc.response().setStatusCode(200).putHeader("content-type", "application/json")
								.end(result.encodePrettily());
					} else {
						rc.response().setStatusCode(401).putHeader("content-type", "application/json")
								.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
					}
				});
	}

	private void putDispositivoparadalinea(RoutingContext routingContext) {
		Dispositivoparadalinea dispositivoparadalinea = Json.decodeValue(routingContext.getBodyAsString(),
				Dispositivoparadalinea.class);
		mySQLPool.preparedQuery(
				// Aqui si que utilizamos los nombres dados en la BBDD
				"INSERT INTO dispositivo_parada-linea (fecha_creacion, iddispositivo_parada, idlinea) VALUES (?,?,?)",
				Tuple.of(dispositivoparadalinea.getFecha_creacion(), dispositivoparadalinea.getIddispositivo_parada(),
						dispositivoparadalinea.getIdlinea()),
				handler -> {
					if (handler.succeeded()) {

						long id = handler.result().property(MySQLClient.LAST_INSERTED_ID);
						dispositivoparadalinea.setIddispositivo_paradalinea_autobus((int) id);

						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
								.end(JsonObject.mapFrom(dispositivoparadalinea).encodePrettily());
					} else {
						System.out.println(handler.cause().toString());
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
								.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
					}
				});
	}

	private void deleteDispositivoparadalinea(RoutingContext routingContext) {
		mySQLPool
				.query("DELETE FROM proyectotussam.dispositivo_parada-linea WHERE iddispositivo_parada-linea_autobus = "
						+ routingContext.request().getParam("iddispositivo_parada-linea_autobus"), res -> {
							if (res.succeeded()) {

								routingContext.response().setStatusCode(200)
										.putHeader("content-type", "application/json")
										.end("<h2>Tu operacion se ha realizado correctamente</h2>");
							} else {
								routingContext.response().setStatusCode(401)
										.putHeader("content-type", "application/json")
										.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
							}
						});
	}

	// lo tultimo: IMPLEMENTACION DE FUNCIONES DE
	// 'DISPOSITIVOPARADA-DISPOSITIVOAUTOBUS'
	public void getAllDispositivosparadadispositivosautobus(RoutingContext rc) {
		mySQLPool.query("SELECT * FROM proyectotussam.dispositivo_parada-dispositivo_autobus", res -> {
			if (res.succeeded()) {
				RowSet<Row> resultSet = res.result();
				System.out.println("El número de elementos obtenidos es " + resultSet.size());

				JsonArray result = new JsonArray();
				for (Row row : resultSet) {
					// IMPORTANTE: Sacar los valores de las filas segun el orden del constructor de
					// la clase DispositivoAutobus
					result.add(JsonObject.mapFrom(new Dispositivoparadadispositivoautobus(
							row.getInteger("iddispositivo_paradaDispositivo_autobus"), row.getLong("fecha_creacion"),
							row.getInteger("iddispositivo_parada"), row.getInteger("iddispositivo_autobus"))));
				}

				rc.response().setStatusCode(200).putHeader("content-type", "application/json")
						.end(result.encodePrettily());
			} else {
				rc.response().setStatusCode(401).putHeader("content-type", "application/json")
						.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
			}
		});
	}

	private void getDispositivoparadadispositivoautobus(RoutingContext rc) {
		mySQLPool.query("SELECT * FROM proyectotussam.dispositivo_parada-dispositivo_autobus WHERE idadministrador = "
				+ rc.request().getParam("iddispositivo_parada-dispositivo_autobus"), res -> {
					if (res.succeeded()) {
						RowSet<Row> resultSet = res.result();
						JsonArray result = new JsonArray();
						for (Row row : resultSet) {
							// IMPORTANTE: Sacar los valores de las filas, con los atributos de la
							// BBDD,segun el orden del constructor
							// de la clase DispositivoAutobus
							result.add(JsonObject.mapFrom(new Dispositivoparadadispositivoautobus(
									row.getInteger("iddispositivo_paradaDispositivo_autobus"),
									row.getLong("fecha_creacion"), row.getInteger("iddispositivo_parada"),
									row.getInteger("iddispositivo_autobus"))));
						}

						rc.response().setStatusCode(200).putHeader("content-type", "application/json")
								.end(result.encodePrettily());
					} else {
						rc.response().setStatusCode(401).putHeader("content-type", "application/json")
								.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
					}
				});
	}

	private void putDispositivoparadadispositivoautobus(RoutingContext routingContext) {
		Dispositivoparadadispositivoautobus dispositivoparadadispositivoautobus = Json
				.decodeValue(routingContext.getBodyAsString(), Dispositivoparadadispositivoautobus.class);
		mySQLPool.preparedQuery(
				// Aqui si que utilizamos los nombres dados en la BBDD
				"INSERT INTO dispositivo_parada-dispositivo_autobus (fecha_creacion, iddispositivo_parada, iddispositivo_autobus) VALUES (?,?,?)",
				Tuple.of(dispositivoparadadispositivoautobus.getFecha_creacion(),
						dispositivoparadadispositivoautobus.getIddispositivo_parada(),
						dispositivoparadadispositivoautobus.getIddispositivo_autobus()),
				handler -> {
					if (handler.succeeded()) {

						long id = handler.result().property(MySQLClient.LAST_INSERTED_ID);
						dispositivoparadadispositivoautobus.setIddispositivo_paradaDispositivo_autobus((int) id);

						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
								.end(JsonObject.mapFrom(dispositivoparadadispositivoautobus).encodePrettily());
					} else {
						System.out.println(handler.cause().toString());
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
								.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
					}
				});
	}

	private void deleteDispositivoparadadispositivoautobus(RoutingContext routingContext) {
		mySQLPool.query(
				"DELETE FROM proyectotussam.dispositivo_parada-dispositivo_autobus WHERE iddispositivo_paradaDispositivo_autobus = "
						+ routingContext.request().getParam("iddispositivo_paradaDispositivo_autobus"),
				res -> {
					if (res.succeeded()) {

						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
								.end("<h2>Tu operacion se ha realizado correctamente</h2>");
					} else {
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
								.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
					}
				});
	}

}
