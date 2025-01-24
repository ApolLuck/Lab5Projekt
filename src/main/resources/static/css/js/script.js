function showPizzaDetails(pizzaId) {
    // Pobierz szczegóły pizzy z serwera lub zmień widoczność modalu
    document.getElementById("pizza-modal").style.display = "block";
}

function closeModal() {
    document.getElementById("pizza-modal").style.display = "none";
}

function increaseQuantity() {
    let quantityElement = document.getElementById("quantity");
    let quantity = parseInt(quantityElement.innerText);
    quantityElement.innerText = quantity + 1;
}

function decreaseQuantity() {
    let quantityElement = document.getElementById("quantity");
    let quantity = parseInt(quantityElement.innerText);
    if (quantity > 1) {
        quantityElement.innerText = quantity - 1;
    }
}

function addToCart() {
    alert("Dodano do koszyka!");
    closeModal();
    // Aktualizuj ilość w koszyku
}

// Przykład kodu do zmiany wartości licznika
function updateCartCount(count) {
    const cartCountElement = document.querySelector('.cart-count');
    if (cartCountElement) {
        cartCountElement.textContent = count;
    }
}

// Domyślna wartość koszyka to 0
updateCartCount(0);

// Test - zwiększenie liczby w koszyku
setTimeout(() => {
    updateCartCount(3); // Przykład - ustaw licznik na 3
}, 3000);

