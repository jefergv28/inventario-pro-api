// Espera a que el DOM se haya cargado completamente antes de ejecutar el script
document.addEventListener("DOMContentLoaded", function () {
  console.log("Cargando productos...");
  cargarProductos(); // Llama a la función para obtener y mostrar los productos en la tabla

  // Agrega un evento al formulario para capturar la acción de enviar (submit)
  document
    .getElementById("productoForm")
    .addEventListener("submit", function (event) {
      event.preventDefault(); // Evita que la página se recargue al enviar el formulario
      agregarProducto(); // Llama a la función para agregar un nuevo producto
    });
});

// Función para obtener y mostrar los productos desde el backend
function cargarProductos() {
  fetch("http://localhost:8000/productos") // Realiza una petición GET al backend
    .then((response) => response.json()) // Convierte la respuesta en un objeto JSON
    .then((data) => {
      console.log("Productos obtenidos:", data);

      let tbody = document.getElementById("productosTable"); // Selecciona el cuerpo de la tabla
      tbody.innerHTML = ""; // Limpia la tabla antes de actualizar los datos

      // Recorre cada producto y crea una fila en la tabla
      data.forEach((producto) => {
        let row = `
          <tr>
            <td >${producto.id}</td>
            <td>${producto.nombreProducto}</td>
            <td>${producto.cantidadProducto}</td>
            <td>${producto.precioProducto}</td>
            <td>
              <button class="btn btn-danger" onclick="eliminarProducto(${producto.id})">Eliminar</button>
            </td>
          </tr>
        `;
        tbody.innerHTML += row; // Agrega la fila a la tabla
      });
    })
    .catch((error) => console.error("Error al cargar productos:", error)); // Muestra errores en la consola si la petición falla
}

// Función para agregar un nuevo producto al backend
function agregarProducto() {
  // Obtiene los valores ingresados en el formulario
  let nombre = document.getElementById("nombre").value;
  let cantidad = parseInt(document.getElementById("cantidad").value);
  let precio = parseFloat(document.getElementById("precio").value);

  // Crea un objeto con los datos del producto
  let producto = {
    nombreProducto: nombre,
    cantidadProducto: cantidad,
    precioProducto: precio,
  };

  // Realiza una petición POST para enviar el nuevo producto al backend
  fetch("http://localhost:8000/productos", {
    method: "POST",
    headers: { "Content-Type": "application/json" }, // Especifica que se enviará JSON
    body: JSON.stringify(producto), // Convierte el objeto en formato JSON para enviarlo
  })
    .then((response) => {
      if (!response.ok) throw new Error("Error en la solicitud"); // Manejo de errores si la petición falla
      return response.json();
    })
    .then(() => {
      cargarProductos(); // Vuelve a cargar la lista de productos para actualizar la tabla
      document.getElementById("productoForm").reset(); // Limpia el formulario después de agregar el producto
    })
    .catch((error) => console.error("Error al agregar producto:", error));
}

// Función para eliminar un producto por su ID
function eliminarProducto(id) {
  // Realiza una petición DELETE al backend con el ID del producto a eliminar
  fetch(`http://localhost:8000/productos/${id}`, { method: "DELETE" })
    .then((response) => {
      if (!response.ok) throw new Error("Error en la solicitud"); // Manejo de errores si la petición falla
      return response.text();
    })
    .then(() => cargarProductos()) // Vuelve a cargar la lista de productos para actualizar la tabla
    .catch((error) => console.error("Error al eliminar producto:", error));
}
