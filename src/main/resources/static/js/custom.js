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

function getGoalsData() {
	 $('#goalsLoader').hide()
    $('#createGoal').on('click', () => {
    	$('#goalsLoader').show();
    	var goalName= $("#nameGoal").get(0).value
    	var startDate= $("#startDate").get(0).value
    	var endDate= $("#endDate").get(0).value
    	var targetAmount= $("#targetAmount").get(0).value
    	var perAmount=$("#percent").get(0).value
    	
        XHR.post(`/app/goal`, {
            "name": goalName,
            "startDate": startDate,
            "endDate": endDate,
            "targetAmount": targetAmount,
            "percent": perAmount
        }, () => {
            window.location = "/app/goals";
        });
    });
}
function showGoals(){
    XHR.get(`/app/goal`, {}, function (data) {
        console.log(data)
        var counter=true;
        $.each(data,function(k,v){
        	console.log(v)
        	if (counter){
        	$("#goalCol1").append(
        			'<div class="card mb-4">'+
                    '<div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">'+
                      '<h6 class="m-0 font-weight-bold text-primary">'+ v.name+'</h6>'+
                      '<div class="dropdown no-arrow">'+
                      '<a class="dropdown-toggle" href="#" role="button" id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">'+
                        '<i class="fas fa-ellipsis-v fa-sm fa-fw text-gray-400"></i>'+
                      '</a>'+
                      '<div class="dropdown-menu dropdown-menu-right shadow animated--fade-in" aria-labelledby="dropdownMenuLink">'+
                        '<div class="dropdown-header">Action:</div>'+
                        '<a class="dropdown-item" href="#" onclick=deleteGoal('+v.id+')>Delete</a>'+
                      '</div>'+
                    '</div>'+
                    '</div>'+
                    '<div class="card-body">'+
                      '<div class="chart-area">'+
                        '<canvas id="goal'+k+'"></canvas>'+
                      '</div>'+
                    '</div>'+
                  '</div>'+
                  '<div class="progress">'+
        			  '<div class="progress-bar" role="progressbar" style="width: '+v.percent+'%;" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100">'+v.percent+'%</div>'+
        			'</div>'
        	)
        	counter=false;
        	}else{
        	   	$("#goalCol2").append(
            			'<div class="card mb-4">'+
                        '<div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">'+
                          '<h6 class="m-0 font-weight-bold text-primary">'+ v.name+'</h6>'+
                          '<div class="dropdown no-arrow">'+
                          '<a class="dropdown-toggle" href="#" role="button" id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">'+
                            '<i class="fas fa-ellipsis-v fa-sm fa-fw text-gray-400"></i>'+
                          '</a>'+
                          '<div class="dropdown-menu dropdown-menu-right shadow animated--fade-in" aria-labelledby="dropdownMenuLink">'+
                          '<div class="dropdown-header">Action:</div>'+
                          '<a class="dropdown-item" href="#" onclick=deleteGoal('+v.id+')>Delete</a>'+
                          '</div>'+
                        '</div>'+
                        '</div>'+
                        '<div class="card-body">'+
                          '<div class="chart-area">'+
                            '<canvas id="goal'+k+'"></canvas>'+
                          '</div>'+
                        '</div>'+
                      '</div>'+
                      '<div class="progress">'+
            			  '<div class="progress-bar" role="progressbar" style="width: '+v.percent+'%;" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100">'+v.percent+'%</div>'+
            			'</div>'
            	)
        	counter=true;
        	}
        	createPieChart("goal"+k,v.amounts);
        });
    });
}
function deleteGoal(goalId){
	  XHR.post(`/app/goal/`, {
		  "goalId":goalId
	  }, function(data){
		  window.location = "/app/goals";
	  });
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
    if($('#goal').length > 0){
    	console.log("Inside goals")
    	showGoals();
    	getGoalsData();
    	
    }
});
