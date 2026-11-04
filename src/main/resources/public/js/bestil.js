const container = document.getElementById("bestilContainer");
const addBtn = document.getElementById("addRow");

// Opdaterer "Cupcake 1, 2, 3 ..." og skjuler Fjern, hvis der kun er én række
function opdater() {
    const rows = container.querySelectorAll(".bestil");

    rows.forEach((row, i) => {
        row.querySelector(".nummer").textContent = i + 1;
    });

    const kunEn = rows.length === 1;
    container.querySelectorAll(".fjern").forEach((knap) => {
        knap.style.display = kunEn ? "none" : "";
    });
}

// + Tilføj en række mere
addBtn.addEventListener("click", () => {
    const forste = container.querySelector(".bestil");
    const ny = forste.cloneNode(true);

    // nulstil valgene i den nye række
    ny.querySelectorAll("select").forEach((s) => (s.selectedIndex = 0));

    container.appendChild(ny);
    opdater();
});

// Fjern (event delegation, virker også på nye rækker)
container.addEventListener("click", (e) => {
    const knap = e.target.closest(".fjern");
    if (!knap) return;

    const rows = container.querySelectorAll(".bestil");
    if (rows.length > 1) {
        knap.closest(".bestil").remove();
        opdater();
    }
});

opdater();