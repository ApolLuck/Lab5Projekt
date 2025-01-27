document.addEventListener('DOMContentLoaded', function () {
    updateTotalOrderValue();
});

function increaseItemQuantity(itemId) {
    const quantityInput = document.getElementById(`quantity-${itemId}`);
    if (quantityInput) {
        const price = parseFloat(quantityInput.closest('.order-item').getAttribute('data-price'));
        let quantity = parseInt(quantityInput.value, 10);

        quantity = isNaN(quantity) ? 0 : quantity + 1;
        quantityInput.value = quantity.toString();

        updateOrderItemValue(itemId, quantity, price);
    }
}

function decreaseItemQuantity(itemId) {
    const quantityInput = document.getElementById(`quantity-${itemId}`);
    if (quantityInput) {
        const price = parseFloat(quantityInput.closest('.order-item').getAttribute('data-price'));
        let quantity = parseInt(quantityInput.value, 10);

        quantity = isNaN(quantity) ? 1 : Math.max(1, quantity - 1);
        quantityInput.value = quantity.toString();

        updateOrderItemValue(itemId, quantity, price);
    }
}

function updateOrderItemValue(itemId, quantityOrPrice, price = null) {
    let orderItemValue;

    if (price !== null) {
        // Jeśli podano "quantity" i "price", oblicz wartość zamówienia
        orderItemValue = quantityOrPrice * price; // quantityOrPrice to ilość
    } else {
        // Jeśli podano tylko "price", ustaw wartość bezpośrednio
        orderItemValue = parseFloat(quantityOrPrice); // quantityOrPrice to cena
    }

    const orderItemElement = document.getElementById(`order-item-value-${itemId}`);
    if (orderItemElement) {
        orderItemElement.textContent = `${orderItemValue.toFixed(2)} zł`;
    }

    updateTotalOrderValue(); // Zawsze aktualizujemy sumę zamówienia
}


function updateTotalOrderValue() {
    const orderItems = document.querySelectorAll('.order-item');
    let totalValue = 0;

    orderItems.forEach(function (orderItem) {
        const quantityInput = orderItem.querySelector('input[type="number"]');
        const quantity = parseInt(quantityInput.value, 10);
        const price = parseFloat(orderItem.getAttribute('data-price'));

        totalValue += quantity * price;
    });

    document.getElementById('total-order-value').textContent = totalValue.toFixed(2);
}

let discountApplied = false; // Flaga globalna dla kodu rabatowego

function applyDiscount() {
    if (discountApplied) {
        alert("Kod rabatowy został już użyty.");
        return;
    }

    const discountCode = document.getElementById('discount-code').value.trim();
    const promotion = promotions.find(promo => promo.code === discountCode.toUpperCase());
    if (promotion) {
        const now = new Date();
        if (now >= promotion.validFrom && now <= promotion.validTo) {
            const orderItems = document.querySelectorAll('.order-item');
            orderItems.forEach(function (orderItem) {
                const pizzaId = parseInt(orderItem.getAttribute('data-pizzaid'));
                if (promotion.pizzaIds.includes(pizzaId)) {
                    const originalPrice = parseFloat(orderItem.getAttribute('data-original-price')) || parseFloat(orderItem.getAttribute('data-price'));
                    const discountedPrice = originalPrice * (1 - (promotion.discountPercentage / 100));
                    orderItem.setAttribute('data-price', discountedPrice.toFixed(2));
                    orderItem.setAttribute('data-original-price', originalPrice.toFixed(2)); // Zapamiętaj pierwotną cenę

                    // Zaktualizuj widoczną cenę pizzy
                    const priceElement = orderItem.querySelector('h4');
                    if (priceElement) {
                        priceElement.textContent = `${priceElement.textContent.split('-')[0].trim()} - Cena: ${discountedPrice.toFixed(2)} zł`;
                    }

                    const quantity = parseInt(orderItem.querySelector('input[type="number"]').value, 10);
                    updateOrderItemValue(pizzaId, quantity, discountedPrice); // Zaktualizuj wartość zamówienia
                }
            });

            updateTotalOrderValue(); // Zaktualizuj całą sumę
            discountApplied = true; // Ustaw flagę
            alert("Kod rabatowy zastosowany.");
        } else {
            alert("Kod rabatowy nie jest aktualnie ważny.");
        }
    } else {
        alert("Nieprawidłowy kod rabatowy.");
    }
}

const promotions = [
    {
        code: "HAWAI",
        discountPercentage: 30,
        validFrom: new Date('2025-01-01'),
        validTo: new Date('2025-02-28'),
        pizzaIds: [3] // ID pizzy hawajskiej
    }
];
