function showShareDetails(data) {
    sessionStorage.setItem("share", data);
    window.location = "/app/trade";
}

function changeBuySellText(type) {
    if (type === "buy") {
        $('#tradeStockTitle').html("Buy Stock");
        $('#buyStock').html("Buy stock");
    } else {
        $('#tradeStockTitle').html("Sell Stock");
        $('#buyStock').html("Sell stock");
    }
}

function applyModalEvents() {
    $('#buyQuantity').on('change', () => {
        const price = parseFloat(currTable.find('td:eq(3)').text());
        const quantity = parseInt(currModal.find('#buyQuantity').val());
        currModal.find('#buyTotalCost').val(quantity * price);
    });

    $('#buyStockLoader').hide();

    $('#buyStock').on('click', () => {
        $('#buyStockLoader').show();
        const symbol = currTable.find('td:eq(1)').text();
        const price = parseFloat(currTable.find('td:eq(3)').text());
        const quantity = parseInt(currModal.find('#buyQuantity').val());

        XHR.post(`/trade/share/${currTradeType}`, {
            "symbol": symbol,
            "price": price,
            "quantity": quantity
        }, (data) => {
            $('#buyStockLoader').hide();
            if (data.hasError) {
                alert(data.errorMessage);
            } else {
                window.location = "/app/stocks";
            }
        });
    });

    $('#buyStockModal').on('show.bs.modal', function (event) {
        const button = $(event.relatedTarget);
        const tradeType = button.data('trade');
        changeBuySellText(tradeType);

        const table = button.parent().parent();
        const symbol = table.find('td:eq(1)').text();
        const price = parseFloat(table.find('td:eq(3)').text());


        const modal = $(this);
        modal.find('#buySymbol').val(symbol);
        modal.find('#buyPrice').val(price);
        modal.find('#buyQuantity').val(1);
        modal.find('#buyTotalCost').val(price);

        // Hacks my friend
        currTable = table;
        currModal = modal;
        currTradeType = tradeType;
    })
}


$(document).ready(function () {

    (function makeLinkActive() {
        let path = window.location.pathname;

        $('.nav-item.active').removeClass('active');
        $('#accordionSidebar .nav-link').each(function () {
            const el = $(this);

            if (el.attr('href') === path) {
                el.parent().addClass('active');
            }
        });
    })();

    $('#logout').on('click', function () {
        sessionStorage.setItem('token', null);
        window.location = "/app/login";
    });


    if ($('#dataTable').length > 0) {
        $('#dataTable').DataTable({
            "processing": true,
            "paging": true,
            "ordering": true,
            "info": true,
            "searching": true,
            "ajax": {
                "url": "/trade/all",
                "dataSrc": "",
                "headers": {
                    "Authorization": "Bearer " + XHR.token
                }
            },
            "columns": [
                {"data": "name"},
                {
                    "data": "symbol", "render": function (data, type, row, meta) {
                        if (type === 'display') {
                            data = '<a href=# onclick="return showShareDetails(\'' + data + '\')">' + data + '<\/a>';

                        }

                        return data;
                    }
                },
                {"data": "mcap"},
                {"data": "price"},
                {"data": "open"},
                {"data": "prevClose"},
                {"data": "vol"},
                {"data": "eps"},
                {"data": "pe"},
                {"data": "bookVal"},
                {
                    "data": "buy", render: function (data, type, row, meta) {
                        if (type === "display") {
                            data = "<button class='btn btn-primary' data-trade='buy' data-toggle='modal' data-target='#buyStockModal'>Buy</button>";
                        }

                        return data;
                    }
                }
            ]
        });

        applyModalEvents();
    }

    if ($('#myStocksTable').length > 0) {
        $('#myStocksTable').DataTable({
            "processing": true,
            "paging": true,
            "ordering": true,
            "info": true,
            "searching": true,
            "ajax": {
                "url": "/trade/my",
                "dataSrc": "",
                "headers": {
                    "Authorization": "Bearer " + XHR.token
                }
            },
            "columns": [
                {"data": "name"},
                {
                    "data": "symbol", "render": function (data, type, row, meta) {
                        if (type === 'display') {
                            data = '<a href=# onclick="return showShareDetails(\'' + data + '\')">' + data + '<\/a>';

                        }
                        return data;
                    }
                },
                {"data": "currPrice"},
                {"data": "open"},
                {"data": "myAvgBuyPrice"},
                {"data": "myAvgSellPrice"},
                {"data": "quantity"},
                {
                    "data": "trade", render: function (data, type, row, meta) {
                        if (type === "display") {
                            data = "<button class='btn btn-primary' data-toggle='modal' data-trade='buy' data-target='#buyStockModal'>Buy shares</button> &nbsp;" +
                                "<button class='btn btn-primary' data-toggle='modal' data-trade='sell' data-target='#buyStockModal'>Sell shares</button>";
                        }

                        return data;
                    }
                }
            ]
        });
        applyModalEvents();
    }

    if ($('#historyTable').length > 0) {
        $('#historyTable').DataTable({
            "processing": true,
            "paging": true,
            "ordering": true,
            "info": true,
            "searching": true,
            "ajax": {
                "url": "/trade/history",
                "dataSrc": "",
                "headers": {
                    "Authorization": "Bearer " + XHR.token
                }
            },
            "columns": [
                {"data": "name"},
                {
                    "data": "symbol", "render": function (data, type, row, meta) {
                        if (type === 'display') {
                            data = '<a href=# onclick="return showShareDetails(\'' + data + '\')">' + data + '<\/a>';

                        }
                        return data;
                    }
                },
                {"data": "type"},
                {"data": "price"},
                {"data": "quantity"},
                {"data": "tradedOn"}
            ]
        });
    }

    if ($('#openCloseChart').length > 0) {
        console.log("shares html share data " + sessionStorage.getItem("share"));
        XHR.get("/trade/sharedetails?user=test&name=" + sessionStorage.getItem("share"), {}, function (data) {
            console.log(data)
            stockChart(data.date, data.open, data.close, "Open", "Close", "openCloseChart")
            stockChart(data.date, data.high, data.low, "High", "Low", "highLowChart")
            drawVolumeChart(data.date, data.volume, "volumeChart")
            $("#shareName").html(data.name)
        })
    }
});
