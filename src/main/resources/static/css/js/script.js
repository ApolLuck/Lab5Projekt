// Tablica przechowująca zamówione elementy
let cart = [];

alert("Skrypt został poprawnie załadowany!");

// Funkcja do wyświetlania modalu z detalami pizzy
function showPizzaDetails(pizzaId) {
    const pizzaNameElement = document.querySelector('#pizza-modal h2');
    const pizzaIngredientsElement = document.querySelector('#pizza-modal p');

    // Ustaw dane w modalu (dynamicznie lub pobrane z serwera)
    pizzaNameElement.textContent = `Pizza ID: ${pizzaId}`;
    pizzaIngredientsElement.textContent = `Składniki pizzy ID ${pizzaId}`;

    // Pokaż modal
    document.getElementById("pizza-modal").style.display = "block";
}

// Funkcja do zamykania modalu z detalami pizzy
function closeModal() {
    document.getElementById("pizza-modal").style.display = "none";
}

// Funkcja zwiększania ilości pizzy
function increaseQuantity() {
    let quantityElement = document.getElementById("quantity");
    let quantity = parseInt(quantityElement.innerText);
    quantityElement.innerText = quantity + 1;
}

// Funkcja zmniejszania ilości pizzy
function decreaseQuantity() {
    let quantityElement = document.getElementById("quantity");
    let quantity = parseInt(quantityElement.innerText);
    if (quantity > 1) {
        quantityElement.innerText = quantity - 1;
    }
}

// Funkcja dodawania pizzy do koszyka
function addToCart() {
    const pizzaId = document.querySelector('#pizza-modal h2').textContent.split(' ')[2];
    const pizzaQuantity = parseInt(document.getElementById("quantity").innerText);

    // Dodaj szczegóły pizzy do koszyka
    cart.push({
        id: pizzaId,
        quantity: pizzaQuantity,
    });

    // Pokaż powiadomienie
    alert(`Dodano pizzę ID ${pizzaId} do koszyka w ilości: ${pizzaQuantity}`);
    closeModal();

    // Aktualizuj licznik koszyka
    updateCartCount(cart.length);
}

// Funkcja aktualizacji licznika koszyka
function updateCartCount(count) {
    const cartCountElement = document.querySelector('.cart-count');
    if (cartCountElement) {
        cartCountElement.textContent = count;
    }
}

// Funkcja otwierania modalu koszyka
function openCart() {
    const cartList = document.getElementById("cart-list");
    cartList.innerHTML = ""; // Wyczyść poprzednią zawartość

    // Dodaj elementy koszyka do listy
    cart.forEach((item) => {
        const listItem = document.createElement("li");
        listItem.textContent = `Pizza ID: ${item.id}, Ilość: ${item.quantity}`;
        cartList.appendChild(listItem);
    });

    // Pokaż modal koszyka
    document.getElementById("cart-modal").style.display = "block";
}

// Funkcja zamykania modalu koszyka
function closeCart() {
    document.getElementById("cart-modal").style.display = "none";
}

// Funkcja kontynuowania zakupów
function continueShopping() {
    closeCart();
}

// Domyślnie ustaw licznik koszyka na 0
updateCartCount(0);

// Obsługa kliknięcia na ikonę koszyka
document.querySelector(".cart-icon").addEventListener("click", openCart);
