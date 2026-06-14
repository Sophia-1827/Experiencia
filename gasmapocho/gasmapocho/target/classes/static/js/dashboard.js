document.addEventListener("DOMContentLoaded", () => {

    // Forzar altura de contenedores
    document.querySelectorAll('.chart-container').forEach(el => {
        el.style.height = '400px';
    });

    // ==========================
    // GRÁFICO DE BARRAS
    // ==========================

    const ctx = document.getElementById("graficoStock");
    let chartStock = null;

    if (ctx) {

        chartStock = new Chart(ctx, {
            type: "bar",

            data: {
                labels: ["5 Kg", "11 Kg", "15 Kg"],

                datasets: [
                    {
                        label: "Llenos",
                        data: stockLlenos,
                        backgroundColor: "#198754"
                    },
                    {
                        label: "Vacíos",
                        data: stockVacios,
                        backgroundColor: "#dc3545"
                    },
                    {
                        label: "Vendidos",
                        data: stockVendidos,
                        backgroundColor: "#0d6efd"
                    },
                    {
                        label: "Viejos",
                        data: stockViejos,
                        backgroundColor: "#6c757d"
                    }
                ]
            },

            plugins: [ChartDataLabels],

            options: {

                responsive: true,

                maintainAspectRatio: false,

                scales: {
                    y: {
                        beginAtZero: true
                    }
                },

                plugins: {

                    legend: {
                        position: "top"
                    },

                    datalabels: {
                        anchor: "end",
                        align: "top",

                        color: "#000",

                        font: {
                            weight: "bold",
                            size: 12
                        },

                        formatter: function(value) {
                            return value;
                        }
                    }
                }
            }
        });

    }

    // ==========================
    // GRÁFICO CIRCULAR
    // ==========================

    const pie = document.getElementById("graficoEstado");

    if (pie) {

        new Chart(pie, {

            type: "doughnut",

            data: {

                labels: [
                    "Llenos",
                    "Vacíos",
                    "Vendidos",
                    "Viejos"
                ],

                datasets: [{
                    data: [
                        totalLlenos,
                        totalVacios,
                        totalVendidos,
                        totalViejos
                    ],

                    backgroundColor: [
                        "#198754",
                        "#dc3545",
                        "#0d6efd",
                        "#6c757d"
                    ],

                    borderWidth: 2
                }]
            },

            plugins: [ChartDataLabels],

            options: {

                responsive: true,

                maintainAspectRatio: false,

                plugins: {

                    legend: {
                        position: "top"
                    },

                    datalabels: {

                        color: "#ffffff",

                        font: {
                            weight: "bold",
                            size: 14
                        },

                        formatter: (value, ctx) => {

                            const total =
                                ctx.chart.data.datasets[0].data
                                    .reduce((a, b) => a + b, 0);

                            const porcentaje =
                                ((value / total) * 100).toFixed(1);

                            return porcentaje + "%";
                        }
                    }
                }
            }
        });

    }

    // Forzar redimensión después de renderizar
    setTimeout(() => {
        if (chartStock) chartStock.resize();
    }, 100);

});