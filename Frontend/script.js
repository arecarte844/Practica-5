const API_BASE = "http://localhost:8080/api";
const CART_ID = "boda-dani-eliza";
const weddingDate = new Date("2027-06-23T00:00:00");

const timelineData = [
  { year: "2016", image: "assets/images/01_highschool.jpg" },
  { year: "2017", image: "assets/images/02_young_couple.jpg" },
  { year: "2018", image: "assets/images/10_tennis_event.jpg" },
  { year: "2019", image: "assets/images/09_proposal.jpg" },
  { year: "2020", image: "assets/images/12_engagement.jpg" },
  { year: "2021", image: "assets/images/07_lake.jpg" },
  { year: "2022", image: "assets/images/06_rainy_day.jpg" },
  { year: "2023", image: "assets/images/11_graduation.jpg" },
  { year: "2024", image: "assets/images/08_horse_and_dog.jpg" },
  { year: "2025", image: "assets/images/04_formal.jpg" },
  { year: "2026", image: "assets/images/04_formal.jpg" },
  { year: "2027", image: "assets/images/03_college.jpg" }
];

const giftItems = [
  {
    id: 1,
    name: "Cafetera espresso",
    price: 180,
    image: "assets/images/cafetera.jpg",
    description: "Para empezar las mañanas juntos."
  },
  {
    id: 2,
    name: "Vajilla clásica",
    price: 95,
    image: "assets/images/vajilla.jpg",
    description: "Perfecta para comidas especiales."
  },
  {
    id: 3,
    name: "Juego de sábanas",
    price: 75,
    image: "assets/images/sabanas.jpg",
    description: "Un detalle útil para nuestra nueva etapa."
  },
  {
    id: 4,
    name: "Set de copas",
    price: 60,
    image: "assets/images/copas.jpg",
    description: "Para brindar con familia y amigos."
  },
  {
    id: 5,
    name: "Cortador de cesped",
    price: 179,
    image: "assets/images/Lawnmower.png",
    description: "Para tener el jardín impecable."
  },
  {
    id: 6,
    name: "Soplador de hojas",
    price: 160,
    image: "assets/images/Soplador.png",
    description: "Para pasar todas las ojas al vecino."
  },
  {
    id: 7,
    name: "Freidora de aire",
    price: 160,
    image: "assets/images/Freidora.png",
    description: "Para ser eficientes en la cocina."
  },
  {
    id: 8,
    name: "Luz Roja",
    price: 477,
    image: "assets/images/Luz.png",
    description: "Para ser como Marcos LLorente."
  },
   {
    id: 9,
    name: "Barbacoa de gas y carbon",
    price: 270,
    image: "assets/images/grill.png",
    description: "Para vivir el verdadero sueño Americano."
  }
];

let currentTimelineIndex = 0;

function getGiftByIdArticulo(idArticulo) {
  return giftItems.find(item => String(item.id) === String(idArticulo));
}

function getDisplayNameFromArticulo(idArticulo) {
  if (String(idArticulo).startsWith("luna-miel")) {
    return "Aportación luna de miel";
  }

  const gift = getGiftByIdArticulo(idArticulo);
  return gift ? gift.name : `Artículo ${idArticulo}`;
}

function getDisplayImageFromArticulo(idArticulo) {
  if (String(idArticulo).startsWith("luna-miel")) {
    return "assets/images/luna-miel.jpg";
  }

  const gift = getGiftByIdArticulo(idArticulo);
  return gift ? gift.image : "";
}

async function ensureCartExists() {
  try {
    const response = await fetch(`${API_BASE}/carritos`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        idCarrito: CART_ID
      })
    });

    if (response.status === 201 || response.status === 200 || response.status === 409) {
      return;
    }

    throw new Error(`No se pudo asegurar el carrito. Status: ${response.status}`);
  } catch (error) {
    console.error("Error creando carrito:", error);
  }
}

async function fetchCartLines() {
  const response = await fetch(`${API_BASE}/carritos/${CART_ID}/lineas`);
  if (!response.ok) {
    throw new Error(`Error obteniendo líneas. Status: ${response.status}`);
  }
  return await response.json();
}

async function fetchCartInfo() {
  const response = await fetch(`${API_BASE}/carritos/${CART_ID}`);
  if (!response.ok) {
    throw new Error(`Error obteniendo carrito. Status: ${response.status}`);
  }
  return await response.json();
}

async function updateNavCartCount() {
  const navCount = document.getElementById("navCartCount");
  if (!navCount) return;

  try {
    const lineas = await fetchCartLines();
    const totalItems = lineas.reduce((acc, item) => acc + item.numeroUnidades, 0);
    navCount.textContent = totalItems;
  } catch (error) {
    navCount.textContent = "0";
  }
}

async function addToCart(itemId) {
  const product = giftItems.find(item => item.id === itemId);
  if (!product) return;

  await ensureCartExists();

  try {
    const response = await fetch(`${API_BASE}/carritos/${CART_ID}/lineas`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        idArticulo: String(product.id),
        precioUnitario: product.price,
        numeroUnidades: 1
      })
    });

    if (!response.ok) {
      throw new Error(`No se pudo añadir al carrito. Status: ${response.status}`);
    }

    await updateNavCartCount();
    await renderCart();
    await renderCheckoutSummary();
  } catch (error) {
    console.error("Error añadiendo al carrito:", error);
  }
}

async function removeFromCart(idArticulo) {
  try {
    const response = await fetch(`${API_BASE}/carritos/${CART_ID}/lineas/${idArticulo}`, {
      method: "DELETE"
    });

    console.log("DELETE status:", response.status);

    if (!response.ok) {
      throw new Error(`No se pudo eliminar la línea del carrito. Status: ${response.status}`);
    }

    await updateNavCartCount();
    await renderCart();
    await renderCheckoutSummary();
  } catch (error) {
    console.error("Error eliminando del carrito:", error);
  }
}

async function clearCart() {
  try {
    const response = await fetch(`${API_BASE}/carritos/${CART_ID}`, {
      method: "DELETE"
    });

    if (!response.ok) {
      throw new Error(`No se pudo vaciar el carrito. Status: ${response.status}`);
    }

    await ensureCartExists();
    await updateNavCartCount();
    await renderCart();
    await renderCheckoutSummary();
  } catch (error) {
    console.error("Error vaciando carrito:", error);
  }
}

function setupHoneymoonForm() {
  const honeymoonForm = document.getElementById("honeymoonForm");
  if (!honeymoonForm) return;

  honeymoonForm.addEventListener("submit", async function (e) {
    e.preventDefault();

    const amountInput = document.getElementById("honeymoonAmount");
    const amount = Number(amountInput.value);

    if (!amount || amount < 1) return;

    await ensureCartExists();

    try {
      const response = await fetch(`${API_BASE}/carritos/${CART_ID}/lineas`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          idArticulo: `luna-miel-${Date.now()}`,
          precioUnitario: amount,
          numeroUnidades: 1
        })
      });

      if (!response.ok) {
        throw new Error(`No se pudo añadir la aportación. Status: ${response.status}`);
      }

      amountInput.value = "";
      await updateNavCartCount();
      await renderCart();
      await renderCheckoutSummary();
      alert("Aportación añadida al carrito.");
    } catch (error) {
      console.error("Error añadiendo aportación:", error);
    }
  });
}

function renderGiftList() {
  const giftList = document.getElementById("giftList");
  if (!giftList) return;

  giftList.innerHTML = giftItems.map(item => `
    <article class="gift-card">
      <img src="${item.image}" alt="${item.name}" class="gift-image">
      <div class="gift-card-content">
        <h3>${item.name}</h3>
        <p>${item.description}</p>
        <p class="gift-price">${item.price} €</p>
        <button class="btn btn-secondary" type="button" onclick="addToCart(${item.id})">
          Añadir al carrito
        </button>
      </div>
    </article>
  `).join("");
}

async function renderCart() {
  const body = document.getElementById("cartItemsBody");
  const emptyMessage = document.getElementById("emptyCartMessage");
  const itemCount = document.getElementById("cartItemCount");
  const total = document.getElementById("cartTotal");

  if (!body || !emptyMessage || !itemCount || !total) return;

  try {
    const [lineas, carrito] = await Promise.all([
      fetchCartLines(),
      fetchCartInfo()
    ]);

    if (lineas.length === 0) {
      body.innerHTML = "";
      emptyMessage.classList.remove("hidden");
      itemCount.textContent = "0";
      total.textContent = "0.00 €";
      return;
    }

    emptyMessage.classList.add("hidden");

    body.innerHTML = lineas.map(item => `
      <tr>
        <td>${getDisplayNameFromArticulo(item.idArticulo)}</td>
        <td>${item.precioUnitario.toFixed(2)} €</td>
        <td>${item.numeroUnidades}</td>
        <td>${item.costeLinea.toFixed(2)} €</td>
        <td>
          <button class="btn btn-secondary" type="button" onclick="removeFromCart('${item.idArticulo}')">
            Eliminar
          </button>
        </td>
      </tr>
    `).join("");

    const totalItems = lineas.reduce((sum, item) => sum + item.numeroUnidades, 0);
    itemCount.textContent = totalItems;
    total.textContent = `${carrito.totalPrecio.toFixed(2)} €`;
  } catch (error) {
    console.error("Error cargando carrito:", error);
    body.innerHTML = "";
    emptyMessage.classList.remove("hidden");
    itemCount.textContent = "0";
    total.textContent = "0.00 €";
  }
}

async function renderCheckoutSummary() {
  const summary = document.getElementById("checkoutSummary");
  const total = document.getElementById("checkoutTotal");

  if (!summary || !total) return;

  try {
    const [lineas, carrito] = await Promise.all([
      fetchCartLines(),
      fetchCartInfo()
    ]);

    if (lineas.length === 0) {
      summary.innerHTML = "<p>No hay artículos en el carrito.</p>";
      total.textContent = "0 €";
      return;
    }

    summary.innerHTML = lineas.map(item => `
      <p>${getDisplayNameFromArticulo(item.idArticulo)} x ${item.numeroUnidades} <strong>${item.costeLinea.toFixed(2)} €</strong></p>
    `).join("");

    total.textContent = `${carrito.totalPrecio.toFixed(2)} €`;
  } catch (error) {
    console.error("Error cargando resumen checkout:", error);
    summary.innerHTML = "<p>No hay artículos en el carrito.</p>";
    total.textContent = "0 €";
  }
}

function setupCheckoutForm() {
  const form = document.getElementById("checkoutForm");
  const successMessage = document.getElementById("checkoutSuccess");
  if (!form || !successMessage) return;

  form.addEventListener("submit", async event => {
    event.preventDefault();

    const nombreCliente = document.getElementById("name").value;
    const correoCliente = document.getElementById("email").value;

    try {
      const response = await fetch(`${API_BASE}/pedidos/${CART_ID}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          nombreCliente: nombreCliente,
          correoCliente: correoCliente
        })
      });

      if (!response.ok) {
        throw new Error(`Error creando pedido. Status: ${response.status}`);
      }

      successMessage.classList.remove("hidden");
      form.reset();

      await updateNavCartCount();
      await renderCart();
      await renderCheckoutSummary();

    } catch (error) {
      console.error("Error al confirmar pedido:", error);
      alert("No se pudo confirmar el pedido.");
    }
  });
}

async function loadAdminOrders() {
  const clave = prompt("Introduce la clave de administrador");
  const container = document.getElementById("adminOrders");

  if (!container) return;

  try {
    const response = await fetch(`${API_BASE}/admin/pedidos`, {
      headers: {
        "X-Admin-Key": clave
      }
    });

    if (response.status === 401) {
      container.innerHTML = "<p>No autorizado.</p>";
      return;
    }

    if (!response.ok) {
      throw new Error(`Error cargando pedidos. Status: ${response.status}`);
    }

    const pedidos = await response.json();

    if (pedidos.length === 0) {
      container.innerHTML = "<p>No hay pedidos todavía.</p>";
      return;
    }

    container.innerHTML = pedidos.map(pedido => `
      <article class="gift-card">
        <div class="gift-card-content">
          <h3>Pedido #${pedido.id}</h3>
          <p><strong>Cliente:</strong> ${pedido.nombreCliente}</p>
          <p><strong>Correo:</strong> ${pedido.correoCliente}</p>
          <p><strong>Total:</strong> ${pedido.totalPedido.toFixed(2)} €</p>
          <p><strong>Fecha:</strong> ${pedido.fechaPedido}</p>
        </div>
      </article>
    `).join("");

  } catch (error) {
    console.error("Error admin:", error);
    container.innerHTML = "<p>Error cargando pedidos.</p>";
  }
}

function renderTimelineSlide() {
  const image = document.getElementById("timelineImage");
  const year = document.getElementById("timelineYear");

  if (!image || !year) return;

  const item = timelineData[currentTimelineIndex];
  image.src = item.image;
  image.alt = `Foto del año ${item.year}`;
  year.textContent = item.year;
}

function setupTimelineSlider() {
  const prevButton = document.getElementById("prevSlide");
  const nextButton = document.getElementById("nextSlide");

  if (!prevButton || !nextButton) return;

  prevButton.addEventListener("click", function () {
    currentTimelineIndex--;
    if (currentTimelineIndex < 0) {
      currentTimelineIndex = timelineData.length - 1;
    }
    renderTimelineSlide();
  });

  nextButton.addEventListener("click", function () {
    currentTimelineIndex++;
    if (currentTimelineIndex >= timelineData.length) {
      currentTimelineIndex = 0;
    }
    renderTimelineSlide();
  });

  renderTimelineSlide();
}

function startCountdown() {
  const days = document.getElementById("days");
  const hours = document.getElementById("hours");
  const minutes = document.getElementById("minutes");
  const seconds = document.getElementById("seconds");

  if (!days || !hours || !minutes || !seconds) return;

  function updateCountdown() {
    const now = new Date();
    const difference = weddingDate - now;

    if (difference <= 0) {
      days.textContent = "0";
      hours.textContent = "0";
      minutes.textContent = "0";
      seconds.textContent = "0";
      return;
    }

    days.textContent = Math.floor(difference / (1000 * 60 * 60 * 24));
    hours.textContent = Math.floor((difference / (1000 * 60 * 60)) % 24);
    minutes.textContent = Math.floor((difference / (1000 * 60)) % 60);
    seconds.textContent = Math.floor((difference / 1000) % 60);
  }

  updateCountdown();
  setInterval(updateCountdown, 1000);
}

document.addEventListener("DOMContentLoaded", async () => {
  await ensureCartExists();

  renderGiftList();
  setupHoneymoonForm();
  setupTimelineSlider();
  startCountdown();
  setupCheckoutForm();

  await updateNavCartCount();
  await renderCart();
  await renderCheckoutSummary();

  const clearCartButton = document.getElementById("clearCartButton");
  if (clearCartButton) {
    clearCartButton.addEventListener("click", clearCart);
  }
});