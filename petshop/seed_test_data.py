#!/usr/bin/env python3
import os
from contextlib import closing
from dotenv import load_dotenv
import pymysql
import bcrypt

load_dotenv()

MYSQL_HOST = os.getenv("MYSQL_HOST", "127.0.0.1")
MYSQL_PORT = int(os.getenv("MYSQL_PORT", "3306"))
MYSQL_DATABASE = os.getenv("MYSQL_DATABASE")
MYSQL_USER = os.getenv("MYSQL_USER")
MYSQL_PASSWORD = os.getenv("MYSQL_PASSWORD")

if not all([MYSQL_DATABASE, MYSQL_USER, MYSQL_PASSWORD]):
    raise SystemExit("Faltan variables en .env (MYSQL_DATABASE, MYSQL_USER, MYSQL_PASSWORD). Opcional: MYSQL_HOST, MYSQL_PORT.")

print(f"Conectando a MySQL {MYSQL_HOST}:{MYSQL_PORT} / db={MYSQL_DATABASE} ...")

# -----------------------
# Datos base (en claro)
# -----------------------
roles = [
    (1, "Cliente"),
    (2, "Admin"),
]

# usuarios en texto plano: (id, nombre, apellido, email, password_plain, direccion, telefono, rol_id)
usuarios_plain = [
    (1, "Franco", "Miño", "admin@petshop.com", "admin123", "Av. Siempre Viva 742", "1134567890", 2),
    (2, "Marta", "Lopez", "marta@cliente.com", "abcd", "Calle Falsa 123", "1145678901", 1),
    (3, "Luis", "Gómez", "luis@cliente.com", "pass", "Belgrano 450", "1178901234", 1),
    (4, "Ana", "Ruiz", "ana@cliente.com", "secret1", "San Martín 200", "1150011223", 1),
    (5, "Diego", "Fernández", "diego@cliente.com", "hunter2", "Belgrano 900", "1167788990", 1),
]

categorias = [
    (1, "Perros", "Productos para perros: alimento, juguetes, camas y más."),
    (2, "Gatos", "Productos para gatos: rascadores, comida, accesorios y más."),
]

# Productos ampliados (id, nombre, descripcion, precio, stock, categoria_id, usuario_id, image_url)
productos = [
    (1, "Alimento Premium Perro 15kg", "Bolsa de 15kg para perros adultos", 25000, 40, 1, 2, "https://example.com/perro_alimento.jpg"),
    (2, "Cama Suave para Perro M", "Cama acolchada lavable tamaño mediano", 18000, 15, 1, 2, "https://example.com/cama_perro.jpg"),
    (3, "Pelota de Goma", "Juguete resistente para perros medianos", 4500, 60, 1, 2, "https://example.com/pelota_perro.jpg"),
    (4, "Correa Nylon 2m", "Correa resistente para paseos", 3500, 50, 1, 2, "https://example.com/correa_perro.jpg"),
    (5, "Shampoo Antipulgas", "Shampoo para perros antipulgas 500ml", 4200, 30, 1, 2, "https://example.com/shampoo_perro.jpg"),
    (6, "Rascador Deluxe 1.5m", "Rascador con hamaca para gatos", 23000, 10, 2, 2, "https://example.com/rascador_gato.jpg"),
    (7, "Arena Sanitaria 10kg", "Arena aglutinante para gatos", 9000, 30, 2, 2, "https://example.com/arena_gato.jpg"),
    (8, "Comedero Doble Acero", "Comedero de acero inoxidable antideslizante", 7500, 25, 2, 2, "https://example.com/comedero_gato.jpg"),
    (9, "Rueda Interactiva", "Juguete interactivo para gatos", 3200, 40, 2, 2, "https://example.com/juguete_gato.jpg"),
    (10, "Transportadora M", "Transportadora con ventilación mediana", 15500, 8, 2, 2, "https://example.com/transportadora.jpg"),
    (11, "Snack Saludable Perro", "Pack 30 snacks bajos en grasa", 2800, 100, 1, 2, "https://example.com/snack_perro.jpg"),
    (12, "Cuchillo para Uñas", "Corta uñas para mascotas con protección", 1200, 75, 1, 2, "https://example.com/cortauñas.jpg"),
]

# Pedidos ampliados: (id, estado, fecha_pedido_literal, precio_total, usuario_id)
pedidos = [
    (1, "PENDIENTE", "NOW() - INTERVAL 5 DAY", 32000, 2),
    (2, "ENTREGADO", "NOW() - INTERVAL 2 DAY",  4500, 3),
    (3, "ENVIADO", "NOW() - INTERVAL 1 DAY",  2800, 5),
]

# Detalles ampliados: (id, cantidad, precio_subtotal, pedido_id, producto_id)
detalles = [
    (1, 1, 25000, 1, 1),
    (2, 1, 7000, 1, 8),
    (3, 1, 4500, 2, 3),
    (4, 2, 5600, 3, 11),  # 2 x snack
]

# Facturas ampliadas: (id, fecha_emision_literal, metodo_pago, total, pedido_id)
# metodo_pago debe ser uno de: EFECTIVO,TARJETA_CREDITO,TARJETA_DEBITO,TRANSFERENCIA_BANCARIA
facturas = [
    (1, "NOW() - INTERVAL 4 DAY", "TARJETA_CREDITO",       32000, 1),
    (2, "NOW() - INTERVAL 1 DAY", "EFECTIVO",               4500,  2),
    (3, "NOW()",                  "TARJETA_DEBITO",         2800,  3),
]

# -----------------------
# Helpers
# -----------------------
def hash_password_bcrypt(plain: str) -> str:
    """Devuelve bcrypt hash (utf-8 string)."""
    hashed = bcrypt.hashpw(plain.encode("utf-8"), bcrypt.gensalt())
    return hashed.decode("utf-8")

def insert_rows(cur, table, cols, rows, literal_cols=None):
    """
    Inserta filas con ON DUPLICATE KEY UPDATE id=id (no-ops).
    literal_cols: set con nombres de columnas que llegan como literales SQL (e.g. NOW() - INTERVAL 2 DAY).
    """
    if not rows:
        return 0
    literal_cols = literal_cols or set()
    idx_literals = {cols.index(c) for c in literal_cols if c in cols}

    cols_sql = ", ".join(f"`{c}`" for c in cols)

    values_sql_parts = []
    params = []
    for row in rows:
        parts = []
        for i, v in enumerate(row):
            if i in idx_literals:
                parts.append(str(v))  # literal SQL (no placeholder)
            else:
                parts.append("%s")
                params.append(v)
        values_sql_parts.append("(" + ", ".join(parts) + ")")

    values_sql = ",\n".join(values_sql_parts)
    sql = f"INSERT INTO `{table}` ({cols_sql}) VALUES {values_sql} ON DUPLICATE KEY UPDATE `id`=`id`;"

    cur.execute(sql, params)
    return cur.rowcount

# -----------------------
# Main: prepara usuarios con hash y ejecuta inserts
# -----------------------
def main():
    # hasheamos las contraseñas antes de insertar
    usuarios = []
    for u in usuarios_plain:
        uid, nombre, apellido, email, pwd_plain, direccion, telefono, rol_id = u
        pwd_hash = hash_password_bcrypt(pwd_plain)
        usuarios.append((uid, nombre, apellido, email, pwd_hash, direccion, telefono, rol_id))

    with closing(pymysql.connect(
        host=MYSQL_HOST,
        port=MYSQL_PORT,
        user=MYSQL_USER,
        password=MYSQL_PASSWORD,
        database=MYSQL_DATABASE,
        charset="utf8mb4",
        autocommit=False,
    )) as conn, conn.cursor() as cur:

        inserted = {}

        # Orden por dependencias
        inserted["rol"] = insert_rows(cur, "rol", ["id", "nombre"], roles)
        inserted["usuario"] = insert_rows(cur, "usuario",
            ["id", "nombre", "apellido", "email", "password", "direccion", "telefono", "rol_id"],
            usuarios
        )
        inserted["categoria"] = insert_rows(cur, "categoria",
            ["id", "nombre_categoria", "descripcion"],
            categorias
        )
        inserted["producto"] = insert_rows(cur, "producto",
            ["id", "nombre", "descripcion", "precio", "stock", "categoria_id", "usuario_id", "image_url"],
            productos
        )
        inserted["pedido"] = insert_rows(cur, "pedido",
            ["id", "estado", "fecha_pedido", "precio_total", "usuario_id"],
            pedidos,
            literal_cols={"fecha_pedido"}
        )
        inserted["detalle_pedido"] = insert_rows(cur, "detalle_pedido",
            ["id", "cantidad", "precio_subtotal", "pedido_id", "producto_id"],
            detalles
        )
        inserted["factura"] = insert_rows(cur, "factura",
            ["id", "fecha_emision", "metodo_pago", "total", "pedido_id"],
            facturas,
            literal_cols={"fecha_emision"}
        )

        conn.commit()
        print("Inserciones realizadas (ON DUPLICATE KEY):")
        for k, v in inserted.items():
            print(f"  - {k}: {v} filas (intento de inserción)")

if __name__ == "__main__":
    try:
        main()
    except Exception as e:
        print(f"Error: {e}")
        raise
