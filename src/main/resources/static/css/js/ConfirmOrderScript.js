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


let index = 1
function resetIndex(){
    index = 1
}


