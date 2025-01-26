calculateOrderValue()
function increaseValue() {
    var value = parseInt(document.getElementById('number').value, 10);
    value = isNaN(value) ? 0 : value;
    value++;
    document.getElementById('number').value = value;
}

function decreaseValue() {
    var value = parseInt(document.getElementById('number').value, 10);
    value = isNaN(value) ? 1 : value;
    value = Math.max(1, value - 1); // wartość nie spadnie poniżej 1
    document.getElementById('number').value = value;
}

// aby użytkownik nie mógł ręcznie wpisać liczby mniejszej niż 1
document.getElementById('number').addEventListener('input', function () {
    var value = parseInt(this.value, 10);
    if (isNaN(value) || value < 1) {
        this.value = 1;
    }
});

function updateHiddenOrderValue() {
    // Pobierz wartość zamówienia z <span>
    const orderValueElement = document.getElementById("order-value");
    const hiddenOrderValueInput = document.getElementById("hidden-order-value");

    // Usuń tekst "Wartość zamówienia: " i " zł", aby pobrać tylko liczbę
    const rawValue = orderValueElement.textContent.replace("Wartość zamówienia: ", "").replace(" zł", "").trim();

    // Ustaw wartość ukrytego pola
    hiddenOrderValueInput.value = parseFloat(rawValue) || 0;
}

function calculateOrderValue() {
    var basePrice = parseFloat(document.getElementById('base-price').textContent);
    var orderValue = basePrice;

    var sizeFamilijna = document.getElementById('size-familijna').checked;
    var sizeDuza = document.getElementById('size-duza').checked;
    var sizeMala = document.getElementById('size-mala').checked;
    var grubeCiasto = document.querySelector('input[name="grube_ciasto"]:checked').value === "true";
    var dodatkowySer = document.querySelector('input[name="dodatkowy_ser"]:checked').value === "true";
    var coverType = document.querySelector('input[name="coverType"]:checked');

    if (grubeCiasto) {
        orderValue += 5;
    }

    if (dodatkowySer) {
        orderValue += 5;
    }

    if (coverType && coverType.value !== "1") {
        orderValue += 5;
    }

    if (sizeFamilijna) {
        orderValue *= 2;
    } else if (sizeDuza) {
        orderValue *= 1.5;
    }

    orderValue *= parseInt(document.getElementById('number').value, 10);

    document.getElementById('order-value').textContent = 'Wartość zamówienia: '
        + orderValue.toFixed(2) + ' zł';

    updateHiddenOrderValue()

}