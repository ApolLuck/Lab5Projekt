document.addEventListener('DOMContentLoaded', function () {
    updateTotalOrderValue();
});

function increaseItemQuantity(itemId) {
    const quantityInput = document.getElementById(`quantity-${itemId}`);
    const price = parseFloat(quantityInput.closest('.order-item').getAttribute('data-price'));
    let quantity = parseInt(quantityInput.value, 10);

    quantity = isNaN(quantity) ? 0 : quantity + 1;
    quantityInput.value = quantity.toString();  // Konwersja do string

    updateOrderItemValue(itemId, quantity, price);
}

function decreaseItemQuantity(itemId) {
    const quantityInput = document.getElementById(`quantity-${itemId}`);
    const price = parseFloat(quantityInput.closest('.order-item').getAttribute('data-price'));
    let quantity = parseInt(quantityInput.value, 10);

    quantity = isNaN(quantity) ? 1 : Math.max(1, quantity - 1);  // Minimalna ilość to 1
    quantityInput.value = quantity.toString();  // Konwersja do string

    updateOrderItemValue(itemId, quantity, price);
}

function updateOrderItemValue(itemId, quantity, price) {
    const orderItemValue = quantity * price;
    document.getElementById(`order-item-value-${itemId}`).textContent =
        `Wartość zamówienia: ${orderItemValue.toFixed(2)} zł`;
    updateTotalOrderValue();
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
