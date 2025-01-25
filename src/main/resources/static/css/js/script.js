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
